/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pokemonbattlegame;

import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author dilro
 */
public class PokemonBattleGame 
{   
    private Trainer trainer;
    private Trainer opponent; 
    private Pokemon trainerCurrentPokemon; 
    private Pokemon opponentCurrentPokemon;
    private Pokemon[] pokemons;
    private ArrayList<Type> types;
    
    public static void main(String[] args) throws InterruptedException
    {
        // Create a new game
        PokemonBattleGame game = new PokemonBattleGame();
        
        // Establish a connection
        try (Connection connection = DatabaseManager.getConnection())
        {
            // Initalise the PokemonBattleGame database
            DatabaseManager.createTables(connection);
            DatabaseManager.populateTypesTable(connection);
            DatabaseManager.populatePokemonTable(DatabaseManager.getPokemonList(), connection);
            DatabaseManager.populateWeaknesses(connection);
            DatabaseManager.populateStrengths(connection);
            DatabaseManager.populateMoves(connection);
            DatabaseManager.populateTrainers(connection);
        
            // Load the effectiveness of all the types 
            game.setTypes(DatabaseManager.loadTypesWithEffectiveness(connection));
        } catch (Exception e)
        {
            e.printStackTrace();
        } 
        
        // Show the professor Oaks intro JFrame 
        GameSetup oaksIntro = new GameSetup();
        oaksIntro.setVisible(true);
        
        // Setup the game
        SetupGame setup = new SetupGame();
        Trainer trainer = setup.run();

        // If trainer creation fails, stop program
        if (trainer == null) 
        {
            System.out.println("Professor Oak: Looks like you are not fit to be a trainer just yet ...");
            return;
        }

        game.setTrainer(trainer);
        SaveManager.saveTrainer(trainer);
        game.setPokemons(setup.getPokemons());
        
        // Prompt user for start 
        BattleManager battleManager = new BattleManager(game);
        
        // Randomly generate an opponent
        try 
        {
            GenerateOpponent generatedOpponent = new GenerateOpponent();
            generatedOpponent.generateOpponent(game.getTrainer());
            game.setOpponent(generatedOpponent.getOpponent());
        } catch (InterruptedException e)
        {
            System.out.println("Professor Oak: Looks like there are no trainers to battle! Try again later..." + e.getMessage());
        }
        
        // Randomly generate pokemon teams
        GeneratePokemonTeams generatedPokemonTeams = new GeneratePokemonTeams();
        generatedPokemonTeams.setPokemons(setup.getPokemons());
        generatedPokemonTeams.generatePokemonTeams(game.getTrainer(), game.getOpponent());
       
        battleManager.startBattle();  
    }

    public void setTrainer(Trainer trainer) 
    {
        this.trainer = trainer;
    }

    public Trainer getTrainer() 
    {
        return trainer;
    }

    public Trainer getOpponent() 
    {
        return opponent;
    }

    public void setOpponent(Trainer opponent) 
    {
        this.opponent = opponent;
    }
    
    public Pokemon[] getPokemons() 
    {
        return pokemons;
    }

    public void setPokemons(Pokemon[] pokemons) 
    {
        this.pokemons = pokemons;
    }
    
    private ArrayList<Type> getTypes()
    {
        return types;
    }
    
    public void setTypes(ArrayList<Type> types) 
    {
        this.types = types;
    }
}
