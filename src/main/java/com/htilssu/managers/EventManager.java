package com.htilssu.managers;

import com.htilssu.events.GameEvent;

import java.util.EventListener;
import java.util.LinkedList;

public class EventManager {
    LinkedList<GameEvent> gameEvents = new LinkedList<>();

    /**
     * Kiểm tra các sự kiện trong game nếu 1 sự kiện xảy ra thì thực thi
     */
    public void checkEvents() {
        for (GameEvent event : gameEvents) {
            if (event.check()) {
                event.execute();
            }
        }
    }

    public void registerEvent(GameEvent event) {
        gameEvents.add(event);
    }
}
