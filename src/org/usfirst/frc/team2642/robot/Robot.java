package org.usfirst.frc.team2642.robot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Robot extends IterativeRobot {
	
	private static final int IMG_WIDTH = 160;
	private static final int IMG_HEIGHT = 120;
	
	private static VisionThread visionThread;
	private UsbCamera camera;
	private double centerX = 0.0;
	private double centerY = 0.0;
	private double targetArea = 0.0;
	
	private RobotDrive drive;
	private XboxController xboxController;
	
	private final Object imgLock = new Object();
	
	@Override
	public void robotInit() {
	    camera = CameraServer.getInstance().startAutomaticCapture();
	    camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
	    camera.setWhiteBalanceManual(25);
	    camera.setFPS(15);
	    camera.setBrightness(0);
	    camera.setExposureManual(0);
	    
	    visionThread = new VisionThread(camera, new Pipeline(), pipeline -> {
	        if (!pipeline.filterContoursOutput().isEmpty()) {
	            Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
	            synchronized (imgLock) {
	                centerX = 2*r.x + r.width - (IMG_WIDTH/2);
	                centerY = 2*r.y + r.height - (IMG_HEIGHT/2);
	                targetArea = r.area();
	            }
	        }
	    });
	    visionThread.start();
	        
	    drive = new RobotDrive(0, 3);
	    drive.setSafetyEnabled(false);
	    xboxController = new XboxController(0);
	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopPeriodic() {
		
			
		drive(xboxController.getY(Hand.kLeft), xboxController.getX(Hand.kLeft));
		
		if(xboxController.getAButton()) {
			camera.setExposureManual(0);
			camera.setBrightness(0);
			
			synchronized (imgLock) {
				SmartDashboard.putNumber("Center X", centerX);
				SmartDashboard.putNumber("Center Y", centerY);
				SmartDashboard.putNumber("Area", targetArea);
				
				if(Math.abs(centerX) > 3) {
					if(centerX > 0) {
						drive(0, 1.0);
					} else {
						drive(0, -1.0);
					}
				}
			}
			
		} else {
			camera.setExposureManual(25);
			camera.setBrightness(25);
		}
	}
	
	public void drive(double y, double x) {
		double speed = 0.75;
		drive.arcadeDrive(-y*speed, -x*speed);
	}

	@Override
	public void testPeriodic() {
	}
}

