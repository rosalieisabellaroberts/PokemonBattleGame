/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pokemonbattlegame;

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
    
    public static void main(String[] args) throws InterruptedException
    {
        
        // Initalise the PokemonBattleGame database 
        DatabaseManager.createTables();
        DatabaseManager.populateTypesTable();
        DatabaseManager.populateWeaknesses();
        DatabaseManager.populateStrengths();
        DatabaseManager.populatePokemonTable(DatabaseManager.getPokemonList());
        DatabaseManager.populateTrainers();
        DatabaseManager.populateMoves();
    
        // Show the professor Oaks intro JFrame 
        GameSetup oaksIntro = new GameSetup();
        oaksIntro.setVisible(true);
        
        // Create a new game
        PokemonBattleGame game = new PokemonBattleGame();
      
        // Setup the game
        SetupGame setup = new SetupGame();
        Trainer trainer = setup.run();

        // If trainer creation fails, stop program
        if (trainer == null) 
        {
            System.out.println("Professor Oak: Looks like you are not fit to be a trainer just yet ...");
            return;
        }

        game.setTrainer(trainer);
        SaveManager.saveTrainer(trainer);
        game.setPokemons(setup.getPokemons());
        
        // Prompt user for start 
        BattleManager battleManager = new BattleManager(game);
        
        // Randomly generate an opponent
        try 
        {
            GenerateOpponent generatedOpponent = new GenerateOpponent();
            generatedOpponent.generateOpponent(game.getTrainer());
            game.setOpponent(generatedOpponent.getOpponent());
        } catch (InterruptedException e)
        {
            System.out.println("Professor Oak: Looks like there are no trainers to battle! Try again later..." + e.getMessage());
        }
        
        // Randomly generate pokemon teams
        GeneratePokemonTeams generatedPokemonTeams = new GeneratePokemonTeams();
        generatedPokemonTeams.setPokemons(setup.getPokemons());
        generatedPokemonTeams.generatePokemonTeams(game.getTrainer(), game.getOpponent());
       
        battleManager.startBattle();  
    }

    public void setTrainer(Trainer trainer) 
    {
        this.trainer = trainer;
    }

    public Trainer getTrainer() 
    {
        return trainer;
    }

    public Trainer getOpponent() 
    {
        return opponent;
    }

    public void setOpponent(Trainer opponent) 
    {
        this.opponent = opponent;
    }
    
    public Pokemon[] getPokemons() 
    {
        return pokemons;
    }

    public void setPokemons(Pokemon[] pokemons) 
    {
        this.pokemons = pokemons;
    }
}
