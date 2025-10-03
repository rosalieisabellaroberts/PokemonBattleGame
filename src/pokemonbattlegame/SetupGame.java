/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import com.google.gson.Gson;
import java.io.*;
import java.util.*;

/**
 *
 * @author dilro
 */
    public class SetupGame
    {
        private Pokemon[] pokemons;
        private Trainer trainer;

        public Trainer run() 
        {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Professor Oak: Hello there! Welcome to the world of Pokemon! "
                    + "My name is Oak! People call me the Pokemon Prof! This world "
                    + "is inhabited by creatures called Pokemon! For some people, "
                    + "Pokemon are pets. Others use them for fights. Myself... I study "
                    + "Pokemon as a profession.");
            System.out.println("Professor Oak: What is your username, Trainer? ");

            String username = scanner.nextLine().trim();

            Trainer[] trainers = new Trainer[0];
            File trainersFile = new File("Trainers.json");

            try 
            {
                if (trainersFile.exists()) 
                {
                    try (BufferedReader trainersReader = new BufferedReader(new FileReader(trainersFile))) 
                    {
                        trainers = new Gson().fromJson(trainersReader, Trainer[].class);
                        if (trainers == null) 
                        {
                            trainers = new Trainer[0]; // if file is empty
                        }
                    }
                } else 
                {
                    // Create empty Trainers.json if not found
                    trainersFile.createNewFile();
                    try (FileWriter writer = new FileWriter(trainersFile)) 
                    {
                        writer.write("[]"); // start with empty array
                    }
                }
            } catch (IOException e) 
            {
                System.out.println("Professor Oak: Oooops... couldn't load trainers: " + e.getMessage());
            }           

            // Load Pokémon
            boolean loadedPokemon = false;

            // Try loading from working directory first
            File pokemonFile = new File("Pokemon.json");
            if (pokemonFile.exists()) 
            {
                try (BufferedReader pokemonReader = new BufferedReader(new FileReader(pokemonFile))) 
                {
                    this.pokemons = new Gson().fromJson(pokemonReader, Pokemon[].class);
                    loadedPokemon = true;
                }
                catch (IOException e)
                {
                    System.out.println("Professor Oak: Oooops... couldn't load Pokémon file: " + e.getMessage());
                }
            }

            // If not found in working directory, try loading as resource
            if (!loadedPokemon) 
            {
                InputStream pokemonStream = getClass().getResourceAsStream("/pokemonbattlegame/Pokemon.json");
                if (pokemonStream != null) 
                {
                    try (BufferedReader pokemonReader = new BufferedReader(new InputStreamReader(pokemonStream))) 
                    {
                        this.pokemons = new Gson().fromJson(pokemonReader, Pokemon[].class);
                        loadedPokemon = true;
                    }
                    catch (IOException e)
                    {
                    System.out.println("Professor Oak: Oooops... couldn't load Pokémon file: " + e.getMessage());
                    }
                }
            }

            // If still not loaded, return error message
            if (!loadedPokemon) 
            {
                System.out.println("Professor Oak: No Pokémon file found!");
                wait(2);
            }
           
            GeneratePokemonTeams teamGenerator = new GeneratePokemonTeams();
            teamGenerator.setPokemons(this.pokemons);
            
            boolean found = false;

            for (Trainer t : trainers) 
            {
                if (t.getUsername().equals(username)) 
                {
                    found = true;
                    this.trainer = t;
                    System.out.println("Professor Oak: Welcome back " + t.getName() + "! Your score is: " + t.getScore());
                    break;
                }
            }

            if (!found) 
            {
                try 
                {
                    createNewTrainer(username, scanner);
                }
                catch (IOException e) 
                {
                    System.out.println("Professor Oak: Oooops..." + e.getMessage());
                }
            }
            return this.trainer;
    }

    private void createNewTrainer(String username, Scanner scanner) throws IOException 
    {
        System.out.println("Professor Oak: You must be new around here! What is your name?");
        String name = scanner.nextLine().trim();

        this.trainer = new Trainer(username, name, "");
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
                        System.out.println("Professor Oak: Cool choice. Squirtle is truly devoted and fights with style!");
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

                for (Pokemon p : pokemons) 
                {
                    if (p.getName().equals(chosenPokemon)) 
                    {
                        this.trainer.setStarterPokemon(p);
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

    

