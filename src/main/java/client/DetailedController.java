package client;

import javafx.event.ActionEvent;

public class DetailedController {
    private DetailedView view;
    private Model model;

    public DetailedController(DetailedView view, Model model) {
        this.view = view;
        this.model = model;

        this.view.setPutButtonAction(this::handlePutButton);
    }

    private void handlePutButton(ActionEvent event) {
        String name = view.getRecipeName();
        String[] typeAndRecipe = new String[]{view.getRecipeType(), view.getRecipeTotal()};
        String response = model.performRequest("POST", name, typeAndRecipe, null);
        Recipe curr = view.getRecipe();
        curr.setRecipeName(name);
        curr.setRecipeTotal(typeAndRecipe[1]);
        curr.setRecipeType(typeAndRecipe[0]);

        view.getRecipeAsText().setEditable(false);
        view.getRecipe().setRecipeTotal(view.getRecipeAsText().getText());
        view.getStage().close();

        view.showAlert("Response", response);
    }
}