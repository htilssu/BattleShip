package com.htilssu.event.player;

import com.htilssu.component.Position;
import com.htilssu.entity.player.Player;

/**
 * Sự kiện người chơi thực hiện hành động bắn tàu đối phương, chứa thông tin của người chơi ({@link
 * Player}) và {@link Position} là vị trí bắn
 */
public class PlayerAttackEvent extends PlayerEvent {

    Position position;

    /**
     * Khởi tạo sự kiện bắn của người chơi
     *
     * @param player Người chơi thực hiện hành động bắn
     * @param position Vị trí bắn
     */
    public PlayerAttackEvent(Player player, Position position) {
        super(player);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
