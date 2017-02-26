package com.tomas.neuralNetwork;

import java.io.*;
import java.util.*;



public class Conf {
	
	private static HashMap<String, String> map = new HashMap<>();
	
	private Conf(){}
	
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

	public static String s(String key){
		if(map.containsKey(key))
			return map.get(key);
		else {
			System.out.println("Not key " + key + " in conf");
			return "";
		}
	}

	public static int i(String key){
		if(map.containsKey(key))
			return Integer.parseInt(map.get(key));
		else {
			System.out.println("Not key " + key + " in conf");
			return 0;
		}
	}
	
	public static float f(String key){
		if(map.containsKey(key))
			return Float.parseFloat(map.get(key));
		else {
			System.out.println("Not key " + key + " in conf");
			return 0;
		}
	}
	
}
