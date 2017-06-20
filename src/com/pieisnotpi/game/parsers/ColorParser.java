package com.pieisnotpi.game.parsers;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.utility.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ColorParser
{
    private static final Logger logger = new Logger("COLOR PARSER");
    private static final String ds = "START TILES\ndefault:#191900\n2:#FFFFE6\n4:#FFF0BE\n8:#FFB672\n16:#FF993F\n32:#FF7855\n64:#FF413C\n128:#F9D47C\n256:#F9D47C\n512:#F9D459\n1024:#F9D459\n2048:#F9D459\n" +
                                    "START TEXT\ndefault:#FFFFE6\n2:#323232\n4:#323232\n128:#FFFFFF\n256:#FFFFFF\n" +
                                    "START BACK\nbg:#D9D9CC\nbp:#998C80\nbt:#B3A699";
    
    private Map<String, Color> tileColors = new HashMap<>(), textColors = new HashMap<>(), backColors = new HashMap<>();
    
    public ColorParser(String path)
    {
        File file = new File(path);
        Scanner scanner;
        
        if(!file.exists())
        {
            write(file);
            scanner = new Scanner(ds);
        }
        else try { scanner = new Scanner(file); }
        catch(FileNotFoundException e)
        {
            logger.err("File not found after attempting write to disk, this is bad");
            return;
        }
    
        Map<String, Color> map = null;
        tileColors.put("default", new Color("#191900"));
        textColors.put("default", new Color("#FFFFE6"));
        backColors.put("default", new Color("#D9D9CC"));
    
        while(scanner.hasNextLine())
        {
            String next = scanner.nextLine().toLowerCase();
        
            if(next.startsWith("start "))
            {
                String m = next.substring(6);
                switch(m)
                {
                    case "tiles": map = tileColors; break;
                    case "text": map = textColors; break;
                    case "back": map = backColors; break;
                    default: logger.err("Theme attempted to start non-existent category"); break;
                }
                continue;
            }
        
            if(map == null) continue;
        
            parseInteger(next, map);
        }
    
        scanner.close();
    }
    
    public Color getTileColor(int value)
    {
        Color c = tileColors.get(Integer.toString(value));
        return c == null ? tileColors.get("default") : c;
    }
    
    public Color getTextColor(int value)
    {
        Color c = textColors.get(Integer.toString(value));
        return c == null ? textColors.get("default") : c;
    }
    
    public Color getBackColor(String value)
    {
        Color c = backColors.get(value);
        return c == null ? backColors.get("default") : c;
    }

    private void write(File file)
    {
        try(PrintWriter output = new PrintWriter(file))
        {
            output.println(ds);
        }
        catch(IOException e)
        {
            logger.err("Unable to write theme file to '" + file.getAbsolutePath() + '\'');
        }
    }

    private void parseInteger(String string, Map<String, Color> map)
    {
        if(!string.contains(":") || !string.contains("#")) return;
        String number = string.substring(0, string.indexOf(':')), value = string.substring(string.indexOf('#'));

        map.put(number, new Color(value));
    }
}
