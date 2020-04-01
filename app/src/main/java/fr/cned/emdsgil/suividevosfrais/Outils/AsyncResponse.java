package fr.cned.emdsgil.suividevosfrais.outils;

/**
 * \author emds
 * \version 1.0
 * \date 07/08/2015 (creation)
 * \brief interface pour classe AccesHTTP
 *
 * \details autorise l'écriture sur 'save.fic' des données
 *          serialisées de frais.
 */
public interface AsyncResponse {
    void processFinish(String output);
}
