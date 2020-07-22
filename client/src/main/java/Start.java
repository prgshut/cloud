import java.io.IOException;
import java.net.Socket;

public class Start {
    public static void main(String[] args) {
        int port=8189;
        String addres = "localhost";
        try {
            new ConnectServer(new Socket(addres,port)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
