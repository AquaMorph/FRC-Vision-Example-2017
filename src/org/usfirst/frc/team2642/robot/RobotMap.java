package org.usfirst.frc.team2642.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// USB
	public static int xBox = 0;
	
	// PWM
	public static int turretMotor = 9;
	
	// Analog
	public static int turretPot = 0;
	
	// Limits
	public static double turrentMax = 0.75;
	public static double turrentMin = 0.25;
}
