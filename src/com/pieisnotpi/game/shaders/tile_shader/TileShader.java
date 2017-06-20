package com.pieisnotpi.game.shaders.tile_shader;

import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.window.Window;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TileShader extends ShaderProgram
{
    public static final int id = 16243;

    public TileShader(Window window)
    {
        super(window, ShaderFile.getShaderFile("tile.vert", GL_VERTEX_SHADER), ShaderFile.getShaderFile("tile.frag", GL_FRAGMENT_SHADER));
    }
}
