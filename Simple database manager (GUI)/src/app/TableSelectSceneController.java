package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableSelectSceneController {

    //main stuff
    @FXML
    private Label label1;
    @FXML
    private VBox vBox;
    @FXML
    private ScrollPane scrollPane;

    //elements for resizing
    @FXML
    private TextField columnCountField;
    @FXML
    private TextField rowCountField;
    @FXML
    private Button minusColumnBtn;
    @FXML
    private Button plusColumnBtn;
    @FXML
    private Button minusRowsBtn;
    @FXML
    private Button plusRowsBtn;

    //elements for SQL statements
    //@FXML
    //private Button SQLquerryBtn;
    @FXML
    private TextArea sqlTextArea;

    //elements for mode switching
    @FXML
    private Button modeButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ArrayList<HBox> rows;

    private int mode;

    //resizing
    private int columnView;
    private int rowView;
    private double spacing = 5;

    public void display(String db) {

        mode = 0;

        rows = new ArrayList<>();
        label1.setText("Database in use: " + db);

        minusColumnBtn.setOnAction(actionEvent -> changeColumnCount(minusColumnBtn));
        plusColumnBtn.setOnAction(actionEvent -> changeColumnCount(plusColumnBtn));
        minusRowsBtn.setOnAction(actionEvent -> changeRowCount(minusRowsBtn));
        plusRowsBtn.setOnAction(actionEvent -> changeRowCount(plusRowsBtn));

        updateList();

    }

    public void logout(ActionEvent event) {
        Main.logout(event);
    }

    public void updateList() { //method for updating database list

        if (vBox != null) {
            if (vBox.getChildren().stream().count() > 0) {
                vBox.getChildren().clear();

            }
        }

        columnView = Integer.valueOf(columnCountField.getText());
        rowView = Integer.valueOf(rowCountField.getText());

        vBox.setSpacing(spacing);
        vBox.setPadding(new Insets(spacing,spacing,spacing*3,spacing));

        try {
            createList(Main.getTables());  //gets the list of tables in the database from main
            updateMode();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createList(List list) { //creates buttons from a list of strings

        if (list.size() > 0) {

            int countPos = 0;

            while (true) {

                HBox hBox = new HBox();

                if (list.size() - countPos < 0) {
                    break;
                }

                for (int i = 0; i < columnView; i++) {

                    if (i+countPos<list.size()) {

                        Button button = new Button(list.get(i+countPos).toString());
                        //setting propertise of the button
                        button.setPrefWidth(scrollPane.getPrefWidth()/columnView-spacing);
                        button.setPrefHeight(scrollPane.getPrefHeight()/rowView-spacing);
                        button.setOnAction(actionEvent -> click(button));
                        hBox.getChildren().add(button);
                        hBox.setSpacing(spacing);
                    }

                    if (i+1 == columnView) {
                        vBox.getChildren().add(hBox);

                        countPos = countPos + columnView;

                    }
                }
            }
        }
    }

    public void createTable(ActionEvent event) {

        try {
            //loading fxml files for login scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("createNewTable.fxml"));
            root = loader.load();

            Stage stage2 = new Stage();
            stage2.setTitle("Table creator");

            scene = new Scene(root);

            stage2.setScene(scene);
            stage2.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void click(Button button) {

        String table = button.getText();

        if (mode == 0) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("tableEditScene.fxml"));
                root = loader.load();

                TableEditSceneController tableEditSceneController = loader.getController();
                tableEditSceneController.display(table);

                stage = (Stage) (button.getScene().getWindow());
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Are you sure you want to delete " + table + "?",
                    ButtonType.OK,
                    ButtonType.CANCEL);
            alert.setTitle("Delete confirmation");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                try {
                    Main.query("DROP TABLE " + table);
                    updateList();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateMode() {
        if (mode == 1) {
            modeButton.setText("Select mode");

            for (Node node : vBox.getChildren()) {
                if (node instanceof HBox) {
                    for (Node node2 : ((HBox) node).getChildren()) {
                        if (node2 instanceof Button) {
                            node2.setStyle("-fx-background-color: rgba(150, 0, 0, 0.5);");
                        }
                    }
                }
            }

        } else {
            modeButton.setText("Delete mode");

            for (Node node : vBox.getChildren()) {
                if (node instanceof HBox) {
                    for (Node node2 : ((HBox) node).getChildren()) {
                        if (node2 instanceof Button) {
                            node2.setStyle(null);
                        }
                    }
                }
            }
        }
    }

    public void switchMode() {
        if (mode == 0) {
            mode = 1;
            updateMode();

        } else {
            mode = 0;
            updateMode();
        }
    }

    public void showDatabases(ActionEvent event) {

        try {
            //loading fxml files for login scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("databaseSelectScene.fxml"));
            root = loader.load();

            DatabaseSceneController databaseSceneController = loader.getController();
            databaseSceneController.display();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeColumnCount(Button button) {

        int count = Integer.valueOf(columnCountField.getText());

        if (button.getText().equals("+")) {
            count++;
            columnCountField.setText(String.valueOf(count));
        } else if (button.getText().equals("-")){
            if (count>1) {
                count--;
                columnCountField.setText(String.valueOf(count));
            }
        }

        updateList();
    }

    public void changeRowCount(Button button) {

        int count = Integer.valueOf(rowCountField.getText());

        if (button.getText().equals("+")) {
            count++;
            rowCountField.setText(String.valueOf(count));
        } else if (button.getText().equals("-")){
            if (count>1) {
                count--;
                rowCountField.setText(String.valueOf(count));
            }
        }

        updateList();
    }

    public void SQLquery() {

        try {

            Main.query(sqlTextArea.getText());
            updateList();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
