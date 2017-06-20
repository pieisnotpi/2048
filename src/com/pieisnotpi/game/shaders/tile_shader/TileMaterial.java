package com.pieisnotpi.game.shaders.tile_shader;

import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.utility.BufferUtility;

import java.util.List;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class TileMaterial extends Material
{
    private Texture texture;

    public TileMaterial(int matrixID, Texture texture)
    {
        super(TileShader.id, matrixID);
        this.texture = texture;
    }

    @Override
    public Attribute[] genAttributes(boolean isStatic)
    {
        int mode = isStatic ? GL_STATIC_DRAW : GL_DYNAMIC_DRAW;
        return new Attribute[]{new Attribute("VertexPosition", shader, 3, mode, isStatic), new Attribute("VertexColor", shader, 4, mode, isStatic), new Attribute("VertexTexCoord", shader, 2, mode, isStatic)};
    }

    @Override
    public void putElements(List<? extends Renderable> renderables, VertexArray a)
    {
        renderables.forEach(r ->
        {
            BufferUtility.putVec3s(a.attributes[0].buffer, r.points);
            BufferUtility.putColors(a.attributes[1].buffer, r.colors);
            BufferUtility.putVec2s(a.attributes[2].buffer, r.texCoords);
        });
    }
    
    @Override
    public void bind()
    {
        texture.bind(0);
    }
}
