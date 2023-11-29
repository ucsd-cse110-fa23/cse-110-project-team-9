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
import org.json.JSONException;

import java.io.*;
import java.net.*;
import org.json.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.rmi.RMISecurityException;
import java.util.ArrayList;
import java.util.Collections;
import javax.sound.sampled.*;

public class AppFrame extends Stage {

    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    // private RecipeController recipeController;
    private RecipeList recipeList;
    private String currRecipeText;
    private String currRecipeType;

    Button addButton;

    public AppFrame() {
        setTitle("Recipe List View");

        BorderPane mainLayout = createMainLayout();
        Scene mainScene = new Scene(mainLayout, 600, 600);
        audioFormat = getAudioFormat();
        setScene(mainScene);

    }

    private BorderPane createMainLayout() {
        BorderPane root = new BorderPane();

        Header header = new Header();
        Footer footer = new Footer();
        recipeList = new RecipeList();

        ScrollPane scroll = new ScrollPane(recipeList);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        root.setTop(header);
        root.setCenter(scroll);
        root.setBottom(footer);
        return root;
    }

    private void openPopup() {

        String defaultButtonStyle = "-fx-background-color: #BCEAD5; -fx-font-weight: bold";

        Recipe recipe = new Recipe();

        // currRecipeText = "Current Recipe: ";
        // currRecipeType = "Recipe Type: ";

        Label recipeText = new Label();
        Label recipeType = new Label();

        recipeText.setVisible(false);
        recipeType.setVisible(false);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(this); // Set the main stage as the owner
        popupStage.setTitle("Creating Recipe");

        Label recordingLabel1 = new Label("Recording. . .");
        Label recordingLabel2 = new Label("Recording. . .");
        recordingLabel1.setVisible(false);
        recordingLabel2.setVisible(false);

        Button recTypeStart = new Button("Start Recording Meal Type (Breakfast, Lunch, Dinner)");
        recTypeStart.setStyle(defaultButtonStyle);
        recTypeStart.setOnAction(e -> {
            startRecording();
            recordingLabel1.setVisible(true);
        });

        Button recTypeStop = new Button("Stop Recording Meal Type");
        recTypeStop.setStyle(defaultButtonStyle);
        recTypeStop.setOnAction(e -> {
            recordingLabel1.setVisible(false);
            stopRecording();
            audioToText();
            setType();
            String typeLabel = "Recipe Type: " + currRecipeType;
            recipeType.setText(typeLabel);
            recipeType.setVisible(true);
        });

        Button startRecording = new Button("Start Recording Ingredients");
        startRecording.setStyle(defaultButtonStyle);
        startRecording.setOnAction(e -> {
            startRecording();
            recordingLabel2.setVisible(true);
        });

        Button stopRecording = new Button("Stop Recording Ingredients");
        stopRecording.setStyle(defaultButtonStyle);
        stopRecording.setOnAction(e -> {
            recordingLabel2.setVisible(false);
            stopRecording();
            audioToText();
            ingredientsToRecipe();
            currRecipeText = ChatGPT.getResult();
            String recipeLabel = "Recipe Preview: " + currRecipeText;
            recipeText.setText(recipeLabel);
            recipeText.setVisible(true);

            // recipe.setRecipe("eat");
        });

        Button closeButton = new Button("Cancel Recipe");
        closeButton.setStyle(defaultButtonStyle);
        closeButton.setOnAction(e -> popupStage.close());

        recipe.getDeleteButton().setOnAction(e -> {
            recipeList.deleteRecipe(recipe);
        });

        Button saveRecipe = new Button("Save Recipe");
        saveRecipe.setStyle(defaultButtonStyle);
        saveRecipe.setOnAction(e -> {
            recipe.setRecipeName(ChatGPT.returnPrompt());
            recipe.setRecipeTotal(currRecipeText);
            recipe.setRecipeType(currRecipeType);
            recipeList.getChildren().add(recipe);
            popupStage.close();

        });


        Button refreshButton = new Button("Regenerate Recipe");
        refreshButton.setStyle(defaultButtonStyle);
        refreshButton.setOnAction(e -> {
            ingredientsToRecipe();
            currRecipeText = ChatGPT.getResult();
            String recipeLabel = "Recipe Preview: " + currRecipeText;
            recipeText.setText(recipeLabel);
            recipeText.setVisible(true);
        });

        BorderPane popupLayout = new BorderPane();
        Scene popupScene = new Scene(popupLayout, 600, 600);
        popupStage.setScene(popupScene);

        HBox typeBox = new HBox();
        typeBox.getChildren().addAll(recTypeStart, recTypeStop, recordingLabel1);
        typeBox.setAlignment(Pos.CENTER);
        typeBox.setSpacing(5);

        HBox recordingBox = new HBox();
        recordingBox.getChildren().addAll(startRecording, stopRecording, recordingLabel2);
        recordingBox.setSpacing(5);
        recordingBox.setAlignment(Pos.CENTER);

        VBox topBox = new VBox();
        topBox.setStyle("-fx-background-color: #8EC3B0");
        topBox.getChildren().addAll(typeBox, recordingBox);
        topBox.setSpacing(5);
        topBox.setPadding(new Insets(5, 0, 5, 0));

        popupLayout.setTop(topBox);

        VBox middleBox = new VBox();
        middleBox.setStyle("-fx-background-color: #DEF5E5");

        /* 
        Label recipeTypeLabel = new Label("Recipe Type: ");
        recipeTypeLabel.setStyle("-fx-font-weight: bold");

        Label recipePreviewLabel = new Label("Recipe Preview: ");
        recipePreviewLabel.setStyle("-fx-font-weight: bold");
        */

        String typeLabel = "Recipe Type: " + currRecipeType;
        String recipeLabel = "Recipe Preview: " + currRecipeText;
        recipeType.setText(typeLabel);
        recipeText.setText(recipeLabel);

        /* 
        ScrollPane scroll = new ScrollPane(recipeText);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        */

        middleBox.getChildren().addAll(recipeType, recipeText);
        middleBox.setSpacing(5);
        middleBox.setPadding(new Insets(5, 2, 5, 2));
        popupLayout.setCenter(middleBox);

        HBox bottomBox = new HBox();
        bottomBox.setStyle("-fx-background-color: #8EC3B0");
        bottomBox.getChildren().addAll(refreshButton, closeButton, saveRecipe);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(5);
        bottomBox.setPadding(new Insets(5, 0, 5, 0));
        popupLayout.setBottom(bottomBox);

        popupStage.showAndWait();
    }

    private void audioToText() {
        String[] args = new String[] { "recording.wav" };
        try {
            Whisper.main(args);
        } catch (URISyntaxException f) {
            f.printStackTrace();
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

    private void ingredientsToRecipe() {
        String[] args = new String[] { Whisper.getResult(), currRecipeType };
        try {
            ChatGPT.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void setType() {
        String[] args = new String[] { Whisper.getResult() };
        currRecipeType = args[0];
    }

    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 2;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    private void startRecording() {
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        try {
                            // the format of the TargetDataLine
                            DataLine.Info dataLineInfo = new DataLine.Info(
                                    TargetDataLine.class,
                                    audioFormat);
                            // the TargetDataLine used to capture audio data from the microphone
                            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                            targetDataLine.open(audioFormat);
                            targetDataLine.start();

                            // the AudioInputStream that will be used to write the audio data to a file
                            AudioInputStream audioInputStream = new AudioInputStream(
                                    targetDataLine);

                            // the file that will contain the audio data
                            File audioFile = new File("recording.wav");
                            AudioSystem.write(
                                    audioInputStream,
                                    AudioFileFormat.Type.WAVE,
                                    audioFile);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
        t.start();
    }

    public Label getRecordingLable() {
        return recordingLabel;
    }

    private void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }

    class Footer extends HBox {

        private Button addButton;

        Footer() {
            this.setPrefSize(500, 40);
            this.setStyle("-fx-background-color: #8EC3B0");
            this.setSpacing(15);

            // set a default style for buttons - background color, font size, italics
            String defaultButtonStyle = "-fx-background-color: #BCEAD5; -fx-font-weight: bold";

            addButton = new Button("Create a Recipe"); // text displayed on add button
            addButton.setStyle(defaultButtonStyle); // styling the button

            addButton.setOnAction(e -> {
                openPopup();
            });

            this.getChildren().addAll(addButton);
            this.setAlignment(Pos.CENTER); // aligning the buttons to center
        }

        public Button getAddButton() {
            return addButton;
        }
    }

    class Header extends HBox {

        Header() {
            this.setPrefSize(500, 60); // Size of the header
            this.setStyle("-fx-background-color: #8EC3B0");

            Text titleText = new Text("PantryPal"); // Text of the Header
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20");
            this.getChildren().add(titleText);
            this.setAlignment(Pos.CENTER); // Align the text to the Center
        }
    }

    class RecipeList extends VBox {

        RecipeList() {
            this.setSpacing(3); // sets spacing between recipes
            this.setPrefSize(500, 560);
            this.setStyle("-fx-background-color: #DEF5E5;");
        }

        public void deleteRecipe(Recipe recipe) {
            this.getChildren().remove(recipe);
        }
    }

}