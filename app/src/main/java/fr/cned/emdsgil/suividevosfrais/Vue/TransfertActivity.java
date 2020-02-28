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

import fr.cned.emdsgil.suividevosfrais.Modele.AccesDistant;
import fr.cned.emdsgil.suividevosfrais.Modele.FraisHf;
import fr.cned.emdsgil.suividevosfrais.Modele.Global;
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

    public static JSONArray prepareFraisForfait() {

        List lesFraisForfait = new ArrayList();
        //Remplir la liste avec les éléments extraits de la HashTable
        Set keys = Global.listFraisMois.keySet();
        Iterator itr = keys.iterator();
        Object key;
        while (itr.hasNext()) {
            key = itr.next();
            lesFraisForfait.add(key);
            lesFraisForfait.add(Global.listFraisMois.get(key).getKm());
            lesFraisForfait.add(Global.listFraisMois.get(key).getRepas());
            lesFraisForfait.add(Global.listFraisMois.get(key).getEtape());
            lesFraisForfait.add(Global.listFraisMois.get(key).getNuitee());
        }

        JSONArray FF = new JSONArray(lesFraisForfait);
        Log.d("prepareFraisForfait", FF.toString());
        return FF;
    }

    public static JSONArray prepareFraisHF() {
        List lesFraisHorsForfait = new ArrayList();
        //Remplir la liste avec les éléments extraits de la HashTable
        Set keys = Global.listFraisMois.keySet();
        Iterator itr = keys.iterator();
        Object key;
        int cpt = 0;
        while (itr.hasNext()) {
            key = itr.next();
            ArrayList<FraisHf> listeHF = Global.listFraisMois.get(key).getLesFraisHf();
            for (cpt = 0; cpt <= listeHF.size() - 1; cpt++) {
                lesFraisHorsForfait.add(key);
                lesFraisHorsForfait.add(listeHF.get(cpt).getJour());
                lesFraisHorsForfait.add(listeHF.get(cpt).getMotif());
                lesFraisHorsForfait.add(listeHF.get(cpt).getMontant());
            }

        }
        JSONArray FhF = new JSONArray(lesFraisHorsForfait);
        Log.d("prepareFraisHorsForfait", FhF.toString());
        return FhF;
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

                List list = new ArrayList();
                list.add(login);
                list.add(mdp);
                JSONArray login_mdp = new JSONArray(list);
                AccesDistant monacces = new AccesDistant();
                monacces.envoi("check", login_mdp);
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
