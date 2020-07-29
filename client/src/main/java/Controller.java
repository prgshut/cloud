import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

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
        String [] op = command.split(" ");
        byte [] buffer = new byte[1024];
        if (op[0].equals("./dow")) {
            try {
                os.write(command.getBytes());
                String response = String.valueOf(is.read(buffer));
                System.out.println("resp: " + response);
                if (response.equals("OK")) {
                    File file = new File(dir + "/" + op[1]);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    long len = is.readLong();

                    try(FileOutputStream fos = new FileOutputStream(file)) {
                        if (len < 1024) {
                            int count = is.read(buffer);
                            fos.write(buffer, 0, count);
                        } else {
                            for (long i = 0; i < len / 1024; i++) {
                                int count = is.read(buffer);
                                fos.write(buffer, 0, count);
                            }
                        }
                    }
                    lv.getItems().add(op[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (op[0].equals("./upl")) {

            try {
                os.write(command.getBytes());
                String response = String.valueOf(is.read(buffer));
                System.out.println("resp: " + response);
                File file = new File(clientFilesPath + "/" + op[1]);
                if (response.equals("OK") && file.exists()) {
                    long len = file.length();
                    os.write(Long.toString(len).getBytes());
                    os.flush();
                    try(FileInputStream fis = new FileInputStream(file)) {
                        if (len < 1024) {
                            int count = fis.read(buffer);
                            os.write(buffer, 0, count);
                        } else {
                            for (long i = 0; i < len / 1024; i++) {
                                int count = fis.read(buffer);
                                os.write(buffer, 0, count);
                            }
                        }
                    }
                    lv.getItems().add(op[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
