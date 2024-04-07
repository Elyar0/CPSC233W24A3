package rw.util;

import javafx.scene.control.Button;
import rw.battle.Battle;
import rw.battle.Entity;
import rw.battle.Maximal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Writer {

    public static void saveBattle(Battle battle, File file) throws IOException {
        System.out.println(file.getName());
        StringBuilder sb = new StringBuilder();

        int rows = battle.getRows();
        int columns = battle.getColumns();

        sb.append(rows);
        sb.append("\n");
        sb.append(columns);
        sb.append("\n");

        for(int row = 0; row < rows; row++){
            for (int column = 0; column < columns; column++) {
                Entity entity = battle.getEntity(row, column);
                if(entity != null){
                    System.out.println(entity);

                    Pattern wallPattern = Pattern.compile("^rw.battle.Wall");
                    Matcher wallRawData = wallPattern.matcher(entity.toString());
//                    Pattern maximalPattern = Pattern.compile("^Maxi\\(\\d+\\)\\s+(\\w+)\\s+(\\)");
                    Pattern maximalPattern = Pattern.compile("^Maxi");
                    Matcher maximalRawData = maximalPattern.matcher(entity.toString());
//                    Pattern predaconPattern = Pattern.compile("^Pred\\(\\d+\\)");
                    Pattern predaconPattern = Pattern.compile("^Pred");
                    Matcher predaconRawData = predaconPattern.matcher(entity.toString());

                    if(wallRawData.find()){
                        sb.append(row + "," + column + ",WALL\n");
                    }
                    else if(maximalRawData.find()){

                    }
                    else if(predaconRawData.find()){

                    }

                }
            }
        }

        String finalString = sb.toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(finalString);
            System.out.println("String saved to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        System.out.println(finalString);  // Output: Hello World
    }

   // private static String serializeEntity(Entity entity) {
        // place holder
     //   String type = entity.getClass().getSimpleName();

       // return ""; // Return the entity string
    //}
}
