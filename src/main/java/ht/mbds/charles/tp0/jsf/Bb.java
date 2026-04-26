package ht.mbds.charles.tp0.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing bean pour la page JSF index.xhtml.
 * Portée view pour conserver l'état de la conversation qui dure pendant plusieurs requêtes HTTP.
 * La portée view nécessite l'implémentation de Serializable (le backing bean peut être mis en mémoire secondaire).
 */
@Named
@ViewScoped
public class Bb implements Serializable {

    /**
     * Rôle "système" que l'on attribuera plus tard à un LLM.
     * Possible d'écrire un nouveau rôle dans la liste déroulante.
     */
    private String roleSysteme;

    /**
     * Quand le rôle est choisi par l'utilisateur dans la liste déroulante,
     * il n'est plus possible de le modifier (voir code de la page JSF), sauf si on veut un nouveau chat.
     */
    private boolean roleSystemeChangeable = true;

    /**
     * Liste de tous les rôles de l'API prédéfinis.
     */
    private List<SelectItem> listeRolesSysteme;

    /**
     * Dernière question posée par l'utilisateur.
     */
    private String question;
    /**
     * Dernière réponse de l'API.
     */
    private String reponse = "";
    /**
     * La conversation depuis le début.
     */
    private StringBuilder conversation = new StringBuilder();

    /**
     * Service pour modifier la question et générer la réponse.
     */
    @Inject
    private Modificateur modificateur;

    /**
     * Contexte JSF. Utilisé pour qu'un message d'erreur s'affiche dans le formulaire.
     */
    @Inject
    private FacesContext facesContext;

    /**
     * Obligatoire pour un bean CDI (classe gérée par CDI), s'il y a un autre constructeur.
     */
    public Bb() {
    }

    public String getRoleSysteme() {
        return roleSysteme;
    }

    public void setRoleSysteme(String roleSysteme) {
        this.roleSysteme = roleSysteme;
    }

    public boolean isRoleSystemeChangeable() {
        return roleSystemeChangeable;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    /**
     * setter indispensable pour le textarea.
     *
     * @param reponse la réponse à la question.
     */
    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getConversation() {
        return conversation.toString();
    }

    public void setConversation(String conversation) {
        this.conversation = new StringBuilder(conversation);
    }

    /**
     * Envoie la question au serveur.
     *
     * @return null pour rester sur la même page.
     */
    public String envoyer() {
        if (question == null || question.isBlank()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Texte question vide", "Il manque le texte de la question");
            facesContext.addMessage(null, message);
            return null;
        }

        // Traite la question pour construire la réponse.
        String roleSystemePourModification = null;
        if (this.conversation.isEmpty()) { // Si la conversation n'a pas encore commencé
            roleSystemePourModification = this.roleSysteme; // Pour Modificateur.modifier()
            // Invalide la liste pour changer le rôle système
            this.roleSystemeChangeable = false;
        }
        this.reponse = this.modificateur.modifier(this.question, roleSystemePourModification);

        // La conversation contient l'historique des questions-réponses depuis le début.
        afficherConversation();
        return null;
    }

    /**
     * Pour un nouveau chat.
     * Termine la portée view en retournant "index" (la page index.xhtml sera affichée après le traitement
     * effectué pour construire la réponse) et pas null. null aurait indiqué de rester dans la même page (index.xhtml)
     * sans changer de vue.
     * @return "index"
     */
    public String nouveauChat() {
        return "index";
    }

    /**
     * Pour afficher la conversation dans le textArea de la page JSF.
     */
    private void afficherConversation() {
        this.conversation.append("== User:\n").append(question).append("\n== Serveur:\n").append(reponse).append("\n");
    }

    public List<SelectItem> getRolesSysteme() {
        if (this.listeRolesSysteme == null) {
            this.listeRolesSysteme = new ArrayList<>();

            String role = """
                    Vous êtes un assistant serviable. Vous aidez l'utilisateur à trouver les informations dont il a besoin.
                    Si l'utilisateur saisit une question, vous y répondez.
                    """;
            this.listeRolesSysteme.add(new SelectItem(role, "Assistant"));

            role = """
                    Vous êtes un interprète. Vous traduisez de l'anglais vers le français et du français vers l'anglais.
                    Si l'utilisateur saisit un texte en français, vous le traduisez en anglais.
                    Si l'utilisateur saisit un texte en anglais, vous le traduisez en français.
                    Si le texte contient seulement un à trois mots, donnez quelques exemples d'utilisation de ces mots en anglais.
                    """;
            this.listeRolesSysteme.add(new SelectItem(role, "Traducteur Anglais-Français"));

            role = """
                    Vous êtes un guide touristique. Si l'utilisateur saisit le nom d'un pays ou d'une ville,
                    vous lui indiquez les principaux lieux à visiter dans ce pays ou dans cette ville
                    et vous lui donnez le prix moyen d'un repas.
                    """;
            this.listeRolesSysteme.add(new SelectItem(role, "Guide touristique"));
        }

        return this.listeRolesSysteme;
    }
}
