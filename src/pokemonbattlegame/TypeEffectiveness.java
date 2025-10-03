/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
public class TypeEffectiveness 
{
    public static boolean isSuperEffective(pokemonbattlegame.Type attackType, pokemonbattlegame.Type defenderType)
    {
        for (String strongType : attackType.getStrongAgainst())
        {
            if (strongType.equalsIgnoreCase(defenderType.getName()))
            {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isNotVeryEffective(pokemonbattlegame.Type attackType, pokemonbattlegame.Type defenderType)
    {
        for (String weakType : attackType.getWeakAgainst())
        {
            if (weakType.equalsIgnoreCase(defenderType.getName()))
            {
                return true;
            }
        }
        return false;
    }
}
