package com.htilssu.entity.player;

import java.util.UUID;

public class Player {
    String name;
    UUID id = UUID.randomUUID();

    public String getName() {
        return name;
    }

    public String getId() {
        return id.toString();
    }
}
