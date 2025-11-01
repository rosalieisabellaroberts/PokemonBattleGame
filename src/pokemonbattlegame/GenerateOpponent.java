/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.util.List;
import java.util.Random;
import java.sql.*;

/**
 *
 * @author dilro
 */

public class GenerateOpponent 
{
    private Trainer opponent;
    
    public void generateOpponent(Trainer trainer, Connection connection) throws InterruptedException
    {
        // Call getAllTrainers from Database Manager to return a list of trainer objects
        List<Trainer> trainers = DatabaseManager.getAllTrainers(connection);
                
        // If array is empty, return error message and set opponent to null
        if (trainers == null || trainers.isEmpty())
        {
            System.out.println("Professor Oak: No other trainers are available for battle!");
            opponent = null;
            return;
        }
      
        // Remove current user from list of possible Trainers to battle
        trainers.removeIf(t -> t.getUsername().equals(trainer.getUsername()));
           
        // If array is empty, return error message and set opponent to null
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
    
    public Trainer getOpponent() 
    {
        return opponent;
    }
}
