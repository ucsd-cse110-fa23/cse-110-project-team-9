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
        recipeList = view.getRecipeList();
        
        this.view.setSaveButtonAction(this::handlePostButton);
        //this.view.getRecipe().setGetButtonAction(this::handleGetButton);
        //this.view.getRecipe().setDeleteButtonAction(this::handleDeleteButton);
    }

    private void handlePostButton(ActionEvent event) {
        String name = view.getRecipeName();
        String[] typeAndRecipe = new String[]{view.getRecipeType(), view.getRecipeText()};
        String response = model.performRequest("POST", name, typeAndRecipe, null);
        Recipe curr = new Recipe();
        curr.setRecipeName(name);
        curr.setRecipeTotal(typeAndRecipe[1]);
        curr.setRecipeType(typeAndRecipe[0]);
        System.out.println("posting name: " + curr.getRecipeLabelName() + "total: " + curr.getRecipeTotal());
        view.getRecipeList().getChildren().add(curr);
        view.showAlert("Response", response);
        //view.getAppFrame().close();
    }

    private void handleGetButton(ActionEvent event) {
        String query = view.getRecipeName();
        System.out.println("Get: " + query);
        //view.getRecipe().setDetailedView(new DetailedView(view.getRecipe()));
        view.getRecipe().getDetailedView().getStage().show();
        String response = model.performRequest("GET", null, null, query);
        view.showAlert("Response", response);
    }

    public static void handleDeleteButton(ActionEvent event) {
        String query = view.getRecipeName();
        System.out.println("delete: " + query);
        String response = model.performRequest("DELETE", null, null, query);
        view.getRecipeList().getChildren().removeIf(n -> (
            ((Recipe) n).getRecipeLabelName().equals(query)
        ));
        view.showAlert("Response", response);
    }
}