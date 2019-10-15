package com.pieisnotpi.game.ui;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.text.TextMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.text.TextQuad;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.ui.text.font.CharSprite;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.scenes.GameScene;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static com.pieisnotpi.game.Constants.MENU_Z;

public class OptionsButton extends UiObject
{
    private static final CharSprite sprite = new CharSprite(1, 1, 0, 0, 1, 1, 0, 0);

    private final TextMaterial material = new TextMaterial(Camera.ORTHO2D_S, Texture.getTextureFile("gear.png", Texture.FILTER_LINEAR));
    private GameScene scene;
    private OptionsMenu menu;
    private Vector3f pos;

    public OptionsButton(Color normal, Color highlight, OptionsMenu menu)
    {
        this.menu = menu;

        pos = transform.pos;
        size.set(0.15f, 0.15f, 0);

        TextQuad quad = new TextQuad(0, 0, MENU_Z, size.x, size.y, sprite, normal, highlight, 0);
        Mesh<TextQuad> mesh = new Mesh<TextQuad>(material, MeshConfig.QUAD_STATIC).addPrimitive(quad).build();
        createRenderable(1, 0, mesh);

        transform.setTranslate(-0.6f, 0.75f, 0);
    }
    
    @Override
    public void onLeftClick()
    {
        super.onLeftClick();
        
        if(mouseHoverStatus)
        {
            if(menu.isOpen())
            {
                scene.setNewBoardSize(menu.w, menu.h);
                menu.closeMenu();
            }
            else menu.openMenu();
        }
    }
    
    @Override
    public void onMouseEntered()
    {
        super.onMouseEntered();
        material.outlineSize = 4;
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
