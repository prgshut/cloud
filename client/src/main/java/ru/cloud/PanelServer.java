package ru.cloud;

import ru.cloud.command.FileInfo;
import ru.cloud.command.FileListServer;
import ru.cloud.command.OtheCommand;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

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
        homeDir= OtheCommand.getHomeDir();
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
                                               if (tvServer.getSelectionModel().getSelectedItem().getType()==FileInfo.FileType.DIRECTORY) {
                                                   updateList(path);
                                               }
                                           }
                                       }
                                   }

        );

        updateList(Paths.get(homeDir));

    }
    public void updateList(Path path) {
        List<FileInfo> listFil= FileListServer.getListFileServer(path.toString());
        textServer.setText(path.normalize().toString());
        tvServer.getItems().clear();
        tvServer.getItems().addAll(listFil);
        tvServer.sort();
    }

    public void update(ActionEvent actionEvent) {
        updateList(Paths.get(textServer.getText()));
    }

    public void upServer(ActionEvent actionEvent) {
        Path upPath = Paths.get(textServer.getText()).getParent();
        if (upPath != null) {
            updateList(upPath);
        }
    }
    public String getSelectionFileName() {
        if (!tvServer.isFocused()) {
            return null;
        }
        return tvServer.getSelectionModel().getSelectedItem().getNameFile();
    }
    public String getSelectionPath() {
        return textServer.getText();
    }
}