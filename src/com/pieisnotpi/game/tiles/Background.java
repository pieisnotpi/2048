package com.pieisnotpi.game.tiles;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorTriangle;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.Constants;
import com.pieisnotpi.game.scenes.GameScene;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static com.pieisnotpi.game.Constants.*;

public class Background extends GameObject
{
    private static final ColorMaterial material = new ColorMaterial(Camera.ORTHO2D_S);
    private static final double halfPI = Math.PI/2;

    private float zoom = 1;

    public Background(Color tileColor, Color bgColor, BackTile[][] backTiles, int w, int h)
    {
        float size = Constants.size;

        Mesh<ColorTriangle> mGrid = new Mesh<>(material, MeshConfig.TRIANGLE_STATIC);

        createRenderable(0, 0, mGrid);

        float gridWidth = w* size + (w - 1)* TILE_SPACE, gridHeight = h*size + (h - 1)* TILE_SPACE;
        float xOffset = -gridWidth/2, yOffset = -gridHeight/2;

        float bgX = xOffset - BG_SPACE, bgY = yOffset - BG_SPACE, bgW = gridWidth + BG_SPACE *2, bgH = gridHeight + BG_SPACE *2;
        Vector3f bgTL = new Vector3f(bgX + radii, bgY + bgH, BG_Z), bgBR = new Vector3f(bgX + bgW - radii, bgY, BG_Z);
        Vector3f bgRB = new Vector3f(bgX + bgW, bgY + radii, BG_Z), bgLT = new Vector3f(bgX, bgY + bgH - radii, BG_Z);

        mGrid.addPrimitive(new ColorTriangle(new Vector3f(bgX + radii, bgY, BG_Z), bgTL, bgBR, bgColor, bgColor, bgColor));
        mGrid.addPrimitive(new ColorTriangle(bgTL, new Vector3f(bgX + bgW - radii, bgY + bgH, BG_Z), bgBR, bgColor, bgColor, bgColor));
        mGrid.addPrimitive(new ColorTriangle(new Vector3f(bgX, bgY + radii, BG_Z), bgLT, bgRB, bgColor, bgColor, bgColor));
        mGrid.addPrimitive(new ColorTriangle(bgLT, new Vector3f(bgX + bgW, bgY + bgH - radii, BG_Z), bgRB, bgColor, bgColor, bgColor));

        assembleCorner(bgX + bgW - radii, bgY + bgH - radii, BG_Z, radii, 0, bgColor, mGrid);
        assembleCorner(bgX + radii, bgY + bgH - radii, BG_Z, radii, halfPI, bgColor, mGrid);
        assembleCorner(bgX + radii, bgY + radii, BG_Z, radii, Math.PI, bgColor, mGrid);
        assembleCorner(bgX + bgW - radii, bgY + radii, BG_Z, radii, halfPI*3, bgColor, mGrid);

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                float nx = xOffset + x*size + x* TILE_SPACE, ny = yOffset + y*size + y* TILE_SPACE;
                backTiles[x][y] = new BackTile(nx, ny, size);

                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx + radii, ny, BG_TILE_Z), new Vector3f(nx + radii, ny + size, BG_TILE_Z), new Vector3f(nx + size - radii, ny, BG_TILE_Z), tileColor, tileColor, tileColor));
                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx + radii, ny + size, BG_TILE_Z), new Vector3f(nx + size - radii, ny + size, BG_TILE_Z), new Vector3f(nx + size - radii, ny, BG_TILE_Z), tileColor, tileColor, tileColor));
                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx, ny + radii, BG_TILE_Z), new Vector3f(nx + size, ny + radii, BG_TILE_Z), new Vector3f(nx + 0, ny + size - radii, BG_TILE_Z), tileColor, tileColor, tileColor));
                mGrid.addPrimitive(new ColorTriangle(new Vector3f(nx + size, ny + radii, BG_TILE_Z), new Vector3f(nx + size, ny + size - radii, BG_TILE_Z), new Vector3f(nx, ny + size - radii, BG_TILE_Z), tileColor, tileColor, tileColor));

                assembleCorner(nx + size - radii, ny + size - radii, BG_TILE_Z, radii, 0, tileColor, mGrid);
                assembleCorner(nx + radii, ny + size - radii, BG_TILE_Z, radii, halfPI, tileColor, mGrid);
                assembleCorner(nx + radii, ny + radii, BG_TILE_Z, radii, Math.PI, tileColor, mGrid);
                assembleCorner(nx + size - radii, ny + radii, BG_TILE_Z, radii, halfPI*3, tileColor, mGrid);
            }
        }

        mGrid.build();
    }

    @Override
    public void onScroll(float xAmount, float yAmount)
    {
        super.onScroll(xAmount, yAmount);

        if(GameScene.isInputLocked()) return;

        final float zoomOut = 0.25f, zoomIn = 1.1f;
        float scale = getTransform().scale.x;

        if(yAmount < 0)
        {
            float zoomAmount = 0.9f*scale > zoomOut ? 0.9f : zoomOut/(scale);

            getTransform().scale(zoomAmount);
            zoom *= zoomAmount;
        }
        else if(yAmount > 0)
        {
            float zoomAmount = 1.1f*scale < zoomIn ? 1.1f : zoomIn/(scale);

            getTransform().scale(zoomAmount);
            zoom *= zoomAmount;
        }
    }

    @Override
    public void onWindowResize(Vector2i res)
    {
        super.onWindowResize(res);

        float rx = (float) res.x/res.y, ratio = Float.min(rx, 1f);

        getTransform().setScale(zoom*ratio/BORDER);
    }

    private void assembleCorner(float dx, float dy, float z, float radii, double offset, Color color, Mesh<ColorTriangle> mesh)
    {
        double angle = halfPI/ CORNER_SIDES;

        for(int i = 0; i < CORNER_SIDES; i++)
        {
            double a0 = angle*i + offset, a1 = angle*(i+1) + offset;
            float x0 = dx + (float) (radii*Math.cos(a0)), y0 = dy + (float) (radii*Math.sin(a0)), x1 = dx + (float) (radii*Math.cos(a1)), y1 = dy + (float) (radii*Math.sin(a1));

            mesh.addPrimitive(new ColorTriangle(new Vector3f(x0, y0, z), new Vector3f(dx, dy, z), new Vector3f(x1, y1, z), color, color, color));
        }
    }
}
