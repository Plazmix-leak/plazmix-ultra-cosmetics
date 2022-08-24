package net.plazmix.ultracosmetics.menu.menus;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.cosmetics.elements.emotes.EmoteType;
import net.plazmix.ultracosmetics.menu.CosmeticMenu;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.menu.Menu;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Emote {@link Menu Menu}.
 *
 * @author iSach
 * @since 08-23-2016
 */
public class MenuEmotes extends CosmeticMenu<EmoteType> {

    public MenuEmotes(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, Category.EMOTES);
    }

    @Override
    protected void putItems(Inventory inventory, CosmeticPlayer cosmeticPlayer, int page) {
    }

    @Override
    protected ItemStack filterItem(ItemStack itemStack, EmoteType cosmeticType, CosmeticPlayer player) {
        ItemMeta itemMeta = itemStack.getItemMeta().clone();
        itemStack = cosmeticType.getFrames().get(cosmeticType.getMaxFrames() - 1).clone();
        ItemMeta other = itemStack.getItemMeta().clone();

        other.setDisplayName(itemMeta.getDisplayName());
        other.setLore(itemMeta.getLore());
        itemStack.setItemMeta(other);

        return itemStack;
    }

    @Override
    public List<EmoteType> enabled() {
        return EmoteType.enabled();
    }

    @Override
    protected void toggleOn(CosmeticPlayer cosmeticPlayer, EmoteType emoteType, UltraCosmetics ultraCosmetics) {
        emoteType.equip(cosmeticPlayer, ultraCosmetics);
    }

    @Override
    protected void toggleOff(CosmeticPlayer cosmeticPlayer) {
        cosmeticPlayer.removeEmote();
    }

    @Override
    protected Cosmetic getCosmetic(CosmeticPlayer cosmeticPlayer) {
        return cosmeticPlayer.getCurrentEmote();
    }
}
