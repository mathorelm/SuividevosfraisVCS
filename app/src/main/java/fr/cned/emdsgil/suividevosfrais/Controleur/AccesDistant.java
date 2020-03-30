package fr.cned.emdsgil.suividevosfrais.controleur;

import android.util.Log;

import org.json.JSONArray;

import fr.cned.emdsgil.suividevosfrais.outils.AccesHTTP;
import fr.cned.emdsgil.suividevosfrais.outils.AsyncResponse;
import fr.cned.emdsgil.suividevosfrais.vue.TransfertActivity;

/**
 * \author emds
 * \date 25/02/2018
 * \brief permet d'accéder à la base distante
 */

public class AccesDistant implements AsyncResponse {

    // adresse du fichier PHP d'interface d'accès à la base de données
    private static final String SERVERADDR = "http://www.gsb2020.org/Android/index.php";

    /**
     * Constructeur
     */
    public AccesDistant() {
        super();
    }

    /**
     * Retour du serveur HTTP
     *
     * @param output chaîne récupérée en provenance du serveur
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
                    //TODO : comment supprimer la base locale et éviter une nouvelle saisie des données ?
                    //TODO : en fait non...archivage local en permanence...
                }
                if (message[1].equals("MDP")) {
                    Log.d("reception", "erreur de mot de passe !");
                    TransfertActivity.serveurMessage = "Erreur de login/mot de passe";
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