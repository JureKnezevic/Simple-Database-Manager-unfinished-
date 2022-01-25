package app;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Main extends Application {

    //things needed to make a connection and/or SQL stuff
    static Connection con;
    static Statement st;
    static String url; // jdbc:mysql://localhost:3306/
    static ResultSet rs;

    //user data
    static String user = ""; //user = root
    static String pass = ""; //pass = 4580

    @Override
    public void start(Stage stage) {

        try {
            //starting up the initial login screen
            Parent root = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
            Scene scene = new Scene(root);

            stage.setTitle("Simple Database Manager (unfinished) - Jure Knežević");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setUsername(String string) {
        user = string;
    }

    public static void setPassword(String string) {
        pass = string;
    }

    public static String getUsername() {
        return user;
    }

    public static void setUrl(String string) {
        url = string;
        if (url.equals("")) {
            url = "jdbc:mysql://localhost:3306/";
        }
    }

    public static void logout(ActionEvent event) {
        //loading fxml files for login scene
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("loginScene.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void connection(String db) throws SQLException {

        con = DriverManager.getConnection(url + db, user, pass);
        st = con.createStatement();
    }

    public static ArrayList<String> getDatabases() throws SQLException {

        connection("");
        ArrayList<String> dbList = new ArrayList<>();

        rs = st.executeQuery("SHOW DATABASES");

        while (rs.next()) {
            dbList.add(rs.getObject(1).toString());
        }

        return dbList;
    }

    public static ArrayList<String> getTables() throws SQLException {

        ArrayList<String> tbList = new ArrayList<>();
        rs = st.executeQuery("SHOW TABLES");

        while (rs.next()) {
            tbList.add(rs.getObject(1).toString());
        }

        return tbList;
    }

    public static void query(String sql) throws SQLException {

        System.out.println(sql);
        st.executeUpdate(sql);
    }

    public static ResultSet newResultSet(String sql) throws SQLException {

        System.out.println(sql);
        rs = st.executeQuery(sql);

        return rs;
    }

    public static ResultSet getResultSet() {
        return rs;
    }
}

