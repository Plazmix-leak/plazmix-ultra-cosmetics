package net.plazmix.ultracosmetics.cosmetics.type;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ServerVersion;
import net.plazmix.utility.player.PlazmixUser;

import java.lang.reflect.InvocationTargetException;
import java.util.List;



public abstract class CosmeticType<T extends Cosmetic> {

    private String signature;
    private Class<? extends T> clazz;
    private Category category;
    private ServerVersion baseVersion;
    private Boolean isEnable;

    public CosmeticType(Category category, String signature, Class clazz, ServerVersion baseVersion, Boolean isEnable) {
        this.signature = signature;
        this.clazz = clazz;
        this.category = category;
        this.baseVersion = baseVersion;
        this.isEnable = isEnable;
    }

    public T equip(CosmeticPlayer player, UltraCosmetics ultraCosmetics) {
        T cosmetic = null;
        try {
            cosmetic = getClazz().getDeclaredConstructor(CosmeticPlayer.class, UltraCosmetics.class).newInstance(player, ultraCosmetics);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        cosmetic.equip();
        return cosmetic;
    }

    public boolean isEnabled() {
        return isEnable;
    }

    public String getName(PlazmixUser user) {
        return user.getLanguage().getResource().getText(this.signature);
    }

    public String getSignature() {
        return signature;
    }


    public Class<? extends T> getClazz() {
        return clazz;
    }

    public Category getCategory() {
        return category;
    }


    /**
     * Transforms the description from a String to a list.
     * Without colors.
     *
     * @return The description as a string.
     */
    public String getDescription(PlazmixUser user) {
        return user.localization().getMessageText(String.format("COSMETICS_%S_DESCRIPTION", signature));
    }

    /**
     * Transforms the description from a String to a list.
     * Without colors.
     *
     * @return The description as a list.
     */
    public List<String> getDescriptionList(PlazmixUser user) {
        return user.localization().getMessageList(String.format("COSMETICS_%S_DESCRIPTION", signature));
    }


    /**
     * Override toString method to show Cosmetic name.
     *
     * @return
     */
    @Override
    public String toString() {
        return signature.toUpperCase();
    }
}
