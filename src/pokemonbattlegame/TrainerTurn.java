
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

// Implements the BattleAction interface which declares the takeTurn() method and returns a BattleResult object
public class TrainerTurn extends Turn implements BattleAction 
{
    public TrainerTurn(Trainer trainer, Trainer opponent, 
                       Pokemon trainerCurrentPokemon, Pokemon opponentCurrentPokemon,
                       Scanner scanner, Random random) 
    {
        super(trainer, opponent, trainerCurrentPokemon, opponentCurrentPokemon, random);
        this.scanner = scanner;
    }

    @Override
    public BattleResult takeTurn() throws InterruptedException {
    boolean gameFinished = false;
    boolean trainerWon = false;
    boolean ranAway = false;

    int orderNumber;
    int moveSelection;
    double randomAccuracy;

    System.out.println("\n(1) FIGHT (2) SWITCH POKEMON (3) RUN");
    System.out.println("What will you do?");
    int selection = getValidInput(scanner, 1, 3, "Enter a digit between 1-3...");

    switch (selection) {
        case 1:
            orderNumber = 1;
            ArrayList<Move> moves = trainerCurrentPokemon.getMoves();

            System.out.print("MOVES: ");
            for (Move move : moves) {
                System.out.print("(" + orderNumber + ") " + move.getName() + " ");
                orderNumber++;
            }

            System.out.println("\nWhich will you use?");
            moveSelection = getValidInput(scanner, 1, moves.size(), "Enter a digit between 1-"+ moves.size()+"...");

            Move selectedMove = moves.get(moveSelection - 1);
            System.out.println("\n" + trainerCurrentPokemon.getName() + " used " + selectedMove.getName() + "!");
            Thread.sleep(2000);

            randomAccuracy = random.nextDouble() * 100;
            if (randomAccuracy <= selectedMove.getAccuracy()) {
                
                int damage = calculateDamage(selectedMove, trainerCurrentPokemon, opponentCurrentPokemon);
                
                opponentCurrentPokemon.setHP(Math.max(0, opponentCurrentPokemon.getHP() - damage));
                Thread.sleep(2000);
                System.out.println("It dealt " + damage + " damage.");
                Thread.sleep(2000);
                
                if (opponentCurrentPokemon.getHP() <= 0) {
                    System.out.println(opponentCurrentPokemon.getName() + " fainted!");
                    Thread.sleep(2000);
                    boolean oppSwitched = false;

                    for (Pokemon p : opponent.getTeam()) 
                    {
                        if (p.getHP() > 0) 
                        {
                            opponentCurrentPokemon = p;
                            opponent.setStarterPokemon(p);
                            System.out.println(opponent.getName() + " sent out " + p.getName() + "!");
                            Thread.sleep(2000);
                            oppSwitched = true;
                            break;
                        }
                    }

                    if (!oppSwitched) {
                        trainerWon = true;
                        gameFinished = true;
                        break;
                    }
                }

            } else {
                System.out.println(trainerCurrentPokemon.getName() + " missed!");
                Thread.sleep(2000);
            }

            // Trainer's Pokémon faint check after opponent's attack
            if (trainerCurrentPokemon.getHP() <= 0) {
                handleTrainerSwitch();
            }

            DisplayGameDetails gameDetails = new DisplayGameDetails();
            gameDetails.displayGameDetails(trainer, opponent);
            break;

        case 2:
            switchPokemon();
            break;

        case 3:
            Thread.sleep(2000);
            System.out.println("You ran away! The battle ends.");
            ranAway = true;
            gameFinished = true;
            break;
        }
        return new BattleResult(trainerCurrentPokemon, opponentCurrentPokemon, gameFinished, trainerWon, ranAway);
    }
    
    private void handleTrainerSwitch() 
    {
        System.out.println(trainerCurrentPokemon.getName() + " fainted!");
        boolean switched = false;

        while (!switched) 
        {
            System.out.println("\nChoose a new pokemon to continue:");
            int idx = 1;

            for (Pokemon p : trainer.getTeam()) 
            {
                String tag = (p.getHP() <= 0) ? " [FNT]" : "";
                System.out.println("(" + idx + ") " + p.getName() + tag);
                idx++;
            }

            System.out.println("(0) Forfeit battle");
            int choice = getValidInput(scanner, 0, trainer.getTeam().size(), "Choose a pokemon or enter 0 to forfeit the battle...");

            if (choice == 0) 
            {
                System.out.println("You forfeited the battle.");
                System.exit(0); // Ends the battle
            }

            if (choice < 1 || choice > trainer.getTeam().size()) 
            {
                System.out.println("Invalid choice.");
                continue;
            }

            Pokemon chosen = trainer.getTeam().get(choice - 1);

            if (chosen.getHP() <= 0) 
            {
                System.out.println(chosen.getName() + " has fainted. Pick another Pokémon.");
                continue;
            }

            trainerCurrentPokemon = chosen;
            trainer.setStarterPokemon(chosen);
            System.out.println("Go, " + chosen.getName() + "!");
            switched = true;
        }
    }
    
    private void switchPokemon() throws InterruptedException 
    {
        int orderNumber = 1;
        System.out.print("YOUR TEAM: ");

        for (Pokemon p : trainer.getTeam()) 
        {
            String tag = (p.getHP() > 0) ? "" : " [FNT]";
            System.out.print("(" + orderNumber + ") " + p.getName() + tag + " ");
            orderNumber++;
        }

        int switchChoice = getValidInput(scanner, 0, trainer.getTeam().size(), "Choose a pokemon (1-" + trainer.getTeam().size() + "} or enter 0 to cancel...");

        if (switchChoice == 0) return;

        if (switchChoice < 1 || switchChoice > trainer.getTeam().size()) 
        {
            System.out.println("Invalid index.");
            return;
        }

        Pokemon chosen = trainer.getTeam().get(switchChoice - 1);

        if (chosen.getHP() <= 0) 
        {
            System.out.println(chosen.getName() + " has fainted. Choose another.");
            return;
        }
        if (chosen == trainerCurrentPokemon) 
        {
            System.out.println(chosen.getName() + " is already active.");
            return;
        }

        trainerCurrentPokemon = chosen;
        trainer.setStarterPokemon(chosen);
        System.out.println("Go, " + chosen.getName() + "!");

        DisplayGameDetails gameDetails = new DisplayGameDetails();
        gameDetails.displayGameDetails(trainer, opponent);
    }
    
    // Helper method to check for valid input 
    private int getValidInput(Scanner scanner, int min, int max, String prompt)
    {
        int input = -1;
        
        while (true)
        {
            System.out.println(prompt);
            // Read in full line
            String line = scanner.nextLine();
            
            try
            {
                // Try to parse input 
                input = Integer.parseInt(line.trim());
                
                // If input is within range 
                if (input >= min && input <= max)
                {
                    // Return input
                    return input;
                }
                else
                {
                    // Return error message
                    System.out.println("Please enter a digit between "+ min +" and " + max + "...");
                }
            } catch (NumberFormatException e)
            {
                System.out.println("Invalid input trainer! Please enter a digit...");
            }
        }
    }
}

