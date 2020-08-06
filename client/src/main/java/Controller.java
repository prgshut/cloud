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
    Scanner in;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", 8189);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            clientName++;
            in = new Scanner(socket.getInputStream());
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
        ByteBuffer byteBuf = ByteBuffer.allocate(1024) ;

        byte [] buffer = new  byte[1024];
//        System.out.println("start buffer "+ buffer);
        if (op[0].equals("/put")) {
            try {
                StringBuffer com = new StringBuffer();
                com.append("/put").append(" ").append(dir.length()).append(" ").append(op[1]);
                os.write(com.toString().getBytes());
                System.out.println(com.toString());
                is.read(buffer);
                int i = (int) buffer[0];
                String response =new String(buffer).trim();
                System.out.println("resp: " + response+" "+i);
                if (response.equals("wait_file")) {
                    File file = new File(dir + "/" + op[1]);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    System.out.println(file.toString());
                    try(FileInputStream fin = new FileInputStream(file)) {
                        while (fin.available()>0) {
                            int count=fin.read(buffer);
                            os.write(buffer, 0,count);
                            }
                        System.out.println("File OUT");
                    }

                        Thread.sleep(100);

                    os.write("/fin".getBytes());

                    lv.getItems().add(op[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (op[0].equals("/get")) {

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
