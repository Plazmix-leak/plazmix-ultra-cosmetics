package net.plazmix.ultracosmetics.cosmetics;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.elements.emotes.EmoteType;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.GadgetType;
import net.plazmix.ultracosmetics.cosmetics.elements.hats.HatType;
import net.plazmix.ultracosmetics.cosmetics.elements.mounts.MountType;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffectType;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.PetType;
import net.plazmix.ultracosmetics.cosmetics.type.*;
import net.plazmix.ultracosmetics.menu.CosmeticMenu;
import net.plazmix.utility.player.PlazmixUser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public enum Category {

    PETS("Pets", Material.SKULL_ITEM, PetType.enabled()),
    GADGETS("Gadgets", Material.SKULL_ITEM, GadgetType.enabled()) {
        @Override
        public CosmeticMenu getMenu(UltraCosmetics ultraCosmetics) {
            return ultraCosmetics.getMenus().getGadgetsMenu();
        }
    },
    EFFECTS("Particle-Effects", Material.SKULL_ITEM, ParticleEffectType.enabled()) {
        @Override
        public CosmeticMenu getMenu(UltraCosmetics ultraCosmetics) {
            return ultraCosmetics.getMenus().getEffectsMenu();
        }
    },
    MOUNTS("Mounts", Material.SKULL_ITEM, MountType.enabled()) {
        @Override
        public CosmeticMenu getMenu(UltraCosmetics ultraCosmetics) {
            return ultraCosmetics.getMenus().getMountsMenu();
        }

    },
    HATS("Hats", Material.SKULL_ITEM, HatType.enabled()) {
        @Override
        public CosmeticMenu getMenu(UltraCosmetics ultraCosmetics) {
            return ultraCosmetics.getMenus().getHatsMenu();
        }
    },
    EMOTES("Emotes", Material.SKULL_ITEM, EmoteType.enabled()) {
        @Override
        public CosmeticMenu getMenu(UltraCosmetics ultraCosmetics) {
            return ultraCosmetics.getMenus().getEmotesMenu();
        }
    };

    public static int enabledSize() {
        return enabled().size();
    }

    public static List<Category> enabled() {
        return Arrays.stream(values()).filter(Category::isEnable).collect(Collectors.toList());
    }

    /**
     * The config path name.
     */
    private String signature;

    /**
     * The ItemStack in Main Menu.
     */
    private ItemStack menuElement;

    private Boolean isEnable;

    Category(String signature, ItemStack menuElement, Boolean isEnable) {
        this.signature = signature;
        this.menuElement = menuElement;
        this.isEnable = isEnable;

    }

    public Boolean isEnable() {
        return isEnable;
    }

    public ItemStack getItemStack() {
        return menuElement;
    }

    public String getSignature(){
        return signature;
    }

    public String getTitleName(PlazmixUser user){
        return user.localization().getMessageText(String.format("%s_TITLE", signature));
    }

    public String getActivateMenu(PlazmixUser user) {
        return user.localization().getMessageText(String.format("%s_ACTIVATE_MENU", signature));
    }


    public String getDeactivateMenu(PlazmixUser user) {
        return user.localization().getMessageText(String.format("%s_DEACTIVATE_MENU", signature));
    }

    public abstract List<? extends CosmeticType> getEnabled();
}
