package command;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class FileListServer {
    private static List<FileInfo> fileInfoList = new LinkedList<>();
    private static byte DIR = 11;
    private static byte FILE = 12;
    private static byte END_FILE = 13;

    public static List<FileInfo> getListFileServer(String path) {
        fileInfoList.clear();
            try {
                ByteBuffer buf = ByteBuffer.allocateDirect(1 + 4 + path.length());
                buf.put((byte) 10);
                buf.putInt(path.length());
                buf.put(path.getBytes());
                buf.flip();
                Network.getInstance().getCurrentChannel().write(buf);
                while (true) {
                    byte tmp;
                    FileInfo fileInfo = new FileInfo();
                    tmp = Network.getInstance().getIn().readByte();
                    if (tmp == DIR) {
                        fileInfo.setType(FileInfo.FileType.DIRECTORY);
                    } else if (tmp == FILE) {
                        fileInfo.setType(FileInfo.FileType.FILE);
                    } else if (tmp == END_FILE) {
                        break;
                    }
                    int sizeNameFile = Network.getInstance().getIn().readInt();
                    byte[] nameFile = new byte[sizeNameFile];
                    Network.getInstance().getIn().read(nameFile, 0, sizeNameFile);
                    fileInfo.setNameFile(new String(nameFile));
                    long size = Network.getInstance().getIn().readLong();
                    if (fileInfo.getType()== FileInfo.FileType.DIRECTORY){
                        fileInfo.setSize(-1l);
                    }else {
                        fileInfo.setSize(size);
                    }
                    fileInfoList.add(fileInfo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return fileInfoList;
    }
}
