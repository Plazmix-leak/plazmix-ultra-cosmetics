package net.plazmix.ultracosmetics.cosmetics.elements.pets.variables;

import org.bukkit.entity.Player;

/**
 * Player follower interface.
 *
 * @author iSach
 * @since 03-07-2016
 */
public interface IPlayerFollower {

    void follow(Player player);

    Runnable getTask();
}
