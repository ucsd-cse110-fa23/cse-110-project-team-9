package client;

import client.View.RecipeList;
import javafx.event.ActionEvent;

public class Controller {
    private static View view;
    private static Model model;
    private RecipeList recipeList;
    private Recipe recipe;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        
        this.view.setSaveButtonAction(this::handlePostButton);
    }

    private void handlePostButton(ActionEvent event) {
        String name = view.getRecipeName();
        String[] typeAndRecipe = new String[]{view.getRecipeType(), view.getRecipeText()};
        String response = model.performRequest("POST", name, typeAndRecipe, null);
        //pinging to server
        Recipe curr = new Recipe();
        curr.setRecipeName(name);
        curr.setRecipeType(typeAndRecipe[0]);
        curr.setRecipeTotal(typeAndRecipe[1]);
        System.out.println("posting name: " + curr.getRecipeLabelName() + "total: " + curr.getRecipeTotal());
        view.getRecipeList().getChildren().add(curr);
        view.showAlert("Response", response);
    }
}