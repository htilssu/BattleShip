package com.htilssu.event.player;

import com.htilssu.entity.component.Position;
import com.htilssu.entity.player.Player;
import com.htilssu.entity.player.PlayerBoard;

/**
 * Sự kiện người chơi thực hiện hành động bắn tàu đối phương, chứa thông tin của người chơi ({@link
 * Player}) và {@link Position} là vị trí bắn
 */
public class PlayerShootEvent extends PlayerEvent {

    Position position;
    PlayerBoard board;

    /**
     * Khởi tạo sự kiện bắn của người chơi
     *
     * @param player   Người chơi thực hiện hành động bắn
     * @param position Vị trí bắn
     */
    public PlayerShootEvent(Player player, PlayerBoard playerBoard, Position position) {
        super(player);
        this.position = position;
        board = playerBoard;

    }

    public PlayerBoard getBoard() {
        return board;
    }

    public Position getPosition() {
        return position;
    }
}
