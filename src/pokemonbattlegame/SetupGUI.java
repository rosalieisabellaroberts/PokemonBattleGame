/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.List;
import java.sql.*;

/**
 *
 * @author dilro
 */
public class SetupGUI extends JFrame 
{
    private JTextArea battleLog;
    private JTextField inputField;
    private InputListener listener;
    private JPanel pokeballPanel;
    // Allow space for 4 pokeballs (in event pikachu is selected)
    private JLabel[] pokeballLabels = new JLabel[4];
    
    // Card layout for multiple screens
    private JPanel cards;
    private CardLayout cardLayout;
    private BattleGUI battleGUI;
    
    // Create blocking queue for input to enable waiting for user inout 
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    
    public SetupGUI()
    {   
        // WINDOW VIEW 
        // Set window size dynamically
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
       
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = screenWidth / 2;
        int frameHeight = screenHeight / 2;
        
        this.setSize(frameWidth, frameHeight);
        setTitle("Pokemon Battle Game!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // CARD LAYOUT STRUCTURE
        // Create card layout container 
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        add(cards);
        
        // SETUP SCREEN PANEL
        // Create a panel for the current screen (setupGame)
        JPanel setupScreen = new JPanel(new BorderLayout());
          
        // OUTPUT AREA IN WINDOW VIEW (TOP LEFT)
        // Create a new JTextArea
        battleLog = new JTextArea();
        // Set as not editable for users, only showing displaying battle log output
        battleLog.setEditable(false);
        battleLog.setFont(new Font("Consolas", Font.PLAIN, 14));
        // Add black border 
        battleLog.setBorder(new LineBorder(Color.BLACK, 3)); 
        // Add battleLog output area to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(battleLog);
        
        // PROFESSOR OAK IMAGE IN WINDOW VIEW (TOP RIGHT)
        ImageIcon oakIcon = new ImageIcon("C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\oak.png");
        JLabel oakLabel = new JLabel(oakIcon);
        oakLabel.setHorizontalAlignment(JLabel.CENTER);
        oakLabel.setVerticalAlignment(JLabel.CENTER);
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(oakLabel, BorderLayout.CENTER);
        
        // DIVIDER FOR TOP WINDOW VIEW (OUTPUT TEXT & OAK IMAGE)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, imagePanel);
        splitPane.setResizeWeight(0.7);
        splitPane.setDividerLocation(0.7);
        splitPane.setOneTouchExpandable(true);
        setupScreen.add(splitPane, BorderLayout.CENTER);
        
        // MIDDLE PANEL WITH POKEBALLS
        pokeballPanel = new JPanel();
        pokeballPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pokeballPanel.setBackground(new Color(220, 220, 220));
        pokeballPanel.setBorder(new LineBorder(Color.BLACK, 2));
        
        // Create labels to hold 4 pokeballs 
        for (int i = 0; i < 4; i++)
        {
            pokeballLabels[i] = new JLabel();
            
            ImageIcon pokeball = new ImageIcon("C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\pokeballClosed.png");
            Image scaled = pokeball.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);        
            
            pokeballLabels[i].setIcon(new ImageIcon(scaled));
            pokeballLabels[i].setPreferredSize(new Dimension(50, 50));
            pokeballPanel.add(pokeballLabels[i]);
        }
        pokeballLabels[3].setEnabled(false);
              
        // INPUT FIELD IN WINDOW VIEW (BOTTOM)
        inputField = new JTextField();
        inputField.setFont(new Font("Consolas", Font.PLAIN, 14));
        inputField.setPreferredSize(new Dimension(inputField.getPreferredSize().width, 80));
        inputField.setBorder(new LineBorder(Color.BLACK, 3));
        
        // CONTAINER PANEL FOR MIDDLE PANEL AND BOTTOM FIELD
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(pokeballPanel, BorderLayout.CENTER);
        bottomPanel.add(inputField, BorderLayout.SOUTH);
        setupScreen.add(bottomPanel, BorderLayout.SOUTH);

        // Add action listener for enter key 
        inputField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Get and trim user input from inputField
                String userInput = inputField.getText().trim();
                
                // If there is user input 
                if (!userInput.isEmpty())
                {
                    // Append user message to output area 
                    appendMessage("You: " + userInput);
                    // Reset text field
                    inputField.setText("");
                    
                    // Add input to queue for the BattleManager
                    inputQueue.offer(userInput);
                    
                    // If event listener is not null
                    if (listener != null)
                    {
                        // Pass user input to game logic 
                        listener.onInputSubmitted(userInput);
                    }
                }
            }         
        });
        
        // Add SetupScreen to cards 
        cards.add(setupScreen, "SetupScreen");
        cardLayout.show(cards, "SetupScreen");
     
        setVisible(true); 
        
        // Add cards to the JFrame
        add(cards);
    }
    
    // Method for adding messages to the window 
    public void appendMessage(String message)
    {
        battleLog.append(message + "\n");
        
        // Auto scroll in output area
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }
    
    // Get access to BattleGUI
    public BattleGUI getBattleGUI()
    {
        return battleGUI;
    }
    
    // Set input listener for game logic
    public void setInputListener(InputListener listener)
    {
        this.listener = listener;
    }
    
    // Input callbacks 
    public interface InputListener
    {
        void onInputSubmitted(String input);
    }
    
    // Reveal pokemon images
    public void setPokeballImage(int index, String imagePath)
    {
        // If the index is out of range, return
        if (index < 0 || index >= pokeballLabels.length)
        {
            return;
        }
        
        // Create a new image icon object from the specified photo path
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaled = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        
        // Set new image icon at specified index
        pokeballLabels[index].setIcon(new ImageIcon(scaled));
    }
    
    // Enable pikachu Image (prevent from being greyed out)
    public void enablePikachuImage() 
    {
        pokeballLabels[3].setEnabled(true);
    }
    
    // Show a card by name
    public void showScreen(String name)
    {
        cardLayout.show(cards, name);
    }
    
    // Add a new card
    public void addCard(JPanel panel, String name)
    {
        cards.add(panel, name);
    }
    
    // Blocks execution of calling thread until user types in input field and presses enter
    public String waitForInput()
    {
        // Try taking an element from the input queue
        try 
        {
            // If queue is empty, call to thread will pause until user input becomes available
            return inputQueue.take();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            
            // If interrupted exception is thrown, return an empty string to avoid crashing
            return "";
        }
    }
    
    // Create method to create and show the BattleGUI
    public void createAndShowBattleGUI(Trainer trainer, Trainer opponent, Battle battle, Connection connection, TrainerTurn trainerTurn)
    {
        // Create a single BattleGUI instance for the current battle
        battleGUI = new BattleGUI(trainer.getStarterPokemon(), opponent.getStarterPokemon(), opponent.getImagePath(), this, battle);

        // Add the BattleGUI instance as a card in the card layout
        addCard(battleGUI, "BattleGUI");

        // Show the BattleGUI card
        showScreen("BattleGUI");
    }  
    
    // Getter and setter methods
    public JPanel getCards()
    {
        return cards;
    }

    public CardLayout getCardLayout()
    {
        return cardLayout;
    }
}

