package command;

import javafx.application.Platform;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


public class AuthCommand {
    public static void sendAuth(String login, String pass, Callback callback,Callback callbackErr){
        System.out.println("1");
       new Thread(()-> {
            try {
                System.out.println("2");
                System.out.println(Network.getInstance().getCurrentChannel().isConnected());
                ByteBuffer buf = ByteBuffer.allocate(1+4+login.length()+4+pass.length());
                buf.put((byte) 5);
                buf.putInt(login.length());
                buf.put(login.getBytes());
                buf.putInt(pass.length());
                buf.put(pass.getBytes());
                buf.flip();
                Network.getInstance().getCurrentChannel().write(buf);
                System.out.println("читаем ответ");
                byte rez= Network.getInstance().getIn().readByte();
                System.out.println("Входящин данные "+rez);
                if(rez==(byte)1){
                    Platform.runLater(()-> {
                        try {
                            System.out.println("3");
                            callback.call();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }else {

                            System.out.println("4");
                            Platform.runLater(()-> {
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
