package rw.util;

import rw.battle.Battle;
import rw.battle.Entity;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {

    public static void saveBattle(Battle battle, File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            // Assuming the first two lines should be the dimensions of the battle.
            bw.write(battle.getRows() + "\n");
            bw.write(battle.getColumns() + "\n");

            // place holder

            for (Entity entity : entities) {
                String line = serializeEntity(entity);
                bw.write(line + "\n");
            }
        }
    }

    private static String serializeEntity(Entity entity) {
        // place holder
        String type = entity.getClass().getSimpleName();

        return ""; // Return the entity string
    }
}
