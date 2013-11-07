package org.usfirst.hyperion.utilitaire;

import edu.wpi.first.wpilibj.AnalogChannel;
import org.usfirst.hyperion.hardware.AnalogDevice;

public class RangeFinder {
       AnalogChannel macUltrasonic;
       
       double mMotorSpeed, range, speed, maxSpeed;
       final double g = 9.81;
       final double net = 2.5;
       
       public RangeFinder(double maxMotorSpeed){
           macUltrasonic = new AnalogChannel(AnalogDevice.mUltraSonic);
           this.maxSpeed = maxMotorSpeed;
       }
       
       public double getSpeed(double ang){
           range = (macUltrasonic.getVoltage()/0.009766)*0.0257;
           System.out.println("Distance: " + range);
           speed = Math.sqrt((g*range)/(Math.sin(2*ang))); // Ceci est une approximation puisque qu'il y a une hauteur initiale
           mMotorSpeed = speed/maxSpeed;
           //Puisque range Finder ne fonctionne pas
           mMotorSpeed = 1;
           return mMotorSpeed;
       }
}
