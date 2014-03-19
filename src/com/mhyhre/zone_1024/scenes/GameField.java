package com.mhyhre.zone_1024.scenes;

import java.util.ArrayList;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.game.logic.GameManager;
import com.mhyhre.zone_1024.game.logic.SimpleGrid;
import com.mhyhre.zone_1024.game.logic.SimpleTile;
import com.mhyhre.zone_1024.game.logic.Tile;
import com.mhyhre.zone_1024.utils.Position;
import com.mhyhre.zone_1024.utils.Size;
import com.mhyhre.zone_1024.utils.TileColors;


public class GameField extends SimpleScene {
    
    private final String cellsRegionName = "EquipmentCell";
    private final int BETWEEN_CELLS_SIZE = 4;

    private Rectangle fieldRect;
    private SpriteBatch tilesSpriteBatch;
    private ArrayList<Text> cellsTextEntityList;
    private TileColors tileColors;

    private Size size;
    
    
    public GameField(Size size) {
        setBackgroundEnabled(false);
        show();
        
        this.size = size;
        tileColors = TileColors.getInstance();
        
        ITextureRegion cellRegion = MainActivity.resources.getTextureRegion(cellsRegionName);
        
        // Generate background rectangle
        int backgroundSizeX = ((int) cellRegion.getWidth()) * size.getWidth();
        int backgroundSizeY = ((int) cellRegion.getHeight()) * size.getHeight();
        fieldRect = new Rectangle(MainActivity.getHalfWidth(), MainActivity.getHalfHeight(), backgroundSizeX, backgroundSizeY, MainActivity.getVboManager());
        fieldRect.setColor(0.73f, 0.68f, 0.62f);
        attachChild(fieldRect);

        createTilesGraphics();
    }
    

    private void createTilesGraphics() {

        tilesSpriteBatch = new SpriteBatch(MainActivity.resources.getTextureAtlas("User_Interface"), size.getWidth() * size.getHeight(),
                MainActivity.getVboManager());
        tilesSpriteBatch.setPosition(MainActivity.getHalfWidth() - ((fieldRect.getWidth() / 2) - BETWEEN_CELLS_SIZE), MainActivity.getHalfHeight()
                - ((fieldRect.getHeight() / 2) - BETWEEN_CELLS_SIZE));
        attachChild(tilesSpriteBatch);

        // Create cells label
        int textCount = size.getWidth() * size.getHeight();
        cellsTextEntityList = new ArrayList<Text>(textCount);

        IFont textFont = MainActivity.resources.getFont("White Furore");
        for (int i = 0; i < textCount; i++) {
            Text text = new Text(0, 0, textFont, "0", 8, MainActivity.getVboManager());
            attachChild(text);
            text.setVisible(false);
            cellsTextEntityList.add(text);
        }

    }

    public void update(SimpleGrid grid) {
        
        if(grid == null) {
            return;
        }

        ITextureRegion cellRegion = MainActivity.resources.getTextureRegion(cellsRegionName);
        Size cellsOffset = new Size((int) cellRegion.getWidth() + BETWEEN_CELLS_SIZE, (int) cellRegion.getHeight() + BETWEEN_CELLS_SIZE);
        Position currentCell = new Position(0, 0);

        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getWidth(); y++) {

                currentCell.set(x, y);
                SimpleTile tile = grid.getTile(currentCell);

                if (tile != null) {
                    Color cellColor = tileColors.getColorByCellValue(tile.getValue());

                    float cellX = x * cellsOffset.getWidth();
                    float cellY = y * cellsOffset.getHeight();
                    tilesSpriteBatch.draw(cellRegion, cellX, cellY, cellRegion.getWidth(), cellRegion.getHeight(), 0, cellColor.getRed(), cellColor.getGreen(),
                            cellColor.getBlue(), 1);

                    Text cellText = cellsTextEntityList.get(y * size.getWidth() + x);
                    if (tile.getValue() == 0) {
                        cellText.setVisible(false);
                    } else {
                        cellText.setText("" + tile.getValue());
                        cellText.setColor(0, 0, 0);
                        cellText.setPosition(tilesSpriteBatch.getX() + cellX + cellRegion.getWidth() / 2,
                                tilesSpriteBatch.getY() + cellY + cellRegion.getWidth() / 2);
                        cellText.setVisible(true);
                    }
                }
            }
        }
        tilesSpriteBatch.submit();
    }

}
