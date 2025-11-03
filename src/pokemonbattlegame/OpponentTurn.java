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

// Implements the BattleAction interface which declares the takeTurn() method and returns a BattleResult object
public class OpponentTurn extends Turn implements BattleAction 
{

    public OpponentTurn(Trainer trainer, Trainer opponent,
                        Pokemon trainerCurrentPokemon, Pokemon opponentCurrentPokemon,
                        java.util.Random random) {
        super(trainer, opponent, trainerCurrentPokemon, opponentCurrentPokemon, random);
    }

    @Override
    public BattleResult takeTurn() throws InterruptedException 
    {
        boolean gameFinished = false;
        boolean trainerWon = false;

        // Functionality for trainer's turn (copy, paste and adjust slight details from users loop above
        ArrayList<Move> oppMoves = opponentCurrentPokemon.getMoves();
        
        if(oppMoves == null || oppMoves.isEmpty())
        {
            Thread.sleep(2000);  
            System.out.println(opponentCurrentPokemon.getName() + " has no usable moves!");
            return new BattleResult(trainerCurrentPokemon, opponentCurrentPokemon, false, false, false);
        }
                
        Move oppMove = oppMoves.get(random.nextInt(oppMoves.size()));
                
        Thread.sleep(2000);  
        System.out.println("\n"+opponentCurrentPokemon.getName() + " used " + oppMove.getName() + "!");
        Thread.sleep(2000);  
        
        if (moveHit(oppMove, random))
        {                    
            int damage = calculateDamage(oppMove, trainerCurrentPokemon, opponentCurrentPokemon);
                
            opponentCurrentPokemon.setHP(Math.max(0, opponentCurrentPokemon.getHP() - damage));
            Thread.sleep(2000);
            System.out.println("It dealt " + damage + " damage.");
            Thread.sleep(2000);
                    
            // If pokemon fainteds
            if (trainerCurrentPokemon.getHP() <= 0)
            {
                Thread.sleep(2000);  
                System.out.println(trainerCurrentPokemon.getName() + " fainted!");
                Thread.sleep(2000);
                boolean playerSwitched = false;
                for (Pokemon p:trainer.getTeam())
                {
                    if (p.getHP() > 0)
                    {
                        trainerCurrentPokemon = p;
                        
                        try
                        {
                            trainer.setStarterPokemon(p);
                        } catch (Throwable ignored) 
                        {
                        }
                        
                        System.out.println("You sent out " + p.getName() + "!");
                        Thread.sleep(2000);
                        playerSwitched = true;
                        break;
                    }
                }
                if (!playerSwitched)
                {
                    //no pokemon left on your side then opponent wins
                    trainerWon = false;
                    gameFinished = true;
                    return new BattleResult(trainerCurrentPokemon, opponentCurrentPokemon, gameFinished, trainerWon, false);
                } 
            }
        }
        else
        {
            Thread.sleep(2000);  
            System.out.println(opponentCurrentPokemon.getName() + " missed!");
            Thread.sleep(2000);
        }
    
        DisplayGameDetails gameDetails = new DisplayGameDetails();
        gameDetails.displayGameDetails(trainer, opponent);
        
        return new BattleResult(trainerCurrentPokemon, opponentCurrentPokemon, gameFinished, trainerWon, false);
    }
}

