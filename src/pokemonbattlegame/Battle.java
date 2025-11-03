/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
    
import java.util.*;
import java.sql.*;
import javax.swing.*;
import pokemonbattlegame.BattleResult;
import java.lang.reflect.InvocationTargetException;

public class Battle 
{
    private Trainer trainer;
    private Trainer opponent;
    private Pokemon trainerCurrentPokemon;
    private Pokemon opponentCurrentPokemon;
    private boolean trainerWon = false;
    private boolean gameFinished = false;
    private boolean ranAway = false;
    private Pokemon savedStarter;
    private Connection connection;
    private int startingScore;
    private BattleGUI battleGUI;
    private SetupGUI gui;
    private Random random = new Random();
    private TrainerTurn trainerTurn;

    public Battle(Trainer trainer, Trainer opponent, Pokemon trainerStarterPokemon, Pokemon opponentStarterPokemon, SetupGUI gui, TrainerTurn trainerTurn)  
    {
        this.trainer = trainer;
        this.opponent = opponent;
        this.trainerCurrentPokemon = trainerStarterPokemon;
        this.opponentCurrentPokemon = opponentStarterPokemon;
        this.gui = gui;
        this.trainerTurn = trainerTurn;
    }

    public void runBattle(Connection connection) throws InterruptedException, InvocationTargetException
    {
        // Save starting score and trainer starter pokemon
        this.startingScore = trainer.getScore();
        this.savedStarter = trainer.getStarterPokemon();
        this.connection = connection;

        // Get BattleGUI for battle after it has been created
        battleGUI = gui.getBattleGUI();
        
        // Sync the trainerTurn in the BattleGUI 
        battleGUI.updateTrainerTurn(trainerTurn);
        
        // Create JFrame simultaneously on Swing thread and wait for it to show the battleGUI      
        try
        {
            SwingUtilities.invokeAndWait(new Runnable() 
            {
                @Override
                public void run() 
                {
                    gui.createAndShowBattleGUI(trainer, opponent, Battle.this, connection, trainerTurn);
                }
            });    
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }

        // Run battle loop in a separate thread 
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try 
                {
                    Thread.sleep(2000);

                    // Battle loop
                    while (!gameFinished) 
                    {
                        // TRAINERS TURN
                        // Save turn into a battle result
                        BattleResult result = trainerTurn.takeTurn();

                        // Update current battle data from result
                        trainerCurrentPokemon = result.updatedTrainerPokemon;
                        opponentCurrentPokemon = result.updatedOpponentPokemon;
                        gameFinished = result.gameFinished;
                        ranAway = result.ranAway;
                        trainerWon = result.trainerWon;

                        // Update images after opponent turn 
                        SwingUtilities.invokeLater(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                battleGUI.updateTrainerPokemonImage();
                                battleGUI.updateOpponentPokemonImage();
                            }
                        });
                        
                        if (gameFinished)
                        {
                            break;
                        }
                        
                        // OPPONENTS TURN
                        // Save turn into a battle result
                        OpponentTurn opponentTurn = new OpponentTurn(trainer, opponent, trainerCurrentPokemon, opponentCurrentPokemon, random, battleGUI);
                        BattleResult opponentResult = opponentTurn.takeTurn();
                        
                        // Update current battle data from the result 
                        trainerCurrentPokemon = opponentResult.updatedTrainerPokemon;
                        opponentCurrentPokemon = opponentResult.updatedOpponentPokemon;
                        gameFinished = opponentResult.gameFinished;
                        trainerWon = opponentResult.trainerWon;
                        
                        // Update images after opponent turn 
                        SwingUtilities.invokeLater(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                battleGUI.updateTrainerPokemonImage();
                                battleGUI.updateOpponentPokemonImage();
                            }
                        });
                    }
                    // End of game 
                    endBattleSequence();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private int stageFromScore(int score) 
    {
        // If the trainers score is greater than or equal to 1,000, return stage 3
        if (score >= 1000) return 3;
        // If the trainers score is greater than or equal to 500, return stage 2
        if (score >= 500) return 2;
        // Else return stage 1
        return 1;
    }

    private void playEvolutionSequence(Pokemon mon, int targetStage) throws InterruptedException {
        battleGUI.appendMessage("What? " + mon.getName() + " is evolving!");
        Thread.sleep(900);
        battleGUI.appendMessage("."); Thread.sleep(600);
        battleGUI.appendMessage("."); Thread.sleep(600);
        battleGUI.appendMessage("."); Thread.sleep(600);
        battleGUI.appendMessage("Congratulations! Your " + mon.getName() + " evolved to Stage " + targetStage + "!");
    }
    
    public void resetBattle()
    {
        trainerCurrentPokemon = trainer.getStarterPokemon();
        opponentCurrentPokemon = opponent.getStarterPokemon();
        trainerWon = false;
        gameFinished = false;
        ranAway = false;
        
        // For all pokemon in the team array of the trainer
        for (Pokemon pokemon : trainer.getTeam())
        {
            // Reset HP to original HP
            pokemon.setHP(pokemon.getOriginalHP());
        }
        
        // For all pokemon in the team array of the opponent
        for (Pokemon pokemon : opponent.getTeam())
        {
            // Reset HP to original HP
            pokemon.setHP(pokemon.getOriginalHP());
        }
    }
    
    // Getter and setter for trainerTurn
    public TrainerTurn getTrainerTurn() 
    {
        return trainerTurn;
    }
    
    public void setTrainerTurn(TrainerTurn trainerTurn)
    {
        this.trainerTurn = trainerTurn;
        
        // Keep GUI in sync if available
        if (this.battleGUI != null)
        {
            this.battleGUI.updateTrainerTurn(trainerTurn);
        }
    }
    
    // Getter for random 
    public Random getRandom()
    {
        return random;
    }
    
    public void endBattleSequence()
    {
        // Resave starter pokemon
        trainer.setStarterPokemon(savedStarter);

        // If the trainer has not opted to run away
        try 
        {
            if (!ranAway) 
            {
                // And if the trainer has won the game
                if (trainerWon) 
                {
                    // Increment the trainer's score by 100
                    trainer.setScore(trainer.getScore() + 100);
                    // Update score in database 
                    DatabaseManager.updateTrainerScore(connection, trainer.getUsername(), trainer.getScore());
                    battleGUI.appendMessage("You win! +100 score. New score: " + trainer.getScore());
                    Thread.sleep(2000);
                    // Otherwise if the trainer has lost the game
                } else
                {
                    // Decrement the trainer's score by 50
                    trainer.setScore(trainer.getScore() - 50);
                    // Update score in database 
                    DatabaseManager.updateTrainerScore(connection, trainer.getUsername(), trainer.getScore());
                    battleGUI.appendMessage("You lost... -50 score. New score: " + trainer.getScore());
                    Thread.sleep(2000);
                }
            } else    
            {
                battleGUI.appendMessage("No score change for running.");
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
            
            // Save trainer data in database 
            SaveManager.saveTrainer(trainer, connection);

            // Check the evolution of the starter pokemon
            int prevStage = stageFromScore(startingScore);
            int newStage = stageFromScore(trainer.getScore());

            try
            {
               if (prevStage < 2 && newStage >= 2) 
                {
                    playEvolutionSequence(trainer.getStarterPokemon(), 2);
                }
                if (prevStage < 3 && newStage >= 3) 
                {
                    playEvolutionSequence(trainer.getStarterPokemon(), 3);
                } 
            } catch(InterruptedException e)
            {
                e.printStackTrace();
            }   
        }
    
        // Getter and setter methods 
        public Pokemon getTrainerCurrentPokemon() 
        {
            return trainerCurrentPokemon;
        }
        
        public Pokemon getOpponentCurrentPokemon() 
        {
            return opponentCurrentPokemon;
        }
        
        public void setTrainerRanAway(boolean ranAway)
        {
            this.ranAway = ranAway;
        }
        
        public BattleGUI getBattleGUI()
        {
            return this.battleGUI;
        }
        
        public void setBattleGUI(BattleGUI battleGUI) 
        {
            this.battleGUI = battleGUI;
        }

        public void setConnection(Connection connection) 
        {
            this.connection = connection;
        }

        public void setTrainerCurrentPokemon(Pokemon pokemon) 
        {
            this.trainerCurrentPokemon = pokemon;
        }

        public void setOpponentCurrentPokemon(Pokemon pokemon) 
        {
            this.opponentCurrentPokemon = pokemon;
        }
        
        public boolean isFinished() 
        {
            return gameFinished;
        }

        public void setFinished(boolean finished) 
        {
            this.gameFinished = finished;
        }
}

