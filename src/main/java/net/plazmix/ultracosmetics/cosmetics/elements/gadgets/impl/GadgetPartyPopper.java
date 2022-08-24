package net.plazmix.ultracosmetics.cosmetics.elements.gadgets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.Gadget;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.GadgetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.*;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Represents an instance of a party popper gadget summoned by a player.
 *
 * @author iSach
 * @since 12-16-2015
 */
public class GadgetPartyPopper extends Gadget {

    public GadgetPartyPopper(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, GadgetType.valueOf("partypopper"), ultraCosmetics);

        setAsynchronous(true);
    }

    @Override
    void onRightClick() {
        for (int i = 0; i < 30; i++) {
            Vector rand = new Vector(Math.random() - 0.5D,
                    Math.random() - 0.5D, Math.random() - 0.5D);

            if (UltraCosmeticsData.get().getServerVersion().compareTo(ServerVersion.v1_14_R1) >= 0) {
                ItemStack randomDye = UCMaterial.DYES.get(MathUtils.random(0, 14)).parseItem();
                Vector v = getPlayer().getEyeLocation().getDirection().add(rand.multiply(0.2)).multiply(3.2);
                getPlayer().getWorld().spawnParticle(Particle.ITEM_CRACK, getPlayer().getEyeLocation(), 10, v.getX(), v.getY(), v.getZ(),
                        0.2d, randomDye);

            } else {
                Particles.ITEM_CRACK.display(new Particles.ItemData(BlockUtils.getDyeByColor(MathUtils.randomByte(15)),
                                MathUtils.randomByte(15)), getPlayer().getEyeLocation().getDirection().add(rand.multiply(0.2)).multiply(1.2),
                        0.6f, getPlayer().getEyeLocation(), 128);
            }
        }
        for (int i = 0; i < 3; i++)
            SoundUtil.playSound(getPlayer().getLocation(), Sounds.CHICKEN_EGG_POP, 1.0f, 1.0f);
    }

    @Override
    void onLeftClick() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onClear() {
    }
}
