import command.AuthCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public class AuthControl {
    @FXML
    TextField loginField;
    @FXML
    PasswordField passField;

    private Stage primaryStage;

    public void sendLoginPass(ActionEvent actionEvent) throws IOException {
        String login=loginField.getText();
        String pass=passField.getText();
        System.out.println("Отправка");
        AuthCommand.sendAuth(login,pass,()->{
            FXMLLoader loaderAuth = new FXMLLoader();
            Parent  rootChat = loaderAuth.load(getClass().getResourceAsStream("sample.fxml"));
            AuthControl authDialog = loaderAuth.getController();
//            authDialog.setController(this);
            Scene scene = new Scene(rootChat, 500, 230);
            primaryStage.setTitle("Авторизация");
            primaryStage.setScene(scene);
            primaryStage.setIconified(false);
            primaryStage.show();
            primaryStage.setOnCloseRequest(e -> {
                System.exit(0);
            });

        },()->{
            System.out.println("Нет пользователя");
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Info");
            alert.getDialogPane().setContentText("Неверное имя");
        });




    }

    public void exitLoginPass(ActionEvent actionEvent) {
        System.exit(0);
    }


    public void setController(Stage primaryStage) {
    this.primaryStage=primaryStage;

    }
}
