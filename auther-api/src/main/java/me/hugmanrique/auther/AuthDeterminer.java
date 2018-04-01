package me.hugmanrique.auther;

import net.md_5.bungee.api.connection.PendingConnection;

/**
 * @author Hugo Manrique
 * @since 01/04/2018
 */
public interface AuthDeterminer {
    /**
     * Determines whether a pending connection should perform the complete
     * login process (authenticating the client with Mojang) or
     * directly jump to the Login Success phase.
     * @see <a href="http://wiki.vg/Protocol#Login_Start">http://wiki.vg/Protocol#Login_Start</a>
     * @param connection Only {@link PendingConnection#getAddress()}, {@link PendingConnection#getName()},
     *   {@link PendingConnection#getVirtualHost()} and {@link PendingConnection#getVersion()} are safe to use.
     */
    boolean shouldAuthenticate(PendingConnection connection);
}
