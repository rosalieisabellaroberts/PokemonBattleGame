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
public class Trainer extends Character
{
    private String username;
    private int score;
    private String challengeMessage;
    private String imagePath;
    
    public Trainer (String username, String name, int score, int level, Pokemon starterPokemon, String challengeMessage, String imagePath)
    {
        super(name, level);
        this.username = username;
        this.score = score;
        this.setStarterPokemon(starterPokemon);
        this.team = new ArrayList<>();
        this.team.add(starterPokemon);
        this.challengeMessage = challengeMessage;
        this.imagePath = imagePath;
    }
    
    public Trainer(String username, String name, String challengeMessage)
    {
        super(name, 1);
        this.username = username;
        this.score = 0;
        this.challengeMessage = challengeMessage;
        this.setTeam(new ArrayList<>());
        this.imagePath = "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\trainer.png";
                
    }
    
    // Implement dialogue of battle challenge message 
    @Override 
    public String speak()
    {
        return getName() + ": " + challengeMessage;
    }
    
    // Getter and setter methods
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

     public String getImagePath()
    {
        return imagePath;
    }
     
    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
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
    
    public void setTeam(ArrayList<Pokemon> team) 
    {
        this.team = team;
    }
}
