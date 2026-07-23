package badgerutils.motor;

import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

public class FollowerUtil {
  private static Follower follower = new Follower(0, MotorAlignmentValue.Aligned);

  public static void setFollowControl(ControlRequest request, TalonFX... motors) {
    for (TalonFX motor : motors) {
      if (motor.isConnected()) {
        motor.setControl(request);

        follower.LeaderID = motor.getDeviceID();
        
        for (TalonFX talonFX : motors) {
          if (talonFX != motor) {
            talonFX.setControl(follower);
          }
        }

        return;
      }
    }
  }
}
