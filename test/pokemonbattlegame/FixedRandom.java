/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.util.Random;

/**
 *
 * @author dilro
 */
public class FixedRandom extends Random
{
    private final double fixedRandomValue;
    
    public FixedRandom(double fixedRandomValue)
    {
        this.fixedRandomValue = fixedRandomValue;
    }
    
    // Always return the fixed random value for testing purposes
    @Override 
    public double nextDouble()
    {
        return fixedRandomValue;
    }
}
