package client;

import client.View.RecipeList;
import javafx.event.ActionEvent;

public class Controller {
    private static View view;
    private static Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        
        this.view.setSaveButtonAction(this::handlePostButton);
    }

    private void handlePostButton(ActionEvent event) {
        Recipe curr = view.getRecipe();
        curr.setRecipeName(view.getRecipeName());
        curr.setRecipeType(view.getRecipeType());
        String[] typeAndRecipe = new String[]{curr.getRecipeType(), curr.getRecipeTotal()};
        String response = model.performRequest("POST", curr.getRecipeLabelName(), typeAndRecipe, null);
        //pinging to server
        System.out.print("posting name: " + curr.getRecipeLabelName() + "total: " + curr.getRecipeTotal() + "TYPE AND RECIPE: " + typeAndRecipe[0] + typeAndRecipe[1]);
        view.getRecipeList().getChildren().add(curr);
        //view.showAlert("Response", response);
    }
}