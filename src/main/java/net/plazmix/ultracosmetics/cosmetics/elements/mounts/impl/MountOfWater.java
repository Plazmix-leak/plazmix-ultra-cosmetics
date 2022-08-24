package net.plazmix.ultracosmetics.cosmetics.elements.mounts.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.config.SettingsManager;
import net.plazmix.ultracosmetics.cosmetics.elements.mounts.MountType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.BlockUtils;
import net.plazmix.ultracosmetics.util.Particles;
import net.plazmix.ultracosmetics.util.UtilParticles;
import net.plazmix.ultracosmetics.version.VersionManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents an instance of a mount of water mount.
 *
 * @author iSach
 * @since 08-10-2015
 */
public class MountOfWater extends MountHorse {

    public MountOfWater(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, MountType.valueOf("mountofwater"), ultraCosmetics);
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
            List<Byte> datas = new ArrayList<>();
            datas.add((byte) 0x3);
            datas.add((byte) 0x9);
            datas.add((byte) 0xb);
            for (Block b : BlockUtils.getBlocksInRadius(event.getPlayer().getLocation(), 3, false)) {
                if (b.getLocation().getBlockY() == event.getPlayer().getLocation().getBlockY() - 1) {
                    if (VersionManager.IS_VERSION_1_13) {
                        BlockUtils.setToRestore(b, BlockUtils.getBlockByColor("STAINED_CLAY", datas.get(new Random().nextInt(2))), datas.get(new Random().nextInt(2)), 20);
                    } else {
                        BlockUtils.setToRestore(b, Material.valueOf("STAINED_CLAY"), datas.get(new Random().nextInt(2)), 20);
                    }
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        UtilParticles.display(Particles.DRIP_WATER, 0.4f, 0.2f, 0.4f, entity.getLocation().clone().add(0, 1, 0), 5);
    }

    @Override
    protected Horse.Color getColor() {
        return Horse.Color.BLACK;
    }

    @Override
    protected Horse.Variant getVariant() {
        return Horse.Variant.HORSE;
    }
}