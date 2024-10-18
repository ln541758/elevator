package building;

import scanerzus.Request;

/**
 * This interface is used to represent a building.
 */
public interface BuildingInterface {

  /**
   * Get the number of floors in the building.
   *
   * @return the number of floors in the building.
   */
  int getNumberOfFloors();

  /**
   * Get the number of elevators in the building.
   *
   * @return the number of elevators in the building.
   */
  int getNumberOfElevators();

  /**
   * Get the capacity of the elevators in the building.
   *
   * @return the capacity of the elevators in the building.
   */
  int getElevatorCapacity();

  /**
   * Get the status of the elevator system.
   *
   * @return the status of the elevator system.
   */
  BuildingReport getElevatorSystemStatus();

  /**
   * Tell all the elevators to go to the ground floor and stop servicing requests.
   */
  void stopElevatorSystem();

  /**
   * Add a request to the building.
   *
   * @param request the request to add.
   * @return true if the request was added, false otherwise.
   */
  boolean handleAddRequest(Request request);

  /**
   * Start the elevator system.
   *
   * @return true if the elevator system was started, false otherwise.
   */
  boolean startElevatorSystem();

  /**
   * Step the elevator system.
   *
   */
  void stepElevatorSystem();

}
