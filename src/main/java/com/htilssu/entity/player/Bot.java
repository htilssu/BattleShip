package com.htilssu.entity.player;

import com.htilssu.entity.component.Position;

public class Bot extends Player {

  public static final int INTEL = 2;
  private static final int RANDOM = 1;
  int botType = RANDOM;

  public Bot(String id, String name) {
    super(id, name);
  }

  public Bot(String name) {
    super(name);
  }

  public Position getNextShoot() {
    if (botType == RANDOM) return randomShoot();

    // TODO: return itel shoot
    return null;
  }

  private Position randomShoot() {
    int x = 0;
    int y = 0;
    x = (int) (Math.random() * getBoard().getCellSize());
    Position pos = new Position(x, y);

    /* while (canShoot(pos)) {
      x = random.nextInt(0, shot.length);
      y = random.nextInt(0, shot[0].length);
      pos = new Position(x, y);
    }*/
    return pos;
  }

  private Position calculateShoot() {
    // TODO: intel shoot

    return null;
  }
}
