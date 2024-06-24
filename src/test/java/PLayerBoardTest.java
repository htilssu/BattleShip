import com.htilssu.entity.Ship;
import com.htilssu.entity.Sprite;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.player.Player;
import com.htilssu.entity.player.PlayerBoard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

@DisplayName("Test PlayerBoard")
public class PLayerBoardTest {

    static PlayerBoard playerBoard;

    @BeforeAll
    public static void setUp() {
        playerBoard = new PlayerBoard(10, new Player("Player 1"));
        playerBoard.setSize(100, 100);
        playerBoard.update();
    }

    @Test
    @DisplayName("Test add ship")
    public void testAddShip() {
        playerBoard.addShip(
                new Ship(Ship.VERTICAL, new Sprite(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)),
                         new Position(0, 0), Ship.SHIP_2));


        Assertions.assertFalse(playerBoard.canAddShip(
                                       new Ship(Ship.VERTICAL, new Sprite(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)),
                                                new Position(0, 0), Ship.SHIP_2)),
                               "Should not add ship");
        Assertions.assertFalse(playerBoard.canAddShip(
                                       new Ship(Ship.VERTICAL, new Sprite(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)),
                                                new Position(0, 2), Ship.SHIP_2)),
                               "Should not add ship");
        Assertions.assertTrue(playerBoard.canAddShip(
                                      new Ship(Ship.VERTICAL, new Sprite(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)),
                                               new Position(0, 6), Ship.SHIP_2)),
                              "Can Add Ship");

    }

}
