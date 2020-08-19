import javafx.application.Platform;
import javafx.event.ActionEvent;

public class AuthControl {
    private ClientController controller;

    public void sendLoginPass(ActionEvent actionEvent) {

    }

    public void exitLoginPass(ActionEvent actionEvent) {
        Platform.exit();
    }
    public void setController(ClientController controller) {
        this.controller = controller;
    }
}
