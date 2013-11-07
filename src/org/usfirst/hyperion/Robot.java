package org.usfirst.hyperion;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import org.usfirst.hyperion.hardware.DigitalDevice;
import org.usfirst.hyperion.hardware.RelayDevice;

public class Robot extends SimpleRobot {

    static final int mVersionMajeur = 2012;
    static final int mVErsionMineur = 4;
    //
    Compressor mCompresseur;
    DriveTrain mDriveTrain;
    Canon mCanon;
    ReserveBallon mReserveBalon;
    Pont mPont;
    AxisCamera camera;
    
    boolean disabled;

    public void robotInit() {
        // Ce commentaire va s'imprimmer sur la console de NetBeans
        System.out.println("Initialisation du Robot HYPERION 3360 : Version " + mVersionMajeur + "." + mVErsionMineur);

        // Le compresseur doit etre toujours demarer pour avoir le plus de pression.
        mCompresseur = new Compressor(DigitalDevice.mPressionCompresseur, RelayDevice.mCompresseur);
        mCompresseur.start();
        
        // Activer les composantes du robot dans autonomus et operatorControl.
        mDriveTrain = new DriveTrain();
        mReserveBalon = new ReserveBallon();
        mCanon = new Canon(mReserveBalon);
        mPont = new Pont();
        
        disabled = false;
        
        //Timer.delay(10);
        
        //camera.getInstance();
        //camera.writeResolution(AxisCamera.ResolutionT.k640x480);
        //camera.writeBrightness(0);
    }

    public void autonomous() {
        System.out.println("Demarage du mode autonome du Robot HYPERION 3360 : Version " + mVersionMajeur + "." + mVErsionMineur);
    }

    public synchronized void operatorControl() {
        System.out.println("Demarage du mode teleopere du Robot HYPERION 3360 : Version " + mVersionMajeur + "." + mVErsionMineur);
        /* DRIVE TRAIN
         * FAIT:
         * - Vitesse connect/ directe sur le joystick
         * - transmission: controle manuel fait.
         * - Faire un filrage de la commande de sortie pour éviter les bris de jaguar
         * 
         * A FAIRE:
         * 
         */
        Thread tDriveTrain = new Thread(mDriveTrain, "DriveTrain");
        /* CANON:
         * FAIT:
         * - Vitesse canon
         * - tir
         * - vitesse rotation fait.
         * - Calculer la distance au mur avec le Sonar.
         * - Calculer l'angle et la vitesse de tir selon la distance au mur.
         * - Calculer l'angle de tir avec deux accéléromètre:
         *   angle = Tan^-1(x/z)+Tan^-1(x_ref/z_ref);
         *
         * A FAIRE:
         * - Calculer une vitesse aproximative des moteurs de tir en stabilisant
         *   la tension fournis. ***À TESTER***
         */
        Thread tCanon = new Thread(mCanon, "Canon");
        /* RESERVE DE BALON
         * FAIT:
         *  - Une limite switch controle si un balon est pret a etre envoyé.
         *  - Une limite switch qui compte le nombre deb allon qui rentre
         *  - Il faut arrêter des tourner les moteurs si un ballon est pret.
         *  - Il faut repartir les moteurs si pas de ballon pret.
         * A FAIRE:
         */
        Thread tReserveBalon = new Thread(mReserveBalon, "ReserveBalon");
        /* PONT
         * FAIT:
         *  - Activer les bras lorsqu'un bouton est pesé.
         *  - Barrer le bras lorsque baisser
         * A FAIRE:
         */
        Thread tPont = new Thread(mPont, "Pont");

        tDriveTrain.start();
        tReserveBalon.start();
        tCanon.start();
        tPont.start();

        while (isEnabled() && isOperatorControl()) {
            try {
                wait(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                }
        }

        mDriveTrain.stop();
        mReserveBalon.stop();
        mCanon.stop();
        mPont.stop();

        try {
            tDriveTrain.join();
            tReserveBalon.join();
            tCanon.join();
            tPont.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void disabled() {
        if(disabled){
            System.out.println("Desactivation du Robot HYPERION 3360 : Version " + mVersionMajeur + "." + mVErsionMineur);
        }else{
            disabled = true;
        }
    }
}