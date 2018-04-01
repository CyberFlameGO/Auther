package me.hugmanrique.auther;

/**
 * @author Hugo Manrique
 * @since 01/04/2018
 */
public interface Auther {
    AuthDeterminer getDeterminer();

    void setDeterminer(AuthDeterminer authDeterminer);
}
