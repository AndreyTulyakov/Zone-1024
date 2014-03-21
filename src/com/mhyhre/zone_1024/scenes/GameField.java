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
import com.mhyhre.zone_1024.game.logic.GameManager;
import com.mhyhre.zone_1024.game.logic.SimpleGrid;
import com.mhyhre.zone_1024.game.logic.SimpleTile;
import com.mhyhre.zone_1024.utils.Position;
import com.mhyhre.zone_1024.utils.Size;
import com.mhyhre.zone_1024.utils.TileColors;


public class GameField extends SimpleScene {
    
    private final String cellsRegionName = "EquipmentCell";
    private final ITextureRegion cellRegion;
    private final int BETWEEN_CELLS_SIZE = 4;
    private final Size cellsOffset;

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
        
        cellRegion = MainActivity.resources.getTextureRegion(cellsRegionName);
        cellsOffset = new Size((int) cellRegion.getWidth() + BETWEEN_CELLS_SIZE, (int) cellRegion.getHeight() + BETWEEN_CELLS_SIZE);
        
        // Generate background rectangle
        int backgroundSizeX = ((int) cellRegion.getWidth()+BETWEEN_CELLS_SIZE) * size.getWidth() - BETWEEN_CELLS_SIZE/2;
        int backgroundSizeY = ((int) cellRegion.getHeight()+BETWEEN_CELLS_SIZE) * size.getHeight() - BETWEEN_CELLS_SIZE/2;
        fieldRect = new Rectangle(MainActivity.getHalfWidth(), MainActivity.getHalfHeight(), backgroundSizeX, backgroundSizeY, MainActivity.getVboManager());
        fieldRect.setColor(0.3f, 0.3f, 0.3f);
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
        
        hideCellsText();

        List<SimpleTile> tiles = grid.getAllTiles();
        int counter = 0;
        
        for(SimpleTile tile: tiles) {
            drawCell(tile.getX(), tile.getY(), tile.getValue(), tile.getZoom(), counter);
            counter++;
        }
        
        tilesSpriteBatch.submit();
    }
    
   
    private void drawCell(float x, float y, int value, float zoom, int counter) {
        
        Color cellColor = tileColors.getColorByCellValue(value);

        float cellX = x * cellsOffset.getWidth() + BETWEEN_CELLS_SIZE;
        float cellY = y * cellsOffset.getHeight() + BETWEEN_CELLS_SIZE;
        
        // Draw cells texture
        tilesSpriteBatch.draw(cellRegion, cellX, cellY, cellRegion.getWidth(), cellRegion.getHeight(), 0, zoom, zoom, cellColor.getRed(), cellColor.getGreen(), cellColor.getBlue(), 1);
     
        // Draw cells text
        Text cellText = cellsTextEntityList.get(counter);
        cellText.setText("" + value);
        cellText.setColor(0, 0, 0);
        cellText.setPosition(tilesSpriteBatch.getX() + cellX + cellRegion.getWidth() / 2,
                tilesSpriteBatch.getY() + cellY + cellRegion.getWidth() / 2);
        cellText.setVisible(true);
    }
    
    private void hideCellsText() {
        for(Text text: cellsTextEntityList) {
            text.setVisible(false);
        }
    }
}
