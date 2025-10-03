package pokemonbattlegame;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author dilro
 */
public class DatabaseManager 
{
    private static final String URL = "jdbc:derby:PokemonBattleGameDB;create=true";
    private static Connection connection;
    
    // Return a connection to the Derby database 
    public static Connection getConnection() throws SQLException
    {
        // If there is no existing connection object, or it was previously closed
        if (connection == null || connection.isClosed())
        {
            // Create a new connection to the derby database using the URL
           connection = DriverManager.getConnection(URL);
        }
        // Return the active connection 
        return connection;
    }
    
    // Create types, trainers, pokemon, moves, weaknesses, and strengths
    public static void createTables()
    {
        try (Connection connection = getConnection();
            Statement statement = connection.createStatement())
        {
            // Create table for types 
            statement.executeUpdate("CREATE TABLE Types(" +
                    "typeName VARCHAR(50) PRIMARY KEY)");
            
            // Create table for pokemon
            statement.executeUpdate("CREATE TABLE Pokemon ("+
                    "name VARCHAR(50) PRIMARY KEY," +
                    "type VARCHAR(50)," +
                    "HP INT," +
                    "originalHP INT," +
                    "FOREIGN KEY (type) REFERENCES Types(typeName))");
            
            // Create table for trainers 
            statement.executeUpdate("CREATE TABLE Trainers (" +
                    "username VARCHAR(50) PRIMARY KEY," +
                    "name VARCHAR(50)," +
                    "score INT," +
                    "level INT," +
                    "challengeMessage VARCHAR(255)," +
                    "starterPokemon VARCHAR(50)," +
                    "FOREIGN KEY (starterPokemon) REFERENCES Pokemon(name))");
   
            // Create table for moves
            statement.executeUpdate("CREATE TABLE Moves (" +
                    "id INT GENERATED ALWAYS IDENTITY PRIMARY KEY," +
                    "pokemonName VARCHAR(50)," +
                    "name VARCHAR(50)," +
                    "power DOUBLE," +
                    "accuracy DOUBLE," +
                    "FOREIGN KEY (pokemonName) REFERENCES Pokemon(name)");
            
            // Create table for weaknesses
            statement.executeUpdate("CREATE TABLE Weaknesses(" +
                    "typeName VARCHAR(50)," +
                    "weakAgainst VARCHAR(50)," +
                    "FOREIGN KEY (typeName) REFERENCES Types(typeName)," +
                    "FOREIGN KEY (weakAgainst) REFERENCES Types(typeName))");
            
            // Create table for strengths
            statement.executeUpdate("CREATE TABLE Strengths(" +
                    "typeName VARCHAR(50)," +
                    "strongAgainst VARCHAR(50)," +
                    "FOREIGN KEY (typeName) REFERENCES Types(typeName)," +
                    "FOREIGN KEY (strongAgainst) REFERENCES Types(typeName))");
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    // Add pokemon types into types table
    public static void populateTypesTable()
    {
        String[] types = {"Grass", "Fire", "Water", "Bug", "Electric", "Normal",
            "Fighting", "Rock", "Psychic", "Ghost"};

        try (Connection connection = getConnection())
        {
            String sql = "INSERT INTO Types (typeName) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            for (String t : types)
            {
                try 
                {
                    preparedStatement.setString(1,t);
                    preparedStatement.executeUpdate();
                } catch (SQLException ignored)
                {   
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    // Create a new list of Pokemon objects and populate with data 
    public static List<Pokemon> getPokemonList()
    {
        // Create a new list of Pokemon objects 
        List<Pokemon> pokemons = new ArrayList<>();
        
        // Pokemon data
        pokemons.add(new Pokemon("Bulbasaur", new Type("Grass"), 45, new ArrayList<>()));
        pokemons.add(new Pokemon("Ivysaur", new Type("Grass"), 60, new ArrayList<>()));
        pokemons.add(new Pokemon("Venusaur", new Type("Grass"), 80, new ArrayList<>()));

        pokemons.add(new Pokemon("Charmander", new Type("Fire"), 39, new ArrayList<>()));
        pokemons.add(new Pokemon("Charmeleon", new Type("Fire"), 58, new ArrayList<>()));
        pokemons.add(new Pokemon("Charizard", new Type("Fire"), 78, new ArrayList<>()));

        pokemons.add(new Pokemon("Squirtle", new Type("Water"), 44, new ArrayList<>()));
        pokemons.add(new Pokemon("Wartortle", new Type("Water"), 59, new ArrayList<>()));
        pokemons.add(new Pokemon("Blastoise", new Type("Water"), 79, new ArrayList<>()));

        pokemons.add(new Pokemon("Caterpie", new Type("Bug"), 45, new ArrayList<>()));
        pokemons.add(new Pokemon("Metapod", new Type("Bug"), 50, new ArrayList<>()));
        pokemons.add(new Pokemon("Butterfree", new Type("Bug"), 60, new ArrayList<>()));

        pokemons.add(new Pokemon("Pikachu", new Type("Electric"), 35, new ArrayList<>()));
        pokemons.add(new Pokemon("Raichu", new Type("Electric"), 60, new ArrayList<>()));

        pokemons.add(new Pokemon("Jigglypuff", new Type("Normal"), 115, new ArrayList<>()));
        pokemons.add(new Pokemon("Wigglytuff", new Type("Normal"), 140, new ArrayList<>()));

        pokemons.add(new Pokemon("Meowth", new Type("Normal"), 40, new ArrayList<>()));
        pokemons.add(new Pokemon("Persian", new Type("Normal"), 65, new ArrayList<>()));

        pokemons.add(new Pokemon("Psyduck", new Type("Water"), 50, new ArrayList<>()));
        pokemons.add(new Pokemon("Golduck", new Type("Water"), 80, new ArrayList<>()));

        pokemons.add(new Pokemon("Machop", new Type("Fighting"), 70, new ArrayList<>()));
        pokemons.add(new Pokemon("Machoke", new Type("Fighting"), 80, new ArrayList<>()));
        pokemons.add(new Pokemon("Machamp", new Type("Fighting"), 90, new ArrayList<>()));

        pokemons.add(new Pokemon("Geodude", new Type("Rock"), 40, new ArrayList<>()));
        pokemons.add(new Pokemon("Graveler", new Type("Rock"), 55, new ArrayList<>()));
        pokemons.add(new Pokemon("Golem", new Type("Rock"), 80, new ArrayList<>()));

        pokemons.add(new Pokemon("Abra", new Type("Psychic"), 25, new ArrayList<>()));
        pokemons.add(new Pokemon("Kadabra", new Type("Psychic"), 40, new ArrayList<>()));
        pokemons.add(new Pokemon("Alakazam", new Type("Psychic"), 55, new ArrayList<>()));

        pokemons.add(new Pokemon("Gastly", new Type("Ghost"), 30, new ArrayList<>()));
        pokemons.add(new Pokemon("Haunter", new Type("Ghost"), 45, new ArrayList<>()));
        pokemons.add(new Pokemon("Gengar", new Type("Ghost"), 60, new ArrayList<>()));

        pokemons.add(new Pokemon("Onix", new Type("Rock"), 35, new ArrayList<>()));

        pokemons.add(new Pokemon("Magikarp", new Type("Water"), 20, new ArrayList<>()));
        pokemons.add(new Pokemon("Gyarados", new Type("Water"), 95, new ArrayList<>()));

        pokemons.add(new Pokemon("Eevee", new Type("Normal"), 55, new ArrayList<>()));
        pokemons.add(new Pokemon("Vaporeon", new Type("Water"), 130, new ArrayList<>()));
        pokemons.add(new Pokemon("Jolteon", new Type("Electric"), 65, new ArrayList<>()));
        pokemons.add(new Pokemon("Flareon", new Type("Fire"), 65, new ArrayList<>()));

        pokemons.add(new Pokemon("Snorlax", new Type("Normal"), 160, new ArrayList<>()));

        pokemons.add(new Pokemon("Mewtwo", new Type("Psychic"), 106, new ArrayList<>()));
        pokemons.add(new Pokemon("Mew", new Type("Psychic"), 100, new ArrayList<>()));

        // Return list of Pokemon Objects 
        return pokemons;
    }
    
    // Populate pokemon data into pokemon table 
    public static void populatePokemonTable(List<Pokemon> pokemons) 
    {
        try (Connection connection = getConnection())
        {
            String sql = "INSERT INTO Pokemon (name, type, HP, originalHP) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
            for (Pokemon p : pokemons) 
            {
                preparedStatement.setString(1, p.getName());
                preparedStatement.setString(2, p.getPokemonType().getName());
                preparedStatement.setInt(3, p.getHP());
                preparedStatement.setInt(4, p.getOriginalHP());

                try
                {
                  preparedStatement.executeUpdate();  
                } catch (SQLException ignored)
                {
                } 
            } 
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    // Populate weakAgainst types into weaknesses table
    public static void populateWeaknesses()
    {
        String[][] weaknesses = 
        {
            {"Grass", "Fire"},
            {"Grass", "Bug"},
            {"Fire", "Water"},
            {"Water", "Electric"},
            {"Psychic", "Ghost"},
            {"Normal", "Fighting"},
            {"Rock", "Water"},
            {"Grass", "Fire"},
            {"Fighting", "Psychic"},
            {"Bug", "Fire"},
        };
        
        try (Connection connection = getConnection())
        {
            String sql = "INSERT INTO Weaknesses (typeName, weakAgainst) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            for (String[] pair : weaknesses)
            {
                try
                {
                    preparedStatement.setString(1, pair[0]);
                    preparedStatement.setString(2, pair[1]);
                    preparedStatement.executeUpdate();
                } catch (SQLException ignored)
                {         
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
     // Populate strongAgainst types into strengths table
    public static void populateStrengths()
    {
        String[][] strengths = 
        {
            {"Grass", "Water"},
            {"Fire", "Grass"},
            {"Water", "Fire"},
            {"Electic", "Water"},
            {"Psychic", "Fighting"},
            {"Bug", "Grass"},
            {"Rock", "Fire"},
            {"Fighting", "Rock"},
            {"Ghost", "Psychic"}
        };
        
        try (Connection connection = getConnection())
        {
            String sql = "INSERT INTO Strengths (typeName, strongAgainst) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            for (String[] pair : strengths)
            {
                try
                {
                    preparedStatement.setString(1, pair[0]);
                    preparedStatement.setString(2, pair[1]);
                    preparedStatement.executeUpdate();
                } catch (SQLException ignored)
                {         
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    // Populate trainer data into trainer table
    public static void populateTrainers()
    {
        String[][] trainers = 
        {
            {"k3tchup", "Ash", "100", "5", "Pikachu", "Pikachu, I choose you!"},
            {"broccol1", "Brock", "90", "4", "Onix", "Rock-solid victory will be mine!"},
            {"youngster_j", "Youngster Joey", "85", "4", "Butterfree", "You don't stand a chance against my Butterfree!"},
            {"1mJessie", "Jessie", "100", "5", "Meowth", "Prepare for trouble... and make it double!"},
            {"and1mJames", "James", "100", "5", "Meowth", "Surrender now, or prepare to fight!"},
            {"wet_and_misty", "Misty", "90", "4", "Psyduck", "My Psyduck is full of surprises!"},
            {"bossman_gravy", "Giovanni", "150", "7", "Mewtwo", "My team's tough... but my gravy is tougher!"},
            {"kabr1na", "Sabrina", "80", "3", "Kadabra", "Your mind will be my battlefield!"},
            {"oakHugger", "Oak", "150", "7", "Blastoise", "Come on, you wouldn't hit an old man!"},
            {"smogAndSlay", "Koga", "160", "7", "Gengar", "You just walked into a gas chamber... I can already smell victory!"},
        };
        
        try (Connection connection = getConnection())
        {
            String sql = "INSERT INTO Trainers (username, name, score, level, starterPokemon, challengeMessage)" +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            for (String[] trainer : trainers)
            {
                preparedStatement.setString(1, trainer[0]);
                preparedStatement.setString(2, trainer[1]);
                preparedStatement.setInt(3, Integer.parseInt(trainer[2]));
                preparedStatement.setInt(4, Integer.parseInt(trainer[3]));
                preparedStatement.setString(5, trainer[4]);
                preparedStatement.setString(6, trainer[5]);
                
                try
                {
                    preparedStatement.executeUpdate();
                } catch(SQLException ignored)
                {  
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }   
    }
        
    // Populate moves data into moves table 
    public static void populateMoves() 
    {
        String[][] moves = {
            {"Bulbasaur", "Vine Whip", "45", "100"},
            {"Bulbasaur", "Tackle", "40", "100"},
            {"Bulbasaur", "Growl", "50", "100"},
            {"Ivysaur", "Razor Leaf", "90", "95"},
            {"Ivysaur", "Seed Bomb", "80", "100"},
            {"Ivysaur", "Vine Whip", "85", "100"},
            {"Venusaur", "Solar Beam", "120", "100"},
            {"Venusaur", "Sludge Bomb", "90", "100"},
            {"Venusaur", "Leaf Storm", "150", "100"},
            {"Charmander", "Ember", "45", "100"},
            {"Charmander", "Smokescreen", "40", "100"},
            {"Charmander", "Scratch", "50", "100"},
            {"Charmeleon", "Flamethrower", "90", "100"},
            {"Charmeleon", "Dragon Rage", "85", "100"},
            {"Charmeleon", "Fire Fang", "80", "100"},
            {"Charizard", "Fire Blast", "120", "85"},
            {"Charizard", "Wing Attack", "90", "100"},
            {"Charizard", "Dragon Claw", "150", "100"},
            {"Squirtle", "Water Gun", "40", "100"},
            {"Squirtle", "Tackle", "45", "100"},
            {"Squirtle", "Tail Whip", "50", "100"},
            {"Wartortle", "Aqua Tail", "90", "90"},
            {"Wartortle", "Bite", "80", "100"},
            {"Wartortle", "Water Pulse", "85", "100"},
            {"Blastoise", "Hydro Pump", "120", "80"},
            {"Blastoise", "Skull Bash", "90", "100"},
            {"Blastoise", "Iron Defense", "150", "100"},
            {"Caterpie", "String Shot", "45", "95"},
            {"Caterpie", "Tackle", "40", "100"},
            {"Caterpie", "Bug Bite", "60", "100"},
            {"Metapod", "Harden", "50", "100"},
            {"Metapod", "Tackle", "40", "100"},
            {"Metapod", "Bug Bite", "60", "100"},
            {"Butterfree", "Confusion", "50", "100"},
            {"Butterfree", "Psybeam", "65", "100"},
            {"Butterfree", "Bug Buzz", "90", "100"},
            {"Pikachu", "Thunder Shock", "40", "100"},
            {"Pikachu", "Quick Attack", "40", "100"},
            {"Pikachu", "Electro Ball", "60", "100"},
            {"Raichu", "Thunderbolt", "90", "100"},
            {"Raichu", "Volt Tackle", "120", "100"},
            {"Raichu", "Brick Break", "75", "100"},
            {"Jolteon", "Thunder", "110", "70"},
            {"Jolteon", "Pin Missile", "25", "95"},
            {"Jolteon", "Agility", "80", "100"},
            {"Jigglypuff", "Sing", "70", "55"},
            {"Jigglypuff", "Double Slap", "15", "85"},
            {"Jigglypuff", "Body Slam", "85", "100"},
            {"Wigglytuff", "Hyper Voice", "90", "100"},
            {"Wigglytuff", "Play Rough", "90", "90"},
            {"Wigglytuff", "Double-Edge", "120", "100"},
            {"Meowth", "Pay Day", "40", "100"},
            {"Meowth", "Scratch", "40", "100"},
            {"Meowth", "Bite", "60", "100"},
            {"Persian", "Slash", "70", "100"},
            {"Persian", "Bite", "60", "100"},
            {"Persian", "Fake Out", "40", "100"},
            {"Eevee", "Quick Attack", "40", "100"},
            {"Eevee", "Bite", "60", "100"},
            {"Eevee", "Swift", "60", "100"},
            {"Vaporeon", "Water Pulse", "60", "100"},
            {"Vaporeon", "Aqua Tail", "90", "90"},
            {"Vaporeon", "Hydro Pump", "110", "80"},
            {"Snorlax", "Body Slam", "85", "100"},
            {"Snorlax", "Hyper Beam", "150", "90"},
            {"Snorlax", "Earthquake", "100", "100"},
            {"Psyduck", "Water Gun", "40", "100"},
            {"Psyduck", "Confusion", "50", "100"},
            {"Psyduck", "Zen Headbutt", "80", "90"},
            {"Golduck", "Hydro Pump", "110", "80"},
            {"Golduck", "Psychic", "90", "100"},
            {"Golduck", "Aqua Tail", "90", "90"},
            {"Machop", "Karate Chop", "50", "100"},
            {"Machop", "Low Kick", "60", "100"},
            {"Machop", "Seismic Toss", "60", "100"},
            {"Machoke", "Seismic Toss", "60", "100"},
            {"Machoke", "Karate Chop", "50", "100"},
            {"Machoke", "Bulk Up", "55", "100"},
            {"Machamp", "Cross Chop", "100", "80"},
            {"Machamp", "Dynamic Punch", "100", "50"},
            {"Machamp", "Close Combat", "120", "100"},
            {"Geodude", "Rock Throw", "50", "90"},
            {"Geodude", "Tackle", "40", "100"},
            {"Geodude", "Magnitude", "70", "100"},
            {"Graveler", "Rock Blast", "25", "90"},
            {"Graveler", "Earthquake", "100", "100"},
            {"Graveler", "Explosion", "250", "100"},
            {"Golem", "Earthquake", "100", "100"},
            {"Golem", "Stone Edge", "100", "80"},
            {"Golem", "Heavy Slam", "80", "100"},
            {"Onix", "Rock Slide", "75", "90"},
            {"Onix", "Iron Tail", "100", "75"},
            {"Onix", "Dig", "80", "100"},
            {"Abra", "Teleport", "55", "100"},
            {"Abra", "Confusion", "50", "100"},
            {"Abra", "Psybeam", "65", "100"},
            {"Kadabra", "Psychic", "90", "100"},
            {"Kadabra", "Recover", "80", "100"},
            {"Kadabra", "Calm Mind", "85", "100"},
            {"Alakazam", "Psychic", "90", "100"},
            {"Alakazam", "Focus Blast", "120", "70"},
            {"Alakazam", "Shadow Ball", "80", "100"},
            {"Mewtwo", "Psystrike", "100", "100"},
            {"Mewtwo", "Aura Sphere", "90", "100"},
            {"Mewtwo", "Shadow Ball", "120", "100"},
            {"Mew", "Psychic", "90", "155"},
            {"Mew", "Aura Sphere", "180", "100"},
            {"Mew", "Metronome", "200", "100"},
            {"Gastly", "Lick", "30", "100"},
            {"Gastly", "Night Shade", "95", "100"},
            {"Gastly", "Confuse Ray", "75", "100"},
            {"Haunter", "Shadow Ball", "80", "100"},
            {"Haunter", "Dark Pulse", "80", "100"},
            {"Haunter", "Destiny Bond", "90", "100"},
            {"Gengar", "Dream Eater", "100", "100"},
            {"Gengar", "Shadow Ball", "80", "100"},
            {"Gengar", "Sludge Bomb", "90", "100"},
            {"Magikarp", "Splash", "5", "100"},
            {"Magikarp", "Tackle", "40", "100"},
            {"Magikarp", "Flail", "10", "100"},
            {"Gyarados", "Dragon Dance", "60", "100"},
            {"Gyarados", "Hyper Beam", "150", "90"},
            {"Gyarados", "Waterfall", "80", "100"},
            {"Flareon", "Flame Charge", "85", "100"},
            {"Flareon", "Fire Fang", "80", "95"},
            {"Flareon", "Flamethrower", "90", "100"},
        };

        try (Connection connection = getConnection()) 
        {
            String sql = "INSERT INTO Moves (pokemonName, moveName, power, accuracy) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (String[] move : moves) 
            {
                preparedStatement.setString(1, move[0]); 
                preparedStatement.setString(2, move[1]);
                preparedStatement.setDouble(3, Double.parseDouble(move[2]));
                preparedStatement.setDouble(4, Double.parseDouble(move[3]));

                try 
                {
                    preparedStatement.executeUpdate();
                } catch (SQLException ignored) 
                {
                }
            }
        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    // Get method to return a list of all trainers 
    public static List<Trainer> getAllTrainers()
    {
        List<Trainer> trainers = new ArrayList<>();
        
        try (Connection connection = getConnection())
        {
            String sql = "SELECT * FROM Trainers";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                Trainer trainer = new Trainer (
                    resultSet.getString("username"),
                    resultSet.getString("name"),
                    resultSet.getInt("score"),
                    resultSet.getInt("level"),
                    getPokemon(resultSet.getString("starterPokemon")),
                    resultSet.getString("challengeMessage")
                );
                trainers.add(trainer);   
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return trainers;
    }
    
    // Get method to return a trainer whose name matches the query 
    public static Trainer getTrainer(String username) 
    {
        Trainer trainer = null;
        String sql = "SELECT username, name, challengeMessage, score FROM Trainers WHERE username = ?";

        try (Connection conn = getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)) 
        {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) 
            {
                trainer = new Trainer
                (
                    resultSet.getString("username"),
                    resultSet.getString("name"),
                    resultSet.getString("challengeMessage")
                );
                trainer.setScore(resultSet.getInt("score")); 
            }   
        } catch (SQLException e) 
        {
            System.out.println("Professor Oak: Could not fetch trainer! " + e.getMessage());
        }
        
        return trainer; 
    }
    
    // Get method to return a pokemon whose name matches the query 
    public static Pokemon getPokemon(String pokemonName) 
    {
        try (Connection connection = getConnection()) 
        {
            String sql = "SELECT * FROM Pokemon WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, pokemonName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) 
            {
                return new Pokemon
                (
                    resultSet.getString("name"),
                    new Type(resultSet.getString("type")),
                    resultSet.getInt("HP"),
                    new ArrayList<>()
                );
            }
        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return null;
    }   
}
