package frc.robot.subsystems;


import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SerializerConstants;
import frc.robot.Constants.CANConstants;


/** The serializer moves balls from the intake up into the shooter. */
public class Serializer extends SubsystemBase {


  private final TalonFX m_motor = new TalonFX(SerializerConstants.kMotorId, CANConstants.kCanivore); // CAN ID  -must match Phoenix Tuner X


  public Serializer() {}


  public void setSpeed(double speed) {
    m_motor.set(speed);
  }


  public void stop() {
    m_motor.set(0);
  }
}
