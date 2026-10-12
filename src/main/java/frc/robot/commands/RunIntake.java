package frc.robot.commands;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

/**
 * Runs the intake at the given speed (positive = in, negative = reverse).
 *
 * Staller: if the intake jams, the motor shuts off and STAYS off until the
 * driver lets go of the trigger. Squeeze it again to retry.
 */
public class RunIntake extends Command {

  private final Intake m_intake;
  private final DoubleSupplier m_speedSupplier;
  private boolean m_lockedOut = false;

  public RunIntake(Intake intake, DoubleSupplier speedSupplier) {
    m_intake = intake;
    m_speedSupplier = speedSupplier;
    addRequirements(m_intake);
  }

  @Override
  public void initialize() {
    m_lockedOut = false;
    m_intake.resetStallDetection();
  }

  @Override
  public void execute() {
    double speed = m_speedSupplier.getAsDouble();

    if (m_intake.isStalled()) {
      m_lockedOut = true; // jammed: shut off
    }

    if (m_lockedOut && Math.abs(speed) < 0.05) {
      // Trigger released: clear the lockout so the next squeeze works.
      m_lockedOut = false;
      m_intake.resetStallDetection();
    }

    m_intake.setSpeed(m_lockedOut ? 0 : speed);
  }

  @Override
  public void end(boolean interrupted) {
    m_intake.stop();
  }

  @Override
  public boolean isFinished() {
    return false; // runs all the time as the intake's default command
  }
}
