package com.gammarush.engine.scripts;

import java.util.ArrayList;

import com.gammarush.axil.AxilLoader;
import com.gammarush.axil.AxilScript;
import com.gammarush.axil.compiler.AxilCompiler;
import com.gammarush.axil.methods.AxilMethodInterface;
import com.gammarush.axil.methods.AxilMethodMap;
import com.gammarush.engine.ui.UIManager;

public class ScriptManager {
	
	private AxilCompiler compiler;
	private AxilMethodMap methods = new AxilMethodMap();
	
	private ScriptHashMap scripts = new ScriptHashMap();
	private ArrayList<String> queue = new ArrayList<String>();
	
	private UIManager uiManager;
	
	public ScriptManager(UIManager uiManager) {
		this.uiManager = uiManager;
		compiler = new AxilCompiler(methods);
	}
	
	public void addMethod(String name, int argsLength, AxilMethodInterface method) {
		methods.put(name, argsLength, method);
	}
	
	public void callMethod(String name, Object... parameters) {
		for(AxilScript script : scripts.getArray()) {
			script.call(name, parameters);
		}
	}
	
	public void compile() {
		for(String path : queue) {
			compiler.compileFile(path + ".txt");
			scripts.put(path + ".axil", AxilLoader.open(path + ".axil", methods));
		}
		queue.clear();
	}
	
	public void load() {
		for(String path : queue) {
			scripts.put(path + ".axil", AxilLoader.open(path + ".axil", methods));
		}
		queue.clear();
	}
	
	public AxilCompiler getCompiler() {
		return compiler;
	}
	
	public ArrayList<String> getQueue() {
		return queue;
	}
	
	public AxilScript getScript(String name) {
		return scripts.get(name);
	}
	
	public UIManager getUIManager() {
		return uiManager;
	}

}
