/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.util.ArrayList;

/**
 *
 * @author dilro
 */
public class TypeStorage 
{
    private static ArrayList<Type> types;
    
    public static void setTypes(ArrayList<Type> loadedTypes)
    {
        types = loadedTypes;
    }
    
    public static ArrayList<Type> getTypes()
    {
        return types;
    }
    
    public static Type getTypeByName(String name)
    {
        for (Type type : types)
        {
            if (type.getName().equalsIgnoreCase(name))
            {
                return type;
            }
        }
        return null;
    }
}
