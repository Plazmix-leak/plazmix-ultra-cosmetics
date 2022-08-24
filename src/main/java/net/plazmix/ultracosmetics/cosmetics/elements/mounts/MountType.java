package net.plazmix.ultracosmetics.cosmetics.elements.mounts;

import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.config.MessageManager;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.elements.mounts.impl.*;
import net.plazmix.ultracosmetics.cosmetics.type.CosmeticEntType;
import net.plazmix.ultracosmetics.util.ServerVersion;
import net.plazmix.ultracosmetics.util.UCMaterial;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A cosmetic type.
 *
 * @author iSach
 * @since 12-18-2015
 */
public class MountType extends CosmeticEntType<Mount> {

    private final static List<MountType> ENABLED = new ArrayList<>();
    private final static List<MountType> VALUES = new ArrayList<>();

    public static List<MountType> enabled() {
        return ENABLED;
    }

    public static List<MountType> values() {
        return VALUES;
    }

    public static MountType valueOf(String s) {
        for (MountType mountType : VALUES) {
            if (mountType.getSignature().equalsIgnoreCase(s)) return mountType;
        }
        return null;
    }

    public static void checkEnabled() {
        ENABLED.addAll(values().stream().filter(MountType::isEnabled).collect(Collectors.toList()));
    }

    private int repeatDelay;

    MountType(String signature, UCMaterial material, EntityType entityType, int repeatDelay, Class<? extends Mount> mountClass, ServerVersion baseVersion, Boolean isEnable) {
        super(Category.MOUNTS, signature, material, entityType, mountClass, baseVersion, isEnable);
        this.repeatDelay = repeatDelay;

        VALUES.add(this);
    }

    public int getRepeatDelay() {
        return repeatDelay;
    }

    public static void register() {
        new MountType("DruggedHorse", UCMaterial.SUGAR, EntityType.HORSE, 2, MountDruggedHorse.class, ServerVersion.v1_8_R1, true);
        new MountType("InfernalHorror", UCMaterial.BONE, UltraCosmeticsData.get().getVersionManager().getMounts().getHorrorType(), 2, UltraCosmeticsData.get().getVersionManager().getMounts().getHorrorClass(), ServerVersion.v1_8_R1, true);
        new MountType("GlacialSteed", UCMaterial.PACKED_ICE, EntityType.HORSE, 2, MountGlacialSteed.class, ServerVersion.v1_8_R1, true);
        new MountType("WalkingDead", UCMaterial.ROTTEN_FLESH, UltraCosmeticsData.get().getVersionManager().getMounts().getWalkingDeadType(), 2, UltraCosmeticsData.get().getVersionManager().getMounts().getWalkingDeadClass(), ServerVersion.v1_8_R1, true);
        new MountType("MountOfFire", UCMaterial.BLAZE_POWDER, EntityType.HORSE, 2, MountOfFire.class, ServerVersion.v1_8_R1, true);
        new MountType("Snake", UCMaterial.WHEAT_SEEDS, EntityType.SHEEP, 2, MountSnake.class, ServerVersion.v1_8_R1, true);
        new MountType("Dragon", UCMaterial.DRAGON_EGG, EntityType.ENDER_DRAGON, 1, MountDragon.class, ServerVersion.v1_8_R1, true);
        new MountType("Slime", UCMaterial.SLIME_BALL, EntityType.SLIME, 2, UltraCosmeticsData.get().getVersionManager().getMounts().getSlimeClass(), ServerVersion.v1_8_R1, true);
        new MountType("HypeCart", UCMaterial.MINECART, EntityType.MINECART, 1, MountHypeCart.class, ServerVersion.v1_8_R1, true);
        new MountType("Spider", UCMaterial.COBWEB, EntityType.SPIDER, 2, UltraCosmeticsData.get().getVersionManager().getMounts().getSpiderClass(), ServerVersion.v1_8_R1, true);
        new MountType("Rudolph", UCMaterial.DEAD_BUSH, UltraCosmeticsData.get().getVersionManager().getMounts().getRudolphType(), 2, UltraCosmeticsData.get().getVersionManager().getMounts().getRudolphClass(), ServerVersion.v1_8_R1, true);
        new MountType("MoltenSnake", UCMaterial.MAGMA_CREAM, EntityType.MAGMA_CUBE, 1, MountMoltenSnake.class, ServerVersion.v1_8_R1, true);

        new MountType("MountOfWater", UCMaterial.LIGHT_BLUE_DYE, EntityType.HORSE, 2, MountOfWater.class, ServerVersion.v1_8_R1, true);
        new MountType("NyanSheep", UCMaterial.CYAN_DYE, EntityType.SHEEP, 1, MountNyanSheep.class, ServerVersion.v1_8_R1, true);
        new MountType("EcologistHorse", UCMaterial.GREEN_DYE, EntityType.HORSE, 2, MountEcologistHorse.class, ServerVersion.v1_8_R1, true);

    }
}
