package com.pieisnotpi.game.ui;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.text.TextMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.text.TextQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.ui.text.font.CharSprite;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.Constants;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class ExitButton extends UiObject
{
    private static final CharSprite sprite = new CharSprite(new Sprite(0f, 0f, 1f, 1f), ' ', 0, 0);
    
    private final TextMaterial material = new TextMaterial(Camera.ORTHO2D_S, Texture.getTextureFile("exit.png", Texture.FILTER_LINEAR));
    private Vector3f pos;
    
    public ExitButton(Color normal, Color highlight)
    {
        pos = transform.pos;
        size.set(0.15f, 0.15f, 0);

        matrixID = Camera.ORTHO2D_S;
        setHAlignment(HAlignment.RIGHT, -0.25f);

        TextQuad quad = new TextQuad(0, 0, Constants.MENU_BG_Z - 0.05f, size.x, size.y, sprite, normal, highlight, 0);
        Mesh<TextQuad> mesh = new Mesh<TextQuad>(material, MeshConfig.QUAD_STATIC).addPrimitive(quad).build();
        createRenderable(1, 0, mesh);
        transform.setTranslate(0, 0.75f, 0);
    }
    
    @Override
    public void onLeftClick()
    {
        super.onLeftClick();
        
        if(mouseHoverStatus) PiEngine.close();
    }
    
    @Override
    public void onMouseEntered()
    {
        super.onMouseEntered();
        material.outlineSize = 3;
    }
    
    @Override
    public void onMouseExited()
    {
        super.onMouseExited();
        material.outlineSize = 0;
    }
    
    @Override
    public boolean isPointInsideObject(Vector2f point)
    {
        return point.x >= pos.x && point.x <= pos.x + size.x && point.y >= pos.y && point.y <= pos.y + size.y;
    }
}
