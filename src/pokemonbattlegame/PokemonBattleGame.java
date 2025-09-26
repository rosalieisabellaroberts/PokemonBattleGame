/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pokemonbattlegame;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/**
 *
 * @author dilro
 */
public class PokemonBattleGame 
{   
    private Trainer trainer;
    private Trainer opponent; 
    private Pokemon trainerCurrentPokemon; 
    private Pokemon opponentCurrentPokemon;
    private Pokemon[] pokemons;
    
    PokemonBattleGame()
    {
        
    }
    
    public static void main(String[] args) throws InterruptedException
    {
        // Show the professor Oaks intro JFrame 
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                GameSetup oaksIntro = new GameSetup();
                oaksIntro.setVisible(true);
            }
        });
        
        // Create a new game
        PokemonBattleGame game = new PokemonBattleGame();
      
        // Setup the game
        new SetupGame(game);
        
        // Prompt user for start 
        game.startBattle();
            
        // Randomly generate an opponent
        try 
        {
            GenerateOpponent generatedOpponent = new GenerateOpponent();
            generatedOpponent.generateOpponent(game.getTrainer());
            game.setOpponent(generatedOpponent.getOpponent());
        } catch (InterruptedException e)
        {
            System.out.println("Error generating opponent!" + e.getMessage());
        }
            
        // Randomly generate an opponent
        GeneratePokemonTeams generatedPokemonTeams = new GeneratePokemonTeams();
        generatedPokemonTeams.generatePokemonTeams(game.getTrainer(), game.getOpponent());

        // Display game details 
        DisplayGameDetails gameDetails = new DisplayGameDetails();
        gameDetails.displayGameDetails(game.getTrainer(), game.getOpponent());
        
        // Enter battle loop 
        game.startBattle();
        
    }
   
    public void startBattle() throws InterruptedException 
    {
        System.out.println("Professor Oaks: It is now time for you and " + this.trainer.getStarterPokemon().getName() +
                " to journey into the magical world of Pokemon!\n");
        Thread.sleep(2000);
        System.out.println("When you are ready, press any key to start...");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        // Generate opponent
        GenerateOpponent generatedOpponent = new GenerateOpponent();
        generatedOpponent.generateOpponent(this.getTrainer());
        this.setOpponent(generatedOpponent.getOpponent());

        // Generate Pokemon teams
        GeneratePokemonTeams generatedPokemonTeams = new GeneratePokemonTeams();
        generatedPokemonTeams.generatePokemonTeams(getTrainer(), getOpponent());

        // Display game details
        DisplayGameDetails gameDetails = new DisplayGameDetails();
        gameDetails.displayGameDetails(this.getTrainer(), this.getOpponent());

        // Start battle
        Battle battle = new Battle(trainer, opponent, trainer.getStarterPokemon(), opponent.getStarterPokemon());
        battle.runBattle();
    }

    public void setTrainer(Trainer trainer) 
    {
        this.trainer = trainer;
    }

    public Trainer getTrainer() 
    {
        return trainer;
    }

    public Trainer getOpponent() 
    {
        return opponent;
    }

    public void setOpponent(Trainer opponent) 
    {
        this.opponent = opponent;
    }
    
    // Setter method for pokemons
    public void setPokemons(Pokemon[] pokemons) 
    {
        this.pokemons = pokemons;
    }

    //score for evolution stage 1,2(>=500),3(>=1000)
    private int stageFromScore(int score)
    {
        if(score >= 1000) return 3;
        if(score >= 500) return 2;
        return 1;
    }
    
    private void playEvolutionSequence(Pokemon mon, int targetStage) throws InterruptedException
    {
        System.out.println("What? " + mon.getName() + " is evolving!");
        Thread.sleep(900);
        System.out.print("."); Thread.sleep(600);
        System.out.print("."); Thread.sleep(600);
        System.out.println("."); Thread.sleep(600);
        System.out.println("Congratulations! Your " + mon.getName() + " evolved to Stage " + targetStage + "!");
    }
}
