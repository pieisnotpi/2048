package com.pieisnotpi.game.shaders.tile_shader;

import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.utility.Color;

public class TileQuad extends TexQuad
{
    public TileQuad(float x, float y, float z, float width, float height, float depth, Sprite sprite, Color color)
    {
        super(x, y, z, width, height, depth, sprite);

        setQuadColors(color);
    }
}
