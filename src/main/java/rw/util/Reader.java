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

            //




            return battle;
        } catch (IOException e) {
            e.printStackTrace();
            // Return null if failed loading battle
            return null;
        }
    }
}
