package fr.cned.emdsgil.suividevosfrais.controleur;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.lang.reflect.Field;
import java.util.Hashtable;

import fr.cned.emdsgil.suividevosfrais.modele.FraisMois;

/**
 * \author emds
 * \author mise à jour par louis-marin mathorel
 * \version 2.0
 * \date ????? (creation) 30/03/2020 (mise à jour)
 * \class Global Global.java
 * \brief contient les éléments généraux du programme
 * <p>
 * \details Déclaration des variables globales et de la
 * fonction 'changeAfficheDate' commune.
 */

public abstract class Global {

    // fichier contenant les informations sérialisées
    public static final String filename = "save.fic";
    // tableau d'informations mémorisées
    public static Hashtable<Integer, FraisMois> listFraisMois = new Hashtable<>();

    /**
     * \brief Modifie l'affichage de la date dans les activity
     * \details permet d'afficher la date comme souhaitée, c'est à dire sans le jour
     * \param datePicker \e DatePicker porte l'élément à modifier
     * \param afficheJours \e Boolean \e False pour ne pas afficher les jours, \e True sinon
     */
    public static void changeAfficheDate(DatePicker datePicker, boolean afficheJours) {
        try {
            Field f[] = datePicker.getClass().getDeclaredFields();
            for (Field field : f) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), null);
                if (daySpinnerId != 0) {
                    View daySpinner = datePicker.findViewById(daySpinnerId);
                    if (!afficheJours) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            }
        } catch (SecurityException | IllegalArgumentException e) {
            Log.d("ERROR", e.getMessage());
        }
    }
}
