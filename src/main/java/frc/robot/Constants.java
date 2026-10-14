// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Map;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  public static class CANConstants {
    /**
     * The CANivore all the Krakens are on. The name must match EXACTLY what
     * Phoenix Tuner X shows (case-sensitive). If the motors are on the roboRIO's
     * own CAN bus instead, change this to: new CANBus("rio")
     */
    public static final CANBus kCanivore = new CANBus("canivore");
  }

  public static class IntakeConstants {
    // Each motor on the same CAN bus needs a unique ID. These must match Phoenix Tuner X.
    public static final int kMotorId = 25;

    // Stall detection: the motor counts as stalled when it is pulling a lot of current
    // but barely spinning, continuously, for kStallTimeSeconds.
    public static final double kStallCurrentAmps = 30.0;  // stator limit is 35 A
    public static final double kStallMaxVelocityRps = 2.0; // rotations per second
    public static final double kStallTimeSeconds = 0.25;   // ignores the normal spike at startup
  }

  public static class ShooterConstants {
    public static final int kMotorId = 28; // must match Phoenix Tuner X
    public static final double kP = 0.11; // Proportional gain
    public static final double kI = 0.0;  // Integral gain
    public static final double kD = 0.01; // Derivative gain
    public static final double kV = 0.12; // Velocity feedforward (volts per RPS)

  }

  public static class SerializerConstants {
    public static final int kMotorId = 24; // must match Phoenix Tuner X

    /** How fast the serializer feeds balls into the shooter (0.0 to 1.0). Flip the sign if it runs backward. */
    public static final double kFeedSpeed = 0.6;

    /**
     * Shooter speed (rotations per second) that counts as "spun up".
     * The serializer won't feed until the shooter is at least this fast, so balls
     * don't get stuck in a flywheel that isn't moving yet. Tune on the robot.
     */
    public static final double kShooterReadyRps = 20.0;



  }

  public static class AimAndShootConstants {
    /*
     * Auto-interpolating hash map of shooting values formatted as following:
     * <Distance,ShooterSpeed (RPM)>
     * To use the table, use shooterTable.get(Double distance) to get the RPM
     */
    public final static InterpolatingDoubleTreeMap shooterTable = InterpolatingDoubleTreeMap.ofEntries(
      Map.entry(null, null),
      Map.entry(null,null)
    );

  }
}
