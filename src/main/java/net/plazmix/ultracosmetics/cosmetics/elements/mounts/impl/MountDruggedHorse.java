package net.plazmix.ultracosmetics.cosmetics.elements.mounts.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.elements.mounts.MountType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.Particles;
import net.plazmix.ultracosmetics.util.UtilParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by sacha on 10/08/15.
 */
public class MountDruggedHorse extends MountHorse {

    private Player effectPlayer;

    public MountDruggedHorse(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, MountType.valueOf("druggedhorse"), ultraCosmetics);
    }

    @Override
    public void onEquip() {
        super.onEquip();

        UltraCosmeticsData.get().getVersionManager().getEntityUtil().setHorseSpeed(getEntity(), 1.1d);
        getEntity().setJumpStrength(1.3);

        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
            try {
                getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10000000, 1));
                effectPlayer = getPlayer();
            } catch (Exception ignored) {
            }
        }, 1);
    }

    @Override
    public void onUpdate() {
        Location loc = entity.getLocation().add(0, 1, 0);
        UtilParticles.display(Particles.FIREWORKS_SPARK, 0.4f, 0.2f, 0.4f, loc, 5);
        UtilParticles.display(Particles.SPELL, 0.4f, 0.2f, 0.4f, loc, 5);
        UtilParticles.display(Particles.SPELL_MOB_AMBIENT, 0.4f, 0.2f, 0.4f, loc, 5);
        UtilParticles.display(Particles.SPELL_MOB, 5, 255, 0, loc);
    }

    @Override
    protected void onClear() {

        // Make sure it's calling the effect method synchronously with Spigot's thread. TODO make onClear sync all the time.
        new BukkitRunnable() {
            @Override
            public void run() {
                if (effectPlayer != null) {
                    effectPlayer.removePotionEffect(PotionEffectType.CONFUSION);
                }
            }
        }.runTask(getUltraCosmetics());
        super.onClear();
    }

    @Override
    protected Horse.Variant getVariant() {
        return Horse.Variant.HORSE;
    }

    @Override
    protected Horse.Color getColor() {
        return Horse.Color.CHESTNUT;
    }
}
