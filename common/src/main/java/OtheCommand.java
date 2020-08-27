import io.netty.buffer.ByteBuf;

public class OtheCommand {

    public static String getPath(ByteBuf buf) {
        System.out.println("getPath");
        int len = buf.readInt();
        byte[] dir = new byte[len];
        buf.readBytes(dir);
        System.out.println(new String(dir));
        return new String(dir);
    }
}
