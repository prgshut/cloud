import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PanelClient implements Initializable {
    @FXML
    TableView<FileInfo> tvClient;
    @FXML
    ComboBox<String> cbDiskClient;
    @FXML
    TextField textClient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        TableColumn<FileInfo, String> filenameColumn = new TableColumn<>("Имя");
        filenameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNameFile()));
        filenameColumn.setPrefWidth(140);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });
        fileSizeColumn.setPrefWidth(60);

        tvClient.getColumns().addAll(fileTypeColumn,filenameColumn,fileSizeColumn);
        tvClient.getSortOrder().add(fileTypeColumn);

        cbDiskClient.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            cbDiskClient.getItems().add(p.toString());
        }
        cbDiskClient.getSelectionModel().select(0);

        tvClient.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                       @Override
                                       public void handle(MouseEvent event) {
                    if (event.getClickCount()==2){
                        Path path = Paths.get(textClient.getText()).resolve(tvClient.getSelectionModel().getSelectedItem().getNameFile());
                        if (Files.isDirectory(path)){
                            updateList(path);
                        }
                    }
                                       }
                                   }

        );

        updateList(Paths.get("."));
    }
    public void updateList(Path path) {
        try {
            textClient.setText(path.normalize().toAbsolutePath().toString());
            tvClient.getItems().clear();
            tvClient.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            tvClient.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "По какой-то причине не удалось обновить список файлов", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void upClient(ActionEvent actionEvent) {
        Path upPath = Paths.get(textClient.getText()).getParent();
        if(upPath!=null){
            updateList(upPath);
        }
    }

    public void selecDisk(ActionEvent actionEvent) {
        ComboBox<String> selct = (ComboBox) actionEvent.getSource();
        updateList(Paths.get(selct.getSelectionModel().getSelectedItem()));
    }
    public String getSelectionFileName(){
        if(!tvClient.isFocused()){
            return null;
        }
        return tvClient.getSelectionModel().getSelectedItem().getNameFile();
    }
    public String getSelectionPath(){
        return textClient.getText();
    }
}
