package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.input.keyboard.Keybind;
import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.ui.text.Text;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.parsers.ColorParser;
import com.pieisnotpi.game.tiles.BackTile;
import com.pieisnotpi.game.tiles.Background;
import com.pieisnotpi.game.tiles.GameTile;
import com.pieisnotpi.game.ui.ExitButton;
import com.pieisnotpi.game.ui.OptionsButton;
import com.pieisnotpi.game.ui.OptionsMenu;
import com.pieisnotpi.game.ui.RestartButton;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.pieisnotpi.game.Constants.*;

public class GameScene extends Scene
{
    private static final Random random = new Random();

    public int w, h, nw, nh, score = 0;

    private Background bg;
    public List<GameTile> deletionQue = new ArrayList<>(10);
    public BackTile[][] backTiles;
    public GameTile[][] gameTiles;
    public Text scoreText;
    public ColorParser colors;
    public static boolean restart = false, openOptions = false, closeOptions = false, menuOpen = false;
    
    private ExitButton exitButton;
    private OptionsMenu optionsMenu;
    private Text loseText;
    private float zoom = 1;
    public boolean lost = false;
    private Vector2i mPos = new Vector2i();

    public GameScene(int w, int h)
    {
        this.w = nw = w;
        this.h = nh = h;
    }

    public GameScene init() throws Exception
    {
        super.init();
    
        colors = new ColorParser("options/theme.cfg");
        
        addCamera(new Camera(1, new Vector2f(0, 0), new Vector2f(1, 1)));
        clearColor.set(colors.getBackColor("bg"));

        loseText = new Text(loseFont, "You have lost...", new Vector3f(), new Color("#FFFFE6"), new Color("#323232"), Camera.ORTHO2D_S);
        addGameObject(loseText);
        loseText.setAlignment(UiObject.HAlignment.CENTER, UiObject.VAlignment.CENTER, 0, 0.3f);
        loseText.getTransform().setScale(0.004f);
        loseText.setOutlineSize(2);

        setBoardSize(w, h);
        
        removeGameObject(loseText);

        scoreText = new Text(scoreFont, "Score: " + score, new Vector3f(), Camera.ORTHO2D_S);
        addGameObject(scoreText);
        scoreText.setTextColor(colors.getTextColor(2));
        scoreText.setAlignment(UiObject.HAlignment.CENTER, UiObject.VAlignment.TOP, 0, -0.1f);
        scoreText.getTransform().setScale(0.002f);
        scoreText.setOutlineSize(0);

        optionsMenu = new OptionsMenu(w, h, colors.getTextColor(2), clearColor);
        addGameObject(optionsMenu);

        addGameObject(new RestartButton(colors.getTextColor(2), colors.getTextColor(2)));
        addGameObject(new OptionsButton(colors.getTextColor(2), colors.getTextColor(2), optionsMenu));
        
        exitButton = new ExitButton(colors.getTextColor(2), colors.getTextColor(2));
        
        genTiles(2);

        // Debug tile placement
        /*setTile(0, 0, 2);
        setTile(1, 0, 4);
        setTile(2, 0, 8);
        setTile(3, 0, 16);
        setTile(0, 1, 32);
        setTile(1, 1, 64);
        setTile(2, 1, 128);
        setTile(3, 1, 256);
        setTile(0, 2, 512);
        setTile(1, 2, 1024);
        setTile(2, 2, 2048);
        setTile(3, 2, 4096);*/
        
        addKeybind(new Keybind(Keyboard.KEY_F11, () ->
        {
            window.setFullscreen(!window.isFullscreen());
            if(window.isFullscreen()) addGameObject(exitButton);
            else removeGameObject(exitButton);
            
        }, null, null));

        return this;
    }

    private void setBoardSize(int w, int h)
    {
        if(bg != null)
        {
            clearBoard();
            bg.destroy();
            removeGameObject(bg);
        }

        this.w = w;
        this.h = h;

        Color tileColor = colors.getBackColor("bt"), bgColor = colors.getBackColor("bp");

        backTiles = new BackTile[w][h];
        gameTiles = new GameTile[w][h];

        float qw = 2f/w, qh = 2f/h;
        size = Float.min(qw, qh);
        radii = size/radiiDiv;

        bg = new Background(tileColor, bgColor, backTiles, w, h);
        addGameObject(bg);

        genTiles(2);

        if(scoreText != null)
        {
            score = 0;
            scoreText.setText("Score: " + score);
        }
        if(window != null) onWindowResize(window.getWindowRes());
    }

    private void resetTiles()
    {
        boolean locked = true;

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                GameTile tile = gameTiles[x][y];

                if(tile != null)
                {
                    tile.merged = false;

                    if(locked && (tile.scanLeft() != x || tile.scanRight() != x || tile.scanDown() != y || tile.scanUp() != y)) locked = false;
                }
                else locked = false;
            }
        }

        if(locked)
        {
            addGameObject(loseText);
            lost = true;
        }
    }

    public void clearBoard()
    {
        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                if(gameTiles[x][y] != null)
                {
                    gameTiles[x][y].destroy();
                    gameTiles[x][y] = null;
                }
            }
        }
    }

    public void resetGame()
    {
        clearBoard();
        genTiles(2);
        score = 0;
        scoreText.setText("Score: 0");
        restart = false;
        lost = false;
    }

    private void genTiles(int amount)
    {
        for(int i = 0, x, y; i < amount; i++)
        {
            do
            {
                x = randInt(0, w - 1);
                y = randInt(0, h - 1);
            }
            while(gameTiles[x][y] != null);

            int r = randInt(1, 100), value;
            if(r < 90) value = 2;
            else value = 4;

            GameTile t = gameTiles[x][y] = new GameTile(x, y, value, size, this);
            addGameObject(t);
            bg.addChild(t);
        }
    }

    public void setTile(int x, int y, int value)
    {
        if(gameTiles[x][y] != null) gameTiles[x][y].setValue(value);
        else
        {
            gameTiles[x][y] = new GameTile(x, y, value, size, this);
            addGameObject(gameTiles[x][y]);
            bg.addChild(gameTiles[x][y]);
        }
    }

    private boolean moveTilesUp()
    {
        boolean moved = false;

        for(int y = h - 1; y >= 0; y--)
        {
            for(int x = 0; x < w; x++)
            {
                GameTile moving = gameTiles[x][y];
                if(moving == null) continue;

                int maxY = moving.scanUp();

                if(maxY == y) continue;

                if(gameTiles[x][maxY] == null) moving.moveTo(x, maxY, false);
                else moving.mergeWith(gameTiles[x][maxY]);

                moved = true;
            }
        }

        return moved;
    }

    private boolean moveTilesDown()
    {
        boolean moved = false;

        for(int y = 0; y < h; y++)
        {
            for(int x = 0; x < w; x++)
            {
                GameTile moving = gameTiles[x][y];
                if(moving == null) continue;

                int maxY = moving.scanDown();

                if(maxY == y) continue;

                if(gameTiles[x][maxY] == null) moving.moveTo(x, maxY, false);
                else moving.mergeWith(gameTiles[x][maxY]);

                moved = true;
            }
        }

        return moved;
    }

    private boolean moveTilesLeft()
    {
        boolean moved = false;

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                GameTile moving = gameTiles[x][y];
                if(moving == null) continue;

                int maxX = moving.scanLeft();

                if(maxX == x) continue;

                if(gameTiles[maxX][y] == null) moving.moveTo(maxX, y, false);
                else moving.mergeWith(gameTiles[maxX][y]);

                moved = true;
            }
        }

        return moved;
    }

    private boolean moveTilesRight()
    {
        boolean moved = false;

        for(int x = w - 1; x >= 0; x--)
        {
            for(int y = 0; y < h; y++)
            {
                GameTile moving = gameTiles[x][y];
                if(moving == null) continue;

                int maxX = moving.scanRight();

                if(maxX == x) continue;

                if(gameTiles[maxX][y] == null) moving.moveTo(maxX, y, false);
                else moving.mergeWith(gameTiles[maxX][y]);

                moved = true;
            }
        }

        return moved;
    }

    private int randInt(int min, int max)
    {
        return random.nextInt(max - min + 1) + min;
    }
    
    @Override
    public void onScroll(float xAmount, float yAmount)
    {
        super.onScroll(xAmount, yAmount);

        if(openOptions) return;
        
        final float zoomOut = 0.25f, zoomIn = 1.1f;
        float scale = bg.getTransform().scale.x;
        
        if(yAmount < 0)
        {
            float zoomAmount = 0.9f*scale > zoomOut ? 0.9f : zoomOut/(scale);
            
            bg.getTransform().scale(zoomAmount);
            zoom *= zoomAmount;
        }
        else if(yAmount > 0)
        {
            float zoomAmount = 1.1f*scale < zoomIn ? 1.1f : zoomIn/(scale);
    
            bg.getTransform().scale(zoomAmount);
            zoom *= zoomAmount;
        }
    }
    
    @Override
    public void update(float timeStep) throws Exception
    {
        super.update(timeStep);
        
        if(restart) resetGame();

        if(openOptions)
        {
            //addGameObject(optionsMenu);
            optionsMenu.open();
            openOptions = false;
            menuOpen = true;
        }
        if(closeOptions)
        {
            //removeGameObject(optionsMenu);
            optionsMenu.close();
            closeOptions = false;
            menuOpen = false;
        }
        if(w != nw || h != nh)
        {
            setBoardSize(nw, nh);
        }
    }
    
    @Override
    public void onWindowResize(Vector2i res)
    {
        super.onWindowResize(res);

        float rx = (float) res.x/res.y, ratio = Float.min(rx, 1f);
    
        bg.getTransform().setScale(zoom*ratio/border);
    }

    @Override
    public void drawUpdate(float timeStep) throws Exception
    {
        super.drawUpdate(timeStep);

        for(int i = 0; i < deletionQue.size(); i++)
        {
            GameTile g = deletionQue.get(i);
            if(g.shouldDestroy)
            {
                g.destroy();
                deletionQue.remove(g);
                i--;
            }
        }
    }

    @Override
    public void onKeyPressed(int key, int mods)
    {
        super.onKeyPressed(key, mods);

        if(menuOpen) return;

        boolean moved = false;

        if(lost)
        {
            if(key < Keyboard.KEY_RIGHT || key > Keyboard.KEY_UP)
            {
                removeGameObject(loseText);
                resetGame();
            }

            return;
        }

        if(key == Keyboard.KEY_UP) moved = moveTilesUp();
        if(key == Keyboard.KEY_DOWN) moved = moveTilesDown();
        if(key == Keyboard.KEY_LEFT) moved = moveTilesLeft();
        if(key == Keyboard.KEY_RIGHT) moved = moveTilesRight();

        if(moved) genTiles(1);
        if(key >= Keyboard.KEY_RIGHT && key <= Keyboard.KEY_UP) resetTiles();
    }
    
    @Override
    public void onLeftClick()
    {
        super.onLeftClick();

        if(menuOpen) return;
        
        mPos.set(window.inputManager.cursorPos);
    }

    @Override
    public void onLeftRelease()
    {
        super.onLeftRelease();

        if(menuOpen) return;

        if(lost)
        {
            removeGameObject(loseText);
            resetGame();

            return;
        }

        window.inputManager.cursorPos.sub(mPos, mPos);
        
        int ax = Math.abs(mPos.x), ay = Math.abs(mPos.y);
        
        if(ax < 24 && ay < 24) return;

        boolean moved;

        if(ax > ay)
        {
            if(mPos.x > 0) moved = moveTilesRight();
            else moved = moveTilesLeft();
        }
        else
        {
            if(mPos.y > 0) moved = moveTilesUp();
            else moved = moveTilesDown();
        }

        if(moved) genTiles(1);
        resetTiles();
    }

    public Color getTileColor(int value)
    {
        return colors.getTileColor(value);
    }

    public Color getTextColor(int value)
    {
        return colors.getTextColor(value);
    }


}
