package me.hugmanrique.auther.plugin;

import me.hugmanrique.auther.plugin.util.ReflectionUtil;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.netty.ChannelWrapper;

/**
 * @author Hugo Manrique
 * @since 01/04/2018
 */
public class UnauthenticatedCallback implements Callback<PreLoginEvent> {
    private static final BaseComponent[] FINISH_ERROR_MESSAGE = TextComponent.fromLegacyText("Error finishing login request");

    @Override
    public void done(PreLoginEvent result, Throwable throwable) {
        InitialHandler initialHandler = (InitialHandler) result.getConnection();

        if (result.isCancelled()) {
            initialHandler.disconnect(result.getCancelReasonComponents());
            return;
        }

        ChannelWrapper ch = ReflectionUtil.getChannelWrapper(initialHandler);

        if (ch == null || ch.isClosed()) {
            return;
        }

        boolean success = ReflectionUtil.finishLoginRequest(initialHandler);

        if (!success) {
            initialHandler.disconnect(FINISH_ERROR_MESSAGE);
        }
    }
}
