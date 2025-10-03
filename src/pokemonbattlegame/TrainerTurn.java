
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

public class TrainerTurn 
{
    private Trainer trainer;
    private Trainer opponent;
    private Pokemon trainerCurrentPokemon;
    private Pokemon opponentCurrentPokemon;
    private Scanner scanner;
    private Random random;

    public TrainerTurn(Trainer trainer, Trainer opponent, 
                       Pokemon trainerCurrentPokemon, Pokemon opponentCurrentPokemon,
                       Scanner scanner, Random random) 
    {
        this.trainer = trainer;
        this.opponent = opponent;
        this.trainerCurrentPokemon = trainerCurrentPokemon;
        this.opponentCurrentPokemon = opponentCurrentPokemon;
        this.scanner = scanner;
        this.random = random;
    }

    public TrainerTurnResult takeTurn() throws InterruptedException {
    boolean gameFinished = false;
    boolean trainerWon = false;
    boolean ranAway = false;

    int orderNumber;
    int moveSelection;
    double randomAccuracy;

    System.out.println("\n(1) FIGHT (2) SWITCH POKEMON (3) RUN");
    System.out.println("What will you do? Enter a digit between 1-3...");
    int selection = scanner.nextInt();

    switch (selection) {
        case 1:
            orderNumber = 1;
            ArrayList<Move> moves = trainerCurrentPokemon.getMoves();

            System.out.print("MOVES: ");
            for (Move move : moves) {
                System.out.print("(" + orderNumber + ") " + move.getName() + " ");
                orderNumber++;
            }

            System.out.println("\nWhich will you use? Enter a digit...");
            moveSelection = scanner.nextInt();

            Move selectedMove = moves.get(moveSelection - 1);
            System.out.println("\n" + trainerCurrentPokemon.getName() + " used " + selectedMove.getName() + "!");
            Thread.sleep(2000);

            randomAccuracy = random.nextDouble() * 100;
            if (randomAccuracy <= selectedMove.getAccuracy()) {
                Type atkType = trainerCurrentPokemon.getPokemonType();
                Type defType = opponentCurrentPokemon.getPokemonType();

                double typeMult = 1.0;
                if (TypeEffectiveness.isSuperEffective(atkType, defType)) {
                    typeMult = 2.0;
                    Thread.sleep(2000);
                    System.out.println("It's super effective!");
                } else if (TypeEffectiveness.isNotVeryEffective(atkType, defType)) {
                    typeMult = 0.5;
                    Thread.sleep(2000);
                    System.out.println("It's not very effective...");
                }

                boolean crit = random.nextDouble() < 0.0625;
                double critMult = crit ? 1.5 : 1.0;
                double randMult = 0.85 + (random.nextDouble() * 0.15);

                int dmg = (int) Math.floor(selectedMove.getPower() * typeMult * critMult * randMult);
                if (dmg < 1) dmg = 1;
                if (crit) System.out.println("A critical hit!");

                int newHP = Math.max(0, opponentCurrentPokemon.getHP() - dmg);
                opponentCurrentPokemon.setHP(newHP);
                Thread.sleep(2000);
                System.out.println("It dealt " + dmg + " damage.");
                Thread.sleep(2000);

                if (opponentCurrentPokemon.getHP() <= 0) {
                    System.out.println(opponentCurrentPokemon.getName() + " fainted!");
                    boolean oppSwitched = false;

                    for (Pokemon p : opponent.getTeam()) 
                    {
                        if (p.getHP() > 0) 
                        {
                            opponentCurrentPokemon = p;
                            opponent.setStarterPokemon(p);
                            System.out.println(opponent.getName() + " sent out " + p.getName() + "!");
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
        return new TrainerTurnResult(trainerCurrentPokemon, opponentCurrentPokemon, gameFinished, trainerWon, ranAway);
    }
    
    private void handleTrainerSwitch() 
    {
        System.out.println(trainerCurrentPokemon.getName() + " fainted!");
        boolean switched = false;

        while (!switched) 
        {
            System.out.println("\nChoose a new Pokémon to continue:");
            int idx = 1;

            for (Pokemon p : trainer.getTeam()) 
            {
                String tag = (p.getHP() <= 0) ? " [FNT]" : "";
                System.out.println("(" + idx + ") " + p.getName() + tag);
                idx++;
            }

            System.out.println("(0) Forfeit battle");
            int choice = scanner.nextInt();

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

        System.out.println("\nChoose a Pokémon (1-" + trainer.getTeam().size() + ") or 0 to cancel:");
        int switchChoice = scanner.nextInt();

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
}

