package main;

import controller.BuildingController;
import view.ConsoleView;
import view.StartView;

/**
 * The driver class for the elevator system.
 * This class will create the elevator system and run it.
 * This is for testing the elevator system.
 */
public class Main {
  /**
   * The main method for the elevator system.
   * This method creates the elevator system and runs it.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    StartView startView = new StartView();
    BuildingController controller = new BuildingController(startView);
    controller.startGame();
  }
}
