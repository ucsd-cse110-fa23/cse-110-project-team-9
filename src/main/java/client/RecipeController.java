package client;

import org.bson.json.JsonObject;

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
        //System.out.println("Get: " + recipe.getRecipeLabelName());
        
        System.out.print("ID: " + recipe.getID());
        String response = model.performRequest("GET", null, null, recipe.getID());
        System.out.print("GET RESPONSE: " + response);
/* 
        String name = response.substring(0,response.indexOf("!"));
        String type = response.substring(response.indexOf("!") + 1, response.indexOf("="));
        String details = response.substring(response.indexOf("=") + 1);


        recipe.setRecipeName(name);
        recipe.setRecipeTotal(details);
        recipe.setRecipeType(type);
        recipe.getDetailedView().getStage().show();
        */
    }
}
