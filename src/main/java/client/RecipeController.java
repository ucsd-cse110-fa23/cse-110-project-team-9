package client;

import client.View.RecipeList;
import javafx.event.ActionEvent;

public class RecipeController {
    private static Model model;
    private Recipe recipe;

    public RecipeController(Recipe recipe, Model model) {
        this.recipe = recipe;
        this.model = model;
        
        this.recipe.setGetButtonAction(this::handleGetButton);
        this.recipe.setDeleteButtonAction(this::handleDeleteButton);
    }

    public void handleDeleteButton(ActionEvent event) {
        System.out.println("delete: " + recipe.getRecipeLabelName());
        String response = model.performRequest("DELETE", null, null, recipe.getRecipeLabelName());
        
    }

    private void handleGetButton(ActionEvent event) {
        System.out.println("Get: " + recipe.getRecipeLabelName());
        recipe.getDetailedView().getStage().show();
        String response = model.performRequest("GET", null, null, recipe.getRecipeLabelName());
    }
}
