package com.fit_sx.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class ReadConfig {
	
	public static String read(String key) {
		String fileName = "src/main/resources/config.properties";
		Properties props = new Properties();
        try {
            props.load(new InputStreamReader(ReadConfig.class.getClassLoader().
                    getResourceAsStream(fileName),"UTF-8"));
           
        } catch (IOException e) {
           
        }
        return  props.get(key).toString();
		
	}
}
