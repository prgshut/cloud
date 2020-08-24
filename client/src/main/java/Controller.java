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
import java.util.ResourceBundle;

public class Controller implements Initializable {



    private Parent root;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        try {
//            socket = new Socket("localhost", 8189);
//            is = new DataInputStream(socket.getInputStream());
//            os = new DataOutputStream(socket.getOutputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    public void btnExit(ActionEvent actionEvent) {
        Platform.exit();
    }

//    public void ConnectServer(ActionEvent actionEvent) {
//        Stage renameStage = new Stage();
//        FXMLLoader loaderRename = new FXMLLoader();
//
//        try {
//            root = loaderRename.load(getClass().getResourceAsStream("../rename/RenameForm.fxml"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        RenameControl renameModel = loaderRename.getController();
//        renameModel.setController(this.controller);
//        Scene scene = new Scene(root, 300, 200);
//        renameStage.setTitle("Переименовка");
//        renameStage.setScene(scene);
//        renameStage.initModality(Modality.WINDOW_MODAL);
//        renameStage.initOwner(((Node)event.getSource()).getScene().getWindow());
//        renameStage.setIconified(false);
//        renameStage.show();
//    }
}
