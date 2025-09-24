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
            
        // Randomly generate pokemon teams 
        game.generatePokemonTeams();

        // Display game details 
        game.displayGameDetails();
        
        // Enter battle loop 
        game.runBattle();
        
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
                // Prompt user for start 
            this.startBattle();
            
            // Randomly generate an opponent
            try 
            {
                GenerateOpponent generatedOpponent = new GenerateOpponent();
                generatedOpponent.generateOpponent(this.getTrainer());
                this.setOpponent(generatedOpponent.getOpponent());
            } catch (InterruptedException e)
            {
                System.out.println("Error generating opponent!" + e.getMessage());
            }
            
            // Randomly generate pokemon teams 
            this.generatePokemonTeams();

            // Display game details 
            this.displayGameDetails();
        
            // Enter battle loop 
            this.runBattle();
                } catch (InterruptedException e)
                {
                    System.out.println("[ERROR] " + e.getMessage());
                }
            } else
            {
                System.out.println("Thanks for playing!");
            }
        }
    // Setter method for trainer
    public void setTrainer(Trainer trainer) 
    {
        this.trainer = trainer;
    }
    
    // Getter method for trainer
    public Trainer getTrainer() 
    {
        return trainer;
    }
    
    // Getter method for opponent
    public Trainer getOpponent() 
    {
        return opponent;
    }

    // Setter method for opponent
    public void setOpponent(Trainer opponent) 
    {
        this.opponent = opponent;
    }

    // Setter method for pokemons
    public void setPokemons(Pokemon[] pokemons) 
    {
        this.pokemons = pokemons;
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
