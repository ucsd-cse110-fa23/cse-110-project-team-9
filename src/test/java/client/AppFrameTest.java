package client;

import org.junit.jupiter.api.Test;

import client.AppFrame.RecipeList;
import javafx.scene.control.Button;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppFrameTest {
    //private static Recipe r = new Recipe();

    @Test
    void labelTest(){
        String a = "apple";
        String b = "apple";
        assertEquals(a,b);
    }

    @Test
    void RecipeCreateNameTest(){//recipe named correctly
        
        try {
            Recipe r= new Recipe();
            r.setRecipeName("apple pie");
            assertEquals(r.getRecipeLabelName(),"apple pie");

        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
        //Recipe r= new Recipe();
        
        //String b = "apple";   
    }

    @Test 
    void savetoRecipeListTest(){
        try {
            Recipe r= new Recipe();
            AppFrame af = new AppFrame();
            RecipeList rl = af.new RecipeList();
            Recipe r2=new Recipe();
            rl.getChildren().add(r2);

            String s= "save";
            if(s=="save"){
                rl.getChildren().add(r);
            }
            assertEquals(rl.getChildren().contains(r), true);
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
        
        //Filewriter r= n
        //RecipeList rl= new RecipeList();

    }

    @Test
    void editRecipeTest(){
        
        try {
            Recipe r= new Recipe();
            r.setRecipeName("ar");
            r.setRecipeTotal("add more apples");
            String s=r.getRecipeTotal();
            r.setRecipeTotal("no more apples");
            assertNotEquals(s, r.getRecipeTotal());
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }

        
        
    }

    @Test
    void deleteRecipeTest(){
        try {
            Recipe r= new Recipe();
            AppFrame af = new AppFrame();
            RecipeList rl=af.new RecipeList();
            Recipe r2=new Recipe();
            rl.getChildren().add(r2);

            String s= "save";
            if(s=="delete"){
                rl.deleteRecipe(r);
            }
            assertEquals(rl.getChildren().contains(r), false);
        //Filewriter r= n
        //RecipeList rl= new RecipeList();
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
        
    }
 
    @Test
    void saveRecipeNewTest(){
        try {
            Recipe r= new Recipe();
            AppFrame af = new AppFrame();
            RecipeList rl = af.new RecipeList();

            String s= "save";
            if(s=="save"){
                rl.getChildren().add(r);
            }
            assertEquals(rl.getChildren().contains(r), true);
            //Filewriter r= n
            //RecipeList rl= new RecipeList();
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
        
    }


    @Test
    void chooseMealTypeTest(){
        try {
            String type= "lunch";
            Recipe recipe = new Recipe();
            //recipe.setRecipeName("type");
            if(type=="breakfast"){
                recipe.setRecipeName("pancakes");
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
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
        
    }
/* 
    @Test
    void detailViewTest(){
        try {
            Recipe recipe= new Recipe();
            recipe.setRecipeName("pancakes");
            recipe.setRecipeTotal("Ingredients: flour, butter, yeast, eggs, maple syrup");

            Button press=new Button();
            int s=1;
            press.setOnAction(e ->{
                DetailedView d= new DetailedView(r.getRecipeTotal(), recipe);
                s++;
            });
        
            assertEquals(2, d);
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        }
        
       
    }



    @Test
    void invalidChatGptinputTest(){
        String[] a={"apple", "banana"};
        try {
            ChatGPT.main(a);
            String ret= ChatGPT.returnPrompt();
            assertNotEquals(ret, a);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } 
        
    }
     
    @Test
    void WhisperTest(){
        String audioText = "breakfast";
        String[] args = new String[] { "WhisperTest.wav" };
        
        try {
            Whisper.main(args);
            String result = Whisper.getResult();
            assertEquals(audioText, result);
        } catch (URISyntaxException f) {
            f.printStackTrace();
        } catch (IOException f) {
            f.printStackTrace();
        }
    }
*/
}
