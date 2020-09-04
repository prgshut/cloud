package ru.cloud;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommandGetFileList {
    public static void getFileList(Path path, Channel channel){
        ByteBuf bufEnd=null;
        System.out.println("Зашли в отправку списка");
//        try (Stream<Path> streamPath= Files.walk(path)){
            System.out.println("Out list file");
        try {
            Files.list(path).map(Path::toFile)
                    .forEach(file->{
                ByteBuf buf = null;
                if (file.isDirectory()){
                    buf=ByteBufAllocator.DEFAULT.directBuffer(1);
                    buf.writeByte(Command.DIR);
                    channel.writeAndFlush(buf);

                    System.out.println("DIR");
                }else {
                    buf=ByteBufAllocator.DEFAULT.directBuffer(1);
                    buf.writeByte(Command.FILE);
                    channel.writeAndFlush(buf);

                    System.out.println("FILE");
                }
                buf.clear();

                byte[] filenameBytes = file.getName().toString().getBytes(StandardCharsets.UTF_8);
                buf = ByteBufAllocator.DEFAULT.directBuffer(4);
                buf.writeInt(filenameBytes.length);
                        System.out.println(buf.isWritable());
                channel.writeAndFlush(buf);
                System.out.println(new String(filenameBytes));
                        buf.clear();

                buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
                buf.writeBytes(filenameBytes);
                channel.writeAndFlush(buf);
                        buf.clear();

                buf = ByteBufAllocator.DEFAULT.directBuffer(8);
                        buf.writeLong(file.length());
                        channel.writeAndFlush(buf);
                        buf.clear();

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        bufEnd=ByteBufAllocator.DEFAULT.directBuffer(1);
        bufEnd.writeByte(Command.END_LIST);
        System.out.println("END_LIST");
        channel.writeAndFlush(bufEnd);

    }
}
