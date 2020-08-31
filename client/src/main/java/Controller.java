import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller {
@FXML
    VBox server,client;
    private PanelServer serverPC;
    private PanelClient clientPC;
    private static String LOGIN_CLIENT;

    public void btnExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void setController(String login){
        this.LOGIN_CLIENT=login;
        this.serverPC= (PanelServer) server.getProperties().get("ctrl");
         this.clientPC = (PanelClient) client.getProperties().get("ctrl");
    }

    public void cutFile(ActionEvent actionEvent) {


        if (serverPC.getSelectionFileName()==null && clientPC.getSelectionFileName()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ни один файл не был выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }
    }
}
