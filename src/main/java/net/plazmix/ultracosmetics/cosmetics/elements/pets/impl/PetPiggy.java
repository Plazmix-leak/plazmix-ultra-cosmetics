package net.plazmix.ultracosmetics.cosmetics.elements.pets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.Pet;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.PetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.util.UCMaterial;

/**
 * Represents an instance of a pig pet summoned by a player.
 *
 * @author iSach
 * @since 08-12-2015
 */
public class PetPiggy extends Pet {
    public PetPiggy(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, ultraCosmetics, PetType.getByName("piggy"), ItemFactory.create(UCMaterial.PORKCHOP, UltraCosmeticsData.get().getItemNoPickupString()));
    }
}
