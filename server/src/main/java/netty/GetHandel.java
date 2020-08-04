package netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetHandel extends ChannelInboundHandlerAdapter {
    int  sizeData=2048;
    byte[] dataFile=new byte[sizeData];

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String nameFile;
        String dir= "serverClient/";
        Path path;


        byte[] data = (byte[]) msg;
        String[] dat = new String(data).split(" ");
        if(dat[0].equals("/get")){
            nameFile=dat[1];
            path= Paths.get(dir,nameFile);
            if (Files.exists(path)){
                ctx.writeAndFlush("start file");
                FileInputStream is = new FileInputStream(path.toString());
                    while (is.available() > 0) {
                        is.read(dataFile);
                        ctx.writeAndFlush(dataFile);
                    }
                    ctx.writeAndFlush("end file");
            }else {
                ctx.writeAndFlush("Файла нет\n");
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
       ctx.close();
    }
}
