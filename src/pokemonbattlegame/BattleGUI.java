/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 *
 * @author dilro
 */

public class BattleGUI extends JPanel
{

    private JTextArea battleLog;
    private JLabel opponentTrainerLabel;
    private JLabel opponentPokemonLabel;
    private JLabel trainerPokemonLabel;
    private JButton fightButton;
    private JButton switchButton;
    private JButton runButton;
    private Image backgroundImage;
    private JPanel optionsPanel;
    private SetupGUI setupGUI;
    private Battle battle;
    private TrainerTurn trainerTurn;

    public BattleGUI(Pokemon trainerPokemon, Pokemon opponentPokemon, String opponentImagePath, SetupGUI setupGUI, Battle battle)
    {
        this.setupGUI = setupGUI;
        this.battle = battle;
        
        // Set border layout for GUI
        setLayout(new BorderLayout());

        // Load the background image
        backgroundImage = new ImageIcon("C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\battleBackground.jpg").getImage();
        
        // TOP PANEL (opponent trainer and pokemon)
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 20));
        
        // WRAPPER FOR TOP PANEL 
        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        topWrapper.setOpaque(false);
        topWrapper.add(topPanel);
        add(topWrapper, BorderLayout.NORTH);
        
        // Opponent trainer label
        opponentTrainerLabel = new JLabel(new ImageIcon(
            new ImageIcon(opponentImagePath).getImage().getScaledInstance(100,100, Image.SCALE_SMOOTH)
        ));
        opponentTrainerLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        topPanel.add(opponentTrainerLabel);
        
        // Opponent pokemon label
        opponentPokemonLabel = new JLabel(new ImageIcon(
            new ImageIcon(opponentPokemon.getImagePath()).getImage().getScaledInstance(180,180, Image.SCALE_SMOOTH)
        ));
        opponentPokemonLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(opponentPokemonLabel);
        
        // BOTTOM LEFT PANEL (trainer pokemon)
        JPanel trainerPokemonPanel = new JPanel();
        trainerPokemonPanel.setOpaque(false);
        trainerPokemonPanel.setLayout(new BoxLayout(trainerPokemonPanel, BoxLayout.Y_AXIS));
        
        // TRAINER POKEMON IMAGE
        trainerPokemonLabel = new JLabel(new ImageIcon(
            new ImageIcon(trainerPokemon.getImagePath()).getImage().getScaledInstance(180,180, Image.SCALE_SMOOTH)
        ));
        trainerPokemonLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        trainerPokemonPanel.add(trainerPokemonLabel);
        trainerPokemonPanel.add(Box.createVerticalStrut(15));
        
        // BOTTOM LEFT OUTPUT AREA (battleLog)
        battleLog = new JTextArea(10, 40);        
        battleLog.setEditable(false);
        battleLog.setFont(new Font("Consolas", Font.PLAIN, 14));
        battleLog.setOpaque(false);
        battleLog.setForeground(Color.WHITE);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
        
        JScrollPane scrollPane = new JScrollPane(battleLog, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setPreferredSize(new Dimension(400, 120));
        trainerPokemonPanel.add(scrollPane);
        
        // BOTTOM RIGHT PANEL (buttons)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 5));
        buttonPanel.setOpaque(false);
        // Create buttons
        fightButton = new JButton("FIGHT");
        switchButton = new JButton("SWITCH");
        runButton = new JButton("RUN");

        // ADD HOVER EFFECT TO BUTTONS
        JButton[] buttons = {fightButton, switchButton, runButton};
        // For every button in the JButton array
        for(JButton button : buttons)
        {
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Consolas", Font.PLAIN, 18));
            
            // Create mouse listener for hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() 
            {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e)
                {
                    // Colour for hover effect 
                    button.setForeground(Color.decode("#22445a"));
                }
                
                @Override
                public void mouseExited(java.awt.event.MouseEvent e)
                {
                    // Default colour for button
                    button.setForeground(Color.decode("#fff9fd"));
                }
            });
            
            buttonPanel.add(button);
        }
        
        // HORIZONTAL PANEL TO HOLD BOTTOM LEFT BATLE LOG AND RIGHT BUTTONS
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);
        bottomRow.add(trainerPokemonPanel, BorderLayout.WEST);
        bottomRow.add(buttonPanel, BorderLayout.EAST);
        
        // OPTIONS PANEL (for once FIGHT or SWITCH is selected)
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 2, 5, 5));
        optionsPanel.setOpaque(false);
        optionsPanel.setVisible(false);
        
        // BOTTOM PANEL TO ANCHOR COMPONENTS 
        JPanel bottomAnchorPanel = new JPanel(new BorderLayout());
        bottomAnchorPanel.setOpaque(false);
        bottomAnchorPanel.add(bottomRow, BorderLayout.CENTER);
        bottomAnchorPanel.add(optionsPanel, BorderLayout.SOUTH);
       
        // ADD BOTTOM PANEL TO WINDOW VIEW
        add(bottomAnchorPanel, BorderLayout.SOUTH);
    }

    // Method for painting background image
    @Override
    protected void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        graphics.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
    
    // Method for adding messages to the window 
    public void appendMessage(String message)
    {
        // Make method thread-safe by always posting to event dispatch thread
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                battleLog.append(message + "\n");
                
                 // Auto-scrolling for battleLog view
                battleLog.setCaretPosition(battleLog.getDocument().getLength());
            }
        });
    }
    
    // Action listeners for buttons
    public void setButtonListeners(ActionListener fightListener, ActionListener switchListener, ActionListener runListener)
    {
        // Action listener for fight button
        fightButton.addActionListener(fightListener);
        switchButton.addActionListener(switchListener);
        runButton.addActionListener(runListener);
    }
    
    // Show pokemon move options 
    public void showMoveOptions(List<Move> moves, ActionListener moveListener)
    {
        // Clear previous buttons
        optionsPanel.removeAll();
        
        // For all moves in the array
        for(int i = 0; i < moves.size(); i++)
        {
            // Create a move object
            Move move = moves.get(i);
            // Create a button for the move object 
            JButton moveButton = new JButton(move.getName());
            //Add action command to move index
            moveButton.setActionCommand("" + i);
            // Add action listener to move button
            moveButton.addActionListener(moveListener);
            // Add button to options panel 
            optionsPanel.add(moveButton);
        }
        
        // Set all main buttons to not be visible 
        fightButton.setVisible(false);
        switchButton.setVisible(false);
        runButton.setVisible(false);
        // Set the options panel to be visible
        optionsPanel.setVisible(true);
        // Have layout manager redo the layout to update changed components
        revalidate();
        // Redraw panel on the screen
        repaint();
    }
    
    // Show pokemon team options 
    public void showPokemonTeamOptions(List<Pokemon> team, ActionListener pokemonListener)
    {
        // Clear previous buttons
        optionsPanel.removeAll();
        
        // For all pokemon in the team
        for(int i = 0; i < team.size(); i++)
        {
            // Create a pokemon object
            Pokemon pokemon = team.get(i);
            // Create a button for the pokemon object 
            JButton pokemonButton = new JButton(pokemon.getName() + (pokemon.getHP() <= 0 ? "[FNT]" : ""));
            // Only enable pokemon with HP greater than 0
            pokemonButton.setEnabled(pokemon.getHP() > 0);
            // Set action command to pokemon index
            pokemonButton.setActionCommand("" + i);
            // Add action listener to pokemon button
            pokemonButton.addActionListener(pokemonListener);
            // Add pokemon button to options panel
            optionsPanel.add(pokemonButton);
        }

        // Set all main buttons to not be visible 
        fightButton.setVisible(false);
        switchButton.setVisible(false);
        runButton.setVisible(false);
        optionsPanel.setVisible(true);
        // Have layout manager redo the layout to update changed components
        revalidate();
        // Redraw panel on the screen
        repaint();
    }
    
    // Hide dynamic options and return to main buttons
    public void hideOptions()
    {
        // Set options panel to not be visible
        optionsPanel.setVisible(false);
        // Set all main buttons to be visible 
        fightButton.setVisible(true);
        switchButton.setVisible(true);
        runButton.setVisible(true);
        // Have layout manager redo the layout to update changed components
        revalidate();
        // Redraw panel on the screen
        repaint();
    }       
    
    // Update trainer pokemon image 
    public void updateTrainerPokemonImage()
    {
        trainerPokemonLabel.setIcon(new ImageIcon(new ImageIcon(battle.getTrainerCurrentPokemon().getImagePath()).getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
    }
    
    // Update opponent pokemon image
    public void updateOpponentPokemonImage()
    {
        opponentPokemonLabel.setIcon(new ImageIcon(new ImageIcon(battle.getOpponentCurrentPokemon().getImagePath()).getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
    }
    
    // Setter method for battle
    public void setBattle(Battle battle) 
    {
        this.battle = battle;
    }
    
    // Getter metthods for main buttons
    public JButton getFightButton() 
    {
        return fightButton;
    }

    public JButton getSwitchButton() 
    {
        return switchButton;
    }

    public JButton getRunButton() 
    {
        return runButton;
    }
    
    // Disable main buttons so players cannot click any buttons while turn is being taken
    public void disableMainButtons() 
    {
        fightButton.setEnabled(false);
        switchButton.setEnabled(false);
        runButton.setEnabled(false);
    }
    
    // Enable main buttons again once turn has been taken 
    public void enableMainButtons() 
    {
        fightButton.setEnabled(true);
        switchButton.setEnabled(true);
        runButton.setEnabled(true);
    }
    
    // Update BattleGUI's reference to active trainer turn instance
    public void updateTrainerTurn(TrainerTurn newTrainerTurn) 
    {
        this.trainerTurn = newTrainerTurn;
    }
    
    // Getter for trainerTurn
    public TrainerTurn getTrainerTurn() 
    {
        return trainerTurn;
    }
}