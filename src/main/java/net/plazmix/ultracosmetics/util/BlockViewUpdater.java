package net.plazmix.ultracosmetics.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class BlockViewUpdater extends BukkitRunnable {
    private static final Set<Location> blocksUpdating = new HashSet<>();
    private Block block;
    public BlockViewUpdater(Block block) {
        this.block = block;
        blocksUpdating.add(block.getLocation());
    }

    @Override
    public void run() {
        if (!blocksUpdating.remove(block.getLocation())) return;
        for (Player player : block.getWorld().getPlayers()) {
            player.sendBlockChange(block.getLocation(), block.getType(), block.getData());
        }
    }

    public static boolean isUpdating(Location loc) {
        return blocksUpdating.contains(loc);
    }
}
