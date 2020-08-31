import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ReadCommand extends ChannelInboundHandlerAdapter {
    private static final byte COMMAND_GET_FILE_LIST = 10;
    private static final byte COMMAND_SEND_FILE = 20;
    private static final byte COMMAND_RECIPIENT_FILE = 30;
    private static final byte COMMAND_AUTH = 5;
    private static final byte COMMAND_GET_HOME_DIR = 40;
    private static  String nameUser;
    private ConnectBase conect;
    private Path pathClient;
    private Path defalPath =Paths.get("serverClient");

    public ReadCommand(ConnectBase connect) {

        this.conect = connect;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("31");
        int temp = 0;
        ByteBuf buf = (ByteBuf) msg;

        Path path;
        byte reader = buf.readByte();

        if (reader == COMMAND_AUTH) {
            System.out.println("аутинфикация");
            nameUser = CommandAuth.auth(ctx, buf, conect);

            if (nameUser == null) {
                System.out.println("Нет такого пользователя");

            }

        }
        if (nameUser != null) {
            path = Paths.get("serverClient", nameUser);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
                pathClient = path;
            } else {
                pathClient = path;
            }
            if (reader == COMMAND_SEND_FILE) {
                ProtoFileRecipient.recipientFile(path, (ByteBuf) msg);
            } else if (reader == COMMAND_RECIPIENT_FILE) {
                ProtoFileSender.sendFile(path, ctx.channel(), (future) -> {
                });
            } else if (reader == COMMAND_GET_FILE_LIST) {
                           path= defalPath.resolve(OtheCommand.getPath(buf));
                System.out.println(path.toString());
                CommandGetFileList.getFileList(path, ctx.channel());
            }else if(reader==COMMAND_GET_HOME_DIR){

                ByteBuf bufTemp = ByteBufAllocator.DEFAULT.directBuffer(4);
                bufTemp.writeInt(nameUser.length());
                ctx.writeAndFlush(bufTemp);
                bufTemp=ByteBufAllocator.DEFAULT.directBuffer(nameUser.length());
                bufTemp.writeBytes(nameUser.getBytes());
                ctx.writeAndFlush(bufTemp);
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
