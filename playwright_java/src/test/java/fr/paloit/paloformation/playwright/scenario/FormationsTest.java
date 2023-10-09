package fr.paloit.paloformation.playwright.scenario;

import fr.paloit.paloformation.playwright.outil.PlaywrightExtension;
import fr.paloit.paloformation.playwright.page.FormationPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.UUID;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormationsTest {

    @RegisterExtension
    public static PlaywrightExtension playwright = new PlaywrightExtension();
    FormationPage formationPage;
    @BeforeEach
    public void debut() {
        formationPage= new FormationPage(playwright.page());
        formationPage.ouvrir();
    }
    @Test
    void ajouter_une_formation_inexistante() {

        assertThat(formationPage.titre).hasText("Liste des formations :");
        final int nb_items = formationPage.items_formation.all().size();

        UUID uuid = UUID.randomUUID();
        String nom_formation = "Test_" + uuid;

        assertTrue(formationPage.items_formation.all()
                .stream().noneMatch(p -> p.textContent().equals(nom_formation)));

        formationPage.ajouter_formation(nom_formation);

        assertEquals(nb_items + 1,formationPage.items_formation.all().size());

        assertEquals(1,formationPage.items_formation.all()
                .stream().filter(p -> p.textContent().equals(nom_formation))
                .count());

    }
    @Test
    void ajouter_une_formation_existante() {

        assertThat(formationPage.titre).hasText("Liste des formations :");
        final int nb_items = formationPage.items_formation.all().size();

        UUID uuid = UUID.randomUUID();
        String nom_formation = "Test_" + uuid;

        assertTrue(formationPage.items_formation.all()
                .stream().noneMatch(p -> p.textContent().equals(nom_formation)));

        formationPage.ajouter_formation(nom_formation);

        assertEquals(nb_items + 1,formationPage.items_formation.all().size());

        assertEquals(1,formationPage.items_formation.all()
                .stream().filter(p -> p.textContent().equals(nom_formation))
                .count());

        formationPage.ajouter_formation(nom_formation);

        assertEquals(nb_items + 1,formationPage.items_formation.all().size());

        assertEquals(1,formationPage.items_formation.all()
                .stream().filter(p -> p.textContent().equals(nom_formation))
                .count());

    }
}