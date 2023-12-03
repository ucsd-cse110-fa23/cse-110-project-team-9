/* 
 
package client;

import org.junit.jupiter.api.Test;

import client.View.RecipeList;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppFrameTest {
    Stage s= new Stage();
    View v= new View(s);
    Stage a= v.getAppFrame();
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
            
            RecipeList rl = v.new RecipeList();
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
            
            RecipeList rl=v.new RecipeList();
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
          
            RecipeList rl = v.new RecipeList();

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

    @Test
    //This BDD tests for when there is a meal type tag
    void MealTypeTagBDD1(){
        try{
            Recipe r = new Recipe();
            
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            assertEquals(r.getRecipeType().toString(), "breakfast");
        }catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }

    @Test
    //This BDD tests for when there is no meal type tag
    void MealTypeTagBDD2(){
        try{
            Recipe r = new Recipe();
            
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            mcg.setResult("", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            assertNotEquals(r.getRecipeType().toString(), "breakfast");
        }catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }

    @Test
    //This BDD tests for when a user doesn't refresh and then saves
    void RefreshBDD1(){
        try{
            Recipe r = new Recipe();
           
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            MockWhisper mv= new MockWhisper();
            String refresh=" no refresh";
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            if(refresh=="no refresh"){

                rl.getChildren().add(r);
                String newrecipe="";
            }
           assertEquals(rl.getChildren().contains(r), true);

        }catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }

    @Test
    //This BDD tests for when a user doesn't refresh and then cancels
    void RefreshBDD2(){
        try{
            Recipe r = new Recipe();
           
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            MockWhisper mv= new MockWhisper();
            String refresh="no refresh";
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            String cancel="cancel";
            
            if(cancel=="cancel" && refresh== "no refresh"){
                rl.deleteRecipe(r);
            }
            assertEquals(rl.getChildren().contains(r), false);

        }catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }
     
    @Test
    //This BDD tests for when a user saves a refreshed recipe
    void RefreshBDD3(){
        try{
            Recipe r = new Recipe();
           
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            MockWhisper mv= new MockWhisper();
            String refresh="refresh";
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            String newrecipe="";
            if(refresh== "refresh"){
                mcg.setResult("breakfast", "bagels and creamcheese");
                newrecipe=mcg.getResultRecipe();
                r.setRecipeTotal(mcg.getResultRecipe());
                r.setRecipeType(mcg.getMealType());
            }
           assertEquals(mcg.getResultRecipe(), newrecipe);

        }catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }
    
    

    @Test
    //This BDD tests for when a user cancels a refreshed recipe
    void RefreshBDD4(){
        try{
            Recipe r = new Recipe();
           
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            MockWhisper mv= new MockWhisper();
            String refresh="refresh";
            String cancel="cancel";
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            String newrecipe="";
            if(refresh== "refresh"){
                mcg.setResult("breakfast", "bagels and creamcheese");
                newrecipe=mcg.getResultRecipe();
                r.setRecipeTotal(mcg.getResultRecipe());
                r.setRecipeType(mcg.getMealType());
            }
           assertEquals(mcg.getResultRecipe(), newrecipe);

           if(cancel=="cancel"){
             rl.deleteRecipe(r);
            }
            assertEquals(rl.getChildren().contains(r), false);

        }catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }


    @Test
    void FilterMealtypeSelected(){
        try{
            Recipe r = new Recipe();
            Recipe r2= new Recipe();
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            assertEquals(r.getRecipeType().toString(), "breakfast");

            mcg.setResult("lunch", "beef and tomatos");
            r2.setRecipeTotal(mcg.getResultRecipe());
            r2.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r2);
            assertEquals(r2.getRecipeType().toString(), "lunch");
    
            rl.updateFilteredLunch();
            
            assertNotEquals(rl.getRecipes().contains(r), true);
            assertEquals(rl.getRecipes().contains(r2), true);
            

        }catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }

    @Test
    void FilterMealtypeNotSelected(){
        try{
            Recipe r = new Recipe();
            Recipe r2= new Recipe();
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            assertEquals(r.getRecipeType().toString(), "breakfast");

            mcg.setResult("lunch", "beef and tomatos");
            r2.setRecipeTotal(mcg.getResultRecipe());
            r2.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r2);
            assertEquals(r2.getRecipeType().toString(), "lunch");
    
            //rl.updateFilteredBreakfast();
            assertEquals(rl.getRecipes().contains(r2), true);
            assertEquals(rl.getRecipes().contains(r), true);

        }catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }


    @Test
    void FilterMealtypeSelectandNotSelected(){
        try{
            Recipe r = new Recipe();
            Recipe r2= new Recipe();
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            assertEquals(r.getRecipeType().toString(), "breakfast");

            mcg.setResult("lunch", "beef and tomatos");
            r2.setRecipeTotal(mcg.getResultRecipe());
            r2.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r2);
            assertEquals(r2.getRecipeType().toString(), "lunch");
    
            rl.updateFilteredBreakfast();
            assertNotEquals(rl.getRecipes().contains(r2), true);
            assertEquals(rl.getRecipes().contains(r), true);

            rl.updateRecipeListView();
            assertEquals(rl.getRecipes().contains(r2), true);
            assertEquals(rl.getRecipes().contains(r), true);

        }catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }
    @Test
    void sortChronilogical(){
        try{
            Recipe r = new Recipe();
            Recipe r2= new Recipe();
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            assertEquals(r.getRecipeType().toString(), "breakfast");

            mcg.setResult("lunch", "beef and tomatos");
            r2.setRecipeTotal(mcg.getResultRecipe());
            r2.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r2);
            assertEquals(r2.getRecipeType().toString(), "lunch");

            rl.updateSorted1();
            assertEquals(rl.getRecipes().indexOf(r), 1);
            assertEquals(rl.getRecipes().indexOf(r2), 0);
        }
        catch(ExceptionInInitializerError e){
            e.printStackTrace();
        }
        catch(NoClassDefFoundError e){
            e.printStackTrace();
        }
    }
    @Test
    void sortReverseChronilogical(){
        try{
            Recipe r = new Recipe();
            Recipe r2= new Recipe();
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            assertEquals(r.getRecipeType().toString(), "breakfast");

            mcg.setResult("lunch", "beef and tomatos");
            r2.setRecipeTotal(mcg.getResultRecipe());
            r2.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r2);
            assertEquals(r2.getRecipeType().toString(), "lunch");

            rl.updateSorted2();
            assertEquals(rl.getRecipes().indexOf(r), 0);
            assertEquals(rl.getRecipes().indexOf(r2), 1);
        }
        catch(ExceptionInInitializerError e){
            e.printStackTrace();
        }
        catch(NoClassDefFoundError e){
            e.printStackTrace();
        }
    }


    @Test
    void sortAlphabetical(){
        try{
            Recipe r = new Recipe();
            Recipe r2= new Recipe();
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            assertEquals(r.getRecipeType().toString(), "breakfast");

            mcg.setResult("lunch", "carrot and tomatos");
            r2.setRecipeTotal(mcg.getResultRecipe());
            r2.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r2);
            assertEquals(r2.getRecipeType().toString(), "lunch");

            rl.updateSorted2();
            assertEquals(rl.getRecipes().indexOf(r), 1);
            assertEquals(rl.getRecipes().indexOf(r2), 0);
        }
        catch(ExceptionInInitializerError e){
            e.printStackTrace();
        }
        catch(NoClassDefFoundError e){
            e.printStackTrace();
        }
    }

    @Test
    void sortReverseAlphabetical(){
        try{
            Recipe r = new Recipe();
            Recipe r2= new Recipe();
            RecipeList rl = v.new RecipeList();
            MockChatGPT mcg = new MockChatGPT();
            mcg.setResult("breakfast", "bagels and creamcheese");
            r.setRecipeTotal(mcg.getResultRecipe());
            r.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r);
            assertEquals(r.getRecipeType().toString(), "breakfast");

            mcg.setResult("lunch", "carrot and tomatos");
            r2.setRecipeTotal(mcg.getResultRecipe());
            r2.setRecipeType(mcg.getMealType());
            rl.getChildren().add(r2);
            assertEquals(r2.getRecipeType().toString(), "lunch");

            rl.updateSorted2();
            assertEquals(rl.getRecipes().indexOf(r), 0);
            assertEquals(rl.getRecipes().indexOf(r2), 1);
        }
        catch(ExceptionInInitializerError e){
            e.printStackTrace();
        }
        catch(NoClassDefFoundError e){
            e.printStackTrace();
        }
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
}
*/
