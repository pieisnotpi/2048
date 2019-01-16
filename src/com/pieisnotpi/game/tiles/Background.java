package com.pieisnotpi.game.tiles;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorTriangle;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.Constants;
import org.joml.Vector3f;

import static com.pieisnotpi.game.Constants.*;

public class Background extends GameObject
{
    private static final ColorMaterial material = new ColorMaterial(Camera.ORTHO2D_S);
    private static final double halfPI = Math.PI/2;

    public Background(Color tileColor, Color bgColor, BackTile[][] backTiles, int w, int h)
    {
        float size = Constants.size;

        Mesh<ColorTriangle> mGrid = new Mesh<>(material, MeshConfig.TRIANGLE_STATIC);

        createRenderable(0, 0, mGrid);

        float gridWidth = w* size + (w - 1)*tileSpace, gridHeight = h*size + (h - 1)*tileSpace;
        float xOffset = -gridWidth/2, yOffset = -gridHeight/2;

        float bgX = xOffset - bgSpace, bgY = yOffset - bgSpace, bgW = gridWidth + bgSpace*2, bgH = gridHeight + bgSpace*2;
        Vector3f bgTL = new Vector3f(bgX + radii, bgY + bgH, bgZ), bgBR = new Vector3f(bgX + bgW - radii, bgY, bgZ);
        Vector3f bgRB = new Vector3f(bgX + bgW, bgY + radii, bgZ), bgLT = new Vector3f(bgX, bgY + bgH - radii, bgZ);

        mGrid.addPrimitive(new ColorTriangle(new Vector3f(bgX + radii, bgY, bgZ), bgTL, bgBR, bgColor, bgColor, bgColor));
        mGrid.addPrimitive(new ColorTriangle(bgTL, new Vector3f(bgX + bgW - radii, bgY + bgH, bgZ), bgBR, bgColor, bgColor, bgColor));
        mGrid.addPrimitive(new ColorTriangle(new Vector3f(bgX, bgY + radii, bgZ), bgLT, bgRB, bgColor, bgColor, bgColor));
        mGrid.addPrimitive(new ColorTriangle(bgLT, new Vector3f(bgX + bgW, bgY + bgH - radii, bgZ), bgRB, bgColor, bgColor, bgColor));

        assembleCorner(bgX + bgW - radii, bgY + bgH - radii, bgZ, radii, 0, bgColor, mGrid);
        assembleCorner(bgX + radii, bgY + bgH - radii, bgZ, radii, halfPI, bgColor, mGrid);
        assembleCorner(bgX + radii, bgY + radii, bgZ, radii, Math.PI, bgColor, mGrid);
        assembleCorner(bgX + bgW - radii, bgY + radii, bgZ, radii, halfPI*3, bgColor, mGrid);

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                float nx = xOffset + x*size + x*tileSpace, ny = yOffset + y*size + y*tileSpace;
                backTiles[x][y] = new BackTile(nx, ny, size);

                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx + radii, ny, bgTileZ), new Vector3f(nx + radii, ny + size, bgTileZ), new Vector3f(nx + size - radii, ny, bgTileZ), tileColor, tileColor, tileColor));
                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx + radii, ny + size, bgTileZ), new Vector3f(nx + size - radii, ny + size, bgTileZ), new Vector3f(nx + size - radii, ny, bgTileZ), tileColor, tileColor, tileColor));
                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx, ny + radii, bgTileZ), new Vector3f(nx + size, ny + radii, bgTileZ), new Vector3f(nx + 0, ny + size - radii, bgTileZ), tileColor, tileColor, tileColor));
                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx + size, ny + radii, bgTileZ), new Vector3f(nx + size, ny + size - radii, bgTileZ), new Vector3f(nx, ny + size - radii, bgTileZ), tileColor, tileColor, tileColor));

                assembleCorner(nx + size - radii, ny + size - radii, bgTileZ, radii, 0, tileColor, mGrid);
                assembleCorner(nx + radii, ny + size - radii, bgTileZ, radii, halfPI, tileColor, mGrid);
                assembleCorner(nx + radii, ny + radii, bgTileZ, radii, Math.PI, tileColor, mGrid);
                assembleCorner(nx + size - radii, ny + radii, bgTileZ, radii, halfPI*3, tileColor, mGrid);
            }
        }

        mGrid.build();
        //mGridBg.build();
    }

    /*public Background(Color tileColor, Color bgColor, float size, int w, int h)
    {
        Mesh<ColorTriangle> mGrid = new Mesh<>(material, MeshConfig.TRIANGLE_STATIC);
        Mesh<ColorQuad> mGridBg = new Mesh<>(material, MeshConfig.QUAD_STATIC);

        createRenderable(0, 0, mGrid, mGridBg);

        float gridWidth = w* size + (w - 1)*tileSpace, gridHeight = h*size + (h - 1)*tileSpace;
        float xOffset = -gridWidth/2, yOffset = -gridHeight/2;

        mGridBg.addPrimitive(new ColorQuad(xOffset - bgSpace, yOffset - bgSpace, bgZ, gridWidth + bgSpace*2, gridHeight + bgSpace*2, 0, bgColor));

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                float nx = xOffset + x*size + x*tileSpace, ny = yOffset + y*size + y*tileSpace;

                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx + radii, ny, bgTileZ), new Vector3f(nx + radii, ny + size, bgTileZ), new Vector3f(nx + size - radii, ny, bgTileZ), tileColor, tileColor, tileColor));
                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx + radii, ny + size, bgTileZ), new Vector3f(nx + size - radii, ny + size, bgTileZ), new Vector3f(nx + size - radii, ny, bgTileZ), tileColor, tileColor, tileColor));
                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx, ny + radii, bgTileZ), new Vector3f(nx + size, ny + radii, bgTileZ), new Vector3f(nx + 0, ny + size - radii, bgTileZ), tileColor, tileColor, tileColor));
                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx + size, ny + radii, bgTileZ), new Vector3f(nx + size, ny + size - radii, bgTileZ), new Vector3f(nx, ny + size - radii, bgTileZ), tileColor, tileColor, tileColor));

                assembleCorner(nx + size - radii, ny + size - radii, bgTileZ, radii, 0, tileColor, mGrid);
                assembleCorner(nx + radii, ny + size - radii, bgTileZ, radii, halfPI, tileColor, mGrid);
                assembleCorner(nx + radii, ny + radii, bgTileZ, radii, Math.PI, tileColor, mGrid);
                assembleCorner(nx + size - radii, ny + radii, bgTileZ, radii, halfPI*3, tileColor, mGrid);
            }
        }

        mGrid.build();
        mGridBg.build();
    }*/

    private void assembleCorner(float dx, float dy, float z, float radii, double offset, Color color, Mesh<ColorTriangle> mesh)
    {
        double angle = halfPI/cornerSides;

        for(int i = 0; i < cornerSides; i++)
        {
            double a0 = angle*i + offset, a1 = angle*(i+1) + offset;
            float x0 = dx + (float) (radii*Math.cos(a0)), y0 = dy + (float) (radii*Math.sin(a0)), x1 = dx + (float) (radii*Math.cos(a1)), y1 = dy + (float) (radii*Math.sin(a1));

            mesh.addPrimitive(new ColorTriangle(new Vector3f(x0, y0, z), new Vector3f(dx, dy, z), new Vector3f(x1, y1, z), color, color, color));
        }
    }
}
