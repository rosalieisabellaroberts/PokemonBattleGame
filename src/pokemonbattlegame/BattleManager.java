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
import javax.swing.SwingUtilities;


public class BattleManager 
{
    private PokemonBattleGame game;
    private SetupGUI gui;

    public BattleManager(PokemonBattleGame game) 
    {
        this.game = game;
    }

    public void startBattle(Connection connection) throws InterruptedException, SQLException
    {
        boolean keepBattling = true;
        
        printMessage("Professor Oaks: It is now time for you and " +
                    game.getTrainer().getStarterPokemon().getName() +
                    " to journey into the magical world of Pokemon!\n");

        Thread.sleep(2000);
        printMessage("When you are ready, press enter to start...");
        
        // Wait for user to press enter in the GUI
        gui.waitForInput();
        
        while (keepBattling)
        {   
            // Generate a new opponent for each battle
            GenerateOpponent generatedOpponent = new GenerateOpponent();
            generatedOpponent.generateOpponent(game.getTrainer(), connection);
            game.setOpponent(generatedOpponent.getOpponent());
            
            // Generate new Pokemon teams for each battle 
            GeneratePokemonTeams generatedPokemonTeams = new GeneratePokemonTeams();
            generatedPokemonTeams.setPokemons(DatabaseManager.getPokemonList().toArray(new Pokemon[0]));
            generatedPokemonTeams.generatePokemonTeams(game.getTrainer(), game.getOpponent(), connection);

            game.getOpponent().speak();
            Thread.sleep(2000);

            // Display game details
            DisplayGameDetails gameDetails = new DisplayGameDetails();
            gameDetails.displayGameDetails(game.getTrainer(), game.getOpponent());

            // Start actual battle
            Battle battle = new Battle(game.getTrainer(), game.getOpponent(), game.getTrainer().getStarterPokemon(), game.getOpponent().getStarterPokemon());
            battle.runBattle(connection);

            // Prompt user to battle again 
            printMessage("Your poke-adventure isn't over yet! Battle again? (yes/no)");

            String selection = gui.waitForInput().trim().toLowerCase();
            if (selection.equals("y") || selection.equals("yes"))
            {
                printMessage("\nPrepare for your next battle!");
            }
            else
            {
                printMessage("\nThanks for playing! Safe travels trainer...");
                keepBattling = false;
            }
        }       
    }
    
    // Print messages to GUI 
     private void printMessage(String message) 
    {
        // If GUI is connected, display message to GUI
        if (gui != null) 
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    gui.appendMessage(message);
                }
            });
        }
    }
     
    // Setter method for GUI 
    public void setGUI(SetupGUI gui) 
    {
        this.gui = gui;
    }
}

