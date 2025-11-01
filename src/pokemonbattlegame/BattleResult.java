/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
public class BattleResult 
{
    public Pokemon updatedTrainerPokemon;
    public Pokemon updatedOpponentPokemon;
    public boolean gameFinished;
    public boolean trainerWon;
    public boolean ranAway;

    public BattleResult(Pokemon trainerPokemon, Pokemon opponentPokemon, boolean finished, boolean won, boolean ran) 
    {
        this.updatedTrainerPokemon = trainerPokemon;
        this.updatedOpponentPokemon = opponentPokemon;
        this.gameFinished = finished;
        this.trainerWon = won;
        this.ranAway = ran;
    }

     // Getter and setter methods 
    public Pokemon getTrainerPokemon()
    {
        return updatedTrainerPokemon;
    }
    
    public Pokemon getOpponentPokemon()
    {
        return updatedOpponentPokemon;
    }
    
    public boolean getGameFinished()
    {
        return gameFinished;
    }
    
    public boolean getTrainerWon()
    {
        return trainerWon;
    }
    
    public boolean getRanAway()
    {
        return ranAway;
    }
}
