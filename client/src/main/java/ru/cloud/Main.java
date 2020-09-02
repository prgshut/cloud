package ru.cloud;

import ru.cloud.command.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Main extends Application {
    private static final String DEFAULT_ADDR ="localhost";
    private static final int DEFAULT_PORT = 8189;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loaderAuth = new FXMLLoader();
         Parent rootAuth = loaderAuth.load(getClass().getResourceAsStream("/auth.fxml"));
        AuthControl authDialog = loaderAuth.getController();
        authDialog.setController(primaryStage);
        Scene scene = new Scene(rootAuth, 500, 230);
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(scene);
        primaryStage.setIconified(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
    }


    public static void main(String[] args) throws InterruptedException {

        CountDownLatch networkStarter = new CountDownLatch(1);
        new Thread(() -> {
            try {
                Network.getInstance().start(networkStarter,DEFAULT_ADDR,DEFAULT_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        networkStarter.await();
        launch(args);
    }
}
