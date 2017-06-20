package com.pieisnotpi.game.tiles.animations;

import com.pieisnotpi.game.tiles.GameTile;

public class ScaleAnimation extends TileAnimation
{
    float speed, s, n;

    public static final float SCALE_SPEED = 7;

    public ScaleAnimation(GameTile tile)
    {
        super(tile);
    }

    public ScaleAnimation start(float s, float n)
    {
        tile.getTransform().setScaleCentered(s);

        this.s = s;
        this.n = n;

        speed = SCALE_SPEED*(n-s);

        started = true;
        finished = false;

        return this;
    }

    public ScaleAnimation stop()
    {
        if(started) tile.getTransform().setScaleCentered(n);

        started = false;
        finished = true;

        return this;
    }

    public void process(float timeStep)
    {
        if(!started) return;

        float ns = n - s;

        if(ns < 0) ns = Float.max(speed*timeStep, ns);
        else if(ns > 0) ns = Float.min(speed*timeStep, ns);

        tile.getTransform().setScaleCentered(s + ns);

        s += ns;

        if(s == 0) stop();
    }
}
