import io.netty.buffer.ByteBuf;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.plaf.TableHeaderUI;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Controller implements Initializable {


    private ClientController controller;
    private Parent rootRename;
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

    public void ConnectServer(ActionEvent actionEvent) {
        Stage renameStage = new Stage();
        FXMLLoader loaderRename = new FXMLLoader();

        try {
            rootRename = loaderRename.load(getClass().getResourceAsStream("../rename/RenameForm.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RenameControl renameModel = loaderRename.getController();
        renameModel.setController(this.controller);
        Scene scene = new Scene(rootRename, 300, 200);
        renameStage.setTitle("Переименовка");
        renameStage.setScene(scene);
        renameStage.initModality(Modality.WINDOW_MODAL);
        renameStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        renameStage.setIconified(false);
        renameStage.show();
    }
}
