package fr.cned.emdsgil.suividevosfrais.controleur;

import android.util.Log;

import org.json.JSONArray;

import fr.cned.emdsgil.suividevosfrais.outils.AccesHTTP;
import fr.cned.emdsgil.suividevosfrais.outils.AsyncResponse;
import fr.cned.emdsgil.suividevosfrais.vue.TransfertActivity;

/**
 * \author emds
 * \author mise à jour par louis-marin mathorel
 * \version 2.0
 * \date 25/02/2018 (creation) 30/03/2020 (mise à jour)
 * \class AccesDistant AccesDistant.java
 * \brief permet d'accéder à la base distante
 *
 * \details Met en place un envoi de données POST puis un thread
 *          d'attente de réponse du serveur HTTP.
 */

public class AccesDistant implements AsyncResponse {

    // adresse du fichier PHP d'interface d'accès à la base de données
    private static final String SERVERADDR = "http://www.gsb2020.org/Android/index.php";

    /**
     * \brief Constructeur
     */
    public AccesDistant() {
        super();
    }

    /**
     * \brief Retour du serveur HTTP
     * \details cette fonction est déclenchée par le thread
     *          lorsqu'une donnée entrante arrive du serveur
     * \param output    \e String contenant les données
     */
    @Override
    public void processFinish(String output) {
        String[] message = output.split("%");
        // contrôle si le retour est correct (au moins 2 cases)
        if (message.length > 1) {
            //Renseigne .serveurMessage : mesure conservatoire pour utilisation future.
            //TODO utiliser .serveurMessage pour améliorer la communication vers l'utilisateur
            if (message[0].equals("check")) {
                if (message[1].equals("OK")) {
                    TransfertActivity.serveurMessage = "Transmission effectuée !";
                }
                if (message[1].equals("MDP")) {
                    TransfertActivity.serveurMessage = "Erreur de login/mot de passe";
                }
            } else {
                TransfertActivity.serveurMessage = "Echec de transmission !";
            }

            if (message[0].equals("Erreur !")) {
                TransfertActivity.serveurMessage = "Erreur : " + message[1];
            }
        }
    }

    /**
     * \brief Envoi de données vers le serveur distant
     * \details cette fonction envoi des données en POST vers le serveur
     *          avec les paramètres fournis
     * \param operation    \e String information précisant au serveur l'opération à exécuter
     * \param lesDonnesJSON \e String les données à traiter par le serveur.
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