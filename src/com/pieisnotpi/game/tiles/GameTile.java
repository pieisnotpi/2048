package com.pieisnotpi.game.tiles;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.ui.text.Text;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.scenes.GameScene;
import com.pieisnotpi.game.shaders.tile_shader.TileQuad;
import com.pieisnotpi.game.tiles.animations.MoveAnimation;
import com.pieisnotpi.game.tiles.animations.ScaleAnimation;
import org.joml.Vector3f;

public class GameTile extends GameObject<TileQuad>
{
    private Text text;
    private GameScene scene;
    private TileQuad quad;
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

        createMesh(GameScene.gridMaterial, MeshConfig.QUAD);
        text = new Text(GameScene.tileFont, "", new Vector3f(), new Color(0, 0, 0), new Color(1, 1, 1), Camera.ORTHO2D_S);
        addChild(text);

        text.getTransform().setScale(TEXT_SCALE*size);
        mesh.addRenderable(quad = new TileQuad(0, 0, -0.05f, size, size, 0, GameScene.sprite, new Color(0, 0, 0)));

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
            mesh.getTransform().translateAbs(0, 0, -0.1f);
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

        quad.setQuadColors(tileColor);
        mesh.flagForBuild();
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

        for(int x = tileX + 1; x < scene.w; x++)
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

        for(int y = tileY + 1; y < scene.h; y++)
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
        scene.scoreText.setText("Score: " + (scene.score += existing.value));
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
