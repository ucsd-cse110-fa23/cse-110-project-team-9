package PantryPal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.rmi.RMISecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import javax.sound.sampled.*;

public class Controller {
    Button mainScene, detailedView;

    public void handleBtn1() throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("detailedView"));

        Stage window = (Stage) mainScene.getScene().getWindow();
        window.setScene(new Scene(root, 500, 600));
    }

    public void handleBtn2() throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("mainScene"));

        Stage window = (Stage) detailedView.getScene().getWindow();
        window.setScene(new Scene(root, 500, 600));
    }
}
