package controller;

import building.Building;
import building.BuildingInterface;
import building.BuildingReport;
import building.enums.ElevatorSystemStatus;
import elevator.ElevatorReport;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import scanerzus.Request;
import view.ConsoleView;
import view.StartView;


/**
 * This class represents a building controller. It is responsible for managing the state of the
 * elevator system and responding to requests.
 */
public class BuildingController {
  private BuildingInterface building;
  private final StartView startView;
  private ConsoleView consoleView;

  /**
   * Constructor for the building controller.
   * @param startView the view interface that the controller will interact with
   */
  public BuildingController(StartView startView) {
    this.startView = Objects.requireNonNull(startView, "StartView could not be null.");
    this.consoleView = null;
  }

  /**
   * Load the initialization view to let the user input the number of floors, elevators, and
   * elevator capacity.
   */
  public void startGame() {
    startView.setVisible();
    startView.addListener(this);
  }

  /**
   * Initialize the building with the given number of floors, elevators, and capacity.
   *
   * @param numberOfFloors the number of floors in the building
   * @param numberOfElevators the number of elevators in the building
   * @param capacity the capacity of each elevator
   */
  public void initializeBuilding(String numberOfFloors, String numberOfElevators, String capacity) {
    int numOfFloors;
    int numOfElevators;
    int elevatorCapacity;

    if (numberOfFloors.isEmpty() || numberOfElevators.isEmpty() || capacity.isEmpty()) {
      startView.displayMessage("All fields must be filled!");
      return;
    }

    try {
      numOfFloors = Integer.parseInt(numberOfFloors);
      numOfElevators = Integer.parseInt(numberOfElevators);
      elevatorCapacity = Integer.parseInt(capacity);

      this.building = new Building(numOfFloors, numOfElevators, elevatorCapacity);
      building.startElevatorSystem();

      this.consoleView = new ConsoleView(numOfFloors, numOfElevators,
          elevatorCapacity, this, startView);
      this.consoleView.setVisible(true);
      this.consoleView.addListener(this);

    } catch (NumberFormatException e) {
      startView.displayMessage("Invalid input. Please try again." + e.getMessage());
    }
  }


  /**
   * Step the elevator system.
   */
  public void stepElevatorSystem() {
    building.stepElevatorSystem();
    updateElevatorDisplays();
  }

  /**
   * Start the elevator system.
   *
   * @return true if the system is started, false otherwise
   */
  public boolean startElevatorSystem() {
    try {
      return building.startElevatorSystem();
    } catch (IllegalStateException | IllegalArgumentException e) {
      if (consoleView != null) {
        consoleView.displayMessage(e.getMessage());
      }
    }
    return false;
  }


  /**
   * Stop the elevator system.
   */
  public void stopElevatorSystem() {
    try {
      building.stopElevatorSystem();
    } catch (IllegalStateException | IllegalArgumentException e) {
      if (consoleView != null) {
        consoleView.displayMessage(e.getMessage());
      }
    }
  }

  /**
   * Generate a report of the elevator system.
   *
   * @param requests the list of requests
   *
   * @return the report of the elevator system
   */
  public String formatRequestList(List<Request> requests) {
    return requests.stream()
        .map(request -> (request.getStartFloor() + 1) + "->" + (request.getEndFloor() + 1))
        .collect(Collectors.joining(", "));
  }

  /**
   * Generate a report of the elevator system.
   *
   */
  public void updateElevatorDisplays() {
    ElevatorReport[] elevatorReports = this.getBuildingReport().getElevatorReports();
    for (int i = 0; i < elevatorReports.length; i++) {
      ElevatorReport report = elevatorReports[i];
      if (report != null) {
        consoleView.scheduledStops[i].setText("Timer " + report.getEndWaitTimer());
        consoleView.currentDirections[i].setText("Dir " + report.getDirection().toString());
      }
    }
  }

  /**
   * Handle a request to add a new request to the elevator system.
   *
   * @param startFloor the start floor of the request
   * @param endFloor the end floor of the request
   */
  public void handleAddRequest(String startFloor, String endFloor) {
    // check if the system is running
    if (getBuildingReport().getSystemStatus() != ElevatorSystemStatus.running) {
      consoleView.displayMessage("Request failed to add. System is not running.");
      return;
    }

    // check if the input is valid
    try {
      int start = Integer.parseInt(startFloor.replace("Floor ", "")) - 1;
      int end = Integer.parseInt(endFloor.replace("Floor ", "")) - 1;

      // check if the floors are the same
      if (start == end) {
        consoleView.displayMessage("Start and end floors cannot be the same.");
        return;
      }

      // add the request
      Request request = new Request(start, end);
      boolean requestAdded = building.handleAddRequest(request);

      if (requestAdded) {
        consoleView.updateText();
        consoleView.updateIcon();
      } else {
        consoleView.displayMessage("Request could not be added.");
      }
    } catch (NumberFormatException e) {
      consoleView.displayMessage("Floor numbers must be integers.");
    }
  }


  /**
   * Generate random requests for the elevator system.
   *
   * @param count the number of random requests to generate
   */
  public void generateRandomRequests(int count) {
    for (int i = 0; i < count; i++) {
      int fromFloor = (int) (Math.random() * building.getNumberOfFloors());
      int toFloor;
      do {
        toFloor = (int) (Math.random() * building.getNumberOfFloors());
      } while (fromFloor == toFloor);

      building.handleAddRequest(new Request(fromFloor, toFloor));
    }
  }

  /**
   * Get the current floor of the elevator.
   * @param elevatorIndex the index of the elevator
   * @return the current floor of the elevator
   */
  public int getCurrentFloors(int elevatorIndex) {
    if (building != null && building.getElevatorSystemStatus() != null
        && building.getElevatorSystemStatus().getElevatorReports() != null
        && elevatorIndex >= 0 && elevatorIndex < building.getElevatorSystemStatus()
        .getElevatorReports().length) {
      return building.getElevatorSystemStatus().getElevatorReports()[elevatorIndex]
          .getCurrentFloor();
    } else {
      return -1;
    }
  }

  /**
   * Check if the door of the elevator is closed.
   * @param elevatorIndex the index of the elevator
   * @return true if the door is closed, false otherwise
   */
  public boolean isDoorClosed(int elevatorIndex) {
    return building.getElevatorSystemStatus().getElevatorReports()[elevatorIndex].isDoorClosed();
  }

  /**
   * Generate a report of the elevator system.
   * @return the report of the elevator system
   */
  public BuildingReport getBuildingReport() {
    return building.getElevatorSystemStatus();
  }
}
