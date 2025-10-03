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
public class Type
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