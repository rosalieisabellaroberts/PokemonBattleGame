/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */

import java.util.Scanner;

public class BattleManager 
{
    private PokemonBattleGame game;

    public BattleManager(PokemonBattleGame game) 
    {
        this.game = game;
    }

    public void startBattle() throws InterruptedException 
    {
        Thread.sleep(2000);
        
        System.out.println("Professor Oaks: It is now time for you and " +
                game.getTrainer().getStarterPokemon().getName() +
                " to journey into the magical world of Pokemon!\n");

        Thread.sleep(2000);
        System.out.println("When you are ready, press any key to start...");
        new Scanner(System.in).nextLine();

        // Generate Pokemon teams
        GeneratePokemonTeams generatedPokemonTeams = new GeneratePokemonTeams();
        generatedPokemonTeams.generatePokemonTeams(game.getTrainer(), game.getOpponent());

        System.out.println(game.getOpponent().getName()+": "+game.getOpponent().getChallengeMessage());
        Thread.sleep(2000);
        
        // Display game details
        DisplayGameDetails gameDetails = new DisplayGameDetails();
        gameDetails.displayGameDetails(game.getTrainer(), game.getOpponent());
            
        // Start actual battle
        Battle battle = new Battle(game.getTrainer(), game.getOpponent(), game.getTrainer().getStarterPokemon(), game.getOpponent().getStarterPokemon());
        battle.runBattle();
    }
}

