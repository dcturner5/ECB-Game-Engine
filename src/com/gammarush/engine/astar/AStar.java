package com.gammarush.engine.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.world.World;

public class AStar {
	
	public static final int MAX_ITERATIONS = 1000;
	
	private World world;
	
	private List<Node> open = new ArrayList<Node>();
	private List<Node> closed = new ArrayList<Node>();
	
	private Comparator<Node> sorter = new Comparator<Node>() {
		@Override
		public int compare(Node n1, Node n2) {
			if(n1.fCost > n2.fCost) return 1;
			if(n1.fCost < n2.fCost) return -1;
			return 0;
		}
	};
	
	public AStar(World world) {
		this.world = world;
	}
	
	public ArrayList<Vector2i> findPath(Vector2i start, Vector2i goal) {
		return findPath(start, goal, 0);
	}
	
	public ArrayList<Vector2i> findPath(Vector2i start, Vector2i goal, int layer) {
		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();
		
		Node current = new Node(start, null, 0, calculateHCost(start, goal));
		open.add(current);
		
		int i = 0;
		while(open.size() > 0 && i < MAX_ITERATIONS) {
			Collections.sort(open, sorter);
			current = open.get(0);
			
			if(current.tile.equals(goal)) {
				ArrayList<Vector2i> path = new ArrayList<Vector2i>();
				while(current.parent != null) {
					path.add(current.tile);
					current = current.parent;
				}
				open.clear();
				closed.clear();
				Collections.reverse(path);
				return path;
			}
			
			open.remove(current);
			closed.add(current);
			
			ArrayList<Vector2i> neighborPositions = new ArrayList<Vector2i>();
			if(walkable(current.tile.x, current.tile.y - 1, layer)) neighborPositions.add(current.tile.add(0, -1));
			if(walkable(current.tile.x, current.tile.y + 1, layer)) neighborPositions.add(current.tile.add(0, 1));
			if(walkable(current.tile.x - 1, current.tile.y, layer)) neighborPositions.add(current.tile.add(-1, 0));
			if(walkable(current.tile.x + 1, current.tile.y, layer)) neighborPositions.add(current.tile.add(1, 0));
			
			for(Vector2i position : neighborPositions) {
				double gCost = current.gCost + 1;
				double hCost = calculateHCost(position, goal);
				Node node = new Node(position, current, gCost, hCost);
				Node openListNode = nodeInList(open, position);
				Node closedListNode = nodeInList(closed, position);
				
				if(closedListNode != null) {
					continue;
				}
				if(openListNode == null) {
					open.add(node);
				}
				else if(gCost >= openListNode.gCost) {
					continue;
				}
			}
			
			i++;
		}
		
		closed.clear();
		return new ArrayList<Vector2i>();
	}
	
	private boolean walkable(int x, int y, int layer) {
		if(layer > 0) {
			if(x == 4 && y == 1) System.out.println(world.getStructureTop(x, y));
			return world.getStructureTop(x, y) == layer;
		}
		return !(world.checkSolid(x, y) || world.getEntityCollision(x, y));
	}
	
	private Node nodeInList(List<Node> list, Vector2i v) {
		for(Node n : list) {
			if(n.tile.equals(v)) return n;
		}
		return null;
	}
	
	private double calculateHCost(Vector2i v1, Vector2i v2) {
		return Math.abs(v1.x - v2.x) + Math.abs(v1.y - v2.y);
	}
	
}