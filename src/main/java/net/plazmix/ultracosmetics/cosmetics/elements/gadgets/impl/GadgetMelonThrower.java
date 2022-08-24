package net.plazmix.ultracosmetics.cosmetics.elements.gadgets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.config.MessageManager;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.Gadget;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.GadgetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.util.SoundUtil;
import net.plazmix.ultracosmetics.util.Sounds;
import net.plazmix.ultracosmetics.util.UCMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Represents an instance of a melon thrower gadget summoned by a player.
 *
 * @author iSach
 * @since 08-03-2015
 */
public class GadgetMelonThrower extends Gadget implements Listener {

    private Random random = new Random();
    private Item melon = null;
    private World world = null;

    public GadgetMelonThrower(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, GadgetType.valueOf("melonthrower"), ultraCosmetics);
    }

    @EventHandler
    public void onTakeUpMelon(PlayerPickupItemEvent event) {
        if (event.getItem().hasMetadata("UC#MELONITEM")
                && event.getItem().getTicksLived() > 5
                && affectPlayers) {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 2));
            SoundUtil.playSound(getPlayer().getLocation(), Sounds.BURP, 1.4f, 1.5f);
            event.setCancelled(true);
            event.getItem().remove();
        }
    }

    @Override
    protected boolean checkRequirements(PlayerInteractEvent event) {
        // Check if the current melon has finished exploding.
        if (melon != null) {
            event.getPlayer().sendMessage(MessageManager.getMessage("Gadgets.MelonThrower.Wait-For-Finish"));
            return false;
        }
        return true;
    }

    @Override
    void onRightClick() {
        this.world = getPlayer().getWorld();
        SoundUtil.playSound(getPlayer().getLocation(), Sounds.EXPLODE, 1.4f, 1.5f);
        Item item = getPlayer().getWorld().dropItem(getPlayer().getEyeLocation(), ItemFactory.create(UCMaterial.MELON, UltraCosmeticsData.get().getItemNoPickupString()));
        item.setPickupDelay(0);
        item.setMetadata("UNPICKABLEUP", new FixedMetadataValue(getUltraCosmetics(), "UC#MELONBLOCK"));
        item.setVelocity(getPlayer().getEyeLocation().getDirection().multiply(1.3d));
        melon = item;
    }

    @Override
    public void onUpdate() {
        Bukkit.getScheduler().runTask(getUltraCosmetics(), () -> {
            if (melon == null || !melon.isValid()) {
                return;
            }
            if (melon.isOnGround()) {
                melon.getWorld().playEffect(melon.getLocation(), Effect.STEP_SOUND, 103);
                for (int i = 0; i < 8; i++) {
                    final Item newItem = getPlayer().getWorld().dropItem(melon.getLocation(), ItemFactory.create(UCMaterial.MELON_SLICE, UltraCosmeticsData.get().getItemNoPickupString()));
                    newItem.setVelocity(new Vector(random.nextDouble() - 0.5, random.nextDouble() / 2.0, random.nextDouble() - 0.5).multiply(0.75D));
                    newItem.setMetadata("UC#MELONITEM", new FixedMetadataValue(getUltraCosmetics(), "UC#MELONTHROWER"));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (newItem.isValid()) {
                                newItem.remove();
                            }
                        }
                    }.runTaskLaterAsynchronously(getUltraCosmetics(), 100);
                }
                melon.remove();
                melon = null;
            }
        });
    }

    @Override
    public void onClear() {
        if (melon != null) {
            melon.remove();
        }

        if (world != null) {
            for (Item i : world.getEntitiesByClass(Item.class)) {
                if (i.hasMetadata("UC#MELONITEM")) {
                    i.remove();
                }
            }
        }
    }

    @Override
    void onLeftClick() {
    }
}
