package org.usfirst.hyperion;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import org.usfirst.hyperion.hardware.*;
import org.usfirst.hyperion.utilitaire.FiltrePasseBas;

public class DriveTrain implements Runnable {

    Joystick mjGauche;
    Joystick mjDroite;
    FiltrePasseBas mfMoteurGauche;
    FiltrePasseBas mfMoteurDroite;
    RobotDrive mDriveTrain;
    Encoder meTransmissionGauche;
    Encoder meTransmissionDroite;
    Solenoid msTransmissionHi;
    Solenoid msTransmissionLow;
    boolean mStarted;

    public DriveTrain() {
        mjGauche = JoystickDevice.GetTankDriveGauche();
        mjDroite = JoystickDevice.GetTankDriveDroite();
        mDriveTrain = new RobotDrive(PwmDevice.mMoteurGaucheAvant,
                PwmDevice.mMoteurGaucheArriere,
                PwmDevice.mMoteurDroiteAvant,
                PwmDevice.mMoteurDroiteArriere);


        msTransmissionHi = new Solenoid(SolenoidDevice.mTransmissionHi);
        msTransmissionLow = new Solenoid(SolenoidDevice.mTransmissionLow);
        meTransmissionGauche = new Encoder(DigitalDevice.mTransmissionGaucheEncodeurA,
                DigitalDevice.mTransmissionGaucheEncodeurB);
        mfMoteurGauche = new FiltrePasseBas(25);
        meTransmissionDroite = new Encoder(DigitalDevice.mTransmissionDroiteEncodeurA,
                DigitalDevice.mTransmissionDroiteEncodeurB);
        mfMoteurDroite = new FiltrePasseBas(25);

        msTransmissionHi.set(false);
        msTransmissionLow.set(true);
    }

    public synchronized void run() {
        mStarted = true;

        while (mStarted) {
            /*
             * METTRE LE CODE DE CONTROLE DU DRIVE TRAIN APRES ICI
             */


            // Controle des moteurs.
            mDriveTrain.tankDrive(/*mfMoteurGauche.Feed(*/-mjDroite.getRawAxis(JoystickAxis.mAxisDriveTrainGauche)/*)*/,
                    /*mfMoteurDroite.Feed(*/-mjGauche.getRawAxis(JoystickAxis.mAxisDriveTrainDroite))/*)*/;

            // Controle des transmissions.
            if (mjDroite.getRawButton(JoystickButtons.mButtonTransmissionHi)) {
                msTransmissionLow.set(false);
                msTransmissionHi.set(true);
            } else if (mjGauche.getRawButton(JoystickButtons.mButtonTransmissionLow)) {
                msTransmissionHi.set(false);
                msTransmissionLow.set(true);

            }

            /*
             * METTRE LE CODE DE CONTROLE DU DRIVE TRAIN AVANT ICI
             */
            try {
                wait(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void stop() {
        mStarted = false;
    }
}
