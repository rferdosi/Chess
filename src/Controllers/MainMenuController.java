package Controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController extends FatherController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void about() {
        goTo("about");
    }
}