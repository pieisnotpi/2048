package com.pieisnotpi.game.tiles.animations;

import com.pieisnotpi.game.tiles.GameTile;

public abstract class TileAnimation
{
    protected GameTile tile;
    protected boolean started, finished;

    public TileAnimation(GameTile tile)
    {
        this.tile = tile;
    }
    
    public boolean isStarted()
    {
        return started;
    }

    public boolean hasFinished()
    {
        if(finished)
        {
            finished = false;
            return true;
        }
        else return false;
    }
}
