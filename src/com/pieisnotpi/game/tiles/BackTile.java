package com.pieisnotpi.game.tiles;

import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.scenes.GameScene;
import com.pieisnotpi.game.shaders.tile_shader.TileQuad;

public class BackTile
{
    public TileQuad quad;
    public float x, y, size;

    public BackTile(float x, float y, float size, Color color)
    {
        this.x = x;
        this.y = y;
        this.size = size;

        quad = new TileQuad(x, y, -0.4f, size, size, 0, GameScene.sprite, color);
    }
}
