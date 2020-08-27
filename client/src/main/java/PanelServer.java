import command.FileInfo;
import command.FileListServer;
import command.OtheCommand;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PanelServer implements Initializable {
    @FXML
    TableView<FileInfo> tvServer;
    @FXML
    TextField textServer;
    @FXML
    ComboBox<String> cbDiskServer;
    private String homeDir;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Создание серверной части");
        homeDir= OtheCommand.getHomeDir();
        System.out.println(homeDir);
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

        tvServer.getColumns().addAll(fileTypeColumn, filenameColumn, fileSizeColumn);
        tvServer.getSortOrder().add(fileTypeColumn);

        tvServer.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                       @Override
                                       public void handle(MouseEvent event) {
                                           if (event.getClickCount() == 2) {
                                               Path path = Paths.get(textServer.getText()).resolve(tvServer.getSelectionModel().getSelectedItem().getNameFile());
                                               if (Files.isDirectory(path)) {
                                                   updateList(path);
                                               }
                                           }
                                       }
                                   }

        );

        updateList(Paths.get(homeDir));

    }
    public void updateList(Path path) {
        System.out.println(path.toString());
        try {
            List<FileInfo> listFil= FileListServer.getListFileServer(path.toString());
            textServer.setText(path.normalize().toAbsolutePath().toString());
            tvServer.getItems().clear();
            tvServer.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            tvServer.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "По какой-то причине не удалось обновить список файлов", ButtonType.OK);
            alert.showAndWait();
        }
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
