import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectServer {
    private static final int BYTE_TRANSFER = 256;
    DataOutputStream out;
    DataInputStream in ;


    private String path = "./client/";
    private final Socket socket;

    public ConnectServer(Socket socket) throws IOException {
               this.socket= socket;
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        try  {
            System.out.println("Подключились к серверу");
                      while (true) {
                System.out.println("Введите команду:");
                String read = scanner.nextLine();
                String[] commandRead = read.split(" ", 2);
                if (commandRead[0].equals("\\in")) {
                    writeFileServer(read);  //отправить файл на сервер
                } else if (commandRead[0].equals( "\\out")) {
                    readFileServer(read);  //получить файл с сервера

                } else if (commandRead[0].equals("\\info")) {
                    infoFileServer(read);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void infoFileServer(String read) {
        try  {
            out.writeUTF(read);
            String infoFolder = in.readUTF();
            System.out.println(infoFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // отправляем файл на сервер
    private void writeFileServer(String read) throws FileNotFoundException {
        String[] commandRead = read.split(" ", 2);
        File file = new File(path + commandRead[1]);
        InputStream is = new FileInputStream(file);
        long size = file.length();
//        int count = (int) (size / BYTE_TRANSFER) / 10, readBuckets = 0;
        // /==========/
        try {
            byte[] buffer = new byte[BYTE_TRANSFER];
            out.writeUTF(read);
            System.out.print("/");
            while (is.available() > 0) {
                int readBytes = is.read(buffer);
//                readBuckets++;
//                if (readBuckets % count == 0) {
//                    System.out.print("=");
//                }
                out.write(buffer, 0, readBytes);
            }
            System.out.println("/");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // получаем файл с сервера
    private void readFileServer(String read) {
        String[] commandRead = read.split(" ", 2);
        File file = new File(path + commandRead[1]);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            out.writeUTF(read);

        byte[] buffer = new byte[BYTE_TRANSFER];
                while (true) {
                    int lenByteIn = in.read(buffer);
                    if (lenByteIn == -1) break;
                    out.write(buffer, 0, lenByteIn);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
