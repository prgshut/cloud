import io.netty.buffer.ByteBuf;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;

public class ProtoFileRecipient {

    public static void recipientFile(Path path,ByteBuf msg ) throws Exception {

        long receivedFileLength = 0L;
        int nextLength =0;
        long fileLength=0;

        ByteBuf buf=msg;
        BufferedOutputStream out = null;

        if (buf.readableBytes() >= 4) {
            System.out.println("STATE: Get filename length");
            nextLength = buf.readInt();
        }

        if (buf.readableBytes() >= nextLength) {
            byte[] fileName = new byte[nextLength];
            buf.readBytes(fileName);
            System.out.println("STATE: Filename received - " + new String(fileName, "UTF-8"));
            out = new BufferedOutputStream(new FileOutputStream(path + new String(fileName)));
        }

        if (buf.readableBytes() >= 8) {
            fileLength = buf.readLong();
            System.out.println("STATE: File length received - " + fileLength);
        }

        while (buf.readableBytes() > 0) {
            out.write(buf.readByte());
            receivedFileLength++;
            if (fileLength == receivedFileLength) {
                System.out.println("File received");
                out.close();
                break;
            }
        }
        if(buf.readableBytes()==0)

        {
            buf.release();
        }

    }

}

