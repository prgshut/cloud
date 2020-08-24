import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.sql.*;

public class CommandAuth {
    private static ResultSet res;

    public static String auth(ChannelHandlerContext channel, ByteBuf msg, ConnectBase con){
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
        System.out.println(login +" "+password);
        final String str =String.format("SELECT UserName FROM name  where UserName='%s' and Password='%s';", login, password);
        res=con.select(str);
        try {
            ByteBuf buf =  ByteBufAllocator.DEFAULT.directBuffer(1);
            while (res.next()){
                System.out.println("Отправляем ответ удачно");
                buf.writeByte((byte)1);
                channel.writeAndFlush(buf);
                return res.getString("UserName");

            }
            System.out.println("отправляем не удачно");
        buf.writeByte((byte)2);
        channel.writeAndFlush(buf);
        } catch (SQLException e) {
        e.printStackTrace();
    }
        return null;
    }

}
