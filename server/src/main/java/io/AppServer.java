package io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AppServer {
    private final int port;
    private static int nameClient=0;

    public AppServer(int port) {
        this.port = port;
    }

    public void start(){
        try (ServerSocket server = new ServerSocket(port)){
            while (true) {
                System.out.println("Ожидаем подключения пользователя");
                Socket socket = server.accept();
                nameClient++;
                System.out.println("Клиент подключился" + nameClient);
                new Thread(()->runClient(socket)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void runClient(Socket socket) {
        ClientWork clientWork = new ClientWork(socket, nameClient);
        clientWork.job();
    }
}
