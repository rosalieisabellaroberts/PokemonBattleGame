/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
public interface BattleAction 
{
    // Declare takeTurn() method to be used by trainer and opponent turns, and return BattleResult object
    BattleResult takeTurn() throws InterruptedException;
}
