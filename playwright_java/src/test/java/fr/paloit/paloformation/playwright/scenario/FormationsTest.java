package fr.paloit.paloformation.playwright.scenario;

import fr.paloit.paloformation.playwright.outil.PlaywrightExtension;
import fr.paloit.paloformation.playwright.page.DetailFormationPage;
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
        formationPage = new FormationPage(playwright.page());
        formationPage.ouvrir();
    }

    @Test
    void page_des_formations() {
        assertThat(formationPage.titre).hasText("Liste des formations");
    }

    @Test
    void ajouter_une_formation_inexistante() {
        final int nb_items = formationPage.items_formation.all().size();

        UUID uuid = UUID.randomUUID();
        String nom_formation = "Test_" + uuid;

        assertTrue(formationPage.items_formation.all()
                .stream().noneMatch(p -> p.textContent().equals(nom_formation)));

        formationPage.ajouterFormation(nom_formation);

        assertEquals(nb_items + 1,formationPage.items_formation.all().size());

        assertEquals(1,formationPage.items_formation.all()
                .stream().filter(p -> p.textContent().equals(nom_formation))
                .count());

    }
    @Test
    void ajouter_une_formation_existante() {

        final int nb_items = formationPage.items_formation.all().size();

        UUID uuid = UUID.randomUUID();
        String nom_formation = "Test_" + uuid;

        assertTrue(formationPage.items_formation.all()
                .stream().noneMatch(p -> p.textContent().equals(nom_formation)));

        formationPage.ajouterFormation(nom_formation);

        assertEquals(nb_items + 1,formationPage.items_formation.all().size());

        assertEquals(1,formationPage.items_formation.all()
                .stream().filter(p -> p.textContent().equals(nom_formation))
                .count());

        formationPage.ajouterFormation(nom_formation);

        assertEquals(nb_items + 1,formationPage.items_formation.all().size());

        assertEquals(1,formationPage.items_formation.all()
                .stream().filter(p -> p.textContent().equals(nom_formation))
                .count());

    }

    @Test
    void afficher_une_formation() {
        DetailFormationPage detailFormationPage = new DetailFormationPage(playwright.page());

        String nom_formation = creer_formation();
        formationPage.ouvrir();
        assertEquals(1, playwright.page().locator("a:has-text(\""+nom_formation+"\")").count());
        formationPage.allerSurFormation(nom_formation);

        assertEquals(nom_formation, detailFormationPage.getTitre());
        detailFormationPage.supprimerFormation();

        formationPage.ouvrir();
        assertEquals(0, playwright.page().locator("a:has-text(\""+nom_formation+"\")").count());


    }

    private String creer_formation() {
        final int nb_items = formationPage.items_formation.all().size();

        UUID uuid = UUID.randomUUID();
        String nom_formation = "Test_" + uuid;

        assertTrue(formationPage.items_formation.all()
                .stream().noneMatch(p -> p.textContent().equals(nom_formation)));

        formationPage.ajouterFormation(nom_formation);

        assertEquals(nb_items + 1,formationPage.items_formation.all().size());
        return nom_formation;
    }
}