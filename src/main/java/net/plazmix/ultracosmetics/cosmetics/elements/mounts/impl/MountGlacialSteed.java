package net.plazmix.ultracosmetics.cosmetics.elements.mounts.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.config.SettingsManager;
import net.plazmix.ultracosmetics.cosmetics.elements.mounts.MountType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.BlockUtils;
import net.plazmix.ultracosmetics.util.Particles;
import net.plazmix.ultracosmetics.util.UtilParticles;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Represents an instance of a glacial steed mount.
 *
 * @author iSach
 * @since 08-10-2015
 */
public class MountGlacialSteed extends MountHorse {

    public MountGlacialSteed(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, MountType.valueOf("glacialsteed"), ultraCosmetics);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        entity.setJumpStrength(0.7);
        UltraCosmeticsData.get().getVersionManager().getEntityUtil().setHorseSpeed(entity, 0.4d);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer() == getPlayer()
                && getOwner().getCurrentMount() == this
                && (boolean) SettingsManager.getConfig().get("Mounts-Block-Trails")) {
            for (Block b : BlockUtils.getBlocksInRadius(event.getPlayer().getLocation(), 3, false)) {
                if (b.getLocation().getBlockY() == event.getPlayer().getLocation().getBlockY() - 1) {
                    BlockUtils.setToRestore(b, Material.SNOW_BLOCK, (byte) 0x0, 20);
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        UtilParticles.display(Particles.SNOW_SHOVEL, 0.4f, 0.2f, 0.4f, entity.getLocation().clone().add(0, 1, 0), 5);
    }

    @Override
    protected Horse.Variant getVariant() {
        return Horse.Variant.HORSE;
    }

    @Override
    protected Horse.Color getColor() {
        return Horse.Color.WHITE;
    }
}