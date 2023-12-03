package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.rmi.RMISecurityException;
import java.util.ArrayList;
import java.util.Collections;
import javax.sound.sampled.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.*;
import java.net.*;
import org.json.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;


public class Model {
    public String performRequest(String method, String name, String[] typeAndDetails, String query) {
        // Implement your HTTP request logic here and return the response

        try {
            String urlString = "http://localhost:8100/";
            if (query != null) {
                urlString += "?=" + query;
            }
            URL url = new URI(urlString).toURL(); //error here bcuz of not refactoring query - fix at recipe controller 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            if (method.equals("POST") || method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(name + "!" + typeAndDetails[0] + "=" + typeAndDetails[1]);
                //System.out.print("temporary name" + "!" + typeAndDetails[0] + "=" + typeAndDetails[1]);
                out.flush();
                out.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

}

class ChatGPT {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";
    private static String result;

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {

        // Set request parameters
        String prompt1 = "Create a ";
        String prompt2 = " recipe with the following ingredients: ";
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

        if (args.length > 1 && args[1] != null) {
            prompt = prompt1 + args[1] + prompt2 + args[0];
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

    public static String returnPrompt() {
        String title = "";
        String delimiter = "Ingredients"; // Use "\n" as the delimiter
        title = result.replace("\n", "");
        title = title.replace("-", " ");
        int delimiterindex = title.indexOf(delimiter);
        if (delimiterindex != -1) {
            title = title.substring(0, delimiterindex);
        }
        return title;
    }

    public static String getResult() {
        return result;
    }
}

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

class MockChatGPT extends ChatGPT{
        private static String resultRecipe;
        private static String resultMealType;

        public void setResult(String mealType, String ingrediants){
            if (mealType == "breakfast"){
                resultRecipe = "A breakfast recipe with " + ingrediants;
            }
            else if (mealType == "lunch"){
                resultRecipe = "A lunch recipe with " + ingrediants;
            }
            else if (mealType == "dinner"){
                resultRecipe = "A dinner recipe with " + ingrediants;
            }
            else{
                resultRecipe = "random recipe with " + ingrediants;
            }
            resultMealType = mealType;
        }

        public String getResultRecipe() {
            return resultRecipe;
        }

        public String getMealType(){
            return resultMealType;
        }
    }

class MockWhisper extends Whisper{
    private static String result1;
    private static String result2;
    public static void main(String[] args) throws IOException, URISyntaxException {
        result1 = "Ingredients";
        result2 = "Meal Type";
    }
}

class Recipe extends HBox {

    private Button deleteButton;
    private Button detailedView;
    private Label recipeLabel;
    private String recipeTotal;
    private Label recipeType;
    private String id;

    private DetailedView currDetailedView;
    private DetailedController detailedController;
    private RecipeController recipeController;

    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 12 arial; -fx-pref-height: 30px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-height: 30px; -fx-text-fill: black;";

    Recipe() {
        this.setPrefSize(600, 30); // sets size of recipe
        this.setStyle("-fx-background-color: #F0F8FF; -fx-border-width: 0;");

        deleteButton = new Button("X");

        deleteButton.setTextAlignment(TextAlignment.CENTER);
        deleteButton.setPrefHeight(Double.MAX_VALUE);
        deleteButton.setStyle(
                "-fx-background-color: #FFA9A9; -fx-border-width: 0; -fx-border-color: #8B0000; -fx-font-weight: bold");
        this.getChildren().add(deleteButton);
        detailedView = new Button("Detailed View");
        detailedView.setStyle(defaultButtonStyle);
        detailedView.setAlignment(Pos.CENTER_RIGHT);

        recipeLabel = new Label("Recipe: "); // create task name text field
        recipeLabel.setStyle(defaultLabelStyle);
        recipeLabel.setTextAlignment(TextAlignment.CENTER);

        recipeType = new Label("Recipe Type: NONE");
        recipeType.setStyle("-fx-background-color: #BCEAD5; -fx-border-width: 1; -fx-border-color: #BCEAD5; -fx-font-weight: bold; -fx-pref-height: 30px");
        recipeType.setTextAlignment(TextAlignment.CENTER);

        this.getChildren().addAll(recipeType, recipeLabel, detailedView);
        currDetailedView = new DetailedView(this, this.getRecipeTotal());
        Model model = new Model();
        detailedController = new DetailedController(currDetailedView, model);
        recipeController = new RecipeController(this, model);
    }

    public String getID(){
        return id;
    }

    public void setID(String id){
        this.id = id;
    }


    public DetailedView getDetailedView(){
        return currDetailedView;
    }

    public void setDetailedView(DetailedView view){
        currDetailedView = view;
    }

    public Button getDetailedViewButton() {
        return detailedView;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }
    

    public String getRecipeLabelName() {
        return recipeLabel.getText();
    }
    // this will fix the query error for delete
    public String getQueryRecipeLabelName(){
        String formattedName;
        String base = recipeLabel.getText();

        formattedName = base.replace(' ', '-');

        return formattedName;

    }

    public String getRecipeType() {
        return recipeType.getText();
    }

    public void setRecipeType(String type) {
        //Run this line only if trying to test meal type tag without microphone working (Meal type registers anything other than breakfast, lunch, dinner)
        //recipeType.setText(type);

        if(type.toLowerCase().contains("breakfast")) {
            recipeType.setText("Breakfast");
        } else if (type.toLowerCase().contains("lunch")) {
            recipeType.setText("Lunch");
        } else if (type.toLowerCase().contains("dinner")) {
            recipeType.setText("Dinner"); 
        } else {
            return;
        }
        //recipeType.setVisible(true);
        //this.getChildren().add(1, recipeType);
    }

    public void setRecipeName(String newRecipe) {// set title of recipe
        recipeLabel.setText(newRecipe);
    }

    public void setRecipeTotal(String therecipe) {
        recipeTotal = therecipe; // put entire recipe in string
        // Now that the recipeTotal is set, initialize the DetailedView IMPORTANT
        currDetailedView = new DetailedView(this, this.getRecipeTotal());
    }

    public String getRecipeTotal() {
        return recipeTotal;
    }
    public void setRecipeText(String newText) {
        setRecipeTotal(newText);
    }
    
    public void setDeleteButtonAction(EventHandler<ActionEvent> eventHandler) {
        deleteButton.setOnAction(eventHandler);
    }

    public void setGetButtonAction(EventHandler<ActionEvent> eventHandler) {
        detailedView.setOnAction(eventHandler);
    }
}
