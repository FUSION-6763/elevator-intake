# Elevator/Intake Code
This code will run our 2018 intake. Here's everything you need to know.
## Elevator
The right joystick on a standard xBox controller will control the elevator.
#### Elevator connections
The left motor should be connected to channel 3, and the right motor should be connected to channel 4 on the RoboRIO.
#### Elevator programming
On the elevator, the left motor (channel 3 and `ElevSpark1`) should be reversed. Use `ElevSpark1.setInverted(true);` in `robotInit` to achieve this.

## Intake
As for the intake, it's controlled by the X and Y buttons on the controller. X will intake the cube, and Y will spit out the cube.
#### Intake connections
The left side of the intake should be connected to channel 7 and the right side should be on channel 8.
#### Intake programming
The right side of the intake (channel 8 and `RightArm`) is inverted. Do this with `RightArm.setInverted(true);` in `robotInit`.

When these rules are followed and the motors are connected to the correct Sparks, positive values will bring the elevator up and make the intake spit out a cube.
