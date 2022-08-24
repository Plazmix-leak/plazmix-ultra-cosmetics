package net.plazmix.ultracosmetics.cosmetics.elements.gadgets;

import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.elements.gadgets.impl.*;
import net.plazmix.ultracosmetics.cosmetics.type.CosmeticMatType;
import net.plazmix.ultracosmetics.cosmetics.type.CosmeticType;
import net.plazmix.ultracosmetics.util.ServerVersion;
import net.plazmix.ultracosmetics.util.UCMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Gadget types.
 *
 * @author iSach
 * @since 12-01-2015
 */
public class GadgetType extends CosmeticMatType<Gadget> {

    private final static List<GadgetType> ENABLED = new ArrayList<>();
    private final static List<GadgetType> VALUES = new ArrayList<>();

    public static List<GadgetType> enabled() {
        return ENABLED;
    }

    public static List<GadgetType> values() {
        return VALUES;
    }

    public static GadgetType valueOf(String s) {
        return valueOf(s, false);
    }

    public static GadgetType valueOf(String s, boolean onlyEnabled) {
        s = s.toLowerCase();
        String finalS = s;
        Stream<GadgetType> stream = VALUES.stream().filter(gadgetType -> gadgetType.getSignature().equalsIgnoreCase(finalS));
        if(onlyEnabled) {
            stream.filter(gadgetType -> gadgetType.isEnabled());
        }
        Optional<GadgetType> optionalType = stream.findFirst();
        if(optionalType.isPresent()) {
            return optionalType.get();
        } else {
            stream = VALUES.stream().filter(gadgetType -> gadgetType.getSignature().toLowerCase().startsWith(finalS));
            if(onlyEnabled) {
                stream.filter(gadgetType -> gadgetType.isEnabled());
            }
            Optional<GadgetType> bestMatchOptional = stream.findFirst();
            if(bestMatchOptional.isPresent())
                return bestMatchOptional.get();
            return null;
        }
    }

    public static void checkEnabled() {
        ENABLED.addAll(values().stream().filter(CosmeticType::isEnabled).collect(Collectors.toList()));
    }

    private double countdown;

    GadgetType(UCMaterial material, double countdown, String signature, Class<? extends Gadget> clazz, ServerVersion baseVersion, boolean isEnable) {
        super(Category.GADGETS, signature, material, clazz, baseVersion, isEnable);
        this.countdown = countdown;

        VALUES.add(this);
    }


    public double getCountdown() {
        return countdown;
    }

    public static void register() {
        new GadgetType(UCMaterial.IRON_HORSE_ARMOR, 8, "GADGET_BATBLASTER",  GadgetBatBlaster.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.COOKED_CHICKEN, 6, "GADGET_CHICKENATOR", GadgetChickenator.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.BEACON, 45, "GADGET_DISCOBALL", GadgetDiscoBall.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.ENDER_PEARL, 2, "GADGET_ETHEREALPEARL", GadgetEtherealPearl.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.TRIPWIRE_HOOK, 5, "fleshhook", GadgetFleshHook.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.MELON, 2, "melonthrower", GadgetMelonThrower.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.PACKED_ICE, 12, "blizzardblaster", GadgetBlizzardBlaster.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.COMPARATOR, 2, "portalgun", GadgetPortalGun.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.SHEARS, 25, "explosivesheep", GadgetExplosiveSheep.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.DIAMOND_HORSE_ARMOR, 0.5, "paintballgun", GadgetPaintballGun.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.IRON_AXE, 8, "thorhammer", GadgetThorHammer.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.ENDER_EYE, 30, "antigravity", GadgetAntiGravity.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.FIREWORK_STAR, 15, "smashdown",  GadgetSmashDown.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.FIREWORK_ROCKET, 60, "rocket", GadgetRocket.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.WATER_BUCKET, 12, "tsunami", GadgetTsunami.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.TNT, 10, "tnt", GadgetTNT.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.BLAZE_ROD, 4, "fungun", GadgetFunGun.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.LEAD, 60, "parachute", GadgetParachute.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.DIAMOND_HOE, 3, "quakegun", GadgetQuakeGun.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.SKELETON_SKULL, 45, "ghostparty",  GadgetGhostParty.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.FIREWORK_ROCKET, 0.2, "firework", GadgetFirework.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.FERN, 20, "christmastree", GadgetChristmasTree.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.ICE, 8, "freezecannon", GadgetFreezeCannon.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.SNOWBALL, 0.5, "snowball", GadgetSnowball.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.GOLDEN_CARROT, 2, "partypopper", GadgetPartyPopper.class, ServerVersion.v1_8_R1, true);

        new GadgetType(UCMaterial.LIGHT_BLUE_WOOL, 25, "colorbomb", GadgetColorBomb.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.BLUE_WOOL, 75, "trampoline", GadgetTrampoline.class, ServerVersion.v1_8_R1, true);
        new GadgetType(UCMaterial.BLACK_TERRACOTTA, 35, "blackhole", GadgetBlackHole.class, ServerVersion.v1_8_R1, true);

        /*if (VersionManager.IS_VERSION_1_13) {
			new GadgetType(BlockUtils.getBlockByColor("WOOL", (byte) 3), (byte) 3, 25, "ultracosmetics.gadgets.colorbomb", "ColorBomb", "&7&oA colorful bomb!", GadgetColorBomb.class, ServerVersion.v1_13_R1);
			new GadgetType(BlockUtils.getBlockByColor("WOOL", (byte) 11), (byte) 11, 75, "ultracosmetics.gadgets.trampoline", "Trampoline", "&7&oConstructs a trampoline!" + "\n&7&othat sends you and your\n&7&ofriends into air!", GadgetTrampoline.class, ServerVersion.v1_13_R1);
			new GadgetType(BlockUtils.getBlockByColor("STAINED_CLAY", (byte) 15), (byte) 15, 35, "ultracosmetics.gadgets.blackhole", "BlackHole", "&7&oYou should not get caught by it..", GadgetBlackHole.class, ServerVersion.v1_13_R1);
		} else {
			new GadgetType(BlockUtils.getBlockByColor("WOOL", (byte) 3), (byte) 3, 25, "ultracosmetics.gadgets.colorbomb", "ColorBomb", "&7&oA colorful bomb!", GadgetColorBomb.class, ServerVersion.v1_8_R1);
			new GadgetType(BlockUtils.getBlockByColor("WOOL", (byte) 11), (byte) 11, 75, "ultracosmetics.gadgets.trampoline", "Trampoline", "&7&oConstructs a trampoline!" + "\n&7&othat sends you and your\n&7&ofriends into air!", GadgetTrampoline.class, ServerVersion.v1_8_R1);
			new GadgetType(BlockUtils.getBlockByColor("STAINED_CLAY", (byte) 15), (byte) 15, 35, "ultracosmetics.gadgets.blackhole", "BlackHole", "&7&oYou should not get caught by it..", GadgetBlackHole.class, ServerVersion.v1_8_R1);
		}*/

    }
}
