import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.scene.control.skin.CellSkinBase;

import java.nio.file.Path;
import java.nio.file.Paths;


public class ReadCommand extends ChannelInboundHandlerAdapter {
    private static final byte COMMAND_GET_FILE_LIST = 10;
    private static final byte COMMAND_SEND_FILE = 20;
    private static final byte COMMAND_RECIPIENT_FILE = 30;
    private static final byte COMMAND_AUTH = 5;

    private ConnectBase conect;

    public ReadCommand(ConnectBase connect) {

        this.conect = connect;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("3");
        ByteBuf buf = (ByteBuf) msg;
        String nameUser=null;
        Path path;
//        while (true) {
            byte reader = buf.readByte();
            if (reader==COMMAND_AUTH){
                System.out.println("аутинфикация");
              nameUser=CommandAuth.auth(ctx,buf,conect);
              if (nameUser==null){
                  System.out.println("Нет такого пользователя");

              }

            }
            if (nameUser!=null) {
                path = Paths.get("serverClient", nameUser);
                if (reader == COMMAND_SEND_FILE) {
                    ProtoFileRecipient.recipientFile(path, (ByteBuf) msg);
                } else if (reader == COMMAND_RECIPIENT_FILE) {
                    ProtoFileSender.sendFile(path, ctx.channel(), (future) -> {
                    });
                } else if (reader == COMMAND_GET_FILE_LIST) {
                    CommandGetFileList.getFileList(path, ctx.channel());
                }
            }
//        }
//        System.out.println("read in byte: "+ Arrays.toString(data));
//        System.out.println("Обработали входящий запрос");
//        ctx.fireChannelRead(data);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
