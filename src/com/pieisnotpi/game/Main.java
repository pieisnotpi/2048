package com.pieisnotpi.game;

import com.pieisnotpi.engine.PiEngine;

import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        if(args.length > 0 && args[0].equals("novsync")) MainInstance.vsync = false;
        System.setProperty("org.lwjgl.librarypath", "natives");

        if(args.length == 2)
        {
            try
            {
                MainInstance.w = Integer.parseInt(args[0]);
                MainInstance.h = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException e)
            {
                MainInstance.w = MainInstance.h = 4;
            }
        }

        File d = new File("options");
        if(!d.exists() || !d.isDirectory()) d.mkdir();

        PiEngine.start(new MainInstance());
    }
}
