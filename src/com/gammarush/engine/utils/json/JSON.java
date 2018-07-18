package com.gammarush.engine.utils.json;

import java.util.ArrayList;
import java.util.HashMap;

import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.entities.animations.AnimationHashMap;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

public class JSON extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 494413607983759569L;

	@SuppressWarnings("unchecked")
	public Object getJSON(String string) {
		String[] path = string.split("\\.");
		Object map = this;
		for(int i = 0; i < path.length; i++) {
			String object = path[i];
			int open = object.indexOf('['), close = object.indexOf(']');
			if(open != -1 && close != -1) {
				int index = Integer.parseInt(object.substring(open + 1, close));
				object = object.substring(0, open);
				map = ((ArrayList<Object>) ((JSON) map).get(object)).get(index);
			}
			else {
				map = ((JSON) map).get(object);
			}
		}
		return map;
	}
	
	public AnimationHashMap getAnimationHashMap() {
		AnimationHashMap result = new AnimationHashMap();
		ArrayList<JSON> array = this.getArray();
		for(JSON element : array) {
			result.put(new Animation(element));
		}
		return result;
	}
	
	public AnimationHashMap getAnimationHashMap(String string) {
		AnimationHashMap result = new AnimationHashMap();
		ArrayList<JSON> array = getArray(string);
		for(JSON element : array) {
			result.put(new Animation(element));
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<JSON> getArray() {
		return (ArrayList<JSON>) (Object) this;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<JSON> getArray(String string) {
		return (ArrayList<JSON>) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<JSON>> get2DArray() {
		return (ArrayList<ArrayList<JSON>>) (Object) this;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<JSON>> get2DArray(String string) {
		return (ArrayList<ArrayList<JSON>>) getJSON(string);
	}
	
	public boolean getBoolean() {
		return (boolean) (Object) this;
	}
	
	public boolean getBoolean(String string) {
		Object json = getJSON(string);
		if(json == null) return false;
		return (boolean) json;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Boolean> getBooleanArray() {
		return (ArrayList<Boolean>) (Object) this;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Boolean> getBooleanArray(String string) {
		return (ArrayList<Boolean>) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Boolean>> getBoolean2DArray() {
		return (ArrayList<ArrayList<Boolean>>) (Object) this;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Boolean>> getBoolean2DArray(String string) {
		return (ArrayList<ArrayList<Boolean>>) getJSON(string);
	}
	
	public Vector4f getColor() {
		return new Vector4f(this.getFloat("r"), this.getFloat("g"), this.getFloat("b"), 1f);
	}
	
	public Vector4f getColor(String name) {
		JSON color = (JSON) getJSON(name);
		if(color == null) return null;
		return new Vector4f(color.getFloat("r"), color.getFloat("g"), color.getFloat("b"), 1f);
	}
	
	public float getFloat() {
		Object json = this;
		return json instanceof Float ? (float) json : (int) json * 1.0f;
	}
	
	public float getFloat(String string) {
		Object json = getJSON(string);
		return json instanceof Float ? (float) json : (int) json * 1.0f;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Float> getFloatArray() {
		return (ArrayList<Float>) (Object) this;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Float> getFloatArray(String string) {
		return (ArrayList<Float>) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Float>> getFloat2DArray() {
		return (ArrayList<ArrayList<Float>>) (Object) this;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Float>> getFloat2DArray(String string) {
		return (ArrayList<ArrayList<Float>>) getJSON(string);
	}
	
	public int getInteger() {
		return (int) (Object) this;
	}
	
	public int getInteger(String string) {
		return (int) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> getIntegerArray() {
		return (ArrayList<Integer>) (Object) this;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> getIntegerArray(String string) {
		return (ArrayList<Integer>) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>> getInteger2DArray() {
		return (ArrayList<ArrayList<Integer>>) (Object) this;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>> getInteger2DArray(String string) {
		return (ArrayList<ArrayList<Integer>>) getJSON(string);
	}
	
	public String getString() {
		return (String) (Object) this;
	}
	
	public String getString(String string) {
		return (String) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getStringArray() {
		return (ArrayList<String>) (Object) this;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getStringArray(String string) {
		return (ArrayList<String>) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<String>> getString2DArray() {
		return (ArrayList<ArrayList<String>>) (Object) this;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<String>> getString2DArray(String string) {
		return (ArrayList<ArrayList<String>>) getJSON(string);
	}
	
	public Vector2f getVector2f() {
		return new Vector2f(this.getFloat("x"), this.getFloat("y"));
	}
	
	public Vector2f getVector2f(String name) {
		JSON v = (JSON) getJSON(name);
		if(v == null) return null;
		return new Vector2f(v.getFloat("x"), v.getFloat("y"));
	}
	
	public Vector2i getVector2i() {
		return new Vector2i(this.getInteger("x"), this.getInteger("y"));
	}
	
	public Vector2i getVector2i(String name) {
		JSON v = (JSON) getJSON(name);
		if(v == null) return null;
		return new Vector2i(v.getInteger("x"), v.getInteger("y"));
	}
	
	public Vector3f getVector3f() {
		return new Vector3f(this.getFloat("x"), this.getFloat("y"), this.getFloat("z"));
	}
	
	public Vector3f getVector3f(String name) {
		JSON v = (JSON) getJSON(name);
		if(v == null) return null;
		return new Vector3f(v.getFloat("x"), v.getFloat("y"), v.getFloat("z"));
	}
	
	public Vector4f getVector4f() {
		return new Vector4f(this.getFloat("x"), this.getFloat("y"), this.getFloat("z"), this.getFloat("w"));
	}
	
	public Vector4f getVector4f(String name) {
		JSON v = (JSON) getJSON(name);
		if(v == null) return null;
		return new Vector4f(v.getFloat("x"), v.getFloat("y"), v.getFloat("z"), v.getFloat("w"));
	}
	
}
