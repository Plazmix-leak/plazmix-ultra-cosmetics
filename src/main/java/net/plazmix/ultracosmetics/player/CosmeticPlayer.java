package net.plazmix.ultracosmetics.player;

import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.cosmetics.elements.emotes.Emote;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.Gadget;
import net.plazmix.ultracosmetics.cosmetics.elements.hats.Hat;
import net.plazmix.ultracosmetics.cosmetics.elements.mounts.Mount;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffect;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.Pet;
import net.plazmix.ultracosmetics.cosmetics.type.CosmeticType;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.GadgetType;
import net.plazmix.ultracosmetics.mysql.CosmeticsProfileInformation;
import net.plazmix.ultracosmetics.mysql.CosmeticsSqlHandler;
import net.plazmix.ultracosmetics.run.FallDamageManager;
import net.plazmix.utility.player.PlazmixUser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;


public class CosmeticPlayer {
    /**
     * Saves the username for logging usage.
     */
    private PlazmixUser user;
    /**
     * Current Cosmetics.
     */
    private Gadget currentGadget;
    private Mount currentMount;
    private ParticleEffect currentParticleEffect;
    private Pet currentPet;
    private Hat currentHat;
    private Emote currentEmote;
    private CosmeticsProfileInformation userSettings;
    /**
     * Specifies if the player can currently be hit by any other gadget.
     * Exemple: Get hit by a flesh hook.
     */
    private boolean canBeHitByOtherGadgets = true;
    /**
     * Cooldown map storing all the current cooldowns for gadgets.
     */
    private HashMap<GadgetType, Long> gadgetCooldowns = null;

    private volatile boolean moving;
    private volatile Location lastPos;

    /**
     * Indicates if the player is leaving the server.
     * Useful to differentiate for example when a player deactivates
     * a cosmetic on purpose or because they leave.
     */
    private boolean quitting = false;

    /**
     * Allows to store custom data for each player easily.
     * <p/>
     * Created on join, and deleted on quit.
     *
     * @param user The PlazmixUser.
     */
    public CosmeticPlayer(PlazmixUser user) {
        this.user = user;
        this.userSettings = CosmeticsSqlHandler.INSTANCE.loadDataFromPlayer(user.getPlayerId());
    }

    /**
     * Checks if a player can use a given gadget type.
     *
     * @param gadget The gadget type.
     * @return -1 if player can use, otherwise the time left (in seconds).
     */
    public double canUse(GadgetType gadget) {
        Object count = gadgetCooldowns.get(gadget);

        if (count == null || System.currentTimeMillis() > (long) count) {
            return -1;
        }

        double valueMillis = (long) count - System.currentTimeMillis();
        return valueMillis / 1000d;
    }

    /**
     * Sets the cooldown of a gadget.
     *
     * @param gadget    The gadget.
     * @param countdown The cooldown to set.
     */
    public void setCooldown(GadgetType gadget, double countdown) {
        gadgetCooldowns.put(gadget, (long) (countdown * 1000 + System.currentTimeMillis()));
    }

    /**
     * Get the player owning the UltraPlayer.
     *
     * @return The player owning the UltraPlayer.
     */
    public Player getBukkitPlayer() {
        return user.handle();
    }

    /**
     * Get plazmix player.
     *
     * @return The player owning the UltraPlayer.
     */
    public PlazmixUser getPlazmixUser() {
        return user;
    }


    /**
     * Removes the current gadget.
     */
    public void removeGadget() {
        if (currentGadget == null) {
            return;
        }

        currentGadget.clear();
        setCurrentGadget(null);
    }

    /**
     * Removes the current emote.
     */
    public void removeEmote() {
        if (currentEmote == null) {
            return;
        }
        currentEmote.clear();
        setCurrentEmote(null);
    }


    /**
     * Removes the current Mount.
     */
    public void removeMount() {
        if (currentMount == null) {
            return;
        }
        currentMount.clear();
        setCurrentMount(null);
    }

    /**
     * Removes the current Pet.
     */
    public void removePet() {
        if (currentPet == null) {
            return;
        }
        currentPet.clear();
        setCurrentPet(null);
    }

    /**
     * Removes the current hat.
     */
    public void removeHat() {
        if (currentHat == null) {
            return;
        }

        currentHat.clear();
        setCurrentHat(null);
    }

//    public void setCurrentSuitPart(ArmorSlot armorSlot, Suit suit) {
//        suitMap.put(armorSlot, suit);
//        if (!isQuitting() && cosmeticsProfile != null && UltraCosmeticsData.get().areCosmeticsProfilesEnabled())
//            cosmeticsProfile.setEnabledSuitPart(armorSlot, suit == null ? null : suit.getType());
//    }

//    /**
//     * Removes the current suit of armorSlot.
//     *
//     * @param armorSlot The ArmorSlot to remove.
//     */
//    public void removeSuit(ArmorSlot armorSlot) {
//        if (!suitMap.containsKey(armorSlot)) {
//            suitMap.put(armorSlot, null);
//            return;
//        }
//
//        if (suitMap.get(armorSlot) == null) {
//            return;
//        }
//
//        suitMap.get(armorSlot).clear();
//        setCurrentSuitPart(armorSlot, null);
//    }

    public double getCoinsBalance() {
        return getPlazmixUser().getCoins();
    }

    public double getPlazmaBalance() {
        return getPlazmixUser().getGolds();
    }


    public void saveCurrentCosmetic(Category cosmeticCategory, Cosmetic cosmetic) {
        if (cosmetic == null) {
            userSettings.removeSelected(cosmeticCategory);
        } else {
            userSettings.setSelected(cosmeticCategory, cosmetic.getType().getSignature());
        }
        CosmeticsSqlHandler.INSTANCE.save(user, userSettings);

    }

    public void addAvailableCosmetic() {
        CosmeticsSqlHandler.INSTANCE.save(user, userSettings);
    }
//    /**
//     * @param armorSlot The armorslot to get.
//     * @return The Suit from the armor slot.
//     */
//    public Suit getSuit(ArmorSlot armorSlot) {
//        if (!suitMap.containsKey(armorSlot)) {
//            suitMap.put(armorSlot, null);
//        }
//
//        return suitMap.get(armorSlot);
//    }

//    /**
//     * Checks if this player has any suit piece on.
//     *
//     * @return True if this player has any suit piece on, false otherwise.
//     */
//    public boolean hasSuitOn() {
//        for (ArmorSlot armorSlot : ArmorSlot.values()) {
//            if (suitMap.get(armorSlot) != null)
//                return true;
//        }
//        return false;
//    }

//    /**
//     * Removes entire suit.
//     */
//    public void removeSuit() {
//        for (ArmorSlot armorSlot : ArmorSlot.values()) {
//            removeSuit(armorSlot);
//        }
//    }

    /**
     * Clears all gadgets.
     */
    public boolean clear() {
        boolean toReturn = currentGadget != null
                || currentParticleEffect != null
                || currentPet != null
                || currentMount != null
                || currentHat != null
                || currentEmote != null;

        removeGadget();
        removeParticleEffect();
        removePet();
        removeMount();
        removeHat();
        removeEmote();
        return toReturn;
    }

    public <T extends Cosmetic> T getCosmetic(Category category) {
        switch (category) {
            case EFFECTS:
                return (T) getCurrentParticleEffect();
            case EMOTES:
                return (T) getCurrentEmote();
            case GADGETS:
                return (T) getCurrentGadget();
            case HATS:
                return (T) getCurrentHat();
            case MOUNTS:
                return (T) getCurrentMount();
            case PETS:
                return (T) getCurrentPet();
        }
        return null;
    }

    /**
     * Removes current Particle Effect.
     */
    public void removeParticleEffect() {
        if (currentParticleEffect == null) {
            return;
        }

        currentParticleEffect.clear();
        setCurrentParticleEffect(null);
    }

    public void applyVelocity(Vector vector) {
        getBukkitPlayer().setVelocity(vector);
        Bukkit.getScheduler().runTaskLaterAsynchronously(UltraCosmeticsData.get().getPlugin(), () ->
                FallDamageManager.addNoFall(getBukkitPlayer()), 2);
    }


//    /**
//     * Gives the Menu Item.
//     */
//    public void giveMenuItem() {
//        int slot = 9;
//        if (getBukkitPlayer().getInventory().getItem(slot) != null) {
//            if (getBukkitPlayer().getInventory().getItem(slot).hasItemMeta()
//                    && getBukkitPlayer().getInventory().getItem(slot).getItemMeta().hasDisplayName()
//                    && getBukkitPlayer().getInventory().getItem(slot).getItemMeta().getDisplayName().equalsIgnoreCase(SettingsManager.getConfig().getString("Menu-Item.Displayname"))) {
//                // getBukkitPlayer().getInventory().remove(slot);
//                getBukkitPlayer().getInventory().setItem(slot, null);
//            }
//            getBukkitPlayer().getWorld().dropItemNaturally(getBukkitPlayer().getLocation(), getBukkitPlayer().getInventory().getItem(slot));
//            // getBukkitPlayer().getInventory().remove(slot);
//            getBukkitPlayer().getInventory().setItem(slot, null);
//        }
//        String name = ChatColor.translateAlternateColorCodes('&', SettingsManager.getConfig().getString("Menu-Item.Displayname"));
//        UCMaterial material = UCMaterial.matchUCMaterial(SettingsManager.getConfig().getString("Menu-Item.Type"));
//        // byte data = Byte.valueOf(SettingsManager.getConfig().getString("Menu-Item.Data"));
//        getBukkitPlayer().getInventory().setItem(slot, ItemFactory.create(material, name));
//    }

//    /**
//     * Removes the menu Item.
//     */
//    public void removeMenuItem() {
//        if (getBukkitPlayer() == null)
//            return;
//        int slot = SettingsManager.getConfig().getInt("Menu-Item.Slot");
//        if (getBukkitPlayer().getInventory().getItem(slot) != null
//                && getBukkitPlayer().getInventory().getItem(slot).hasItemMeta()
//                && getBukkitPlayer().getInventory().getItem(slot).getItemMeta().hasDisplayName()
//                && getBukkitPlayer().getInventory().getItem(slot).getItemMeta().getDisplayName()
//                .equals(ChatColor.translateAlternateColorCodes('&', String.valueOf(SettingsManager.getConfig().get("Menu-Item.Displayname")))))
//            getBukkitPlayer().getInventory().setItem(slot, null);
//    }

    public Emote getCurrentEmote() {
        return currentEmote;
    }

    public void setCurrentEmote(Emote currentEmote) {
        this.currentEmote = currentEmote;
        saveCurrentCosmetic(Category.EMOTES, currentEmote);
    }

    public Gadget getCurrentGadget() {
        return currentGadget;
    }

    public void setCurrentGadget(Gadget currentGadget) {
        this.currentGadget = currentGadget;
        saveCurrentCosmetic(Category.GADGETS, currentGadget);
    }

    public HashMap<GadgetType, Long> getGadgetCooldowns() {
        return gadgetCooldowns;
    }

    public void setGadgetCooldowns(HashMap<GadgetType, Long> gadgetCooldowns) {
        this.gadgetCooldowns = gadgetCooldowns;
    }

    public Hat getCurrentHat() {
        return currentHat;
    }

    public void setCurrentHat(Hat currentHat) {
        this.currentHat = currentHat;
        saveCurrentCosmetic(Category.HATS, currentGadget);
    }

    public Mount getCurrentMount() {
        return currentMount;
    }

    public void setCurrentMount(Mount currentMount) {
        this.currentMount = currentMount;
        saveCurrentCosmetic(Category.MOUNTS, currentGadget);

    }

    public ParticleEffect getCurrentParticleEffect() {
        return currentParticleEffect;
    }

    public void setCurrentParticleEffect(ParticleEffect currentParticleEffect) {
        this.currentParticleEffect = currentParticleEffect;
        saveCurrentCosmetic(Category.EFFECTS, currentParticleEffect);
    }

    public Pet getCurrentPet() {
        return currentPet;
    }

    public void setCurrentPet(Pet currentPet) {
        this.currentPet = currentPet;
        saveCurrentCosmetic(Category.PETS, currentPet);
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Location getLastPos() {
        return lastPos;
    }

    public void setLastPos(Location lastPos) {
        this.lastPos = lastPos;
    }

    public void setCanBeHitByOtherGadgets(boolean canBeHitByOtherGadgets) {
        this.canBeHitByOtherGadgets = canBeHitByOtherGadgets;
    }

    public boolean canBeHitByOtherGadgets() {
        return canBeHitByOtherGadgets;
    }

    public void removeCosmetic(Category category) {
        switch (category) {
            case EFFECTS:
                removeParticleEffect();
            case EMOTES:
                removeEmote();
            case GADGETS:
                removeGadget();
            case HATS:
                removeHat();
            case MOUNTS:
                removeMount();
            case PETS:
                removePet();
        }
    }

    public boolean isQuitting() {
        return quitting;
    }

    public void setQuitting(boolean quitting) {
        this.quitting = quitting;
    }

    public boolean hasAccessCosmetic(CosmeticType cosmetic) {
        if (!userSettings.available.containsKey(cosmetic.getCategory().getSignature())) {
            return false;
        }

        return userSettings.available.get(cosmetic.getCategory().getSignature()).contains(cosmetic.getSignature());
    }

    public void load() {
    }
}