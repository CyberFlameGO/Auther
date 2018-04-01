package me.hugmanrique.auther.plugin;

import lombok.RequiredArgsConstructor;
import me.hugmanrique.auther.AuthDeterminer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author Hugo Manrique
 * @since 01/04/2018
 */
@RequiredArgsConstructor
public class PreLoginListener implements Listener {
    private final AutherPlugin plugin;

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        AuthDeterminer determiner = plugin.getDeterminer();
        PendingConnection connection = event.getConnection();

        if (determiner != null && !determiner.shouldAuthenticate(connection)) {
            plugin.replaceEventCallback(event);
        }
    }
}
