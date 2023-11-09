package client;

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

public class DetailedView extends Stage {
    private String recipeData;
    public DetailedView(String recipeText) {
        setTitle("Detailed View");
        BorderPane mainLayout = createMainLayout(recipeText);
        Scene detailedScene = new Scene(mainLayout, 500, 600);
        setScene(detailedScene);
        recipeData=recipeText;//store recipeText in field.
    }                                   

    private BorderPane createMainLayout(String recipeText) {
        BorderPane root = new BorderPane();

        Header header = new Header();
        Text recipeAsText = new Text(recipeText);
        ScrollPane scroll = new ScrollPane(recipeAsText);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        Footer footer = new Footer();

        root.setTop(header);
        root.setCenter(scroll);
        root.setBottom(footer);

        return root;
    }

    class Header extends BorderPane {
        Header() {
            setPrefSize(500, 60);
            setStyle("-fx-background-color: #e8e113;");

            Text titleText = new Text("Detailed View");
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-background-color: Orange");
            setCenter(titleText);
        }
    }

    class Footer extends HBox {
        //private Button ediButton;
        Footer() {
            setPrefSize(500, 60);
            setStyle("-fx-background-color: #F0F8FF;");
            setSpacing(15);

            Button backButton = new Button("Save Recipe");//is back button save?
            backButton.setOnAction(e -> close());
            backButton.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-background-color: Green");
            Button editButton= new Button("edit recipe");
           // editButton.setOnAction(e-> 
                //recipeText=editText()
            //);
            editButton.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-background-color: Green");


            Button deleteButton=new Button("Delete Button");
            deleteButton.setOnAction(e->{
                //this.recipe
                close();
            });//add method to delete the recipe as a whole
            deleteButton.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-background-color: Green");


            getChildren().addAll(backButton);
            getChildren().addAll(editButton);
            getChildren().addAll(deleteButton);
            setAlignment(Pos.CENTER);
        }

        
    }
}