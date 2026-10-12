package frc.robot.subsystems;


import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.CANConstants;


public class Shooter extends SubsystemBase {


  private final TalonFX m_motor = new TalonFX(ShooterConstants.kMotorId, CANConstants.kCanivore); // CAN ID  -must match Phoenix Tuner X


  public Shooter() {}


  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter/Velocity", getVelocityRps());
  }


  public void setSpeed(double speed) {
    m_motor.set(speed);
  }


  public void stop() {
    m_motor.set(0);
  }


  /** How fast the shooter is spinning, in rotations per second (always positive). */
  public double getVelocityRps() {
    return Math.abs(m_motor.getVelocity().refresh().getValueAsDouble());
  }
}
