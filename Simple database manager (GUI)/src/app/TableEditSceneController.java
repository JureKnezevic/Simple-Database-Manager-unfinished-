package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class TableEditSceneController {

    //@FXML
    //private Button SQLquerryBtn;

    @FXML
    private VBox VBoxTableRows;

    @FXML
    private Label label1;

    //@FXML
    //private ToggleButton toggleButton;

    //@FXML
    //private MenuItem logoutMenu;

    @FXML
    private Button recreateButton;

    @FXML
    private TextArea sqlTextArea;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ResultSet rs;
    private ResultSetMetaData rsmd;

    private ArrayList<String> columnNamesList;
    private ArrayList<String> dataTypeList;
    private ArrayList<TextField> rowsValuesList;
    private ArrayList<RowToInsert> addedRowsValuesList;

    private int mode = 0;
    private int columnCount;
    private String tableName;
    private Integer[] toSkip;

    private VBox addedRows;

    private Button insert;

    public void display(String table) {

        tableName = table;
        label1.setText("Table in use: " + tableName);

        //preparing lists for easier managing of textfields and values
        columnNamesList = new ArrayList<>();
        dataTypeList = new ArrayList<>();
        rowsValuesList = new ArrayList<>();
        addedRowsValuesList = new ArrayList<>();

        updateTable();
    }

    public void updateResultSet() {

        try {
            //getting resultset and resultsetmetadata
            rs = Main.newResultSet("SELECT * FROM " + tableName);
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateTable() {

        if (VBoxTableRows != null) {

            if ((long) VBoxTableRows.getChildren().size() > 0) {
                VBoxTableRows.getChildren().clear();
                rowsValuesList.clear();
                dataTypeList.clear();
                columnNamesList.clear();
            }

        }

        getDataTypeList();

        try {

            updateResultSet();
            //creating the column name row and adding it to the table vbox
            HBox columnNames = new HBox();

            if (columnNamesList.size() == 0) {
                columnNames = createColumnNameRow();
            }

            VBoxTableRows.getChildren().add(columnNames);

            int count = 0;
            while (rs.next()) {

                createInitialRows();

                HBox row = new HBox();

                for (int i = 0; i < columnCount; i++) {
                    row.getChildren().add(rowsValuesList.get(i + count));

                }
                count = count + columnCount;
                VBoxTableRows.getChildren().add(row);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        HBox buttonRow = new HBox();
        addedRows = new VBox();

        Button addARow = new Button("Add a new row");
        insert = new Button("Insert");
        insert.setDisable(true);

        addARow.setOnAction(actionEvent -> AddARow());
        insert.setOnAction(actionEvent -> insertNewData());

        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.getChildren().addAll(addARow, insert);

        VBoxTableRows.getChildren().addAll(addedRows, buttonRow);

    }

    public HBox createColumnNameRow() {

        HBox columns = new HBox();

        try {

            for (int i = 1; i <= columnCount; i++) {

                TextField column = new TextField(rsmd.getColumnName(i) + " " + dataTypeList.get(i - 1));
                column.setEditable(false);
                column.setStyle(getColumnNamesStyle());
                columnNamesList.add(rsmd.getColumnName(i));

                columns.getChildren().add(column);

            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return columns;

    }


    public void createInitialRows() {

        try {

            rsmd = rs.getMetaData();

            for (int i = 1; i <= columnCount; i++) {
                TextField value = new TextField(rs.getString(i));
                value.setEditable(false);
                value.setStyle(getValuesStyle());
                rowsValuesList.add(value);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void AddARow() {

        insert.setDisable(false);

        RowToInsert row = new RowToInsert(columnCount);
        recreateButton.setDisable(true);

        addedRowsValuesList.add(row);
        updateAddedRows();

    }

    public void updateAddedRows() {

        if (addedRows != null) {
            addedRows.getChildren().clear();
        }

        if (addedRowsValuesList.size() > 0) {
            insert.setDisable(false);
        }

        for (int a = 0; a < addedRowsValuesList.size(); a++) {

            DeleteButton deleteButton = new DeleteButton();
            deleteButton.setText("\uD83D\uDDD9");
            deleteButton.setDeleteId(a);
            deleteButton.setOnAction(ActionEvent -> {
                addedRowsValuesList.remove(deleteButton.getDeleteId());
                updateAddedRows();
            });

            if (addedRows == null) {
                addedRows = new VBox();
            }

            HBox rowWithButton = new HBox();
            rowWithButton.getChildren().addAll(addedRowsValuesList.get(a).getRow(), deleteButton);

            addedRows.getChildren().add(rowWithButton);
        }

    }

    public void getDataTypeList() {

        updateResultSet();

        for (int i = 0; i < columnCount; i++) {

            try {
                dataTypeList.add(rsmd.getColumnTypeName(i + 1) + "(" + rsmd.getColumnDisplaySize(i + 1) + ")");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void recreateTable() {

        for (int i = 0; i < VBoxTableRows.getChildren().size(); i++) {

            Node node1 = VBoxTableRows.getChildren().get(i);

            if (!(node1 instanceof VBox)) {

                HBox row = (HBox) node1;

                try {

                    rsmd = Main.getResultSet().getMetaData();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                for (int a = 0; a < row.getChildren().size(); a++) {

                    if (!(row.getChildren().get(a) instanceof Button)) {

                        if (i < 1) {

                            String extra = "";

                            try {
                                if (rsmd.isNullable(a+1)==0) {
                                    extra = extra + " NOT NULL ";
                                }
                                if (rsmd.isAutoIncrement(a+1)) {
                                    extra = extra + " AUTO_INCREMENT ";
                                }



                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            System.out.println(((TextField) row.getChildren().get(a)).getText() + extra);

                        } else {

                            System.out.println(((TextField) row.getChildren().get(a)).getText());
                        }
                    }
                }
            }
        }
    }

    public void insertNewData() {

        insert.setDisable(true);

        String toInsertInto = columnsToInsertInto(columnNamesList);

        StringBuilder values = new StringBuilder();

        for (int i = 0; i < addedRowsValuesList.size(); i++) {
            values.append(addedRowsValuesList.get(i).toString());
            if (i + 1 < addedRowsValuesList.size()) {
                values.append(",");
            }
        }

        toSkip = new Integer[columnNamesList.size()];

        try {
            Main.query("INSERT INTO " + tableName + "( " + toInsertInto + " )" + " VALUES " + values);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        display(tableName);

    }

    public void SQLquery() {

        try {

            Main.query(sqlTextArea.getText());
            updateTable();
            updateAddedRows();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String columnsToInsertInto(ArrayList<String> columns) {

        String toInsertInto = "";
        toSkip = new Integer[columnNamesList.size()];

        try {
            rsmd = Main.getResultSet().getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int b = 0;

        for (int i = 0; i < columns.size(); i++) {
            try {

                if (rsmd.isAutoIncrement(i + 1)) {

                    toSkip[i] = 1;
                    System.out.println("SKIPPABLE");

                } else {

                    toSkip[i] = 0;
                    System.out.println("NOT SKIPPABLE");

                    if (b == 0) {
                        toInsertInto = columns.get(i);

                        System.out.println(toInsertInto);

                        b++;

                    } else {
                        toInsertInto = toInsertInto + "," + columns.get(i);

                        System.out.println(toInsertInto);

                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return toInsertInto;

    }

    public void getTableAsText() { // nedovrseno

        //printa ovo:
        //CREATE TABLE testtable1 ( id INT(10), name VARCHAR(20) )        -> fali auto_increment, not null, primary key i takve stvari
        //INSERT INTO testtable1( name ) VALUES 1,ed,2,edd,3,eddy         -> ne skipa auto_increment stupce

        StringBuilder columnsToMake = new StringBuilder();

        for (int i = 0; i < columnNamesList.size(); i++) {
            if (i + 1 != columnNamesList.size()) {
                columnsToMake.append(" ").append(columnNamesList.get(i)).append(" ").append(dataTypeList.get(i)).append(",");
            } else {
                columnsToMake.append(" ").append(columnNamesList.get(i)).append(" ").append(dataTypeList.get(i));
            }

        }
        String tableToCreate = "CREATE TABLE " + tableName + " (" + columnsToMake + " )";

        String toInsertTo = columnsToInsertInto(columnNamesList);
        StringBuilder valuesToInsert = new StringBuilder();

        int count = 0;
        for (int i = 0; i < rowsValuesList.size(); i++) {

            if (count==0) {
                valuesToInsert.append("(").append(rowsValuesList.get(i).getText());
                count++;

            } else if (count+1==columnCount) {

                valuesToInsert.append(",").append(rowsValuesList.get(i).getText()).append(")");

                if (i+1 != rowsValuesList.size()) {
                    valuesToInsert.append(",");
                }

                count = 0;

            } else {

                valuesToInsert.append(",").append(rowsValuesList.get(i).getText());
                count++;
            }
        }

        String tableInsert = "INSERT INTO " + tableName + "( " + toInsertTo + " )" + " VALUES " + valuesToInsert; //

        System.out.println(tableToCreate);
        System.out.println(tableInsert);
    }

    public void editMode() {

        System.out.println("mode: " + mode);
        if (mode == 0) {

            recreateButton.setDisable(true);
            mode = 1;

            for (Node node : VBoxTableRows.getChildren()) {
                if (!(node instanceof VBox)) {
                    HBox row = (HBox) node;

                    for (int a = 0; a < row.getChildren().size(); a++) {

                        if (row.getChildren().get(a) instanceof TextField) {

                            (row.getChildren().get(a)).setStyle(null);
                            ((TextField) row.getChildren().get(a)).setEditable(true);
                        }

                    }
                    recreateButton.setDisable(true);

                }
            }
        } else {

            recreateButton.setDisable(false);
            mode = 0;
            int count = 0;
            for (Node node : VBoxTableRows.getChildren()) {
                if (!(node instanceof VBox)) {
                    HBox row = (HBox) node;

                    for (int a = 0; a < row.getChildren().size(); a++) {

                        if (row.getChildren().get(a) instanceof TextField) {

                            if (count < columnCount) {
                                (row.getChildren().get(a)).setStyle(getColumnNamesStyle());
                                count++;
                            } else {
                                (row.getChildren().get(a)).setStyle(getValuesStyle());
                            }

                            ((TextField) row.getChildren().get(a)).setEditable(false);
                        }

                    }
                    recreateButton.setDisable(false);
                }
            }
        }
    }

    public void logout(ActionEvent event) {
        Main.logout(event);
    }

    public void backToTables(ActionEvent event) {

        try {
            //loading fxml files for login scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableSelectScene.fxml"));
            root = loader.load();

            String db = null;
            try {
                ResultSet rs = Main.newResultSet("SELECT DATABASE()");
                while (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    String column = rsmd.getColumnName(1);
                    db = rs.getString(column);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            TableSelectSceneController tableSelectSceneController = loader.getController();
            tableSelectSceneController.display(db);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    public String getColumnNamesStyle() {

        return "-fx-background-color: rgba(100, 100, 100, 0.25);";

    }

    public String getValuesStyle() {
        return "-fx-background-color: rgba(100, 100, 100, 0.10);";
    }
}
