package fr.paloit.paloformation.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
@Table(name = "tache")
public class Tache {

    public boolean estDeType(Type type) {
        return type.id == this.id;
    }

    public Type getType() {
        return Arrays.stream(Type.values())
                .filter(type -> type.id == this.id)
                .findFirst()
                .orElse(Type.INDETERMINE);
    }
    public enum Type {
        INDETERMINE(-1),
        INVITER_PARTICIPANT(4),
        CREER_FEUILLE_EMARGEMENT(5),
        CREER_ATTESTATION_FORMATION(6),
        ENVOYER_FEUILLE_EMARGEMENT(7),
        ENVOYER_QUESTIONNAIRE(8),
        TRANSMETTRE_ATTESTATION_FORMATION(9);

        private final int id;

        Type(int id) {
            this.id = id;
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String libelle;

    @OneToMany(mappedBy = "tache")
    private List<ToDo> todos = new ArrayList<>();

    public Tache(Long id, String libelle, List<ToDo> todos) {
        this.id = id;
        this.libelle = libelle;
        this.todos = todos;
    }

    public Tache() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<ToDo> getTodos() {
        return todos;
    }

    public void setTodos(List<ToDo> todos) {
        this.todos = todos;
    }

    @Override
    public String toString() {
        return "Tache{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", todos=" + todos +
                '}';
    }
}
