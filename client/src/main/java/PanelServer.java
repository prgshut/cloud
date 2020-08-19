import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PanelServer implements Initializable {
    @FXML
    TableView tvServer;
    @FXML
    TextField textServer;
    @FXML
    ComboBox<String> cbDiskServer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
//    public String getSelectionFileName(){
//        if(!tvServer.isFocused()){
//            return null;
//        }
//        return tvServer.getSelectionModel().getSelectedItem().getNameFile();
//    }
    public String getSelectionPath(){
        return textServer.getText();
    }
}
