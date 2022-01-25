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

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DatabaseSceneController {

    //main stuff
    @FXML
    private Label label1;
    //@FXML
    //private Button logoutBtn;
    @FXML
    private TextField userInput;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;

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

    //elements for mode switching
    @FXML
    private Button modeButton;

    //elements for SQL statements
    //@FXML
    //private Button SQLquerryBtn;
    @FXML
    private TextArea sqlTextArea;

    private int mode = 0;

    private int columnView;
    private int rowView;
    private double spacing = 5;

    public void display() {

        label1.setText("Hello: " + Main.getUsername());
        updateList();

        minusColumnBtn.setOnAction(actionEvent -> changeColumnCount(minusColumnBtn));
        plusColumnBtn.setOnAction(actionEvent -> changeColumnCount(plusColumnBtn));
        minusRowsBtn.setOnAction(actionEvent -> changeRowCount(minusRowsBtn));
        plusRowsBtn.setOnAction(actionEvent -> changeRowCount(plusRowsBtn));

    }

    public void logout(ActionEvent event) {
        Main.logout(event);
    }

    public void updateList() { //method for updating database list

        if ((long) vBox.getChildren().size() > 0) {
            vBox.getChildren().clear();
        }

        columnView = Integer.parseInt(columnCountField.getText());
        rowView = Integer.parseInt(rowCountField.getText());

        vBox.setSpacing(spacing);
        vBox.setPadding(new Insets(spacing, spacing, spacing * 3, spacing));

        try {
            createList(Main.getDatabases());
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

                    if (i + countPos < list.size()) {

                        Button button = new Button(list.get(i + countPos).toString());
                        //setting propertise of the button
                        button.setPrefWidth(scrollPane.getPrefWidth() / columnView - spacing);
                        button.setPrefHeight(scrollPane.getPrefHeight() / rowView - spacing);
                        button.setOnAction(actionEvent -> click(button));
                        hBox.getChildren().add(button);
                        hBox.setSpacing(spacing);
                    }

                    if (i + 1 == columnView) {
                        vBox.getChildren().add(hBox);

                        countPos = countPos + columnView;

                    }
                }
            }
        }
    }

    public void createDatabase() { //takes user input in the textfield and makes a new db

        String dbName = userInput.getText();

        try {
            userInput.clear();
            Main.query("CREATE DATABASE " + dbName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateList();
    }

    public void click(Button button) {

        if (mode == 0) {

            try {
                Main.query("USE " + button.getText());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("tableSelectScene.fxml"));
                Parent root = loader.load();

                TableSelectSceneController tableSceneController = loader.getController();
                tableSceneController.display(button.getText());

                Stage stage = (Stage) (button.getScene().getWindow());
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Are you sure you want to delete " + button.getText() + "?",
                    ButtonType.OK,
                    ButtonType.CANCEL);
            alert.setTitle("Delete confirmation");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                try {
                    Main.query("DROP DATABASE " + button.getText());
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

    public void changeColumnCount(Button button) {

        int count = Integer.parseInt(columnCountField.getText());

        if (button.getText().equals("+")) {
            count++;
            columnCountField.setText(String.valueOf(count));
        } else if (button.getText().equals("-")) {
            if (count > 1) {
                count--;
                columnCountField.setText(String.valueOf(count));
            }
        }

        updateList();
    }

    public void changeRowCount(Button button) {

        int count = Integer.parseInt(rowCountField.getText());

        if (button.getText().equals("+")) {
            count++;
            rowCountField.setText(String.valueOf(count));
        } else if (button.getText().equals("-")) {
            if (count > 1) {
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

