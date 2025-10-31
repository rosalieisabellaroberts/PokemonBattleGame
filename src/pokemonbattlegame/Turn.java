/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author dilro
 */
public abstract class Turn implements BattleAction 
{
    protected Trainer trainer;
    protected Trainer opponent;
    protected Pokemon trainerCurrentPokemon;
    protected Pokemon opponentCurrentPokemon;
    protected Scanner scanner;
    protected Random random;
    
    public Turn(Trainer trainer, Trainer opponent, Pokemon trainerCurrentPokemon, Pokemon opponentCurrentPokemon, Random random)
    {
        this.trainer = trainer;
        this.opponent = opponent;
        this.trainerCurrentPokemon = trainerCurrentPokemon;
        this.opponentCurrentPokemon = opponentCurrentPokemon;
        this.random = random;
    }
    
    // Calculate damage for turn 
    protected int calculateDamage(Move move, Pokemon atkPokemon, Pokemon defPokemon) throws InterruptedException
    {
        Type atkType = atkPokemon.getPokemonType();
        Type defType = defPokemon.getPokemonType();
        
        // Calculate type multiplier 
        double typeMultiplier = 1.0;
        
        // If attack type is strong against defensive type, increase type multiplier 
        if (atkType.isSuperEffectiveAgainst(atkType, defType))
        {
            typeMultiplier = 2.0; 
            Thread.sleep(2000);  
            System.out.println("It's super effective!");
        // If attack type is weak against defensive type, decrease type multiplier 
        } else if (atkType.isNotVeryEffectiveAgainst(atkType, defType))
        {
            typeMultiplier = 0.5;
            Thread.sleep(2000);
            System.out.println("It's not very effective...");
        }
        
        // Calculate critical hit multiplier 
        boolean criticalHit = random.nextDouble() < 0.0625;
        double criticalHitMultiplier = criticalHit ? 1.5 : 1.0;
        
        if (criticalHit)
        {
            Thread.sleep(2000);
            System.out.println("A critical hit!");
        }
        
        // Calculate random multiplier 
        double randomMultiplier = 0.85 + random.nextDouble() * 0.15;
        
        // Adjust power of move by multipliers to calculate move damage
        int damage = (int)Math.floor(move.getPower() * typeMultiplier * criticalHitMultiplier * randomMultiplier);
        if (damage < 1) 
        {
            damage = 1;
        }  
            
        return Math.max(1, damage);     
    }
}

