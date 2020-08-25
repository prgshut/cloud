package command;

import java.io.IOException;

public class OtheCommand {
    public static String getHomeDir(){
        try {
            Network.getInstance().getOut().write((byte)40);
            Network.getInstance().getOut().flush();
            int lenPath=Network.getInstance().getIn().readByte();
            byte[] homeDir= new byte[lenPath];
            int temp=0;
            while (temp<lenPath){
                temp+=Network.getInstance().getIn().read(homeDir);
            }
            return new String(homeDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
