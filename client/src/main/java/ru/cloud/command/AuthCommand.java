package ru.cloud.command;

import javafx.application.Platform;
import ru.cloud.Callback;
import ru.cloud.Command;


import java.io.IOException;
import java.nio.ByteBuffer;


public class AuthCommand {
    public static void sendAuth(String login, String pass, Callback callback, Callback callbackErr) {
        new Thread(() -> {
            try {
                ByteBuffer buf = ByteBuffer.allocate(1 + 4 + login.length() + 4 + pass.length());
                buf.put(Command.COMMAND_AUTH);
                buf.putInt(login.length());
                buf.put(login.getBytes());
                buf.putInt(pass.length());
                buf.put(pass.getBytes());
                buf.flip();
                Network.getInstance().getCurrentChannel().write(buf);
                byte rez = Network.getInstance().getIn().readByte();
                if (rez == (byte) 1) {
                    Platform.runLater(() -> {
                        try {
                            callback.call();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        try {
                            callbackErr.call();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
