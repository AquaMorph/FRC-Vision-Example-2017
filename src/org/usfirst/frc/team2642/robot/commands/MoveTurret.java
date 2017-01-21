package org.usfirst.frc.team2642.robot.commands;

import org.usfirst.frc.team2642.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveTurret extends Command {

    public MoveTurret() {
    	requires(Robot.turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.turret.moveTurret(-(Robot.oi.getXbox().getX(Hand.kRight)));
    	System.out.println("Vision X " + Robot.turret.returnPIDInput());
    	if(Robot.oi.getXbox().getAButton()) {
    		Robot.turret.enable();
    	} else {
    		Robot.turret.disable();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
