import io.netty.buffer.ByteBuf;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

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
}
