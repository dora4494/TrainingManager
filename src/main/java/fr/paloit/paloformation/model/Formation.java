package fr.paloit.paloformation.model;

import jakarta.persistence.*;

@Entity
@Table(name = "formation")
public class Formation {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String intitule;


    public Formation(Long id, String intitule) {
        this.id = id;
        this.intitule = intitule;
    }


    public Formation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", intitule='" + intitule + '\'' +
                '}';
    }
}
