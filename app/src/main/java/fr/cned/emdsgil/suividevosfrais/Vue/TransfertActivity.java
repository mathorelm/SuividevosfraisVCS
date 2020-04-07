package fr.cned.emdsgil.suividevosfrais.vue;

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

import fr.cned.emdsgil.suividevosfrais.controleur.AccesDistant;
import fr.cned.emdsgil.suividevosfrais.modele.FraisHf;
import fr.cned.emdsgil.suividevosfrais.controleur.Global;
import fr.cned.emdsgil.suividevosfrais.R;

/**
 * \author Louis-Marin Mathorel
 * \version 1.0
 * \date 30/03/2020
 * \class TransfertActivity TransfertActivity.java
 * \brief Vue transfert des données
 * <p>
 * \details Propose l'IHM "transfert des données " à l'utilisateur
 */
/**
 * Activity d'authentification pour transfert des données
 * vers BDD distante.
 */
public class TransfertActivity extends AppCompatActivity {

    public static String serveurMessage = "";

    /**
     * \brief Vérification de la connection à Internet
     * \details après autorisation de l'utilisateur, vérifie si le terminal
     *          est sous couverture GSM ou WIFI
     * \param context \e Context Accès au contexte de l'application
     * \return \e Boolean True si connecté.
     */
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) ||
                    (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.retour_accueil))) {
            retourActivityPrincipale();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfert);
        setTitle("GSB : Transférer les données locales");

        cmdValider_clic();
    }

    /**
     * \brief Valorise le tableau privé \e lesDonneesAEnvoyer
     * \details pour préparer l'envoi en chaîne JSON, parcours l'intégralité
     * du tableau \e listFraisMois et rentre les données les unes après les autres
     * en intercalant 'FF' dès qu'il trouve un frais forfaitisé, 'HF' pour un hors-forfait
     * \param login \e String login pour l'accès à la base de données
     * \param mdp \e String mot de passe transmis en clair
     * \return \e JSONArray Données mise en forme JSON
     */
    public JSONArray prepareFrais(String login, String mdp) {
        List lesDonneesAEnvoyer = new ArrayList();
        //Remplir la liste avec les éléments extraits de la HashTable
        Set keys = Global.listFraisMois.keySet();
        Iterator itr = keys.iterator();
        Object key;
        int cpt = 0;
        while (itr.hasNext()) {
            key = itr.next();
            lesDonneesAEnvoyer.add("FF");
            lesDonneesAEnvoyer.add(key);
            lesDonneesAEnvoyer.add(Global.listFraisMois.get(key).getEtape());
            lesDonneesAEnvoyer.add(Global.listFraisMois.get(key).getKm());
            lesDonneesAEnvoyer.add(Global.listFraisMois.get(key).getNuitee());
            lesDonneesAEnvoyer.add(Global.listFraisMois.get(key).getRepas());
            ArrayList<FraisHf> listeHF = Global.listFraisMois.get(key).getLesFraisHf();
            for (cpt = 0; cpt <= listeHF.size() - 1; cpt++) {
                lesDonneesAEnvoyer.add("HF");
                lesDonneesAEnvoyer.add(key);
                lesDonneesAEnvoyer.add(listeHF.get(cpt).getJour());
                lesDonneesAEnvoyer.add(listeHF.get(cpt).getMotif());
                lesDonneesAEnvoyer.add(listeHF.get(cpt).getMontant());
            }
        }
        return new JSONArray(lesDonneesAEnvoyer);
    }

    /**
     * \brief Réaction au clic sur l'image en haut à gauche
     * \details Retourne au menu principal
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgTransfertReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale();
            }
        });
    }

    /**
     * \brief Réaction au clic sur 'Valider'
     * \details Effectue la conversion du tableau en JSON
     *          vérifie la capacité à envoyer la donnée
     *          transfère la donnée en utilisant \e AccesDistant
     *          retourne à l'activité principale
     */
    private void cmdValider_clic() {
        findViewById(R.id.cmdAuthentValider).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (isConnectingToInternet(TransfertActivity.this)) {
                    String login = ((EditText) findViewById(R.id.txtTransfertLogin)).getText().toString();
                    String mdp = ((EditText) findViewById(R.id.txtTransfertMdp)).getText().toString();
                    JSONArray lesDonnees = prepareFrais(login, mdp);
                    AccesDistant monAcces = new AccesDistant();
                    monAcces.envoi("check", lesDonnees);

                    //Affichage du message d'attente et du gif animé
                    findViewById(R.id.cmdAuthentValider).setEnabled(Boolean.FALSE);
                    findViewById(R.id.transfertText).setVisibility(View.VISIBLE);
                    findViewById(R.id.transfertAttente).setVisibility(View.VISIBLE);
                    serveurMessage = "";
                    //TODO comment gérer la fin de l'attente et donc la suppression du message / GIF animé ?

                    retourActivityPrincipale();
                } else {
                    Toast.makeText(getApplicationContext(), "Aucune connection disponible. Veuillez réessayer ultérieurement.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    /**
     * Retour à l'activité principale (le menu)
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(TransfertActivity.this, MainActivity.class);
        startActivity(intent);

    }
}
