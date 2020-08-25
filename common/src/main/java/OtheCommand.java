import io.netty.buffer.ByteBuf;

public class OtheCommand {

    public static String getPath(ByteBuf buf) {
        int len = buf.readInt();
        byte[] dir = new byte[len];
        buf.writeBytes(dir);
        return new String(dir);
    }
}
