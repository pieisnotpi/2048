package com.pieisnotpi.game.ui;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorTriangle;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static com.pieisnotpi.game.Constants.menuZ;

public class ArrowButton extends UiObject
{
    private final ColorMaterial material = new ColorMaterial(Camera.ORTHO2D_S);
    private Vector3f pos;

    public ArrowButton(float xPos, float yPos, Color color, boolean flip)
    {
        pos = transform.pos;
        size.set(0.125f, 0.125f, 0);

        transform.setTranslate(xPos - size.x/2, yPos, 0);
        transform.setCenter(size.x/2, size.y/2, 0);
        if(flip) transform.scaleCentered(-1, 1, 1);

        ColorTriangle triangle = new ColorTriangle(new Vector3f(0, size.y/2, menuZ), new Vector3f(size.x, size.y, menuZ), new Vector3f(size.x, 0, menuZ), color, color, color);
        Mesh<ColorTriangle> mesh = new Mesh<ColorTriangle>(material, MeshConfig.TRIANGLE_STATIC).addPrimitive(triangle).build();
        createRenderable(0, 0, mesh);
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
        getTransform().scaleCentered(1.1f);
    }
    
    @Override
    public void onMouseExited()
    {
        super.onMouseExited();
        getTransform().scaleCentered(0.9f);
    }
    
    @Override
    public boolean isPointInsideObject(Vector2f point)
    {
        return point.x >= pos.x && point.x <= pos.x + size.x && point.y >= pos.y && point.y <= pos.y + size.y;
    }
}
