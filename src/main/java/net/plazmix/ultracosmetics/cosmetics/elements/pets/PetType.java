package net.plazmix.ultracosmetics.cosmetics.elements.pets;

import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.impl.*;
import net.plazmix.ultracosmetics.cosmetics.type.CosmeticMatType;
import net.plazmix.ultracosmetics.cosmetics.type.CosmeticType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ServerVersion;
import net.plazmix.ultracosmetics.util.UCMaterial;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Pet types.
 *
 * @author iSach
 * @since 12-20-2015
 */
public final class PetType extends CosmeticMatType<Pet> {

    private final static List<PetType> ENABLED = new ArrayList<>();
    private final static List<PetType> VALUES = new ArrayList<>();

    public static List<PetType> enabled() {
        return ENABLED;
    }

    public static List<PetType> values() {
        return VALUES;
    }

    public static PetType valueOf(String s) {
        for (PetType petType : VALUES) {
            if (petType.getSignature().equalsIgnoreCase(s)) return petType;
        }
        return null;
    }

    public static PetType getByName(String s) {
        try {
            return VALUES.stream().filter(value -> value.getSignature().equalsIgnoreCase(s)).findFirst().get();
        } catch (Exception exc) {
            return null;
        }
    }

    public static void checkEnabled() {
        ENABLED.addAll(values().stream().filter(CosmeticType::isEnabled).collect(Collectors.toList()));
    }

    private EntityType entityType;

    private PetType(String signature, UCMaterial material, EntityType entityType, Class<? extends Pet> clazz, ServerVersion baseVersion, Boolean isEnable) {
        super(Category.PETS, signature, material, clazz, baseVersion, isEnable);

        this.entityType = entityType;
        VALUES.add(this);
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public String getEntityName(CosmeticPlayer player) {
        return String.format("%s %s",
                player.getPlazmixUser().localization().getMessageText(
                        String.format("COSMETIC_PET_NAME_%s", this.getSignature())),
                player.getPlazmixUser().getDisplayName()

                );
    }

    public static void register() {
        new PetType("Piggy", UCMaterial.PORKCHOP, EntityType.PIG, PetPiggy.class, ServerVersion.v1_8_R1, true);

        new PetType("EasterBunny", UCMaterial.CARROT, EntityType.RABBIT, PetEasterBunny.class, ServerVersion.v1_8_R1, true);
        new PetType("Cow", UCMaterial.MILK_BUCKET, EntityType.COW, PetCow.class, ServerVersion.v1_8_R1, true);
        new PetType("Mooshroom", UCMaterial.RED_MUSHROOM, EntityType.MUSHROOM_COW, PetMooshroom.class, ServerVersion.v1_8_R1, true);
        new PetType("Kitty", UCMaterial.TROPICAL_FISH, EntityType.OCELOT, PetKitty.class, ServerVersion.v1_8_R1, true);
        new PetType("Dog", UCMaterial.BONE, EntityType.WOLF, PetDog.class, ServerVersion.v1_8_R1, true);
        new PetType("Chick", UCMaterial.EGG, EntityType.CHICKEN, PetChick.class, ServerVersion.v1_8_R1, true);
        new PetType("Pumpling", UCMaterial.PUMPKIN, EntityType.ZOMBIE, UltraCosmeticsData.get().getVersionManager().getPets().getPumplingClass(), ServerVersion.v1_8_R1, true);
        new PetType("ChristmasElf", UCMaterial.BEACON, EntityType.VILLAGER, PetChristmasElf.class, ServerVersion.v1_8_R1, true);
        new PetType("IronGolem", UCMaterial.IRON_INGOT, EntityType.IRON_GOLEM, PetIronGolem.class, ServerVersion.v1_8_R1, true);
        new PetType("Snowman", UCMaterial.SNOWBALL, EntityType.SNOWMAN, PetSnowman.class, ServerVersion.v1_8_R1, true);
        new PetType("Villager", UCMaterial.EMERALD, EntityType.VILLAGER, PetVillager.class, ServerVersion.v1_8_R1, true);
        new PetType("Bat", UCMaterial.COAL, EntityType.BAT, PetBat.class, ServerVersion.v1_8_R1, true);

        new PetType("PolarBear", UCMaterial.SNOW_BLOCK, EntityType.POLAR_BEAR, PetPolarBear.class, ServerVersion.v1_10_R1, true);
        new PetType("Llama", UCMaterial.RED_WOOL, EntityType.LLAMA, PetLlama.class, ServerVersion.v1_11_R1, true);
        new PetType("Parrot", UCMaterial.COOKIE, EntityType.PARROT, PetParrot.class, ServerVersion.v1_12_R1, true);


        new PetType("Sheep", UCMaterial.WHITE_WOOL, EntityType.SHEEP, PetSheep.class, ServerVersion.v1_8_R1, true);
        new PetType("Wither", UCMaterial.WITHER_SKELETON_SKULL, EntityType.WITHER, PetWither.class, ServerVersion.v1_8_R1, true);

    }
}
