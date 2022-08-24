package net.plazmix.ultracosmetics.run;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by sacha on 11/26/2016.
 */
public class MovingChecker extends BukkitRunnable {

    private UltraCosmetics ultraCosmetics;

    public MovingChecker(UltraCosmetics ultraCosmetics) {
        this.ultraCosmetics = ultraCosmetics;
    }

    @Override
    public void run() {
        synchronized (ultraCosmetics.getPlayerManager().getUltraPlayers()) {
            for (CosmeticPlayer cosmeticPlayer : ultraCosmetics.getPlayerManager().getUltraPlayers()) {
                if (cosmeticPlayer == null
                        || cosmeticPlayer.getBukkitPlayer() == null) {
                    continue;
                }

                Location currentPos = cosmeticPlayer.getBukkitPlayer().getLocation();
                cosmeticPlayer.setMoving(!areEqual(currentPos, cosmeticPlayer.getLastPos()));
                cosmeticPlayer.setLastPos(currentPos);
            }
        }
    }

    private boolean areEqual(Location from, Location to) {
        return !(from == null || to == null) && from.getClass() == to.getClass() && !(from.getWorld() != to.getWorld() && (from.getWorld() == null || !from.getWorld().equals(to.getWorld()))) && (Double.doubleToLongBits(from.getX()) == Double.doubleToLongBits(to.getX()) && (Double.doubleToLongBits(from.getY()) == Double.doubleToLongBits(to.getY()) && (Double.doubleToLongBits(from.getZ()) == Double.doubleToLongBits(to.getZ()))));
    }

}
