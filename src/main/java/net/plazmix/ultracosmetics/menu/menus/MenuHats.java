package net.plazmix.ultracosmetics.menu.menus;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.cosmetics.elements.hats.HatType;
import net.plazmix.ultracosmetics.menu.CosmeticMenu;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.menu.Menu;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Hat {@link Menu Menu}.
 *
 * @author iSach
 * @since 08-23-2016
 */
public class MenuHats extends CosmeticMenu<HatType> {

    public MenuHats(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, Category.HATS);
    }

    @Override
    protected void putItems(Inventory inventory, CosmeticPlayer cosmeticPlayer, int page) {
    }

    @Override
    public List<HatType> enabled() {
        return HatType.enabled();
    }

    @Override
    protected ItemStack filterItem(ItemStack itemStack, HatType cosmeticType, CosmeticPlayer player) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack = cosmeticType.getItemStack().clone();
        ItemMeta other = itemStack.getItemMeta().clone();
        other.setDisplayName(itemMeta.getDisplayName());
        other.setLore(itemMeta.getLore());
        itemStack.setItemMeta(other);
        return itemStack;
    }

    @Override
    protected void toggleOn(CosmeticPlayer cosmeticPlayer, HatType hatType, UltraCosmetics ultraCosmetics) {
        hatType.equip(cosmeticPlayer, ultraCosmetics);
    }

    @Override
    protected void toggleOff(CosmeticPlayer cosmeticPlayer) {
        cosmeticPlayer.removeHat();
    }

    @Override
    protected Cosmetic getCosmetic(CosmeticPlayer cosmeticPlayer) {
        return cosmeticPlayer.getCurrentHat();
    }
}
