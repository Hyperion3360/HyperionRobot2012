package org.usfirst.hyperion;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.buttons.DigitalIOButton;
import org.usfirst.hyperion.hardware.DigitalDevice;
import org.usfirst.hyperion.hardware.JoystickButtons;
import org.usfirst.hyperion.hardware.JoystickDevice;
import org.usfirst.hyperion.hardware.SolenoidDevice;

public class Pont implements Runnable {

    Joystick mjControl;
    Joystick mjDroite;
    Solenoid msBrasDown;
    Solenoid msBrasUp;
    Solenoid msBrasLock;
    Solenoid msBrasUnlock;
    DigitalIOButton mdLimitLock;
    boolean started;

    public Pont() {
        mjControl = JoystickDevice.GetCoPilot();
        mjDroite = JoystickDevice.GetTankDriveDroite();
        msBrasDown = new Solenoid(SolenoidDevice.mBrasDown);
        msBrasUp = new Solenoid(SolenoidDevice.mBrasUp);
        msBrasLock = new Solenoid(SolenoidDevice.mBrasLock);
        msBrasUnlock = new Solenoid(SolenoidDevice.mBrasUnlock);
        mdLimitLock = new DigitalIOButton(DigitalDevice.mBrasLimiteLock);
        
        msBrasLock.set(false);
        msBrasUnlock.set(true);
        msBrasDown.set(false);
        msBrasUp.set(true);
    }

    public synchronized void run() {
        started = true;


        while (started) {
            /*
             * METTRE LE CODE DE CONTROLE DU PONT APRES ICI
             */
            if (msBrasUp.get() && !msBrasDown.get()
                    && mjDroite.getRawButton(JoystickButtons.mButtonBrasDown)) {
                msBrasUp.set(false);
                msBrasDown.set(true);
            } else if (!msBrasUp.get() && msBrasDown.get()
                    && mjDroite.getRawButton(JoystickButtons.mButtonBrasUp)) {
                msBrasLock.set(false);
                msBrasUnlock.set(true);
                msBrasDown.set(false);
                msBrasUp.set(true);
            }

            if (!msBrasUp.get() && msBrasDown.get() && mdLimitLock.get()) {
                msBrasUnlock.set(false);
                msBrasLock.set(true);
            }
            
            if(mjControl.getRawButton(JoystickButtons.mButtonIOState)){
                System.out.println("Piston: " + mdLimitLock.get());
            }


            /*
             * METTRE LE CODE DE CONTROLE DU PONT AVANT ICI
             */

            try {
                wait(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void stop() {
        started = false;
    }
}
