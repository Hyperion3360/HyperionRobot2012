/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.hyperion.hardware;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author HYPERION_3360
 */
public class JoystickDevice {

    // Enumeration des joystick utilisé
    static final int mTankDriveGauche = 1;
    static final int mTankDriveDroite = 2;
    static final int mCoPilot = 3;

    Joystick mjTankDriveGauche;
    Joystick mjTankDriveDroite;
    Joystick mjCoPilot;
    static JoystickDevice mInstance = null;

    JoystickDevice() {
        mjTankDriveGauche = new Joystick(mTankDriveGauche);
        mjTankDriveDroite = new Joystick(mTankDriveDroite);
        mjCoPilot = new Joystick(mCoPilot);
    }

    public static Joystick GetTankDriveGauche() {
        if (mInstance == null) {
            mInstance = new JoystickDevice();
        }
        return mInstance.mjTankDriveGauche;
    }
    public static  Joystick GetTankDriveDroite() {
        if (mInstance == null) {
            mInstance = new JoystickDevice();
        }
        return mInstance.mjTankDriveDroite;
    }
    public static  Joystick GetCoPilot() {
        if (mInstance == null) {
            mInstance = new JoystickDevice();
        }
        return mInstance.mjCoPilot;
    }
}
