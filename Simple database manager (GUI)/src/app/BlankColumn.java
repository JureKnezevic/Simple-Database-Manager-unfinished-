package app;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class BlankColumn {

    private TextField columnName;
    private TextField extras;
    private HBox hBox;
    private ComboBox dataTypeMenu;

    //create dropdown menu for datatypes selector
    //create toggles for extras to make things toggable

    public BlankColumn() {
        //descriptive labels for user input
        Label label1 = new Label();
        label1.setText("Column name:");

        Label label2 = new Label();
        label2.setText("Data type:");

        Label label3 = new Label();
        label3.setText("Extras:");

        columnName = new TextField();
        extras = new TextField();
        dataTypeMenu = new ComboBox();

        dataTypeMenu.setEditable(true);
        dataTypeMenu.getItems().addAll(
                "--NUMERIC--",
                "INT",
                "DECIMAL",
                "FLOAT",
                "--STRING--",
                "CHAR",
                "VARCHAR()",
                "--DATETIME--",
                "DATE",
                "TIME",
                "DATETIME",
                "TIMESTAMP",
                "YEAR");

        hBox = new HBox();

        //hBox.setMaxWidth(130);
        hBox.getChildren().addAll(label1, columnName, label2, dataTypeMenu, label3, extras);
        hBox.setSpacing(5);
    }

    public String getColumnName() {
        return columnName.getText();
    }

    public String getDataType() {
        return dataTypeMenu.getValue().toString();
    }

    public String getExtras() {
        return extras.getText();
    }

    public HBox getColumn() {

        return hBox;
    }

    public String getTableColumnString() {
        return getColumnName() + " " + getDataType() + " " + getExtras();
    }
}
