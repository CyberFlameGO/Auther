package me.hugmanrique.auther.plugin.util;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.event.AsyncEvent;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.netty.ChannelWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Hugo Manrique
 * @since 01/04/2018
 */
public class ReflectionUtil {
    private static final Field CHANNEL_WRAPPER_FIELD;
    private static final Method FINISH_METHOD;
    private static final Field CALLBACK_FIELD;

    static {
        try {
            CHANNEL_WRAPPER_FIELD = getField(InitialHandler.class, "ch");
            FINISH_METHOD = InitialHandler.class.getDeclaredMethod("finish");
            FINISH_METHOD.setAccessible(true);

            CALLBACK_FIELD = getField(AsyncEvent.class, "done");
            Field modifiersField = getField(Field.class, "modifiers");

            modifiersField.set(CALLBACK_FIELD, CALLBACK_FIELD.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException  e) {
            throw new RuntimeException("Cannot find ChannelWrapper field", e);
        }
    }

    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);

        return field;
    }

    public static ChannelWrapper getChannelWrapper(InitialHandler initialHandler) {
        try {
            return (ChannelWrapper) CHANNEL_WRAPPER_FIELD.get(initialHandler);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean finishLoginRequest(InitialHandler initialHandler) {
        try {
            FINISH_METHOD.invoke(initialHandler);
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static <T> boolean setEventCallback(AsyncEvent<T> event, Callback<T> callback) {
        try {
            CALLBACK_FIELD.set(event, callback);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }
}
