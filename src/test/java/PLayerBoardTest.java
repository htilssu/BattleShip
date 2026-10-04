import com.htilssu.entity.Ship;
import com.htilssu.entity.Sprite;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.player.Player;
import com.htilssu.entity.player.PlayerBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test PlayerBoard")
public class PLayerBoardTest {

    PlayerBoard playerBoard;

    @BeforeEach
    public void setUp() {
        playerBoard = new PlayerBoard(10, new Player("Player 1"));
        playerBoard.setSize(100, 100);
        playerBoard.update();
    }

    @Test
    @DisplayName("Stores every occupied cell of a horizontal ship")
    public void testAddShip() {
        Ship ship = createShip(Ship.HORIZONTAL, new Position(2, 3), Ship.SHIP_4);

        playerBoard.addShip(ship);

        assertSame(ship, playerBoard.getShipAtPosition(new Position(2, 3)));
        assertSame(ship, playerBoard.getShipAtPosition(new Position(5, 3)));
        assertNull(playerBoard.getShipAtPosition(new Position(6, 3)));
    }

    @Test
    @DisplayName("Rejects overlapping and out-of-board ships")
    public void testRejectInvalidShipPlacement() {
        playerBoard.addShip(createShip(Ship.VERTICAL, new Position(4, 4), Ship.SHIP_3));

        assertFalse(playerBoard.canAddShip(createShip(Ship.HORIZONTAL,
                new Position(3, 5), Ship.SHIP_4)));
        assertFalse(playerBoard.canAddShip(createShip(Ship.HORIZONTAL,
                new Position(8, 0), Ship.SHIP_4)));
        assertFalse(playerBoard.canAddShip(createShip(Ship.VERTICAL,
                new Position(0, 8), Ship.SHIP_4)));
    }

    @Test
    @DisplayName("Clears occupied cells when a ship is removed")
    public void testRemoveShip() {
        Ship ship = createShip(Ship.HORIZONTAL, new Position(1, 1), Ship.SHIP_3);
        playerBoard.addShip(ship);

        playerBoard.removeShip(ship);

        assertNull(playerBoard.getShipAtPosition(new Position(1, 1)));
        assertNull(playerBoard.getShipAtPosition(new Position(3, 1)));
        assertTrue(playerBoard.canAddShip(createShip(Ship.HORIZONTAL,
                new Position(1, 1), Ship.SHIP_3)));
    }

    @Test
    @DisplayName("Copies the ship occupancy index")
    public void testCopyBoard() {
        Ship ship = createShip(Ship.VERTICAL, new Position(7, 2), Ship.SHIP_3);
        playerBoard.addShip(ship);
        playerBoard.markShipDestroyed(ship);

        PlayerBoard copy = new PlayerBoard(playerBoard);

        assertNotNull(copy.getShipAtPosition(new Position(7, 2)));
        assertNotNull(copy.getShipAtPosition(new Position(7, 4)));
        assertTrue(copy.getShipAtPosition(new Position(7, 2)).isSunk());
        assertNotSame(playerBoard.getShipAtPosition(new Position(7, 2)),
                copy.getShipAtPosition(new Position(7, 2)));
    }

    @Test
    @DisplayName("Registers a revealed ship before marking it destroyed")
    public void testMarkUnregisteredShipDestroyed() {
        Ship ship = createShip(Ship.HORIZONTAL, new Position(2, 4), Ship.SHIP_3);

        playerBoard.markShipDestroyed(ship);

        assertSame(ship, playerBoard.getShipAtPosition(new Position(2, 4)));
        assertTrue(ship.isSunk());
    }

    @Test
    @DisplayName("Renders a destroyed ship above the destroyed-cell overlay")
    public void testRenderDestroyedShip() {
        BufferedImage shipImage = new BufferedImage(30, 10, BufferedImage.TYPE_INT_ARGB);
        var shipGraphics = shipImage.createGraphics();
        shipGraphics.setColor(Color.GREEN);
        shipGraphics.fillRect(0, 0, shipImage.getWidth(), shipImage.getHeight());
        shipGraphics.dispose();

        Ship ship = new Ship(Ship.HORIZONTAL, new Sprite(shipImage),
                new Position(2, 4), Ship.SHIP_3);
        playerBoard.markShipDestroyed(ship);

        BufferedImage output = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        playerBoard.render(output.getGraphics(), false);

        assertEquals(Color.GREEN.getRGB(), output.getRGB(25, 45));
    }

    @Test
    @DisplayName("Renders a revealed destroyed ship even when it cannot enter the occupancy index")
    public void testRenderDetachedDestroyedShip() {
        playerBoard.addShip(createShip(Ship.VERTICAL, new Position(3, 3), Ship.SHIP_2));
        BufferedImage shipImage = new BufferedImage(30, 10, BufferedImage.TYPE_INT_ARGB);
        var shipGraphics = shipImage.createGraphics();
        shipGraphics.setColor(Color.GREEN);
        shipGraphics.fillRect(0, 0, shipImage.getWidth(), shipImage.getHeight());
        shipGraphics.dispose();
        Ship revealedShip = new Ship(Ship.HORIZONTAL, new Sprite(shipImage),
                new Position(2, 3), Ship.SHIP_3);

        playerBoard.markShipDestroyed(revealedShip);
        BufferedImage output = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        playerBoard.render(output.getGraphics(), false);

        assertTrue(revealedShip.isSunk());
        assertEquals(Color.GREEN.getRGB(), output.getRGB(25, 35));
    }

    @Test
    @DisplayName("Restores ship size after it was added before board layout")
    public void testShipSizeAfterBoardLayout() {
        PlayerBoard board = new PlayerBoard(10, new Player("Player 1"));
        Ship ship = createShip(Ship.HORIZONTAL, new Position(1, 1), Ship.SHIP_3);
        board.addShip(ship);
        assertEquals(0, ship.getSprite().getWidth());

        board.setSize(100, 100);
        board.update();

        assertEquals(30, ship.getSprite().getWidth());
        assertEquals(10, ship.getSprite().getHeight());
    }

    private Ship createShip(int direction, Position position, int length) {
        BufferedImage image = direction == Ship.HORIZONTAL
                ? new BufferedImage(length * 10, 10, BufferedImage.TYPE_INT_ARGB)
                : new BufferedImage(10, length * 10, BufferedImage.TYPE_INT_ARGB);
        return new Ship(direction, new Sprite(image), position, length);
    }
}
