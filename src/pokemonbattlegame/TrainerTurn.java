
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
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Implements the BattleAction interface which declares the takeTurn() method and returns a BattleResult object
public class TrainerTurn extends Turn implements BattleAction 
{
    private BattleGUI battleGUI;
    private Battle battle;

    // Player action is set as int, where 1 = FIGHT, 2 = SWITCH and 3 = RUN
    private PlayerActionType playerActionType = null;
    private int selectedMove = 0;
    private int selectedPokemon = -1;
    private boolean actionSelected = false;

    public TrainerTurn(Trainer trainer, Trainer opponent, Pokemon trainerCurrentPokemon, Pokemon opponentCurrentPokemon, Random random, BattleGUI battleGUI, Battle battle) 
    {
        super(trainer, opponent, trainerCurrentPokemon, opponentCurrentPokemon, random, battleGUI);
        this.battle = battle;
    }

    @Override
    public synchronized BattleResult takeTurn() throws InterruptedException 
    {
        boolean gameFinished = false;
        boolean trainerWon = false;
        boolean ranAway = false;

        // Show main action options in battleGUI by hiding sub-options for FIGHT and SWITCH
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                battleGUI.hideOptions();
                battleGUI.enableMainButtons();
            }
        });


        // Wait for trainer selection of action 
        while (!actionSelected)
        {
            wait();
        }
          
        // Determine action type from current playerAction
        switch (playerActionType)
        {
            // FIGHT
            case FIGHT:
                // Assign chosen move into a move object
                Move move = trainerCurrentPokemon.getMoves().get(selectedMove - 1);
                // Display message about chosen move in battle log
                SwingUtilities.invokeLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        battleGUI.appendMessage("\n" + trainerCurrentPokemon.getName() + " used " + move.getName() + "!");
                    }
                });

                Thread.sleep(2000);

                if (moveHit(move, random)) 
                {
                    // Calculate damage 
                    int damage = calculateDamage(move, trainerCurrentPokemon, opponentCurrentPokemon);
                    // Apply damage to opponent pokemon
                    opponentCurrentPokemon.setHP(Math.max(0, opponentCurrentPokemon.getHP() - damage));
                    Thread.sleep(2000);
                    // Display message about damge to opponent pokemon in battle log
                    SwingUtilities.invokeLater(new Runnable() 
                    {
                        @Override
                        public void run() 
                        {
                            battleGUI.appendMessage("It dealt " + damage + " damage.");
                        }
                    });
                    Thread.sleep(2000);

                    // If the opponent pokemons' HP became less than or equal to 0
                    if (opponentCurrentPokemon.getHP() <= 0) 
                    {
                        // Display message about opponent pokemon fainting in battle log
                        SwingUtilities.invokeLater(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                battleGUI.appendMessage(opponentCurrentPokemon.getName() + " fainted!");
                            }
                        });
                        Thread.sleep(2000);
                        boolean opponentSwitchedPokemon = false;

                        // For every pokemon in opponent team
                        for (Pokemon pokemon : opponent.getTeam()) 
                        {
                            // If pokemons' HP is greater than 0
                            if (pokemon.getHP() > 0) 
                            {
                                // Set opponents' current pokemon to the pokemon with HP greater than 0
                                opponentCurrentPokemon = pokemon;
                                opponent.setStarterPokemon(pokemon);
                                // Display message about new pokemon sent out by opponent to battle log
                                 SwingUtilities.invokeLater(new Runnable() 
                                 {
                                    @Override
                                    public void run() 
                                    {
                                        battleGUI.appendMessage(opponent.getName() + " sent out " + opponentCurrentPokemon.getName() + "!");
                                    }
                                });
                                Thread.sleep(2000);
                                // Set opponentSwitchedPokemon flag to true
                                opponentSwitchedPokemon = true;
                                break;
                            }
                        }

                        // If opponent could not switch pokemon (because they have no remaining pokemon with HP greater than 0)
                        if (!opponentSwitchedPokemon) 
                        {
                            // Finish game and set trainer as having won
                            trainerWon = true;
                            gameFinished = true;
                            break;
                        }
                    }
                } else 
                {
                    SwingUtilities.invokeLater(new Runnable() 
                    {
                        @Override
                        public void run() 
                        {
                            battleGUI.appendMessage(trainerCurrentPokemon.getName() + " missed!");
                        }
                    });
                    Thread.sleep(2000);
                }
                break;

            // SWITCH
            case SWITCH:
 
            // If index of pokemon chosen in team is out of range, exit the loop
            if (selectedPokemon < 0 || selectedPokemon >= trainer.getTeam().size())
            {
                actionSelected = false;
                playerActionType = null;
                break;
            }

            // Set chosen pokemon from the teamIndex passed into the function
            Pokemon chosenPokemon = trainer.getTeam().get(selectedPokemon);

            // If chosen pokemon has an HP less than or equal to 0
            if (chosenPokemon.getHP() <= 0)
            {
                // Display message about fainted pokemon
                SwingUtilities.invokeLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        battleGUI.appendMessage(chosenPokemon.getName() + " has fainted. Choose another.");
                    }
                });
                actionSelected = false;
                playerActionType = null;
                break;
            }

            // If chosen pokemon is already chosen 
            if (chosenPokemon == trainerCurrentPokemon)
            {
                // Display message about active pokemon 
                SwingUtilities.invokeLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        battleGUI.appendMessage(chosenPokemon.getName() + " is already active.");
                    }
                });
                actionSelected = false;
                playerActionType = null;
                break;
            }

            // Display message about chosen pokemon
            SwingUtilities.invokeLater(new Runnable() 
            {
                @Override
                public void run() 
                {
                    battleGUI.appendMessage("Go, " + chosenPokemon.getName() + "!");
                    actionSelected = false;
                    playerActionType = null;
                }
            });
            Thread.sleep(2000);
            break;

            // RUN
            case RUN:
                // Display message about user running away to battle log
                SwingUtilities.invokeLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        battleGUI.appendMessage("You ran away! The battle ends.");
                        // Reset action state for next turn 
                        actionSelected = false;
                        playerActionType = null;
                    }
                });
                Thread.sleep(2000);
                ranAway = true;
                gameFinished = true;
                break;
            }

            // Reset action state for next turn 
            actionSelected = false;
            playerActionType = null;
            selectedMove = 0;
            selectedPokemon = -1;

            // Return battleResult object 
            return new BattleResult(trainerCurrentPokemon, opponentCurrentPokemon, gameFinished, trainerWon, ranAway);
        }
        
    // Call method when FIGHT button is selected 
    public synchronized void selectFight(int moveIndex)
    {
        // Set the index of the chosen move and raise actionSelected flag
        this.playerActionType = PlayerActionType.FIGHT;
        this.selectedMove = moveIndex;
        this.actionSelected = true;
        
        // Unlock the battle loop waiting for trainer's choice
        notify();
    }

    // Call method when SWITCH button is selected
    public synchronized void selectSwitch(int pokemonIndex)
    {
        this.playerActionType = PlayerActionType.SWITCH;
        this.selectedPokemon = pokemonIndex;
        this.actionSelected = true;
        
        // Unlock the battle loop
        notify(); 
    }
        
    // Call method when RUN button is selected 
    public synchronized void selectRun()
    {
       this.playerActionType = PlayerActionType.RUN;
       this.actionSelected = true;
       
       // Unlock the battle loop
        notify();       
    }
    
    // Getter and setter methods
    public void setBattleGUI(BattleGUI battleGUI) 
    {
        this.battleGUI = battleGUI;
    }
    
    public void setBattle(Battle battle) 
    {
        this.battle = battle;
    }
    
    public void setTrainerCurrentPokemon(Pokemon pokemon) 
    {
        this.trainerCurrentPokemon = pokemon;
    }
}

