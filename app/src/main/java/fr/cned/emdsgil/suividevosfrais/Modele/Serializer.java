package fr.cned.emdsgil.suividevosfrais.modele;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fr.cned.emdsgil.suividevosfrais.controleur.Global;

/**
 * \author emds
 * \version 1.0
 * \date ?????? (creation)
 * \class Serializer Serializer.java
 * \brief permet de (de)serialiser des objets (frais)
 *
 * \details autorise l'écriture sur 'save.fic' des données
 *          serialisées de frais.
 */
public abstract class Serializer {

    /**
     * \brief Sérialise un objet
     * \details permet d'enregistrer dans le fichier précisé dans Global
     *          la donnée passée en paramètre
     * \param object \e Object objet cible de la sérialisation
     * \param context \e Context contexte appelant la sérialisation (obligatoire)
     */
    public static void serialize(Object object, Context context) {
        try {
            FileOutputStream file = context.openFileOutput(Global.filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos;
            try {
                oos = new ObjectOutputStream(file);
                oos.writeObject(object);
                oos.flush();
                oos.close();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * \brief Dé-sérialise un objet
     * \details permet de récupérer depuis le fichier précisé dans Global
     *          la donnée désérialisée
     * \param context \e Context contexte appelant la sérialisation (obligatoire)
     * \return \e Object un objet désérialisé.
     */
    public static Object deSerialize(Context context) {
        try {
            FileInputStream file = context.openFileInput(Global.filename);
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(file);
                try {
                    Object object = ois.readObject();
                    ois.close();
                    file.close();
                    return object;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // fichier non trouvé
            e.printStackTrace();
        }
        return null;
    }
}
