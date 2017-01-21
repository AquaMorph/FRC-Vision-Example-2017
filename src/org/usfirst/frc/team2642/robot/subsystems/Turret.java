package org.usfirst.frc.team2642.robot.subsystems;

import org.usfirst.frc.team2642.robot.Robot;
import org.usfirst.frc.team2642.robot.RobotMap;
import org.usfirst.frc.team2642.robot.commands.MoveTurret;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Turret extends PIDSubsystem {
	Spark turretMotor = new Spark(RobotMap.turretMotor);
	AnalogPotentiometer turretPot = new AnalogPotentiometer(RobotMap.turretPot);
	private double turretPotPos = 0.0;

    // Initialize your subsystem here
    public Turret() {
    	super("turret" , 0.05, 0, 0.06);
    	setSetpoint(0);
    	disable();
    }

    public void initDefaultCommand() {
        setDefaultCommand(new MoveTurret());
    }

    public double returnPIDInput() {
    	synchronized (Robot.imgLock) {
    		return Robot.centerX;
		}
    }

    protected void usePIDOutput(double output) {
        moveTurret(output);
    }
    
    public void enablePID() {
    	enable();
    }
    
    public void disablePID() {
    	disable();
    }
    
    public double getPot() {
    	return turretPot.get();
    }
    
    public void moveTurret(double speed) {
		turretPotPos = getPot();
		if (speed < 0 && turretPotPos < RobotMap.turrentMax) {
			turretMotor.set(speed);
		} else if (speed > 0 && turretPotPos > RobotMap.turrentMin) {
			turretMotor.set(speed);
		} else {
			turretMotor.set(0.0);
		}
	}
}
