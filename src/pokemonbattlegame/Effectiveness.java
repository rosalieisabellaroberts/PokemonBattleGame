/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
public interface Effectiveness 
{
    boolean isSuperEffectiveAgainst(Type attackType, Type defenderType);
    boolean isNotVeryEffectiveAgainst(Type attackType, Type defenderType);
}
