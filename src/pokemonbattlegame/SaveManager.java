/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */

import java.sql.*;

public class SaveManager 
{
    public static void saveTrainer(Trainer trainer) 
    {
        // Check if trainer username exists
        if (trainer.getUsername() == null || trainer.getUsername().isEmpty()) 
        {
            System.out.println("Professor Oak: Your username is missing! I won't save your data then...");
            return;
        }
        
        try (Connection connection = DatabaseManager.getConnection())
        {
            // Check if trainer already exists in the database 
            String checkSql = "SELECT username FROM Trainers WHERE username = ?";
            
            try (PreparedStatement checkPreparedStatement = connection.prepareStatement(checkSql))
            {
                checkPreparedStatement.setString(1, trainer.getUsername());
                ResultSet resultSet = checkPreparedStatement.executeQuery();
                
                if (resultSet.next())
                {
                    // Trainer exists and must be updated in the trainer table 
                    String updateSql = "UPDATE Trainers SET name = ?, score = ?, level = ?, starterPokemon = ?, challengeMessage = ? WHERE username = ?";
                    
                    try (PreparedStatement updatePreparedStatement = connection.prepareStatement(updateSql))
                    {
                        updatePreparedStatement.setString(1, trainer.getName());
                        updatePreparedStatement.setInt(2, trainer.getScore());
                        updatePreparedStatement.setInt(3, trainer.getLevel());
                        updatePreparedStatement.setString(4, trainer.getStarterPokemon().getName());
                        updatePreparedStatement.setString(5, trainer.getChallengeMessage());
                        updatePreparedStatement.setString(6, trainer.getUsername());
                        
                        updatePreparedStatement.executeUpdate();
                    } catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                } else
                {
                    // Trainer does not exist and must be inserted into the trainer table
                    String insertSql = "INSERT INTO Trainers (username, name, score, level, starterPokemon, challengeMessage) VALUES (?, ?, ?, ?, ?, ?)";
                
                    try (PreparedStatement insertPreparedStatement = connection.prepareStatement(insertSql))
                    {
                        insertPreparedStatement.setString(1, trainer.getUsername());
                        insertPreparedStatement.setString(2, trainer.getName());
                        insertPreparedStatement.setInt(3, trainer.getScore());
                        insertPreparedStatement.setInt(4, trainer.getLevel());
                        insertPreparedStatement.setString(5, trainer.getStarterPokemon().getName());
                        insertPreparedStatement.setString(6, trainer.getChallengeMessage());
                        
                        insertPreparedStatement.executeUpdate();
                    } catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}

