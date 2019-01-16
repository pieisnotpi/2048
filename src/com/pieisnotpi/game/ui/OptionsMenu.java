package com.pieisnotpi.game.ui;

import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorQuad;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.ui.text.Text;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.Constants;
import com.pieisnotpi.game.scenes.GameScene;
import org.joml.Vector3f;

import static com.pieisnotpi.game.Constants.loseFont;
import static com.pieisnotpi.game.Constants.menuZ;

public class OptionsMenu extends GameObject
{
    public int w, h;

    private static final float MOVE_SPEED = 4f;
    private static final float wPos = 0.15f, hPos = -0.4f, valOffset = 0.06f, textOffset = 0.15f, arrowOffset = 0.25f, textScale = 0.002f, valueScale = 0.002f;

    private float yMoveSpeed = 0, yDest;

    public OptionsMenu(int width, int height, Color buttonColor, Color bgColor)
    {
        w = width;
        h = height;

        getTransform().translate(0, 2, 0);

        Text wText = new Text(loseFont, "Width", new Vector3f(0, wPos + textOffset, menuZ), buttonColor, buttonColor, Camera.ORTHO2D_S);
        wText.setHAlignment(UiObject.HAlignment.CENTER, 0);
        wText.getTransform().setScale(textScale);

        Text wValueText = new Text(loseFont, w + "", new Vector3f(0, wPos - valOffset, menuZ), buttonColor, buttonColor, Camera.ORTHO2D_S);
        wValueText.setHAlignment(UiObject.HAlignment.CENTER, 0);
        wValueText.getTransform().setScale(valueScale);

        ArrowButton wLeft = new ArrowButton(-arrowOffset, wPos, buttonColor, false)
        {
            @Override
            public void onLeftClick()
            {
                if(mouseHoverStatus && w > 2)
                {
                    w--;
                    wValueText.setText(w + "");
                }
            }
        };

        ArrowButton wRight = new ArrowButton(arrowOffset, wPos, buttonColor, true)
        {
            @Override
            public void onLeftClick()
            {
                if(mouseHoverStatus && w < 20)
                {
                    w++;
                    wValueText.setText(w + "");
                }
            }
        };

        Text hText = new Text(loseFont, "Height", new Vector3f(0, hPos + textOffset, menuZ), buttonColor, buttonColor, Camera.ORTHO2D_S);
        hText.setHAlignment(UiObject.HAlignment.CENTER, 0);
        hText.getTransform().setScale(textScale);

        Text hValueText = new Text(loseFont, h + "", new Vector3f(0, hPos - valOffset, menuZ), buttonColor, buttonColor, Camera.ORTHO2D_S);
        hValueText.setHAlignment(UiObject.HAlignment.CENTER, 0);
        hValueText.getTransform().setScale(valueScale);

        ArrowButton hLeft = new ArrowButton(-arrowOffset, hPos, buttonColor, false)
        {
            @Override
            public void onLeftClick()
            {
                if(mouseHoverStatus && h > 2)
                {
                    h--;
                    hValueText.setText(h + "");
                }
            }
        };

        ArrowButton hRight = new ArrowButton(arrowOffset, hPos, buttonColor, true)
        {
            @Override
            public void onLeftClick()
            {
                if(mouseHoverStatus && h < 20)
                {
                    h++;
                    hValueText.setText(h + "");
                }
            }
        };

        addChild(wLeft);
        addChild(wRight);
        addChild(wText);
        addChild(wValueText);

        addChild(hLeft);
        addChild(hRight);
        addChild(hText);
        addChild(hValueText);

        Mesh<ColorQuad> bg = new Mesh<>(new ColorMaterial(Camera.ORTHO2D_S), MeshConfig.QUAD_STATIC);
        bg.addPrimitive(new ColorQuad(-10, -1, Constants.menuBgZ, 20, 2, 0, bgColor));
        createRenderable(0, 0, bg);
    }

    public void open()
    {
        yMoveSpeed = -MOVE_SPEED;
        yDest = 0;
    }

    public void close()
    {
        yMoveSpeed = MOVE_SPEED;
        yDest = 2;
    }

    @Override
    public void drawUpdate(float timeStep)
    {
        super.drawUpdate(timeStep);

        if(yMoveSpeed == 0) return;

        float dy = timeStep*yMoveSpeed;
        float y = transform.pos.y;
        boolean neg = yMoveSpeed < 0;

        if( (dy <= yDest - y && neg) || (dy >= yDest - y && !neg) )
        {
            transform.translate(0, yDest - y, 0);
            yMoveSpeed = 0;
        }
        else transform.translate(0, dy, 0);
    }

    @Override
    public void onKeyPressed(int key, int mods)
    {
        super.onKeyPressed(key, mods);

        if(key == Keyboard.KEY_ESCAPE) GameScene.closeOptions = true;
    }
}
