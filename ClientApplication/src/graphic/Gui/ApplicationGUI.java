package graphic.Gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileSystem;

/**
 * Created by casa on 26/05/17.
 */
public class ApplicationGUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent login= fxmlLoader.load(new FileInputStream(System.getProperty("user.dir") + File.separator +"ClientApplication" + File.separator+ "src" +File.separator + "graphic" +File.separator +"Gui" +File.separator+ "fxml" +File.separator+ "login_scene.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(login));
        stage.show();
    }
    public void startGUI(String[] args){
        launch(args);
    }
}
