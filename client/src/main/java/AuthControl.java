import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthControl {
    @FXML
    TextField loginField;
    @FXML
    PasswordField passField;

    private ClientController controller;


    public void sendLoginPass(ActionEvent actionEvent) {
        String login=loginField.getText();
        String pass=passField.getText();
        ByteBuf buf;
        buf= ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte((byte)5);
        Network.getInstance().getCurrentChannel().writeAndFlush(buf);

        buf=ByteBufAllocator.DEFAULT.directBuffer(4);
        buf.writeInt(login.length());
        Network.getInstance().getCurrentChannel().writeAndFlush(buf);
        buf=ByteBufAllocator.DEFAULT.directBuffer(login.length());
        buf.writeBytes(login.getBytes());
        Network.getInstance().getCurrentChannel().writeAndFlush(buf);

        buf=ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeInt(pass.length());
        Network.getInstance().getCurrentChannel().writeAndFlush(buf);
        buf=ByteBufAllocator.DEFAULT.directBuffer(pass.length());
        buf.writeBytes(pass.getBytes());
        Network.getInstance().getCurrentChannel().writeAndFlush(buf);



    }

    public void exitLoginPass(ActionEvent actionEvent) {
        Platform.exit();
    }
    public void setController(ClientController controller) {
        this.controller = controller;
    }
}
