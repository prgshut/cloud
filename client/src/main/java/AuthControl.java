import command.AuthCommand;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthControl {
    @FXML
    TextField loginField;
    @FXML
    PasswordField passField;

    private Stage primaryStage;
    private Stage totalStage;

    public void sendLoginPass(ActionEvent actionEvent) throws IOException {
        String login = loginField.getText();
        String pass = passField.getText();
        System.out.println("Отправка");
        AuthCommand.sendAuth(login, pass, () -> {
            openTotal(login);
            primaryStage.close();

        }, () -> {
            System.out.println("Нет пользователя");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Info", ButtonType.OK);
            alert.getDialogPane().setContentText("Неверное имя");
            alert.showAndWait();
        });
    }

    private void openTotal(String login) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent rootTotal = loader.load();
        Controller dialogTotal = loader.getController();
        System.out.println(login);
        dialogTotal.setController(login);
        Scene sceneTotal = new Scene(rootTotal, 800, 600);
        totalStage.setTitle("Авторизация");
        totalStage.setScene(sceneTotal);
        totalStage.setIconified(false);
        totalStage.show();
        totalStage.show();
        totalStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
    }

    public void exitLoginPass(ActionEvent actionEvent) {
        System.exit(0);
    }


    public void setController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.totalStage = new Stage();

    }
}
