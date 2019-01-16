package com.pieisnotpi.game;

import com.pieisnotpi.engine.GameInstance;
import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.image.WritableImage;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorShader;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexShader;
import com.pieisnotpi.engine.rendering.shaders.types.text.TextShader;
import com.pieisnotpi.engine.rendering.window.GLInstance;
import com.pieisnotpi.engine.rendering.window.HintInitializer;
import com.pieisnotpi.engine.rendering.window.ShaderInitializer;
import com.pieisnotpi.engine.rendering.window.Window;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.scenes.GameScene;
import com.pieisnotpi.game.tiles.GameTile;

import java.io.*;

import static com.pieisnotpi.engine.utility.MathUtility.intToByte;

class MainInstance extends GameInstance
{
    static int dw = 4, dh = 4;
    static boolean vsync = true;
    static boolean aa = true;
    private GameScene s;
    private File f = new File("options/save.gam");

    public void init() throws Exception
    {
        Window w0;
        
        Window.shaderInitializer = new ShaderInitializer()
        {
            @Override
            public void init(GLInstance inst)
            {
                inst.registerShaderProgram(ColorShader.ID, new ColorShader(inst.window).init());
                inst.registerShaderProgram(TexShader.ID, new TexShader(inst.window).init());
                inst.registerShaderProgram(TextShader.ID, new TextShader(inst.window).init());
            }
        };
        
        Window.hintInitializer = new HintInitializer()
        {
            @Override
            public void init()
            {
                defaultHints();
                if(aa) hint(SAMPLES, 4);
                hint(VISIBLE, FALSE);
                hint(RESIZABLE, TRUE);
                hint(AUTO_ICONIFY, FALSE);
            }
        };

        windows.add(w0 = new Window("2048", 600, 600, PiEngine.getMonitor(0)).init());
        if(vsync) w0.setVsync(1);

        s = new GameScene(dw, dh);
    
        if(f.exists())
        {
            try(DataInputStream input = new DataInputStream(new FileInputStream(f)))
            {
                s.score = input.readInt();
                s.w = s.nw = input.readInt();
                s.h = s.nh = input.readInt();
                s.init();
                
                s.scoreText.setText("Score: " + s.score);
            
                s.clearBoard();
                for(int y = 0; y < s.h; y++)
                {
                    for(int x = 0; x < s.w; x++)
                    {
                        int value = input.readInt();
                        if(value != 0 && value != -1) s.setTile(x, y, value);
                    }
                }
            }
            catch(IOException e) {}
        }
        else s.init();
        
        w0.setScene(s);
    
        WritableImage i16 = new WritableImage("/assets/textures/icon_16.png"), i32 = new WritableImage("/assets/textures/icon_32.png");
        Color tileColor = s.colors.getTileColor(2), textColor = s.colors.getTextColor(2);
        
        byte white = intToByte(255), black = intToByte(0);
        
        for(int x = 0; x < 16; x++) for(int y = 0; y < 16; y++)
        {
            if(i16.pixels[x][y][3] == 0) continue;
            byte pixel = i16.pixels[x][y][0];
            
            if(pixel == white) i16.setPixel(x, y, tileColor);
            else if(pixel == black) i16.setPixel(x, y, textColor);
        }
        for(int x = 0; x < 32; x++) for(int y = 0; y < 32; y++)
        {
            if(i32.pixels[x][y][3] == 0) continue;
            byte pixel = i32.pixels[x][y][0];
            
            if(pixel == white) i32.setPixel(x, y, tileColor);
            else if(pixel == black) i32.setPixel(x, y, textColor);
        }
        
        i16.write();
        i16.freePixels();
        i32.write();
        i32.freePixels();
        
        w0.setIcon(i16, i32);
    }

    public void start() throws Exception
    {
        windows.forEach(Window::show);

        super.start();
    }
    
    @Override
    public void onClose()
    {
        super.onClose();
    
        if(s.lost) s.resetGame();
        
        try(DataOutputStream output = new DataOutputStream(new FileOutputStream(f)))
        {
            output.writeInt(s.score);
            output.writeInt(s.w);
            output.writeInt(s.h);
    
            GameTile[][] tiles = s.gameTiles;
            for(int y = 0; y < s.h; y++)
            {
                for(int x = 0; x < s.w; x++)
                {
                    GameTile t = tiles[x][y];
                    if(t != null) output.writeInt(tiles[x][y].getValue());
                    else output.writeInt(0);
                }
            }
        }
        catch(IOException e) {}
    }
}
