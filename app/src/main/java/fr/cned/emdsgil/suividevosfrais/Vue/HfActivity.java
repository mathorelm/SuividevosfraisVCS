package fr.cned.emdsgil.suividevosfrais.vue;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import fr.cned.emdsgil.suividevosfrais.modele.FraisMois;
import fr.cned.emdsgil.suividevosfrais.controleur.Global;
import fr.cned.emdsgil.suividevosfrais.modele.Serializer;
import fr.cned.emdsgil.suividevosfrais.R;

/**
 * \author emds
 * \author Louis-Marin Mathorel
 * \version 2.0
 * \date ?????? (creation) 30/03/2020 (mise à jour)
 * \class HfActivity HfActivity.java
 * \brief Vue saisie des frais hors forfait
 * <p>
 * \details Propose l'IHM "saisie des frais hors forfait" à l'utilisateur
 */
public class HfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf);
        setTitle("GSB : Frais HF");
        // modification de l'affichage du DatePicker
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datHf), true);
        // mise à 0 du montant
        ((EditText) findViewById(R.id.txtHf)).setText("0");
        imgReturn_clic();
        cmdAjouter_clic();
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
     * \brief Réaction au clic sur l'image en haut à gauche
     * \details Retourne au menu principal
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgHfReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale();
            }
        }) ;
    }

    /**
     * \brief Réaction au clic sur 'Ajouter'
     * \details Effectue l'enregistrement en liste, la sérialisation puis retourne au menu principal
     */
    private void cmdAjouter_clic() {
        findViewById(R.id.cmdHfAjouter).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                enregListe();
                Serializer.serialize(Global.listFraisMois, HfActivity.this);
                retourActivityPrincipale();
            }
        }) ;
    }

    /**
     * \brief Enregistrement des données de l'IHM dans la liste
     * \details corrige la donnée si déjà existante, sinon la crée.
     */
    private void enregListe() {
        // récupération des informations saisies
        Integer annee = ((DatePicker) findViewById(R.id.datHf)).getYear();
        Integer mois = ((DatePicker) findViewById(R.id.datHf)).getMonth() + 1;
        Integer jour = ((DatePicker) findViewById(R.id.datHf)).getDayOfMonth();
        Float montant = Float.valueOf((((EditText) findViewById(R.id.txtHf)).getText().toString()));
        String motif = ((EditText) findViewById(R.id.txtHfMotif)).getText().toString();
        // enregistrement dans la liste
        Integer key = annee * 100 + mois;
        if (!Global.listFraisMois.containsKey(key)) {
            // creation du mois et de l'annee s'ils n'existent pas déjà
            Global.listFraisMois.put(key, new FraisMois(annee, mois));
        }
        Global.listFraisMois.get(key).addFraisHf(montant, motif, jour);
    }

    /**
     * \brief Retour à l'activité principale
     * \details Revient à 'MainActivity'
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(HfActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
