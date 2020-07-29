package io;

import java.io.*;
import java.net.Socket;


public class ClientWork {
    private final Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private final int nameClient;
    private static final int BYTE_TRANSFER = 256;
    String path ;


    public ClientWork(Socket clientSocket, int nameClient) {
        this.clientSocket = clientSocket;
        this.nameClient=nameClient;
        path= "./server/"+nameClient+"/";
        
    }

    public void job() {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            in= new DataInputStream(clientSocket.getInputStream());
            out=new DataOutputStream(clientSocket.getOutputStream());
            readComand();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            clientClose();
        }

    }

    private void readComand() {
        try {
            String read=in.readUTF();
            String[] commandRead=read.split(" ", 2);
            if(commandRead[0].equals("\\in")){
                writeFileServer(commandRead[1]); // получаем файл от клиента
            }else if (commandRead[0].equals("\\out")){
                readFileServer(commandRead[1]); //отправляем файл клиенту

            }else if(commandRead[0].equals("\\info")){
                infoFileServer();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Клиент отключился "+ nameClient);
        }
    }

    private void infoFileServer() {
         File dir = new File(path);
        StringBuffer info = null;
        if(dir.isDirectory())
        {
            for(File item : dir.listFiles()){
                if(item.isDirectory()){
                   info.append(item.getName() + " folder");
                }
                else{
                    info.append(item.getName() + " file");
                }
            }
        }
        try {
            out.writeUTF(info.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //отправляем файл клиенту
    private void readFileServer(String nameFile)  {

        File file = new File(path+nameFile);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long size = file.length();
        int count = (int) (size / BYTE_TRANSFER) / 10, readBuckets = 0;
        // /==========/
        try {
            byte [] buffer = new byte[BYTE_TRANSFER];
            out.writeUTF(file.getName());
            System.out.print("/");
            while (is.available() > 0) {
                int readBytes = is.read(buffer);
                readBuckets++;
                if (readBuckets % count == 0) {
                    System.out.print("=");
                }
                out.write(buffer, 0, readBytes);
            }
            System.out.println("/");
        }catch (Exception e){
            e.getMessage();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    // получаем файл от клиента
    private void writeFileServer(String nameFile) {


        System.out.println("fileName: " + nameFile);
        File file = new File(path + nameFile);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("createNewFile " + nameFile);
        try (FileOutputStream os = new FileOutputStream(file)) {
            byte[] buffer = new byte[BYTE_TRANSFER];
            while (true) {
                int r = in.read(buffer);
                if (r == -1) break;
                os.write(buffer, 0, r);
            }
            System.out.println("write file  " + nameFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void clientClose() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
