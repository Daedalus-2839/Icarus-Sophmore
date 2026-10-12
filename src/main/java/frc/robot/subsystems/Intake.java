package frc.robot.subsystems;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.CANConstants;

public class Intake extends SubsystemBase {

  private final TalonFX m_motor = new TalonFX(IntakeConstants.kMotorId, CANConstants.kCanivore); // must match Phoenix Tuner X

  // Only reports "stalled" after the stall condition has been true for kStallTimeSeconds
  // in a row. Without this, the current spike every time the motor starts would look like a stall.
  private final Debouncer m_stallDebouncer =
      new Debouncer(IntakeConstants.kStallTimeSeconds, Debouncer.DebounceType.kRising);

  private double m_commandedSpeed = 0;
  private boolean m_stalled = false;

  public Intake() {
    TalonFXConfiguration configs = new TalonFXConfiguration();

    // Protect the motor from burning out during a hard stall/jam
    configs.CurrentLimits.StatorCurrentLimit = 35.0; // 35 Amps max torque limit
    configs.CurrentLimits.StatorCurrentLimitEnable = true;

    // Protect the PDP/PDH breaker from tripping
    configs.CurrentLimits.SupplyCurrentLimit = 40.0; // 40 Amps max from the battery
    configs.CurrentLimits.SupplyCurrentLimitEnable = true;

    m_motor.getConfigurator().apply(configs);
  }

  @Override
  public void periodic() {
    // Runs every 20 ms. Math.abs() so stalls are caught in reverse too.
    double current = Math.abs(m_motor.getStatorCurrent().refresh().getValueAsDouble());
    double velocity = Math.abs(m_motor.getVelocity().refresh().getValueAsDouble());

    boolean looksStalled =
        Math.abs(m_commandedSpeed) > 0.05                       // we're actually trying to run it
        && current > IntakeConstants.kStallCurrentAmps          // working hard...
        && velocity < IntakeConstants.kStallMaxVelocityRps;     // ...but not moving

    m_stalled = m_stallDebouncer.calculate(looksStalled);

    SmartDashboard.putNumber("Intake/Current", current);
    SmartDashboard.putNumber("Intake/Velocity", velocity);
    SmartDashboard.putBoolean("Intake/Stalled", m_stalled);
  }

  /** Positive = intake, negative = reverse. */
  public void setSpeed(double speed) {
    m_commandedSpeed = speed;
    m_motor.set(speed);
  }

  public void stop() {
    setSpeed(0);
  }

  /** True once the motor has been jammed for kStallTimeSeconds. */
  public boolean isStalled() {
    return m_stalled;
  }

  /** Clears the stall state so a fresh button press starts clean. */
  public void resetStallDetection() {
    m_stallDebouncer.calculate(false);
    m_stalled = false;
  }
}
