package command;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class FileListServer {
    private static List<FileInfo> fileInfoList= new LinkedList<>();
    private static byte DIR = 11;
    private static byte FILE = 12;
    private static byte END_FILE = 13;

    public static List<FileInfo> getListFileServer(String path){
        new Thread(()->{
            try {
            fileInfoList.clear();
//            Network.getInstance().getOut().write((byte) 10);
//            Network.getInstance().getOut().flush();
                ByteBuffer buf=ByteBuffer.allocateDirect(1+4+path.length());
                buf.put((byte) 10);
                buf.putInt(path.length());
                buf.put(path.getBytes());
                buf.flip();
                Network.getInstance().getCurrentChannel().write(buf);
            DataInputStream in =Network.getInstance().getIn();
            while (true) {
                int temp=0;
                byte tmp;
                FileInfo fileInfo= new FileInfo();
                tmp=in.readByte();
                if (tmp == DIR) {
                    fileInfo.setType(FileInfo.FileType.DIRECTORY);
                }else if(tmp==FILE) {
                    fileInfo.setType(FileInfo.FileType.DIRECTORY);
                }else if(tmp==END_FILE){
                    break;
                }
                int sizeNameFile= in.readInt();
                byte[] nameFile = new  byte[sizeNameFile];
                while (temp<sizeNameFile){
                    temp+=in.read(nameFile);
                }
                fileInfo.setNameFile(new String(nameFile));
                long size =  in.readLong();
                fileInfo.setSize(size);
                fileInfoList.add(fileInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }).start();
        return fileInfoList;
    }
}
