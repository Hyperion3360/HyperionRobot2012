package org.usfirst.hyperion;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.buttons.DigitalIOButton;
import java.util.Enumeration;
import org.usfirst.hyperion.hardware.DigitalDevice;
import org.usfirst.hyperion.hardware.JoystickButtons;
import org.usfirst.hyperion.hardware.JoystickDevice;
import org.usfirst.hyperion.hardware.RelayDevice;

public class ReserveBallon implements Runnable {

    Relay mElevator;
    DigitalIOButton limitSwitchBallonHaut;
    DigitalIOButton limitSwitchBallonBas;
    Joystick mjGauche;
    Joystick mjControl;
    boolean started;
    int count;

    public ReserveBallon() {
        mElevator = new Relay(RelayDevice.mReserveBallon);
        
        mjGauche = JoystickDevice.GetTankDriveGauche();
        mjControl = JoystickDevice.GetCoPilot();

        limitSwitchBallonHaut = new DigitalIOButton(DigitalDevice.mReserveBallonPresenceHaut);
        limitSwitchBallonBas = new DigitalIOButton(DigitalDevice.mReserveBallonPresenceBas);
        mElevator.setDirection(Relay.Direction.kForward);
        
        count = 0;
    }

    public synchronized void run() {
        started = true;
        boolean isCountWorking = true;
        boolean ballonBasRelacher = false;
        boolean elevatorOn = false;
        while (started) {

            /* METTRE LE CODE DE CONTROLE DE LA RESERVE DE BALON APRES ICI */
            
            if(isCountWorking && mjControl.getRawButton(JoystickButtons.mButtonReserveManuel)){
                isCountWorking = false;
            }else if(!isCountWorking && mjControl.getRawButton(JoystickButtons.mButtonReserveAutomatique)){
                count = 0;
                isCountWorking = true;
            }
            
            if(isCountWorking){
                if(limitSwitchBallonBas.get() && !ballonBasRelacher){
                    addBallCount(1);
                    ballonBasRelacher = false;
                }else{
                    ballonBasRelacher = true;
                }

                if(limitSwitchBallonHaut.get() && elevatorOn){
                    mElevator.set(Relay.Value.kOff);
                    elevatorOn = false;
                }else if(count <= 1 && !elevatorOn ){
                    mElevator.set(Relay.Value.kOn);
                    elevatorOn = true;
                }
            }else if(!isCountWorking){
                if(mjControl.getRawButton(JoystickButtons.mButtonAscenseur)){
                    mElevator.set(Relay.Value.kOn);
                }else{
                    mElevator.set(Relay.Value.kOff);
                }
            }
            
            if(mjControl.getRawButton(JoystickButtons.mButtonAscenseur)){
                mElevator.set(Relay.Value.kOn);
            }
            if(mjControl.getRawButton(JoystickButtons.mButtonIOState)){
                System.out.println("Presence haut: " + limitSwitchBallonHaut.get());
                System.out.println("Presence bas: " + limitSwitchBallonBas.get());
            }

            /* METTRE LE CODE DE CONTROLE DE LA RESERVE DE BALON AVANT ICI */

            try {
                wait(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public synchronized void addBallCount(int add){
        if(add == 1){
            count++;
        }else if(add == -1){
            count--;
        }
    }
    
    public synchronized void stop() {
        started = false;
    }
}
