package fr.cned.emdsgil.suividevosfrais.modele;

import java.io.Serializable;

/**
 * \author emds
 * \version 1.0
 * \date ?????? (creation)
 * \class FraisHF FraisHF.java
 * \brief classe métier - Frais hors forfait
 *
 * \details Contient la description d'un frais hors-forfait
 *          avec les getters.
 */

public class FraisHf implements Serializable {

    private final Float montant;
    private final String motif;
    private final Integer jour;

    public FraisHf(Float montant, String motif, Integer jour) {
        this.montant = montant;
        this.motif = motif;
        this.jour = jour;
    }

    public Float getMontant() {
        return montant;
    }

    public String getMotif() {
        return motif;
    }

    public Integer getJour() {
        return jour;
    }

}
