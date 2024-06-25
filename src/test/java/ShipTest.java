import com.htilssu.entity.Ship;
import com.htilssu.entity.Sprite;
import com.htilssu.entity.component.Position;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ShipTest {

    @Test
    public void testSetHorizontalToVerticalDirection() {
        BufferedImage image = new BufferedImage(20, 10, BufferedImage.TYPE_INT_ARGB);
        Ship ship = new Ship(Ship.HORIZONTAL, new Sprite(image), new Position(0, 0), Ship.SHIP_2);
        int width = ship.getSprite().getAsset().getWidth();
        int height = ship.getSprite().getAsset().getHeight();
        assertEquals(20, width);
        assertEquals(10, height);
        assertEquals(20, ship.getSprite().getWidth());
        assertEquals(10, ship.getSprite().getHeight());
        ship.setDirection(Ship.VERTICAL);
        width = ship.getSprite().getAsset().getWidth();
        height = ship.getSprite().getAsset().getHeight();
        assertEquals(10, width);
        assertEquals(20, height);
        assertEquals(10, ship.getSprite().getWidth());
        assertEquals(20, ship.getSprite().getHeight());
    }

    @Test
    public void testSetVerticalToHorizontalDirection() {
        BufferedImage image = new BufferedImage(10, 20, BufferedImage.TYPE_INT_ARGB);
        Ship ship = new Ship(Ship.VERTICAL, new Sprite(image), new Position(0, 0), Ship.SHIP_2);
        int width = ship.getSprite().getAsset().getWidth();
        int height = ship.getSprite().getAsset().getHeight();
        assertEquals(10, width);
        assertEquals(20, height);
        ship.setDirection(Ship.HORIZONTAL);
        width = ship.getSprite().getAsset().getWidth();
        height = ship.getSprite().getAsset().getHeight();
        assertNotEquals(10, width);
        assertNotEquals(20, height);
        assertNotEquals(10, ship.getSprite().getWidth());
        assertNotEquals(20, ship.getSprite().getHeight());
    }
}
