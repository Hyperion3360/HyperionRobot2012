package org.usfirst.hyperion;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import org.usfirst.hyperion.hardware.*;
import org.usfirst.hyperion.utilitaire.FiltrePasseBas;
import org.usfirst.hyperion.utilitaire.RangeFinder;

public class Canon implements Runnable {

    Joystick mjControl;
    Jaguar mjAngleDeTir;
    FiltrePasseBas mfAngleDeTir;
    Jaguar mjOrientationDeTir;
    FiltrePasseBas mfOrientationDeTir;
    Solenoid msTir;
    Solenoid msTirRev;
    //
    DigitalInput mdLimiteRotationGauche;
    DigitalInput mdLimiteRotationDroite;
    //
    Jaguar mjCanonBas;
    FiltrePasseBas mfCanonBas;
    AnalogChannel macCanonBas;
    PIDController mpidCanonBas;
    //
    Jaguar mjCanonHaut;
    FiltrePasseBas mfCanonHaut;
    AnalogChannel macCanonHaut;
    PIDController mpidCanonHaut;
    //
    RangeFinder mRangeFinder;
    ReserveBallon mReserve;
    //
    ADXL345_I2C maAngle;
    ADXL345_I2C maRef;
    //
    boolean started;
    double angle;
    
    //Proportional, Integral, and Dervative constants.
    //These values will need to be tuned for your robot.
    private final double Kp = 0.3;
    private final double Ki = 0.0;
    private final double Kd = 0.0;

    public Canon(ReserveBallon reserve) {
        mReserve = reserve;
        mjControl = JoystickDevice.GetCoPilot();
        mjAngleDeTir = new Jaguar(PwmDevice.mCanonAngleDeTir);
        mfAngleDeTir = new FiltrePasseBas(25);
        mjOrientationDeTir = new Jaguar(PwmDevice.mCanonOrientationDeTir);
        mfOrientationDeTir = new FiltrePasseBas(25);
        msTir = new Solenoid(SolenoidDevice.mCanonTir);
        msTirRev = new Solenoid(SolenoidDevice.mCanonTirRev);

        mdLimiteRotationGauche = new DigitalInput(DigitalDevice.mCanonLimiteRotationGauche);
        mdLimiteRotationDroite = new DigitalInput(DigitalDevice.mCanonLimiteRotationDroite);

        mjCanonHaut = new Jaguar(PwmDevice.mCanonMoteurHaut);
        mfCanonHaut = new FiltrePasseBas(25);
        macCanonHaut = new AnalogChannel(AnalogDevice.mCanonMoteurHaut);
        mpidCanonHaut = new PIDController(Kp,Ki, Kd, macCanonHaut, mjCanonHaut);
        mpidCanonHaut.setInputRange(0.0, 4095.0);
        mpidCanonHaut.setOutputRange(-1.0, 1.0);

        mjCanonBas = new Jaguar(PwmDevice.mCanonMoteurBas);
        mfCanonBas = new FiltrePasseBas(25);
        macCanonBas = new AnalogChannel(AnalogDevice.mCanonMoteurBas);
        mpidCanonBas = new PIDController(Kp,Ki, Kd, macCanonBas, mjCanonBas);
        mpidCanonBas.setInputRange(0.0, 4095.0);
        mpidCanonBas.setOutputRange(-1.0, 1.0);
        
        mRangeFinder = new RangeFinder(5);
        
        maAngle = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k8G);
        maRef = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k8G);
        
        msTir.set(false);
        msTirRev.set(true);
    }

    public synchronized void run() {
        started = true;
        

        int shotDelay = 0;

        while (started) {
            if(mjControl.getRawAxis(JoystickAxis.mAxisCanonOrientation) < 0 && !mdLimiteRotationDroite.get()){
                mjOrientationDeTir.set(mjControl.getRawAxis(JoystickAxis.mAxisCanonOrientation));
            }else if(mjControl.getRawAxis(JoystickAxis.mAxisCanonOrientation) > 0 && !mdLimiteRotationGauche.get()){
                mjOrientationDeTir.set(mjControl.getRawAxis(JoystickAxis.mAxisCanonOrientation));
            }
            if(mjControl.getRawButton(JoystickButtons.mButtonIOState)){
                System.out.println("Limite rotation droite 7: " + mdLimiteRotationDroite.get());
                System.out.println("Limite rotation gauche 6: " + mdLimiteRotationGauche.get());
            }
            
            mjAngleDeTir.set(mjControl.getRawAxis(JoystickAxis.mAxisCanonAngle));
            
            if(mjControl.getRawButton(JoystickButtons.mButtonCanon)){
                /*
                double angle = 90 - MathUtils.atan2(maAngle.getAcceleration(ADXL345_I2C.Axes.kX),
                                                    maAngle.getAcceleration(ADXL345_I2C.Axes.kZ));
                angle += 90 - MathUtils.atan2(maRef.getAcceleration(ADXL345_I2C.Axes.kX),
                                              maRef.getAcceleration(ADXL345_I2C.Axes.kZ));
                
                System.out.println("Angle: " + angle);
                System.out.println("accelero angle X: " + maAngle.getAcceleration(ADXL345_I2C.Axes.kX));
                System.out.println("accelero angle Z: " + maAngle.getAcceleration(ADXL345_I2C.Axes.kZ));
                System.out.println("accelero  ref X: " + maRef.getAcceleration(ADXL345_I2C.Axes.kX));
                System.out.println("accelero  ref X: " + maRef.getAcceleration(ADXL345_I2C.Axes.kZ));
                System.out.println("accelero angle Y: " + maAngle.getAcceleration(ADXL345_I2C.Axes.kY));
                System.out.println("accelero  ref Y: " + maAngle.getAcceleration(ADXL345_I2C.Axes.kY));
                
                angle = 30;
                
                mjCanonBas.set(rangeFinder.getSpeed(angle));
                mjCanonHaut.set(rangeFinder.getSpeed(angle));
                System.out.println("Speed: " + rangeFinder.getSpeed(angle));
                */
                angle = Math.PI/4;
                mjCanonBas.set(mRangeFinder.getSpeed(angle));
                mjCanonHaut.set(mRangeFinder.getSpeed(angle));
                System.out.println("Vitesse des moteurs: " + mRangeFinder.getSpeed(angle));
                if(mjControl.getRawButton(JoystickButtons.mButtonTir)){
                    msTirRev.set(false);
                    msTir.set(true);
                    mReserve.addBallCount(-1);
                    if(shotDelay <= 0){
                        shotDelay = 100;
                    }
                }
            }else{               
                mjCanonBas.set(0);
                mjCanonHaut.set(0);
            }            
            
            if(shotDelay > 0){
                shotDelay -= 5;
            } else if(shotDelay <= 0){
                msTir.set(false);
                msTirRev.set(true);
            }
            
            /* METTRE LE CODE DE CONTROLE DU CANON  AVANT ICI */

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
