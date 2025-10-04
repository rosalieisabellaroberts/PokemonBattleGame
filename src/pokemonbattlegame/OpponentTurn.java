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

public class OpponentTurn 
{
    private Trainer trainer;
    private Trainer opponent;
    private Pokemon trainerCurrentPokemon;
    private Pokemon opponentCurrentPokemon;
    private Random random;

    public OpponentTurn(Trainer trainer, Trainer opponent,
                        Pokemon trainerCurrentPokemon, Pokemon opponentCurrentPokemon,
                        Random random) {
        this.trainer = trainer;
        this.opponent = opponent;
        this.trainerCurrentPokemon = trainerCurrentPokemon;
        this.opponentCurrentPokemon = opponentCurrentPokemon;
        this.random = random;
    }

    public OpponentTurnResult takeTurn() throws InterruptedException 
    {
        boolean gameFinished = false;
        boolean trainerWon = false;

        // TO DO: Functionality for trainer's turn (copy, paste and adjust slight details from users loop above
        ArrayList<Move> oppMoves = opponentCurrentPokemon.getMoves();
        
        if(oppMoves == null || oppMoves.isEmpty())
        {
            Thread.sleep(2000);  
            System.out.println(opponentCurrentPokemon.getName() + " has no usable moves!");
            return new OpponentTurnResult(trainerCurrentPokemon, opponentCurrentPokemon, false, false);
        }
                
        Move oppMove = oppMoves.get(random.nextInt(oppMoves.size()));
                
        Thread.sleep(2000);  
        System.out.println("\n"+opponentCurrentPokemon.getName() + " used " + oppMove.getName() + "!");
        Thread.sleep(2000);  
                
        //accuracy check
        double randomAccuracy = random.nextDouble()*100;
        
        if (randomAccuracy <= oppMove.getAccuracy())
        {
            pokemonbattlegame.Type atkType = opponentCurrentPokemon.getPokemonType();
            pokemonbattlegame.Type defType = trainerCurrentPokemon.getPokemonType();
                    
            //added multipliers 
            double typeMult = 1.0;
            if (TypeEffectiveness.isSuperEffective(atkType, defType))
            {
                typeMult = 2.0;
                Thread.sleep(2000);  
                System.out.println("It's super effective!");
            } else if (TypeEffectiveness.isNotVeryEffective(atkType, defType))
            {
                typeMult = 0.5;
                Thread.sleep(2000);  
                System.out.println("It's not very effective...");
            }
                    
            //crit and random factor
            boolean crit = random.nextDouble() < 0.0625; //6.25%
            double critMult = crit ? 1.5 : 1.0;
            double randMult = 0.85 + (random.nextDouble()*0.15);
                    
            int dmg = (int) Math.floor(oppMove.getPower() *typeMult * critMult * randMult);
            if (dmg < 1) dmg = 1;
            Thread.sleep(2000);  
            
            if (crit) System.out.println("A critical hit!");
                    
            int newHP = Math.max(0, trainerCurrentPokemon.getHP() - dmg);
            trainerCurrentPokemon.setHP(newHP); 
            System.out.println("It dealt " +dmg+ " damage.");
            Thread.sleep(2000); 
                    
            //if fainted
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
                    return new OpponentTurnResult(trainerCurrentPokemon, opponentCurrentPokemon, gameFinished, trainerWon);
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
        
        return new OpponentTurnResult(trainerCurrentPokemon, opponentCurrentPokemon, gameFinished, trainerWon);
    }
}

