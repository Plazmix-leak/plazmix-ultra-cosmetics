package net.plazmix.ultracosmetics.player;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.utility.player.PlazmixUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for UltraPlayers.
 *
 * @author iSach
 * @since 12-16-2015
 */
public class CosmeticPlayerManager {

    private Map<Integer, CosmeticPlayer> playerCache;
    private UltraCosmetics ultraCosmetics;

    public CosmeticPlayerManager(UltraCosmetics ultraCosmetics) {
        this.playerCache = new ConcurrentHashMap<>();
        this.ultraCosmetics = ultraCosmetics;
    }

    public CosmeticPlayer getCosmeticPlayer(PlazmixUser user) {
        if (user == null || !playerCache.containsKey(user.getPlayerId()))
            return null;

        CosmeticPlayer p = playerCache.get(user.getPlayerId());
        if (p == null) {
            return create(user);
        }
        return p;
    }

    public CosmeticPlayer create(PlazmixUser user) {
        CosmeticPlayer customPlayer = new CosmeticPlayer(user);
        customPlayer.load();
        playerCache.put(user.getPlayerId(), customPlayer);
        return customPlayer;
    }

    public boolean remove(Player player) {
        return playerCache.remove(player.getUniqueId()) != null;
    }

    public Collection<CosmeticPlayer> getUltraPlayers() {
        return playerCache.values();
    }

    /**
     * Initialize players.
     */
    public void initPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            create(PlazmixUser.of(player));
            // Todo: add cosmetics menu
        }
    }

    public void dispose() {
        Collection<CosmeticPlayer> set = playerCache.values();
        for (CosmeticPlayer cp : set) {
            cp.clear();
        }

        playerCache.clear();
        playerCache = null;
    }
}
