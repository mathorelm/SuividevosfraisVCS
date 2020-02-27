package fr.cned.emdsgil.suividevosfrais.Modele;

import android.util.Log;


import fr.cned.emdsgil.suividevosfrais.Vue.TransfertActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by emds on 25/02/2018.
 */

public class AccesDistant implements AsyncResponse {

    // constante
    private static final String SERVERADDR = "http://192.168.0.43/coach/index.php";
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
                    Log.d("reception", "envoi du transfert fraisforfait");
                    JSONArray letableauJSON = TransfertActivity.prepareFraisForfait();
                    this.envoi("lesfraisforfait", letableauJSON);
                    JSONArray le2emeTableauJSON = TransfertActivity.prepareFraisHF();
                    this.envoi("lesfraisHF", le2emeTableauJSON);
                }
            } else {
                if (message[0].equals("lesfraisforfait")) {
                    Log.d("reception", "fraisforfait ! = " + message[1]);

                } else {
                    if (message[0].equals("lesfraisHF")) {
                        Log.d("reception", "fraisHF ! = " + message[1]);
                    } else {
                        if (message[0].equals("Erreur !")) {
                            Log.d("Erreur !", "****************" + message[1]);
                        }
                    }
                }
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