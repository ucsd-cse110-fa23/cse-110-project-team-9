package client;

import org.junit.jupiter.api.Test;

import client.AppFrame.RecipeList;
import javafx.scene.control.Button;
import javafx.scene.shape.Ellipse;

import org.junit.jupiter.api.BeforeEach;
import client.Recipe;
import client.AppFrame.RecipeList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppFrameTest {
    private AppFrame a= new AppFrame();
    private Recipe r=new Recipe();
    
    //@BeforeEach
    //void initialize(){
      //  RecipeList rlist=new RecipeList();
//    }
    @Test
    void dummyTest(){
        String a="s";
        String c="s";
        assertEquals(a, c);
    }


    @Test
    void RecipeCreateNameTest(){//recipe named correctly
        Recipe r= new Recipe();
        r.setRecipeName("apple pie");
        //String b = "apple";
        assertEquals(r.getRecipeLabelName(),"apple pie");
    }

    @Test 
    void savetoRecipeListTest(){
        Recipe r= new Recipe();
        RecipeList rl=new RecipeList();
        Recipe r2=new Recipe();
        rl.getChildren().add(r2);

        String s= "save";
        if(s=="save"){
            rl.getChildren().add(r);
        }
        assertEquals(rl.getChildren().contains(r), true);
        //Filewriter r= n
        //RecipeList rl= new RecipeList();

    }

    @Test
    void editRecipeTest(){
        Recipe r= new Recipe();
        r.setRecipeName("ar");
        r.setRecipeTotal("add more apples");
        String s=r.getRecipeTotal();
        r.setRecipeTotal("no more apples");
        assertNotEquals(s, r.getRecipeTotal());
        
    }

    @Test
    void deleteRecipeTest(){
        Recipe r= new Recipe();
        RecipeList rl=new RecipeList();
        Recipe r2=new Recipe();
        rl.getChildren().add(r2);

        String s= "save";
        if(s=="delete"){
            rl.deleteRecipe(r);
        }
        assertEquals(rl.getChildren().contains(r), false);
        //Filewriter r= n
        //RecipeList rl= new RecipeList();
    }

    @Test
    void saveRecipeNewTest(){
        Recipe r= new Recipe();
        RecipeList rl=new RecipeList();

        String s= "save";
        if(s=="save"){
            rl.getChildren().add(r);
        }
        assertEquals(rl.getChildren().contains(r), true);
        //Filewriter r= n
        //RecipeList rl= new RecipeList();
    }


    @Test
    void chooseMealTypeTest(){
        String type= "lunch";
        Recipe recipe = new Recipe();
        //recipe.setRecipeName("type");
        if(type=="breakfast"){
            recipe.setRecipeName("pakackes");
            recipe.setRecipeTotal("Ingredients: flour, butter, yeast, eggs, maple syrup");
        }
        if(type=="lunch"){
            recipe.setRecipeName("taco");
            recipe.setRecipeTotal("Ingredients: tortilla, beef, tomato, cheese, salsa");
        }
        if(type=="dinner"){
            recipe.setRecipeName("steak dinner");
            recipe.setRecipeTotal("Ingredients: steak, salt, pepper, potatos, carrots, wine, butter, thyme");
        }

        assertEquals(type, "lunch");
        assertEquals(recipe.getRecipeTotal(), "Ingredients: tortilla, beef, tomato, cheese, salsa");
        assertEquals(type, recipe);
    }

    @Test
    void detailViewTest(){
       Recipe recipe= new Recipe();
       recipe.setRecipeName("pakackes");
        recipe.setRecipeTotal("Ingredients: flour, butter, yeast, eggs, maple syrup");

        Button press=new Button();
        int s=1;
        press.setOnAction(e ->{
            DetailedView d= new DetailedView(r.getRecipeTotal(), recipe);
            s++;
        }
        );
        
        assertEquals(2, d);
    }


    @Test
    void invalidChatGptinputTest(){
        String[] a={"apple", "banana"};

        ChatGPT ch= new ChatGPT();
        ch.main(a);
        String ret= ch.returnPrompt();
        assertNotEquals(ret, a);
    }

    @Test
    void WhisperTest(){
        //String[] s= {"apples", "banana", "watermellon"};
        //Whisper a= new Whisper();
        String Whisperinput="apple";
        

    }
}
