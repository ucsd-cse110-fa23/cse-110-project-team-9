package PantryPal;

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
//import java.util.Scanner;

class Whisper {

    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";
    private static final String FILE_PATH = "/Users/arnavkamra/Documents/PantryPal/PantryPal/you almost made me drop my croissant vine.wav";

    private static String result;

    // Helper method to write a parameter to the output stream in multipart form
    // data format
    private static void writeParameterToOutputStream(OutputStream outputStream, String parameterName,
            String parameterValue, String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes());
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    // Helper method to write a file to the output stream in multipart form data
    // format
    private static void writeFileToOutputStream(OutputStream outputStream, File file, String boundary)
            throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                ("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n").getBytes());
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    // Helper method to handle a successful response
    private static void handleSuccessResponse(HttpURLConnection connection) throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject responseJson = new JSONObject(response.toString());
        String generatedText = responseJson.getString("text");

        result = generatedText;
        // Save result
        // System.out.println("Transcription Result: " + generatedText);
    }

    public static String getResult() {
        return result;
    }

    // Helper method to handle an error response
    private static void handleErrorResponse(HttpURLConnection connection) throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream()));
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        System.out.println("Error Result: " + errorResult);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {

        String fileName = FILE_PATH;

        if (args.length > 0 && args[0] != null) {
            fileName = args[0];
        }

        // Create file object from file path
        File file = new File(fileName);

        // Set up HTTP connection
        URL url = new URI(API_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

        // Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();

        // Write model parameter to request body
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);

        // Write file parameter to request body
        writeFileToOutputStream(outputStream, file, boundary);

        // Write closing boundary to request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        // Flush and close output stream
        outputStream.flush();
        outputStream.close();

        // Get response code
        int responseCode = connection.getResponseCode();

        // Check response code and handle response accordingly
        if (responseCode == HttpURLConnection.HTTP_OK) {
            handleSuccessResponse(connection);
        } else {
            handleErrorResponse(connection);
        }

        // Disconnect connection
        connection.disconnect();
    }

}

class ChatGPT {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";
    private static String result;

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {

        // Set request parameters
        String prompt = "Create a recipe with the following ingredients: ";
        int maxTokens = 1000;

        /*
         * if (args.length > 0 && args[0] != null) {
         * maxTokens = Integer.parseInt(args[0]);
         * }
         */

        if (args.length > 0 && args[0] != null) {
            prompt += args[0];
        }

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);

        // Create the HTTP Client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request object
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", String.format("Bearer %s", API_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        // Send the request and receive the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Process the response
        String responseBody = response.body();

        JSONObject responseJson = new JSONObject(responseBody);

        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text");

        result = generatedText;

        // System.out.println(generatedText);

    }

    public static String getResult() {
        return result;
    }

}

class DetailedView extends BorderPane {
    private Button backbutton;
    private Header header;
    private Recipe recipe;

    DetailedView(Stage primaryStage, Recipe recipe) {
        this.recipe = recipe;
        header = new Header(recipe.getRecipe().getText());
        Text recipeAsText = new Text(recipe.getRecipe().getText());
        ScrollPane scroll = new ScrollPane(recipeAsText);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        this.setCenter(scroll);
    }

}

class Recipe extends HBox {

    private Button deleteButton;
    private Button startButton;
    private Button stopButton;
    private Label recordingLabel;
    private Label recipe;
    // private AudioFormat audioFormat;

    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 12 arial; -fx-pref-height: 30px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-height: 30px; -fx-text-fill: red; visibility: hidden";

    Recipe() {
        this.setPrefSize(400, 1000); // sets size of recipe
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        deleteButton = new Button("X");
        deleteButton.setTextAlignment(TextAlignment.CENTER);
        deleteButton.setPrefHeight(Double.MAX_VALUE);
        deleteButton.setStyle(
                "-fx-background-color: #FFA9A9; -fx-border-width: 0; -fx-border-color: #8B0000; -fx-font-weight: bold");
        this.getChildren().add(deleteButton);

        startButton = new Button("Start Recording Ingredients");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop Recording");
        stopButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);
        recordingLabel.setTextAlignment(TextAlignment.CENTER);

        recipe = new Label("Recipe: "); // create task name text field
        recipe.setTextAlignment(TextAlignment.CENTER);

        this.getChildren().addAll(startButton, stopButton, recordingLabel, recipe);

        // audioFormat = getAudioFormat();

    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public Label getRecordingLabel() {
        return recordingLabel;
    }

    public Label getRecipe() {
        return recipe;
    }

    public void setRecipe(String newRecipe) {
        recipe.setText(newRecipe);
    }

}

class RecipeList extends VBox {

    RecipeList() {
        this.setSpacing(3); // sets spacing between recipes
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }
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

        this.getChildren().addAll(addButton);
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getAddButton() {
        return addButton;
    }
}

class Header extends HBox {

    Header(String title) {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text(title); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class AppFrame extends BorderPane {

    private Header header;
    private Footer footer;
    private RecipeList recipeList;

    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;

    private Button addButton;
    private Label recordingLabel;

    private Stage primaryStage;

    AppFrame(Stage primaryStage) {

        this.primaryStage = primaryStage;
        // Initialise the header Object
        header = new Header("PantryPal");

        // Create a recipeList Object to hold the recipes
        recipeList = new RecipeList();

        // Initialise the Footer Object
        footer = new Footer();

        /*
         * Website Title: How to create a scroll pane using java fx
         * Source:
         * https://www.tutorialspoint.com/how-to-create-a-scroll-pane-using-javafx
         * Date: 10/5/1023
         * Usage: Example of a scroll pane to see how ScrollPane() works
         */
        ScrollPane scroll = new ScrollPane(recipeList);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scroll);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Initialise Button Variables through the getters in Footer
        addButton = footer.getAddButton();

        // Get the audio format
        audioFormat = getAudioFormat();

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners() {

        // Add button functionality
        addButton.setOnAction(e -> {
            // Create a new task
            Recipe recipe = new Recipe();
            // Add task to tasklist
            recipeList.getChildren().add(recipe);
            // Add deleteButton
            Button deleteButton = recipe.getDeleteButton();
            Button startButton = recipe.getStartButton();
            Button stopButton = recipe.getStopButton();
            // recordingLabel = recipe.getRecordingLabel();

            deleteButton.setOnAction(e1 -> {
                recipeList.getChildren().remove(recipe);
            });

            // Start Button
            startButton.setOnAction(e2 -> {
                startRecording();
            });

            // Stop Button
            stopButton.setOnAction(e3 -> {
                stopRecording();
                // Transcribing recording
                audioToText();
                ingredientsToRecipe();
                recipe.setRecipe(ChatGPT.getResult());
                Platform.runLater(() -> { //Using temproot until recipeData is initialized properly
                    DetailedView root = new DetailedView(primaryStage, recipe);
                    primaryStage.setTitle("Detailed View");
                    primaryStage.setScene(new Scene(root, 500, 600));
                    primaryStage.show();
                });
            });
        });
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
                            recordingLabel.setVisible(true);

                            // the AudioInputStream that will be used to write the audio data to a file
                            AudioInputStream audioInputStream = new AudioInputStream(
                                    targetDataLine);

                            // the file that will contain the audio data
                            File audioFile = new File("recording.wav");
                            AudioSystem.write(
                                    audioInputStream,
                                    AudioFileFormat.Type.WAVE,
                                    audioFile);
                            recordingLabel.setVisible(false);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
        t.start();
    }

    private void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();

    }
}

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AppFrame root = new AppFrame(primaryStage);
        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(root, 500, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
