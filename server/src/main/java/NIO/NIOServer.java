package NIO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class NIOServer implements Runnable {
    private ServerSocketChannel server;
    private Selector selector;
    private Path serverPath;
    private Path clientPath;
    private int clientNum;
    private SelectionKey key;

    public NIOServer() throws IOException {
        server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(8189));
        server.configureBlocking(false);
        selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        try {
            System.out.println("server started");
            while (server.isOpen()) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        System.out.println("client accepted");
                        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                        channel.write(ByteBuffer.wrap("Hello!".getBytes()));
                    }
                    if (key.isReadable()) {

                        System.out.println("read key");
                        ByteBuffer buffer = ByteBuffer.allocate(80);
                        int count = ((SocketChannel)key.channel()).read(buffer);
                        if (count == -1) {
                            key.channel().close();
                            break;
                        }
                        buffer.flip();
                        StringBuilder s = new StringBuilder();
                        while (buffer.hasRemaining()) {
                            s.append((char)buffer.get());
                        }
                        System.out.println(s.toString());
                        for (SelectionKey key1 : selector.keys()) {
                            if (key1.channel() instanceof SocketChannel && key1.isReadable()) {
                                ((SocketChannel) key1.channel()).write(ByteBuffer.wrap(s.toString().getBytes()));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void createHomeDir() throws IOException {
        clientNum += 1;
        serverPath = Paths.get("CloudServer/data");
        clientPath = Paths.get(serverPath+"/client" +clientNum);
        if (!Files.exists(clientPath)) {
            Files.createDirectory(clientPath);
        }
    }

    private void checkCommand(String s) throws IOException, InterruptedException {
        String[] commandSplit = s.split(" ", 2);
        SocketChannel channel = ((SocketChannel) key.channel());
        Path file = Paths.get(clientPath + "/" +commandSplit[1]);
        if (commandSplit[0].equals("./dow")) {
            long size = Files.size(file);
            if (Files.exists(file)) {
                channel.write(ByteBuffer.wrap("OK".getBytes()));
                ByteBuffer bufferLong = ByteBuffer.allocate(Long.BYTES);
                bufferLong.putLong(size);
                bufferLong.flip();
                channel.write(bufferLong);
                channel.write(ByteBuffer.wrap(Files.readAllBytes(file)));
            } else {
                channel.write(ByteBuffer.wrap("FALSE".getBytes()));
            }
        }
        if (commandSplit[0].equals("./upl")) {
            channel.write(ByteBuffer.wrap("OK".getBytes()));
            ByteBuffer bufferL = ByteBuffer.allocate(Long.BYTES);
            Thread.sleep(100);
            channel.read(bufferL);
            bufferL.flip();
            StringBuilder sb = new StringBuilder();
            while (bufferL.hasRemaining()) {
                sb.append((char)bufferL.get());
            }
            channel.write(ByteBuffer.wrap("OK".getBytes()));
            Thread.sleep(100);
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            if (buffer.position() < Long.decode(sb.toString()) ) {
                channel.read(buffer);
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                FileChannel fc = new FileOutputStream(file.toFile(), false).getChannel();
                fc.write(buffer);
            }
        }
    }
    public static void main(String[] args) throws IOException {
        new Thread(new NIOServer()).start();
    }
}
