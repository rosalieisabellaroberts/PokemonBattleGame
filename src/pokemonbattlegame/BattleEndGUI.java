/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BattleEndGUI extends JPanel
{
    private JButton battleAgainButton;
    private JButton exitButton;
    
    public BattleEndGUI()
    {
        setLayout(new BorderLayout());
        
        // EXIT MESSAGE
        JLabel exitMessage = new JLabel("Do you wanna be the very best, like no one ever was?", SwingConstants.CENTER);
        exitMessage.setFont(new Font("Arial", Font.BOLD, 16));
        add(exitMessage, BorderLayout.CENTER);
        
        // BUTTON PANEL
        JPanel buttonPanel = new JPanel();
        
        // PLAY AGAIN BUTTON
        battleAgainButton = new JButton("Battle Again!");
        buttonPanel.add(battleAgainButton);
        
        // EXIT BUTTON
        exitButton = new JButton("Exit");
        buttonPanel.add(exitButton);
                
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Set action listeners for buttons
    public void setBattleAgainListener(ActionListener listener) 
    {
        battleAgainButton.addActionListener(listener);
    }

    public void setExitListener(ActionListener listener) 
    {
        exitButton.addActionListener(listener);
    }
   
}


