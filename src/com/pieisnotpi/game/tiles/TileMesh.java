package com.pieisnotpi.game.tiles;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorTriangle;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector3f;

import static com.pieisnotpi.game.Constants.*;

public class TileMesh extends Mesh<ColorTriangle>
{
    private static final ColorMaterial material = new ColorMaterial(Camera.ORTHO2D_S);
    private static final double halfPI = Math.PI/2;

    public TileMesh()
    {
        super(material, MeshConfig.TRIANGLE);

        Color color = new Color(0, 0, 0);

        addPrimitive(new ColorTriangle(new Vector3f(radii, 0, tileZ), new Vector3f(radii, size, tileZ), new Vector3f(size - radii, 0, tileZ), color, color, color));
        addPrimitive(new ColorTriangle(new Vector3f(radii, size, tileZ), new Vector3f(size - radii, size, tileZ), new Vector3f(size - radii, 0, tileZ), color, color, color));
        addPrimitive(new ColorTriangle(new Vector3f(0, radii, tileZ), new Vector3f(size, radii, tileZ), new Vector3f(0, size - radii, tileZ), color, color, color));
        addPrimitive(new ColorTriangle(new Vector3f(size, radii, tileZ), new Vector3f(size, size - radii, tileZ), new Vector3f(0, size - radii, tileZ), color, color, color));

        assembleCorner(size - radii, size - radii, radii, 0, color);
        assembleCorner(radii, size - radii, radii, halfPI, color);
        assembleCorner(radii, radii, radii, Math.PI, color);
        assembleCorner(size - radii, radii, radii, halfPI*3, color);
    }

    private void assembleCorner(float dx, float dy, float radii, double offset, Color color)
    {
        double angle = halfPI/cornerSides;

        for(int i = 0; i < cornerSides; i++)
        {
            double a0 = angle*i + offset, a1 = angle*(i+1) + offset;
            float x0 = dx + (float) (radii*Math.cos(a0)), y0 = dy + (float) (radii*Math.sin(a0)), x1 = dx + (float) (radii*Math.cos(a1)), y1 = dy + (float) (radii*Math.sin(a1));

            addPrimitive(new ColorTriangle(new Vector3f(x0, y0, tileZ), new Vector3f(dx, dy, tileZ), new Vector3f(x1, y1, tileZ), color, color, color));
        }
    }

    public void setTileColor(Color color)
    {
        primitives.forEach(r -> r.setColor(color));
        build();
    }
}
