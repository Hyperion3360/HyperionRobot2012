/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.hyperion.utilitaire;

/**
 *
 * @author HYPERION_3360
 */
public class FiltrePasseBas {
    int mIntervalMS;
    double mSortiePrecedente[];
    int mTailleSortiePrecedente;

    public FiltrePasseBas(int largeurFenetre)
    {
        mSortiePrecedente = new double[largeurFenetre];
        mTailleSortiePrecedente = largeurFenetre;

        for (int index = 0; index < mTailleSortiePrecedente; index++)
        {
            mSortiePrecedente[index] = 0.0;
        }
    }
    
    public double Feed(double nouvelleEntree){

        double sortie = 0.0;

        // Il ne faut pas prendre le premier elements, on le jette.
        for (int index = 1; index < mTailleSortiePrecedente; index++)
        {
            sortie +=  mSortiePrecedente[index];
        }

        // On fait la moyenne pour filtrer.
        sortie = (sortie + nouvelleEntree) / mTailleSortiePrecedente;

        // On garde une copie de la derniere sortie.
        for (int index = 0; index < mTailleSortiePrecedente-1; index++)
        {
            mSortiePrecedente[index] = mSortiePrecedente[index+1];
        }
        mSortiePrecedente[mTailleSortiePrecedente-1] = sortie;

        return sortie;
    }
}
