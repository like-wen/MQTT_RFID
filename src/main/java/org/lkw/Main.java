package org.lkw;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/RFIDGUI.fxml")));
            primaryStage.setTitle("自助收银系统");
            primaryStage.getIcons().add(new Image("/lkw.png"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
