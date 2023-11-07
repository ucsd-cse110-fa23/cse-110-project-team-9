package test;
//package client;
//package client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PantryPalTest {
    @BeforeEach
    void setUp(){
        String input="pasta, tomato, and sugar";
        String label= "tomato pasta";
        String output= "Boiled Water with pasta, blend tomatos with salt to make tomato sauce, and use sugar for making tomato juice";
        Recipe r= new Recipe();
        RecipeList rl= new RecipeList();
        String s="hi";
    }
  
    //rl.add(r);


    @Test
    void newRecipeTestBDD(){//test new BDD adds label 
        r.setRecipe(label);
        assertEquals(r, input);
    }
}
