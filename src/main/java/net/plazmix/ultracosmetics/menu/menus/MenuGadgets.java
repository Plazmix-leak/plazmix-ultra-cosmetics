package net.plazmix.ultracosmetics.menu.menus;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.config.MessageManager;
import net.plazmix.ultracosmetics.config.SettingsManager;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.GadgetType;
import net.plazmix.ultracosmetics.menu.ClickRunnable;
import net.plazmix.ultracosmetics.menu.CosmeticMenu;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.menu.Menu;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Gadget {@link Menu Menu}.
 *
 * @author iSach
 * @since 07-23-2016
 */
public class MenuGadgets extends CosmeticMenu<GadgetType> {

    public MenuGadgets(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, Category.GADGETS);
    }

    @Override
    protected void putItems(Inventory inventory, CosmeticPlayer player, int page) {
        putToggleGadgetsItems(inventory, player);
    }

    private void putToggleGadgetsItems(Inventory inventory, CosmeticPlayer player) {
        int slot = inventory.getSize() - (getCategory().hasGoBackArrow() ? 5 : 6);
        String configPath;
        boolean toggle;
        if (player.hasGadgetsEnabled()) {
            configPath = "Categories.Gadgets-Item.When-Enabled";
            toggle = false;
        } else {
            configPath = "Categories.Gadgets-Item.When-Disabled";
            toggle = true;
        }
        String msg = MessageManager.getMessage((toggle ? "Enable" : "Disable") + "-Gadgets");
        ClickRunnable run = data -> {
            player.setGadgetsEnabled(!player.hasGadgetsEnabled());
            putToggleGadgetsItems(inventory, player);
        };
        putItem(inventory, slot, ItemFactory.rename(ItemFactory.getItemStackFromConfig(configPath), msg), run);
    }

    @Override
    protected ItemStack filterItem(ItemStack itemStack, GadgetType gadgetType, CosmeticPlayer player) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (UltraCosmeticsData.get().isAmmoEnabled() && gadgetType.requiresAmmo()) {
            List<String> loreList = new ArrayList<>();
            if (itemMeta.hasLore()) {
                loreList = itemMeta.getLore();
            }

            loreList.add("");
            int ammo = player.getAmmo(gadgetType.toString().toLowerCase());
            loreList.add(MessageManager.getMessage("Ammo").replace("%ammo%", "" + ammo));
            loreList.add(MessageManager.getMessage("Right-Click-Buy-Ammo"));

            if (SettingsManager.getConfig().getBoolean("Ammo-System-For-Gadgets.Show-Ammo-In-Menu-As-Item-Amount")
                    && !(player.getCurrentGadget() != null
                    && player.getCurrentGadget().getType() == gadgetType)) {
                itemStack.setAmount(Math.max(1, Math.min(64, ammo)));
            }
            itemMeta.setLore(loreList);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public List<GadgetType> enabled() {
        return GadgetType.enabled();
    }

    @Override
    protected void toggleOn(CosmeticPlayer cosmeticPlayer, GadgetType gadgetType, UltraCosmetics ultraCosmetics) {
        gadgetType.equip(cosmeticPlayer, ultraCosmetics);
    }

    @Override
    protected void toggleOff(CosmeticPlayer cosmeticPlayer) {
        cosmeticPlayer.removeGadget();
    }

    @Override
    protected Cosmetic getCosmetic(CosmeticPlayer cosmeticPlayer) {
        return cosmeticPlayer.getCurrentGadget();
    }
}
