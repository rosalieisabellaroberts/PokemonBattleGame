/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
    
import java.util.*;
import java.sql.*;

public class Battle 
{
    private Trainer trainer;
    private Trainer opponent;
    private Pokemon trainerCurrentPokemon;
    private Pokemon opponentCurrentPokemon;
    private boolean trainerWon = false;
    private boolean gameFinished = false;
    private boolean ranAway = false;

    public Battle(Trainer trainer, Trainer opponent, Pokemon trainerStarterPokemon, Pokemon opponentStarterPokemon)  
    {
        this.trainer = trainer;
        this.opponent = opponent;
        this.trainerCurrentPokemon = trainer.getStarterPokemon();
        this.opponentCurrentPokemon = opponent.getStarterPokemon();
    }

    public void runBattle(Connection connection) throws InterruptedException 
    {
        int startingScore = trainer.getScore();
        Pokemon savedStarter = trainer.getStarterPokemon();

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        while (!gameFinished) 
        {
            // Trainer’s turn
            if (!gameFinished) 
            {
                TrainerTurn trainerTurn = new TrainerTurn(
                        trainer, opponent,
                        trainerCurrentPokemon, opponentCurrentPokemon,
                        scanner, random
                );
                BattleResult result = trainerTurn.takeTurn();
                trainerCurrentPokemon = result.updatedTrainerPokemon;
                opponentCurrentPokemon = result.updatedOpponentPokemon;
                gameFinished = result.gameFinished;
                ranAway = result.ranAway;
                trainerWon = result.trainerWon;
            }

            // Opponent’s turn
            if (!gameFinished) 
            {
                OpponentTurn oppTurn = new OpponentTurn(
                        trainer, opponent,
                        trainerCurrentPokemon, opponentCurrentPokemon,
                        random
                );
                BattleResult result = oppTurn.takeTurn();
                trainerCurrentPokemon = result.updatedTrainerPokemon;
                opponentCurrentPokemon = result.updatedOpponentPokemon;
                gameFinished = result.gameFinished;
                trainerWon = result.trainerWon;
            }
        }

        // End of game 
        trainer.setStarterPokemon(savedStarter);

        // If the trainer has not opted to run away
        if (!ranAway) {
            // And if the trainer has won the game
            if (trainerWon) {
                // Increment the trainer's score by 100
                trainer.setScore(trainer.getScore() + 100);
                // Update score in database 
                DatabaseManager.updateTrainerScore(connection, trainer.getUsername(), trainer.getScore());
                System.out.println("You win! +100 score. New score: " + trainer.getScore());
                Thread.sleep(2000);
            // Otherwise if the trainer has lost the game
            } else {
                // Decrement the trainer's score by 50
                trainer.setScore(trainer.getScore() - 50);
                // Update score in database 
                DatabaseManager.updateTrainerScore(connection, trainer.getUsername(), trainer.getScore());
                System.out.println("You lost... -50 score. New score: " + trainer.getScore());
                Thread.sleep(2000);
            }
        } else {
            System.out.println("No score change for running.");
        }

        // Save trainer data in database 
        SaveManager.saveTrainer(trainer, connection);

        // Check the evolution of the starter pokemon
        int prevStage = stageFromScore(startingScore);
        int newStage = stageFromScore(trainer.getScore());

        if (prevStage < 2 && newStage >= 2) {
            playEvolutionSequence(trainer.getStarterPokemon(), 2);
        }
        if (prevStage < 3 && newStage >= 3) {
            playEvolutionSequence(trainer.getStarterPokemon(), 3);
        }
    }

    private int stageFromScore(int score) 
    {
        // If the trainers score is greater than or equal to 1,000, return stage 3
        if (score >= 1000) return 3;
        // If the trainers score is greater than or equal to 500, return stage 2
        if (score >= 500) return 2;
        // Else return stage 1
        return 1;
    }

    private void playEvolutionSequence(Pokemon mon, int targetStage) throws InterruptedException {
        System.out.println("What? " + mon.getName() + " is evolving!");
        Thread.sleep(900);
        System.out.print("."); Thread.sleep(600);
        System.out.print("."); Thread.sleep(600);
        System.out.println("."); Thread.sleep(600);
        System.out.println("Congratulations! Your " + mon.getName() + " evolved to Stage " + targetStage + "!");
    }
    
    public void resetBattle()
    {
        trainerCurrentPokemon = trainer.getStarterPokemon();
        opponentCurrentPokemon = opponent.getStarterPokemon();
        trainerWon = false;
        gameFinished = false;
        ranAway = false;
        
        // For all pokemon in the team array of the trainer
        for (Pokemon pokemon : trainer.getTeam())
        {
            // Reset HP to original HP
            pokemon.setHP(pokemon.getOriginalHP());
        }
        
        // For all pokemon in the team array of the opponent
        for (Pokemon pokemon : opponent.getTeam())
        {
            // Reset HP to original HP
            pokemon.setHP(pokemon.getOriginalHP());
        }
    }
}

