package dev.graumann.neuralnetwork;

import dev.graumann.neuralnetwork.controller.ViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.net.URL;

import dev.graumann.guidecorator.GUIDecorator;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
        FXMLLoader fxmlLoader = new FXMLLoader();

        URL path = new File(System.getProperty("user.dir") + "/src/main/java/view/view2.fxml").toURI().toURL();

        Parent root = FXMLLoader.load(path);
        */

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dev/graumann/neuralnetwork/fxml/view.fxml"));
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("Neuronal Network - Snake Game");
        primaryStage.setScene(new Scene(root));

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });

        GUIDecorator guiDecorator = new GUIDecorator();
        guiDecorator.decorate(primaryStage);
        guiDecorator.setTitelbarSVGIconContent("M6,9H8V11H10V13H8V15H6V13H4V11H6V9M18.5,9A1.5,1.5 0 0,1 20,10.5A1.5,1.5 0 0,1 18.5,12A1.5,1.5 0 0,1 17,10.5A1.5,1.5 0 0,1 18.5,9M15.5,12A1.5,1.5 0 0,1 17,13.5A1.5,1.5 0 0,1 15.5,15A1.5,1.5 0 0,1 14,13.5A1.5,1.5 0 0,1 15.5,12M17,5A7,7 0 0,1 24,12A7,7 0 0,1 17,19C15.04,19 13.27,18.2 12,16.9C10.73,18.2 8.96,19 7,19A7,7 0 0,1 0,12A7,7 0 0,1 7,5H17M7,7A5,5 0 0,0 2,12A5,5 0 0,0 7,17C8.64,17 10.09,16.21 11,15H13C13.91,16.21 15.36,17 17,17A5,5 0 0,0 22,12A5,5 0 0,0 17,7H7Z");
        guiDecorator.setRepositoryURL("https://github.com/BAAMMM1/AI4-IS-P2-Lernen");
        
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
