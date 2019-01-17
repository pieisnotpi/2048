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
import com.pieisnotpi.game.ui.*;
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

    private int w, h, nw, nh, score = 0;

    private Background bg;
    public List<GameTile> deletionQue = new ArrayList<>(10);
    public BackTile[][] backTiles;
    public GameTile[][] gameTiles;
    public ColorParser colors;
    private Text scoreText;
    private static boolean restart = false, lockGameInput = false;
    
    private ExitButton exitButton;
    private OptionsMenu optionsMenu;
    private EndMenu endMenu;
    public boolean lost = false, won = false;
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

        setBoardSize(w, h);

        scoreText = new Text(SCORE_FONT, "Score: " + score, new Vector3f(), Camera.ORTHO2D_S);
        addGameObject(scoreText);
        scoreText.setTextColor(colors.getTextColor(2));
        scoreText.setAlignment(UiObject.HAlignment.CENTER, UiObject.VAlignment.TOP, 0, -0.1f);
        scoreText.getTransform().setScale(0.002f);
        scoreText.setOutlineSize(0);

        optionsMenu = new OptionsMenu(w, h, colors.getTextColor(2), clearColor);
        addGameObject(optionsMenu);

        endMenu = new EndMenu(colors.getTextColor(2), clearColor);
        addGameObject(endMenu);

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

    /**
     * Empties the game board, removing and destroying all tiles
     */
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

    /**
     * Resets the game board, bringing it back to the default state
     */
    public void resetGame()
    {
        clearBoard();
        genTiles(2);
        score = 0;
        scoreText.setText("Score: 0");
        restart = false;
        lost = false;
    }

    /**
     * Sets the given tile coordinate to the given value
     * If the space is already occupied, sets the occupying tile's value
     * If it isn't, creates a new tile with the specified value
     * @param x x-coordinate of the tile (in board coordinates)
     * @param y y-coordinate of the tile (in board coordinates)
     * @param value The tile's new value
     */
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

    /**
     * Sets the game score
     * @param score New score value
     */
    public void setScore(int score)
    {
        this.score = score;
        if(scoreText != null) scoreText.setText("Score: " + score);
    }

    /**
     * Sets the board's new width and height
     * Used by game objects that can't disturb engine flow
     * @param newWidth New board width
     * @param newHeight New board height
     */
    public void setNewBoardSize(int newWidth, int newHeight)
    {
        nw = newWidth;
        nh = newHeight;
    }

    /**
     * Returns board's current width
     * @return Board width
     */
    public int getBoardWidth()
    {
        return w;
    }

    /**
     * Returns board's current height
     * @return Board height
     */
    public int getBoardHeight()
    {
        return h;
    }

    /**
     * Returns current game score
     * @return Game score
     */
    public int getScore()
    {
        return score;
    }

    /**
     * Returns the tile color for the given tile value
     * Returns default value if the tile value doesn't have a specified tile color
     * @param value Tile value
     * @return Tile color for the specified value
     */
    public Color getTileColor(int value)
    {
        return colors.getTileColor(value);
    }

    /**
     * Returns the text color for the given tile value
     * Returns default value if the tile value doesn't have a specified text color
     * @param value Tile value
     * @return Text color for the specified value
     */
    public Color getTextColor(int value)
    {
        return colors.getTextColor(value);
    }

    /**
     * Sets the board's size to the given width and height
     * Completely resets the board as a result
     * @param w New width of the board
     * @param h New height of the board
     */
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
        radii = size/RADII_DIV;

        bg = new Background(tileColor, bgColor, backTiles, w, h);
        addGameObject(bg);

        genTiles(2);

        if(scoreText != null)
        {
            score = 0;
            scoreText.setText("Score: 0");
        }
        if(window != null) onWindowResize(window.getWindowRes());
    }

    /**
     * Game state check for the tiles
     * Checks each individual tile to see if it can move
     * If no tiles can move, activates the 'game lost' procedure
     */
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

                    if(!won && tile.getValue() == 2048)
                    {
                        endMenu.openWithWin(score);
                        won = true;
                    }

                    if(locked && (tile.scanLeft() != x || tile.scanRight() != x || tile.scanDown() != y || tile.scanUp() != y)) locked = false;
                }
                else locked = false;
            }
        }

        if(locked)
        {
            endMenu.openWithLoss(score);
            //addGameObject(loseText);
            lost = true;
        }
    }

    /**
     * Generates the specified number of tiles at random positions
     * @param amount Number of tiles to generate
     */
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

    /**
     * Moves all tiles upward
     * @return  true of any tiles successfully moved
     *          false if no tiles moved
     */
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

    /**
     * Moves all tiles downward
     * @return  true of any tiles successfully moved
     *          false if no tiles moved
     */
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

    /**
     * Moves all tiles leftward
     * @return  true of any tiles successfully moved
     *          false if no tiles moved
     */
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

    /**
     * Moves all tiles rightward
     * @return  true of any tiles successfully moved
     *          false if no tiles moved
     */
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

    @Override
    public void update(float timeStep) throws Exception
    {
        super.update(timeStep);

        if(restart) resetGame();

        if(w != nw || h != nh) setBoardSize(nw, nh);
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

        if(lockGameInput) return;

        boolean moved = false;

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

        if(lockGameInput) return;
        
        mPos.set(window.inputManager.cursorPos);
    }

    @Override
    public void onLeftRelease()
    {
        super.onLeftRelease();

        if(lockGameInput) return;

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

    /**
     * Tells the game to restart next update
     * Used by game objects to trigger a restart since actual restarting causes a concurrent modification exception
     */
    public static void restartGame()
    {
        restart = true;
    }

    /**
     * Locks game input. Prevents tile movements and zooming
     */
    public static void lockGameInput()
    {
        lockGameInput = true;
    }

    /**
     * Unlocks game input
     */
    public static void unlockGameInput()
    {
        lockGameInput = false;
    }

    /**
     * Returns the game's input lock status
     * @return True if input is locked
     *         False otherwise
     */
    public static boolean isInputLocked()
    {
        return lockGameInput;
    }

    /**
     * Generates a random integer in a range
     * @param min Minimum integer value
     * @param max Maximum integer value
     * @return A random value
     */
    public static int randInt(int min, int max)
    {
        return random.nextInt(max - min + 1) + min;
    }
}
