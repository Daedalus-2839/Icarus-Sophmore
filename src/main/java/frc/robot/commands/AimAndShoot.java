/*package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.AimAndShootConstants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class AimAndShoot extends Command{
    private double distance;
    private final Shooter m_shooter;

  public AimAndShoot(Shooter shooter) {
    this.m_shooter = shooter;
    addRequirements(shooter);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    //once target speed is inferred, set the motor to said speed
    m_shooter.setRPM(AimAndShootConstants.shooterTable.get(distance));
  }
  @Override
  public void end(boolean interrupted) {
  }
  private double getDistance(){
    
  }

  @Override
  public boolean isFinished() {
    return false; // runs all the time as the intake's default command
  }
}
*/