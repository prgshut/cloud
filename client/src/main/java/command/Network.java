package command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;

public class Network {
    private static Network ourInstance = new Network();

    public static Network getInstance() {
        return ourInstance;
    }

    private Network() {
    }
    private DataInputStream in;
    private DataOutputStream out;
    private SocketChannel currentChannel;

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public SocketChannel getCurrentChannel() {
        return currentChannel;
    }

    public void start(CountDownLatch countDownLatch, String addr, int port) throws IOException {

            InetSocketAddress serverAddress = new InetSocketAddress(addr, port);
            currentChannel = SocketChannel.open(serverAddress);
            out = new DataOutputStream(currentChannel.socket().getOutputStream());
            in = new DataInputStream(currentChannel.socket().getInputStream());
            countDownLatch.countDown();
        System.out.println("Подключение к серрверу");


    }

    public void stop() throws IOException {
        currentChannel.close();
        System.out.println("Соединение завершено");

    }
}
