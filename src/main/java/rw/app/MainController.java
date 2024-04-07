package rw.app;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rw.battle.Battle;
import rw.battle.Entity;
import rw.battle.Maximal;
import rw.battle.PredaCon;
import rw.enums.WeaponType;
import rw.util.Reader;
import rw.util.Writer;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class MainController {



    @FXML private Label statusLabelLeft;
    @FXML private Label statusLabelRight;
    @FXML private TextField entityNameTextField;
    @FXML private TextField entitySymbolTextField;
    @FXML private TextField entityHealthTextField;

    @FXML
    private RadioButton predaconType , maximalType;
    @FXML private ToggleGroup entityToggleGroup;

    @FXML
    private Pane predaconPane, maximalPane;
    @FXML private GridPane battleGrid;
    @FXML private TextField rowsTextField;
    @FXML private TextField columnsTextField;

    @FXML private TextField predaconSymbol;
    @FXML private TextField predaconName;
    @FXML private TextField predaconHealth;
    @FXML private RadioButton predaconClaws, predaconTeeth, predaconLaser;


    @FXML private TextField maximalSymbol;
    @FXML private TextField maximalName;
    @FXML private TextField maximalHealth;
    @FXML private TextField maximalWeapon;
    @FXML private TextField maximalArmor;


    @FXML private TextArea informationArea;



    private Battle battle;

    public MainController() {
    }

    @FXML
    private void initialize() {
    }


    @FXML
    private void handleLoad() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fileChooser.showOpenDialog(null);

        if(file != null){
            battle = Reader.loadBattle(file);
            DisplayMap();
        }
    }

    private void DisplayMap(){
        // Clearing all children
        battleGrid.getChildren().clear();

//        // Clearing column constraints
//        battleGrid.getColumnConstraints().clear();
//
//        // Clearing row constraints
//        battleGrid.getRowConstraints().clear();

        int rows = battle.getRows();
        int columns = battle.getColumns();

        for(int row = 0; row <= rows +1; row++){
            for (int column = 0; column <= columns +1; column++) {

                Button cellButton = new Button();
                cellButton.setPrefSize(50, 50); // Or adjust the size based on your needs
                if(row == 0 || row == rows + 1 || column == 0 || column == columns + 1) cellButton.setText("#");
                else {
                    Entity entity = battle.getEntity(row - 1, column - 1);
//                    System.out.println(battle.getEntity(row - 1, column - 1));
//                    System.out.println(entity);
                    if(entity != null) cellButton.setText(String.valueOf(entity.getSymbol()));
                    else cellButton.setText(".");
                }
                final int fRow = row, fCol = column;
                cellButton.setOnAction(event -> handleCellAction(fRow, fCol));
                battleGrid.add(cellButton, column, row);
            }
        }
    }

    @FXML
    private void handleSave() {
        // Save the current battle configuration
        // TODO: Save the battle to a file
        File file = new File("battle.txt");
        try {
            Writer.saveBattle(battle, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        statusLabelLeft.setText("Saved current battle configuration.");
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // Save the current battle configuration with a new name
            statusLabelLeft.setText("Saved as new battle configuration to " + file.getName());
            // TODO: Save the battle as a new file
        } else {
            statusLabelLeft.setText("Save As operation cancelled.");
        }
    }

    @FXML
    private void handleQuit() {
        Platform.exit();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About RWMapEditor");
        alert.setHeaderText("About RWMapEditor");
        alert.setContentText("Author: [Name Test]\nEmail: [Email test]\nVersion: 1.0\nThis is a Battle editor for PredaCons versus Maximals.");

        alert.showAndWait();
    }

    @FXML
    private void handleCreateNewBattle() {
        try {
            int rows = Integer.parseInt(rowsTextField.getText());
            int columns = Integer.parseInt(columnsTextField.getText());

            battle = new Battle(rows, columns);
            DisplayMap();
            statusLabelLeft.setText("New battle created with rows: " + rows + " and columns: " + columns);
        } catch (NumberFormatException e) {
            statusLabelLeft.setText("Please enter valid integer values for rows and columns.");
        }
    }


    private void handleCellAction(int row, int column) {
        int rows = battle.getRows();
        int columns = battle.getColumns();

        if(row == 0 || row == rows + 1 || column == 0 || column == columns + 1) {
            informationArea.setText("its border");
        }
        else {
            Entity entity = battle.getEntity(row - 1, column - 1);
            if(entity == null) {
                if(predaconType.isSelected()){
                    try {
                        String stringSymbol = predaconSymbol.getText();
                        char symbol = stringSymbol.charAt(0);
                        String name = predaconName.getText();
                        int health = Integer.parseInt(predaconHealth.getText());
                        WeaponType weapon = null;
                        if(predaconTeeth.isSelected()){
                            weapon = WeaponType.TEETH;
                        }
                        else if(predaconClaws.isSelected()){
                            weapon = WeaponType.CLAWS;
                        }
                        else if(predaconLaser.isSelected()){
                            weapon = WeaponType.LASER;
                        }

                        PredaCon predacon = new PredaCon(symbol, name, health, weapon);
                        battle.addEntity(row - 1, column - 1, predacon);
                    }catch (Exception e){
                        statusLabelLeft.setText("empty credentials");
                    }

                }
                else if(maximalType.isSelected()){
                    try {
                        String stringSymbol = maximalSymbol.getText();
                        char symbol = stringSymbol.charAt(0);
                        String name = maximalName.getText();
                        int health = Integer.parseInt(maximalHealth.getText());
                        int strength = Integer.parseInt(maximalWeapon.getText());
                        int armor = Integer.parseInt(maximalArmor.getText());

                        Maximal maximal = new Maximal(symbol, name, health, strength, armor);
                        battle.addEntity(row - 1, column - 1, maximal);
                    }catch (Exception e){
                        statusLabelLeft.setText("empty credentials");
                    }
                }
            }
            if(entity == null) {
                informationArea.setText("null");
            }
            else {
                informationArea.setText(entity.toString());
            }

        }

        DisplayMap();

//        Optional<RadioButton> selectedEntity = Optional.ofNullable((RadioButton) entityToggleGroup.getSelectedToggle());
//        selectedEntity.ifPresent(radioButton -> {
//            // Here you can check which radio button is selected and add the entity to the battle
//            if (radioButton.equals(predaconRadioButton)) {
//                // Add a PredaCon at the specified location
//                statusLabelRight.setText("PredaCon placed at (" + row + ", " + col + ")");
//            } else if (radioButton.equals(maximalRadioButton)) {
//                // Add a Maximal at the specified location
//                statusLabelRight.setText("Maximal placed at (" + row + ", " + col + ")");
//            }
//
//            // Update the view to reflect changes
//            // TODO: Implement the actual logic to update the model and view
//        });
    }
    @FXML
    private void handleAddEntity() {
        String name = entityNameTextField.getText();
        String symbol = entitySymbolTextField.getText();
        int health = Integer.parseInt(entityHealthTextField.getText());

        // Example of adding a PredaCon. You would add similar logic for Maximal.
    }

    private void updateBattleGrid() {
        battleGrid.getChildren().clear();

        if (battle != null) {
            // Example: Assuming each cell in your grid is represented by a Button in the UI.
            for (int row = 0; row < battle.getRows(); row++) {
                for (int col = 0; col < battle.getColumns(); col++) {
                    Button cellButton = new Button();
                    cellButton.setMinSize(50, 50); // Customize as needed

                    String cellSymbol = determineCellSymbol(row, col);
                    cellButton.setText(cellSymbol);

                    final int fRow = row, fCol = col;
                    cellButton.setOnAction(event -> handleCellAction(fRow, fCol));

                    battleGrid.add(cellButton, col, row);
                }
            }
        }
    }

    private String determineCellSymbol(int row, int col) {
        // Placeholder logic: Determine what symbol or text should be displayed for a cell
        return ""; // Return the appropriate symbol or an empty string
    }

    @FXML
    public void GetRobot(ActionEvent e){

        if(predaconType.isSelected()){
            predaconPane.setVisible(true);
            maximalPane.setVisible(false);
        }
        else if(maximalType.isSelected()){
            predaconPane.setVisible(false);
            maximalPane.setVisible(true);


        }

    }
}