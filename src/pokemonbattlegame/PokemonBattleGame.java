/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pokemonbattlegame;

import java.util.ArrayList;
import java.sql.*;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.lang.reflect.InvocationTargetException;

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
            
            // Create BattleStartGUI (passing temporary null for BattleManager)
            BattleStartGUI battleStartGUI = new BattleStartGUI(trainer, connection, gui, null);
            
            // Get starter pokemon 
            Pokemon starterPokemon = game.getTrainer().getStarterPokemon();
            
            // Show battleStartGUI on event dispatch thread
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    // Create BattleManager
                    BattleManager battleManager = new BattleManager(game, battleStartGUI, gui);
                    
                    // Inject BattleManager into BattleStartGUI
                    battleStartGUI.setBattleManager(battleManager);
                    
                    // Replace placeholder in BattleManager
                    battleManager.setBattleStartGUI(battleStartGUI);
                    
                    // Link SetuPGI so BattleManager can update the battle lgo
                    battleManager.setGUI(gui);

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
                                    
                                    // Use Swing timer to delay starting the battle logic untol BattleStart screen is visible
                                    int delay = 2000;
                                    
                                    javax.swing.Timer timer = new javax.swing.Timer(delay, new java.awt.event.ActionListener()
                                    {
                                        @Override
                                        public void actionPerformed (java.awt.event.ActionEvent e)
                                        {
                                            // Stop timer after it is used once
                                            ((javax.swing.Timer) e.getSource()).stop();
                                            
                                            // Once BattleStart screen is visible, start battle logic in BattleManager
                                            Thread battleThread = new Thread(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    try
                                                    {                           
                                                        // Create new battleManager instance for the game
                                                        BattleManager battleManager = new BattleManager(game, battleStartGUI, gui);
                                                        // Link SetupGUI so battleManager can update battle log
                                                        battleManager.setGUI(gui);
                                                        battleManager.startBattle(connection);
                                                    }
                                                    catch (InterruptedException e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                    catch (SQLException e)
                                                    {
                                                        e.printStackTrace();
                                                    } 
                                                    catch (InvocationTargetException e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }         
                                            });
                                            // Start the battleThread
                                            battleThread.start();
                                        }
                                    });
                                    
                                    // Start the swing timer with no repeats 
                                    timer.setRepeats(false);
                                    timer.start();
                                } 
                            });                         
                        }
                    });
                    // Start the pauseThread
                    pauseThread.start();
                }
            });
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
