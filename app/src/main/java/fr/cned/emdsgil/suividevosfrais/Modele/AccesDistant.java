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
        Log.d("serveur", output);
        // découpage du message reçu
        String[] message = output.split("%");
        // contrôle si le retour est correct (au moins 2 cases)
        if (message.length > 1) {
            if (message[0].equals("check")) {
                Log.d("check", "****************" + message[1]);
                if (message[1] == "OK") {
                    TransfertActivity.isAuthenticated = Boolean.TRUE;
                }
            } else {
                if (message[0].equals("fraisforfait")) {
                    Log.d("fraisforfait", "****************" + message[1]);
                    try {
                        // récupération des informations
                        JSONObject info = new JSONObject(message[1]);
                        Integer poids = info.getInt("poids");
                        Integer taille = info.getInt("taille");
                        Integer age = info.getInt("age");
                        Integer sexe = info.getInt("sexe");
                        //Date dateMesure = MesOutils.convertStringToDate(info.getString("datemesure"),"yyyy-MM-dd hh:mm:ss");
                        // création et mémorisation du profil
                        //Profil profil = new Profil(dateMesure, poids, taille, age, sexe);
                        //controle.setProfil(profil);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (message[0].equals("fraishf")) {
                        Log.d("fraishf", "****************" + message[1]);
                        try {
                            // récupération des informations
                            JSONArray lesInfos = new JSONArray(message[1]);
                            //ArrayList<Profil> lesProfils = new ArrayList<Profil>();
                            for (int k = 0; k < lesInfos.length(); k++) {
                                JSONObject info = new JSONObject(lesInfos.get(k).toString());
                                Integer poids = info.getInt("poids");
                                Integer taille = info.getInt("taille");
                                Integer age = info.getInt("age");
                                Integer sexe = info.getInt("sexe");
                                //Date dateMesure = MesOutils.convertStringToDate(info.getString("datemesure"), "yyyy-MM-dd hh:mm:ss");
                                // création et mémorisation du profil
                                //Profil profil = new Profil(dateMesure, poids, taille, age, sexe);
                                // ajout du profil dans la collection
                                // lesProfils.add(profil);
                            }
                            // mémorisation des profils
                            //controle.setLesProfis(lesProfils);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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