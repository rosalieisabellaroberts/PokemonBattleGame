/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
public class DisplayGameDetails 
{
     public void displayGameDetails(Trainer trainer, Trainer opponent)
    {
        // Display opponent details 
        System.out.println("\nOPPONENT: "+opponent.getName());
        System.out.println("LEVEL: "+opponent.getLevel());
        System.out.print("TEAM: ");
        int orderNumber = 1;
        for (Pokemon pokemon : opponent.getTeam()) 
        {
        System.out.print("("+orderNumber+ ") " + pokemon.getName() + " ");
        orderNumber++;
        }
        System.out.println("\nCURRENT POKEMON: "+ opponent.getStarterPokemon().getName());
        System.out.println("HP: "+opponent.getStarterPokemon().getHP()+"/"+opponent.getStarterPokemon().getOriginalHP()); 
        
        // Display options here
        System.out.println("_____________________________________________");
        
        // Display user details 
        System.out.println("TRAINER: "+trainer.getName());
        System.out.println("LEVEL: "+trainer.getLevel());
        System.out.print("TEAM: ");
        orderNumber = 1;
        for (Pokemon pokemon : trainer.getTeam()) 
        {
        System.out.print("("+orderNumber+ ") " + pokemon.getName() + " ");
        orderNumber++;
        }
        System.out.println("\nCURRENT POKEMON: "+trainer.getStarterPokemon().getName());
        System.out.println("HP: "+trainer.getStarterPokemon().getHP()+"/"+trainer.getStarterPokemon().getOriginalHP());   
    }
}
