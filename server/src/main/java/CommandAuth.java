import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.sql.*;

public class CommandAuth {
    private static ResultSet res;

    public static String auth(Channel channel, ByteBuf msg, ConnectBase con){
        int length=0;
        String login = null;
        String password = null;

        if (msg.readableBytes() >= 4) {
            length = msg.readInt();
        }

        if (msg.readableBytes() >= length) {
            byte[] clientName = new byte[length];
            msg.readBytes(clientName);
            login= new String(clientName);
        }
        if (msg.readableBytes() >= 4) {
            length = msg.readInt();
        }

        if (msg.readableBytes() >= length) {
            byte[] pas = new byte[length];
            msg.readBytes(pas);
            password= new String(pas);
        }
        final String str =String.format("SELECT user.name FROM test_base.user where user.login='%s' and user.pass='%s';", login, password);
        res=con.select(str);
        try {
            ByteBuf buf =  ByteBufAllocator.DEFAULT.directBuffer(1);
            while (res.next()){
                buf.writeByte((byte)1);
                channel.writeAndFlush(buf);
                return res.getString("UserName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
