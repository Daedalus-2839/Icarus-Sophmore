package frc.robot.subsystems;


import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.CANConstants;


public class Shooter extends SubsystemBase {


  private final TalonFX m_motor = new TalonFX(ShooterConstants.kMotorId, CANConstants.kCanivore); // CAN ID  -must match Phoenix Tuner X
  private final TalonFXConfiguration pids = new TalonFXConfiguration();
  private final double feedForward = 0.5;
  

  public Shooter() {
    pids.Slot0.kP = ShooterConstants.kP; // Proportional gain
    pids.Slot0.kI = ShooterConstants.kI;  // Integral gain
    pids.Slot0.kD = ShooterConstants.kD; // Derivative gain
    pids.Slot0.kV = ShooterConstants.kV; // Velocity feedforward (volts per RPS)
    m_motor.getConfigurator().apply(pids);
  }


  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter/Velocity", getVelocityRps());
  }


  public void setSpeed(double speed) {
    m_motor.set(speed);
  }

  public void setRPM(double targetRPM){
    m_motor.setControl(new VelocityVoltage(0).withSlot(0).withVelocity(targetRPM/60).withFeedForward(feedForward));
  }

  public void stop() {
    m_motor.set(0);
  }


  /** How fast the shooter is spinning, in rotations per second (always positive). */
  public double getVelocityRps() {
    return Math.abs(m_motor.getVelocity().refresh().getValueAsDouble());
  }
}
