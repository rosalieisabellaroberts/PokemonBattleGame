/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */

public class Move 
{
    private String name;
    private double power;
    private double accuracy;


    public Move(String name, double power, double accuracy)
    { 
        this.name = name;
        this.power = power;
        this.accuracy = accuracy;
    }  
  
  public String getName()
  {
        return name;
  }
  
  public double getAccuracy()
  {
        return accuracy;
  }
  
  public double getPower()
  {
        return power;
  }
}
