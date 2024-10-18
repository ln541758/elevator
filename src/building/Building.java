package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import elevator.ElevatorInterface;
import elevator.ElevatorReport;
import java.util.ArrayList;
import java.util.List;
import scanerzus.Request;


/**
 * This class represents a building.
 */
public class Building implements BuildingInterface {
  private final int numberOfFloors;
  private final int numberOfElevators;
  private final int elevatorCapacity;
  private final ElevatorInterface[] elevators;
  private final List<Request> upRequests = new ArrayList<>();
  private ElevatorSystemStatus elevatorsStatus;
  private final List<Request> downRequests = new ArrayList<>();

  /**
   * The constructor for the building.
   *
   * @param numberOfFloors    the number of floors in the building.
   * @param numberOfElevators the number of elevators in the building.
   * @param elevatorCapacity  the capacity of the elevators in the building.
   * @throws IllegalArgumentException if the number of floors is less than 2, the number of
   *                                  elevators is less than 1, or the elevator capacity is less
   *                                  than 1.
   */

  public Building(int numberOfFloors, int numberOfElevators, int elevatorCapacity)
      throws IllegalArgumentException {
    if (numberOfFloors < 2) {
      throw new IllegalArgumentException("Number of floors should be larger or equals to 2");
    } else if (numberOfElevators < 1) {
      throw new IllegalArgumentException("At least one elevator is required.");
    } else if (elevatorCapacity < 1) {
      throw new IllegalArgumentException("Capacity of elevator should be larger or equals to 1.");
    } else {
      this.numberOfFloors = numberOfFloors;
      this.numberOfElevators = numberOfElevators;
      this.elevatorCapacity = elevatorCapacity;
      this.elevators = new Elevator[numberOfElevators];     // a list of elevators
      this.elevatorsStatus = ElevatorSystemStatus.outOfService;

      for (int i = 0; i < numberOfElevators; i++) {
        this.elevators[i] = new Elevator(numberOfFloors, this.elevatorCapacity);
      }
    }
  }

  @Override
  public int getNumberOfFloors() {
    return this.numberOfFloors;
  }

  @Override
  public int getNumberOfElevators() {
    return this.numberOfElevators;
  }

  @Override
  public int getElevatorCapacity() {
    return this.elevatorCapacity;
  }

  /**
   * Get the elevators in the building.
   *
   * @return the elevators in the building.
   */
  public ElevatorInterface[] getElevators() {
    return this.elevators;
  }

  /**
   * Get the up requests in the building.
   *
   * @return the up requests in the building.
   */
  public List<Request> getUpRequests() {
    return this.upRequests;
  }

  /**
   * Get the down requests in the building.
   *
   * @return the down requests in the building.
   */
  public List<Request> getDownRequests() {
    return this.downRequests;
  }

  @Override
  public BuildingReport getElevatorSystemStatus() {
    ElevatorReport[] elevatorReports = new ElevatorReport[this.elevators.length];

    for (int i = 0; i < this.elevators.length; ++i) {
      elevatorReports[i] = this.elevators[i].getElevatorStatus();
    }

    return new BuildingReport(this.numberOfFloors, this.numberOfElevators,
        this.elevatorCapacity, elevatorReports, this.upRequests, this.downRequests,
        this.elevatorsStatus);
  }

  @Override
  public void stopElevatorSystem() {
    // if the elevator system is not stopping or out of service
    if (this.elevatorsStatus == ElevatorSystemStatus.running) {
      for (ElevatorInterface elevator : this.elevators) {
        // All elevator are notified of a stop request.takeOutOfService()is called
        elevator.takeOutOfService();
        // All requests are purged
        this.upRequests.clear();
        this.downRequests.clear();

        this.elevatorsStatus = ElevatorSystemStatus.stopping;
      }
    }
  }

  /**
   * Check if the request is valid.
   *
   * @param request the request to check.
   * @return true if the request is valid, false otherwise.
   */
  private boolean isValidRequest(Request request) {
    // Check if the request is null
    if (request == null) {
      System.out.println("Request cannot be null");
      return false;
    }

    // Check if the start floor is valid
    if (request.getStartFloor() < 0 || request.getStartFloor() > this.numberOfFloors) {
      System.out.println("Start floor is not valid");
      return false;
    }

    // Check if the end floor is valid
    if (request.getEndFloor() < 0 || request.getEndFloor() > this.numberOfFloors) {
      System.out.println("End floor is not valid");
      return false;
    }

    // Check if the start floor is the same as the end floor
    if (request.getStartFloor() == request.getEndFloor()) {
      System.out.println("Start floor cannot be the same as the end floor");
      return false;
    }

    return true;
  }

  @Override
  public boolean handleAddRequest(Request request) {
    // Check if the request is valid
    if (!this.isValidRequest(request)) {
      return false;
    }

    // When the building status is ElevatorSystemStatus.running then all requests are accepted.
    if (this.elevatorsStatus == ElevatorSystemStatus.running) {
      if (request.getStartFloor() < request.getEndFloor()) {
        this.upRequests.add(request);
      } else {
        this.downRequests.add(request);
      }
    } else {
      return false;
    }
    return true;
  }

  @Override
  public boolean startElevatorSystem() {
    // If ElevatorSystemStatus.stopping it cannot be started.
    if (this.elevatorsStatus == ElevatorSystemStatus.stopping) {
      System.out.println("Elevator cannot be started when the system is stopping");
      return false;
    } else if (this.elevatorsStatus == ElevatorSystemStatus.outOfService) {
      // If ElevatorSystemStatus.outOfService then the building is started.
      for (ElevatorInterface elevator : this.elevators) {
        elevator.start();
      }
      this.elevatorsStatus = ElevatorSystemStatus.running;
      System.out.println("Continuing the operations of the building.\n");
      return true;
    } else {
      // If the building is already running then the building is not started.
      System.out.println("Elevator system is already running");
      return true;
    }
  }

  /**
   * Distribute the requests to the elevators.
   */
  private void distributeRequests() {
    // No requests are needed to be distributed if there are no requests.
    if (!this.upRequests.isEmpty() || !this.downRequests.isEmpty()) {
      for (ElevatorInterface elevator : this.elevators) {
        if (elevator.isTakingRequests()) {
          List<Request> waitingRequest = new ArrayList<>();
          // On the ground floor, the elevator can only process up requests.
          if (elevator.getCurrentFloor() == 0) {
            while (waitingRequest.size() < this.elevatorCapacity && !this.upRequests.isEmpty()) {
              waitingRequest.add(this.upRequests.remove(0));
            }
            // Process the requests to the elevator.
            elevator.processRequests(waitingRequest);
          }
          // On the top floor, the elevator can only process down requests.
          if (elevator.getCurrentFloor() == this.numberOfFloors - 1) {
            while (waitingRequest.size() < this.elevatorCapacity
                && !this.downRequests.isEmpty()) {
              waitingRequest.add(this.downRequests.remove(0));
            }
            // Process the requests to the elevator.
            elevator.processRequests(waitingRequest);
          }
        }
      }
    }
  }

  @Override
  public void stepElevatorSystem() {
    // If the elevator system is out of service then system does not accept requests.
    if (this.elevatorsStatus == ElevatorSystemStatus.outOfService) {
      System.out.println("Elevator system not accepting requests."
          + "No further requests will be generated."
          + "Press 'c' will resume the operations of the building.\n");
      return;
    }

    // If the elevator system is running then the requests are distributed.
    if (this.elevatorsStatus == ElevatorSystemStatus.running) {
      this.distributeRequests();
    }

    // If the elevator system is not out of service then the elevators are stepped.
    for (ElevatorInterface elevator : this.elevators) {
      elevator.step();
    }

    // Check if all elevators are on the ground floor when the elevator system is stopping.
    if (this.elevatorsStatus == ElevatorSystemStatus.stopping) {
      boolean allOnGroundFloor = true;

      for (ElevatorInterface elevator : this.elevators) {
        if (elevator.getCurrentFloor() != 0 || elevator.isDoorClosed()) {
          allOnGroundFloor = false;
          break;
        }
      }

      // If all elevators are on the ground floor then the elevator system is out of service.
      if (allOnGroundFloor) {
        this.elevatorsStatus = ElevatorSystemStatus.outOfService;
      }
    }
  }
}
