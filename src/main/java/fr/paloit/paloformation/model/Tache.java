package fr.paloit.paloformation.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tache")
public class Tache {

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
