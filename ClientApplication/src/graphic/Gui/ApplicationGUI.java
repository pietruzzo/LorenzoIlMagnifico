package graphic.Gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by casa on 26/05/17.
 */
public class ApplicationGUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
    stage.show();
    stage.setScene(new Scene(new GridPane()));
    }
    public void startGUI(String[] args){
        launch(args);
    }
}
