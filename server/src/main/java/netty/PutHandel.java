package netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class PutHandel extends ChannelInboundHandlerAdapter {
    private boolean isPut = false;
    long size;
    String nameFile;
    String dir = "serverClient/";
    Path path;
    FileOutputStream fout;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] data = (byte[]) msg;
        String[] dat = new String(data).split(" ");

        if (dat[0].equals("/fin")) {
            FileChannel fileChannel = FileChannel.open(path);
            if (fileChannel.size() == size) {
                ctx.writeAndFlush("OK".getBytes());
                fileChannel.close();
            } else {
                ctx.writeAndFlush("ERR_FILES".getBytes());
            }
            isPut = false;
            fout.close();
        }

        if (isPut) {
            fout = new FileOutputStream(dir + nameFile);
            fout.write(data);
            ctx.writeAndFlush(data);
            ctx.writeAndFlush("block received".getBytes());
        } else {
            if (dat[0].equals("/put")) {
                System.out.println(new String(data));
                size = Long.parseLong(dat[1]);
                nameFile = dat[2];
//                isPut = true;
                path = Paths.get(dir, nameFile);

                if (!Files.exists(path)) {
                    File file = new File(dir + nameFile);
                    file.createNewFile();
                }
                ctx.writeAndFlush("wait_file".getBytes());

                System.out.println("Отравили ответ ");
//                ctx.flush();
                System.out.println("Отравили ответ 1");
            } else if (dat[0].equals("/get")) {
                ctx.fireChannelRead(data);
            } else {
                ctx.writeAndFlush("ERR command\n");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
