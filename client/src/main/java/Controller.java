import io.netty.buffer.ByteBuf;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.swing.plaf.TableHeaderUI;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {

    public ListView<String> lv;
    public TextField txt;
    public Button send;
    private Socket socket;
    private DataInputStream is;
    private DataOutputStream os;
    private final String clientFilesPath = "./client/clientFiles";
    private static int clientName=0;
    private File dir;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", 8189);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            clientName++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        dir = new File(clientFilesPath+"/"+clientName);
        if (!dir.exists()){
            dir.mkdir();
        }
        for (String file : dir.list()) {
            lv.getItems().add(file);
        }
    }

    // ./dow fileName
    // ./upload fileName
    public void sendCommand(ActionEvent actionEvent) {
        String command = txt.getText();

    }
}
