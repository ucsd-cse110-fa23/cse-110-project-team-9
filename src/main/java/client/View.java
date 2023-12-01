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

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.json.JSONException;

import java.io.*;
import java.net.*;
import org.json.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
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
import java.util.List;

import javax.sound.sampled.*;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.result.DeleteResult;


public class View{

    private Stage appFrame;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    private static RecipeList recipeList;

    private Recipe currRecipe;

    private Button saveButton;
    private Button addButton;

    public Stage getAppFrame(){
        return appFrame;
    }

    public Recipe getRecipe(){
        return currRecipe;
    }
    public String getRecipeText(){
        return currRecipe.getRecipeTotal();
    }

    public String getRecipeType(){
        return currRecipe.getRecipeType();
    }

    public String getRecipeName(){
        return currRecipe.getRecipeLabelName();
    }

    public RecipeList getRecipeList(){
        return recipeList;
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public View(Stage stage) {
        currRecipe = new Recipe();
        appFrame = stage;
        appFrame.setTitle("Recipe List View");
        BorderPane mainLayout = createMainLayout();
        Scene mainScene = new Scene(mainLayout, 600, 600);
        audioFormat = getAudioFormat();
        appFrame.setScene(mainScene);
        saveButton = new Button("Save Recipe");
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

        Recipe recipe = new Recipe();

        // currRecipeText = "Current Recipe: ";
        // currRecipeType = "Recipe Type: ";

        Label recipeText = new Label();
        Label recipeType = new Label();

        recipeText.setVisible(false);
        recipeType.setVisible(false);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(this.appFrame); // Set the main stage as the owner
        popupStage.setTitle("Creating Recipe");

        Label recordingLabel1 = new Label("Recording. . .");
        Label recordingLabel2 = new Label("Recording. . .");
        recordingLabel1.setVisible(false);
        recordingLabel2.setVisible(false);

        Button recTypeStart = new Button("Start Recording Meal Type (Breakfast, Lunch, Dinner)");
        recTypeStart.setOnAction(e -> {
            startRecording();
            recordingLabel1.setVisible(true);
        });

        Button recTypeStop = new Button("Stop Recording Meal Type");
        recTypeStop.setOnAction(e -> {
            recordingLabel1.setVisible(false);
            stopRecording();
            audioToText();
            setType();
            String typeLabel = "Recipe Type: " + currRecipe.getRecipeType();
            recipeType.setText(typeLabel);
            recipeType.setVisible(true);
        });

        Button startRecording = new Button("Start Recording Ingredients");
        startRecording.setOnAction(e -> {
            startRecording();
            recordingLabel2.setVisible(true);
        });
        Button stopRecording = new Button("Stop Recording Ingredients");
        stopRecording.setOnAction(e -> {
            recordingLabel2.setVisible(false);
            stopRecording();
            audioToText();
            ingredientsToRecipe();
            currRecipe.setRecipeTotal(ChatGPT.getResult()); 
            currRecipe.setRecipeName(ChatGPT.returnPrompt());
            //String recipeLabel = "Recipe Preview: " + currRecipe.getRecipeLabelName();
            recipeText.setText(currRecipe.getRecipeTotal());
            recipeText.setVisible(true);
        });

        Button closeButton = new Button("Back");
        closeButton.setOnAction(e -> popupStage.close());
        
        Button saveRecipe = new Button("Save Recipe");
        //saveRecipe.setStyle(defaultButtonStyle);
        saveRecipe.setOnAction(e -> {
            currRecipe.setRecipeName(ChatGPT.returnPrompt());
            currRecipe.setRecipeTotal(ChatGPT.getResult());
            currRecipe.setRecipeType(currRecipe.getRecipeType());// MAYBE correct?
            recipeList.getChildren().add(currRecipe);
            popupStage.close();

        });

        Button refreshButton = new Button("Regenerate Recipe");
        //refreshButton.setStyle(defaultButtonStyle);
        refreshButton.setOnAction(e -> {
            ingredientsToRecipe();
             currRecipe.setRecipeName(ChatGPT.returnPrompt());
            currRecipe.setRecipeTotal(ChatGPT.getResult());
            String recipeLabel = "Recipe Preview: " + currRecipe.getRecipeTotal();
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
        topBox.getChildren().addAll(typeBox, recordingBox);
        topBox.setSpacing(10);

        popupLayout.setTop(topBox);

        VBox middleBox = new VBox();

        String typeLabel = "Recipe Type: " + currRecipe.getRecipeType();
        String recipeLabel = "Recipe Preview: " + currRecipe.getRecipeLabelName();
        recipeType.setText(typeLabel);
        recipeText.setText(recipeLabel);
        middleBox.getChildren().addAll(recipeType, recipeText);
        middleBox.setSpacing(5);

        ScrollPane scroller = new ScrollPane(middleBox);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);
        popupLayout.setCenter(scroller);

        HBox bottomBox = new HBox();
        bottomBox.getChildren().addAll(refreshButton, closeButton, saveButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(5);
        popupLayout.setBottom(bottomBox);

        popupStage.showAndWait();
    }

    public void setSaveButtonAction(EventHandler<ActionEvent> eventHandler) {
        saveButton.setOnAction(eventHandler);
    }

    public void setDeleteButtonAction(EventHandler<ActionEvent> eventHandler) {
        currRecipe.getDeleteButton().setOnAction(eventHandler);
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
        String[] args = new String[] { Whisper.getResult(), currRecipe.getRecipeType() };
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
        currRecipe.setRecipeType(args[0]);
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
    
    public class RecipeList extends VBox implements Observable {

        MongoCollection<Document> recipesCollection;
        MongoDatabase recipe_db;
        String uri;
        MongoClient mongoClient;

        private List<InvalidationListener> listeners;
        private ObservableList<Recipe> recipes;
        RecipeList() {

            uri = "mongodb+srv://admin:123@cluster0.cp02bnz.mongodb.net/?retryWrites=true&w=majority";
            this.setSpacing(3); // sets spacing between recipes
            this.setPrefSize(500, 560);
            this.setStyle("-fx-background-color: #F0F8FF;");

            listeners = new ArrayList<>();
            mongoClient = MongoClients.create(uri);
            recipe_db = mongoClient.getDatabase("recipe_db");
            recipesCollection = recipe_db.getCollection("recipes");

            recipes = FXCollections.observableArrayList();
            fetchRecipesFromMongoDB();
        }
        
        public void deleteRecipe(Recipe recipe) {
            recipesCollection.deleteOne(eq("name", recipe.getRecipeLabelName()));
            notifyListeners();
        }

        public void fetchRecipesFromMongoDB() {
            List<Document> recipeDocuments = recipesCollection.find().into(new ArrayList<>());
            Platform.runLater(() -> {
                recipes.clear(); 
                for (Document document : recipeDocuments) {
                    Recipe recipe = new Recipe();
                    recipe.setRecipeName(document.getString("name"));
                    recipe.setRecipeType(document.getString("type"));
                    recipe.setRecipeTotal(document.getString("total"));
                    recipes.add(recipe);
                }
                notifyListeners();
            });
        }
        private void notifyListeners() {
            for (InvalidationListener listener : listeners) {
                listener.invalidated(this);
            }
        }
        @Override
        public void addListener(InvalidationListener listener) {
            listeners.add(listener);
        }
        @Override
        public void removeListener(InvalidationListener listener) {
            listeners.remove(listener);
        }
        public ObservableList<Recipe> getRecipes() {
            return recipes;
        }
        public void addRecipe(Recipe recipe) {
            getChildren().add(recipe);
        }
        public void updateRecipeListView() {
            recipeList.getChildren().clear(); 
            recipeList.getChildren().addAll(recipeList.getRecipes()); 
        }
    }
} 
