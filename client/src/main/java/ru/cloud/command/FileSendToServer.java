package ru.cloud.command;

import javafx.application.Platform;
import ru.cloud.Callback;
import ru.cloud.Command;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSendToServer {
    public static void filSendServer(Path pathSrc, Path pathDst, Callback callback) {
        new Thread(()-> {
            ByteBuffer buf = null;
            // Отправляем место назначения
            buf = ByteBuffer.allocateDirect(1 + 4 + pathDst.toString().length());
            buf.put(Command.COMMAND_SEND_FILE);
            buf.putInt(pathDst.toString().length());
            buf.put(pathDst.toString().getBytes());
            buf.flip();
            try {
                Network.getInstance().getCurrentChannel().write(buf);
                System.out.println("Отправили имя директ " );
                buf.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Отправляем имя и размер файла
            buf = ByteBuffer.allocateDirect(4 + pathSrc.getFileName().toString().length() + 8);
            buf.putInt(pathSrc.getFileName().toString().length());
            buf.put(pathSrc.getFileName().toString().getBytes());
            System.out.println(pathSrc.getFileName().toString());
            try {
                buf.putLong(Files.size(pathSrc));
                System.out.println("Размер файла "+Files.size(pathSrc));
            buf.flip();
                Network.getInstance().getCurrentChannel().write(buf);
                buf.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Отправка файла
            try ( BufferedInputStream redFil = new BufferedInputStream(new FileInputStream(pathSrc.toString()))){
                int i = 0;
                while (true) {

                System.out.println("Отправляем блок " + i);
                byte[] sendByte = new byte[1024];
//                buf= ByteBuffer.allocateDirect(1024);
                int end = redFil.read(sendByte);
                Network.getInstance().getOut().write(sendByte, 0, end);
                i+=end;
//                Network.getInstance().getOut().flush();
                if (end < 1024) {
                    break;
                }
            }

            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(()-> {
                try {
                    callback.call();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }
}
