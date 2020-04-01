package fr.cned.emdsgil.suividevosfrais.outils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.os.AsyncTask;

/**
 * \author emds
 * \version 1.0
 * \date ?????? (creation)
 * \class AccesHTTP AccesHTTP.java
 * \brief classe technique de connection distante HTTP
 *
 * \details fournit les services pour AccesDistant
 */
public class AccesHTTP extends AsyncTask<String, Integer, Long> {

    // propriétés
    public String ret = ""; // information retournée par le serveur
    public AsyncResponse delegate = null; // gestion du retour asynchrone
    private String parametres = ""; // paramètres à envoyer en POST au serveur

    /**
     * \brief Constructeur
     */
    public AccesHTTP() {
        super();
    }

    /**
     * \brief Construction de la chaîne paramètre POST (encodage UTF-8)
     * \details ajoute à chaque appel un paramètre déterminé par nom = valeur
     * \param nom \e String chaîne représentative du paramètre à renseigner
     * \param valeur \e String chaîne contenant la valeur à associer
     */
    public void addParam(String nom, String valeur) {
        try {
            if (parametres.equals("")) {
                // premier paramètre
                parametres = URLEncoder.encode(nom, "UTF-8") + "=" + URLEncoder.encode(valeur, "UTF-8");
            } else {
                // paramètres suivants (séparés par &)
                parametres += "&" + URLEncoder.encode(nom, "UTF-8") + "=" + URLEncoder.encode(valeur, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * \brief méthode appelée depuis \e execute
     * \details envoie au serveur une liste de paramètres en POST
     * \param urls \e String... contient l'adresse du serveur dans la case 0
     */
    @Override
    protected Long doInBackground(String... urls) {

        // pour éliminer certaines erreurs
        System.setProperty("http.keepAlive", "false");
        // objets pour gérer la connexion, la lecture et l'écriture
        PrintWriter writer = null;
        BufferedReader reader = null;
        HttpURLConnection connexion = null;
        try {
            // création de l'url à partir de l'adresse reçu en paramètre, dans urls[0]
            URL url = new URL(urls[0]);
            // ouverture de la connexion
            connexion = (HttpURLConnection) url.openConnection();
            connexion.setDoOutput(true);
            // choix de la méthode POST pour l'envoi des paramètres
            connexion.setRequestMethod("POST");
            connexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connexion.setFixedLengthStreamingMode(parametres.getBytes().length);
            // création de la requete d'envoi sur la connexion, avec les paramètres
            writer = new PrintWriter(connexion.getOutputStream());
            writer.print(parametres);
            // Une fois l'envoi réalisé, vide le canal d'envoi
            writer.flush();
            // Récupération du retour du serveur
            reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            ret = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // fermeture des canaux d'envoi et de réception
            try {
                writer.close();
            } catch (Exception e) {
            }
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * \brief Transmission de la donnée retour
     * \details Renvoie le résultat du serveur à \e processFinish via la variable \e ret
     *          et l'objet \e delegate
     * \param result \e Long code résultat
     */
    @Override
    protected void onPostExecute(Long result) {
        // ret contient l'information récupérée
        delegate.processFinish(this.ret);
    }

}
