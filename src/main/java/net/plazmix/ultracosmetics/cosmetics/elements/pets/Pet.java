package net.plazmix.ultracosmetics.cosmetics.elements.pets;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.cosmetics.variable.Updatable;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.variables.IPlayerFollower;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.EntitySpawningManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents an instance of a pet summoned by a player.
 *
 * @author iSach
 * @since 03-08-2015
 */
public abstract class Pet extends Cosmetic<PetType> implements Updatable {
    /**
     * List of items popping out from Pet.
     */
    public ArrayList<Item> items = new ArrayList<>();

    /**
     * ArmorStand for nametags. Most pets don't use this.
     */
    public ArmorStand armorStand;

    /**
     * Runs the task for pets following players |: Problems with async??
     */
    protected ExecutorService pathUpdater;
    //protected int followTaskId;

    /**
     * Task that forces pets to follow player
     */
    protected IPlayerFollower followTask;

    /**
     * If Pet is a normal entity, it will be stored here.
     */
    public Entity entity;

    /**
     * The {@link org.bukkit.inventory.ItemStack ItemStack} this pet drops, null if none.
     */
    private ItemStack dropItem;

    private Random r = new Random();

    private Boolean baby;

    public Pet(CosmeticPlayer owner, UltraCosmetics ultraCosmetics, PetType petType, ItemStack dropItem, Boolean baby) {
        super(ultraCosmetics, Category.PETS, owner, petType);

        this.dropItem = dropItem;
        this.pathUpdater = Executors.newSingleThreadExecutor();
        this.baby = baby;
    }

    @Override
    protected void onEquip() {
        if (getOwner().getCurrentPet() != null) {
            getOwner().removePet();
        }

        Player bukkitPlayer = getOwner().getBukkitPlayer();

        this.followTask = UltraCosmeticsData.get().getVersionManager().newPlayerFollower(this, bukkitPlayer);

        getOwner().setCurrentPet(this);

        runTaskTimer(getUltraCosmetics(), 0, 3);

        // Bypass WorldGuard protection.
        EntitySpawningManager.setBypass(true);
        setEntity(bukkitPlayer.getWorld().spawnEntity(bukkitPlayer.getLocation(), getType().getEntityType()));
        EntitySpawningManager.setBypass(false);

        UltraCosmeticsData.get().getVersionManager().getEntityUtil().clearPathfinders(entity);

        if (entity instanceof Ageable) {
            if (this.baby) {
                ((Ageable) entity).setBaby();
            } else {
                ((Ageable) entity).setAdult();
            }
            ((Ageable) entity).setAgeLock(true);
        }

        getEntity().setCustomNameVisible(true);
        getEntity().setCustomName(getType().getEntityName(getOwner()));

        ((LivingEntity) entity).setRemoveWhenFarAway(false);
        UltraCosmeticsData.get().getVersionManager().getPathfinderUtil().removePathFinders(entity);

        this.entity.setMetadata("Pet", new FixedMetadataValue(getUltraCosmetics(), "UltraCosmetics"));
    }

    @Override
    public void run() {
        try {
            if (entity != null && !entity.isValid()) {
                if (armorStand != null) {
                    armorStand.remove();
                }

                entity.remove();

                if (getPlayer() != null) {
                    getOwner().setCurrentPet(null);
                }

                items.forEach(Entity::remove);
                items.clear();

                try {
                    HandlerList.unregisterAll(this);
                } catch (Exception ignored) {
                    // Ignored.
                }

                cancel();
                return;
            }

            if (getOwner().getBukkitPlayer() != null
                    && getOwner().getCurrentPet() != null
                    && getOwner().getCurrentPet().getType() == getType()) {
                if (this.dropItem != null) {
                    onUpdate();
                }

                pathUpdater.submit(followTask.getTask());
            } else {
                cancel();

                if (armorStand != null) {
                    armorStand.remove();
                }

                items.forEach(Entity::remove);
                items.clear();
                clear();
            }
        } catch (NullPointerException exc) {
            exc.printStackTrace();
            cancel();

            if (armorStand != null) {
                armorStand.remove();
            }

            items.forEach(Entity::remove);
            items.clear();
            clear();
        }
    }

    @Override
    protected void onClear() {
        // Remove Armor Stand.
        if (armorStand != null) {
            armorStand.remove();
        }

        // Remove Pet Entity.
        removeEntity();

        // Remove items.
        items.stream().filter(Entity::isValid).forEach(Entity::remove);

        // Clear items.
        items.clear();

        // Shutdown path updater.
        pathUpdater.shutdown();

        // Empty current Pet.
        if (getPlayer() != null && getOwner() != null) {
            getOwner().setCurrentPet(null);
        }
    }

    public boolean isCustomEntity() {
        return false;
    }

    protected void removeEntity() {
        if (entity != null) {
            entity.remove();
        }
    }

    public IPlayerFollower getFollowTask() {
        return followTask;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public void onUpdate() {
        final Item drop = entity.getWorld().dropItem(((LivingEntity) entity).getEyeLocation(), dropItem);
        drop.setPickupDelay(30000);
        drop.setVelocity(new Vector(r.nextDouble() - 0.5, r.nextDouble() / 2.0 + 0.3, r.nextDouble() - 0.5).multiply(0.4));
        items.add(drop);
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
            drop.remove();
            items.remove(drop);
        }, 5);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() == getEntity())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        try {
            assert getPlayer() != null;
            if (event.getPlayer() == getPlayer()) {
                getEntity().teleport(getPlayer().getBukkitPlayer());
            }
        } catch (AssertionError | NullPointerException exc) {
            removeEntity();
            Bukkit.getLogger().warning("Pet player not found! Entity remove!");
        }
    }

    protected final void setEntity(Entity entity) {
        this.entity = entity;
    }
}
