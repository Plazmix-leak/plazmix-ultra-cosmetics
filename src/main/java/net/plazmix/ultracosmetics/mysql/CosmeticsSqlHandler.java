package net.plazmix.ultracosmetics.mysql;

import net.plazmix.coreconnector.CoreConnector;
import net.plazmix.utility.JsonUtil;
import net.plazmix.utility.player.PlazmixUser;

public final class CosmeticsSqlHandler {

    // ============================================================================================================================= //

    public static final CosmeticsSqlHandler INSTANCE = new CosmeticsSqlHandler();

    private static final String INSERT_COSMETICS_QUERY   = "INSERT INTO `PlazmixUltraCosmetics` VALUES (?, ?) ON DUPLICATE KEY UPDATE `playerID`=?";

    // ============================================================================================================================= //


    public CosmeticsProfileInformation loadDataFromPlayer(int plazmixId) {
        return CoreConnector.getInstance().getMysqlConnection().executeQuery(false,"SELECT * FROM `PlazmixUltraCosmetics` WHERE playerID = ?;", resultSet -> JsonUtil.fromJson(resultSet.getString("Payload"), CosmeticsProfile.class), plazmixId);
    }

    public void save(PlazmixUser user, CosmeticsProfileInformation profile) {
        String json = JsonUtil.toJson(profile);
        CoreConnector.getInstance().getMysqlConnection().execute(true, INSERT_COSMETICS_QUERY, json, user.getPlayerId());
    }

    public static void createTable() {
        CoreConnector.getInstance().getMysqlConnection().createTable("PlazmixUltraCosmetics", "`playerID` INT NOT NULL PRIMARY KEY, `Payload` LONGTEXT NOT NULL");
    }


}
