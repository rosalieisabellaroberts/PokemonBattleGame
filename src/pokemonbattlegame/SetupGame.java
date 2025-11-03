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
    private SetupGUI gui;
    private String lastUserInput;

    public Trainer run(Connection connection) 
    {
        printMessage("Professor Oak: Hello there! Welcome to the world of Pokemon!");
        printMessage("My name is Oak! People call me the Pokemon Professor!");
        wait(2);
        printMessage("This world is inhabited by creatures called Pokemon!"); 
        printMessage("For some people, Pokemon are pets. Others use them for fights."); 
        printMessage("Myself...I study Pokemon as a profession.");    
        printMessage("\nProfessor Oak: What is your username, Trainer? ");

        String username = getUserInput();

        // Try to load the trainer from the database 
        this.trainer = DatabaseManager.getTrainer(username, connection);

        // Load list of Pokemon objects from the database 
        List<Pokemon> pokemonList = DatabaseManager.getPokemonList();
            
        // If the list is empty, return an error message 
        if(pokemonList.isEmpty())
        {
            printMessage("Professor Oak: I couldn't find any pokemon in this Pokedex!");
            wait(2);
        }
        // Populate the pokemons array with the values from the database pokemon list 
        this.pokemons = pokemonList.toArray(new Pokemon[0]);
            
        // If trainer exists 
        if (this.trainer != null) 
        {
            // Print welcome message 
            printMessage("Professor Oak: Welcome back " + trainer.getName() + "! Your score is: " + trainer.getScore());
            return this.trainer;
        }
        
        // If trainer doesn't exist, create a new one 
        else
        {
            try 
            {
                createNewTrainer(username);
                   
                // Save new trainer into the database
                SaveManager.saveTrainer(this.trainer, connection);
                   
            } catch (Exception e)
            {
                printMessage("Professor Oak: Oooops..." + e.getMessage());
                e.printStackTrace();
            }       
        }  
        
        return this.trainer;
        
    }

    private void createNewTrainer(String username)
    {
        printMessage("Professor Oak: You must be new around here! What is your name?");
        String name = getUserInput();

        this.trainer = new Trainer(username, name, 0, 1, null, "", "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\trainer.png");
        this.trainer.setTeam(new ArrayList<>());
        
        printMessage("Professor Oak: Ah, splendid! A fine name indeed.");
        wait(2);
        printMessage("Professor Oak: Now, every great journey begins with a choice...");
        wait(2);

        boolean pokemonChosen = false;
        boolean pikachuUnlocked = false;

        while (!pokemonChosen) 
        {
            if (!pikachuUnlocked)
            {
                printMessage("\n(1) Bulbasaur: A steadfast grass-type pokemon that grows stronger with every challenge");
                revealPokemonPokeball(0, "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png");
                wait(2);
                printMessage("(2) Charmander: A spirited fire-type pokemon with passion that burns bright");
                revealPokemonPokeball(1, "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\charmander.png");
                wait(2);
                printMessage("(3) Squirtle: A loyal water-type pokemon that stays cool under pressure");
                revealPokemonPokeball(2, "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\squirtle.png");
                wait(2);
                printMessage("(4) None of these will do...");
                wait(2);
                printMessage("\nProfessor Oak: Which Pokemon will you choose? Enter a digit between 1-4...");
            }
            else 
            {
                printMessage("\n(1) Bulbasaur: A steadfast grass-type pokemon that grows stronger with every challenge");
                wait(2);
                printMessage("(2) Charmander: A spirited fire-type pokemon with passion that burns bright");
                wait(2);
                printMessage("(3) Squirtle: A loyal water-type pokemon that stays cool under pressure");
                wait(2);
                printMessage("(4) Pikachu: A speedy electric-type pokemon with a sparky attitude");
                revealPokemonPokeball(3, "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\pikachu.png");
                gui.enablePikachuImage();
                wait(2);
                printMessage("\nProfessor Oak: Which Pokemon will you choose? Enter a digit between 1-4...");
            }                
            try 
            {
                int selection = Integer.parseInt(getUserInput());
                String chosenPokemon = null;

                switch (selection) 
                {
                    case 1 -> 
                    {
                        printMessage("Professor Oak: Wise choice. Bulbasaur will be a reliable partner in battle!");
                        chosenPokemon = "Bulbasaur";
                        wait(2);
                    }
                    case 2 -> 
                    {
                        printMessage("Professor Oak: Bold choice. Charmander is fiercy determined and never backs down!");
                        chosenPokemon = "Charmander";
                        wait(2);
                    }
                    case 3 -> 
                    {
                        printMessage("Professor Oak: Cool choice. Squirtle fights with style and will be a loyal companion!");
                        chosenPokemon = "Squirtle";
                        wait(2);
                    }
                    case 4 -> 
                    {
                        if (!pikachuUnlocked)
                        {
                            printMessage("Professor Oak: Wait a second!");
                            wait(2);
                            printMessage("Professor Oak: I do have another pokemon back at my lab, but I must warn you-");
                            wait(2);
                            printMessage("Professor Oak: It's incredibly stubborn and almost impossible to train.");
                            wait(2);
                            printMessage("Professor Oak: But if you persevere, it could be a truly powerful partner!");
                            wait(2);
                       
                            pikachuUnlocked = true;
                            continue;   
                        } else
                        {
                            printMessage("Professor Oak: So you want Pikachu eh? Goodluck trainer...");
                            chosenPokemon = "Pikachu";
                            wait(2);
                        } 
                    }
                }

                if (chosenPokemon == null) 
                {
                    printMessage("Professor Oak: It's time to choose your Pokemon trainer... Enter a digit between 1-4!");
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

                printMessage("\nProfessor Oak: Now, what will be your challenge message?");
                String challengeMessage = getUserInput();
                this.trainer.setChallengeMessage(challengeMessage);

            } catch (NumberFormatException e) 
            {
                printMessage("Professor Oak: That doesn't look like a number. Try again.");
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

    private void printMessage(String message) 
    {
        // If GUI is connected, display message to GUI
        if (gui != null) 
        {
            gui.appendMessage(message);
        }
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
    
    // Getter and setter methods for GUI
    public void setGUI(SetupGUI gui) 
    {
        this.gui = gui;
        
        // Set input listener 
        gui.setInputListener(input ->
        {
            synchronized (SetupGame.this)
            {
                lastUserInput = input;
                
                // Notify getUserInput method to set the lastUserInput
                SetupGame.this.notify();
            }
        }); 
    }
    
    private String getUserInput()
    {
        synchronized(this)
        {
            // Reset previous input 
            lastUserInput = null;
            
            try
            {
                // Wait until input listener sets the lastUserInput
                while (lastUserInput == null)
                {
                    wait();
                }
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            return lastUserInput;
        }
    }
    
    // Update pokeball image in the GUI at a given index 
    private void revealPokemonPokeball(int index, String imagePath)
    {
        if (gui != null)
        {
            gui.setPokeballImage(index, imagePath);
        }
    }
}

    

