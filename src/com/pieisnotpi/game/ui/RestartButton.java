package com.pieisnotpi.game.ui;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.text_shader.TextMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.text_shader.TextQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.ui.text.font.CharSprite;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.scenes.GameScene;
import org.joml.Vector2f;

public class RestartButton extends UiObject<TextQuad>
{
    private static final CharSprite sprite = new CharSprite(new Sprite(0f, 0f, 1f, 1f), ' ', 0, 0);
    
    private final TextMaterial material = new TextMaterial(Camera.ORTHO2D_S, Texture.getTextureFile("refresh.png", Texture.FILTER_LINEAR));
    private GameScene scene;
    private Vector2f pos;
    
    public RestartButton(Color normal, Color highlight)
    {
        pos = new Vector2f(0.45f, 0.75f);
        size.set(0.15f, 0.15f, 0);
    
        TextQuad quad = new TextQuad(0, 0, -1, size.x, size.y, sprite, normal, highlight, 0);
        createMesh(material, MeshConfig.QUAD_STATIC).addRenderable(quad).build();
        transform.setTranslate(pos.x, pos.y, 0);
        mesh.setSorting(true);
    }
    
    @Override
    public void onLeftClick()
    {
        super.onLeftClick();
        
        if(mouseHoverStatus) scene.restart = true;
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
    
    @Override
    public void onRegister(Scene scene)
    {
        super.onRegister(scene);
        this.scene = (GameScene) scene;
    }
}
