package com.pieisnotpi.game.ui;

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
import com.pieisnotpi.game.scenes.GameScene;
import org.joml.Vector2f;

public class RestartButton extends UiObject
{
    private static final CharSprite sprite = new CharSprite(new Sprite(0f, 0f, 1f, 1f), ' ', 0, 0);
    
    private final TextMaterial material = new TextMaterial(Camera.ORTHO2D_S, Texture.getTextureFile("refresh.png", Texture.FILTER_LINEAR));
    private Vector2f pos;
    
    public RestartButton(Color normal, Color highlight)
    {
        pos = new Vector2f(0.45f, 0.75f);
        size.set(0.15f, 0.15f, 0);
    
        TextQuad quad = new TextQuad(0, 0, Constants.MENU_BG_Z - 0.05f, size.x, size.y, sprite, normal, highlight, 0);
        Mesh<TextQuad> mesh = new Mesh<TextQuad>(material, MeshConfig.QUAD_STATIC).addPrimitive(quad).build();
        createRenderable(1, 0, mesh);
        transform.setTranslate(pos.x, pos.y, 0);
    }
    
    @Override
    public void onLeftClick()
    {
        super.onLeftClick();
        
        if(mouseHoverStatus) GameScene.restartGame();
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
