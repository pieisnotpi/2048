# 2048

A small java port of 2048. Based on the Pi-Engine. 

This version of 2048 is fully featured and freshly made, featuring a simple but sleek visual aesthetic.
With this rendition of 2048, the game can be customized with fully flexible board sizes, from 2x2 to 16x9 to 20x20, and everything in between. The game's color palette is also fully customizable using theme files, allowing for the warm default palette to be changed into anything else, such as the much cooler palette shown below. 

Not enough? Fair enough. By downloading the source code, the game's visuals can be customized extensively more. Because the board's visual component is mathematically generated, a quick visit to the Constants class allows the board's appearance to be modified extensively, allowing for more or less rounded (or even circular) tiles, larger or smaller gaps between tiles, etc. With more source code exploration, anything and everything can be tweaked. 

The theme files assign hexadecimal values to specific numerical tile values. For the background, the main background color is bg, the board color is bp, and the grid color is bt.

Requires LWJGL 3.1.2 build 13, JOML 1.9.0, and the latest Pi-Engine build, which are included in the release versions

![2048 Red](https://i.imgur.com/nlvSmic.png "2048 Red")
![2048 Blue](https://i.imgur.com/14jjlrU.png "2048 Blue")
