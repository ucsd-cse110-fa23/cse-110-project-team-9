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
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
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
    //private RecipeController recipeController;
    private RecipeList recipeList;

    Button addButton;

    public AppFrame() {
        setTitle("Recipe List View");

        BorderPane mainLayout = createMainLayout();
        Scene mainScene = new Scene(mainLayout, 800, 600);
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
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(this); // Set the main stage as the owner
        popupStage.setTitle("Creating Recipe");

        Label recordingLabel = new Label("Recording. . .");
        recordingLabel.setVisible(false);

        Button closeButton = new Button("Cancel");
        closeButton.setOnAction(e -> popupStage.close());

        BorderPane popupLayout = new BorderPane();
        popupLayout.setBottom(closeButton);

        Button startRecording = new Button("start recording ingredients");
        startRecording.setOnAction(e -> {
            startRecording();
            recordingLabel.setVisible(true);
        });
        Button stopRecording = new Button("stop recording ingredients");
        stopRecording.setOnAction(e1 -> {
            recordingLabel.setVisible(false);
            popupStage.close();
            stopRecording();
            Recipe recipe = new Recipe();
            recipeList.getChildren().add(recipe);
            audioToText();
            ingredientsToRecipe();
            recipe.setRecipe(ChatGPT.returnPrompt());
            //recipe.setRecipe("eat");
            popupStage.close();
            recipe.getDeleteButton().setOnAction(e3->{
                recipeList.getChildren().remove(recipe);
        });
        });

        Scene popupScene = new Scene(popupLayout, 400, 300);
        popupStage.setScene(popupScene);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(startRecording, stopRecording, recordingLabel);

        popupLayout.setCenter(buttonBox);

        popupStage.showAndWait();
    }
    
    

    class Footer extends HBox {

        private Button addButton;
    
        Footer() {
            this.setPrefSize(500, 60);
            this.setStyle("-fx-background-color: #F0F8FF;");
            this.setSpacing(15);
    
            // set a default style for buttons - background color, font size, italics
            String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
    
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
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("PantryPal"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
        }
    }
    class RecipeList extends VBox {

        RecipeList() {
            this.setSpacing(3); // sets spacing between recipes
            this.setPrefSize(500, 560);
            this.setStyle("-fx-background-color: #F0F8FF;");
        }
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
        String[] args = new String[] { Whisper.getResult() };
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
    public Label getRecordingLable(){
        return recordingLabel;
    }
    private void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }
}