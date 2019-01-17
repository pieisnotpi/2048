package com.pieisnotpi.game.tiles;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.ui.text.Text;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.Constants;
import com.pieisnotpi.game.scenes.GameScene;
import com.pieisnotpi.game.tiles.animations.MoveAnimation;
import com.pieisnotpi.game.tiles.animations.ScaleAnimation;
import org.joml.Vector3f;

public class GameTile extends GameObject
{
    private Text text;
    private GameScene scene;
    private TileMesh mesh;
    public boolean merged = false, shouldDestroy = false;

    private MoveAnimation moveAnimation = new MoveAnimation(this);
    private ScaleAnimation scaleAnimation = new ScaleAnimation(this);

    private float size;

    private int tileX, tileY, value;
    private boolean destroyOnFinish = false;
    
    private static final float TEXT_SCALE = 0.009f;

    public GameTile(int tileX, int tileY, int value, float size, GameScene scene)
    {
        this.scene = scene;
        this.size = size;
        this.tileX = tileX;
        this.tileY = tileY;

        mesh = new TileMesh();

        createRenderable(0, 0, mesh);
        text = new Text(Constants.TILE_FONT, "", new Vector3f(), new Color(0, 0, 0), new Color(1, 1, 1), Camera.ORTHO2D_S);
        addChild(text);

        text.getTransform().setScale(TEXT_SCALE*size);

        BackTile tile = scene.backTiles[tileX][tileY];
        transform.translate(tile.x, tile.y, 0).setCenter(size/2, size/2, size/2);

        scaleAnimation.start(0.1f, 1f);

        setValue(value);
    }

    public void moveTo(int tileX, int tileY, boolean destroyOnFinish)
    {
        BackTile before = scene.backTiles[this.tileX][this.tileY], after = scene.backTiles[tileX][tileY];

        if(moveAnimation.isStarted()) moveAnimation.stop();
        if(scaleAnimation.isStarted()) scaleAnimation.stop();

        moveAnimation.start(before.x, before.y, after.x, after.y);

        scene.gameTiles[this.tileX][this.tileY] = null;

        this.tileX = tileX;
        this.tileY = tileY;

        if(!destroyOnFinish) scene.gameTiles[tileX][tileY] = this;
        else
        {
            scene.deletionQue.add(this);
            renderable.getTransform().translate(0, 0, -0.1f);

        }

        this.destroyOnFinish = destroyOnFinish;
    }

    @Override
    public void drawUpdate(float timeStep)
    {
        moveAnimation.process(timeStep);
        scaleAnimation.process(timeStep);

        if(moveAnimation.hasFinished() && destroyOnFinish) flagForDestroy();
    }

    public void setValue(int value)
    {
        this.value = value;

        Color tileColor = scene.getTileColor(value), textColor = scene.getTextColor(value);

        mesh.setTileColor(tileColor);
        text.setTextColor(textColor);
        text.setText(value + "");
        text.getTransform().setTranslateAbs(size/2 - text.getWidth()/2, size/2 - text.getHeight()/2, 0);
    }
    
    public int getValue()
    {
        return value;
    }

    public int scanLeft()
    {
        int maxX = tileX;

        for(int x = tileX - 1; x >= 0; x--)
        {
            GameTile existing = scene.gameTiles[x][tileY];

            if(existing == null) maxX = x;
            else if(existing.value == value && !existing.merged && !merged) { maxX = x; break; }
            else break;
        }

        return maxX;
    }

    public int scanRight()
    {
        int maxX = tileX;

        for(int x = tileX + 1; x < scene.getBoardWidth(); x++)
        {
            GameTile existing = scene.gameTiles[x][tileY];

            if(existing == null) maxX = x;
            else if(existing.value == value && !existing.merged && !merged) { maxX = x; break; }
            else break;
        }

        return maxX;
    }

    public int scanDown()
    {
        int maxY = tileY;

        for(int y = tileY - 1; y >= 0; y--)
        {
            GameTile existing = scene.gameTiles[tileX][y];

            if(existing == null) maxY = y;
            else if(existing.value == value && !existing.merged && !merged) { maxY = y; break; }
            else break;
        }

        return maxY;
    }

    public int scanUp()
    {
        int maxY = tileY;

        for(int y = tileY + 1; y < scene.getBoardHeight(); y++)
        {
            GameTile existing = scene.gameTiles[tileX][y];

            if(existing == null) maxY = y;
            else if(existing.value == value && !existing.merged && !merged) { maxY = y; break; }
            else break;
        }

        return maxY;
    }

    public void mergeWith(GameTile existing)
    {
        existing.merged = true;
        existing.setValue(existing.value*2);
        scene.setScore(scene.getScore() + existing.value);
        moveTo(existing.tileX, existing.tileY, true);
    }

    public void flagForDestroy()
    {
        shouldDestroy = true;
    }

    public void destroy()
    {
        super.destroy();
        shouldDestroy = false;
    }

    @Override
    public String toString()
    {
        return String.format("x:%d,y:%d,v:%d,dof:%b", tileX, tileY, value, destroyOnFinish);
    }
}
