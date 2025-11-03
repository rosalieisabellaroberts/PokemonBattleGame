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
    private static Connection connection = null;
    
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
    public static void createTables(Connection connection)
    {
        try (Statement statement = connection.createStatement())
        {
            // Create table for types if it doesn't already exist 
            if (!tableExists(connection, "Types"))
            {
                statement.executeUpdate("CREATE TABLE Types(" +
                    "typeName VARCHAR(50) PRIMARY KEY)");
            }
            
            
            // Create table for pokemon if it doesn't already exist
            if (!tableExists(connection, "Pokemon"))
            {
                statement.executeUpdate("CREATE TABLE Pokemon ("+
                    "name VARCHAR(50) PRIMARY KEY," +
                    "type VARCHAR(50)," +
                    "HP INT," +
                    "originalHP INT," +
                    "imagePath VARCHAR(255)," +
                    "FOREIGN KEY (type) REFERENCES Types(typeName))");
            }
            
            // Create table for trainers if it doesn't already exist
            if (!tableExists(connection, "Trainers"))
            {
               statement.executeUpdate("CREATE TABLE Trainers (" +
                    "username VARCHAR(50) PRIMARY KEY," +
                    "name VARCHAR(50)," +
                    "score INT," +
                    "level INT," +
                    "starterPokemon VARCHAR(50)," +
                    "challengeMessage VARCHAR(255)," +
                    "FOREIGN KEY (starterPokemon) REFERENCES Pokemon(name))"); 
            }
            
            // Create table for moves if it doesn't already exist
            if (!tableExists(connection, "Moves"))
            {
                statement.executeUpdate("CREATE TABLE Moves (" +
                    "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                    "pokemonName VARCHAR(50)," +
                    "name VARCHAR(50)," +
                    "power DOUBLE," +
                    "accuracy DOUBLE," +
                    "FOREIGN KEY (pokemonName) REFERENCES Pokemon(name))");
            }
                   
            // Create table for weaknesses if it doesn't already exist
            if (!tableExists(connection, "Weaknesses"))
            {
               statement.executeUpdate("CREATE TABLE Weaknesses(" +
                    "typeName VARCHAR(50)," +
                    "weakAgainst VARCHAR(50)," +
                    "FOREIGN KEY (typeName) REFERENCES Types(typeName)," +
                    "FOREIGN KEY (weakAgainst) REFERENCES Types(typeName))"); 
            }
            
            // Create table for strengths if it doesn't already exist 
            if (!tableExists(connection, "Strengths"))
            {
                statement.executeUpdate("CREATE TABLE Strengths(" +
                    "typeName VARCHAR(50)," +
                    "strongAgainst VARCHAR(50)," +
                    "FOREIGN KEY (typeName) REFERENCES Types(typeName)," +
                    "FOREIGN KEY (strongAgainst) REFERENCES Types(typeName))");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    // Add pokemon types into types table
    public static void populateTypesTable(Connection connection)
    {
        // Check if table is already populated
        try (Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM Trainers");
            resultSet.next();
            if (resultSet.getInt(1) > 0)
            {
                // Table is already populated
                return;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
             
        String[] types = 
        {
            "Normal", "Fire", "Water", "Electric", "Grass", "Ice", "Fighting",
            "Poison", "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost",
            "Dragon", "Dark", "Steel", "Fairy"
        };

        try
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
        pokemons.add(new Pokemon("Bulbasaur", new Type("Grass"), 45, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Ivysaur", new Type("Grass"), 60, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Venusaur", new Type("Grass"), 80, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Charmander", new Type("Fire"), 39, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Charmeleon", new Type("Fire"), 58, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Charizard", new Type("Fire"), 78, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Squirtle", new Type("Water"), 44, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Wartortle", new Type("Water"), 59, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Blastoise", new Type("Water"), 79, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Caterpie", new Type("Bug"), 45, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Metapod", new Type("Bug"), 50, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Butterfree", new Type("Bug"), 60, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Pikachu", new Type("Electric"), 35, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Raichu", new Type("Electric"), 60, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Jigglypuff", new Type("Normal"), 115, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Wigglytuff", new Type("Normal"), 140, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Meowth", new Type("Normal"), 40, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Persian", new Type("Normal"), 65, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Psyduck", new Type("Water"), 50, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Golduck", new Type("Water"), 80, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Machop", new Type("Fighting"), 70, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Machoke", new Type("Fighting"), 80, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Machamp", new Type("Fighting"), 90, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Geodude", new Type("Rock"), 40, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Graveler", new Type("Rock"), 55, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Golem", new Type("Rock"), 80, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Abra", new Type("Psychic"), 25, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Kadabra", new Type("Psychic"), 40, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Alakazam", new Type("Psychic"), 55, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Gastly", new Type("Ghost"), 30, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Haunter", new Type("Ghost"), 45, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Gengar", new Type("Ghost"), 60, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Onix", new Type("Rock"), 35, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Magikarp", new Type("Water"), 20, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Gyarados", new Type("Water"), 95, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Eevee", new Type("Normal"), 55, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Vaporeon", new Type("Water"), 130, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Jolteon", new Type("Electric"), 65, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Flareon", new Type("Fire"), 65, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Snorlax", new Type("Normal"), 160, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        pokemons.add(new Pokemon("Mewtwo", new Type("Psychic"), 106, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Mew", new Type("Psychic"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        
        pokemons.add(new Pokemon("Blaziken", new Type("Fire"), 180, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Infernape", new Type("Fire"), 176, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Lucario", new Type("Fighting"), 170, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Garchomp", new Type("Dragon"), 108, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Hydreigon", new Type("Dark"), 192, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Metagross", new Type("Steel"), 180, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Excadrill", new Type("Ground"), 110, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Togekiss", new Type("Fairy"), 185, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Salamence", new Type("Dragon"), 195, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Tyranitar", new Type("Rock"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Volcarona", new Type("Bug"), 185, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Dragonite", new Type("Dragon"), 191, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Tyrantrum", new Type("Rock"), 190, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Kyogre", new Type("Water"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Groudon", new Type("Ground"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Rayquaza", new Type("Dragon"), 105, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Zygarde", new Type("Dragon"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Arceus", new Type("Normal"), 120, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Zekrom", new Type("Electric"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Reshiram", new Type("Fire"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Kyurem", new Type("Ice"), 125, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Darkrai", new Type("Dark"), 170, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Cobalion", new Type("Steel"), 191, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Terrakion", new Type("Rock"), 191, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Virizion", new Type("Grass"), 191, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Keldeo", new Type("Water"), 191, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Giratina", new Type("Ghost"), 150, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Dialga", new Type("Steel"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Palkia", new Type("Water"), 190, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Heatran", new Type("Fire"), 191, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Landorus", new Type("Ground"), 189, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Tornadus", new Type("Flying"), 179, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Thundurus", new Type("Electric"), 179, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Goodra", new Type("Dragon"), 190, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Dragapult", new Type("Dragon"), 188, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Zeraora", new Type("Electric"), 188, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Calyrex", new Type("Psychic"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Glastrier", new Type("Ice"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Spectrier", new Type("Ghost"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Regieleki", new Type("Electric"), 180, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Regidrago", new Type("Dragon"), 100, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Chi-Yu", new Type("Fire"), 185, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Roserade", new Type("Grass"), 175, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Darmanitan", new Type("Fire"), 105, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Camerupt", new Type("Fire"), 170, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Kommo-o", new Type("Dragon"), 175, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Volcanion", new Type("Fire"), 180, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Magnezone", new Type("Electric"), 170, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));
        pokemons.add(new Pokemon("Golisopod", new Type("Bug"), 175, new ArrayList<>(), "C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\bulbasaur.png"));

        // Return list of Pokemon Objects 
        return pokemons;
    }
    
    // Populate pokemon data into pokemon table 
    public static void populatePokemonTable(List<Pokemon> pokemons, Connection connection) 
    {
        // Check if table is already populated
        try (Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM Pokemon");
            resultSet.next();
            if (resultSet.getInt(1) > 0)
            {
                // Table is already populated
                return;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        try
        {
            String sql = "INSERT INTO Pokemon (name, type, HP, originalHP, imagePath) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
            for (Pokemon p : pokemons) 
            {
                preparedStatement.setString(1, p.getName());
                preparedStatement.setString(2, p.getPokemonType().getName());
                preparedStatement.setInt(3, p.getHP());
                preparedStatement.setInt(4, p.getOriginalHP());
                preparedStatement.setString(5, p.getImagePath());

                try
                {
                  preparedStatement.executeUpdate();  
                } catch (SQLException e)
                {
                    e.printStackTrace();
                } 
            } 
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    // Populate weakAgainst types into weaknesses table
    public static void populateWeaknesses(Connection connection)
    {
        // Check if table is already populated
        try (Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM Weaknesses");
            resultSet.next();
            if (resultSet.getInt(1) > 0)
            {
                // Table is already populated
                return;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        String[][] weaknesses = 
        {
            {"Normal", "Fighting"},
            {"Fire", "Water"},
            {"Fire", "Ground"},
            {"Fire", "Rock"},
            {"Water", "Electric"},
            {"Water", "Grass"},
            {"Electric", "Ground"},
            {"Grass", "Fire"},
            {"Grass", "Ice"},
            {"Grass", "Poison"},
            {"Grass", "Flying"},
            {"Grass", "Bug"},
            {"Ice", "Fire"},
            {"Ice", "Fighting"},
            {"Ice", "Rock"},
            {"Ice", "Steel"},
            {"Fighting", "Flying"},
            {"Fighting", "Psychic"},
            {"Fighting", "Fairy"},
            {"Poison", "Ground"},
            {"Poison", "Psychic"},
            {"Ground", "Water"},
            {"Ground", "Grass"},
            {"Ground", "Ice"},
            {"Flying", "Electric"},
            {"Flying", "Ice"},
            {"Flying", "Rock"},
            {"Psychic", "Bug"},
            {"Psychic", "Ghost"},
            {"Psychic", "Dark"},
            {"Bug", "Fire"},
            {"Bug", "Flying"},
            {"Bug", "Rock"},
            {"Rock", "Water"},
            {"Rock", "Grass"},
            {"Rock", "Fighting"},
            {"Rock", "Ground"},
            {"Rock", "Steel"},
            {"Ghost", "Ghost"},
            {"Ghost", "Dark"},
            {"Dragon", "Ice"},
            {"Dragon", "Dragon"},
            {"Dragon", "Fairy"},
            {"Dark", "Fighting"},
            {"Dark", "Bug"},
            {"Dark", "Fairy"},
            {"Steel", "Fire"},
            {"Steel", "Fighting"},
            {"Steel", "Ground"},
            {"Fairy", "Poison"},
            {"Fairy", "Steel"}
        };
        
        try
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
    public static void populateStrengths(Connection connection)
    {
        // Check if table is already populated
        try (Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM Strengths");
            resultSet.next();
            if (resultSet.getInt(1) > 0)
            {
                // Table is already populated
                return;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        String[][] strengths = 
        {
            {"Fire", "Grass"},
            {"Fire", "Ice"},
            {"Fire", "Bug"},
            {"Fire", "Steel"},
            {"Water", "Fire"},
            {"Water", "Ground"},
            {"Water", "Rock"},
            {"Electric", "Water"},
            {"Electric", "Flying"},
            {"Grass", "Water"},
            {"Grass", "Ground"},
            {"Grass", "Rock"},
            {"Ice", "Grass"},
            {"Ice", "Ground"},
            {"Ice", "Flying"},
            {"Ice", "Dragon"},
            {"Fighting", "Normal"},
            {"Fighting", "Ice"},
            {"Fighting", "Rock"},
            {"Fighting", "Dark"},
            {"Fighting", "Steel"},
            {"Poison", "Grass"},
            {"Poison", "Fairy"},
            {"Ground", "Fire"},
            {"Ground", "Electric"},
            {"Ground", "Poison"},
            {"Ground", "Rock"},
            {"Ground", "Steel"},
            {"Flying", "Grass"},
            {"Flying", "Fighting"},
            {"Flying", "Bug"},
            {"Psychic", "Fighting"},
            {"Psychic", "Poison"},
            {"Bug", "Grass"},
            {"Bug", "Psychic"},
            {"Bug", "Dark"},
            {"Rock", "Fire"},
            {"Rock", "Ice"},
            {"Rock", "Flying"},
            {"Rock", "Bug"},
            {"Ghost", "Psychic"},
            {"Ghost", "Ghost"},
            {"Dragon", "Dragon"},
            {"Dark", "Psychic"},
            {"Dark", "Ghost"},
            {"Steel", "Ice"},
            {"Steel", "Rock"},
            {"Steel", "Fairy"},
            {"Fairy", "Fighting"},
            {"Fairy", "Dragon"},
            {"Fairy", "Dark"}
        };

        try
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
    public static void populateTrainers(Connection connection)
    {
        // Check if table is already populated
        try (Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM Trainers");
            resultSet.next();
            if (resultSet.getInt(1) > 0)
            {
                // Table is already populated
                return;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
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
        
        try 
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
                } catch(SQLException e)
                {  
                    e.printStackTrace();
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }   
    }
        
    // Populate moves data into moves table 
    public static void populateMoves(Connection connection) 
    {
        // Check if table is already populated
        try (Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM Moves");
            resultSet.next();
            if (resultSet.getInt(1) > 0)
            {
                // Table is already populated
                return;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
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
            {"Blaziken", "Blaze Kick", "85", "90"},
            {"Blaziken", "Sky Uppercut", "90", "85"},
            {"Blaziken", "Flare Blitz", "120", "85"},

            {"Infernape", "Close Combat", "120", "100"},
            {"Infernape", "Flame Wheel", "60", "100"},
            {"Infernape", "Mach Punch", "40", "100"},

            {"Lucario", "Aura Sphere", "80", "100"},
            {"Lucario", "Close Combat", "120", "100"},
            {"Lucario", "Bone Rush", "25", "90"},

            {"Garchomp", "Dragon Claw", "80", "100"},
            {"Garchomp", "Earthquake", "100", "100"},
            {"Garchomp", "Crunch", "80", "100"},

            {"Hydreigon", "Dark Pulse", "80", "100"},
            {"Hydreigon", "Dragon Pulse", "85", "100"},
            {"Hydreigon", "Hyper Voice", "90", "100"},

            {"Metagross", "Meteor Mash", "90", "90"},
            {"Metagross", "Zen Headbutt", "80", "90"},
            {"Metagross", "Earthquake", "100", "100"},

            {"Excadrill", "Drill Run", "80", "95"},
            {"Excadrill", "Iron Head", "80", "100"},
            {"Excadrill", "Earthquake", "100", "100"},

            {"Togekiss", "Air Slash", "75", "95"},
            {"Togekiss", "Dazzling Gleam", "80", "100"},
            {"Togekiss", "Aura Sphere", "80", "100"},

            {"Salamence", "Dragon Claw", "80", "100"},
            {"Salamence", "Fire Fang", "65", "95"},
            {"Salamence", "Crunch", "80", "100"},

            {"Tyranitar", "Stone Edge", "100", "80"},
            {"Tyranitar", "Crunch", "80", "100"},
            {"Tyranitar", "Earthquake", "100", "100"},

            {"Volcarona", "Fiery Dance", "80", "100"},
            {"Volcarona", "Bug Buzz", "90", "100"},
            {"Volcarona", "Air Slash", "75", "95"},

            {"Dragonite", "Dragon Tail", "60", "90"},
            {"Dragonite", "Outrage", "120", "100"},
            {"Dragonite", "Fire Punch", "75", "100"},

            {"Tyrantrum", "Head Smash", "150", "80"},
            {"Tyrantrum", "Dragon Claw", "80", "100"},
            {"Tyrantrum", "Earthquake", "100", "100"},

            {"Kyogre", "Water Spout", "150", "100"},
            {"Kyogre", "Surf", "90", "100"},
            {"Kyogre", "Ice Beam", "90", "100"},

            {"Groudon", "Precipice Blades", "120", "85"},
            {"Groudon", "Earthquake", "100", "100"},
            {"Groudon", "Fire Punch", "75", "100"},

            {"Rayquaza", "Dragon Ascent", "120", "100"},
            {"Rayquaza", "Extreme Speed", "80", "100"},
            {"Rayquaza", "Outrage", "120", "100"},

            {"Zygarde", "Land’s Wrath", "90", "100"},
            {"Zygarde", "Thousand Arrows", "90", "100"},
            {"Zygarde", "Dragon Pulse", "85", "100"},

            {"Arceus", "Judgment", "100", "100"},
            {"Arceus", "Extreme Speed", "80", "100"},
            {"Arceus", "Recover", "0", "100"},

            {"Zekrom", "Bolt Strike", "130", "85"},
            {"Zekrom", "Dragon Claw", "80", "100"},
            {"Zekrom", "Crunch", "80", "100"},

            {"Reshiram", "Blue Flare", "120", "85"},
            {"Reshiram", "Dragon Pulse", "85", "100"},
            {"Reshiram", "Fusion Flare", "100", "100"},

            {"Kyurem", "Ice Beam", "90", "100"},
            {"Kyurem", "Freeze Shock", "140", "90"},
            {"Kyurem", "Dragon Pulse", "85", "100"},

            {"Darkrai", "Dark Void", "0", "50"},
            {"Darkrai", "Night Daze", "85", "100"},
            {"Darkrai", "Dark Pulse", "80", "100"},

            {"Cobalion", "Iron Head", "80", "100"},
            {"Cobalion", "Sacred Sword", "90", "100"},
            {"Cobalion", "Close Combat", "120", "100"},

            {"Terrakion", "Stone Edge", "100", "80"},
            {"Terrakion", "Close Combat", "120", "100"},
            {"Terrakion", "Iron Head", "80", "100"},

            {"Virizion", "Leaf Blade", "90", "100"},
            {"Virizion", "Sacred Sword", "90", "100"},
            {"Virizion", "Close Combat", "120", "100"},

            {"Keldeo", "Secret Sword", "90", "100"},
            {"Keldeo", "Hydro Pump", "110", "80"},
            {"Keldeo", "Bubble Beam", "65", "100"},

            {"Giratina", "Shadow Force", "120", "100"},
            {"Giratina", "Dragon Claw", "80", "100"},
            {"Giratina", "Aura Sphere", "80", "100"},

            {"Dialga", "Roar of Time", "150", "90"},
            {"Dialga", "Flash Cannon", "80", "100"},
            {"Dialga", "Earth Power", "90", "100"},

            {"Palkia", "Spacial Rend", "100", "95"},
            {"Palkia", "Hydro Pump", "110", "80"},
            {"Palkia", "Dragon Pulse", "85", "100"},

            {"Heatran", "Lava Plume", "80", "100"},
            {"Heatran", "Earth Power", "90", "100"},
            {"Heatran", "Flamethrower", "90", "100"},

            {"Landorus", "Earth Power", "90", "100"},
            {"Landorus", "Stone Edge", "100", "80"},
            {"Landorus", "U-turn", "70", "100"},

            {"Tornadus", "Air Slash", "75", "95"},
            {"Tornadus", "Hurricane", "110", "70"},
            {"Tornadus", "U-turn", "70", "100"},

            {"Thundurus", "Thunderbolt", "90", "100"},
            {"Thundurus", "Discharge", "80", "100"},
            {"Thundurus", "Volt Switch", "70", "100"},

            {"Goodra", "Dragon Pulse", "85", "100"},
            {"Goodra", "Sludge Bomb", "90", "100"},
            {"Goodra", "Thunderbolt", "90", "100"},

            {"Dragapult", "Dragon Darts", "50", "100"},
            {"Dragapult", "Phantom Force", "90", "100"},
            {"Dragapult", "Dragon Pulse", "85", "100"},

            {"Zeraora", "Plasma Fists", "100", "100"},
            {"Zeraora", "Close Combat", "120", "100"},
            {"Zeraora", "Thunder Punch", "75", "100"},

            {"Calyrex", "Psycho Cut", "70", "100"},
            {"Calyrex", "Future Sight", "120", "100"},
            {"Calyrex", "Glacial Lance", "130", "90"},

            {"Glastrier", "Ice Punch", "75", "100"},
            {"Glastrier", "High Horsepower", "95", "100"},
            {"Glastrier", "Close Combat", "120", "100"},

            {"Spectrier", "Shadow Ball", "80", "100"},
            {"Spectrier", "Dark Pulse", "80", "100"},
            {"Spectrier", "Hex", "65", "100"},

            {"Regieleki", "Thunderbolt", "90", "100"},
            {"Regieleki", "Volt Switch", "70", "100"},
            {"Regieleki", "Electro Ball", "80", "100"},

            {"Regidrago", "Dragon Energy", "100", "100"},
            {"Regidrago", "Dragon Claw", "80", "100"},
            {"Regidrago", "Power Whip", "120", "90"},

            {"Chi-Yu", "Fire Blast", "110", "85"},
            {"Chi-Yu", "Flamethrower", "90", "100"},
            {"Chi-Yu", "Overheat", "130", "90"},

            {"Roserade", "Leaf Storm", "130", "90"},
            {"Roserade", "Sludge Bomb", "90", "100"},
            {"Roserade", "Energy Ball", "80", "100"},

            {"Darmanitan", "Flare Blitz", "120", "85"},
            {"Darmanitan", "Fire Punch", "75", "100"},
            {"Darmanitan", "Superpower", "120", "100"},

            {"Camerupt", "Eruption", "150", "85"},
            {"Camerupt", "Earth Power", "90", "100"},
            {"Camerupt", "Lava Plume", "80", "100"},

            {"Kommo-o", "Clanging Scales", "110", "100"},
            {"Kommo-o", "Dragon Claw", "80", "100"},
            {"Kommo-o", "Close Combat", "120", "100"},

            {"Volcanion", "Steam Eruption", "110", "95"},
            {"Volcanion", "Flamethrower", "90", "100"},
            {"Volcanion", "Earth Power", "90", "100"},

            {"Magnezone", "Zap Cannon", "120", "50"},
            {"Magnezone", "Thunderbolt", "90", "100"},
            {"Magnezone", "Flash Cannon", "80", "100"},

            {"Golisopod", "Liquidation", "85", "100"},
            {"Golisopod", "First Impression", "90", "100"},
            {"Golisopod", "Leech Life", "80", "100"}
        };

        try 
        {
            String sql = "INSERT INTO Moves (pokemonName, name, power, accuracy) VALUES (?, ?, ?, ?)";
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
    
    // Load the pokemon moves 
    public static void loadPokemonMoves(Connection connection, Pokemon pokemon)
    {
        String sql = "SELECT name, power, accuracy FROM Moves WHERE pokemonName = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, pokemon.getName());
            
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                ArrayList<Move> moves = new ArrayList<>();
            
                while(resultSet.next())
                {
                    String name = resultSet.getString("name");
                    int power = resultSet.getInt("power");
                    double accuracy = resultSet.getDouble("accuracy");
                    moves.add(new Move(name, power, accuracy));
                }
                pokemon.setMoves(moves);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }        
    }
    
    
    // Get method to return a list of all trainers 
    public static List<Trainer> getAllTrainers(Connection connection)
    {
        List<Trainer> trainers = new ArrayList<>();
        String sql = "SELECT * FROM Trainers";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery())
        {
            
            // Store trainer data in a temporary list
            List<String[]> temporaryTrainers = new ArrayList<>();
            
            while (resultSet.next())
            {
                temporaryTrainers.add(new String[]
                {
                    resultSet.getString("username"),
                    resultSet.getString("name"),
                    String.valueOf(resultSet.getInt("score")),
                    String.valueOf(resultSet.getInt("level")),
                    resultSet.getString("starterPokemon"),
                    resultSet.getString("challengeMessage")
                });
            }
            // Safely create trainer objects with result set closed 
            for (String[] data : temporaryTrainers)
            {
                trainers.add(new Trainer(
                    data[0],
                    data[1],
                    Integer.parseInt(data[2]),
                    Integer.parseInt(data[3]), 
                    getPokemon(connection, data[4]),
                    data[5]
                ));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return trainers;
    }
    
    // Get method to return a trainer whose name matches the query 
    public static Trainer getTrainer(String username, Connection connection) 
    {
        Trainer trainer = null;
        String sql = "SELECT username, name, score, level, starterPokemon, challengeMessage FROM Trainers WHERE username = ?";

        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) 
        {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            
            if (resultSet.next()) 
            {
                Pokemon starter = getPokemon(connection, resultSet.getString("starterPokemon"));
                
                trainer = new Trainer
                (
                    resultSet.getString("username"),
                    resultSet.getString("name"),
                    resultSet.getInt("score"),
                    resultSet.getInt("level"),
                    starter,
                    resultSet.getString("challengeMessage")
                );
            }   
            resultSet.close();
            preparedStatement.close();
                    
        } catch (SQLException e) 
        {
            System.out.println("Professor Oak: Could not fetch trainer! " + e.getMessage());
        }
        
        return trainer; 
    }
    
    // Get method to return a pokemon whose name matches the query 
    public static Pokemon getPokemon(Connection connection, String pokemonName) 
    {
        try
        {
            String sql = "SELECT * FROM Pokemon WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, pokemonName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) 
            {
                Pokemon pokemon = new Pokemon
                (
                    resultSet.getString("name"),
                    TypeStorage.getTypeByName(resultSet.getString("type")),
                    resultSet.getInt("HP"),
                    new ArrayList<>(),
                    resultSet.getString("imagePath")
                );
                
                loadPokemonMoves(connection, pokemon);
                return pokemon;
            }
        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return null;
    }   
    
    // Drop tables that exist already and re-create
    private static void dropTableIfExists(Statement statement, String tableName)
    {
        try
        {
            statement.executeUpdate("DROP TABLE " + tableName);
        } catch (SQLException e)
        {
            // If the table does not exist, it is safe to ignore 
            if (!"42Y55".equals(e.getSQLState()))
            {
                e.printStackTrace();
            }
        }
    }
    
    // Load types with effectiveness (strongAgainst and weakAgainst)
    public static ArrayList<Type> loadTypesWithEffectiveness(Connection connection) throws SQLException
    {
        // Create an array list to store type objects 
        ArrayList<Type> types = new ArrayList<>();

        try
        {
            // Query all type names from the types table 
            String sql = "SELECT typeName FROM Types";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Loop through each row in the resultSet
            while (resultSet.next())
            {
                // Create a new type object for each typeName retrieved
                Type type = new Type(resultSet.getString("typeName"));
                // Add the created type to the list
                types.add(type);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        // Link the strongAgainst types 
        try
        {
            // Query the type name of all strongAgainst relationships in the strengths table
            String sql = "SELECT typeName, strongAgainst FROM Strengths";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Loop through each row in the result set (relationship between two types)
            while (resultSet.next())
            {
                // Save the attacking type into a type object
                Type type = findType(types, resultSet.getString("typeName"));
                // Save the defending type into another type object 
                Type strongAgainst = findType(types, resultSet.getString("strongAgainst"));
                
                // If both of the types exists, set the strongAgainst relationship
                if (type != null && strongAgainst != null)
                {
                    type.setStrongAgainst(strongAgainst);
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        // Link the weakAgainst types 
        try
        {
            // Query the type name of all weakAgainst relationships in the weaknesses table
            String sql = "SELECT typeName, weakAgainst FROM Weaknesses";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Loop through each row in the result set (relationship between two types)
            while (resultSet.next())
            {
                // Save the attacking type into a type object
                Type type = findType(types, resultSet.getString("typeName"));
                // Save the defending type into another type object 
                Type weakAgainst = findType(types, resultSet.getString("weakAgainst"));
                
                // If both of the types exists, set the strongAgainst relationship
                if (type != null && weakAgainst != null)
                {
                    type.setWeakAgainst(weakAgainst);
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        TypeStorage.setTypes(types);
        
        return types;
    }
    
    // Check if type name is found in the list of types 
    private static Type findType(List<Type> types, String name) 
    {
        for (Type type : types) 
        {
            if (type.getName().equals(name)) 
                
            return type;
        }
        
        return null;
    }
    
    // Update trainer score in database
    public static void updateTrainerScore(Connection connection, String username, int newScore)
    {
        String sql = "UPDATE Trainers SET score = ? WHERE username = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setInt(1, newScore);
            preparedStatement.setString(2, username);
            
            int rowsUpdated = preparedStatement.executeUpdate();
            
            if (rowsUpdated == 0)
            {
                System.out.println("Professor Oak: I don't remember meeting any trainers with that username!");
            }   
        } catch (SQLException e)
        {
            System.out.println("Professor Oak: Whoops! I forgot which score I was supposed to save! " + e.getMessage());
        }
    }        
    
    // Get trainer score from database 
    public static int getTrainerScore(Connection connection, String username)
    {
        String sql = "SELECT score FROM Trainers WHERE username = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, username);
            
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt("score");
                } else
                {
                    System.out.println("Professor Oak: I don't remember meeting any trainers with that username!");
                }
            } catch (SQLException e)
            {
                System.out.println("Professor Oak: Whoops! I forgot where I saved your score! " + e.getMessage());
            }  
        } catch (SQLException e)
        {
            System.out.println("Professor Oak: Whoops! I forgot where I saved your score! " + e.getMessage());
        }  
        
        // Return default score of 0 if not found;
        return 0;
    }
    
    // Check if table already exists 
    public static boolean tableExists(Connection connection, String tableName) throws SQLException
    {
        DatabaseMetaData metaData = connection.getMetaData();
        
        try(ResultSet tables = metaData.getTables(null, null, tableName.toUpperCase(), null))
        {
            return tables.next();
        }
    }
}
