/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */

import com.google.gson.Gson;
import java.io.*;
import java.util.*;

public class SaveManager 
{
    public static void saveTrainer(Trainer trainer) 
    {
        try 
        {
            // Check if trainer username exists
            if (trainer.getUsername() == null || trainer.getUsername().isEmpty()) 
            {
                System.out.println("Professor Oak: Your username is missing! I won't save your data then...");
                return;
            }
            
            Gson gson = new Gson();
            File file = new File("Trainers.json");
            
            if (!file.exists())
            {
                file.createNewFile();
                
                try (FileWriter writer = new FileWriter(file))
                {
                    // Create an empty JSON array
                    writer.write("[]");
                }
            }  

            // Have Gson convert from JSON to correct type (using TypeToken helper class to deserialise into a List of Trainer objects)
            java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<java.util.List<Trainer>>() {}.getType();
            java.util.List<Trainer> trainersList = new ArrayList<>();

            try (FileReader reader = new FileReader(file)) 
            {
                java.util.List<Trainer> fromFile = gson.fromJson(reader, listType);
                
                // If file is corrupt or empty, return message
                if (fromFile == null) 
                {
                    System.out.println("Professor Oak: My pidgey must have corrupted Trainers.json! Or maybe it's just empty? We will have to start over...");
                } else 
                {
                    trainersList = fromFile;
                }
            }
            
            boolean exists = false;

            // Check if trainer already exists
            for (int i = 0; i < trainersList.size(); i++)
            {
                if (trainersList.get(i).getUsername().equals(trainer.getUsername()))
                {
                    // Update existing trainer
                    trainersList.set(i, trainer);
                    exists = true;
                    break;
                }
            }
            
            // Append new trainer if they do not already exist
            if (!exists)
            {
                trainersList.add(trainer);
            }
            
            try (FileWriter writer = new FileWriter(file, false)) 
            {
                gson.toJson(trainersList, writer);
                writer.flush();
            }
        } catch (Exception e) 
        {
            System.out.println("Professor Oak: I couldn't update save file: " + e.getMessage());
        }
    }
}

