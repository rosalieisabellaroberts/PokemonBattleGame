/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


/**
 *
 * @author dilro
 */
public class GenerateOpponent 
{
    private Trainer opponent;
    
    public void generateOpponent(Trainer trainer) throws InterruptedException
    {
        // Open 'Trainers.json'
        File file = new File("Trainers.json");
        
        // If file does not exist
        if (!file.exists())
        {
            System.out.println("Professor Oak: Whoops! All the trainers left before you got here!");
            return;
        }
        
        // Wraps the file reader in a buffered reader for reading the file in chunks
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            // Create a GSON object to perform conversions from JSON to list of trainer objects and vice versa
            Gson gson = new Gson();
      
            // Extract type of list, for converting JSON into a list containing trainer objects
            java.lang.reflect.Type listType = new TypeToken<List<Trainer>>() {}.getType();
            
            // Read JSON from 'Trainers.json' and convert to a list of trainer objects 
            List<Trainer> trainers = gson.fromJson(reader, listType);
                
            // Remove current user from list of possible Trainers to battle
            trainers.removeIf(t -> t.getUsername().equals(trainer.getUsername()));
            
            if (trainers.isEmpty())
            {
                System.out.println("Professor Oak: No other trainers are available for battle!");
                opponent = null;
                return;
            }
      
            // Create Random object for selecting a random Trainer    
            Random random = new Random();
            opponent = trainers.get(random.nextInt(trainers.size())); 
        }
        // If file 'Trainers.json' is not found, return error message
        catch (FileNotFoundException e)
        {
            System.out.println("Professor Oak: Oooops..." + e.getMessage());
        }
        // If input/output operations fail, return error message
        catch (IOException e)
        {
            System.out.println("Professor Oak: Oooops..." + e.getMessage());
        }
    }
    
    public Trainer getOpponent() 
    {
        return opponent;
    }
}
