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
        
<<<<<<< Updated upstream
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
=======
        String response = model.performRequest("GET", null, null, recipe.getQueryRecipeLabelName());
        curr.getID();
        //System.out.println(recipe.getQueryRecipeLabelName());
        //System.out.print("GET RESPONSE: " + response);
 
       
        final String name;
        final String type;
        final String details;
        final String user;
        
        JSONObject jsonObject = new JSONObject(response);
        String id = jsonObject.getString("_id");
        name = jsonObject.getString("name");
        type = jsonObject.getString("type");
        details = jsonObject.getString("details");
        user = jsonObject.getString("user");
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
            recipe.setUser(user);
            DetailedController detailedController = new DetailedController(recipe.getDetailedView(), model);
            recipe.getDetailedView().getStage().show();
        });
>>>>>>> Stashed changes
    }
}
