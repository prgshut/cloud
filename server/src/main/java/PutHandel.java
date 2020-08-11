import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class PutHandel extends ChannelInboundHandlerAdapter {

    long size;
    String nameFile;
    String dir = "serverClient/";
    Path path;
    FileOutputStream fout;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] data = new byte[((byte[]) msg).length];
                System.arraycopy (((byte[]) msg),0,data,0,((byte[]) msg).length);
        String[] dat = new String(data).split(" ");

        if (dat.length==3) {
            commDat(ctx, data, dat);
        }else{
            fout = new FileOutputStream(dir + nameFile,true);
            if (data.length==4 && data[0]==47){
                System.out.println("Конец передачи");
//                ctx.writeAndFlush("file ok".getBytes());
            }else {
//                System.out.println( "write to file: "+ Arrays.toString(data));
//                System.out.println(new String(data));
                fout.write(data);

            }
//            ctx.writeAndFlush("block received".getBytes());
        }
    }

    private void commDat(ChannelHandlerContext ctx, byte[] data, String[] dat) throws IOException {
        if (dat[0].equals("/put")) {
            System.out.println(new String(data));
            size = Long.parseLong(dat[1]);
            nameFile = dat[2];
            path = Paths.get(dir, nameFile);

            if (!Files.exists(path)) {
                File file = new File(dir + nameFile);
                file.createNewFile();
            }
            ctx.writeAndFlush("wait_file".getBytes());
        } else if (dat[0].equals("/get")) {


        } else {
            ctx.writeAndFlush("ERR command\n");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
