/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.util.*;
import java.sql.*;

/**
 *
 * @author dilro
 */
public class SetupGame
{
    private Pokemon[] pokemons;
    private Trainer trainer;

    public Trainer run(Connection connection) 
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Professor Oak: Hello there! Welcome to the world of Pokemon! "
                + "My name is Oak! People call me the Pokemon Prof! This world "
                + "is inhabited by creatures called Pokemon! For some people, "
                + "Pokemon are pets. Others use them for fights. Myself... I study "
                + "Pokemon as a profession.");
        System.out.println("Professor Oak: What is your username, Trainer? ");

        String username = scanner.nextLine().trim();

        // Try to load the trainer from the database 
        this.trainer = DatabaseManager.getTrainer(username, connection);

        // Load list of Pokemon objects from the database 
        List<Pokemon> pokemonList = DatabaseManager.getPokemonList();
            
        // If the list is empty, return an error message 
        if(pokemonList.isEmpty())
        {
            System.out.println("Professor Oak: I couldn't find any pokemon in this Pokedex!");
            wait(2);
        }
        // Populate the pokemons array with the values from the database pokemon list 
        this.pokemons = pokemonList.toArray(new Pokemon[0]);
            
        // If trainer exists 
        if (this.trainer != null) 
        {
            // Print welcome message 
            System.out.println("Professor Oak: Welcome back " + trainer.getName() + "! Your score is: " + trainer.getScore());
            return this.trainer;
        }
        
        // If trainer doesn't exist, create a new one 
        else
        {
            try 
            {
                createNewTrainer(username, scanner);
                   
                // Save new trainer into the database
                SaveManager.saveTrainer(this.trainer, connection);
                   
            } catch (Exception e)
            {
                System.out.println("Professor Oak: Oooops..." + e.getMessage());
                e.printStackTrace();
            }       
        }  
        
        return this.trainer;
        
    }

    private void createNewTrainer(String username, Scanner scanner)
    {
        System.out.println("Professor Oak: You must be new around here! What is your name?");
        String name = scanner.nextLine().trim();

        this.trainer = new Trainer(username, name, 0, 1, null, "");
        this.trainer.setTeam(new ArrayList<>());
        
        System.out.println("Professor Oak: Ah, splendid! A fine name indeed.");
        wait(2);
        System.out.println("Professor Oak: Now, every great journey begins with a choice...");
        wait(2);

        boolean pokemonChosen = false;
        boolean pikachuUnlocked = false;

        while (!pokemonChosen) 
        {
            if (!pikachuUnlocked)
            {
                System.out.println("\n(1) Bulbasaur: A steadfast grass-type pokemon that grows stronger with every challenge");
                wait(2);
                System.out.println("(2) Charmander: A spirited fire-type pokemon with passion that burns bright");
                wait(2);
                System.out.println("(3) Squirtle: A loyal water-type pokemon that stays cool under pressure");
                wait(2);
                System.out.println("(4) None of these will do...");
                wait(2);
                System.out.println("\nProfessor Oak: Which Pokemon will you choose? Enter a digit between 1-4...");
            }
            else 
            {
                System.out.println("\n(1) Bulbasaur: A steadfast grass-type pokemon that grows stronger with every challenge");
                wait(2);
                System.out.println("(2) Charmander: A spirited fire-type pokemon with passion that burns bright");
                wait(2);
                System.out.println("(3) Squirtle: A loyal water-type pokemon that stays cool under pressure");
                wait(2);
                System.out.println("(4) Pikachu: A speedy electric-type pokemon with a sparky attitude");
                wait(2);
                System.out.println("\nProfessor Oak: Which Pokemon will you choose? Enter a digit between 1-4...");
            }                
            try 
            {
                int selection = Integer.parseInt(scanner.nextLine().trim());
                String chosenPokemon = null;

                switch (selection) 
                {
                    case 1 -> 
                    {
                        System.out.println("Professor Oak: Wise choice. Bulbasaur will be a reliable partner in battle!");
                        chosenPokemon = "Bulbasaur";
                        wait(2);
                    }
                    case 2 -> 
                    {
                        System.out.println("Professor Oak: Bold choice. Charmander is fiercy determined and never backs down!");
                        chosenPokemon = "Charmander";
                        wait(2);
                    }
                    case 3 -> 
                    {
                        System.out.println("Professor Oak: Cool choice. Squirtle fights with style and will be a loyal companion!");
                        chosenPokemon = "Squirtle";
                        wait(2);
                    }
                    case 4 -> 
                    {
                        if (!pikachuUnlocked)
                        {
                            System.out.println("Professor Oak: Wait a second!");
                            wait(2);
                            System.out.println("Professor Oak: I do have another pokemon back at my lab, but I must warn you-");
                            wait(2);
                            System.out.println("Professor Oak: It's incredibly stubborn and almost impossible to train.");
                            wait(2);
                            System.out.println("Professor Oak: But if you persevere, it could be a truly powerful partner!");
                            wait(2);
                       
                            pikachuUnlocked = true;
                            continue;   
                        } else
                        {
                            System.out.println("Professor Oak: So you want Pikachu eh? Goodluck trainer...");
                            chosenPokemon = "Pikachu";
                            wait(2);
                        } 
                    }
                }

                if (chosenPokemon == null) 
                {
                    System.out.println("Professor Oak: It's time to choose your Pokemon trainer... Enter a digit between 1-4!");
                    continue;
                }

                // Find chosen pokemon from database loaded list 
                for (Pokemon pokemon : pokemons) 
                {
                    if (pokemon.getName().equals(chosenPokemon)) 
                    {
                        this.trainer.setStarterPokemon(pokemon);
                        break;
                    }
                }

                pokemonChosen = true;

                System.out.println("\nProfessor Oak: Now, what will be your challenge message?");
                String challengeMessage = scanner.nextLine().trim();
                this.trainer.setChallengeMessage(challengeMessage);

            } catch (NumberFormatException e) 
            {
                System.out.println("Professor Oak: That doesn't look like a number. Try again.");
            }
        }
    }

    public Pokemon[] getPokemons() 
    {
        return pokemons;
    }

    public Trainer getTrainer() 
    {
        return trainer;
    }

    public void wait(int seconds) 
    {
        try 
        {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) 
        {
            Thread.currentThread().interrupt();
        }
    }
}

    

