package com.pieisnotpi.game.parsers;

import com.pieisnotpi.engine.ui.text.font.SystemFont;
import com.pieisnotpi.game.Constants;
import com.pieisnotpi.lib.OptionsFile;
import com.pieisnotpi.lib.types.FloatOption;
import com.pieisnotpi.lib.types.IntOption;
import com.pieisnotpi.lib.types.StringOption;

import java.io.File;

public class ConstantsParser
{
    public static void parseConstants(String path)
    {
        OptionsFile file = new OptionsFile(new File(path));

        file.registerOption("TILES", new IntOption("CORNER_SIDES", 4,
                "The number of triangles per rounded corner. Ex. value of 4 = 4 sides per corner, 16 triangles per tile"));
        file.registerOption("TILES", new FloatOption("TILE_SPACE", 0.04f,
                "Space between game tiles"));
        file.registerOption("TILES", new FloatOption("RADII_DIV", 6,
                "Determines tile corner radii. Higher values mean smaller corners, i.e. 6 means the a corner spans one sixth of the tile. Must be > 2"));
        file.registerOption("TILES", new FloatOption("TILE_SPEED", 8f,
                "Tile speed multiplier. Hire values mean faster tile movements"));

        file.registerOption("BG", new FloatOption("BG_SPACE", 0.04f,
                "Space between outer tiles and the background pane's outer edge"));
        file.registerOption("BG", new FloatOption("BORDER", 2.5f,
                "Determines whitespace around game board"));

        file.registerOption("FONTS", new StringOption("MENU_FONT_NAME", "Arial",
                "The name of the font used for most menu text"));
        file.registerOption("FONTS", new IntOption("MENU_FONT_SIZE", 72,
                "The size in pixels of the font used for most menu text"));
        file.registerOption("FONTS", new StringOption("TILE_FONT_NAME", "Arial",
                "The name of the font used for the tile numbers"));
        file.registerOption("FONTS", new IntOption("TILE_FONT_SIZE", 48,
                "The size of the font used for the tile numbers"));
        file.registerOption("FONTS", new StringOption("SCORE_FONT_NAME", "Arial",
                "The name of the font used for the score counter"));
        file.registerOption("FONTS", new IntOption("SCORE_FONT_SIZE", 48,
                "The size of the font used for the score counter"));

        if (!file.readFromFile()) file.writeToFile();

        Constants.CORNER_SIDES = file.getIntOption("TILES", "CORNER_SIDES").getValue();
        Constants.TILE_SPACE = file.getFloatOption("TILES", "TILE_SPACE").getValue();
        Constants.RADII_DIV = file.getFloatOption("TILES", "RADII_DIV").getValue();
        Constants.TILE_SPEED = file.getFloatOption("TILES", "TILE_SPEED").getValue();

        Constants.BG_SPACE = file.getFloatOption("BG", "BG_SPACE").getValue();
        Constants.BORDER = file.getFloatOption("BG", "BORDER").getValue();

        Constants.MENU_FONT = SystemFont.getFont(
                file.getStringOption("FONTS", "MENU_FONT_NAME").getValue(),
                file.getIntOption("FONTS", "MENU_FONT_SIZE").getValue(),
                SystemFont.BOLD, true
        );

        Constants.TILE_FONT = SystemFont.getFont(
                file.getStringOption("FONTS", "TILE_FONT_NAME").getValue(),
                file.getIntOption("FONTS", "TILE_FONT_SIZE").getValue(),
                SystemFont.BOLD, true
        );

        Constants.SCORE_FONT = SystemFont.getFont(
                file.getStringOption("FONTS", "SCORE_FONT_NAME").getValue(),
                file.getIntOption("FONTS", "SCORE_FONT_SIZE").getValue(),
                SystemFont.BOLD, true
        );
    }
}
