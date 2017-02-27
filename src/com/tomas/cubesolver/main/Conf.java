package com.tomas.cubesolver.main;

import java.io.*;
import java.util.*;

/**
 * Class for using configuration file. It is supposed to load conf file first and then call methods asking conf values.
 *
 */
public class Conf {
	
	private static HashMap<String, String> map = new HashMap<>();
	
	private Conf(){}
	
	/**
	 * Loads configuration file
	 * @param filename Configuration file
	 */
	public static void load(String filename){
		try {
			Scanner sc = new Scanner(new File(filename));
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				if(line.startsWith("#")) continue;
				
				String[] arr = line.split(":",2);
				if(arr.length == 2){
					map.put(arr[0].trim(), arr[1].trim());
				}
			}
			sc.close();
		} catch(FileNotFoundException e) {
		}
	}

	/**
	 * Get string value by key. 
	 * @param key
	 * @return The string value of "" if key doesn't exists
	 */
	public static String s(String key){
		if(map.containsKey(key))
			return map.get(key);
		else {
			System.out.println("Not key " + key + " in conf");
			return "";
		}
	}

	/**
	 * Get in value by key.
	 * @param key
	 * @return The int value or 0.
	 */
	public static int i(String key){
		if(map.containsKey(key))
			return Integer.parseInt(map.get(key));
		else {
			System.out.println("Not key " + key + " in conf");
			return 0;
		}
	}
	
	/**
	 * Get float value by key
	 * @param key
	 * @return The float value or 0.
	 */
	public static float f(String key){
		if(map.containsKey(key))
			return Float.parseFloat(map.get(key));
		else {
			System.out.println("Not key " + key + " in conf");
			return 0;
		}
	}
}