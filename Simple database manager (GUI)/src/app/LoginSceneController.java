package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginSceneController {

    @FXML
    TextField userField;
    @FXML
    TextField passField;
    @FXML
    PasswordField passPassField;
    @FXML
    TextField urlField;
    @FXML
    CheckBox passCheckbox;

    public void passVisibility() {

        if (passCheckbox.isSelected()) {

            passField.setText(passPassField.getText());
            passPassField.setVisible(false);
            passField.setVisible(true);

        } else {

            passPassField.setText(passField.getText());
            passPassField.setVisible(true);
            passField.setVisible(false);
        }
    }

    public void login(ActionEvent event) { //method starts once user enters

        //setting user credentials
        Main.setUsername(userField.getText());

        if (passField.getText().equals("")) {
            passField.setText(passPassField.getText());
        }

        Main.setPassword(passField.getText());
        Main.setUrl(urlField.getText());

        try {
            //initiates connection to the mysql server
            Main.connection("");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("databaseSelectScene.fxml"));
            Parent root = loader.load();

            DatabaseSceneController databaseSceneController = loader.getController();
            databaseSceneController.display();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


