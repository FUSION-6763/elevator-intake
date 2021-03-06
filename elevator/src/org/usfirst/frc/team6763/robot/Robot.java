package org.usfirst.frc.team6763.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	AHRS ahrs;
	private DifferentialDrive m_robotDrive
			= new DifferentialDrive(new Spark(5), new Spark(6));
	private int correction = 0;
	private Joystick m_stick = new Joystick(0);
	private Timer m_timer = new Timer();
	private Encoder leftEncoder = new Encoder(0, 1);
	private Encoder rightEncoder = new Encoder(2, 3);
	private static final float TICKS_PER_INCH = 106.166667F;
	//private static final float TICKS_PER_INCH = 108.333333F;

	JoystickButton button1 = new JoystickButton(m_stick, 1);
	JoystickButton button2 = new JoystickButton(m_stick, 2);
	JoystickButton button3 = new JoystickButton(m_stick, 3);
	JoystickButton button4 = new JoystickButton(m_stick, 4);
	
	SpeedController ElevSpark1 = new Spark(3);
	SpeedController ElevSpark2 = new Spark(4);
	
	SpeedController LeftArm = new Spark(7);
	SpeedController RightArm = new Spark(8);
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() { 
		try {
			ahrs = new AHRS(SerialPort.Port.kUSB);
		}
		catch (RuntimeException ex ) {
	        DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
	    
		}
		
		ElevSpark1.setInverted(true);
		RightArm.setInverted(true);
	}
	
	private void driveForward(final float angle) {
		if (angle > 1) {
			leftEncoder.get();
			rightEncoder.get();
			
			m_robotDrive.tankDrive(0.6, 0.65);
		//	System.out.println("rightEncoder = " + rightEncoder.get());
			//System.out.println("leftEncoder = " + leftEncoder.get());
			System.out.println("Gone to far to the right");
			correction = correction + 1;
			System.out.println("Angle = " + ahrs.getYaw());
		}
		else if (angle < -1) {
			m_robotDrive.tankDrive(0.65, 0.6);
			//System.out.println("rightEncoder = " + rightEncoder.get());
			//System.out.println("leftEncoder = " + leftEncoder.get());
			System.out.println("Gone to far to the left");
		}
		else {
			m_robotDrive.tankDrive(0.6, 0.6);
			System.out.println("rightEncoder = " + rightEncoder.get());
			System.out.println("leftEncoder = " + leftEncoder.get());
			System.out.println("Robot is driving straight");
		}
	}
	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		correction = 0;
		m_timer.reset();
		m_timer.start();
		leftEncoder.reset();
		rightEncoder.reset();
		ahrs.reset();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		// Drive for 2 seconds
		/*
		 if (m_timer.get() < 2.0) {
			m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
		} else {
			m_robotDrive.stopMotor(); // stop robot
		}
		*/
		if (leftEncoder.get() < TICKS_PER_INCH * 36 && rightEncoder.get() > -TICKS_PER_INCH * 36) {
			driveForward(ahrs.getYaw());
		} else if (leftEncoder.get() > TICKS_PER_INCH * 37 && leftEncoder.get() < TICKS_PER_INCH * 39 && rightEncoder.get () < -TICKS_PER_INCH * 37 && rightEncoder.get() > -TICKS_PER_INCH * 39) {
			if (ahrs.getYaw() > -92) {
				m_robotDrive.tankDrive(0.0, 0.5);
				if (ahrs.getYaw() < -88 && ahrs.getYaw() > -92) {
					m_robotDrive.tankDrive(0.0, 0.0);
				}
			} else if (ahrs.getYaw() < -88) {
				m_robotDrive.tankDrive(0.5, 0.0);
				if (ahrs.getYaw() < -88 && ahrs.getYaw() > -92) {
					m_robotDrive.tankDrive(0.0, 0.0);
				}
			} 
		
			/*else if (ahrs.getYaw() > -88 && ahrs.getYaw() < -92) {
				m_robotDrive.tankDrive(0.0, 0.0);
			}
				*/
		}
		else if (leftEncoder.get() > TICKS_PER_INCH * 40 && rightEncoder.get() < -TICKS_PER_INCH * 40) {
			driveForward(ahrs.getYaw());
			}
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
		leftEncoder.reset();
		rightEncoder.reset();
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		m_robotDrive.arcadeDrive(-m_stick.getY(), m_stick.getX());
		/*if (button1.get()) {
			ElevSpark1.set(0.5);
			ElevSpark2.set(0.5);
		}
		else if (button2.get()) {
			ElevSpark1.set(-0.5);
			ElevSpark2.set(-0.5);
		}
		else {
			ElevSpark1.set(0.0);
			ElevSpark2.set(0.0);
		}*/
		
		ElevSpark1.set(-m_stick.getRawAxis(5));
		ElevSpark2.set(-m_stick.getRawAxis(5));
		
		if (button3.get()) {
			LeftArm.set(-0.5);
			RightArm.set(-0.5);
		}
		else if (button4.get()) {
			LeftArm.set(0.5);
			RightArm.set(0.5);
		}
		else {
			LeftArm.set(0.0);
			RightArm.set(0.0);
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
	
	public void disabledPeriodic() {
		//System.out.println("rightEncoder = " + rightEncoder.get());
		//System.out.println("leftEncoder = " + leftEncoder.get());
		
	}
}
