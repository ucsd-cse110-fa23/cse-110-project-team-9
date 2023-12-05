package client;

import client.View.RecipeList;
import javafx.application.Platform;
import javafx.event.ActionEvent;

public class Controller {
    private static View view;
    private static Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        
        this.view.setSaveButtonAction(this::handlePostButton);
        this.view.setDeleteButtonAction(this::handleDeleteButton);
    }

    private void handlePostButton(ActionEvent event) {

        Recipe curr = view.getRecipe();
        curr.setRecipeName(view.getRecipeName());
        curr.setRecipeType(view.getRecipeType());
        
        String[] typeAndRecipe = new String[]{curr.getRecipeType(), view.getUser(), curr.getRecipeTotal()};
        String response = model.performRequest("POST", curr.getQueryRecipeLabelName(), typeAndRecipe, null);
        curr.setID(response);
        
        System.out.print("POST RESPONSE: " + response);
        
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

    }
}