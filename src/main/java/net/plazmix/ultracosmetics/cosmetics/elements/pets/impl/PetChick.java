package net.plazmix.ultracosmetics.cosmetics.elements.pets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.Pet;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.PetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.util.UCMaterial;

/**
 * Represents an instance of a chick pet summoned by a player.
 *
 * @author iSach
 * @since 08-12-2015
 */
public class PetChick extends Pet {
    public PetChick(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, ultraCosmetics, PetType.getByName("chick"), ItemFactory.create(UCMaterial.EGG, UltraCosmeticsData.get().getItemNoPickupString()));
    }
}