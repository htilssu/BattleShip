import com.htilssu.entity.player.Player;
import com.htilssu.entity.player.PlayerBoard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
       

    }

}
