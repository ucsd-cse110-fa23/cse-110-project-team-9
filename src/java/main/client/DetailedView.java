package java.main.client;
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

    public DetailedView(String recipeText) {
        setTitle("Detailed View");
        BorderPane mainLayout = createMainLayout(recipeText);
        Scene detailedScene = new Scene(mainLayout, 500, 600);
        setScene(detailedScene);
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
            setStyle("-fx-background-color: #F0F8FF;");

            Text titleText = new Text("Detailed View");
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
            setCenter(titleText);
        }
    }

    class Footer extends HBox {
        Footer() {
            setPrefSize(500, 60);
            setStyle("-fx-background-color: #F0F8FF;");
            setSpacing(15);

            Button backButton = new Button("Back");
            backButton.setOnAction(e -> close());

            getChildren().addAll(backButton);
            setAlignment(Pos.CENTER);
        }
    }
}