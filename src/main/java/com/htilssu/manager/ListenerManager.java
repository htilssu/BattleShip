package com.htilssu.manager;

import com.htilssu.event.EventHandler;
import com.htilssu.event.GameEvent;
import com.htilssu.listener.Listener;
import com.htilssu.util.GameLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

public class ListenerManager {
    HashMap<Class<?>, List<Method>> eventHandler = new HashMap<>();

    public void registerEvent(Listener listener) {
        Method[] method = listener.getClass().getMethods();
        for (Method med : method) {
            if (med.isAnnotationPresent(EventHandler.class)) {
                Class<?>[] parameterTypes = med.getParameterTypes();
                if (parameterTypes.length > 0 && parameterTypes[0].getSuperclass() != null) {
                    Class<?> eventType = parameterTypes[0];
                    eventHandler.computeIfAbsent(eventType, k -> new ArrayList<>()).add(med);
                }
            }
        }
    }

    public void callEvent(GameEvent event, Object... params) {
        Class<?> eventClass = event.getClass();
        List<Method> handlers = eventHandler.get(eventClass);

        if (handlers != null) {
            for (Method handler : handlers) {
                Object[] objectParam = new Object[handler.getParameterCount()];

                params[0] = event;
                System.arraycopy(params, 0, objectParam, 1, objectParam.length - 1);

                try {
                    handler.invoke(handler.getDeclaringClass().getDeclaredConstructor().newInstance(), objectParam);
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                         InvocationTargetException e) {
                    GameLogger.log(e.getMessage());
                }
            }
        }
    }
}
