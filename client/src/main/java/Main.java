import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private final String DEFAULT_ADDR ="localhost";
    private final int DEFAULT_PORT = 8189;
    @Override
    public void start(Stage primaryStage) throws Exception{
        ClientControler controler = new ClientControler(DEFAULT_ADDR,DEFAULT_PORT,primaryStage);
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root,800,400));
//        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
