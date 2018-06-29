package com.gammarush.engine.scripts;

import com.gammarush.axil.AxilLoader;
import com.gammarush.axil.AxilScript;
import com.gammarush.axil.compiler.AxilCompiler;
import com.gammarush.axil.methods.AxilMethodMap;

public class ScriptManager {
	
	private AxilCompiler compiler;
	private AxilMethodMap methods = new AxilMethodMap();
	
	public ScriptManager() {
		compiler = new AxilCompiler(methods);
		
		compiler.compileFile("res/scripts/main.txt");
		
		AxilScript script = AxilLoader.open("res/scripts/main.axil");
		script.run();
	}
	
	public AxilCompiler getCompiler() {
		return compiler;
	}

}
