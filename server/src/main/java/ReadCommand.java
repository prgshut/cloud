import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Path;
import java.nio.file.Paths;


public class ReadCommand extends ChannelInboundHandlerAdapter {
    private static final byte COMMAND_SEND_FILE = 20;
    private static final byte COMMAND_RECIPIENT_FILE = 30;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        Path path= Paths.get("serverClient");
       while (buf.readableBytes()>0){
           byte reader=buf.readByte();
           if(reader==COMMAND_SEND_FILE){
               ProtoFileRecipient.recipientFile(path,(ByteBuf) msg);
           }else if(reader==COMMAND_RECIPIENT_FILE){
               ProtoFileSender.sendFile(path, ctx.channel(), (future)->{

               });
           }
       }
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
