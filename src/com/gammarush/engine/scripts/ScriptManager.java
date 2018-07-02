package com.gammarush.engine.scripts;

import java.util.ArrayList;

import com.gammarush.axil.AxilLoader;
import com.gammarush.axil.AxilScript;
import com.gammarush.axil.compiler.AxilCompiler;
import com.gammarush.axil.methods.AxilMethodInterface;
import com.gammarush.axil.methods.AxilMethodMap;
import com.gammarush.engine.gui.UIManager;

public class ScriptManager {
	
	private AxilCompiler compiler;
	private AxilMethodMap methods = new AxilMethodMap();
	
	private ScriptHashMap scripts = new ScriptHashMap();
	private ArrayList<String> compileQueue = new ArrayList<String>();
	private ArrayList<String> loadQueue = new ArrayList<String>();
	
	private UIManager uiManager;
	
	public ScriptManager(UIManager uiManager) {
		this.uiManager = uiManager;
		compiler = new AxilCompiler(methods);
	}
	
	public void addMethod(String name, int argsLength, AxilMethodInterface method) {
		methods.put(name, argsLength, method);
	}
	
	public void compile() {
		for(String path : compileQueue) {
			compiler.compileFile(path + ".txt");
			scripts.put(path + ".axil", AxilLoader.open(path + ".axil", methods));
		}
		compileQueue.clear();
	}
	
	public void load() {
		for(String path : loadQueue) {
			scripts.put(path + ".axil", AxilLoader.open(path + ".axil", methods));
		}
		compileQueue.clear();
	}
	
	public AxilCompiler getCompiler() {
		return compiler;
	}
	
	public ArrayList<String> getCompileQueue() {
		return compileQueue;
	}
	
	public ArrayList<String> getLoadQueue() {
		return loadQueue;
	}
	
	public AxilScript getScript(String name) {
		return scripts.get(name);
	}
	
	public UIManager getUIManager() {
		return uiManager;
	}

}
