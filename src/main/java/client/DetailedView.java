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

    public DetailedView(String recipeText, Recipe recipe) {
        setTitle("Detailed Recipe View");
        BorderPane mainLayout = createMainLayout(recipeText, recipe);
        Scene detailedScene = new Scene(mainLayout, 600, 600);
        setScene(detailedScene);
        recipeData = recipeText;// store recipeText in field.

    }

    private BorderPane createMainLayout(String recipeText, Recipe recipe) {
        BorderPane root = new BorderPane();

        DVHeader header = new DVHeader();
        DVFooter footer = new DVFooter(recipe);

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
            setStyle("-fx-background-color: #8EC3B0");

            Text titleText = new Text("Detailed View");
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-background-color: #DEF5E5");
            setCenter(titleText);
        }
    }

    class DVFooter extends HBox {
        // private Button ediButton;
        DVFooter(Recipe recipe) {
            setPrefSize(500, 40);
            setStyle("-fx-background-color: #8EC3B0");
            setSpacing(10);
            setPadding(new Insets(5, 0, 5, 0));
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
                editButton.setStyle("-fx-font-weight: bold; -fx-background-color: #9ED5C5");
            });
            // editButton.setOnAction(e->
            // recipeText=editText()
            // );
            editButton.setStyle("-fx-font-weight: bold; fx-background-color: #BCEAD5");
            /*
             * Button saveButton = new Button("Save Changes");
             * saveButton.setOnAction(e -> {
             * recipeAsText.setEditable(false);
             * });
             */
            Button closeButton = new Button("Save and Close Detailed View");
            closeButton.setOnAction(e -> {
                recipeAsText.setEditable(false);
                recipe.setRecipeTotal(recipeAsText.getText());

                close();
            });// add method to delete the recipe as a whole
            closeButton.setStyle("-fx-background-color: #BCEAD5; -fx-font-weight: bold");

            // getChildren().addAll(saveButton);
            getChildren().addAll(editButton);
            getChildren().addAll(closeButton);
            setAlignment(Pos.CENTER);
        }

    }
}