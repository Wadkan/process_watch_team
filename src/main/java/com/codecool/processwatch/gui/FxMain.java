package com.codecool.processwatch.gui;

import com.codecool.processwatch.domain.ProcessWatchApp;
import com.codecool.processwatch.os.OsProcessSource;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * The JavaFX application Window.
 */
public class FxMain extends Application {
    private static final String TITLE = "Process Watch";

    private App app;

    /**
     * Entrypoint for the javafx:run maven task.
     *
     * @param args an array of the command line parameters.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Build the application window and set up event handling.
     *
     * @param primaryStage a stage created by the JavaFX runtime.
     */
    public void start(Stage primaryStage) {
        primaryStage.setTitle(TITLE);

        ObservableList<ProcessView> displayList = observableArrayList();
        app = new App(displayList);
        // TODO: Factor out the repetitive code
        var tableView = new TableView<ProcessView>(displayList);
        var pidColumn = new TableColumn<ProcessView, Long>("Process ID");
        pidColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, Long>("pid"));
        var parentPidColumn = new TableColumn<ProcessView, Long>("Parent Process ID");
        parentPidColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, Long>("parentPid"));
        var userNameColumn = new TableColumn<ProcessView, String>("Owner");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("userName"));
        var processNameColumn = new TableColumn<ProcessView, String>("Name");
        processNameColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("processName"));
        var argsColumn = new TableColumn<ProcessView, String>("Arguments");
        argsColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("args"));

//        var killColumn = new TableColumn<ProcessView, String>("KILL");
//        killColumn.setCellValueFactory(new PropertyValueFactory< ProcessView, String>("KILL THIS"));

        tableView.getColumns().add(pidColumn);
        tableView.getColumns().add(parentPidColumn);
        tableView.getColumns().add(userNameColumn);
        tableView.getColumns().add(processNameColumn);
        tableView.getColumns().add(argsColumn);
//        tableView.getColumns().add(killColumn);

        // select a row
//        TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
//        selectionModel.setSelectionMode(SelectionMode.SINGLE);
//        selectionModel.select(0);
//        selectionModel.clearSelection();

        var refreshButton = new Button("Refresh");
        refreshButton.setOnAction(ignoreEvent -> System.out.println("Button pressed"));

        var killButton = new Button("Kill process");
        killButton.setOnAction(ignoreEvent -> ProcessWatchApp.killDiscord(37362));

        var box = new VBox();
        var scene = new Scene(box, 640, 480);
        var elements = box.getChildren();
//        scene.getStylesheets().add("font_style.css");
        elements.addAll(refreshButton, killButton,
                        tableView);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
