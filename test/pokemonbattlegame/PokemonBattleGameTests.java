/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.util.*;
import java.sql.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dilro
 */
public class PokemonBattleGameTests 
{
    Trainer trainer;
    Trainer opponent;
    Pokemon pikachu; 
    Pokemon bulbasaur;
    Pokemon charmander;
    Pokemon squirtle;
    
    Random random;
    
    // Connection to Derby database for tests
    private Connection connection;
    
    @Before
    public void setup() throws InterruptedException, SQLException
    {   
        try 
        {     
            // Establish connection 
            connection = DatabaseManager.getConnection();
            
            // Initalise the PokemonBattleGame database
            DatabaseManager.createTables(connection);
            DatabaseManager.populateTypesTable(connection);
            DatabaseManager.populatePokemonTable(DatabaseManager.getPokemonList(), connection);
            DatabaseManager.populateWeaknesses(connection);
            DatabaseManager.populateStrengths(connection);
            DatabaseManager.populateMoves(connection);
            DatabaseManager.populateTrainers(connection);
            
            // Load types with effectiveness 
            ArrayList<Type> types = DatabaseManager.loadTypesWithEffectiveness(connection);
        
            // Load trainer 
            SetupGame setup = new SetupGame();
            Trainer trainer = setup.run(connection);

            // Save the trainer data into the database 
            SaveManager.saveTrainer(trainer, connection);  
        } catch (Exception e)
        {
            e.printStackTrace();
        } 

        // Initalise moves for test pokemon
        ArrayList<Move> pikachuMovesArray = new ArrayList<>(Arrays.asList(
            new Move("Thunder Shock", 40, 100),
            new Move("Quick Attack", 45, 100),
            new Move("Electro Ball", 50, 100) 
        ));
        
        ArrayList<Move> bulbasaurMovesArray = new ArrayList<>(Arrays.asList(
            new Move("Vine Whip", 40, 100),
            new Move("Tackle", 45, 100),
            new Move("Growl", 50, 100) 
        ));
        
        ArrayList<Move> charmanderMovesArray = new ArrayList<>(Arrays.asList(
            new Move("Ember", 40, 100),
            new Move("Smokescreen", 45, 100),
            new Move("Scratch", 50, 100) 
        ));
        
        ArrayList<Move> squirtleMovesArray = new ArrayList<>(Arrays.asList(
            new Move("Water Gun", 40, 100),
            new Move("Tackle", 45, 100),
            new Move("Tail Whip", 50, 100) 
        ));
  
        // Initalise test pokemon
        pikachu = new Pokemon("Pikachu", new Type("Electric"), 35, pikachuMovesArray);
        bulbasaur = new Pokemon("Bulbasaur", new Type("Grass"), 45, bulbasaurMovesArray);
        charmander = new Pokemon("Charmander", new Type("Fire"), 39, charmanderMovesArray);
        squirtle = new Pokemon("Squirtle", new Type("Water"), 44, squirtleMovesArray);
        
        // Initalise test trainer and opponent 
        trainer = new Trainer("ketchup", "Ash", 0, 1, pikachu, "Pikachu, I choose you!");
        opponent = new Trainer("treeHugger", "Professor Oak", 3, 2, bulbasaur, "Don't think I'll go easy on you!");
          
        // Create a new random object 
        random = new Random();
    }

    // Test creation of new trainers 
    @Test 
    public void testNewTrainerCreationAsh()
    {
        // Assert trainer username, name and starter pokemon are were created
        assertEquals("ketchup", trainer.getUsername());
        assertEquals("Ash", trainer.getName());
        assertEquals("Pikachu", trainer.getStarterPokemon().getName());
    }
    
    @Test 
    public void testNewTrainerCreationProfessorOak()
    {
        // Assert trainer username, name and starter pokemon are were created
        assertEquals("treeHugger", opponent.getUsername());
        assertEquals("Professor Oak", opponent.getName());
        assertEquals("Bulbasaur", opponent.getStarterPokemon().getName());
    }

    // Test database population & retrieval of new trainers 
    @Test
    public void testNewTrainerPopulationAndRetrieval() throws Exception
    {
        // Create an array list of pokemon objects (test pokemons initalised in @Before)
        ArrayList<Pokemon> testPokemons = new ArrayList<>(Arrays.asList(pikachu, bulbasaur, charmander, squirtle));
        
        // Add testPokemons to Pokemon table in database
        DatabaseManager.populatePokemonTable(testPokemons, connection);

        // Save a new trainer
        SaveManager.saveTrainer(trainer, connection);
        
        // Retrieve trainer saved
        Trainer savedTrainer = DatabaseManager.getTrainer(trainer.getUsername(), connection);
        
        // Asser the trainer is not null, and username and starter pokemon saved to database match the users' details
        assertNotNull(savedTrainer);
        assertEquals(trainer.getUsername(), savedTrainer.getUsername());
        assertEquals(trainer.getStarterPokemon().getName(), savedTrainer.getStarterPokemon().getName());
    }
    
    // Test database reading/writing 
    @Test 
    public void testDatabaseReadingAndWriting() throws SQLException
    {
        // Create an array list of pokemon objects (test pokemons initalised in @Before)
        ArrayList<Pokemon> testPokemons = new ArrayList<>(Arrays.asList(pikachu, bulbasaur, charmander, squirtle));
        DatabaseManager.populatePokemonTable(testPokemons, connection);
        
        // Retrieve pokemon list from database 
        List<Pokemon> readTestPokemons = DatabaseManager.getPokemonList();
        
        // Verify pokemon were written and read correctly
        assertNotNull(readTestPokemons);
        assertFalse(readTestPokemons.isEmpty());
        
        // Check if specific Pokemon are contained within the read list
        boolean containsPikachu = readTestPokemons.stream().anyMatch(pokemon -> pokemon.getName().equalsIgnoreCase("Pikachu"));
        boolean containsBulbasaur = readTestPokemons.stream().anyMatch(pokemon -> pokemon.getName().equalsIgnoreCase("Bulbasaur"));
        boolean containsCharmander = readTestPokemons.stream().anyMatch(pokemon -> pokemon.getName().equalsIgnoreCase("Charmander"));
        boolean containsSquirtle = readTestPokemons.stream().anyMatch(pokemon -> pokemon.getName().equalsIgnoreCase("Squirtle"));
        
        // Assert that read list contains pikachu, bulbasaur, charmander and squirtle
        assertTrue(containsPikachu);
        assertTrue(containsBulbasaur);
        assertTrue(containsCharmander);
        assertTrue(containsSquirtle);
    }
    
    // Test type weaknesses and strengths 
    @Test
    public void testWeaknessesAndStrengths()
    {
        Type fire = new Type("Fire");
        Type water = new Type("Water");
        
        // Water types are weak against fire types 
        fire.setStrongAgainst(water);
        
        // Assert that fire is super effective against water and vice versa
        assertTrue(fire.isSuperEffectiveAgainst(fire, water));
        assertFalse(fire.isNotVeryEffectiveAgainst(fire, water));
    }
    
    // Test damage calculation 
    @Test 
    public void testDamageCalculation() throws InterruptedException
    {
        // Create a new test turn object
        TestTurn turn = new TestTurn(trainer, opponent, pikachu, squirtle, random);
        
        // Create a move for pikachu
        Move thunderShock = new Move("Thunder Shock", 40, 100);
        
        // Set HP of pikachu and opponent pokemon squirtle
        pikachu.setHP(35);
        squirtle.setHP(44);
        
        // Pikachu uses thunderShock on squirtle 
        int damage = turn.calculateDamage(thunderShock, pikachu, squirtle);
        squirtle.setHP(squirtle.getHP() - damage);
        
        // Assert damage is greater than 0 and the HP of squirtle is not greater than its max HP
        assertTrue(damage > 0);
        assertTrue(squirtle.getHP() < 44);
    }
    
    // Test move accuracy (if moves will hit) 
    @Test
    public void testMoveAccuracy()
    {
        // Move with 100% accuracy 
        Move thunderShock = new Move("Thunder Shock", 40, 100);
        // Move with 0& accuracy 
        Move harden = new Move("Harden", 60, 0);
        
        // Generate a random accuracy for each move
        boolean thunderShockHits = Turn.moveHit(thunderShock, random);
        boolean hardenHits = Turn.moveHit(harden, random);    
        
        // Assert that 100% accuracy move always hits 
        assertTrue(thunderShockHits);
        // Assert that 0% accuracy move never hits 
        assertFalse(hardenHits);
    }
    
    // Test critical hits
    @Test 
    public void testCriticalHits() throws InterruptedException
    {
        // Set up fixed random value that will force a critical hit (less that 0.0625)
        Random criticalRandom = new FixedRandom(0.01);
        // Set up fixed random value that will not force a critical hit (more than 0.0625)
        Random nonCriticalRandom = new FixedRandom(0.5);
                
        // Create a new test turn for critical hit
        TestTurn criticalTurn = new TestTurn(trainer, opponent, charmander, bulbasaur, criticalRandom);
        
        // Create a new test turn for a non-critical hit
        TestTurn nonCriticalTurn = new TestTurn(trainer, opponent, charmander, bulbasaur, nonCriticalRandom);
        
        // Create a new move for charmander 
        Move ember = new Move("Ember", 40, 100);
        
        // Calculate damage for critical hit 
        int criticalDamage = criticalTurn.calculateDamage(ember, charmander, bulbasaur);
        
        // Calculate damage for non-critical hit 
        int nonCriticalDamage = nonCriticalTurn.calculateDamage(ember, charmander, bulbasaur);
        
        // Assert critical hit increases damage over normal hit 
        assertTrue(criticalDamage > nonCriticalDamage);
        
        // Assert damages are atleast 1
        assertTrue(criticalDamage >= 1);
        assertTrue(nonCriticalDamage >=1);
    }
    
    // Test battleresult after move 
    @Test
    public void testBattleResultAfterMove() throws InterruptedException
    {   
        // Set up fixed random value to ensure no critical hits or misses
        Random fixedRandom = new FixedRandom(0.1);
        
        // Create a new test turn 
        TestTurn turn = new TestTurn(trainer, opponent, pikachu, squirtle, fixedRandom);
        
        // Create a controlled move for pikachu with power low enough to test if both pokemon survive
        Move thunderShock = new Move("Thunder Shock", 10, 100);
        
        // Get BattleResult of move performed
        BattleResult result = turn.takeTestTurn(thunderShock, pikachu, squirtle);
        
        // Assert that trainer pokemon in BattleResult is pikachu 
        assertEquals(pikachu, result.getTrainerPokemon());
        
        // Assert that opponent pokemon in BattleResult is squirtle
        assertEquals(squirtle, result.getOpponentPokemon());
        
        // Assert that game is not finished after the move 
        assertFalse(result.getGameFinished());
        
        // Assert that opponent HP has decreased, but not gone beneath 0
        int damage = squirtle.getOriginalHP() - squirtle.getHP();
        assertTrue(squirtle.getHP() >= 0);
        assertTrue(damage > 0);
        
        // Assert damage applied is consistent with damage calculated 
        int expectedDamage = turn.testCalculateDamage(thunderShock, pikachu, squirtle);
        assertEquals(expectedDamage, damage);       
    }
    
    // Test pokemon fainting 
    @Test
    public void testPokemonFainting() throws InterruptedException 
    {
        // Set up fixed random value to ensure critical hit
        Random fixedRandom = new FixedRandom(0.01);
        
        // Create a new test turn 
        TestTurn turn = new TestTurn(trainer, opponent, charmander, bulbasaur, fixedRandom);
           
        // Create a controlled move for charmander with power high enough to reduce opponentPokemon HP below 0
        Move ember = new Move("Ember", 2000, 100);
        
        // Get BattleResult of move performed 
        BattleResult result = turn.takeTestTurn(ember, charmander, bulbasaur);
        
        // Assert that opponentPokemon HP is 0
        assertEquals(0, result.getOpponentPokemon().getHP());
        
        // Asset that game is finished if no pokemon are left (test opponent only has a single pokemon) 
        assertTrue(result.getGameFinished());
        
        // Assert that the trainer has won 
        assertTrue(result.getTrainerWon());
    }
    
    // Test trainer switching pokemon 
    @Test
    public void testTrainerSwitchingPokemon() throws InterruptedException, SQLException
    {
        // Add starter pokemon to the trainers team
        trainer.getTeam().add(trainer.getStarterPokemon());
        
        // Add charmander to the trainers team so it can be switched to
        trainer.getTeam().add(charmander);
        
        // Create a new test turn
        TestTurn turn = new TestTurn(trainer, opponent, bulbasaur, squirtle, random);
        
        // Switch the trainers current pokemon to the team member
        turn.switchTrainerPokemon(charmander);
        
        // Assert that the trainers starter is still the same, but the current pokemon have changed
        assertEquals(pikachu, trainer.getStarterPokemon());
        assertEquals(charmander, turn.getTrainerPokemon());
    }
    
    // Test trainer running away 
    @Test 
    public void testTrainerRunningAway() throws InterruptedException 
    {
        // Create a new test turn 
        TestTurn turn = new TestTurn(trainer, opponent, pikachu, squirtle, random);
        
        // Get Battle Result of running away 
        BattleResult result = turn.runAway(pikachu, squirtle);
        
        // Assert that the game is finished
        assertTrue(result.getGameFinished());
        
        // Assert that ranAway is true
        assertTrue(result.getRanAway());
        
        // Assert pokemon HP did not change 
        assertEquals(pikachu.getHP(), result.getTrainerPokemon().getHP());
        assertEquals(squirtle.getHP(), result.getOpponentPokemon().getHP());
        
        // Assert current pokemon did not change
        assertEquals(pikachu, turn.getTrainerPokemon());
        assertEquals(squirtle, turn.getOpponentPokemon());
    }
    
    @After
    public void teardown() throws Exception 
    {
        if (connection != null && !connection.isClosed())
        {
            connection.close();
        }
        
        try 
        {
            DriverManager.getConnection("jdbc:derby:PokemonBattleGameDB;shutdown=true");
        } catch (SQLException e)
        {
            // If database is not successfully shutdown print exception 
            if (!"08006".equals(e.getSQLState()) && !"XJ015".equals(e.getSQLState()))
            {
                e.printStackTrace();
            }
        }
    }
}
