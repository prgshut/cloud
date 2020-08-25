import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {



    private static String LOGIN_CLIENT;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
updateList(Paths.get(LOGIN_CLIENT));

    }

    private void updateList(Path path) {


    }


    public void btnExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void setController(String login){
        LOGIN_CLIENT=login;
    }
}
