package fr.cned.emdsgil.suividevosfrais.Vue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

//import android.support.v7.app.AppCompatActivity;

/**
 * Activity d'authentification pour transfert des données
 * vers BDD distante.
 */
public class TransfertActivity extends AppCompatActivity {

    public static Boolean isAuthenticated = Boolean.FALSE;
    // informations affichées dans l'activité
    private Integer annee;
    private Integer mois;
    private Integer etapes;
    List lesDonneesAEnvoyer = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfert);
        setTitle("GSB : Transférer les données locales");
        //Activation du bouton Valider
        cmdValider_clic();
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


    /**
     * Valorisation des propriétés avec les informations affichées
     */
    private void valoriseProprietes() {
        /*annee = ((DatePicker) findViewById(R.id.datEtape)).getYear();
        mois = ((DatePicker) findViewById(R.id.datEtape)).getMonth() + 1;
        // récupération de la etapes correspondant au mois actuel
        etapes = 0;
        Integer key = annee * 100 + mois;
        if (Global.listFraisMois.containsKey(key)) {
            etapes = Global.listFraisMois.get(key).getEtape();
        }
        ((TextView) findViewById(R.id.txtEtapes)).setText(String.format(Locale.FRANCE, "%d", etapes));*/
    }

    public void prepareFrais() {
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
    }

    /**
     * Sur la selection de l'image : retour au menu principal
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgTransfertReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale();
            }
        });
    }

    /**
     * Sur le clic du bouton valider : sérialisation
     */
    private void cmdValider_clic() {
        findViewById(R.id.cmdAuthentValider).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Effectuer la vérification mot de passe
                String login = ((EditText) findViewById(R.id.txtTransfertLogin)).getText().toString();

                String mdp = ((EditText) findViewById(R.id.txtTransfertMdp)).getText().toString();

                lesDonneesAEnvoyer.clear();
                lesDonneesAEnvoyer.add(login);
                lesDonneesAEnvoyer.add(mdp);
                prepareFrais();
                JSONArray lesDonnees = new JSONArray(lesDonneesAEnvoyer);
                AccesDistant monacces = new AccesDistant();
                monacces.envoi("check", lesDonnees);
                Log.d("envoi", lesDonneesAEnvoyer.toString());
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
