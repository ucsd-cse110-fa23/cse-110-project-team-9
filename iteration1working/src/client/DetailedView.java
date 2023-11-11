package client;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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
    private static TextArea recipeAsText;

    public DetailedView(String recipeText) {
        setTitle("Detailed View");
        BorderPane mainLayout = createMainLayout(recipeText);
        Scene detailedScene = new Scene(mainLayout, 600, 600);
        setScene(detailedScene);
        recipeData = recipeText;// store recipeText in field.

    }

    private BorderPane createMainLayout(String recipeText) {
        BorderPane root = new BorderPane();

        DVHeader header = new DVHeader();
        DVFooter footer = new DVFooter();

        recipeAsText = new TextArea(recipeText);// create editable text area
        recipeAsText.setEditable(false);
        ScrollPane scroll = new ScrollPane(recipeAsText);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        root.setTop(header);
        root.setCenter(scroll);
        root.setBottom(footer);

        return root;
    }

    public String saveNewRecipe() {
        return recipeAsText.getText();
    }

    class DVHeader extends BorderPane {
        DVHeader() {
            setPrefSize(500, 60);
            setStyle("-fx-background-color: #e8e113;");

            Text titleText = new Text("Detailed View");
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-background-color: Orange");
            setCenter(titleText);
        }
    }

    class DVFooter extends HBox {
        // private Button ediButton;
        DVFooter() {
            setPrefSize(500, 60);
            setStyle("-fx-background-color: #F0F8FF;");
            setSpacing(15);
            /*
             * Button saveButton = new Button("Save Recipe");//is back button save?
             * saveButton.setOnAction(e -> {
             * 
             * close();
             * });
             */
            // saveButton.setStyle("-fx-font-weight: bold; -fx-font-size: 20;
            // -fx-background-color: Green");

            Button editButton = new Button("Edit Recipe");

            editButton.setOnAction(e -> {
                recipeAsText.setEditable(true);
                editButton.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-background-color: Green");
            });
            // editButton.setOnAction(e->
            // recipeText=editText()
            // );
            editButton.setStyle("-fx-font-weight: bold; -fx-font-size: 20");
            /*
             * Button saveButton = new Button("Save Changes");
             * saveButton.setOnAction(e -> {
             * recipeAsText.setEditable(false);
             * });
             */
            Button closeButton = new Button("Save and Close Detailed View");
            closeButton.setOnAction(e -> {
                recipeAsText.setEditable(false);
                close();
            });// add method to delete the recipe as a whole
            closeButton.setStyle("-fx-font-weight: bold; -fx-font-size: 20");

            // getChildren().addAll(saveButton);
            getChildren().addAll(editButton);
            getChildren().addAll(closeButton);
            setAlignment(Pos.CENTER);
        }

    }
}