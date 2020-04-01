package fr.cned.emdsgil.suividevosfrais.modele;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * \author emds
 * \version 1.0
 * \date ?????? (creation)
 * \class FraisMois FraisMois.java
 * \brief classe métier - Frais forfaitisés du mois
 *
 * \details Contient la description des frais forfaitisés.
 *          avec les getters et méthodes métiers (ajout/suppression).
 */
public class FraisMois implements Serializable {

    //liste des frais hors forfait du mois
    private final ArrayList<FraisHf> lesFraisHf;
    //mois concerné
    private Integer mois;
    //année concernée
    private Integer annee;
    // nombre d'étapes du mois
    private Integer etape;
    //nombre de km du mois
    private Integer km;
    //nombre de nuitées du mois
    private Integer nuitee;
    //nombre de repas du mois
    private Integer repas;

    public FraisMois(Integer annee, Integer mois) {
        this.annee = annee;
        this.mois = mois;
        this.etape = 0;
        this.km = 0;
        this.nuitee = 0;
        this.repas = 0;
        lesFraisHf = new ArrayList<>();
    }

    /**
     * \brief Ajout d'un frais hors forfait
     * \details Valorise le tableau privé lesFraisHf
     * \param montant \e Float Montant en euros du frais hors forfait
     * \param motif \e Justification du frais
     * \param jour \e Integer Jour concerné par le frais
     */
    public void addFraisHf(Float montant, String motif, Integer jour) {
        lesFraisHf.add(new FraisHf(montant, motif, jour));
    }

    /**
     * \brief Récupère le montant du frais Hors forfait
     * \param indice \e Integer indice du tableau des frais
     * \return \e Float un montant en euros
     */
    public float getLeFraisHFMontant(int indice) {
        return lesFraisHf.get(indice).getMontant();
    }

    /**
     * \brief Récupère le motif du frais Hors forfait
     * \param indice \e Integer indice du tableau des frais
     * \return \e String une justification
     */
    public String getleFraisHFMotif(int indice) {
        return lesFraisHf.get(indice).getMotif();
    }

    /**
     * \brief Récupère le jour du frais Hors forfait
     * \param indice \e Integer indice du tableau des frais
     * \return \e Integer le jour du mois concerné par le frais
     */
    public int getLeFraisHFJour(int indice) {
        return lesFraisHf.get(indice).getJour();
    }

    /**
     * \brief Suppression d'un frais hors forfait
     * \param index Indice du frais hors forfait à supprimer
     */
    public void supprFraisHf(int index) {
        lesFraisHf.remove(index);
    }

    public Integer getMois() {
        return mois;
    }

    public void setMois(Integer mois) {
        this.mois = mois;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Integer getEtape() {
        return etape;
    }

    public void setEtape(Integer etape) {
        this.etape = etape;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Integer getNuitee() {
        return nuitee;
    }

    public void setNuitee(Integer nuitee) {
        this.nuitee = nuitee;
    }

    public Integer getRepas() {
        return repas;
    }

    public void setRepas(Integer repas) {
        this.repas = repas;
    }

    public ArrayList<FraisHf> getLesFraisHf() {
        return lesFraisHf;
    }

}
