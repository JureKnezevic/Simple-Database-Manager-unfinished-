package app;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RowToInsert {

    HBox row;
    int columnCount;

    public RowToInsert(int columnCount) {

        row = new HBox();
        this.columnCount = columnCount;

        for (int i = 0; i < columnCount; i++) {

            try {
                ResultSet rs = Main.getResultSet();
                ResultSetMetaData rsmd = rs.getMetaData();

                if (rsmd.isAutoIncrement(i+1)) {

                    TextField textField = new TextField("AUTO INCREMENT");
                    textField.setEditable(false);
                    textField.setStyle("-fx-background-color: rgba(0, 100, 0, 0.35);");

                    row.getChildren().add(textField);

                } else if (rsmd.isNullable(i+1)==0) {

                    TextField textField = new TextField("NOT NULL");
                    textField.setAlignment(Pos.CENTER);
                    textField.setEditable(true);
                    textField.setStyle("-fx-background-color: rgba(150, 0, 0, 0.35);");

                    row.getChildren().add(textField);
                } else {

                    TextField textField = new TextField();

                    row.getChildren().add(textField);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public HBox getRow() {
        return row;
    }

    @Override
    public String toString() {
        String toReturn = "(";
        int pos = 0;
        for (int i = 0; i < row.getChildren().size(); i++) {

            if (((TextField) row.getChildren().get(i)).isEditable()) {

                if (pos == 0) {

                    toReturn = toReturn + "'" +((TextField) row.getChildren().get(i)).getText() + "'";
                    pos++;

                } else {

                    toReturn = toReturn + ",'" + ((TextField) row.getChildren().get(i)).getText() + "'";
                    pos++;

                }
            }
        }
        return toReturn + ")";
    }
}
