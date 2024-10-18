# About/Overview.

This program manages an elevator system in a building by handling user requests for elevator services and displaying real-time information about the status of elevators. It simulates elevator operations across multiple floors, efficiently scheduling elevators to meet user demands and updating a graphical user interface (GUI) to reflect current system operations. The system is designed to be responsive and efficient, incorporating user input handling, request processing, and dynamic updates.


# List of features.

Interactive User Interface: 
Offers an intuitive design with a responsive layout, making navigation and operation simple and efficient for all users.

Advanced Functionality: 
Includes key operations such as real-time data processing, customizable workflows, and automated tasks that enhance productivity.

Accessibility Features: 
Ensures the platform is accessible to a diverse audience with features like text-to-speech capabilities and high-contrast visual options.

# How to run the jar file

To run the JAR file, simply execute it from your command line or double-click if your operating system supports this action. The initial screen allows users to select the number of floors, number of elevators, and elevator capacity. Use the 'start' button to launch the elevator simulation. The 'exit' button can be used at any time to close the application.

# What arguments are needed (if any) to run the jar file, what do they mean

int floors: Specifies the number of floors in the building.
int elevators: Specifies the number of elevators.
int capacity: Defines the maximum capacity of each elevator.
BuildingController controller: Manages the simulation operations.
StartView startView: Provides the initial setup interface.

# How to Use the Program. Instructions on how to use functionality in your program. if interactive, how to interact with your program? Pay particular attention to the parts that are not part of the example runs that you provided.

Instructions: 
Click on 'Instructions' on the left side to view detailed guidance.

Parameter Selection: 
Select parameters using dropdown menus on the right side and press 'start' to begin.

Navigating Simulation: 
Use 'back' to return to parameter selection or modify settings. Utilize operational buttons like 'step', 'random', 'add request', 'halt', and 'continue' to interact with the simulation according to project requirements.

Real-time Updates: 
The lower panel displays real-time updates, while the area above each elevator shows a countdown timer. The floor number appears on the left, and the elevator direction is indicated at the bottom.

# Design/Model Changes. It is important to document what changes that you have made from earlier designs. Why were those changes required? You can write these changes in terms of version if you wish.

Changes from initial designs in Project1 included additions and removals of certain methods as the design process evolved and initial concepts were re-evaluated. No changes were made in Project2, as the design was stabilized.


# Assumptions. List what assumptions you made during program development and implementation. Be sure that these do not conflict with the requirements of the project.

I'm assuming there will be no more than 7 elevators. Since I have a limited screen size, I can only show at most 7 elevators if I'm looking for screen clarity. But my program theoretically supports dozens of elevators running at the same time.

# Limitations. Limitations of your program if any. This should include any requirements that were not implemented or were not working correctly (including something that might work some of the time).

One limitation is the inability to display images in the exported JAR file, despite them being visible during driver class runs on my laptop. This issue remains unresolved, despite various attempted solutions. Thus, I recently implemented a color-coding feature to differentiate between open and closed elevator doors. Open doors are indicated by a light orange color, while closed doors are represented by a darker shade of orange, enhancing visual feedback for users.


# Citations.

https://www.javatpoint.com/java-swing