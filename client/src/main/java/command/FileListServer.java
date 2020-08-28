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
        new Thread(() -> {
            try {
                ByteBuffer buf = ByteBuffer.allocateDirect(1 + 4 + path.length());
                buf.put((byte) 10);
                buf.putInt(path.length());
                buf.put(path.getBytes());
                buf.flip();
                Network.getInstance().getCurrentChannel().write(buf);
                System.out.println("Запрос на список файлов");
                while (true) {
                    int temp = 0;
                    byte tmp;
                    FileInfo fileInfo = new FileInfo();
                    tmp = Network.getInstance().getIn().readByte();
                    System.out.println(tmp);
                    if (tmp == DIR) {
                        fileInfo.setType(FileInfo.FileType.DIRECTORY);
                    } else if (tmp == FILE) {
                        fileInfo.setType(FileInfo.FileType.DIRECTORY);
                    } else if (tmp == END_FILE) {
                        break;
                    }
                    int sizeNameFile = Network.getInstance().getIn().readInt();
                    System.out.println(sizeNameFile + " Size");
                    byte[] nameFile = new byte[sizeNameFile];
                    Network.getInstance().getIn().read(nameFile, 0, sizeNameFile);
                    System.out.println(new String(nameFile) + " массив  имени файла");
                    fileInfo.setNameFile(new String(nameFile));
                    long size = Network.getInstance().getIn().readLong();
                    fileInfo.setSize(size);
                    System.out.println(size + " длина файла");
                    fileInfoList.add(fileInfo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
        return fileInfoList;
    }
}
