package fr.paloit.paloformation.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    @ManyToOne
    @JoinColumn(name = "formateur_id")
    private Utilisateur formateur;
    private int duree;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long cout;
    private String modalites;


    public Session(Long id, Formation formation, String client, Set<Utilisateur> participants, Utilisateur formateur, int duree, LocalDate dateDebut, LocalDate dateFin, Long cout, String modalites) {
        this.id = id;
        this.formation = formation;
        this.client = client;
        this.participants = participants;
        this.formateur = formateur;
        this.duree = duree;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.cout = cout;
        this.modalites = modalites;
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

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
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


    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", formation=" + formation +
                ", client='" + client + '\'' +
                ", participants=" + participants +
                ", formateur=" + formateur +
                ", duree=" + duree +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", cout=" + cout +
                ", modalites='" + modalites + '\'' +
                '}';
    }
}

