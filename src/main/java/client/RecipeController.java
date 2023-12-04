package client;

import org.bson.json.JsonObject;

import client.View.RecipeList;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import org.json.JSONObject;


public class RecipeController {
    private static Model model;
    private Recipe recipe;
    private static RecipeList recipeList;
    private static View view;

    public RecipeController(Recipe recipe, Model model) {
        this.recipe = recipe;
        this.model = model;
        
        this.recipe.setGetButtonAction(this::handleGetButton);
        this.recipe.setDeleteButtonAction(this::handleDeleteButton);
    }

    public void handleDeleteButton(ActionEvent event) {

        Recipe curr = this.recipe;

        String response = model.performRequest("DELETE", null, null, recipe.getQueryRecipeLabelName());
        curr.getID();

        //recipeList.deleteRecipe(curr);
        System.out.println("delete: " + response);
    }

    private void handleGetButton(ActionEvent event) {
        Recipe curr = this.recipe;
        
        String response = model.performRequest("GET", null, null, recipe.getQueryRecipeLabelName());
        curr.getID();
        //System.out.print("GET RESPONSE: " + response);
 
       
        final String name;
        final String type;
        final String details;

        
        JSONObject jsonObject = new JSONObject(response);
        String id = jsonObject.getString("_id");
        name = jsonObject.getString("name");
        type = jsonObject.getString("type");
        details = jsonObject.getString("details");
        /*
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Details: " + details);
        */

        Platform.runLater(() -> {
            recipe.setRecipeName(name);
            recipe.setRecipeTotal(details);
            recipe.setRecipeText(details);
            recipe.setRecipeType(type);
            recipe.setID(id);
            DetailedController detailedController = new DetailedController(recipe.getDetailedView(), model);
            recipe.getDetailedView().getStage().show();
        });
    }
}
