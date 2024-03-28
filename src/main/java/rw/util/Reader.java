package rw.util;

import rw.battle.Battle;
import rw.battle.Maximal;
import rw.battle.PredaCon;
import rw.battle.Wall;
import rw.enums.WeaponType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to assist reading in battle file
 *
 * @author Jonathan Hudson
 * @version 1.0
 */
public final class Reader {
    /**
     * Loads battle configuration from a file and initializes a Battle instance with it
     * @param file The file containing the battle configuration
     * @return Battle instance populated with entities defined in the file or null if error occurs
     */
    public static Battle loadBattle(File file) {
        // Reading the battle dimensions rows and columns from the first two lines
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int rows = Integer.parseInt(br.readLine());
            int columns = Integer.parseInt(br.readLine());
            Battle battle = new Battle(rows, columns); // Initialize the battle with dimensions

            String line;
            while ((line = br.readLine()) != null) {
                // Split each line into parts
                String[] parts = line.split(",");
                switch (parts[0]) {
                    case "Maximal":
                        // Create and add a Maximal entity to battle
                        battle.addEntity(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
                                new Maximal(
                                        parts[3].charAt(0), // symbol
                                        parts[4], // name
                                        Integer.parseInt(parts[5]), // health
                                        Integer.parseInt(parts[6]), // weaponStrength
                                        Integer.parseInt(parts[7])  // armorStrength
                                ));
                        break;
                    case "PredaCon":
                        // Create and add a PredaCon entity to battle
                        battle.addEntity(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
                                new PredaCon(
                                        parts[3].charAt(0), // symbol
                                        parts[4], // name
                                        Integer.parseInt(parts[5]), // health
                                        WeaponType.valueOf(parts[6]) // weaponType
                                ));
                        break;
                    case "Wall":
                        // Add a Wall entity to the battle using static
                        battle.addEntity(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Wall.getWall());
                        break;


                }
            }
            return battle;
        } catch (IOException e) {
            e.printStackTrace();
            // Return null if failed loading battle
            return null;
        }
    }
}
