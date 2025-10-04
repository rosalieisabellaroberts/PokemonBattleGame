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
    
    public static void main(String[] args) throws InterruptedException, SQLException
    {
        // Create a new game
        PokemonBattleGame game = new PokemonBattleGame();
        
        // Define connection 
        Connection connection = null;
        
        try 
        {     
            // Establish connection 
            connection = DatabaseManager.getConnection();
            
            // Initalise the PokemonBattleGame database
            DatabaseManager.createTables(connection);
            DatabaseManager.populateTypesTable(connection);
            DatabaseManager.populatePokemonTable(DatabaseManager.getPokemonList(), connection);
            DatabaseManager.populateWeaknesses(connection);
            DatabaseManager.populateStrengths(connection);
            DatabaseManager.populateMoves(connection);
            DatabaseManager.populateTrainers(connection);
            
            // Load types with effectiveness 
            ArrayList<Type> types = DatabaseManager.loadTypesWithEffectiveness(connection);
            
            // Store types and effectiveness 
            TypeStorage.setTypes(types);
            
            // Show the professor Oaks intro JFrame 
            GameSetup oaksIntro = new GameSetup();
            oaksIntro.setVisible(true);
        
            // Load trainer 
            SetupGame setup = new SetupGame();
            Trainer trainer = setup.run(connection);
            game.setTrainer(trainer);
            game.setPokemons(setup.getPokemons());

            // If trainer creation fails, stop program
            if (trainer == null) 
            {
                System.out.println("Professor Oak: Looks like you are not fit to be a trainer just yet ...");
                return;
            }

             // Load the effectiveness of all the types 
            game.setTypes(DatabaseManager.loadTypesWithEffectiveness(connection));
            
            SaveManager.saveTrainer(trainer, connection);

            // Prompt user for start 
            BattleManager battleManager = new BattleManager(game);

            // Randomly generate an opponent
            // GenerateOpponent generatedOpponent = new GenerateOpponent();
            // generatedOpponent.generateOpponent(game.getTrainer(), connection);
            // game.setOpponent(generatedOpponent.getOpponent());
            
            // Randomly generate pokemon teams
            //GeneratePokemonTeams generatedPokemonTeams = new GeneratePokemonTeams();
            //generatedPokemonTeams.setPokemons(setup.getPokemons());
            //generatedPokemonTeams.generatePokemonTeams(game.getTrainer(), game.getOpponent(), connection);

            battleManager.startBattle(connection);  
       
        } catch (Exception e)
        {
            e.printStackTrace();
        } 
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
