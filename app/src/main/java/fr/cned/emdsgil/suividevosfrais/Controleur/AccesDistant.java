package fr.cned.emdsgil.suividevosfrais.Controleur;

import android.util.Log;
import android.widget.Toast;


import fr.cned.emdsgil.suividevosfrais.Modele.Serializer;
import fr.cned.emdsgil.suividevosfrais.Outils.AccesHTTP;
import fr.cned.emdsgil.suividevosfrais.Outils.AsyncResponse;


import org.json.JSONArray;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.cned.emdsgil.suividevosfrais.Controleur.AccesDistant;
import fr.cned.emdsgil.suividevosfrais.Modele.FraisHf;
import fr.cned.emdsgil.suividevosfrais.Controleur.Global;
import fr.cned.emdsgil.suividevosfrais.R;
import fr.cned.emdsgil.suividevosfrais.Vue.TransfertActivity;

//import android.support.v7.app.AppCompatActivity;
/**
 * Created by emds on 25/02/2018.
 */

public class AccesDistant implements AsyncResponse {

    // constante
    private static final String SERVERADDR = "http://www.gsb2020.org/Android/index.php";
    // propriétés

    /**
     * Constructeur
     */
    public AccesDistant() {
        super();
    }

    /**
     * Retour du serveur HTTP
     *
     * @param output
     */
    @Override
    public void processFinish(String output) {
        // pour vérification, affiche le contenu du retour dans la console
        Log.d("retourserveur", output);
        // découpage du message reçu
        String[] message = output.split("%");
        // contrôle si le retour est correct (au moins 2 cases)
        if (message.length > 1) {
            if (message[0].equals("check")) {
                if (message[1].equals("OK")) {
                    Log.d("reception", "Transmission effectuée !");
                    TransfertActivity.serveurMessage = "Transmission effectuée !";
                    //suppression de la base sérialisée locale
                    Boolean reussite = Serializer.supprimerFichier();
                    if (reussite) {
                        Log.d("Fichier local", "suppression du fichier reussie !");
                    } else {
                        Log.d("Fichier local", "echec de suppression du fichier !");
                    }
                }
            } else {
                Log.d("reception", "Tranmission ratée...");
                TransfertActivity.serveurMessage = "Echec de transmission !";
            }
            if (message[0].equals("Erreur !")) {
                Log.d("reception", message[1]);
                TransfertActivity.serveurMessage = "Erreur : " + message[1];
            }
        }
    }

    /**
     * Envoi de données vers le serveur distant
     *
     * @param operation      information précisant au serveur l'opération à exécuter
     * @param lesDonneesJSON les données à traiter par le serveur
     */
    public void envoi(String operation, JSONArray lesDonneesJSON) {
        AccesHTTP accesDonnees = new AccesHTTP();
        // lien avec AccesHTTP pour permettre à delegate d'appeler la méthode processFinish
        // au retour du serveur
        accesDonnees.delegate = this;
        // ajout de paramètres dans l'enveloppe HTTP
        accesDonnees.addParam("operation", operation);
        accesDonnees.addParam("lesdonnees", lesDonneesJSON.toString());
        // envoi en post des paramètres, à l'adresse SERVERADDR
        accesDonnees.execute(SERVERADDR);
    }

}