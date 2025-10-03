/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
public class TrainerTurnResult 
{
    public Pokemon updatedTrainerPokemon;
    public Pokemon updatedOpponentPokemon;
    public boolean gameFinished;
    public boolean trainerWon;
    public boolean ranAway;

    public TrainerTurnResult(Pokemon trainerPokemon, Pokemon opponentPokemon, boolean finished, boolean won, boolean ran) 
    {
        this.updatedTrainerPokemon = trainerPokemon;
        this.updatedOpponentPokemon = opponentPokemon;
        this.gameFinished = finished;
        this.trainerWon = won;
        this.ranAway = ran;
    }
}


