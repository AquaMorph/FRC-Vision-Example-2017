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
	
	private VisionThread visionThread;
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
	    camera.setBrightness(0);
	    camera.setExposureManual(0);
	    
	    visionThread = new VisionThread(camera, new Pipeline(), pipeline -> {
	        if (!pipeline.filterContoursOutput().isEmpty()) {
	            Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
	            synchronized (imgLock) {
	                centerX = r.x + (r.width / 2);
	                centerY = r.y + (r.height / 2);
	                targetArea = r.width * r.height;
	            }
	        }
	    });
	    visionThread.start();
	        
	    drive = new RobotDrive(0, 1, 2, 3);
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
		
		synchronized (imgLock) {
			SmartDashboard.putNumber("Center X", centerX);
			SmartDashboard.putNumber("Center Y", centerY);
			SmartDashboard.putNumber("Area", targetArea);
		}
		
		drive.arcadeDrive(xboxController.getY(Hand.kLeft), xboxController.getX(Hand.kLeft));
		
		if(xboxController.getAButton()) {
			camera.setExposureManual(25);
			camera.setBrightness(25);
		} else {
			camera.setExposureManual(0);
			camera.setBrightness(0);
		}
	}

	@Override
	public void testPeriodic() {
	}
}

