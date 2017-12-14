package com.gammarush.engine.utils.json;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JSONLoader {

	public static JSON load(String path) {
		JSON result = new JSON();
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String string = "";
			String line = null;
			while((line = in.readLine()) != null) {
				string += line;
			}
			in.close();
			
			if(string.length() > 0) {
				char first = string.charAt(0);
				if(first == '{') {
					string = string.substring(1, string.length() - 1);
				}
				result = parseObject(string);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Unable to open file '" + path + "'");
		}
		catch (IOException e) {
			System.out.println("Error reading file '" + path + "'");
		}
		return result;
	}
	
	private static JSON parseObject(String string) {
		if(string.length() == 0) return null;
		
		JSON result = new JSON();
		String[] elements = split(string);
		for(int i = 0; i < elements.length; i++) {
			int quoteIndex = elements[i].indexOf('\"');
			int colonIndex = elements[i].indexOf(':');
			String name = elements[i].substring(quoteIndex, colonIndex).replace("\"", "").trim();
			Object value = parseValue(elements[i].substring(colonIndex + 1).trim());
			result.put(name, value);
		}
		return result;
	}
	
	private static ArrayList<Object> parseArray(String string) {
		ArrayList<Object> result = new ArrayList<Object>();
		String[] elements = split(string);
		for(int i = 0; i < elements.length; i++) {
			result.add(parseValue(elements[i]));
		}
		return result;
	}
	
	private static Object parseValue(String string) {
		string = string.trim();
		char first = string.charAt(0);
		if(first == '{') {
			//object
			return parseObject(string.substring(1, string.length() - 1).trim());
		}
		else if(first == '[') {
			//array
			return parseArray(string.substring(1, string.length() - 1).trim());
		}
		else if(first == '\"') {
			//string
			return string.substring(1, string.length() - 1);
		}
		else if(first == 't' || first == 'f') {
			//boolean
			if(string == "true") {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			//number
			if(string.indexOf('.') != -1) {
				//float
				return Float.parseFloat(string);
			}
			else {
				//integer
				return Integer.parseInt(string);
			}
		}
	}
	
	private static String[] split(String string) {
		ArrayList<String> array = new ArrayList<String>();
		int index = 0, layer = 0, i = 0;
		for(i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if(c == '{' || c == '[') layer++;
			if(c == '}' || c == ']') layer--;
			if(c == ',' && layer == 0) {
				//System.out.println(index + " " + i + " " + string.substring(index, i));
				array.add(string.substring(index, i));
				index = i + 1;
			}
		}
		array.add(string.substring(index, i));
			
		String[] result = new String[array.size()];
		for(int j = 0; j < array.size(); j++) {
			result[j] = array.get(j);
		}
		return result;
	}

}
