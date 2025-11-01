/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author dilro
 */
public class TestTurn extends Turn
{
    public TestTurn(Trainer trainer, Trainer opponent, Pokemon trainerPokemon, Pokemon opponentPokemon, Random random)
    {
        // Inherit values from superclass Turn
        super(trainer, opponent, trainerPokemon, opponentPokemon, random);
    }
    
    @Override
    public BattleResult takeTurn() throws InterruptedException
    {
        // Not necessary for testing 
        return null;
    }
    
    // Test case for calculating damage
    public int testCalculateDamage(Move move, Pokemon atkPokemon, Pokemon defPokemon) throws InterruptedException 
    {
        return calculateDamage(move, atkPokemon, defPokemon);
    }
    
    // Test case for taking a turn and returning a battle result
    public BattleResult takeTestTurn(Move move, Pokemon atkPokemon, Pokemon defPokemon) throws InterruptedException
    {
        // Continue game 
        boolean gameFinished = false;
        boolean trainerWon = false;
        boolean ranAway = false;
        
        // If move hits
        if(moveHit(move, random))
        {
            // Calculate the damage
            int damage = calculateDamage(move, atkPokemon, defPokemon);
            
            // Apply damage to defending pokemon
            defPokemon.setHP(Math.max(0, defPokemon.getHP() - damage));
            
            // If defending pokemon has no HP remaining 
            if (defPokemon.getHP() <= 0)
            {
                // Check if there are any pokemon remaining in team with HP 
                boolean pokemonRemaining = false;
                
                for (Pokemon pokemon : opponent.getTeam())
                {
                    if (pokemon.getHP() > 0)
                    {
                        opponent.setStarterPokemon(pokemon);
                        pokemonRemaining = true;
                        break;
                    }
                } if (!pokemonRemaining)
                {
                    gameFinished = true;
                    trainerWon = true;
                }
            }
        } 
        
        return new BattleResult(atkPokemon, defPokemon, gameFinished, trainerWon, ranAway);
    }
    
    // Test case for switching a trainers' pokemon
    public void switchTrainerPokemon(Pokemon newPokemon) 
    {
        this.trainerCurrentPokemon = newPokemon;
    }
    
    // Test case for running away 
    public BattleResult runAway(Pokemon atkPokemon, Pokemon defPokemon)
    {
        // Set ranAway to true
        boolean ranAway = true;
        
        // Set gameFinished to true (running away ends the game)
        boolean gameFinished = true;
        
        // Set gameWon to false (running away forfeits winning)
        boolean trainerWon = false;
        
        // Return battle result
        return new BattleResult(atkPokemon, defPokemon, gameFinished, trainerWon, ranAway);  
    }
    
    // Getter methods
    public Pokemon getTrainerPokemon()
    {
        return trainerCurrentPokemon;
    }
    
    // Get method for trainer pokemon 
    public Pokemon getOpponentPokemon()
    {
        return opponentCurrentPokemon;
    }
}

