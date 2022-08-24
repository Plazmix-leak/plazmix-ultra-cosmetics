package net.plazmix.ultracosmetics.cosmetics.elements.gadgets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.config.MessageManager;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.Gadget;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.GadgetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.BlockUtils;
import net.plazmix.ultracosmetics.util.Cuboid;
import net.plazmix.ultracosmetics.util.MathUtils;
import net.plazmix.ultracosmetics.version.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an instance of a trampoline gadget summoned by a player.
 *
 * @author iSach
 * @since 12-19-2015
 */
public class GadgetTrampoline extends Gadget {
    private Set<Block> trampoline = new HashSet<>();
    private Cuboid cuboid;
    private Location center;
    private boolean running;

    public GadgetTrampoline(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, GadgetType.valueOf("trampoline"), ultraCosmetics);
    }

    @Override
    void onRightClick() {
        Location loc1 = getPlayer().getLocation().add(-2, 0, -2);
        Location loc2 = getPlayer().getLocation().add(2, 15, 2);

        clearBlocks();

        center = getPlayer().getLocation();
        cuboid = new Cuboid(loc1, loc2);

        generateStructure();

        getPlayer().teleport(getPlayer().getLocation().add(0, 4, 0));

        running = true;
    }

    @Override
    protected boolean checkRequirements(PlayerInteractEvent event) {
        Location loc1 = event.getPlayer().getLocation().add(2, 15, 2);
        Location loc2 = event.getPlayer().getLocation().clone().add(-3, 0, -2);
        Block block = loc1.getBlock().getRelative(3, 0, 0);
        Block block2 = loc1.getBlock().getRelative(3, 1, 0);
        Cuboid checkCuboid = new Cuboid(loc1, loc2);

        if (!checkCuboid.isEmpty() || block.getType() != Material.AIR || block2.getType() != Material.AIR) {
            event.getPlayer().sendMessage(MessageManager.getMessage("Gadgets.Rocket.Not-Enough-Space"));
            return false;
        }
        return true;
    }

    @Override
    void onLeftClick() {
    }

    @Override
    public void onUpdate() {
        if (running && cuboid != null) {
            Bukkit.getScheduler().runTask(getUltraCosmetics(), () -> {
                for (Entity entity : center.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    Block b = entity.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    if (b.getType().toString().contains("WOOL") && cuboid.contains(b))
                        MathUtils.applyVelocity(entity, new Vector(0, 3, 0));
                }
            });
        }
    }

    @Override
    public void onClear() {
        clearBlocks();
        trampoline = null;
        cuboid = null;
        running = false;
    }

    private void generateStructure() {
        genBarr(get(2, 0, 2));
        genBarr(get(-2, 0, 2));
        genBarr(get(2, 0, -2));
        genBarr(get(-2, 0, -2));

        genBlue(get(2, 1, 2));
        genBlue(get(2, 1, 1));
        genBlue(get(2, 1, 0));
        genBlue(get(2, 1, -1));
        genBlue(get(2, 1, -2));
        genBlue(get(-2, 1, 2));
        genBlue(get(-2, 1, 1));
        genBlue(get(-2, 1, 0));
        genBlue(get(-2, 1, -1));
        genBlue(get(-2, 1, -2));
        genBlue(get(1, 1, 2));
        genBlue(get(0, 1, 2));
        genBlue(get(-1, 1, 2));
        genBlue(get(1, 1, -2));
        genBlue(get(0, 1, -2));
        genBlue(get(-1, 1, -2));

        genBlack(get(0, 1, 0));
        genBlack(get(0, 1, 1));
        genBlack(get(1, 1, 0));
        genBlack(get(0, 1, -1));
        genBlack(get(-1, 1, 0));
        genBlack(get(1, 1, 1));
        genBlack(get(-1, 1, -1));
        genBlack(get(1, 1, -1));
        genBlack(get(-1, 1, 1));

        genLadder(get(-3, 1, 0));
        genLadder(get(-3, 0, 0));

        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), this::clearBlocks, 240);
    }

    private void genBarr(Block block) {
        setToRestore(block, BlockUtils.getOldMaterial("FENCE"), (byte) 0);
    }

    private void genBlue(Block block) {
        if (VersionManager.IS_VERSION_1_13) {
            setToRestore(block, BlockUtils.getBlockByColor("WOOL", (byte) 11), (byte) 11);
        } else {
            setToRestore(block, Material.valueOf("WOOL"), (byte) 11);
        }
    }

    private void genBlack(Block block) {
        if (VersionManager.IS_VERSION_1_13) {
            setToRestore(block, BlockUtils.getBlockByColor("WOOL", (byte) 15), (byte) 15);
        } else {
            setToRestore(block, Material.valueOf("WOOL"), (byte) 15);
        }
    }

    private void genLadder(Block block) {
        setToRestore(block, Material.LADDER, (byte) 4);
    }

    @SuppressWarnings("deprecation")
    private void setToRestore(Block block, Material material, byte data) {
        trampoline.add(block);
        block.setType(material);
        BlockState state = block.getState();
        state.setRawData(data);
        state.update();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (cuboid != null && running && cuboid.contains(event.getBlock()))
            event.setCancelled(true);
        if (cuboid != null && running && (event.getBlock().getLocation().equals(center.getBlock().getRelative(-3, 0, 0).getLocation())
                || event.getBlock().getLocation().equals(center.getBlock().getRelative(-3, 1, 0).getLocation())))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (cuboid != null && running && cuboid.contains(event.getBlock()))
            event.setCancelled(true);
        if (cuboid != null && running && (event.getBlock().getLocation().equals(center.getBlock().getRelative(-3, 0, 0).getLocation())
                || event.getBlock().getLocation().equals(center.getBlock().getRelative(-3, 1, 0).getLocation())))
            event.setCancelled(true);
    }

    private void clearBlocks() {
        if (center != null) {
            get(-3, 0, 0).setType(Material.AIR);
            get(-3, 1, 0).setType(Material.AIR);
        }
        if (trampoline != null) {
            for (Block block : trampoline)
                block.setType(Material.AIR);
            trampoline.clear();
        }
        cuboid = null;
        running = false;
    }

    private Block get(int x, int y, int z) {
        return center.getBlock().getRelative(x, y, z);
    }
}
