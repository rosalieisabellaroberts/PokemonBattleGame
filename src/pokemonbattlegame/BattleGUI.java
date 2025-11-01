/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author dilro
 */
public class BattleGUI extends JFrame 
{
    private JTextArea battleLog;
    
    public BattleGUI()
    {
        // WINDOW VIEW 
        // ___________________________________________________________________________________
        // Set window size dynamically
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
       
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = screenWidth / 2;
        int frameHeight = screenHeight / 2;
        
        this.setSize(frameWidth, frameHeight);
    
        // Set window title
        setTitle("Pokemon Battle Game!");
        
        // Set closing options 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // OUTPUT AREA IN WINDOW VIEW 
        // ___________________________________________________________________________________
        // Create a new JTextArea
        battleLog = new JTextArea();
        
        // Set as not editable for users, only showing displaying battle log output
        battleLog.setEditable(false);
        battleLog.setFont(new Font("Consolas", Font.PLAIN, 14));
        
        // Add battleLog output area to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(battleLog);
        
        // Add scrollPane to the window 
        add(scrollPane);
        
        setVisible(true);          
    }
    
    // Method for adding messages to the window 
    public void appendMessage(String message)
    {
        battleLog.append(message + "\n");
    }
}
