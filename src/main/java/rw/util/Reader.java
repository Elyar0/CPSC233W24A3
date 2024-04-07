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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        try {

            int row = 0;
            int column = 0;

            java.io.FileReader reader = new java.io.FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(reader);
            String ldata = bufferedReader.readLine();

            Pattern MapPattern = Pattern.compile("^(\\d+)$");
            Matcher MapRawData = MapPattern.matcher(ldata);
            if(MapRawData.find()) row = Integer.parseInt(MapRawData.group(1));
            ldata = bufferedReader.readLine();
            MapRawData = MapPattern.matcher(ldata);
            if(MapRawData.find()) column = Integer.parseInt(MapRawData.group(1));
            Battle battle = new Battle(row, column);

            ldata = bufferedReader.readLine();

            while (ldata != null){
                Pattern locationPattern = Pattern.compile("^(\\d+),(\\d+)(.*)");
                Matcher locationRawData = locationPattern.matcher(ldata);
                if(locationRawData.find()){
                    int fRow = Integer.parseInt(locationRawData.group(1));
                    int fColumn = Integer.parseInt(locationRawData.group(2));

                    String rData = locationRawData.group(3);

                    Pattern wallPattern = Pattern.compile("^,WALL");
                    Matcher wallRawData = wallPattern.matcher(rData);
                    Pattern maximalPattern = Pattern.compile("^,MAXIMAL,(\\w+),(.*),(\\d+),(\\d+),(\\d+)");
                    Matcher maximalRawData = maximalPattern.matcher(rData);
                    Pattern predaconPattern = Pattern.compile("^,PREDACON,(\\w+),(.*),(\\d+),(\\w+)");
                    Matcher predaconRawData = predaconPattern.matcher(rData);

                    if(wallRawData.find()){
                        battle.addEntity(fRow, fColumn, Wall.getWall());
                    }
                    else if(maximalRawData.find()){
                        String stringSymbol = maximalRawData.group(1);
                        char symbol = stringSymbol.charAt(0);
                        String name = maximalRawData.group(2);
                        int health = Integer.parseInt(maximalRawData.group(3));
                        int attack = Integer.parseInt(maximalRawData.group(4));
                        int armor = Integer.parseInt(maximalRawData.group(5));
                        Maximal maximal = new Maximal(symbol, name, health, attack, armor);
                        battle.addEntity(fRow, fColumn, maximal);
                    }
                    else if(predaconRawData.find()){
                        String stringSymbol = predaconRawData.group(1);
                        char symbol = stringSymbol.charAt(0);
                        String name = predaconRawData.group(2);
                        int health = Integer.parseInt(predaconRawData.group(3));
                        String stringWeapon = predaconRawData.group(4);
                        char weapon = stringWeapon.charAt(0);
                        PredaCon predaCon = new PredaCon(symbol, name, health, WeaponType.getWeaponType(weapon));
                        battle.addEntity(fRow, fColumn, predaCon);
                    }
                }
                ldata = bufferedReader.readLine();
            }

            reader.close();
            return battle;
        } catch (IOException e) {
            e.printStackTrace();
            // Return null if failed loading battle
            return null;
        }
    }
}
