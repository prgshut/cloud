import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class CommandGetFileList {
    private final static byte DIR = 11;
    private final static byte FILE =12;
    private final static byte END_LIST =13;

    public static void getFileList(Path path, Channel channel){
        ByteBuf bufEnd=null;
        System.out.println("Зашли в отправку списка");
        try (Stream<Path> streamPath= Files.walk(path)){
            streamPath.forEach(file->{
                System.out.println("Out list file");
                ByteBuf buf = null;
                if (Files.isDirectory(file)){
                    buf=ByteBufAllocator.DEFAULT.directBuffer(1);
                    buf.writeByte(DIR);
                }else {
                    buf=ByteBufAllocator.DEFAULT.directBuffer(1);
                    buf.writeByte(FILE);
                }

                byte[] filenameBytes = file.getFileName().toString().getBytes(StandardCharsets.UTF_8);
                buf = ByteBufAllocator.DEFAULT.directBuffer(4);
                buf.writeInt(filenameBytes.length);
                channel.writeAndFlush(buf);

                buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
                buf.writeBytes(filenameBytes);
                channel.writeAndFlush(buf);

                buf = ByteBufAllocator.DEFAULT.directBuffer(8);
                try {
                    buf.writeLong(Files.size(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                channel.writeAndFlush(buf);
            });
            bufEnd=ByteBufAllocator.DEFAULT.directBuffer(1);
            bufEnd.writeByte(END_LIST);
            channel.writeAndFlush(bufEnd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
