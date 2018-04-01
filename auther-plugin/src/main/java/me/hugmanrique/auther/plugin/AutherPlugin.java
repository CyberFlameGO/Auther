package me.hugmanrique.auther.plugin;

import lombok.Getter;
import lombok.Setter;
import me.hugmanrique.auther.AuthDeterminer;
import me.hugmanrique.auther.Auther;
import me.hugmanrique.auther.plugin.util.ReflectionUtil;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Hugo Manrique
 * @since 01/04/2018
 */
public class AutherPlugin extends Plugin implements Auther {
    private static final Callback<PreLoginEvent> UNAUTHENTICATED_CALLBACK = new UnauthenticatedCallback();
    private static final BaseComponent[] REPLACE_CALLBACK_ERROR = TextComponent.fromLegacyText("Error while replacing PreLoginEvent callback");

    @Getter
    @Setter
    private AuthDeterminer determiner;

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(
        this,
            new PreLoginListener(this)
        );
    }

    void replaceEventCallback(PreLoginEvent event) {
        boolean success = ReflectionUtil.setEventCallback(event, UNAUTHENTICATED_CALLBACK);

        if (!success) {
            event.setCancelled(true);
            event.setCancelReason(REPLACE_CALLBACK_ERROR);
        }
    }
}
