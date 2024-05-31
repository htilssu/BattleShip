package com.htilssu.event.player;

import com.htilssu.component.Position;
import com.htilssu.entity.player.Player;

/**
 * Sự kiện người chơi thực hiện hành động bắn tàu đối phương, chứa thông tin của người chơi ({@link
 * Player}) và {@link Position} là vị trí bắn
 */
public class PlayerShootEvent extends PlayerEvent {

  boolean isEnemy;
  Position position;
  Player targetPlayer;
  /**
   * Khởi tạo sự kiện bắn của người chơi
   *
   * @param player Người chơi thực hiện hành động bắn
   * @param position Vị trí bắn
   */
  public PlayerShootEvent(Player playerShoot, Player targetPlayer, Position position) {
    super(playerShoot);
    this.targetPlayer = targetPlayer;
    this.position = position;
  }

  public PlayerShootEvent(
      Player playerShoot, Player targetPlayer, Position position, boolean isEnemy) {
    this(playerShoot, targetPlayer, position);
    this.isEnemy = isEnemy;
  }

  public boolean isEnemy() {
    return isEnemy;
  }

  public Player getTargetPlayer() {
    return targetPlayer;
  }

  public Position getPosition() {
    return position;
  }
}
