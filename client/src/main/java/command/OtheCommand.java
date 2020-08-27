package command;

import java.io.IOException;

public class OtheCommand {
    public static String getHomeDir(){
        try {
            Network.getInstance().getOut().writeByte(40);
            Network.getInstance().getOut().flush();
            System.out.println("Отправили команду на доступ к файлам");
            int lenPath=Network.getInstance().getIn().readInt();
            byte[] homeDir= new byte[lenPath];
            int temp=0;
            while (temp<lenPath){
                temp+=Network.getInstance().getIn().read(homeDir);
            }
            System.out.println(new String(homeDir));
            return new String(homeDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
