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
        PokemonBattleGame game = new PokemonBattleGame();
        
        // Check if user exists, load user data or create new user
        game.setupGame();
        
        // Prompt user for start 
        game.startBattle();
            
        // Randomly generate an opponent
        game.generateOpponent();
            
        // Randomly generate pokemon teams 
        game.generatePokemonTeams();

        // Display game details 
        game.displayGameDetails();
        
        // Enter battle loop 
        game.runBattle();
        
    }
    
    public void setupGame() throws InterruptedException
    {
         // Prompt user for username 
        Scanner scanner = new Scanner(System.in);
        System.out.println("Professor Oak: Hello there! Welcome to the world of Pokemon! "
                + "My name is Oak! People call me the Pokemon Prof! This world "
                + "is inhabited by creatures called Pokemon! For some people, "
                + "Pokemon are pets. Others use them for fights. Myself... I study "
                + "Pokemon as a profession.");
        System.out.println("Professor Oak: What is your username, Trainer? ");
        
        // Check if user exists in 'Trainers.json'
        String username  = scanner.nextLine().trim();    
       
       // Open 'Trainers.json' in the working directory 
       File trainersFile = new File("Trainers.json");
       
       // Create an array to hold existing trainers
       Trainer[] trainers;
       
       if(trainersFile.exists())
       {
          
            // Open 'Pokemon.json'
            InputStream inputStreamPokemon = PokemonBattleGame.class.getResourceAsStream("Pokemon.json");
       
            // Opens file 'Trainers.json' and then wraps the file reader in a buffered reader for reading the file in chunks
            try (BufferedReader trainersReader = new BufferedReader(new FileReader(trainersFile));
                BufferedReader pokemonReader = new BufferedReader(new InputStreamReader(inputStreamPokemon))) {
            
                // Check files are found (stream is not null), to avoid NullPointerException
                if (inputStreamPokemon == null) 
                {
                    System.out.println("Professor Oak: Oh no! I lost the pokemon files!");
                    Thread.sleep(2000);
                    System.out.println("Professor Oak: They must be around here somewhere...");
                    return;
                }

                // Read JSON data from trainesReader and convert it into an array of Trainer objects
                trainers = new Gson().fromJson(trainersReader, Trainer[].class);
                
                // Read JSON data from pokemonReader and convert it into an array of Pokemon objects
                this.pokemons = new Gson().fromJson(pokemonReader, Pokemon[].class);
           
                boolean found = false;
                
                // For every trainer in the trainers array
                for (Trainer trainer : trainers)
                {
                    // If the user's username matches with an existing Trainer's username
                    if (trainer.getUsername().equals(username))                      
                    { 
                        // Set found to true
                        found = true;
                        this.trainer = trainer;
                        // Parse score associated with username found 
                        int score = this.trainer.getScore();
                        // Print out Trainer name and score
                        System.out.println("Professor Oak: Welcome back " + trainer.getName() + "! Your score is: " + score);
                        break;
                    }
                }

                // If the user's username is not found
                if (!found)
                {   
                    // Prompt user for name
                    System.out.println("Professor Oak: You must be new around here! What is your name?"); 
                    String name = scanner.nextLine().trim(); 
                    String challengeMessage = "";
                    
                    // Create new trainer
                    this.trainer = new Trainer(username, name, challengeMessage);
                        
                    // Prompt user for starter pokemon
                    System.out.println("Professor Oak: Ah, splendid! A fine name indeed.");
                    // Give 2 second delay in between each message
                    Thread.sleep(2000);
                    System.out.println("Now, every great journey begins with a choice.");
                    Thread.sleep(2000);
                    System.out.println("In my lab, I have three Pokemon waiting to meet their first partner.");
                    Thread.sleep(2000);
                    System.out.println("Choose wisely, for this bond will shape your path ahead."); 
                    Thread.sleep(2000);
                    
                    boolean pokemonChosen = false;
                    int selection;
                    
                    // While a starterPokemon has not been chosen
                    while (pokemonChosen == false)
                    {
                        System.out.println("\n(1) Bulbasaur: A grass-type pokemon with a gentle yet resilient spirit");
                        Thread.sleep(2000); 
                        System.out.println("(2) Charmander: A fire-type pokemon that is both passionate and determined");
                        Thread.sleep(2000);
                        System.out.println("(3) Squirtle: A water-type pokemon with a playful and loyal heart");
                        Thread.sleep(2000);
                        System.out.println("(4) None: I just don't feel a connection to any of these...\n");
                        Thread.sleep(2000);
                        System.out.println("Professor Oak: Which Pokemon will you choose? Enter a digit between 1-4...");
                    
                        // Error handling for user input
                        try
                        {
                            // Set user selection as next token scanned in 
                            selection = Integer.parseInt(scanner.nextLine().trim());
                    
                            if (selection == 1)
                            {
                                System.out.println("Professor Oak: Excellent choice! Bulbasaur will be a kind and reliable companion.");
             
                                // For each pokemon in 'Pokemon.json'
                                for (Pokemon pokemon : pokemons)
                                {
                                    // If the pokemon name is Bulbasaur
                                    if (pokemon.getName().equals("Bulbasaur"))
                                    {
                                        // Assign the starter pokemon of the new trainer to Bulbasaur
                                        this.trainer.setStarterPokemon(pokemon);
                                        break;
                                    }
                                }
                                pokemonChosen = true;
                            }
                            else if (selection == 2)
                            {
                                System.out.println("Professor Oak: Ah, Charmander! A perfect choice for a bold Trainer!");
                                    
                                // For each pokemon in 'Pokemon.json'
                                for (Pokemon pokemon : pokemons)
                                {
                                    // If the pokemon name is Charmander
                                    if (pokemon.getName().equals("Charmander"))
                                    {
                                        // Assign the starter pokemon of the new trainer to Charmander
                                        this.trainer.setStarterPokemon(pokemon);
                                        break;
                                    }
                                }
                                pokemonChosen = true;
                            }
                            else if (selection == 3)
                            {
                                System.out.println("Professor Oak: Squirtle, eh? Smart choice. Cool, calm and ready for battle!");
                                    
                                // For each pokemon in 'Pokemon.json'
                                for (Pokemon pokemon : pokemons)
                                {
                                    // If the pokemon name is Squirtle
                                    if (pokemon.getName().equals("Squirtle"))
                                    {
                                        // Assign the starter pokemon of the new trainer to Squirtle
                                        this.trainer.setStarterPokemon(pokemon);
                                        break;
                                    }
                                }
                                pokemonChosen = true;
                            }
                            else if (selection == 4)
                            {
                                System.out.println("Professor Oak: Hmmm... It seems like all the other Pokemon have already been taken by Trainers who arrived earlier.");
                                Thread.sleep(2000);
                                System.out.println("Wait a second! I do have one more Pokemon left.");
                                Thread.sleep(2000);
                                System.out.println("But I must warn you-");
                                Thread.sleep(2000);
                                System.out.println("It doesn't like to stay inside its Poke Ball, and it can be very... difficult to train.");
                                Thread.sleep(2000);
                                System.out.println("\n(1) Pikachu: An electric-type pokemon with quick reflexes and a feisty attitude\n");
                                Thread.sleep(2000);
                                System.out.println("Professor Oak: Enter '1' to choose Pikachu OR press any other key to select a previous Pokemon...");

                                selection = Integer.parseInt(scanner.nextLine().trim());

                                if (selection == 1)
                                {
                                    System.out.println("Professor Oak: So you really want Pikachu, eh? Do not fear! With great patience and trust, Pikachu "
                                            + "might just become your greatest partner. Besides... I once knew a boy named Ash who tamed his Pikachu!");

                                    // For each pokemon in 'Pokemon.json'
                                    for (Pokemon pokemon : pokemons)
                                    {
                                        // If the pokemon name is Pikachu
                                        if (pokemon.getName().equals("Pikachu"))
                                        {
                                            // Assign the starter pokemon of the new trainer to Pikachu
                                            this.trainer.setStarterPokemon(pokemon);
                                            break;
                                        }
                                    }
                                    pokemonChosen = true;
                                }
                            } 
                            System.out.println("Professor Oak: Now. Every trainer needs their battle challenge line!");
                            Thread.sleep(2000);
                            System.out.println("What will yours be?");
                            challengeMessage = scanner.nextLine().trim();
                            this.trainer.setChallengeMessage(challengeMessage);
                            appendNewTrainer();
                        }
                        // If number isn't entered (can't be converted into a string), return eror message
                        catch (NumberFormatException e)
                        {
                            System.out.println("Professor Oak: That doesn't look like a number, Trainer. Enter a digit between 1-4.");         
                        }
                    }    
                } 
            }
            // If file 'Trainers.json' is not found, return error message
            catch (FileNotFoundException e)
            {
                System.out.println("Professor Oak: Oooops..." + e.getMessage());
            }
            // If input/output operations fail, return error message
            catch (IOException e)
            {
                System.out.println("Professor Oak: Oooops..." + e.getMessage());
            }  
        }   
    }
    
    public void appendNewTrainer()
    {
        // Create a GSON object to perform conversions from JSON to list of trainer objects and vice versa
        Gson gson = new Gson();
        
        // Creates a new list of trainer objects that can grow dynamically (be appended to)
        List<Trainer> trainers = new ArrayList<>();
        
        // Trainers.json file for reading and writing to
        File file = new File("Trainers.json");
        
        // If the file exists
        if (file.exists())
        {
            // Create a new file reader object that can read 'Trainers.json' 
            try (FileReader reader = new FileReader(file))
            {
                // Extract type of list, for converting JSON into a list containing trainer objects
                Type listType = new TypeToken<List<Trainer>>() {}.getType();
                
                // Read JSON from 'Trainers.json' and convert to a list of trainer objects 
                trainers = gson.fromJson(reader, listType);
                
                // If there are no trainers in 'Trainers.json', create a new empty list of Trainer objects
                if (trainers == null) trainers = new ArrayList<>();
            }
            // If input/output operations fail, return error message
            catch (IOException e)
            {
                System.out.println("Professor Oak: Oooops..." + e.getMessage());
            }
        }
        trainers.add(this.trainer);
        
        // Create a new filewriter object that can write to 'Trainers.json'
        try (FileWriter writer = new FileWriter(file))
        {
            // Convert the list of trainer objects to JSON and write JSON to 'Trainers.json'
            gson.toJson(trainers, writer);
        }
        // If input/output operations fail, return error message
        catch (IOException e)
        {
            System.out.println("Professor Oak: Oooops..." + e.getMessage());
        }
    }
    
    public void startBattle() throws InterruptedException
    {
            System.out.println("Professor Oaks: It is now time for you and " + this.trainer.getStarterPokemon().getName() + " to"
                    + " journey into the magical world of Pokemon!\n");
            Thread.sleep(2000);
            // Prompt user for start of new battle
            System.out.println("When you are ready, press any key to start...");

            // Open scanner to register user input
            Scanner scanner = new Scanner (System.in);
            scanner.nextLine();
    }
    
    public void generateOpponent() throws InterruptedException
    {
        // Open 'Trainers.json'
        InputStream inputStreamTrainers = PokemonBattleGame.class.getResourceAsStream("Trainers.json");
        
        // Wraps the file reader in a buffered reader for reading the file in chunks
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamTrainers)))
        {
            // Create a GSON object to perform conversions from JSON to list of trainer objects and vice versa
            Gson gson = new Gson();
      
            // Extract type of list, for converting JSON into a list containing trainer objects
            Type listType = new TypeToken<List<Trainer>>() {}.getType();
            
            // Read JSON from 'Trainers.json' and convert to a list of trainer objects 
            List<Trainer> trainers = gson.fromJson(reader, listType);
                
            // Remove current user from list of possible Trainers to battle
            for (Iterator<Trainer> currentTrainer = trainers.iterator(); currentTrainer.hasNext();) 
            {
                // Get next trainer in the list 
                Trainer nextTrainer = currentTrainer.next();
                if (nextTrainer.getUsername().equals(this.trainer.getUsername()))
                {
                    currentTrainer.remove();
                }
            }
      
            // Create Random object for selecting a random Trainer    
            Random random = new Random();
            this.opponent = trainers.get(random.nextInt(trainers.size()));
            
            System.out.println(opponent.getName() + ": " + opponent.getChallengeMessage());
            Thread.sleep(2000);   
        }
        // If file 'Trainers.json' is not found, return error message
        catch (FileNotFoundException e)
        {
            System.out.println("Professor Oak: Oooops..." + e.getMessage());
        }
        // If input/output operations fail, return error message
        catch (IOException e)
        {
            System.out.println("Professor Oak: Oooops..." + e.getMessage());
        }
    }
    
    public void generatePokemonTeams()
    {
        
        // Create an array list to new pokemon team for the user
        ArrayList<Pokemon> trainerTeam = this.trainer.getTeam();
        
        // Create an array list to new pokemon team for the opponent
        ArrayList<Pokemon> opponentTeam = this.opponent.getTeam();
        
        // Create a new random object
        Random number = new Random();
        
        // Set starter pokemon as first pokemon in team for trainer and opponent
        Pokemon trainerStarter = this.trainer.getStarterPokemon();
        Pokemon opponentStarter = this.opponent.getStarterPokemon();
        
        if(!trainerTeam.contains(trainerStarter))
        {
            // Add the trainers starter pokemon at index 0
            trainerTeam.add(0, trainerStarter);
            // Initalise original HP of the trainer's starter pokemon
            trainerStarter.initaliseOriginalHP();
        }
        
        if(!opponentTeam.contains(opponentStarter))
        {
            // Add the opponents starter pokemon at index 0
            opponentTeam.add(0, opponentStarter);
            // Initalise original HP of the opponent's starter pokemon
            opponentStarter.initaliseOriginalHP();
        }
        
        Pokemon addedPokemon;
        
        // LOOP (x5) generate random pokemon and add to pokemon team array for trainer
        while (trainerTeam.size() < 6)
        {
            // Generate a random int within the size of the pokemon array
            int randomIndex = number.nextInt(pokemons.length);
            
            // Set added pokemon to the pokemon in the array list at the random index
            addedPokemon = pokemons[randomIndex];
            
            // Initalise original HP of addedPokemon
            addedPokemon.initaliseOriginalHP();
            
            // Add the pokemon at the random index to the trainer team array list
            trainerTeam.add(addedPokemon);
        }
        
        // LOOP (x5) generate random pokemon and add to pokemon team array for opponent
        while (opponentTeam.size() < 6)
        {
            // Generate a random int within the size of the pokemon array
            int randomIndex = number.nextInt(pokemons.length);
            
            // Set added pokemon to the pokemon in the array list at the random index
            addedPokemon = pokemons[randomIndex];
            
            // Initalise original HP of addedPokemon
            addedPokemon.initaliseOriginalHP();
            
            // Add the pokemon at the random index to the opponent team array list
            opponentTeam.add(addedPokemon);
        }
        
        // Set the current pokemon of the trainer and opponent to their starter pokemon
        trainerCurrentPokemon = this.trainer.getStarterPokemon();
        opponentCurrentPokemon = this.opponent.getStarterPokemon();
       
        // Reset Pokemon HP 
        int startingHP;
        
        // For each pokemon in the trainer team
        for (Pokemon pokemon : this.trainer.getTeam()) 
        {
            // Get original HP
            startingHP = pokemon.getOriginalHP();
            
            // Set original HP as the starting HP of the pokemon
            pokemon.setHP(startingHP);
        }
        
        // For each pokemon in the opponent team
        for (Pokemon pokemon : this.opponent.getTeam()) 
        {
            // Get original HP
            startingHP = pokemon.getOriginalHP();
            
            // Set original HP as the starting HP of the pokemon
            pokemon.setHP(startingHP);
        }
    }

    public void displayGameDetails()
    {
        // Display opponent details 
        System.out.println("\nOPPONENT: "+this.opponent.getName());
        System.out.println("LEVEL: "+this.opponent.getLevel());
        System.out.print("TEAM: ");
        int orderNumber = 1;
        for (Pokemon pokemon : opponent.getTeam()) 
        {
        System.out.print("("+orderNumber+ ") " + pokemon.getName() + " ");
        orderNumber++;
        }
        System.out.println("\nCURRENT POKEMON: "+this.opponent.getStarterPokemon().getName());
        System.out.println("HP: "+this.opponent.getStarterPokemon().getHP()+"/"+this.opponent.getStarterPokemon().getOriginalHP()); 
        
        // Display options here
        System.out.println("_____________________________________________");
        
        // Display user details 
        System.out.println("TRAINER: "+this.trainer.getName());
        System.out.println("LEVEL: "+this.trainer.getLevel());
        System.out.print("TEAM: ");
        orderNumber = 1;
        for (Pokemon pokemon : trainer.getTeam()) 
        {
        System.out.print("("+orderNumber+ ") " + pokemon.getName() + " ");
        orderNumber++;
        }
        System.out.println("\nCURRENT POKEMON: "+this.trainer.getStarterPokemon().getName());
        System.out.println("HP: "+this.trainer.getStarterPokemon().getHP()+"/"+this.trainer.getStarterPokemon().getOriginalHP());   
    } 
    
    public void runBattle() throws InterruptedException
    {
        boolean trainerWon = false;
        boolean gameFinished = false;
        boolean trainersTurn = true;
        boolean ranAway = false;
        int orderNumber = 1;
        int selection;
        int moveSelection;
        double randomAccuracy;
        int startingScore = trainer.getScore();
        Pokemon savedStarter = trainer.getStarterPokemon();
   
        // Create a new scanner object
        Scanner scanner = new Scanner(System.in);
        
        // Create a new random object
        Random random = new Random();
        
        // Create Battle Loop involving trainer turn and opponent turn 
        while (gameFinished == false)
        {
            // Trainer's turn 
            while (trainersTurn == true)
            {           
                if (gameFinished) break;
                
                // Display user options 
                System.out.println("\n(1) FIGHT (2) SWITCH POKEMON (3) RUN");
                System.out.println("What will you do? Enter a digit between 1-3...");
                
                try 
                {
                    selection = scanner.nextInt();
                     
                     // If 1 is selected
                    switch (selection)
                    {
                        case 1:
                            // Reset orderNumber to 1
                            orderNumber = 1;
                            // Display moves of the current pokemon 
                            System.out.print("MOVES: ");
                            
                            // Create an array of all the moves for the currentPokemon
                            ArrayList<Move> moves = trainerCurrentPokemon.getMoves();
                            
                            // Print out the name of each move in the move array
                            for (Move move : moves) 
                            {
                            System.out.print("(" + orderNumber + ") " + move.getName() + " ");
                            orderNumber++;
                            }
                            
                            // Get user input for chosen move 
                            System.out.println("\nWhich will you use? Enter a digit...");
                            moveSelection = scanner.nextInt();
                            
                            // Get selected move from moves array (adjust by 1 to get correct index) 
                            Move selectedMove = moves.get(moveSelection - 1);
                            
                            // Print out the selected move used 
                            System.out.println("\n" + trainerCurrentPokemon.getName()+" used "+ selectedMove.getName()+"!");
                            Thread.sleep(2000);
                            
                            // Check accuracy of move 
                            randomAccuracy = random.nextDouble() * 100;
                                    
                            // If random accuracy is less than or equal to the move accuracy, it hits
                            if (randomAccuracy <= selectedMove.getAccuracy())
                            {
                                pokemonbattlegame.Type atkType = trainerCurrentPokemon.getPokemonType();
                                pokemonbattlegame.Type defType = opponentCurrentPokemon.getPokemonType();
                                
                                // Apply type multiplier 
                                double typeMult = 1.0;
                                if (isSuperEffective(atkType, defType))
                                {
                                    typeMult = 2.0;
                                    Thread.sleep(2000);
                                    System.out.println("It's super effective!");
                                } else if (isNotVeryEffective(atkType, defType))
                                {
                                    typeMult = 0.5;
                                    Thread.sleep(2000);
                                    System.out.println("It's not very effective...");
                                }
                                
                                //crit and random Factor
                                boolean crit = random.nextDouble() < 0.0625;
                                double critMult = crit ? 1.5 : 1.0;
                                double randMult = 0.85 + (random.nextDouble() * 0.15);
                                
                                int dmg = (int) Math.floor(selectedMove.getPower()* typeMult * critMult* randMult);
                                if (dmg < 1) dmg = 1;
                                if (crit) System.out.println("A critical hit!");
                                
                                int newHP = Math.max(0, opponentCurrentPokemon.getHP() - dmg);
                                opponentCurrentPokemon.setHP(newHP);
                                Thread.sleep(2000);
                                System.out.println("It dealt " + dmg + " damage.");
                                Thread.sleep(2000);
                                
                                
                                // If Pokemon HP is reduced to 0 or less
                                if (opponentCurrentPokemon.getHP() <= 0)
                                {
                                    System.out.println(opponentCurrentPokemon.getName() + " fainted!");
                                    // TO DO: Update current opponent pokemon
                                    boolean oppSwitched = false;
                                    for (Pokemon p:opponent.getTeam())
                                    {
                                        if (p.getHP()>0)
                                        {
                                            opponentCurrentPokemon = p;
                                            try { opponent.setStarterPokemon(p);} catch(Throwable ignored){}
                                            System.out.println(opponent.getName() + " sent out " + p.getName() + "!");
                                            oppSwitched = true;
                                            break;
                                        }
                                    }
                                    // TO DO: If no pokemon remaining in team with HP, set trainerWon to true & exit battle
                                    if (!oppSwitched)
                                    {
                                        trainerWon = true;
                                        gameFinished = true;
                                        break;
                                    }
                                }
                                else
                                {
                                    // TO DO: Display updated details using displayGameDetails(), and set trainersTurn to false
                                    displayGameDetails();
                                    trainersTurn = false;
                                }
                            }
                            else
                            {
                                System.out.println(trainerCurrentPokemon.getName() + " missed!");
                                // Switch to opponents turn
                                trainersTurn = false;
                            }

                            break;
                        
                        case 2:
                            // TO DO: If 2 is selected
                                // Display pokemon in team with HP greater than 0
                            orderNumber = 1;
                            System.out.print("YOUR TEAM: ");
                            for (Pokemon p:trainer.getTeam())
                            {
                                String tag = (p.getHP() > 0)? "" : " [FNT]";
                                System.out.print("(" + orderNumber + ") " + p.getName() + tag + " ");
                                orderNumber++;
                            }
                                // Prompt user to select new pokemon or return 
                                // If returned, display user options again
                            System.out.println("\nChoose a Pokemon (1-" + trainer.getTeam().size() + ") or 0 to cancel:");
                            int switchChoice = scanner.nextInt();
                            if (switchChoice == 0)
                                break;
                            
                            if (switchChoice < 1 || switchChoice > trainer.getTeam().size())
                            {
                                System.out.println("Invalid index.");
                                break;
                            }
                            
                            Pokemon chosen = trainer.getTeam().get(switchChoice - 1);
                            if (chosen.getHP() <= 0)
                            {
                                System.out.println(chosen.getName() + " has fainted. Choose another.");
                                break;
                            }
                            if (chosen == trainerCurrentPokemon)
                            {
                                System.out.println(chosen.getName() + " is already active.");
                                break;
                            }
                            
                            trainerCurrentPokemon = chosen;
                            try
                            {
                                trainer.setStarterPokemon(chosen);
                            } catch (Throwable ignored) {}
                            System.out.println("Go, " + chosen.getName()+ "!");
                            
                            //passing the turn to opponent
                            displayGameDetails();
                            trainersTurn = false;
                            break;
                           
                        
                        case 3: 
                            // TO DO: If 3 is selected
                                // Display message 
                            Thread.sleep(2000);  
                            System.out.println("You ran away! The battle ends.");
                            ranAway = true;
                            gameFinished = true;
                            trainersTurn = false;
                            // Exit battle 
                            break;
                                
                            
                        
                    }   
                }
                // If number isn't entered (can't be converted into a string), return eror message
                catch (NumberFormatException e)
                {
                    System.out.println("Enter a digit between 1-3.");         
                }   
            }  
            
            // Opponents's turn 
            while (trainersTurn == false)
            {
                if (gameFinished) break;
                // TO DO: Functionality for trainer's turn (copy, paste and adjust slight details from users loop above
                ArrayList<Move> oppMoves = opponentCurrentPokemon.getMoves();
                if(oppMoves == null || oppMoves.isEmpty())
                {
                    Thread.sleep(2000);  
                    System.out.println(opponentCurrentPokemon.getName() + " has no usable moves!");
                    trainersTurn = true;
                    continue;
                }
                
                Move oppMove = oppMoves.get(random.nextInt(oppMoves.size()));
                
                Thread.sleep(2000);  
                System.out.println("\n"+opponentCurrentPokemon.getName() + " used " + oppMove.getName() + "!");
                Thread.sleep(2000);  
                
                //accuracy check
                randomAccuracy = random.nextDouble()*100;
                if (randomAccuracy <= oppMove.getAccuracy())
                {
                    pokemonbattlegame.Type atkType = opponentCurrentPokemon.getPokemonType();
                    pokemonbattlegame.Type defType = trainerCurrentPokemon.getPokemonType();
                    
                    //added multipliers 
                    double typeMult = 1.0;
                    if (isSuperEffective(atkType, defType))
                    {
                        typeMult = 2.0;
                        Thread.sleep(2000);  
                        System.out.println("It's super effective!");
                    } else if (isNotVeryEffective(atkType, defType))
                    {
                        typeMult = 0.5;
                        Thread.sleep(2000);  
                        System.out.println("It's not very effective...");
                    }
                    
                    //crit and random factor
                    boolean crit = random.nextDouble() < 0.0625; //6.25%
                    double critMult = crit ? 1.5 : 1.0;
                    double randMult = 0.85 + (random.nextDouble()*0.15);
                    
                    int dmg = (int) Math.floor(oppMove.getPower() *typeMult * critMult * randMult);
                    if (dmg < 1) dmg = 1;
                    Thread.sleep(2000);  
                    if (crit) System.out.println("A critical hit!");
                    
                    int newHP = Math.max(0, trainerCurrentPokemon.getHP() - dmg);
                    trainerCurrentPokemon.setHP(newHP); 
                    System.out.println("It dealt " +dmg+ " damage.");
                    Thread.sleep(2000); 
                    
                    //if fainted
                    if (trainerCurrentPokemon.getHP() <= 0)
                    {
                        Thread.sleep(2000);  
                        System.out.println(trainerCurrentPokemon.getName() + " fainted!");
                        boolean playerSwitched = false;
                        for (Pokemon p:trainer.getTeam())
                        {
                            if (p.getHP() > 0)
                            {
                                trainerCurrentPokemon = p;
                                try
                                {
                                    trainer.setStarterPokemon(p);
                                } catch (Throwable ignored) {}
                                System.out.println("You sent out " + p.getName() + "!");
                                playerSwitched = true;
                                break;
                            }
                        }
                        if (!playerSwitched)
                        {
                            //no pokemon left on your side then opponent wins
                            trainerWon = false;
                            gameFinished = true;
                        } else
                        {
                            //return to player
                            displayGameDetails();
                            trainersTurn = true;
                        }
                    } else
                    {
                        displayGameDetails();
                        trainersTurn = true;
                    }
                }else
                {
                    Thread.sleep(2000);  
                    System.out.println(opponentCurrentPokemon.getName() + " missed!");
                    trainersTurn = true;
                }
            }
        }  
        
        // TO DO: Update user's score & level in Trainers.json
        try
        {
            trainer.setStarterPokemon(savedStarter);
        } catch (Throwable ignored) {}
        
        if (!ranAway)
        {
            if(trainerWon)
            {
                trainer.setScore(trainer.getScore() + 100);
                System.out.println("You win! +100 score. New score: " + trainer.getScore());
            } else
            {
                trainer.setScore(trainer.getScore()-50);
                System.out.println("You lost... -50 score. New score: "+ trainer.getScore());
            }
        } else
        {
            System.out.println("No score change for running.");
        }
        
        //saving tainer back to trainers.json
        //some help from chatgpt on this part
        try
        {
            Gson gson = new Gson();
            File file = new File("Trainers.json");
            java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<java.util.List<Trainer>>() {}.getType();
            java.util.List<Trainer> trainersList = new java.util.ArrayList<>();
            
            if (file.exists())
            {
                try (FileReader reader = new FileReader(file))
                {
                    java.util.List<Trainer> fromFile = gson.fromJson(reader, listType);
                    if (fromFile != null) trainersList = fromFile;
                }
            }
            
            boolean replaced = false;
            for (int i = 0; i < trainersList.size(); i++)
            {
                if (trainersList.get(i).getUsername().equals(trainer.getUsername()))
                {
                    trainersList.set(i, trainer);
                    replaced = true;
                    break;
                }
            }
            if (!replaced) trainersList.add(trainer);
            try (FileWriter writer = new FileWriter(file))
            {
                gson.toJson(trainersList, writer);
            }
            System.out.println("Progress saved.");
        } catch (Exception e)
        {
            System.out.println("[ERROR] Could not update save file: " + e.getMessage());
        }
        // TO DO: Evolution check & evolution of starter pokemon (if applicable)
        //evolution check
        int prevStage = stageFromScore(startingScore);
        int newStage = stageFromScore(trainer.getScore());
        
        if(prevStage < 2 && newStage >= 2)
        {
            try
            {
                playEvolutionSequence(trainer.getStarterPokemon(), 2);
            } catch(InterruptedException ignored) {}
        }
        if (prevStage < 3 && newStage >= 3)
        {
            try
            {
                playEvolutionSequence(trainer.getStarterPokemon(), 3);
            } catch(InterruptedException ignored) {}
        }
                
        // TO DO: Prompt user to start another battle or exit game 
        System.out.println("\nStart another battle? (y/n)");
        scanner.nextLine();
        String again = scanner.nextLine().trim().toLowerCase();
        if (again.equals("y"))
        {
            try
            {
                generateOpponent();
                generatePokemonTeams();
                displayGameDetails();
                runBattle();
            } catch (InterruptedException ex)
            {
                System.out.println("[ERROR] " + ex.getMessage());
            }
        } else
        {
            System.out.println("Thanks for playing!");
        }
    }
    
    public boolean isSuperEffective(pokemonbattlegame.Type attackType, pokemonbattlegame.Type defenderType)
    {
        for (String strongType : attackType.getStrongAgainst())
        {
            if (strongType.equalsIgnoreCase(defenderType.getName()))
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean isNotVeryEffective(pokemonbattlegame.Type attackType, pokemonbattlegame.Type defenderType)
    {
        for (String weakType : attackType.getWeakAgainst())
        {
            if (weakType.equalsIgnoreCase(defenderType.getName()))
            {
                return true;
            }
        }
        return false;
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
