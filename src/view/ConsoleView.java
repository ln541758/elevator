package view;

import building.BuildingReport;
import building.enums.ElevatorSystemStatus;
import controller.BuildingController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * The ConsoleView class is the view for the building simulation.
 */
public class ConsoleView extends JFrame {
  public JLabel[] scheduledStops;
  public JLabel[] currentDirections;
  private final StartView startView;
  private final JLabel[][] liftWays;
  private final int floors;
  private final int elevators;
  private final int liftWaySize;
  private BuildingController controller;
  private JLabel[] elevatorDirection;
  private JTextField upLabel;
  private JTextField downLabel;
  private JTextField systemStatus;

  /**
   * Constructor for the ConsoleView class.
   *
   * @param floors    The number of floors in the building.
   * @param elevators The number of elevators in the building.
   * @param capacity  The capacity of each elevator.
   * @param controller The controller for the building.
   * @param startView The start view for the building.
   */
  public ConsoleView(int floors, int elevators, int capacity, BuildingController controller,
                     StartView startView) {
    this.controller = controller;
    this.startView = startView;
    this.floors = floors;
    this.elevators = elevators;
    this.liftWays = new JLabel[floors][elevators];

    this.liftWaySize = Math.min(800 / elevators, 800 / floors);

    setupFrame();
    initializeComponents();
    initializeConsole();
    updateIcon();
    setVisible(true);
  }

  /**
   * Set up the frame for the console view.
   */
  private void setupFrame() {
    setTitle("Elevator Simulation");
    setSize(1500, 900);
    setLayout(new BorderLayout());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  /**
   * Switch to the start view.
   */
  public void toStartView() {
    this.setVisible(false);
    startView.setVisible(true);
  }

  /**
   * Initialize the components of the console view.
   */
  private void initializeComponents() {
    int totalWidth = getWidth() - 600;
    int totalHeight = getHeight() - 200;

    // calculate the width and height of each cell
    int baseWidth = totalWidth / elevators;
    int baseHeight = totalHeight / floors;

    // keep the aspect ratio of the cell
    int cellWidth = Math.min(baseWidth, baseHeight * 3 / 4);
    final int cellHeight = cellWidth * 4 / 3;

    // Initialize arrays
    scheduledStops = new JLabel[elevators];
    currentDirections = new JLabel[elevators];

    // main panel
    JPanel mainPanel = new JPanel(null);
    mainPanel.setPreferredSize(new Dimension(totalWidth, totalHeight));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 1000));

    // Initialize the labels for the elevator ways
    initializeElevatorIndicators(mainPanel, cellWidth, totalHeight);
    initializeElevatorLabels(mainPanel, cellWidth, cellHeight);
    initializeFloorLabels(mainPanel, cellHeight);

    setLayout(new BorderLayout());
    add(mainPanel, BorderLayout.CENTER);
  }

  /**
   * Initialize the elevator indicators.
   *
   * @param panel       the panel to add the indicators to
   * @param cellWidth   the width of each cell
   * @param totalHeight the total height of the panel
   */
  private void initializeElevatorIndicators(JPanel panel, int cellWidth, int totalHeight) {
    for (int i = 0; i < elevators; i++) {
      // timer label
      scheduledStops[i] = createAndSetupLabel("Timer: 0", 150 + i * cellWidth, 30, cellWidth, 20);
      panel.add(scheduledStops[i]);

      // direction label
      currentDirections[i] = createAndSetupLabel("Dir: None", 150 + i * cellWidth,
          totalHeight + 100, cellWidth, 20);
      panel.add(currentDirections[i]);
    }

  }

  /**
   * Create and set up a label.
   *
   * @param text   the text of the label
   * @param x      the x-coordinate of the label
   * @param y      the y-coordinate of the label
   * @param width  the width of the label
   * @param height the height of the label
   * @return the created label
   */
  private JLabel createAndSetupLabel(String text, int x, int y, int width, int height) {
    JLabel label = new JLabel(text, SwingConstants.CENTER);
    label.setBounds(x, y, width, height);
    label.setOpaque(true);
    return label;
  }

  /**
   * Initialize the elevator labels.
   *
   * @param mainPanel the main panel to add the labels to
   * @param cellWidth the width of each cell
   * @param cellHeight  the height of each cell
   */
  private void initializeElevatorLabels(JPanel mainPanel, int cellWidth, int cellHeight) {
    for (int floor = 0; floor < floors; floor++) {
      for (int elevator = 0; elevator < elevators; elevator++) {
        JLabel liftLabel = new JLabel("", SwingConstants.CENTER);
        liftLabel.setOpaque(true);
        liftLabel.setBackground(Color.WHITE);
        liftLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        int x = 150 + elevator * cellWidth;
        int y = 50 + (floors - 1 - floor) * cellHeight;
        liftLabel.setBounds(x, y, cellWidth, cellHeight);
        mainPanel.add(liftLabel);
        liftWays[floors - floor - 1][elevator] = liftLabel;
      }
    }
  }

  /**
   * Initialize the floor labels.
   *
   * @param mainPanel  the main panel to add the labels to
   * @param cellHeight the height of each cell
   */
  private void initializeFloorLabels(JPanel mainPanel, int cellHeight) {
    for (int i = 0; i < floors; i++) {
      // consider the floor number is traversed
      JLabel floorLabel = new JLabel("Floor " + (floors - i), SwingConstants.CENTER);
      floorLabel.setOpaque(true);
      floorLabel.setBounds(50, 50 + i * cellHeight, 90, cellHeight);
      mainPanel.add(floorLabel);
    }
  }

  /**
   * Update the icon of the elevator.
   */
  public void updateIcon() {
    for (int i = 0; i < elevators; i++) {
      for (int j = 0; j < floors; j++) {
        liftWays[j][i].setBackground(Color.WHITE);  // Default background color
        liftWays[j][i].setOpaque(true);
      }

      int floorIdx = controller.getCurrentFloors(i);
      if (floorIdx >= 0 && floorIdx < floors) {
        // Dark for closed, light for open
        Color color = controller.isDoorClosed(i) ? new Color(255, 140, 0) : new Color(255, 200, 0);
        liftWays[floors - 1 - floorIdx][i].setBackground(color);
      }
    }
  }


  /**
   * Create a button.
   *
   * @param text  the text of the button
   * @param action  the action listener of the button
   * @param buttonHeight  the height of the button
   * @return  the created button
   */
  private JButton createButton(String text, ActionListener action, int buttonHeight) {
    JButton button = new JButton(text);
    button.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonHeight));
    button.addActionListener(action);
    return button;
  }

  /**
   * Create a status field.
   *
   * @param text  the text of the field
   * @param buttonHeight  the height of the button
   * @param consolePanel  the console panel
   * @return  the created text field
   */
  private JTextField createStatusField(String text, int buttonHeight, JPanel consolePanel) {
    JTextField textField = new JTextField(text);
    textField.setEditable(false);
    textField.setHorizontalAlignment(JTextField.CENTER);
    textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2 * buttonHeight));
    textField.setBackground(consolePanel.getBackground());
    return textField;
  }

  /**
   * Create a floor combo box.
   *
   * @param floors  the number of floors
   * @param buttonHeight  the height of the button
   * @return  the created combo box
   */
  private JComboBox<String> createFloorComboBox(int floors, int buttonHeight) {
    JComboBox<String> comboBox = new JComboBox<>();
    for (int i = 1; i <= floors; i++) {
      comboBox.addItem("Floor " + i);
    }
    comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonHeight));
    return comboBox;
  }


  /**
   * Initialize the console of the building.
   */
  private void initializeConsole() {
    // create the console panel
    JPanel consolePanel = new JPanel();
    consolePanel.setLayout(new BoxLayout(consolePanel, BoxLayout.Y_AXIS));
    consolePanel.setPreferredSize(new Dimension(300, getHeight()));

    // set up the gap between components
    final int verticalGap = 30;
    int buttonHeight = 100;
    consolePanel.setBorder(
        BorderFactory.createEmptyBorder(100, 10, 100, 100));

    // back button
    consolePanel.add(createButton("Back", e -> toStartView(), buttonHeight));
    consolePanel.add(Box.createVerticalStrut(verticalGap));

    // input fields
    JComboBox<String> startField = createFloorComboBox(floors, buttonHeight);
    consolePanel.add(startField);
    consolePanel.add(Box.createVerticalStrut(verticalGap));
    JComboBox<String> endField = createFloorComboBox(floors, buttonHeight);
    consolePanel.add(endField);
    consolePanel.add(Box.createVerticalStrut(verticalGap));

    // add request button
    JButton addButton = createButton("Add Request", e -> {
      String startFloor = (String) startField.getSelectedItem();
      String endFloor = (String) endField.getSelectedItem();
      controller.handleAddRequest(startFloor, endFloor);
    }, buttonHeight);
    consolePanel.add(addButton);
    consolePanel.add(Box.createVerticalStrut(verticalGap));

    // random requests button
    JButton randomButton = createButton("Random Requests",
        e -> handleRandomRequests(), buttonHeight);
    consolePanel.add(randomButton);
    consolePanel.add(Box.createVerticalStrut(verticalGap));

    // step button
    JButton stepButton = createButton("Step", e -> {
      controller.stepElevatorSystem();
      updateText();
      updateIcon();
    }, buttonHeight);
    consolePanel.add(stepButton);
    consolePanel.add(Box.createVerticalStrut(verticalGap));

    // halt button
    JButton haltButton = createButton("Halt", e -> {
      controller.stopElevatorSystem();
      updateText();
    }, buttonHeight);
    consolePanel.add(haltButton);
    consolePanel.add(Box.createVerticalStrut(verticalGap));

    // continue button
    JButton continueButton = createButton("Continue", e -> handleContinueOperation(), buttonHeight);
    consolePanel.add(continueButton);
    consolePanel.add(Box.createVerticalStrut(verticalGap));

    // status fields
    initializeStatusFields(consolePanel, buttonHeight, verticalGap);

    // exit button
    JButton exitButton = createButton("Exit", e -> System.exit(0), buttonHeight);
    consolePanel.add(exitButton);
    consolePanel.add(Box.createVerticalStrut(verticalGap));

    // add the console panel to the frame
    add(consolePanel, BorderLayout.EAST);  // 添加到窗体的右侧
  }

  /**
   * Initialize the status fields of the console.
   *
   * @param consolePanel the console panel
   * @param buttonHeight the height of the button
   * @param verticalGap  the vertical gap between components
   */
  private void initializeStatusFields(JPanel consolePanel, int buttonHeight, int verticalGap) {
    upLabel = createStatusField("UP: ", buttonHeight, consolePanel);
    consolePanel.add(upLabel);
    consolePanel.add(Box.createVerticalStrut(verticalGap));

    downLabel = createStatusField("DOWN: ", buttonHeight, consolePanel);
    consolePanel.add(downLabel);
    consolePanel.add(Box.createVerticalStrut(verticalGap));

    systemStatus = createStatusField("STATUS: ", buttonHeight, consolePanel);
    consolePanel.add(systemStatus);
    consolePanel.add(Box.createVerticalStrut(verticalGap));
  }

  /**
   * Handle the continue operation.
   */
  private void handleContinueOperation() {
    SwingUtilities.invokeLater(() -> {
      ElevatorSystemStatus status = controller.getBuildingReport().getSystemStatus();
      if (status == ElevatorSystemStatus.outOfService) {
        if (controller.startElevatorSystem()) {
          systemStatus.setText("STATUS: Running");
        } else {
          systemStatus.setText("STATUS: Out of Service");
          JOptionPane.showMessageDialog(null, "Failed to start the elevator system.", "Start Error",
              JOptionPane.ERROR_MESSAGE);
        }
      } else if (status == ElevatorSystemStatus.running) {
        JOptionPane.showMessageDialog(null, "Elevator system is already running.",
            "No Action Needed", JOptionPane.INFORMATION_MESSAGE);
      } else if (status == ElevatorSystemStatus.stopping) {
        systemStatus.setText("STATUS: Stopping");
        JOptionPane.showMessageDialog(null,
            "Elevator cannot be started when the system is stopping.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else {
        systemStatus.setText("STATUS: ");
        JOptionPane.showMessageDialog(null,
            "Elevator can only restart with the out of service status.", "Status Error",
            JOptionPane.ERROR_MESSAGE);
      }
    });
  }

  /**
   * Handle the random requests.
   */
  private void handleRandomRequests() {
    SwingUtilities.invokeLater(() -> {
      if (controller.getBuildingReport().getSystemStatus() == ElevatorSystemStatus.running) {
        int requestCount = (int) (Math.random() * 5) + 1; // 生成1到5之间的随机请求数量
        controller.generateRandomRequests(requestCount);
        updateText();
        updateIcon();
      } else {
        JOptionPane.showMessageDialog(this, "Cannot add random requests. System is not running.",
            "Random Request Status", JOptionPane.ERROR_MESSAGE);
      }
    });
  }

  /**
   * Update the text of the console.
   */
  public void updateText() {
    BuildingReport report = controller.getBuildingReport();
    updateLabel(upLabel, "UP: ", controller.formatRequestList(report.getUpRequests()));
    updateLabel(downLabel, "DOWN: ", controller.formatRequestList(report.getDownRequests()));
    updateLabel(systemStatus, "STATUS: ", report.getSystemStatus().toString());
  }


  /**
   * Update the label of the console.
   *
   * @param label  the label to be updated
   * @param prefix the prefix of the label
   * @param value  the value of the label
   */
  private void updateLabel(JTextField label, String prefix, String value) {
    if (label != null) {
      label.setText(prefix + value);
    }
  }

  /**
   * Add a listener to the controller.
   *
   * @param controller the controller to add the listener to
   */
  public void addListener(BuildingController controller) {
    this.controller = controller;
  }

  /**
   * Display a message.
   *
   * @param message the message to be displayed
   */
  public void displayMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  /**
   * Set the console view to be visible.
   */
  public void setVisible() {
    setVisible(true);
  }
}