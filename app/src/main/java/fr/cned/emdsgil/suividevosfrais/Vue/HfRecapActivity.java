package fr.cned.emdsgil.suividevosfrais.vue;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import fr.cned.emdsgil.suividevosfrais.modele.FraisHf;
import fr.cned.emdsgil.suividevosfrais.modele.FraisHfAdapter;
import fr.cned.emdsgil.suividevosfrais.controleur.Global;
import fr.cned.emdsgil.suividevosfrais.R;

/**
 * \author emds
 * \author Louis-Marin Mathorel
 * \version 2.0
 * \date ?????? (creation) 30/03/2020 (mise à jour)
 * \class HfRecapActivity HfRecapActivity.java
 * \brief Vue récapitulative des frais hors forfait
 * <p>
 * \details Affiche le récapitulatif des frais hors forfait en
 * utilisant FraisHfAdapter
 */
public class HfRecapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf_recap);
        setTitle("GSB : Récap Frais HF");
        // modification de l'affichage du DatePicker
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datHfRecap), false);
        // valorisation des propriétés
        afficheListe();
        imgReturn_clic();
        dat_clic();
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
     * \brief Affiche la liste des frais hors forfait
     * \details Recherche dans listFraisMois la date sélectionnée et affiche la donnée
     */
    private void afficheListe() {
        Integer annee = ((DatePicker) findViewById(R.id.datHfRecap)).getYear();
        Integer mois = ((DatePicker) findViewById(R.id.datHfRecap)).getMonth() + 1;
        // récupération des frais HF pour cette date
        Integer key = annee * 100 + mois;
        ArrayList<FraisHf> liste;
        if (Global.listFraisMois.containsKey(key)) {
            liste = Global.listFraisMois.get(key).getLesFraisHf();
        } else {
            liste = new ArrayList<>();
        }
        ListView listView = findViewById(R.id.lstHfRecap);
        FraisHfAdapter adapter = new FraisHfAdapter(HfRecapActivity.this, liste, key);
        listView.setAdapter(adapter);
    }

    /**
     * \brief Réaction au clic sur l'image en haut à gauche
     * \details Retourne au menu principal
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgHfRecapReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale();
            }
        });
    }

    /**
     * \brief Réaction au clic 'changement de date'
     * \details Met à jour l'affichage et valorise les propriétés.
     */
    private void dat_clic() {
        final DatePicker uneDate = findViewById(R.id.datHfRecap);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                afficheListe();
            }
        });
    }

    /**
     * \brief Retour à l'activité principale
     * \details Revient à 'MainActivity'
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(HfRecapActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
