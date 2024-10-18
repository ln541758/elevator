package view;

import controller.BuildingController;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * The StartView class is the view for the building initialization.
 */
public class StartView extends JFrame {
  private final JPanel cards;
  private JComboBox<String> floor;
  private JComboBox<String> elevator;
  private JComboBox<String> capacity;
  private JButton startButton;
  private JButton exitButton;
  private BuildingController controller;

  /**
   * Constructor for the StartView class.
   */
  public StartView() {
    super("BUILDING SYSTEM");
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(0, 0, 1500, 900);

    // Left Panel setup
    cards = new JPanel(new CardLayout());
    JPanel welcomePanel = createWelcomePanel();
    JPanel instructionPanel = createInstructionPanel();
    cards.add(welcomePanel, "WELCOME");
    cards.add(instructionPanel, "INSTRUCTIONS");
    cards.setPreferredSize(new Dimension(750, 900));

    // Right Panel setup
    JPanel rightPanel = new JPanel();
    rightPanel.setBackground(Color.WHITE);
    rightPanel.setLayout(new GridLayout(5, 2, 10, 10));

    // Adding components to the right panel
    setupRightPanel(rightPanel);

    // Adding panels to the frame using BorderLayout
    add(cards, BorderLayout.WEST);
    add(rightPanel, BorderLayout.CENTER);

  }

  /**
   * Create the welcome panel.
   *
   * @return the welcome panel
   */
  private JPanel createWelcomePanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.BLACK);

    // Create the welcome label
    JLabel welcomeLabel = new JLabel("Build your own Elevator System!", SwingConstants.CENTER);
    welcomeLabel.setForeground(Color.WHITE);
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 40));
    welcomeLabel.setOpaque(true);
    welcomeLabel.setBackground(Color.BLACK);
    panel.add(welcomeLabel, BorderLayout.CENTER);

    // Create the button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setBackground(Color.BLACK);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 50, 0));

    // Create the switch button
    JButton switchButton = new JButton("Show Instructions");
    switchButton.setOpaque(true);
    switchButton.setContentAreaFilled(true);
    switchButton.setBackground(Color.WHITE);
    switchButton.setForeground(Color.BLACK);
    switchButton.setPreferredSize(new Dimension(200, 50));

    // Switch to the instruction panel when the button is clicked
    switchButton.addActionListener(e -> SwingUtilities.invokeLater(() ->
        ((CardLayout) cards.getLayout()).show(cards, "INSTRUCTIONS")
    ));

    buttonPanel.add(switchButton);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    return panel;
  }

  /**
   * Create the instruction panel.
   *
   * @return the instruction panel
   */
  private JPanel createInstructionPanel() {
    JPanel panel = new JPanel();
    panel.setBackground(Color.BLACK);
    panel.setLayout(new BorderLayout());
    String html = "<html><div style='text-align: center;'>"
        + "<span style='font-size: 20pt; font-weight: bold;'>"
        + "Ready to manage your building's elevators efficiently?"
        + "</span><br><br><br><br><br><br><br><br><br><br><br>"
        + "<span style='font-size: 16pt;'>1. Select your number of floors.</span><br><br><br>"
        + "<span style='font-size: 16pt;'>2. Select your number of elevators.</span><br><br><br>"
        + "<span style='font-size: 16pt;'>3. Select your elevator capacity.</span><br><br><br>"
        + "<span style='font-size: 16pt;'>4. Click 'START' to initialize.</span><br><br><br>"
        + "<span style='font-size: 16pt;'>5. Click 'EXIT' to quit.</span></div></html><br>";

    // Create the instruction label
    JLabel instructionLabel = new JLabel(html);
    instructionLabel.setForeground(Color.WHITE);
    instructionLabel.setHorizontalAlignment(JLabel.CENTER);
    instructionLabel.setVerticalAlignment(JLabel.CENTER);
    panel.add(instructionLabel, BorderLayout.CENTER);

    // Create the button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setBackground(Color.BLACK);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 50, 0));

    // Create the back button
    JButton backButton = new JButton("BACK");
    backButton.setOpaque(true);
    backButton.setContentAreaFilled(true);
    backButton.setBackground(Color.WHITE);
    backButton.setForeground(Color.BLACK);
    backButton.setPreferredSize(new Dimension(200, 50));
    backButton.addActionListener(e -> SwingUtilities.invokeLater(() ->
        ((CardLayout) cards.getLayout()).show(cards, "WELCOME")
    ));

    buttonPanel.add(backButton);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    return panel;
  }

  /**
   * Set up the right panel.
   *
   * @param rightPanel the right panel to setup
   */
  private void setupRightPanel(JPanel rightPanel) {
    rightPanel.setLayout(new GridLayout(6, 1, 10, 10));
    rightPanel.setBackground(new Color(245, 245, 245));

    // Add padding to the top of the panel
    JPanel topPadding = new JPanel();
    topPadding.setOpaque(false);
    topPadding.setPreferredSize(new Dimension(200, 5));
    rightPanel.add(topPadding);

    // Add the combo boxes for the number of floors, elevators, and capacity
    floor = new JComboBox<>(new String[]{"3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
        "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
        "27", "28", "29", "30"});
    elevator = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7"});
    capacity = new JComboBox<>(new String[]{ "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"});

    // Add the combo boxes to the right panel
    rightPanel.add(createLabeledComboPanel("Number of Floors:", floor));
    rightPanel.add(createLabeledComboPanel("Number of Elevators:", elevator));
    rightPanel.add(createLabeledComboPanel("Elevator Capacity:", capacity));

    // Add padding to the bottom of the panel
    JPanel bottomPanel = new JPanel();
    bottomPanel.setOpaque(false);
    bottomPanel.setBackground(new Color(245, 245, 245));
    bottomPanel.setPreferredSize(new Dimension(200, 5));
    rightPanel.add(bottomPanel);

    // Add the start and exit buttons
    startButton = new JButton("START");
    exitButton = new JButton("EXIT");
    JPanel buttonPanel = createButtonPanel(startButton, exitButton);
    rightPanel.add(buttonPanel);

  }

  /**
   * Create a panel with the start and exit buttons.
   *
   * @param startButton the start button
   * @param exitButton  the exit button
   *
   * @return          the panel with the buttons
   */
  private JPanel createButtonPanel(JButton startButton, JButton exitButton) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 150, 10));
    panel.setBackground(new Color(245, 245, 245));

    configureButton(startButton, "START", new Dimension(150, 50));
    configureButton(exitButton, "EXIT", new Dimension(150, 50));

    panel.add(startButton);
    panel.add(exitButton);

    return panel;
  }

  /**
   * Configure the button with the given text and size.
   *
   * @param button the button to configure
   * @param text   the text to set
   * @param size   the size of the button
   */
  private void configureButton(JButton button, String text, Dimension size) {
    button.setText(text);
    button.setPreferredSize(size);
    button.setBackground(Color.BLACK);
    button.setForeground(Color.WHITE);
    button.setOpaque(true);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setMargin(new Insets(10, 20, 10, 20));
  }

  /**
   * Create a panel with a labeled combo box.
   *
   * @param label     the label for the combo box
   * @param comboBox  the combo box
   *
   * @return          the panel with the labeled combo box
   */
  private JPanel createLabeledComboPanel(String label, JComboBox<String> comboBox) {
    JPanel outerPanel = new JPanel(new BorderLayout());
    outerPanel.setBackground(new Color(245, 245, 245));

    // Create the box label
    JLabel boxLabel = new JLabel(label);
    boxLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    boxLabel.setPreferredSize(new Dimension(150, 30));

    // Create the label panel
    JPanel labelPanel = createConfiguredPanel(boxLabel, new Color(245, 245, 245), "RIGHT", 80, 100);

    // Create the combo box panel
    comboBox.setPreferredSize(new Dimension(200, 30));
    JPanel comboPanel = createConfiguredPanel(comboBox, new Color(245, 245, 245), "LEFT", 80, 100);

    outerPanel.add(labelPanel, BorderLayout.WEST);
    outerPanel.add(comboPanel, BorderLayout.CENTER);

    return outerPanel;
  }

  /**
   * Create a panel with the given component, background color, alignment,
   * horizontal gap, and vertical gap.
   *
   * @param component     the component to add
   * @param bgColor       the background color
   * @param alignment     the alignment
   * @param horizonGap    the horizontal gap
   * @param verticalGap   the vertical gap
   *
   * @return          the panel with the component
   */
  private JPanel createConfiguredPanel(Component component, Color bgColor, String alignment,
                                       int horizonGap, int verticalGap) {
    JPanel panel = new JPanel(new FlowLayout(
        "LEFT".equals(alignment) ? FlowLayout.LEFT : FlowLayout.RIGHT, horizonGap, verticalGap));
    panel.setBackground(bgColor);
    panel.add(component);
    return panel;
  }

  /**
   * Set the visibility of the view.
   */
  public void setVisible() {
    this.setVisible(true);
  }

  /**
   * Add a listener to the view.
   *
   * @param controller the controller to add
   */
  public void addListener(BuildingController controller) {
    this.controller = controller;
    exitButton.addActionListener(e -> System.exit(0));
    startButton.addActionListener(e -> {
      try {
        String selectedFloors = (String) floor.getSelectedItem();
        String selectedElevators = (String) elevator.getSelectedItem();
        String selectedCapacity = (String) capacity.getSelectedItem();
        controller.initializeBuilding(selectedFloors, selectedElevators, selectedCapacity);
        dispose();
      } catch (NullPointerException ex) {
        displayMessage("Error: " + ex.getMessage());
      }
    });
  }

  /**
   * Display a message.
   *
   * @param message the message to be displayed
   */
  public void displayMessage(String message) {
    JOptionPane.showMessageDialog(this, message,
        "Message", JOptionPane.INFORMATION_MESSAGE);
  }
}
