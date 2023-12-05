package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import client.View.RecipeList;

import org.json.JSONException;

import java.io.*;
import java.net.*;
import org.json.*;

import javafx.application.Application;
import javafx.application.Platform;
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
import javax.sound.sampled.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
//import java.util.Scanner;


public class PantryPalApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // RecipeController recipeController = new RecipeController(primaryStage);
        LoginView view = new LoginView();
        Model model = new Model();
        LoginController controller = new LoginController(view, model, primaryStage);
        
        Scene scene = new Scene(view.getGrid(), 400, 150);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create Account/Log-In");
        primaryStage.show();
    }

    }