package net.plazmix.ultracosmetics.menu;

import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.config.MessageManager;
import net.plazmix.ultracosmetics.config.SettingsManager;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.cosmetics.type.CosmeticMatType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.util.UCMaterial;
import net.plazmix.utility.player.PlazmixUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * A cosmetic menu.
 *
 * @author iSach
 * @since 08-09-2016
 */
public abstract class CosmeticMenu<T extends CosmeticMatType> extends Menu {

    public final static int[] COSMETICS_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };


    private Category category;

    public CosmeticMenu(UltraCosmetics ultraCosmetics, Category category) {
        super(ultraCosmetics);
        this.category = category;
    }

    @Override
    public void open(CosmeticPlayer player) {
        open(player, 1);
    }

    public void open(CosmeticPlayer player, int page) {
        if (page > getMaxPages()) {
            page = getMaxPages();
        }
        if (page < 1) {
            page = 1;
        }

        Inventory inventory = Bukkit.createInventory(new CosmeticsInventoryHolder(), getSize(), getMaxPages() == 1 ? getName() : getName(page));

        // Cosmetic items.
        int i = 0;
        int from = 21 * (page - 1) + 1;
        int to = 21 * page;
        for (int h = from; h <= to; h++) {
            if (h > enabled().size()) {
                break;
            }

            T cosmeticMatType = enabled().get(h - 1);

            if (!cosmeticMatType.isEnabled()) {
                continue;
            }

            if (SettingsManager.getConfig().getBoolean("No-Permission.Dont-Show-Item") && !PlazmixCoreApi.GROUP_API.isDonate(player.getBukkitPlayer().getName())) {
                continue;
            }

            if (SettingsManager.getConfig().getBoolean("No-Permission.Custom-Item.enabled")
                    && !PlazmixCoreApi.GROUP_API.isDonate(player.getBukkitPlayer().getName())) {
                UCMaterial material = UCMaterial.matchUCMaterial(SettingsManager.getConfig().getString("No-Permission.Custom-Item.Type"));
                String name = ChatColor.translateAlternateColorCodes('&', SettingsManager.getConfig().getString("No-Permission.Custom-Item.Name")).replace("{cosmetic-name}", cosmeticMatType.getName());
                List<String> npLore = SettingsManager.getConfig().getStringList("No-Permission.Custom-Item.Lore");
                String[] array = new String[npLore.size()];
                npLore.toArray(array);
                putItem(inventory, COSMETICS_SLOTS[i], ItemFactory.create(material, name, array), clickData -> {
                    Player clicker = clickData.getClicker().getBukkitPlayer();

                    PlazmixUser.of(clicker.getName()).localization().sendMessage("NO_PERM");
                    clicker.closeInventory();
                });
                i++;
                continue;
            }

            String toggle = category.getActivateMenu();

            if (getCosmetic(player) != null && getCosmetic(player).getType() == cosmeticMatType) {
                toggle = category.getDeactivateMenu();
            }

            String typeName = getTypeName(cosmeticMatType, player);

            ItemStack is = ItemFactory.create(cosmeticMatType.getMaterial(), toggle + " " + typeName);
            if (getCosmetic(player) != null && getCosmetic(player).getType() == cosmeticMatType) {
                is = ItemFactory.addGlow(is);
            }

            ItemMeta itemMeta = is.getItemMeta();
            List<String> loreList = new ArrayList<>();

            if (cosmeticMatType.showsDescription()) {
                loreList.add("");
                loreList.addAll(cosmeticMatType.getDescription());
                loreList.add("");
            }

            if (SettingsManager.getConfig().getBoolean("No-Permission.Show-In-Lore")) {
                PlazmixUser plazmixUser = PlazmixUser.of(player.getBukkitPlayer().getName());

                String yesOrNo = PlazmixCoreApi.GROUP_API.isDonate(plazmixUser.getName()) ? "Yes" : "No";
                String s = SettingsManager.getConfig().getString("No-Permission.Lore-Message-" + yesOrNo);
                loreList.add(ChatColor.translateAlternateColorCodes('&', s));
            }

            itemMeta.setLore(loreList);

            is.setItemMeta(itemMeta);
            is = filterItem(is, cosmeticMatType, player);
            putItem(inventory, COSMETICS_SLOTS[i], is, (data) -> {
                CosmeticPlayer cosmeticPlayer = data.getClicker();
                ItemStack clicked = data.getClicked();
                int currentPage = getCurrentPage(cosmeticPlayer);

                if (UltraCosmeticsData.get().shouldCloseAfterSelect()) {
                    cosmeticPlayer.getBukkitPlayer().closeInventory();
                }

                if (UltraCosmeticsData.get().isAmmoEnabled() && data.getAction() == InventoryAction.PICKUP_HALF) {
                    StringBuilder sb = new StringBuilder();
                    for (int k = 1; k < clicked.getItemMeta().getDisplayName().split(" ").length; k++) {
                        sb.append(clicked.getItemMeta().getDisplayName().split(" ")[k]);

                        try {
                            if (clicked.getItemMeta().getDisplayName().split(" ")[k + 1] != null)
                                sb.append(" ");
                        } catch (Exception ignored) {
                        }
                    }

                    if (getCosmetic(cosmeticPlayer) == null) {
                        toggleOff(cosmeticPlayer);
                    }
                    toggleOn(cosmeticPlayer, cosmeticMatType, getUltraCosmetics());

                    if (getCategory() == Category.GADGETS) {
                        if (cosmeticPlayer.getCurrentGadget().getType().requiresAmmo()) {
                            cosmeticPlayer.getCurrentGadget().lastPage = currentPage;
                            cosmeticPlayer.getCurrentGadget().openAmmoPurchaseMenu();
                            cosmeticPlayer.getCurrentGadget().openGadgetsInvAfterAmmo = true;
                        }
                    }
                    return;
                }

                if (clicked.getItemMeta().getDisplayName().startsWith(category.getDeactivateMenu())) {
                    toggleOff(cosmeticPlayer);
                    if (!UltraCosmeticsData.get().shouldCloseAfterSelect()) {
                        open(cosmeticPlayer, currentPage);
                    }
                } else if (clicked.getItemMeta().getDisplayName().startsWith(category.getActivateMenu())) {
                    toggleOff(cosmeticPlayer);
                    StringBuilder sb = new StringBuilder();
                    String name = clicked.getItemMeta().getDisplayName().replaceFirst(category.getActivateMenu(), "");
                    int j = name.split(" ").length;
                    if (name.contains("(")) {
                        j--;
                    }
                    for (int k = 1; k < j; k++) {
                        sb.append(name.split(" ")[k]);
                        try {
                            if (clicked.getItemMeta().getDisplayName().split(" ")[k + 1] != null)
                                sb.append(" ");
                        } catch (Exception ignored) {
                        }
                    }
                    toggleOn(cosmeticPlayer, cosmeticMatType, getUltraCosmetics());
                    if (category == Category.GADGETS &&
                            cosmeticPlayer.getCurrentGadget() != null &&
                            UltraCosmeticsData.get().isAmmoEnabled() && cosmeticPlayer.getAmmo(cosmeticPlayer.getCurrentGadget().getType().toString().toLowerCase()) < 1 && cosmeticPlayer.getCurrentGadget().getType().requiresAmmo()) {
                        cosmeticPlayer.getCurrentGadget().lastPage = currentPage;
                        cosmeticPlayer.getCurrentGadget().openAmmoPurchaseMenu();
                    } else {
                        if (!UltraCosmeticsData.get().shouldCloseAfterSelect()) {
                            open(cosmeticPlayer, currentPage);
                        }
                    }
                }
            });
            i++;
        }

        // Previous page item.
        if (page > 1) {
            int finalPage = page;
            putItem(inventory, getSize() - 18, ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Previous-Page-Item"),
                    MessageManager.getMessage("Menu.Previous-Page")), (data) -> open(player, finalPage - 1));
        }

        // Next page item.
        if (page < getMaxPages()) {
            int finalPage = page;
            putItem(inventory, getSize() - 10, ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Next-Page-Item"),
                    MessageManager.getMessage("Menu.Next-Page")), (data) -> open(player, finalPage + 1));
        }

        // Clear cosmetic item.
        String message = MessageManager.getMessage(category.getClearConfigPath());
        ItemStack itemStack = ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Clear-Cosmetic-Item"), message);
        putItem(inventory, inventory.getSize() - 4, itemStack, data -> {
            toggleOff(player);
            open(player, getCurrentPage(player));
        });

        // Go Back to Main Menu Arrow.
        if (getCategory().hasGoBackArrow()) {
            ItemStack item = ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Back-Main-Menu-Item"), MessageManager.getMessage("Menu.Main-Menu"));
            putItem(inventory, inventory.getSize() - 6, item, (data) -> getUltraCosmetics().openMainMenu(player));
        }

        putItems(inventory, player, page);
        ItemFactory.fillInventory(inventory);
        player.getBukkitPlayer().openInventory(inventory);
    }

    public T getCosmeticType(String name) {
        for (T effectType : enabled()) {
            if (effectType.getConfigName().replace(" ", "").equals(name.replace(" ", ""))) {
                return effectType;
            }
        }
        return null;
    }

    /**
     * @param cosmeticPlayer The menu owner.
     * @return The current page of the menu opened by ultraPlayer.
     */
    protected int getCurrentPage(CosmeticPlayer cosmeticPlayer) {
        Player player = cosmeticPlayer.getBukkitPlayer();
        String title = player.getOpenInventory().getTitle();
        if (player.getOpenInventory() != null
                && title.startsWith(getName())
                && !title.equals(getName())) {
            String s = player.getOpenInventory().getTitle()
                    .replace(getName() + " " + ChatColor.GRAY + "" + ChatColor.ITALIC + "(", "")
                    .replace("/" + getMaxPages() + ")", "");
            return Integer.parseInt(s);
        }
        return 0;
    }

    /**
     * Gets the max amount of pages.
     *
     * @return the maximum amount of pages.
     */
    protected int getMaxPages() {
        int max = 21;
        int i = enabled().size();
        if (i % max == 0) return i / max;
        double j = i / 21;
        int h = (int) Math.floor(j * 100) / 100;
        return h + 1;
    }

    protected int getItemsPerPage() {
        return 12;
    }

    /**
     * This method can be overridden
     * to modify an itemstack of a
     * category being placed in the
     * inventory.
     *
     * @param itemStack    Item Stack being placed.
     * @param cosmeticType The Cosmetic Type.
     * @param player       The Inventory Opener.
     * @return The new item stack filtered.
     */
    protected ItemStack filterItem(ItemStack itemStack, T cosmeticType, CosmeticPlayer player) {
        return itemStack;
    }

    protected String getTypeName(T cosmeticType, CosmeticPlayer cosmeticPlayer) {
        return cosmeticType.getName();
    }

    /**
     * @param page The page to open.
     * @return The name of the menu with page detailed.
     */
    protected String getName(int page) {
        return MessageManager.getMessage("Menus." + category.getConfigPath()) + " " + ChatColor.GRAY + "" + ChatColor.ITALIC + "(" + page + "/" + getMaxPages() + ")";
    }

    @Override
    protected int getSize() {
        int listSize = enabled().size();
        int slotAmount = 54;
        if (listSize < 22) {
            slotAmount = 54;
        }
        if (listSize < 15) {
            slotAmount = 45;
        }
        if (listSize < 8) {
            slotAmount = 36;
        }
        return slotAmount;
    }

    @Override
    protected void putItems(Inventory inventory, CosmeticPlayer cosmeticPlayer) {
        //--
    }

    /**
     * @return The name of the menu.
     */
    @Override
    protected String getName() {
        return MessageManager.getMessage("Menus." + category.getConfigPath());
    }

    public Category getCategory() {
        return category;
    }

    /**
     * Puts items in the inventory.
     *
     * @param inventory   Inventory.
     * @param cosmeticPlayer Inventory Owner.
     * @param page        Page to open.
     */
    abstract protected void putItems(Inventory inventory, CosmeticPlayer cosmeticPlayer, int page);

    abstract public List<T> enabled();

    abstract protected void toggleOn(CosmeticPlayer cosmeticPlayer, T type, UltraCosmetics ultraCosmetics);

    abstract protected void toggleOff(CosmeticPlayer cosmeticPlayer);

    abstract protected Cosmetic getCosmetic(CosmeticPlayer cosmeticPlayer);
}
