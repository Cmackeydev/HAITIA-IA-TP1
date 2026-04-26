package ht.mbds.charles.tp1.jsf;

import jakarta.enterprise.context.Dependent;
import java.io.Serializable;

@Dependent
public class Modificateur implements Serializable {

    /**
     * Modifie la question pour générer une réponse.
     * @param question  la question posée par l'utilisateur
     * @param roleSysteme le rôle système (non null uniquement pour le premier message)
     * @return la réponse générée
     */
    public String modifier(String question, String roleSysteme) {
        if (roleSysteme != null) {
            // Premier message : affiche la description du rôle en majuscules suivie de la question
            return roleSysteme.toUpperCase() + "\n\n" + question;
        } else {
            // Messages suivants : traitement bonus
            return "Que savez-vous de " + question + " ?";
        }
        }
}
