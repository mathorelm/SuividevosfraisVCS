package fr.cned.emdsgil.suividevosfrais.vue;

import android.content.Intent;
import android.os.Bundle;
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


import fr.cned.emdsgil.suividevosfrais.modele.FraisMois;
import fr.cned.emdsgil.suividevosfrais.controleur.Global;
import fr.cned.emdsgil.suividevosfrais.modele.Serializer;
import fr.cned.emdsgil.suividevosfrais.R;

import java.util.Locale;


/**
 * \author emds
 * \author Louis-Marin Mathorel
 * \version 2.0
 * \date ?????? (creation) 30/03/2020 (mise à jour)
 * \class NuiteeActivity  NuiteeActivity.java
 * \brief Vue saisie des frais de nuitée
 * <p>
 * \details Propose l'IHM "saisie des frais de nuitée" à l'utilisateur
 */
public class NuiteeActivity extends AppCompatActivity {

    // informations affichées dans l'activité
    private Integer annee;
    private Integer mois;
    private Integer nuitees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuitees);
        setTitle("GSB : Frais de nuitées");
        // modification de l'affichage du DatePicker
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datNuitee), false);
        // valorisation des propriétés
        valoriseProprietes();

        imgReturn_clic();
        cmdValider_clic();
        cmdPlus_clic();
        cmdMoins_clic();
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
     * \brief Valorise les propriétés
     * \details Fait correspondre aux propriétés de la classe les données
     *          affichées dans l'IHM
     */
    private void valoriseProprietes() {
        annee = ((DatePicker) findViewById(R.id.datNuitee)).getYear();
        mois = ((DatePicker) findViewById(R.id.datNuitee)).getMonth() + 1;
        // récupération de la nuitees correspondant au mois actuel
        nuitees = 0;
        Integer key = annee * 100 + mois;
        if (Global.listFraisMois.containsKey(key)) {
            nuitees = Global.listFraisMois.get(key).getNuitee();
        }
        ((TextView) findViewById(R.id.txtNuitees)).setText(String.format(Locale.FRANCE, "%d", nuitees));
    }

    /**
     * \brief Réaction au clic sur l'image en haut à gauche
     * \details Retourne au menu principal
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgNuiteesReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale();
            }
        });
    }

    /**
     * \brief Réaction au clic sur 'Valider'
     * \details Effectue la sérialisation puis retourne au menu principal
     */
    private void cmdValider_clic() {
        findViewById(R.id.cmdNuiteeValider).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Serializer.serialize(Global.listFraisMois, NuiteeActivity.this);
                retourActivityPrincipale();
            }
        });
    }

    /**
     * \brief Réaction au clic sur '+'
     * \details Ajoute 1 à la quantité et enregistre.
     */
    private void cmdPlus_clic() {
        findViewById(R.id.cmdNuiteePlus).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                nuitees += 1;
                enregNewQte();
            }
        });
    }

    /**
     * \brief Réaction au clic sur '-'
     * \details Retire 1 (jusqu'à 0) à la quantité et enregistre.
     */
    private void cmdMoins_clic() {
        findViewById(R.id.cmdNuiteeMoins).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                nuitees = Math.max(0, nuitees - 1); // suppression de 1 si possible
                enregNewQte();
            }
        });
    }

    /**
     * \brief Réaction au clic 'changement de date'
     * \details Met à jour l'affichage et valorise les propriétés.
     */
    private void dat_clic() {
        final DatePicker uneDate = findViewById(R.id.datNuitee);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                valoriseProprietes();
            }
        });
    }

    /**
     * \brief Enregistrement de la quantité
     * \details Met à jour la zone de texte et la liste de frais en mémoire, à la date choisie
     */
    private void enregNewQte() {
        // enregistrement dans la zone de texte
        ((TextView) findViewById(R.id.txtNuitees)).setText(String.format(Locale.FRANCE, "%d", nuitees));
        // enregistrement dans la liste
        Integer key = annee * 100 + mois;
        if (!Global.listFraisMois.containsKey(key)) {
            // creation du mois et de l'annee s'ils n'existent pas déjà
            Global.listFraisMois.put(key, new FraisMois(annee, mois));
        }
        Global.listFraisMois.get(key).setNuitee(nuitees);
    }

    /**
     * \brief Retour à l'activité principale
     * \details Revient à 'MainActivity'
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(NuiteeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
