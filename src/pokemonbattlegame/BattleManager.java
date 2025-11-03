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
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.lang.reflect.InvocationTargetException;

public class BattleManager 
{
    private PokemonBattleGame game;
    private SetupGUI setupGUI;
    private BattleStartGUI battleStartGUI;
    private BattleGUI battleGUI;
    private Battle battle;
    private BattleResult lastBattleResult;

    public BattleManager(PokemonBattleGame game, BattleStartGUI battleStartGUI, SetupGUI setupGUI)
    {
        this.game = game;
        this.battleStartGUI = battleStartGUI;
        this.setupGUI = setupGUI;
    }

    public void startBattle(Connection connection) throws InterruptedException, SQLException, InvocationTargetException
    { 
        // Show introductory messages in BattleStartGUI 
        battleStartGUI.appendMessage("Professor Oaks: It is now time for you and " +
                    game.getTrainer().getStarterPokemon().getName() +
                    " to journey into the magical world of Pokemon!\n");

        Thread.sleep(2000);
        battleStartGUI.appendMessage("When you are ready, press any key to start...");
        
        // Generate a new opponent for each battle
        GenerateOpponent generatedOpponent = new GenerateOpponent();
        generatedOpponent.generateOpponent(game.getTrainer(), connection);
        game.setOpponent(generatedOpponent.getOpponent());

        // Generate new Pokemon teams for each battle 
        GeneratePokemonTeams generatedPokemonTeams = new GeneratePokemonTeams(DatabaseManager.getPokemonList());
        generatedPokemonTeams.generatePokemonTeams(game.getTrainer(), game.getOpponent(), connection);

        // Create a trainer turn object once 
        TrainerTurn trainerTurn = new TrainerTurn(game.getTrainer(), game.getOpponent(), game.getTrainer().getStarterPokemon(), game.getOpponent().getStarterPokemon(), new Random(), null, null);
        
        // Create an opponent turn object once 
        OpponentTurn opponentTurn = new OpponentTurn(game.getTrainer(), game.getOpponent(), game.getTrainer().getStarterPokemon(), game.getOpponent().getStarterPokemon(), new Random(), null);
        
        // Create a new battle object once 
        battle = new Battle(game.getTrainer(), game.getOpponent(), game.getTrainer().getStarterPokemon(), game.getOpponent().getStarterPokemon(), setupGUI, trainerTurn);

        // Create and show the Battle GUI 
        setupGUI.createAndShowBattleGUI(game.getTrainer(), game.getOpponent(), battle, connection, trainerTurn);

        // Get the BattleGUI after it is created
        battleGUI = setupGUI.getBattleGUI();

        // Link Battle, TrainerTurn and OpponentTurn objects
        trainerTurn.setBattleGUI(battleGUI);
        trainerTurn.setBattle(battle);
        opponentTurn.setBattleGUI(battleGUI);
        battle.setBattleGUI(battleGUI);
        battle.setTrainerTurn(trainerTurn);
        battle.setConnection(connection);

        // Attach button listeners in BattleManager for trainer turns
        battleGUI.setButtonListeners(createFightListener(trainerTurn), createSwitchListener(trainerTurn), createRunListener(trainerTurn));
            
        // Start single-threaded battle loop 
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // While battle is not finished
                    while(!battle.isFinished())
                    {
                        // Refresh trainer pokemon reference 
                        trainerTurn.setTrainerCurrentPokemon(game.getTrainer().getStarterPokemon());
                        
                        // Execute trainer turn
                        BattleResult trainerResult = trainerTurn.takeTurn();

                        // Update Battle object with the new current Pokemon
                        battle.setTrainerCurrentPokemon(trainerResult.getTrainerPokemon());
                        battle.setOpponentCurrentPokemon(trainerResult.getOpponentPokemon());

                        // Update BattleGUI 
                        SwingUtilities.invokeLater(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                battleGUI.updateTrainerPokemonImage(); 
                                battleGUI.updateOpponentPokemonImage(); 
                            }
                        });

                        // Exit loop if battle is finished
                        if(trainerResult.getGameFinished())
                        {
                            battle.setFinished(true);
                            break;
                        }

                        // Execute opponent turn
                        BattleResult opponentResult = opponentTurn.takeTurn();

                        // Update the Battle object with the new current Pokemon
                        battle.setTrainerCurrentPokemon(opponentResult.getTrainerPokemon());
                        battle.setOpponentCurrentPokemon(opponentResult.getOpponentPokemon());

                        // Update BattleGUI 
                        SwingUtilities.invokeLater(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                battleGUI.updateTrainerPokemonImage(); 
                                battleGUI.updateOpponentPokemonImage(); 
                            }
                        });

                        // Exit loop if battle is finished
                        if(opponentResult.getGameFinished())
                        {
                            battle.setFinished(true);
                            break;
                        }
                    } 
                    
                    // Battle has ended
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // Create BattleEndGUI panel
                            BattleEndGUI battleEndGUI = new BattleEndGUI();
                            
                            // Get main cards panel and cardLayout from SetupGUI
                            JPanel cards = setupGUI.getCards();
                            CardLayout cardLayout = setupGUI.getCardLayout();

                            // Add BattleEndGui to cards with a name
                            cards.add(battleEndGUI, "BattleEnd");
                            
                            // Switch to BattleEnd card
                            cardLayout.show(cards, "BattleEnd");

                            // Create a BATTLE AGAIN listener
                            battleEndGUI.setBattleAgainListener(new ActionListener()
                            {
                                @Override
                                public void actionPerformed(ActionEvent e)
                                {
                                    try
                                    {
                                        // Switch to BattleStartGUI card
                                        cardLayout.show(cards, "BattleStart");
                                        
                                        // Create a new BattleManager for the battle
                                        BattleManager newBattleManager = new BattleManager(game, battleStartGUI, setupGUI);
                                        
                                        // Create a new BattleStartGUI
                                        BattleStartGUI newBattleStartGUI = new BattleStartGUI(game.getTrainer(), connection, setupGUI, newBattleManager);
                                        
                                        // Replace old BattleStartGUI in the card layout with new BattleStartGUI
                                        setupGUI.addCard(newBattleStartGUI, "BattleStart");
                                        
                                        // Update newBattleManager with the new BattleStartGUI
                                        newBattleManager.setBattleStartGUI(newBattleStartGUI);
                                        
                                        // Start the new battle in a separate thread
                                        new Thread(new Runnable()
                                        {
                                           @Override
                                           public void run()
                                           {
                                               try
                                               {
                                                   newBattleManager.startBattle(connection);
                                               }
                                               catch (Exception e)
                                               {
                                                   e.printStackTrace();
                                               }
                                           }
                                        }).start();
                                    } catch (Exception ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                            
                            // Create an EXIT listener
                            battleEndGUI.setExitListener(new ActionListener() 
                            {
                                @Override
                                public void actionPerformed(ActionEvent e) 
                                {
                                    System.exit(0);
                                }
                            });
                        }
                    });  
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Create a FIGHT listener 
    private ActionListener createFightListener(final TrainerTurn trainerTurn)
    {
        return new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                List<Move> moves = battle.getTrainerCurrentPokemon().getMoves();

                battleGUI.showMoveOptions(moves, new ActionListener() 
                {
                    @Override
                    public void actionPerformed(ActionEvent moveEvent) 
                    {
                        // Set move index
                        int moveIndex = Integer.parseInt(moveEvent.getActionCommand());
                        // Pass in move index to take turn with move
                        trainerTurn.selectFight(moveIndex + 1);
                        // Show main buttons, hide options
                        battleGUI.hideOptions();
                    }
                });
            }
        };
    }
    
    // Create a SWITCH listener
    private ActionListener createSwitchListener(final TrainerTurn trainerTurn)
    {
        return new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                List<Pokemon> team = game.getTrainer().getTeam();

                battleGUI.showPokemonTeamOptions(team, new ActionListener() 
                {
                    @Override
                    public void actionPerformed(ActionEvent switchEvent) 
                    {
                        // Get chosen pokemond index
                        int pokemonIndex = Integer.parseInt(switchEvent.getActionCommand());
                        // Pass in chosen pokemon to switch current trainer pokemon
                        trainerTurn.selectSwitch(pokemonIndex);
                        // Show main buttons, hide options
                        battleGUI.hideOptions();
                    }
                });
            }
        };
    }
    
    // Create a RUN listener
    private ActionListener createRunListener(final TrainerTurn trainerTurn)
    {
        return new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                trainerTurn.selectRun();
                // Show main buttons, hide options
                battleGUI.hideOptions();
            }
        };
    }

    
    // Print messages to GUI 
    private void printMessage(String message) 
    {
        // If GUI is connected, display message to GUI
        if (setupGUI != null) 
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    setupGUI.appendMessage(message);
                }
            });
        }
    }
     
    // Setter methods
    public void setBattleGUI(BattleGUI battleGUI)
    {
        this.battleGUI = battleGUI;
    }
    
    public void setBattleStartGUI(BattleStartGUI battleStartGUI)
    {
        this.battleStartGUI = battleStartGUI;
    }
    
    public void setGUI(SetupGUI gui) 
    {
        this.setupGUI = gui;
    }
}