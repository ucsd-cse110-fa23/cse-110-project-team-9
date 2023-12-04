package client;

<<<<<<< Updated upstream
import client.View.RecipeList;
=======
import javafx.application.Platform;
>>>>>>> Stashed changes
import javafx.event.ActionEvent;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        
        this.view.setSaveButtonAction(this::handlePostButton);
<<<<<<< Updated upstream
=======
        //this.view.setDeleteButtonAction(this::handleDeleteButton);
>>>>>>> Stashed changes
    }

    private void handlePostButton(ActionEvent event) {
        Recipe curr = view.getRecipe();
        System.out.print("curr.getRecipeType: " + curr.getRecipeType());
        curr.setRecipeName(view.getRecipeName());
        curr.setRecipeType(view.getRecipeType());
        curr.setUser(view.getUser());
        String[] typeAndRecipe = new String[]{curr.getRecipeType().substring(13), curr.getUser(), curr.getRecipeTotal()};
        String response = model.performRequest("POST", curr.getRecipeLabelName(), typeAndRecipe, null);
        curr.setID(response);
        //pinging to server
        System.out.print("POST RESPONSE: " + response);
<<<<<<< Updated upstream
        view.getRecipeList().getChildren().add(curr);
        //view.showAlert("Response", response);
=======
        
        new Thread(() -> {
            view.getRecipeList().fetchRecipesFromMongoDB();
            
            Platform.runLater(() -> {
                view.getRecipeList().updateRecipeListView();
            });
        }).start();
        
    }
    // this code is not being used, using recipe controller for delete
    public void handleDeleteButton(ActionEvent event) {

        Recipe curr = view.getRecipe();

        String response = model.performRequest("DELETE", null, null, curr.getQueryRecipeLabelName());
        curr.getID();
        
        System.out.println("delete: " + response);

        new Thread(() -> {
            view.getRecipeList().fetchRecipesFromMongoDB();
            Platform.runLater(() -> {
                view.getRecipeList().updateRecipeListView();
            });
        }).start();

>>>>>>> Stashed changes
    }
}