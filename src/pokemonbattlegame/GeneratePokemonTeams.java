/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.sql.*;

/**
 *
 * @author dilro
 */
public class GeneratePokemonTeams 
{
    private static Pokemon[] pokemons;
    private static Pokemon trainerCurrentPokemon; 
    private static Pokemon opponentCurrentPokemon;
    
    public GeneratePokemonTeams(List<Pokemon> pokemons)
    {
        // Convert list to an arrary for simplified random access
        this.pokemons = pokemons.toArray(new Pokemon[0]);
    }
    
    public static void generatePokemonTeams(Trainer trainer, Trainer opponent, Connection connection) throws SQLException
    {
        // Clear old teams 
        trainer.setTeam(new ArrayList<>());
        opponent.setTeam(new ArrayList<>());
        
        // Create an array list to new pokemon team for the user
        ArrayList<Pokemon> trainerTeam = trainer.getTeam();
        
        if (trainerTeam == null)
        {
            trainerTeam = new ArrayList<>();
            trainer.setTeam(trainerTeam);
        }
        
        // Create an array list to new pokemon team for the opponent
        ArrayList<Pokemon> opponentTeam = opponent.getTeam();
        if (opponentTeam == null)
        {
            opponentTeam = new ArrayList<>();
            opponent.setTeam(opponentTeam);
        }
        
        // Create a new random object
        Random number = new Random();
        
        // Set starter pokemon as first pokemon in team for trainer and opponent
        Pokemon trainerStarter = DatabaseManager.getPokemon(connection, trainer.getStarterPokemon().getName());
        Pokemon opponentStarter = DatabaseManager.getPokemon(connection, opponent.getStarterPokemon().getName());
        
        if(!trainerTeam.contains(trainerStarter))
        {
            // Add the trainers starter pokemon at index 0
            trainerTeam.add(0, trainerStarter);
        }
        
        if(!opponentTeam.contains(opponentStarter))
        {
            // Add the opponents starter pokemon at index 0
            opponentTeam.add(0, opponentStarter);
        }
        
        Pokemon addedPokemon;
        
        // LOOP (x5) generate random pokemon and add to pokemon team array for trainer
        while (trainerTeam.size() < 6)
        {
            // Generate a random int within the size of the pokemon array
            int randomIndex = number.nextInt(pokemons.length);
            
            // Set added pokemon to the pokemon in the array list at the random index
            addedPokemon = DatabaseManager.getPokemon(DatabaseManager.getConnection(), pokemons[randomIndex].getName());
            
            // Add the pokemon at the random index to the trainer team array list
            trainerTeam.add(addedPokemon);
        }
        
        // LOOP (x5) generate random pokemon and add to pokemon team array for opponent
        while (opponentTeam.size() < 6)
        {
            // Generate a random int within the size of the pokemon array
            int randomIndex = number.nextInt(pokemons.length);
            
            // Set added pokemon to the pokemon in the array list at the random index
            addedPokemon = DatabaseManager.getPokemon(DatabaseManager.getConnection(), pokemons[randomIndex].getName());
            
            // Add the pokemon at the random index to the opponent team array list
            opponentTeam.add(addedPokemon);
        }
        
        // Set the current pokemon of the trainer and opponent to their starter pokemon
        trainerCurrentPokemon = trainerStarter;
        opponentCurrentPokemon = opponentStarter;
        
        // Set starter pokemon in trainer object 
        trainer.setStarterPokemon(trainerStarter);
        opponent.setStarterPokemon(opponentStarter);
       
        // Reset Pokemon HP 
        int startingHP;
        
        // For each pokemon in the trainer team
        for (Pokemon pokemon : trainer.getTeam()) 
        {
            // Get original HP
            startingHP = pokemon.getOriginalHP();
            
            // Set original HP as the starting HP of the pokemon
            pokemon.setHP(startingHP);
        }
        
        // For each pokemon in the opponent team
        for (Pokemon pokemon : opponent.getTeam()) 
        {
            // Get original HP
            startingHP = pokemon.getOriginalHP();
            
            // Set original HP as the starting HP of the pokemon
            pokemon.setHP(startingHP);
        }
    }
    
    public void setPokemons(Pokemon[] pokemons) 
    {
        this.pokemons = pokemons;
    }
    
    public Pokemon[] getPokemons() 
    {
        return this.pokemons;
    }
}
