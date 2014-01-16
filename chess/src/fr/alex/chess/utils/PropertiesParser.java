package fr.alex.chess.utils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Allow parsing of properties files in libgdx
 * @author alex
 *
 */
public class PropertiesParser {
	/**
	 * Properties file read key/value
	 */
	HashMap<String, String> content;
	
	public PropertiesParser(){
		content = new HashMap<String, String>();
	}
	
	/**
	 * Parse the properties file inputstream
	 * Use getProperty method to retrieve value  
	 * @param inputStream
	 * @throws IOException
	 */
	public void parse(String fileContent) throws IOException{
		String[] lines = fileContent.split("\n");
		for(String line : lines){
			//Research of key
			String key = line.substring(0, line.indexOf('='));
			//Research of value
			String value = line.substring(line.indexOf('=') + 1, line.length());
			//Add to content
			content.put(key, value);
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return corresponding propertie if exists or null otherwise 
	 */
	public String getProperty(String key)
	{
		return content.get(key);
	}
}
