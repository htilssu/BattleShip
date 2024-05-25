package com.htilssu.listener;

import com.htilssu.entity.Player;
import com.htilssu.component.Position;

public interface Listener {
    void onPlayerShoot(Player player, Position position);
}
