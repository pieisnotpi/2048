package com.pieisnotpi.game;

import com.pieisnotpi.engine.PiEngine;

import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        for (String arg : args)
        {
            if(arg.equals("novsync")) MainInstance.vsync = false;
            else if(arg.equals("noaa")) MainInstance.aa = false;
        }

        System.setProperty("org.lwjgl.librarypath", "natives");

        if(args.length == 2)
        {
            try
            {
                MainInstance.dw = Integer.parseInt(args[0]);
                MainInstance.dh = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException e)
            {
                MainInstance.dw = MainInstance.dh = 4;
            }
        }

        File d = new File("options");
        if(!d.exists() || !d.isDirectory()) d.mkdir();

        PiEngine.start(new MainInstance());
    }
}
