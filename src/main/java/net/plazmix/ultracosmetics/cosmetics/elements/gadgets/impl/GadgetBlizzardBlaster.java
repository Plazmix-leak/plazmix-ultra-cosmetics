package net.plazmix.ultracosmetics.cosmetics.elements.gadgets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.Gadget;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.GadgetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 * Represents an instance of a blizzard blaster gadget summoned by a player.
 *
 * @author iSach
 * @since 08-08-2015
 */
public class GadgetBlizzardBlaster extends Gadget {

    private boolean active;
    private Location location;
    private Vector vector;

    public GadgetBlizzardBlaster(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, GadgetType.valueOf("blizzardblaster"), ultraCosmetics);
    }

    @Override
    void onRightClick() {
        this.vector = getPlayer().getLocation().getDirection().normalize().multiply(0.3);
        this.vector.setY(0);

        this.location = getPlayer().getLocation().subtract(0, 1, 0).add(vector);
        this.active = true;

        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), this::clean, 40);

    }

    @Override
    public void onUpdate() {
        if (active) {
            if (location.getBlock().getType() != Material.AIR && location.getBlock().getType().isSolid()) {
                location.add(0, 1, 0);
            }

            if (location.clone().subtract(0, 1, 0).getBlock().getType() == Material.AIR) {
                if (!location.clone().getBlock().getType().toString().contains("SLAB"))
                    location.add(0, -1, 0);
            }

            for (int i = 0; i < 3; i++) {
                UltraCosmeticsData.get().getVersionManager().getEntityUtil()
                        .sendBlizzard(getPlayer(), location, affectPlayers, vector);
            }

            location.add(vector);
        } else {
            location = null;
            vector = null;
        }
    }

    @Override
    void onLeftClick() {
    }

    @Override
    public void onClear() {
        if (getOwner() == null || getPlayer() == null) {
            return;
        }
        UltraCosmeticsData.get().getVersionManager().getEntityUtil().clearBlizzard(getPlayer());
    }

    private void clean() {
        active = false;
    }
}
