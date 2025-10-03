/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dilro
 */
public class Trainer
{
    private String username;
    private String name;
    private Pokemon starterPokemon;
    private int score;
    private int level;
    private String challengeMessage;
    private ArrayList<Pokemon> team;
    
    public Trainer(String username, String name, String challengeMessage)
    {
        this.username = username;
        this.name = name;
        this.level = 1;
        this.score = 0;
        this.challengeMessage = challengeMessage;
        this.team = new ArrayList<>();
    }
    
    public ArrayList<Pokemon> getTeam()
    {
        return team;
    }
    
    public void setStarterPokemon(Pokemon starterPokemon)
    {
        this.starterPokemon = starterPokemon;
    }
    
    public Pokemon getStarterPokemon()
    {
        return starterPokemon;
    }
    
    public String getChallengeMessage()
    {
        return challengeMessage;
    }
    
    public void setChallengeMessage(String challengeMessage)
    {
        this.challengeMessage = challengeMessage;
    }
    
    public String getName() 
    {
        return name;
    }
    
    public int getLevel() 
    {
        return level;
    }
    
    public void setLevel(int level) 
    {
        this.level = level;
    }

    public String getUsername() 
    {
        return username;
    }

    public int getScore() 
    {
        return score;
    }

    public void setScore(int score) 
    {
        this.score = score;
    }
}
