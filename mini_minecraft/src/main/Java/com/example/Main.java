package com.example;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLImageElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.html.HTMLButtonElement;
import org.teavm.jso.dom.events.MouseEvent;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

public class Main {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int BLOCK_SIZE = 20;

    private static final int BLOCK_GRASS = 1;
    private static final int BLOCK_DIRT = 2;
    private static final int BLOCK_STONE = 3;

    private static int[][] terrain;
    private static CanvasRenderingContext2D ctx;

    public static void main(String[] args) {
        HTMLDocument document = HTMLDocument.current();
        HTMLCanvasElement canvas = (HTMLCanvasElement) document.createElement("canvas");
        canvas.setWidth(SCREEN_WIDTH);
        canvas.setHeight(SCREEN_HEIGHT);
        document.getBody().appendChild(canvas);

        ctx = (CanvasRenderingContext2D) canvas.getContext("2d");

        terrain = generateTerrain(SCREEN_WIDTH / BLOCK_SIZE, SCREEN_HEIGHT / BLOCK_SIZE);

        canvas.addEventListener("click", Main::handleClick);

        drawTerrain();
    }

    private static int[][] generateTerrain(int width, int height) {
        int[][] terrain = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y < height - 10) {
                    terrain[y][x] = BLOCK_STONE;
                } else if (y < height - 5) {
                    terrain[y][x] = BLOCK_DIRT;
                } else {
                    terrain[y][x] = BLOCK_GRASS;
                }
            }
        }
        return terrain;
    }

    private static void drawTerrain() {
        for (int y = 0; y < terrain.length; y++) {
            for (int x = 0; x < terrain[y].length; x++) {
                int blockType = terrain[y][x];
                String color = "gray";
                if (blockType == BLOCK_GRASS) {
                    color = "green";
                } else if (blockType == BLOCK_DIRT) {
                    color = "brown";
                }
                ctx.setFillStyle(color);
                ctx.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }

    private static void handleClick(MouseEvent event) {
        int x = event.getClientX();
        int y = event.getClientY();
        int blockX = x / BLOCK_SIZE;
        int blockY = y / BLOCK_SIZE;

        if (event.getButton() == 0) { // Left click to break block
            if (terrain[blockY][blockX] != 0) {
                terrain[blockY][blockX] = 0;
            }
        } else if (event.getButton() == 2) { // Right click to place block
            if (terrain[blockY][blockX] == 0) {
                terrain[blockY][blockX] = BLOCK_GRASS;
            }
        }

        drawTerrain();
    }
}
