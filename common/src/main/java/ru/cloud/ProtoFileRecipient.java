package ru.cloud;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProtoFileRecipient {

    public static void recipientFile(Path path,ByteBuf msg ) throws Exception {
        System.out.println("Длина буфера " + msg.readableBytes());
        long receivedFileLength = 0L;
        int nextLength =0;
        long fileLength=0;
        Path pathDst = null;

//        ByteBuf buf=msg;
        BufferedOutputStream out = null;

//        if (msg.readableBytes() >= 4) {
            System.out.println("STATE: Get filename length");
            nextLength = msg.readInt();
//        }

//        if (msg.readableBytes() >= nextLength) {
            byte[] fileName = new byte[nextLength];
            msg.readBytes(fileName);
            System.out.println("Имя файла "+ new String(fileName));
            pathDst=path.resolve(new String(fileName));
            if (!Files.exists(pathDst)){
                Files.createFile(pathDst);

//            }
            System.out.println("STATE: Filename received - " + pathDst.toString());
            out = new BufferedOutputStream(new FileOutputStream(pathDst.toString()));
        }

//        if (msg.readableBytes() >= 8) {
            fileLength = msg.readLong();
            System.out.println("STATE: File length received - " + fileLength);
//        }

//        while (msg.readableBytes() > 0) {
            while (true) {

                out.write(msg.readByte());
            receivedFileLength++;

            if (fileLength == receivedFileLength) {
                System.out.println("File received");
                out.close();
                break;
            }
        }
//        callback.call();
//        if(msg.readableBytes()==0)
//
//        {
//            buf.release();
//        }

    }

}

