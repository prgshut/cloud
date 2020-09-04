package ru.cloud.command;

import ru.cloud.Command;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class FileListServer {
    private static List<FileInfo> fileInfoList = new LinkedList<>();


    public static List<FileInfo> getListFileServer(String path) {
        fileInfoList.clear();
            try {
                ByteBuffer buf = ByteBuffer.allocateDirect(1 + 4 + path.length());
                buf.put(Command.COMMAND_GET_FILE_LIST);
                buf.putInt(path.length());
                buf.put(path.getBytes());
                buf.flip();
                Network.getInstance().getCurrentChannel().write(buf);
                while (true) {
                    byte tmp;
                    FileInfo fileInfo = new FileInfo();
                    tmp = Network.getInstance().getIn().readByte();
                    if (tmp == Command.DIR) {
                        fileInfo.setType(FileInfo.FileType.DIRECTORY);
                    } else if (tmp == Command.FILE) {
                        fileInfo.setType(FileInfo.FileType.FILE);
                    } else if (tmp == Command.END_LIST) {
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
