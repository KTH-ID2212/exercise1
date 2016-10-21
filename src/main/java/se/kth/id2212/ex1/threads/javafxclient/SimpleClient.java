package se.kth.id2212.ex1.threads.javafxclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Connects to a server, sends a message and displays the answer.
 */
public class SimpleClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "SimpleClient.fxml"));
        Parent rootNode = loader.load();
        Scene scene = new Scene(rootNode);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args There are no command line parameters.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
