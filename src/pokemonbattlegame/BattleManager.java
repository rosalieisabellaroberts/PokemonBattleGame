/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */

import java.util.Scanner;
import java.sql.*;

public class BattleManager 
{
    private PokemonBattleGame game;

    public BattleManager(PokemonBattleGame game) 
    {
        this.game = game;
    }

    public void startBattle(Connection connection) throws InterruptedException, SQLException
    {
        Scanner scanner = new Scanner(System.in);
        boolean keepBattling = true;
        
        System.out.println("Professor Oaks: It is now time for you and " +
                    game.getTrainer().getStarterPokemon().getName() +
                    " to journey into the magical world of Pokemon!\n");

        Thread.sleep(2000);
        System.out.println("When you are ready, press enter to start...");
        scanner.nextLine();
        
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
            System.out.println("Your poke-adventure isn't over yet! Battle again? (yes/no)");

            String selection = scanner.nextLine().trim().toLowerCase();
            if (selection.equals("y") || selection.equals("yes"))
            {
                System.out.println("\nPrepare for your next battle!");
            }
            else
            {
                System.out.println("\nThanks for playing! Safe travels trainer...");
                keepBattling = false;
            }
        }       
    }
}

