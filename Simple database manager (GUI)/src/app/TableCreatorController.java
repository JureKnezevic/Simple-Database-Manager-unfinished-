package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableCreatorController {

    @FXML
    private VBox vBox;

    @FXML
    private TextField tableNameField;

    ArrayList<BlankColumn> list = new ArrayList<>();

    public void addColumn() { //when add column is pressed

        BlankColumn column = new BlankColumn();
        list.add(column);
        updateList();
    }

    public void updateList() {

        if (vBox!=null) {

            if ((long) vBox.getChildren().size() > 0) {
                vBox.getChildren().clear();
            }

        }

        for (int a = 0; a < list.size(); a++) {

            DeleteButton deleteButton = new DeleteButton();
            deleteButton.setText("\uD83D\uDDD9");
            deleteButton.setDeleteId(a);
            deleteButton.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);");

            deleteButton.setOnAction(ActionEvent -> deleteColumn(deleteButton));

            if (vBox==null) {
                vBox = new VBox();
            }

            HBox column = new HBox();
            column.getChildren().addAll(list.get(a).getColumn(), deleteButton);

            vBox.getChildren().addAll(column);
        }

    }

    public void createTable(ActionEvent actionEvent) {

        StringBuilder columns = new StringBuilder();

        for (BlankColumn column: list) {

            if (!column.getColumnName().equals("") || !column.getDataType().equals("")) {
                if (columns.toString().equals("")) {
                    columns = new StringBuilder(column.getTableColumnString());
                } else {
                    columns.append(",").append(column.getTableColumnString());
                }
            }

        }

        try {
            System.out.println("CREATE TABLE " + tableNameField.getText() + " (" + columns + ")");
            Main.query("CREATE TABLE " + tableNameField.getText() + " (" + columns + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button close = (Button) actionEvent.getSource();
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();

    }

    /*
    make tables clickable -> table editor
    - shows table data
    - each piece of data -> text field so it's editable
    - save button -> drop table and remake a new one?
     */

    /*
    make table creator columns deletable
     */

    public void deleteColumn(DeleteButton button) {

        list.remove(button.getDeleteId());

        updateList();
    }
}

