package se.kth.id2212.ex1.threads.javafxclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Event handlers for the SimpleClient application.
 */
public class SimpleClientController implements Initializable {
    private ServerConnection server;

    @FXML
    private Button connectButton;
    @FXML
    private Button reverseButton;
    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private TextField reverseField;
    @FXML
    private Text outputText;

    @FXML
    private void connectHandler(ActionEvent event) {
        connectButton.setDisable(true);
        new ConnectService().start();
    }

    @FXML
    private void reverseHandler(ActionEvent event) throws IOException {
        reverseButton.setDisable(true);
        connectButton.setDisable(false);
        new ReverseService().start(); // multithreaded
        //outputText.setText(server.callServer(reverseField.getText())); // singlethreaded
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private class ConnectService extends Service<ServerConnection> {
        private ConnectService() {
            setOnSucceeded((WorkerStateEvent event) -> {
                server = getValue();
                reverseButton.setDisable(false);
            });
        }

        @Override
        protected Task<ServerConnection> createTask() {
            return new Task<ServerConnection>() {
                @Override
                protected ServerConnection call() {
                    return new ServerConnection(hostField.getText(),
                                                Integer.parseInt(portField.
                                                        getText()));
                }
            };
        }
    }

    private class ReverseService extends Service<String> {
        private ReverseService() {
            setOnSucceeded((WorkerStateEvent event) -> {
                outputText.setText(getValue());
            });

            setOnFailed((WorkerStateEvent event) -> {
                outputText.setText(getException().getMessage());
            });
        }

        @Override
        protected Task<String> createTask() {
            return new Task<String>() {
                @Override
                protected String call() throws IOException {
                    return server.callServer(reverseField.getText());
                }
            };
        }
    }
}
