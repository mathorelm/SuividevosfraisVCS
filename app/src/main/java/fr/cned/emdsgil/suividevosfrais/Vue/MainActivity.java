package fr.cned.emdsgil.suividevosfrais.vue;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Hashtable;

import fr.cned.emdsgil.suividevosfrais.modele.FraisMois;
import fr.cned.emdsgil.suividevosfrais.controleur.Global;
import fr.cned.emdsgil.suividevosfrais.modele.Serializer;
import fr.cned.emdsgil.suividevosfrais.R;

/**
 * \author emds
 * \author Louis-Marin Mathorel
 * \version 2.0
 * \date ?????? (creation) 30/03/2020 (mise à jour)
 * \class MainActivity MainActivity.java
 * \brief Vue principale de l'application
 * <p>
 * \details Propose le menu principal de l'application
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("GSB : Suivi des frais");
        // récupération des informations sérialisées
        recupSerialize();

        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdKm)), KmActivity.class);
        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdHf)), HfActivity.class);
        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdNuitee)), NuiteeActivity.class);
        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdHfRecap)), HfRecapActivity.class);
        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdEtape)), EtapeActivity.class);
        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdRepas)), RepasActivity.class);
        cmdTransfert_clic();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /**
     * \brief Récupère les données sérialisées si elles existent
     * \details Tente la récupération avec \e Serializer, remplit le tableau
     *          ou sinon crée un tableau vide.
     */
    private void recupSerialize() {
        Hashtable<?, ?> monHash = (Hashtable<?, ?>) Serializer.deSerialize(MainActivity.this);
        if (monHash != null) {
            Hashtable<Integer, FraisMois> monHashCast = new Hashtable<>();
            for (Hashtable.Entry<?, ?> entry : monHash.entrySet()) {
                monHashCast.put((Integer) entry.getKey(), (FraisMois) entry.getValue());
            }
            Global.listFraisMois = monHashCast;
        }
        //si rien n'a été récupéré, il faut créer la liste
        if (Global.listFraisMois == null) {
            Global.listFraisMois = new Hashtable<>();
        }
    }

    /**
     * \brief Réaction au clic sur une icône du menu
     * \details Ouvre l'activité adaptée au bouton cliqué.
     */
    private void cmdMenu_clic(ImageButton button, final Class classe) {
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // ouvre l'activité
                Intent intent = new Intent(MainActivity.this, classe);
                startActivity(intent);
            }
        });
    }

    /**
     * \brief Réaction au clic sur le bouton 'Transfert'
     * \details Charge \e TransfertActivity spécialisée dans la communication vers serveur distant
     */
    private void cmdTransfert_clic() {
        findViewById(R.id.cmdTransfert).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransfertActivity.class);
                startActivity(intent);
            }
        });
    }
}
