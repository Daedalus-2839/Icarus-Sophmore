package frc.robot.commands;


import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.SerializerConstants;
import frc.robot.subsystems.Serializer;
import frc.robot.subsystems.Shooter;


/**
 * Feeds balls into the shooter, but only once the shooter has spun up.
 *
 *   Trigger not pressed          -> serializer off
 *   Trigger pressed, spinning up -> serializer off (waits for the flywheel)
 *   Trigger pressed, spun up     -> serializer feeds balls in
 *
 * This only READS the shooter's speed, so it doesn't require the Shooter subsystem
 * and won't interfere with RunShooter.
 */
public class RunSerializer extends Command {


  private final Serializer m_serializer;
  private final Shooter m_shooter;
  private final DoubleSupplier m_triggerSupplier;


  public RunSerializer(Serializer serializer, Shooter shooter, DoubleSupplier triggerSupplier) {
    m_serializer = serializer;
    m_shooter = shooter;
    m_triggerSupplier = triggerSupplier;
    addRequirements(m_serializer);
  }


  @Override
  public void execute() {
    boolean triggerPressed = m_triggerSupplier.getAsDouble() > 0.05;
    boolean shooterReady = m_shooter.getVelocityRps() >= SerializerConstants.kShooterReadyRps;

    if (triggerPressed && shooterReady) {
      m_serializer.setSpeed(SerializerConstants.kFeedSpeed);
    } else {
      m_serializer.stop();
    }
  }


  @Override
  public void end(boolean interrupted) {
    m_serializer.stop();
  }


  @Override
  public boolean isFinished() {
    return false; // runs all the time as the serializer's default command
  }
}
