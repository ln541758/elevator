package building;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import building.enums.ElevatorSystemStatus;
import java.util.Arrays;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scanerzus.Request;


/**
 * Unit tests for the Building class.
 */
public class BuildingTest {
  private static Building buildingTest;

  /**
   * Setup for before all of the tests so we can do a static test.
   */
  @BeforeClass
  public static void setUpClass() {
    buildingTest = new Building(11, 8, 3);
    buildingTest.startElevatorSystem();
  }

  /**
   * Setup for the tests.
   */
  @Before
  public void setUp() {
    buildingTest.startElevatorSystem();
  }

  /**
   * Test that an exception when the number of floors is less than 2.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testExceptionOneFloor() {
    new Building(1, 8, 3);
  }

  /**
   * Test that an exception when the elevator is less than 1.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testExceptionNoElevator() {
    // less than 1 elevator
    new Building(11, 0, 3);
  }

  /**
   * Test that an exception when the capacity is less than 1.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testExceptionElevatorNoCapacity() {
    // less than 1 elevator capacity
    new Building(11, 8, 0);
  }

  /**
   * Test the constructor.
   */
  @Test
  public void testConstructor() {
    Building building = new Building(11, 8, 3);
    assertEquals(11, building.getNumberOfFloors());
    assertEquals(8, building.getNumberOfElevators());
    assertEquals(3, building.getElevatorCapacity());
  }

  /**
   * Test getNumberOfFloors method.
   */
  @Test
  public void testGetNumberOfFloors() {
    assertEquals(11, buildingTest.getNumberOfFloors());
  }

  /**
   * Test getNumberOfElevators method.
   */
  @Test
  public void testGetNumberOfElevators() {
    assertEquals(8, buildingTest.getNumberOfElevators());
  }

  /**
   * Test getElevatorCapacity method.
   */
  @Test
  public void testGetElevatorCapacity() {
    assertEquals(3, buildingTest.getElevatorCapacity());
  }

  /**
   * Test getElevators method.
   */
  @Test
  public void testGetElevators() {
    assertEquals(8, buildingTest.getElevators().length);
  }

  /**
   * Test getUpRequests method.
   */
  @Test
  public void testGetUpRequests() {
    assertEquals(0, buildingTest.getUpRequests().size());

    buildingTest.handleAddRequest(new Request(2, 3));
    assertEquals(1, buildingTest.getUpRequests().size());

    buildingTest.handleAddRequest(new Request(3, 4));
    buildingTest.handleAddRequest(new Request(4, 5));
    assertEquals(3, buildingTest.getUpRequests().size());
  }

  /**
   * Test getDownRequests method.
   */
  @Test
  public void testGetDownRequests() {
    assertEquals(0, buildingTest.getDownRequests().size());

    buildingTest.handleAddRequest(new Request(3, 2));
    assertEquals(1, buildingTest.getDownRequests().size());

    buildingTest.handleAddRequest(new Request(4, 3));
    buildingTest.handleAddRequest(new Request(5, 4));
    assertEquals(3, buildingTest.getDownRequests().size());
  }

  /**
   * Test getElevatorSystemStatus method.
   */
  @Test
  public void testGetElevatorSystemStatus() {
    BuildingReport buildingReport = buildingTest.getElevatorSystemStatus();

    assertEquals(11, buildingReport.getNumFloors());
    assertEquals(8, buildingReport.getNumElevators());
    assertEquals(3, buildingReport.getElevatorCapacity());
    assertEquals(8, buildingReport.getElevatorReports().length);
    assertEquals("[]", buildingReport.getUpRequests().toString());
    assertEquals("[]", buildingReport.getDownRequests().toString());
    assertEquals(ElevatorSystemStatus.running, buildingReport.getSystemStatus());

    String elevatorReport = "[Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
                    + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
                    + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
                    + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5]]";
    assertEquals(elevatorReport, Arrays.toString(buildingReport.getElevatorReports()));

    String buildingReports = "BuildingReport:\n"
        + "Number of floors: 11, Number of elevators: 8, Elevator capacity: 3, "
        + "Up requests: [], Down requests: [], System status: Running\n"
        + "Elevator reports: [Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
        + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
        + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5]]\n";
    assertEquals(buildingReports, buildingReport.toString());

    buildingTest.handleAddRequest(new Request(2, 3));
    buildingTest.handleAddRequest(new Request(3, 4));
    buildingTest.handleAddRequest(new Request(5, 4));
    assertEquals(11, buildingReport.getNumFloors());
    assertEquals(8, buildingReport.getNumElevators());
    assertEquals(3, buildingReport.getElevatorCapacity());
    assertEquals(8, buildingReport.getElevatorReports().length);
    assertEquals("[2->3, 3->4]", buildingReport.getUpRequests().toString());
    assertEquals("[5->4]", buildingReport.getDownRequests().toString());
    assertEquals(ElevatorSystemStatus.running, buildingReport.getSystemStatus());

    String buildingReports2 = "BuildingReport:\n"
        + "Number of floors: 11, Number of elevators: 8, Elevator capacity: 3, "
        + "Up requests: [2->3, 3->4], Down requests: [5->4], System status: Running\n"
        + "Elevator reports: [Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
        + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
        + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5]]\n";
    assertEquals(elevatorReport, Arrays.toString(buildingReport.getElevatorReports()));
    assertEquals(buildingReports2, buildingReport.toString());
  }

  /**
   * Test stopElevatorSystem method with running status.
   */
  @Test
  public void testStopElevatorSystemRunning() {
    assertEquals(ElevatorSystemStatus.running,
        buildingTest.getElevatorSystemStatus().getSystemStatus());

    buildingTest.stopElevatorSystem();
    assertEquals(ElevatorSystemStatus.stopping,
        buildingTest.getElevatorSystemStatus().getSystemStatus());
    assertEquals(0, buildingTest.getUpRequests().size());
    assertEquals(0, buildingTest.getDownRequests().size());
  }

  /**
   * Test stopElevatorSystem method with out of service status.
   */
  @Test
  public void testStopElevatorSystemOutOfService() {
    // set up out of service elevator status
    buildingTest.stopElevatorSystem();
    buildingTest.stepElevatorSystem();
    assertEquals(ElevatorSystemStatus.outOfService,
        buildingTest.getElevatorSystemStatus().getSystemStatus());

    // check if door is open
    for (int i = 0; i < buildingTest.getElevators().length; i++) {
      int currentFloor = buildingTest.getElevators()[i].getCurrentFloor();
      boolean isDoorClosed = buildingTest.getElevators()[i].isDoorClosed();
      assertEquals(0, currentFloor);
      assertFalse(isDoorClosed);
      assertEquals(ElevatorSystemStatus.outOfService,
          buildingTest.getElevatorSystemStatus().getSystemStatus());
    }
    String result1 = "[Out of Service[Floor 0], Out of Service[Floor 0], "
        + "Out of Service[Floor 0], Out of Service[Floor 0], "
        + "Out of Service[Floor 0], Out of Service[Floor 0], "
        + "Out of Service[Floor 0], Out of Service[Floor 0]]";
    assertEquals(result1,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();
    assertEquals(result1,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));
  }

  /**
   * Test addRequest method with null.
   */
  @Test
  public void testAddRequestNull() {
    boolean isValid = buildingTest.handleAddRequest(null);
    assertFalse(isValid);
  }

  /**
   * Test addRequest method with start and end floor are the same.
   */
  @Test
  public void testAddRequestStartEndFloorSame() {
    boolean isValid = buildingTest.handleAddRequest(new Request(2, 2));
    assertFalse(isValid);
  }

  /**
   * Test addRequest method with negative start floor.
   */
  @Test
  public void testAddRequestStartFloorNegative() {
    boolean isValid = buildingTest.handleAddRequest(new Request(-1, 8));
    assertFalse(isValid);
  }

  /**
   * Test addRequest method with start floor exceed bound.
   */
  @Test
  public void testAddRequestStartFloorExceedBound() {
    boolean isValid = buildingTest.handleAddRequest(new Request(13, 8));
    assertFalse(isValid);
  }

  /**
   * Test addRequest method with negative end floor.
   */
  @Test
  public void testAddRequestEndFloorNegative() {
    boolean isValid = buildingTest.handleAddRequest(new Request(2, -1));
    assertFalse(isValid);
  }

  /**
   * Test addRequest method with end floor exceed bound.
   */
  @Test
  public void testAddRequestEndFloorExceedBound() {
    boolean isValid = buildingTest.handleAddRequest(new Request(8, 12));
    assertFalse(isValid);
  }

  /**
   * Test addRequest method with stopping elevator status.
   */
  @Test
  public void testAddRequestStoppingElevatorStatus() {
    buildingTest.stopElevatorSystem();
    buildingTest.handleAddRequest(new Request(2, 9));
    assertEquals("[]", buildingTest.getUpRequests().toString());
  }

  /**
   * Test addRequest method with out of service elevator status.
   */
  @Test
  public void testAddRequestOutOfServiceElevatorStatus() {
    assertEquals(ElevatorSystemStatus.running,
        buildingTest.getElevatorSystemStatus().getSystemStatus());

    // set up out of service elevator status
    buildingTest.stopElevatorSystem();
    buildingTest.stepElevatorSystem();
    assertEquals(ElevatorSystemStatus.outOfService,
        buildingTest.getElevatorSystemStatus().getSystemStatus());

    // add request
    boolean isAdd = buildingTest.handleAddRequest(new Request(2, 9));
    assertFalse(isAdd);
  }

  /**
   * Test addRequest method with up request.
   */
  @Test
  public void testAddOneUpRequest() {
    String result1 = "[Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
        + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
        + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
        + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5]]";
    assertEquals(result1,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // add request to up
    Request newRequest = new Request(2, 3);
    boolean result = buildingTest.handleAddRequest(newRequest);
    assertEquals("[2->3]", buildingTest.getUpRequests().toString());
    assertTrue(result);

    // step the elevator system
    buildingTest.stepElevatorSystem();

    String result2 = "[[1|^|C  ]< -- --  2  3 -- -- -- -- -- -- -->, "
        + "Waiting[Floor 0, Time 4], "
        + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], "
        + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], "
        + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4]]";
    assertEquals(result2,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();

    String result3 = "[[2|^|C  ]< -- --  2  3 -- -- -- -- -- -- -->, "
        + "Waiting[Floor 0, Time 3], "
        + "Waiting[Floor 0, Time 3], Waiting[Floor 0, Time 3], "
        + "Waiting[Floor 0, Time 3], Waiting[Floor 0, Time 3], "
        + "Waiting[Floor 0, Time 3], Waiting[Floor 0, Time 3]]";
    assertEquals(result3,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();

    String result4 = "[[2|^|O 3]< -- -- --  3 -- -- -- -- -- -- -->, "
        + "Waiting[Floor 0, Time 2], "
        + "Waiting[Floor 0, Time 2], Waiting[Floor 0, Time 2], "
        + "Waiting[Floor 0, Time 2], Waiting[Floor 0, Time 2], "
        + "Waiting[Floor 0, Time 2], Waiting[Floor 0, Time 2]]";
    assertEquals(result4,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();

    String result5 = "[[2|^|O 2]< -- -- --  3 -- -- -- -- -- -- -->, "
        + "Waiting[Floor 0, Time 1], "
        + "Waiting[Floor 0, Time 1], Waiting[Floor 0, Time 1], "
        + "Waiting[Floor 0, Time 1], Waiting[Floor 0, Time 1], "
        + "Waiting[Floor 0, Time 1], Waiting[Floor 0, Time 1]]";
    assertEquals(result5,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();

    String result6 = "[[2|^|O 1]< -- -- --  3 -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(result6,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();

    String result7 = "[[2|^|C  ]< -- -- --  3 -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(result7,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();

    String result8 = "[[3|^|C  ]< -- -- --  3 -- -- -- -- -- -- -->, "
        + "[2|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(result8,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));
  }

  /**
   * Test addRequest method with down request.
   */
  @Test
  public void testAddRequestDownRequest() {
    Request newRequest = new Request(4, 3);
    boolean result = buildingTest.handleAddRequest(newRequest);
    assertEquals("[4->3]", buildingTest.getDownRequests().toString());
    assertTrue(result);

    Request newRequest2 = new Request(5, 4);
    boolean result2 = buildingTest.handleAddRequest(newRequest2);
    assertEquals("[4->3, 5->4]", buildingTest.getDownRequests().toString());
    assertTrue(result2);
  }

  /**
   * Test addRequest method with request management.
   */
  @Test
  public void testAddRequestManagement() {
    Request newRequest1 = new Request(2, 3);
    boolean isAdd1 = buildingTest.handleAddRequest(newRequest1);
    assertTrue(isAdd1);

    Request newRequest2 = new Request(3, 4);
    boolean isAdd2 = buildingTest.handleAddRequest(newRequest2);
    assertTrue(isAdd2);

    Request newRequest3 = new Request(3, 2);
    boolean isAdd3 = buildingTest.handleAddRequest(newRequest3);
    assertTrue(isAdd3);

    Request newRequest4 = new Request(0, 2);
    boolean isAdd4 = buildingTest.handleAddRequest(newRequest4);
    assertTrue(isAdd4);

    Request newRequest5 = new Request(9, 2);
    boolean isAdd5 = buildingTest.handleAddRequest(newRequest5);
    assertTrue(isAdd5);

    assertEquals("[2->3, 3->4, 0->2]", buildingTest.getUpRequests().toString());
    assertEquals("[3->2, 9->2]", buildingTest.getDownRequests().toString());
  }

  /**
   * Test addRequest method with over capacity request.
   */
  @Test
  public void testAddRequestOverCapacity() {
    buildingTest.handleAddRequest(new Request(2, 3));
    buildingTest.handleAddRequest(new Request(3, 4));
    buildingTest.handleAddRequest(new Request(4, 5));
    buildingTest.handleAddRequest(new Request(5, 6));   // over capacity

    assertEquals("[2->3, 3->4, 4->5, 5->6]",
        buildingTest.getUpRequests().toString());

    // step the elevator system
    buildingTest.stepElevatorSystem();
    assertEquals("[[1|^|C  ]< -- --  2  3  4  5 -- -- -- -- -->, "
            + "[1|^|C  ]< -- -- -- -- --  5  6 -- -- -- -->, "
            + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], "
            + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], "
            + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4]]",
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));
  }


  /**
   * Test startElevatorSystem method with stopping status.
   */
  @Test
  public void testStartElevatorSystemStopping() {
    buildingTest.stopElevatorSystem();
    assertEquals(ElevatorSystemStatus.stopping,
        buildingTest.getElevatorSystemStatus().getSystemStatus());

    boolean isStart = buildingTest.startElevatorSystem();
    assertFalse(isStart);
  }

  /**
   * Test startElevatorSystem method with out of service status.
   */
  @Test
  public void testStartElevatorSystemOutOfService() {
    // set up out of service elevator status
    buildingTest.stopElevatorSystem();
    buildingTest.stepElevatorSystem();
    assertEquals(ElevatorSystemStatus.outOfService,
        buildingTest.getElevatorSystemStatus().getSystemStatus());

    // start the elevator system
    boolean isStart = buildingTest.startElevatorSystem();
    assertEquals(ElevatorSystemStatus.running,
        buildingTest.getElevatorSystemStatus().getSystemStatus());
    assertTrue(isStart);
  }

  /**
   * Test building report its status correctly (running, stopping, outOfService).
   */
  @Test
  public void testBuildingReportStatus() {
    assertEquals(ElevatorSystemStatus.running,
        buildingTest.getElevatorSystemStatus().getSystemStatus());

    // set up stopping elevator status
    buildingTest.stopElevatorSystem();
    assertEquals(ElevatorSystemStatus.stopping,
        buildingTest.getElevatorSystemStatus().getSystemStatus());

    // step the elevator system
    while (buildingTest.getElevatorSystemStatus().getSystemStatus()
        == ElevatorSystemStatus.stopping) {
      buildingTest.stepElevatorSystem();
    }
    assertEquals(ElevatorSystemStatus.outOfService,
        buildingTest.getElevatorSystemStatus().getSystemStatus());

    // set up running elevator status
    buildingTest.startElevatorSystem();
    assertEquals(ElevatorSystemStatus.running,
        buildingTest.getElevatorSystemStatus().getSystemStatus());
  }

  /**
   * Test stepElevatorSystem method with out of service status.
   */
  @Test
  public void testStepElevatorSystemOutOfService() {
    // set up out of service elevator status
    buildingTest.stopElevatorSystem();
    buildingTest.stepElevatorSystem();
    assertEquals(ElevatorSystemStatus.outOfService,
        buildingTest.getElevatorSystemStatus().getSystemStatus());
    String result = "[Out of Service[Floor 0], Out of Service[Floor 0], "
        + "Out of Service[Floor 0], Out of Service[Floor 0], "
        + "Out of Service[Floor 0], Out of Service[Floor 0], "
        + "Out of Service[Floor 0], Out of Service[Floor 0]]";
    assertEquals(result,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();
    assertEquals(result,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));
  }

  /**
   * Test stepElevatorSystem method with stopping status.
   */
  @Test
  public void testStepElevatorSystemStopping() {
    // set up stopping elevator status
    buildingTest.stepElevatorSystem();
    buildingTest.stepElevatorSystem();
    buildingTest.stepElevatorSystem();
    buildingTest.stepElevatorSystem();
    buildingTest.stepElevatorSystem();
    buildingTest.stepElevatorSystem();
    buildingTest.stepElevatorSystem();
    buildingTest.stopElevatorSystem();
    assertEquals(ElevatorSystemStatus.stopping,
        buildingTest.getElevatorSystemStatus().getSystemStatus());
    String result = "[[2|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[2|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(result,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();
    assertEquals(ElevatorSystemStatus.stopping,
        buildingTest.getElevatorSystemStatus().getSystemStatus());
    String result2 = "[[1|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(result2,
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));
  }

  /**
   * Test stepElevatorSystem method with running status.
   */
  @Test
  public void testStepElevatorSystemRunning() {
    assertEquals(ElevatorSystemStatus.running,
        buildingTest.getElevatorSystemStatus().getSystemStatus());

    // add request
    buildingTest.handleAddRequest(new Request(2, 3));
    buildingTest.handleAddRequest(new Request(3, 4));
    buildingTest.handleAddRequest(new Request(5, 4));

    assertEquals("[2->3, 3->4]", buildingTest.getUpRequests().toString());
    assertEquals("[5->4]", buildingTest.getDownRequests().toString());

    // step the elevator system
    buildingTest.stepElevatorSystem();

    assertEquals("[]", buildingTest.getUpRequests().toString());
    assertEquals("[5->4]", buildingTest.getDownRequests().toString());
    assertEquals("[[1|^|C  ]< -- --  2  3  4 -- -- -- -- -- -->, "
            + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], "
            + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], "
            + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], "
            + "Waiting[Floor 0, Time 4]]",
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();

    assertEquals("[]", buildingTest.getUpRequests().toString());
    assertEquals("[5->4]", buildingTest.getDownRequests().toString());
    assertEquals("[[2|^|C  ]< -- --  2  3  4 -- -- -- -- -- -->, "
            + "Waiting[Floor 0, Time 3], Waiting[Floor 0, Time 3], "
            + "Waiting[Floor 0, Time 3], Waiting[Floor 0, Time 3], "
            + "Waiting[Floor 0, Time 3], Waiting[Floor 0, Time 3], "
            + "Waiting[Floor 0, Time 3]]",
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();

    assertEquals("[]", buildingTest.getUpRequests().toString());
    assertEquals("[5->4]", buildingTest.getDownRequests().toString());
    assertEquals("[[2|^|O 3]< -- -- --  3  4 -- -- -- -- -- -->, "
            + "Waiting[Floor 0, Time 2], Waiting[Floor 0, Time 2], "
            + "Waiting[Floor 0, Time 2], Waiting[Floor 0, Time 2], "
            + "Waiting[Floor 0, Time 2], Waiting[Floor 0, Time 2], "
            + "Waiting[Floor 0, Time 2]]",
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();

    assertEquals("[]", buildingTest.getUpRequests().toString());
    assertEquals("[5->4]", buildingTest.getDownRequests().toString());
    assertEquals("[[2|^|O 2]< -- -- --  3  4 -- -- -- -- -- -->, "
            + "Waiting[Floor 0, Time 1], Waiting[Floor 0, Time 1], "
            + "Waiting[Floor 0, Time 1], Waiting[Floor 0, Time 1], "
            + "Waiting[Floor 0, Time 1], Waiting[Floor 0, Time 1], "
            + "Waiting[Floor 0, Time 1]]",
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    for (int i = 0; i <= 11; i++) {
      buildingTest.stepElevatorSystem();
    }

    assertEquals("[]", buildingTest.getUpRequests().toString());
    assertEquals("[5->4]", buildingTest.getDownRequests().toString());
    assertEquals("[[4|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
            + "Waiting[Floor 10, Time 5], Waiting[Floor 10, Time 5], "
            + "Waiting[Floor 10, Time 5], Waiting[Floor 10, Time 5], "
            + "Waiting[Floor 10, Time 5], Waiting[Floor 10, Time 5], "
            + "Waiting[Floor 10, Time 5]]",
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));

    // step the elevator system
    buildingTest.stepElevatorSystem();

    assertEquals("[]", buildingTest.getUpRequests().toString());
    assertEquals("[]", buildingTest.getDownRequests().toString());
    assertEquals("[[5|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[9|v|C  ]< -- -- -- --  4  5 -- -- -- -- -->, "
        + "Waiting[Floor 10, Time 4], Waiting[Floor 10, Time 4], "
        + "Waiting[Floor 10, Time 4], Waiting[Floor 10, Time 4], "
        + "Waiting[Floor 10, Time 4], Waiting[Floor 10, Time 4]]",
        Arrays.toString(buildingTest.getElevatorSystemStatus().elevatorReports));
  }
}
