package frc.robot.commands;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveRequest.RobotCentric;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;


public class FollowCode extends Command{

    private final CommandSwerveDrivetrain drive;
    private final double xSpeed;
    private final double ySpeed;
    private final PIDController pids = new PIDController(5,0,0);
    private final SwerveRequest.RobotCentric request = new RobotCentric();
    

    public FollowCode(CommandSwerveDrivetrain drive, DoubleSupplier xSpeed, DoubleSupplier ySpeed) {
        this.drive = drive;
        this.xSpeed = xSpeed.getAsDouble();
        this.ySpeed = ySpeed.getAsDouble();      
        addRequirements(drive);
        pids.enableContinuousInput(-Math.PI, Math.PI);
    }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    drive.setControl(request.withVelocityX(xSpeed).withVelocityY(ySpeed).withRotationalRate(pids.calculate(Units.degreesToRadians(LimelightHelpers.getTX(Constants.FollowCodeConstants.limelightName)))));
  }

  @Override
  public void end(boolean interrupted) {
    //just hard stops the robot
    drive.setControl(request.withVelocityX(0).withVelocityY(0).withRotationalRate(0));
  }

  @Override
  public boolean isFinished() {
    return false; // runs all the time as the intake's default command
  }
}
