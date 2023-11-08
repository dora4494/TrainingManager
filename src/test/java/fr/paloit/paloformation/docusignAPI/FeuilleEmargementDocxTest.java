package fr.paloit.paloformation.docusignAPI;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class FeuilleEmargementDocxTest {

    @Test
    public void testEnregistrement(@TempDir Path tempDir) throws Docx4JException, JAXBException {
        final String fichier = "src/test/resources/demo_emargement.docx";
        final FeuilleEmargementDocx feuilleEmargementDocx = new FeuilleEmargementDocx(fichier);
        final String texteInitial = getTexte(fichier);

        final Path fichierSauve = tempDir.resolve("DocumentSauve.docx");
        feuilleEmargementDocx.sauver(fichierSauve);

        final String texteObtenu = getTexte(fichierSauve.toString());
        assertEquals(texteInitial, texteObtenu);
    }

    @Test
    public void testRemplacement(@TempDir Path tempDir) throws Docx4JException, JAXBException {
        final String fichier = "src/test/resources/demo_emargement.docx";
        final FeuilleEmargementDocx feuilleEmargementDocx = new FeuilleEmargementDocx(fichier);
        final String texteInitial = getTexte(fichier);
        assertTrue(texteInitial.contains("<<LieuFormation>>"));
        assertFalse(texteInitial.contains("Paris"));

        feuilleEmargementDocx.remplacer("<<LieuFormation>>", "Paris");
        final Path fichierSauve = tempDir.resolve("DocumentSauve.docx");
        feuilleEmargementDocx.sauver(fichierSauve);

        final String texteObtenu = getTexte(fichierSauve.toString());
        assertFalse(texteObtenu.contains("<<LieuFormation>>"));
        assertTrue(texteObtenu.contains("Paris"));
    }

    @Test
    public void testRemplacementPlusieursTextes(@TempDir Path tempDir) throws Docx4JException, JAXBException {

        final Map<String, String> substitutions = Map.of(
                "<<LieuFormation>>", "Paris",
                "<<NomFormation>>", "Java");

        final String fichier = "src/test/resources/demo_emargement.docx";
        final FeuilleEmargementDocx feuilleEmargementDocx = new FeuilleEmargementDocx(fichier);

        final String texteInitial = getTexte(fichier);
        for (Map.Entry<String, String> substitution : substitutions.entrySet()) {
            assertTrue(texteInitial.contains(substitution.getKey()));
            assertFalse(texteInitial.contains(substitution.getValue()));
        }

        for (Map.Entry<String, String> substitution : substitutions.entrySet()) {
            feuilleEmargementDocx.remplacer(substitution.getKey(), substitution.getValue());
        }

        final Path fichierSauve = tempDir.resolve("DocumentSauve.docx");
        feuilleEmargementDocx.sauver(fichierSauve);

        final String texteObtenu = getTexte(fichierSauve.toString());
        for (Map.Entry<String, String> substitution : substitutions.entrySet()) {
            assertFalse(texteObtenu.contains(substitution.getKey()));
            assertTrue(texteObtenu.contains(substitution.getValue()));
        }
    }

    @Test
    public void testAjoutLigneEmargement(@TempDir Path tempDir) throws Docx4JException, JAXBException {
        final String fichier = "src/test/resources/demo_emargement.docx";

        final Path fichierSauve = tempDir.resolve("DocumentSauve.docx");
        int nbLigneInitial = 0;
        {
            final FeuilleEmargementDocx feuilleEmargementDocx = new FeuilleEmargementDocx(fichier);


            final MainDocumentPart mainDocumentPartInitial = WordprocessingMLPackage.load(new File(fichier)).getMainDocumentPart();
            final Tbl tableInitial = FeuilleEmargementDocx.getElements(mainDocumentPartInitial, Tbl.class).get(0);
            nbLigneInitial = FeuilleEmargementDocx.getElements(tableInitial, Tr.class).size();

            feuilleEmargementDocx.ajouterLigneEmargement("John Doe");
            feuilleEmargementDocx.sauver(fichierSauve);
        }
        {
            final MainDocumentPart mainDocumentPartFinal = WordprocessingMLPackage.load(fichierSauve.toFile()).getMainDocumentPart();
            final Tbl tableFinal = FeuilleEmargementDocx.getElements(mainDocumentPartFinal, Tbl.class).get(0);
            final List<Tr> lignes = FeuilleEmargementDocx.getElements(tableFinal, Tr.class);
            int nbLigneFinal = lignes.size();

            assertEquals(nbLigneInitial + 1, nbLigneFinal);

            final Tr derniereLigne = lignes.get(lignes.size() - 1);
            final List<P> celluleNom = FeuilleEmargementDocx.getElements(derniereLigne, P.class);

            assertEquals("John Doe", celluleNom.get(0).toString());
        }
    }


    private String getTexte(String fichier) throws Docx4JException, JAXBException {
        File doc = new File(fichier);
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(doc);
        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
        String xpath = "//w:p";
        List<Object> nodes = mainDocumentPart.getJAXBNodesViaXPath(xpath, true);
        return nodes.stream().map(Objects::toString).collect(Collectors.joining(" "));
    }
}
