package com.pieisnotpi.game.ui;

import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorQuad;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.ui.text.Text;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.Constants;
import com.pieisnotpi.game.scenes.GameScene;
import org.joml.Vector3f;

import static com.pieisnotpi.game.Constants.MENU_FONT;
import static com.pieisnotpi.game.Constants.MENU_Z;

public class EndMenu extends GameObject
{
    private static final String[] losePrompts = new String[]
            {
                    "Bad news, you've lost.%nGood news, you got a score of %d!%nGood job!",
                    "So close....%nYour final score was %d",
                    "You didn't win, but you still got %d points!%nWoot!",
                    "Welp. You lost.%nIf points were dollars, you'd have $%d.%nDon't spend it all in one place!",
                    "Winning is overrated anyways.%nY'know what isn't? Getting %d points.",
                    "If losing was your goal, ya nailed it!%nIf getting %d points was also your goal,%nyou're on a roll!",
                    "So,%nYou lost at a game of 2048.%nYou screwed up.%nYou know what you did was wrong.%nThe question is,%nHow are you gonna beat %d points?",
                    "Well, you may have lost, but...%nIsn't the real winning...%nthe points you made along the way?%nAll %d of em?",
                    "Are you Alexander Hamilton?%nI hope not, cus you just threw away your shot.%nTell your %d points you're sorry."
            };

    private static final String[] winPrompts = new String[]
            {
                    "Woah! You just won with %d points! Congrats!",
                    "Winner winner, pork dinner!%nYou got %d points, too!",
                    "You won the game!%nYou've done the (probably) impossible!%nAnd with %d points, no less!",
                    "Are you Alexander Hamilton?%n Cus you didn't throw away your shot!%nYour %d points would be proud!",
                    "If love was a game of 2048, then congrats!%nCus you just won with %d points to spare!",
                    "Woah, lookout for mister bigshot,%nwinning with %d points.%nMister highroller, over here.",
                    "If 2048 was Fortnite(TM), kids would think you're cool,%ncus you just won with %d points!",
                    "You won! With %d points!%nThanos would be pr.....",
                    "I hear if you win 2048,%na straight man realizes he's contributing to bigotry%nand actually changes his behavior.%nAlso you got %d points. Congrats!%nPlease keep going!"
            };

    private static final float MOVE_SPEED = 4f;
    private static final float statusScale = 0.001f, promptScale = 0.0015f;

    private float yMoveSpeed = 0, yDest;
    private boolean open = false, wasLoss = false;
    private Text statusText, promptText;

    public EndMenu(Color buttonColor, Color bgColor)
    {
        getTransform().translate(0, 2, 0);

        statusText = new Text(MENU_FONT, "blank", new Vector3f(-0.8f, 0, MENU_Z), buttonColor, buttonColor, Camera.ORTHO2D_S);
        statusText.setHAlignment(UiObject.HAlignment.CENTER, 0f);
        statusText.setVAlignment(UiObject.VAlignment.CENTER, 0.2f);
        statusText.getTransform().setScale(statusScale);

        promptText = new Text(MENU_FONT, "Press any button to continue", new Vector3f(0, -0.9f, MENU_Z), buttonColor, buttonColor, Camera.ORTHO2D_S);
        promptText.setHAlignment(UiObject.HAlignment.CENTER, 0);
        promptText.getTransform().setScale(promptScale);

        Mesh<ColorQuad> bg = new Mesh<>(new ColorMaterial(Camera.ORTHO2D_S), MeshConfig.QUAD_STATIC);
        bg.addPrimitive(new ColorQuad(-10, -1, Constants.MENU_BG_Z, 20, 2, 0, bgColor));
        createRenderable(0, 0, bg);
    }

    @Override
    public void onRegister(Scene scene)
    {
        super.onRegister(scene);
        addChild(statusText);
        addChild(promptText);
    }

    public void openWithWin(int score)
    {
        yMoveSpeed = -MOVE_SPEED;
        yDest = 0;
        GameScene.lockGameInput();

        String prompt = winPrompts[GameScene.randInt(0, winPrompts.length - 1)];

        statusText.setText(String.format(prompt, score));
    }

    public void openWithLoss(int score)
    {
        yMoveSpeed = -MOVE_SPEED;
        yDest = 0;
        GameScene.lockGameInput();

        wasLoss = true;

        String prompt = losePrompts[GameScene.randInt(0, losePrompts.length - 1)];

        statusText.setText(String.format(prompt, score));
    }

    public void closeMenu()
    {
        yMoveSpeed = MOVE_SPEED;
        yDest = 2;
    }

    public boolean isOpen()
    {
        return open;
    }

    @Override
    public void drawUpdate(float timeStep)
    {
        super.drawUpdate(timeStep);

        if(yMoveSpeed == 0) return;

        float dy = timeStep*yMoveSpeed;
        float y = transform.pos.y;
        boolean neg = yMoveSpeed < 0;

        if( (dy <= yDest - y && neg) || (dy >= yDest - y && !neg) )
        {
            transform.translate(0, yDest - y, 0);
            if(yMoveSpeed > 0)
            {
                open = false;
                statusText.setText("blank again");
                GameScene.unlockGameInput();
                wasLoss = false;
            }
            else
            {
                open = true;
            }
            yMoveSpeed = 0;
        }
        else transform.translate(0, dy, 0);
    }

    @Override
    public void onKeyPressed(int key, int mods)
    {
        super.onKeyPressed(key, mods);

        if(key != Keyboard.KEY_PRINT_SCREEN && isOpen())
        {
            closeMenu();
            if(wasLoss) GameScene.restartGame();
        }
    }

    @Override
    public void onLeftClick()
    {
        if(isOpen())
        {
            closeMenu();
            if(wasLoss) GameScene.restartGame();
        }
    }
}
