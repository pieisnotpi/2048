package com.pieisnotpi.game.tiles.animations;

import com.pieisnotpi.game.Constants;
import com.pieisnotpi.game.tiles.GameTile;
import org.joml.Vector2f;

public class MoveAnimation extends TileAnimation
{
    private Vector2f s = new Vector2f(), n = new Vector2f();
    private float speed;

    private static final float MOVE_SPEED = 3.7f;

    public MoveAnimation(GameTile tile)
    {
        super(tile);
    }

    public MoveAnimation start(float sx, float sy, float nx, float ny)
    {
        tile.getTransform().setTranslateAbs(sx, sy, tile.getTransform().pos.z);

        s.set(sx, sy);
        n.set(nx, ny);

        speed = ((nx - sx) + (ny - sy))/Constants.size*MOVE_SPEED;
        started = true;
        finished = false;

        return this;
    }

    public MoveAnimation stop()
    {
        if(started) tile.getTransform().setTranslateAbs(n.x, n.y, tile.getTransform().pos.z);

        started = false;
        finished = true;

        return this;
    }

    public void process(float timeStep)
    {
        if(!started) return;

        float cx = n.x - s.x, cy = n.y - s.y;

        if(cx < 0) cx = Float.max(speed*timeStep, cx);
        else if(cx > 0) cx = Float.min(speed*timeStep, cx);

        if(cy < 0) cy = Float.max(speed*timeStep, cy);
        else if(cy > 0) cy = Float.min(speed*timeStep, cy);

        tile.getTransform().translateAbs(cx, cy, 0);
        s.add(cx, cy);

        if(s.equals(n)) stop();
    }
}
