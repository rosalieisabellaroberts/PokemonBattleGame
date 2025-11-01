/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.util.*;
/**
 *
 * @author dilro
 */
public class Type implements Effectiveness
{
    private String name;
    private ArrayList<Type> weakAgainst;
    private ArrayList<Type> strongAgainst;
    
    public Type(String name)
    {
       this.name = name;
       this.weakAgainst = new ArrayList<>();
       this.strongAgainst = new ArrayList<>();
    }
    
    @Override 
    public boolean isSuperEffectiveAgainst(pokemonbattlegame.Type attackType, pokemonbattlegame.Type defenderType)
    {
        for (Type strongType : attackType.getStrongAgainst())
        {
            if (strongType.getName().equalsIgnoreCase(defenderType.getName()))
            {
                return true;
            }
        }
        return false;
    }
    
    @Override 
    public boolean isNotVeryEffectiveAgainst(pokemonbattlegame.Type attackType, pokemonbattlegame.Type defenderType)
    {
        for (Type weakType : attackType.getWeakAgainst())
        {
            if (weakType.getName().equalsIgnoreCase(defenderType.getName()))
            {
                return true;
            }
        }
        return false;
    }
    
    public String getName()
    { 
        return name;
    }
    
    public ArrayList<Type> getWeakAgainst()
    {
        return weakAgainst;
    }
    
    public ArrayList<Type> getStrongAgainst()
    {
        return strongAgainst;
    }
    
    public void setStrongAgainst(Type type)
    {
        strongAgainst.add(type);
    }
    
    public void setWeakAgainst(Type type)
    {
        weakAgainst.add(type);
    }
    
}