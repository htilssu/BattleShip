import com.htilssu.entity.Ship;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.entity.player.Bot;
import com.htilssu.entity.player.Player;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamePlaySinglePlayerTest {

    @Test
    public void botFleetUsesTheSharedPlayerBoardModel() {
        Player human = new Player("Human");
        Bot bot = new Bot("Computer");
        GamePlay gamePlay = new GamePlay(List.of(human, bot), 0, 10, false);

        gamePlay.prepareSinglePlayer();

        Set<Ship> ships = Collections.newSetFromMap(new IdentityHashMap<>());
        int occupiedCells = 0;
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Ship ship = bot.getBoard().getShipAtPosition(new Position(x, y));
                if (ship != null) {
                    ships.add(ship);
                    occupiedCells++;
                }
            }
        }

        assertEquals(5, ships.size());
        assertEquals(17, occupiedCells);
    }
}
