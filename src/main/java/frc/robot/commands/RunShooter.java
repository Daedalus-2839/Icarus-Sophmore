package frc.robot.commands;


import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;


public class RunShooter extends Command {


  private final Shooter m_subsystem;
  private final DoubleSupplier m_speedSupplier;


  public RunShooter(Shooter subsystem, DoubleSupplier speedSupplier) {
    m_subsystem = subsystem;
    m_speedSupplier = speedSupplier;
    addRequirements(m_subsystem);
  }


  @Override
  public void execute() {
    m_subsystem.setSpeed(-m_speedSupplier.getAsDouble());
  }


  @Override
  public void end(boolean interrupted) {
    m_subsystem.stop();
  }


  @Override
  public boolean isFinished() {
    return false;
  }
}
