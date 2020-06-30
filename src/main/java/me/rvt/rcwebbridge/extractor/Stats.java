package me.rvt.rcwebbridge.extractor;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Stats extends Thread {
    Player p;
    Plugin plugin;

    public Stats(Player p) {
        this.p = p;
        this.plugin = Bukkit.getPluginManager().getPlugin("RCwebBridge");

        this.start();
    }

    public void run() {
        ResultSet rSet = null;
        Statement stmt = null;
        Connection c = null;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:data/Data.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();

            rSet = stmt.executeQuery(
                    String.format("SELECT username FROM Players WHERE username = '%s';", p.getName())
            );

            if(rSet.isClosed())
                stmt.executeUpdate("INSERT INTO Players VALUES " + setStats("NULL", 0));
            else{
                rSet = stmt.executeQuery(
                        String.format("SELECT email, lootboxes FROM Players WHERE username = '%s'", p.getName())
                );

                stmt.executeUpdate("REPLACE INTO Players VALUES " +
                        setStats(rSet.getString("email"), rSet.getInt("lootboxes"))
                );
            }

            c.commit();
        } catch (Exception e) {
            System.out.println("[RCwebBridge]" + e.getMessage());
        }
        finally {
            try{
                if(rSet != null && !rSet.isClosed())
                    rSet.close();
                if(stmt != null && !stmt.isClosed())
                    stmt.close();
                if(c != null && !c.isClosed())
                    c.close();
            }
            catch (Exception ignored) {}
        }
    }

    private String setStats(String email, int lootboxes) {
        return String.format(
                "('%s', '%s', '%s', '%s', '%s', %d, %d, %d, %.2f, %d, %d, %d, '%s', '%s', %d, %d, %d, %d, %d, %d)",
                p.getName(), email, getPH("%essentials_nickname%"), getPH("%player_uuid%"), getPH("%luckperms_primary_group_name%"),
                Integer.parseInt(getPH("%statistic_player_kills%")), Integer.parseInt(getPH("%statistic_deaths%")),
                Integer.parseInt(getPH("%statistic_damage_dealt%")), Float.parseFloat(getPH("%vault_eco_balance_fixed%")),
                Integer.parseInt(getPH("%player_total_exp%")), Integer.parseInt(getPH("%player_level%")),
                Integer.parseInt(getPH("%statistic_hours_played%")), getPH("%player_first_join_date%"),
                getPH("%lastloginapi_last_logout_date%"), Integer.parseInt(getPH("%statistic_mine_block%")),
                (
                    Integer.parseInt(getPH("%statistic_boat_one_cm%")) + Integer.parseInt(getPH("%statistic_climb_one_cm%"))
                    + Integer.parseInt(getPH("%statistic_fly_one_cm%")) + Integer.parseInt(getPH("%statistic_horse_one_cm%"))
                    + Integer.parseInt(getPH("%statistic_minecart_one_cm%")) + Integer.parseInt(getPH("%statistic_pig_one_cm%"))
                    + Integer.parseInt(getPH("%statistic_sprint_one_cm%")) + Integer.parseInt(getPH("%statistic_swim_one_cm%"))
                    + Integer.parseInt(getPH("%statistic_walk_one_cm%")) + Integer.parseInt(getPH("%statistic_aviate_one_cm%"))
                    + Integer.parseInt(getPH("%statistic_crouch_one_cm%"))
                ) / 100,
                Integer.parseInt(getPH("%statistic_craft_item%")), Integer.parseInt(getPH("%statistic_animals_bred%")),
                Integer.parseInt(getPH("%essentials_homes_set%")), lootboxes
        );
    }

    private String getPH(String ph) {
        return PlaceholderAPI.setPlaceholders(p, ph);
    }
}