package com.codecool.processwatch.gui;

import com.codecool.processwatch.domain.ProcessWatchApp;
import com.codecool.processwatch.os.OsProcessSource;
import com.sun.javafx.menu.MenuItemBase;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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

        tableView.getColumns().add(pidColumn);
        tableView.getColumns().add(parentPidColumn);
        tableView.getColumns().add(userNameColumn);
        tableView.getColumns().add(processNameColumn);
        tableView.getColumns().add(argsColumn);

        var refreshButton = new Button("Refresh");
        refreshButton.setOnAction(actionEvent -> {
            System.out.println("List refreshed");
            ProcessWatchApp.option = "all";
            app.refresh();
        });
        var refreshQuestionMark = new Button ("?");
        refreshQuestionMark.setOnAction(actionEvent -> popUpWindow ("Refresh", "This will refresh the page!", primaryStage));
        var aboutButton = new Button("About");
        aboutButton.setOnAction(actionEvent -> popUpWindow("About", "This is our program!", primaryStage));
        var aboutQuestionMark = new Button("?");
        aboutQuestionMark.setOnAction(actionEvent -> popUpWindow("About", "Test", primaryStage));
        HBox refreshBox = new HBox(10, refreshButton, refreshQuestionMark);
        HBox aboutBox = new HBox(10, aboutButton, aboutQuestionMark);

        TextField userInput = new TextField();
        userInput.setPromptText("Search by Owner");
        userInput.setOnKeyPressed(actionEvent -> keyPressed(actionEvent, userInput, "user"));
        userInput.getText();
        HBox userInputHBox = new HBox(10, userInput);

        TextField ppidInput = new TextField();
        ppidInput.setPromptText("Search by PPID");
        ppidInput.setOnKeyPressed(actionEvent -> keyPressed(actionEvent, ppidInput, "ppid"));
        ppidInput.getText();
        HBox ppidInputHBox = new HBox(10, ppidInput);

        TextField cmdInput = new TextField();
        cmdInput.setPromptText("Search by Process Name");
        cmdInput.setOnKeyPressed(actionEvent -> keyPressed(actionEvent, cmdInput, "cmd"));
        cmdInput.getText();
        HBox cmdInputHBox = new HBox(10, cmdInput);

        var box = new VBox(refreshBox, aboutBox, userInputHBox, ppidInputHBox, cmdInputHBox);
        var scene = new Scene(box, 640, 480);
        var elements = box.getChildren();
        elements.addAll(tableView);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void popUpWindow(String title, String text, Stage primaryStage){
        final Stage dialog = new Stage();
        dialog.setTitle(title);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(text));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void keyPressed(KeyEvent e, TextField input, String option) {
        if (e.getCode()== KeyCode.ENTER){
            String inputText = input.getText();
            switch (option) {
                case "user":
                    ProcessWatchApp.userArg = inputText;
                    ProcessWatchApp.option = "user";
                    break;
                case "ppid":
                    if (!inputText.equals("")) {
                        ProcessWatchApp.ppidInput = Integer.parseInt(inputText);
                    }
                    ProcessWatchApp.option = "ppid";
                    break;
                case "cmd":
                    ProcessWatchApp.cmdInput = inputText;
                    ProcessWatchApp.option = "cmd";
            }
            app.refresh();
        }
    }
}
