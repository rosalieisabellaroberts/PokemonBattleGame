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
    private List<String> weakAgainst;
    private List<String> strongAgainst;
    
    public Type()
    {
       
    }
    
    public String getName()
    { 
        return name;
    }
    
    public List<String> getWeakAgainst()
    {
        return weakAgainst;
    }
    
    public List<String> getStrongAgainst()
    {
        return strongAgainst;
    }

}