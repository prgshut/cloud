import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ClientController {
    private final Stage primaryStage;
    private final Stage authStage;
    private Parent root;
    private String addr;
    private int port;

    public ClientController(String default_addr, int default_port, Stage primaryStage) {
        this.addr = default_addr;
        this.port = default_port;
        this.primaryStage = primaryStage;
        this.authStage = new Stage();
    }

    public void ranApp() throws Exception {
        CountDownLatch networkStarter = new CountDownLatch(1);
        new Thread(() -> Network.getInstance().start(networkStarter, addr, port)).start();
        networkStarter.await();
        System.out.println("Сетевое подключение открыто");

        FXMLLoader loaderAuth = new FXMLLoader();
        root = loaderAuth.load(getClass().getResourceAsStream("auth.fxml"));
        AuthControl authDialog = loaderAuth.getController();
        authDialog.setController(this);
        Scene scene = new Scene(root, 200, 100);
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(scene);
        primaryStage.setIconified(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
    }
}
