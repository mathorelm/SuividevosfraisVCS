package fr.cned.emdsgil.suividevosfrais.Vue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import fr.cned.emdsgil.suividevosfrais.Modele.FraisMois;
import fr.cned.emdsgil.suividevosfrais.Modele.Global;
import fr.cned.emdsgil.suividevosfrais.Modele.Serializer;
import fr.cned.emdsgil.suividevosfrais.R;

//import android.support.v7.app.AppCompatActivity;

/**
 * Activity d'authentification pour transfert des données
 * vers BDD distante.
 */
public class AuthentActivity extends AppCompatActivity {

    // informations affichées dans l'activité
    private Integer annee;
    private Integer mois;
    private Integer etapes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Chargement", "Entrée dans AuthentActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authent);
        setTitle("GSB : Transférer les données locales");
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
                /*Serializer.serialize(Global.listFraisMois, AuthentActivity.this);*/
                retourActivityPrincipale();
            }
        });
    }

    /**
     * Retour à l'activité principale (le menu)
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(AuthentActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
