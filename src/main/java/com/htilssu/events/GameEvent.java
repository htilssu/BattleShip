package com.htilssu.events;

public abstract class GameEvent {
    public abstract void execute();

    public abstract boolean check();
}
