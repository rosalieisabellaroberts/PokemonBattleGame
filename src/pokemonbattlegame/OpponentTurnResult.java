/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
public class OpponentTurnResult 
{
    public Pokemon updatedTrainerPokemon;
    public Pokemon updatedOpponentPokemon;
    public boolean gameFinished;
    public boolean trainerWon;

    public OpponentTurnResult(Pokemon trainerPokemon, Pokemon opponentPokemon, boolean finished, boolean won) 
    {
        this.updatedTrainerPokemon = trainerPokemon;
        this.updatedOpponentPokemon = opponentPokemon;
        this.gameFinished = finished;
        this.trainerWon = won;
    }
}
