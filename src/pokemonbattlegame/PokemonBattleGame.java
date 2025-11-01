/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pokemonbattlegame;

import java.util.ArrayList;
import java.sql.*;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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
         final Connection connection;
        
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
        
            // Show Setup GUI 
            // Create a new game setup GUI instance
            SetupGUI gui = new SetupGUI();
            // Create a new game setup instance 
            SetupGame setup = new SetupGame();
            // Connect GUI to the game setup 
            setup.setGUI(gui);

            // Run trainer setup
            Trainer trainer = setup.run(connection);

            // If trainer creation fails, stop program
            if (trainer == null) 
            {
                System.out.println("Professor Oak: Looks like you are not fit to be a trainer just yet ...");
                return;
            }
            
            // Save the trainer data into the database 
            SaveManager.saveTrainer(trainer, connection);
            
            // Set trainer, pokemon, types and effectiveness data in game
            game.setTrainer(trainer);
            game.setPokemons(setup.getPokemons());
            game.setTypes(DatabaseManager.loadTypesWithEffectiveness(connection));
            
            // Get starter pokemon 
            Pokemon starterPokemon = game.getTrainer().getStarterPokemon();
            
            // Show battleStartGUI on event dispatch thread
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    // Create BattleStartGUI panel 
                    BattleStartGUI battleStartGUI = new BattleStartGUI(starterPokemon);
                    
                    // Add BattleStartGUI to the SetupGUI card layout 
                    gui.addCard(battleStartGUI, "BattleStart");
                    
                    // Create thread to pause before switching cards (screens)
                    Thread pauseThread = new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                // Pause for 2 seconds
                                Thread.sleep(2000);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            
                            // Switch cards back on the event dispatch thread
                            SwingUtilities.invokeLater(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    gui.showScreen("BattleStart");
                                } 
                            });                         
                        }
                    });
                    // Start the pauseThread
                    pauseThread.start();
                }
            });
            
            // Run battle logic on background thread
            Thread battleThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        BattleManager battleManager = new BattleManager(game);
                        
                        // Attach GUI to the BattleManager
                        battleManager.setGUI(gui);
                        
                        battleManager.startBattle(connection);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    } catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            
            battleThread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Getter and setter methods 
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
