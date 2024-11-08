# TCSS305

Assignment 2

Jakita Kaur

Autumn 2024

## Assignment Overview
This assignment focused on developing a city traffic simulation in java, which
emphasized core object oriented programming principles such as inheritance,
polymorphism, interfaces, and also abstract classes. The objective was to
design various vehicle classes called Truck, Car, Taxi, Atv, Bicycle, and Human.
Each classes exhibit unique behaviors and movement rules while sharing common
characteristics through an abstract parent class. These vehicles interact
within a city map environment that includes multiple terrain types, traffic
lights, and collision mechanics. Specific movement rules govern each vehicle,
limitations based on terrain, responses to traffic lights, and handling of
collisions. A GUI is provided already to display and animate vehicle movements.
Also, in this assignment it is required to have unit testing for three of
the classes and that ensures each vehicle performs as expected under
various conditions.


## Technical Impression:
In order to complete this assignment, the approach began with structuring
a robust parent class named AbstractVehicle. This streamlined the shared
functionality across subclasses, which reduces redundancy.
A lot of particular attention was required for each vehicle's movement and collision
behavior, and that balanced distinct movement rules with the common
structure provided by the parent class. By implementing terrain and light
interaction logic presented some new challenges for me, especially in
ensuring that all behaviors could accommodate the specified randomness
for vehicles like Atv's. One specific frustration that came was trying
to achieve full branch coverage on the Truck class.The coverage tool
repeatedly indicated that a branch was uncovered, but when I would look
in my code it showed that everything was being covered. After thoroughly
reviewing the code and testing suite, it was determined that the missing
branch involved handling Light.YELLOW for the canPass method on the
CROSSWALK terrain. Adding that test resolved the coverage issue. Another notable
challenge involved creating test cases for the ATV class. Achieving full coverage 
required a series of highly specific test scenarios, such as setting one wall to the 
north or placing walls both to the north and west of the vehicle’s position. To ensure 
the ATV class operated as expected in all configurations, it was necessary to account
for each possible interaction with walls and other terrains. This exhaustive approach
was essential to verify that the ATV would not skip moves or fail to correctly select
a new direction in the presence of walls. Testing all possible configurations provided
insights into edge cases that could interfere with expected behaviors and ensured 
comprehensive validation of the class’s functionality. This assignment presented a very fascinating 
opportunity to explore object oriented programming principles in a more dynamic and real
world simulation. Which was something that hasn't come across up until now. Overall, this assignment offered a valuable and enjoyable experience by
blending creative problem solving with technical rigor.



## Unresolved problems in my submission:
None

## Citations and Collaborations:
None

## Questions:
None
