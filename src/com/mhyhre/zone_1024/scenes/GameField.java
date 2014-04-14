package com.mhyhre.zone_1024.scenes;

import java.util.ArrayList;
import java.util.List;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.game.logic.DemonBot;
import com.mhyhre.zone_1024.game.logic.DemonBot.Intention;
import com.mhyhre.zone_1024.game.logic.Grid;
import com.mhyhre.zone_1024.game.logic.SimpleTile;
import com.mhyhre.zone_1024.utils.Size;
import com.mhyhre.zone_1024.utils.TileColors;

public class GameField extends SimpleScene {

    private final ITextureRegion cellRegion;

    private final int BETWEEN_CELLS_SIZE = 2;
    private final Size cellsOffset;

    private Rectangle fieldRect;
    private SpriteBatch tilesSpriteBatch;
    private ArrayList<Text> cellsTextEntityList;
    private TileColors tileColors;
    private ITextureRegion demonRegion;
    
    private float demonAnimTime = 0;
    private final float demonFrameDureation = 1.0f;
    private boolean demonSleepAnimSwitcher = false;

    private Size size;

    public GameField(Size size) {
        setBackgroundEnabled(false);
        show();

        this.size = size;
        tileColors = TileColors.getInstance();

        demonRegion = null;

        // Select regions
        cellRegion = MainActivity.resources.getTextureRegion("Cell");

        cellsOffset = new Size((int) cellRegion.getWidth() + BETWEEN_CELLS_SIZE, (int) cellRegion.getHeight() + BETWEEN_CELLS_SIZE);

        // Generate background rectangle
        fieldRect = new Rectangle(MainActivity.getHalfWidth(), MainActivity.getHalfHeight(), MainActivity.getWidth(), MainActivity.getWidth(),
                MainActivity.getVboManager());
        fieldRect.setColor(0.6f, 0.6f, 0.6f);
        attachChild(fieldRect);

        createTilesGraphics();
    }

    private void createTilesGraphics() {

        tilesSpriteBatch = new SpriteBatch(MainActivity.resources.getTextureAtlas("Cells"), size.getWidth() * size.getHeight(), MainActivity.getVboManager());
        tilesSpriteBatch.setPosition(MainActivity.getHalfWidth() - ((fieldRect.getWidth() / 2) - BETWEEN_CELLS_SIZE), MainActivity.getHalfHeight()
                - ((fieldRect.getHeight() / 2) - BETWEEN_CELLS_SIZE));
        attachChild(tilesSpriteBatch);

        // Create cells label
        int textCount = size.getWidth() * size.getHeight();
        cellsTextEntityList = new ArrayList<Text>(textCount);

        IFont textFont = MainActivity.resources.getFont("WhiteMono32");

        for (int i = 0; i < textCount; i++) {
            Text text = new Text(0, 0, textFont, "0", 8, MainActivity.getVboManager());
            attachChild(text);
            text.setVisible(false);
            text.setColor(0, 0, 0);
            cellsTextEntityList.add(text);
        }

    }

    public void update(Grid grid) {

        if (grid == null) {
            return;
        }

        hideCellsText();

        List<SimpleTile> tiles = grid.getAllTiles();
        int counter = 0;

        DemonBot demon = grid.getDemon();
        if (demon.isWasChanged()) {
            demonRegion = MainActivity.resources.getTextureRegion("DemonMoving");
        } else {
            if (demon.getBehaviorIntention() == Intention.NONE) {
                demonRegion = MainActivity.resources.getTextureRegion(demonSleepAnimSwitcher ? "DemonSleep0" : "DemonSleep1");
            } else {
                switch (demon.getIntentionDirection()) {
                case DOWN:
                    demonRegion = MainActivity.resources.getTextureRegion("DemonDown");
                    break;

                case LEFT:
                    demonRegion = MainActivity.resources.getTextureRegion("DemonLeft");
                    break;

                case RIGHT:
                    demonRegion = MainActivity.resources.getTextureRegion("DemonRight");
                    break;

                case UP:
                    demonRegion = MainActivity.resources.getTextureRegion("DemonUp");
                    break;

                }
            }
        }

        for (SimpleTile tile : tiles) {
            drawCell(tile.getX(), tile.getY(), tile.getValue(), tile.getZoom(), counter);
            counter++;
        }

        tilesSpriteBatch.submit();
    }

    private void drawCell(float x, float y, int value, float zoom, int counter) {

        Color cellColor = tileColors.getColorByCellValue(value);

        float cellX = 4 + (x * (cellsOffset.getWidth() + BETWEEN_CELLS_SIZE)) + BETWEEN_CELLS_SIZE;
        float cellY = 4 + (y * (cellsOffset.getHeight() + BETWEEN_CELLS_SIZE)) + BETWEEN_CELLS_SIZE;

        // Draw cells texture
        if (value == Grid.DEMON_VALUE) {

            tilesSpriteBatch.draw(demonRegion, cellX, cellY, cellRegion.getWidth(), cellRegion.getHeight(), 0, zoom, zoom, cellColor.getRed(),
                    cellColor.getGreen(), cellColor.getBlue(), 1);
        } else {
            tilesSpriteBatch.draw(cellRegion, cellX, cellY, cellRegion.getWidth(), cellRegion.getHeight(), 0, zoom, zoom, cellColor.getRed(),
                    cellColor.getGreen(), cellColor.getBlue(), 1);
        }

        // Draw cells text
        Text cellText = cellsTextEntityList.get(counter);
        cellText.setText("" + value);
        cellText.setVisible(true);

        if (value == Grid.DEMON_VALUE) {
            cellText.setVisible(false);
        }

        cellText.setColor(0, 0, 0);
        cellText.setPosition(tilesSpriteBatch.getX() + cellX + cellRegion.getWidth() / 2, tilesSpriteBatch.getY() + cellY + cellRegion.getWidth() / 2);

    }

    private void hideCellsText() {
        for (Text text : cellsTextEntityList) {
            text.setVisible(false);
        }
    }
    
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        demonAnimTime += pSecondsElapsed;
        
        if(demonAnimTime > demonFrameDureation) {
            demonAnimTime = 0;
            demonSleepAnimSwitcher = !demonSleepAnimSwitcher;
        }
        
        super.onManagedUpdate(pSecondsElapsed);
    }
}
