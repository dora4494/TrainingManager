package fr.paloit.paloformation.model;


import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;
    private String client;
    @ManyToMany
    @JoinTable(
            name = "session_participant",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private Set<Utilisateur> participants = new HashSet<>();


    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<ToDo> todos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "formateur_id")
    private Utilisateur formateur;
    private int duree;

    private Long cout;
    private String modalites;

    private int etat = 1;


    @ElementCollection
    @CollectionTable(name = "session_dates", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "session_date")
    private Set<LocalDate> dates = new HashSet<>();


    public Session(Long id, Formation formation, String client, Set<Utilisateur> participants, List<ToDo> todos, Utilisateur formateur, int duree, Long cout, String modalites, int etat, Set<LocalDate> dates) {
        this.id = id;
        this.formation = formation;
        this.client = client;
        this.participants = participants;
        this.todos = todos;
        this.formateur = formateur;
        this.duree = duree;
        this.cout = cout;
        this.modalites = modalites;
        this.etat = etat;
        this.dates = dates;
    }


    public Session() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Set<Utilisateur> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Utilisateur> participants) {
        this.participants = participants;
    }

    public List<ToDo> getTodos() {
        return todos;
    }

    public void setTodos(List<ToDo> todos) {
        this.todos = todos;
    }

    public Utilisateur getFormateur() {
        return formateur;
    }

    public void setFormateur(Utilisateur formateur) {
        this.formateur = formateur;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Long getCout() {
        return cout;
    }

    public void setCout(Long cout) {
        this.cout = cout;
    }

    public String getModalites() {
        return modalites;
    }

    public void setModalites(String modalites) {
        this.modalites = modalites;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public Set<LocalDate> getDates() {
        return dates;
    }

    public void setDates(Set<LocalDate> dates) {
        this.dates = dates;
    }


    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", formation=" + formation +
                ", client='" + client + '\'' +
                ", participants=" + participants +
                ", todos=" + todos +
                ", formateur=" + formateur +
                ", duree=" + duree +
                ", cout=" + cout +
                ", modalites='" + modalites + '\'' +
                ", etat=" + etat +
                ", dates=" + dates +
                '}';
    }
}

