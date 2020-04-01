package fr.cned.emdsgil.suividevosfrais.modele;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import fr.cned.emdsgil.suividevosfrais.controleur.Global;
import fr.cned.emdsgil.suividevosfrais.R;

/**
 * \author emds
 * \author Mise à jour par Louis-Marin Mathorel
 * \version 2.0
 * \date ?????? (creation) 30/03/2020 (mise à jour)
 * \class FraisHfAdapter FraisHfAdapter.java
 * \brief Adapter pour les frais hors forfaits.
 * <p>
 * \details Permet l'affichage et la suppression des frais hors forfait
 */
public class FraisHfAdapter extends BaseAdapter {

    // liste des frais du mois
    private final ArrayList<FraisHf> lesFrais;
    private final LayoutInflater inflater;
    private final int key;

    /**
     * \brief Constructeur de l'adapter
     * \details valorise les propriétés private
     * \param context \e Context Accès au contexte de l'application
     * \param lesFrais \e ArrayList<FraisHF> Liste des frais hors forfait
     * \param laCle \e integer Clé d'accès au tableau lesFrais
     */
    public FraisHfAdapter(Context context, ArrayList<FraisHf> lesFrais, int laCle) {
        inflater = LayoutInflater.from(context);
        this.lesFrais = lesFrais;
        this.key = laCle;
    }

    /**
     * retourne le nombre d'éléments de la listview
     */
    @Override
    public int getCount() {
        return lesFrais.size();
    }

    /**
     * retourne l'item de la listview à un index précis
     */
    @Override
    public Object getItem(int index) {
        return lesFrais.get(index);
    }

    /**
     * retourne l'index de l'élément actuel
     */
    @Override
    public long getItemId(int index) {
        return index;
    }

    /**
     * Affichage dans la liste
     */
    /**
     * \brief Affichage dans la liste de l'Adapter
     * \details gènère la liste ou récupère le tag cliqué
     * \param index \e integer index de la donnée dans le tableau de frais
     * \param convertView \e View objet récupérant la liste de l'adapter
     * \param parent \e ViewGroup objet contenant le convertView
     */
    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_liste, parent, false);
            holder.txtListJour = convertView.findViewById(R.id.txtListJour);
            holder.txtListMontant = convertView.findViewById(R.id.txtListMontant);
            holder.txtListMotif = convertView.findViewById(R.id.txtListMotif);
            //déclaration du bouton pour pouvoir gérer sur lequel on clique
            holder.cmdSuppHf = convertView.findViewById(R.id.cmdSuppHf);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtListJour.setText(String.format(Locale.FRANCE, "%d", lesFrais.get(index).getJour()));
        holder.txtListMontant.setText(String.format(Locale.FRANCE, "%.2f", lesFrais.get(index).getMontant()));
        holder.txtListMotif.setText(lesFrais.get(index).getMotif());
        //enregistrement de l'indice du bouton pour écouteur
        holder.cmdSuppHf.setTag(index);
        //Création de l'écouteur d'événement
        holder.cmdSuppHf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Contient l'indice du Frais HF cliqué
                int indice = (int) v.getTag();
                //Effectuer la suppression
                if (Global.listFraisMois.containsKey(key)) {
                    Global.listFraisMois.get(key).supprFraisHf(indice);
                } else {
                    //lancer une exception ?
                }
                //mettre à jour la liste visuelle
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    /**
     * structure contenant les éléments d'une ligne
     */
    private class ViewHolder {
        TextView txtListJour;
        TextView txtListMontant;
        TextView txtListMotif;
        //Ajout du bouton supprimer
        ImageButton cmdSuppHf;
    }

}
