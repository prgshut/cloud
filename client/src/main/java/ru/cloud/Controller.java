package ru.cloud;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import ru.cloud.command.FileSendToServer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        if (clientPC.getSelectionFileName()!=null){
            System.out.println("Отправляем");
            System.out.println(serverPC.getSelectionPath());
            Path pathSrc = Paths.get(clientPC.getSelectionPath()).resolve(clientPC.getSelectionFileName());
            Path pathDst = Paths.get(serverPC.getSelectionPath());
                FileSendToServer.filSendServer(pathSrc,pathDst,()->{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Файл передан", ButtonType.OK);
                    alert.showAndWait();

                    serverPC.updateList(pathDst);
                });

        }
    }
}
