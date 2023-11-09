package fr.paloit.paloformation.docusignAPI;

import jakarta.xml.bind.JAXBException;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    public void testRecuperationContenuEnTableauDeBytes(@TempDir Path tempDir) throws Docx4JException, IOException {
        final String fichier = "src/test/resources/demo_emargement.docx";
        final FeuilleEmargementDocx feuilleEmargementDocx = new FeuilleEmargementDocx(fichier);

        final Path fichierSauve = tempDir.resolve("DocumentSauve.docx");
        feuilleEmargementDocx.sauver(fichierSauve);

        assertArrayEquals(Files.readAllBytes(fichierSauve), feuilleEmargementDocx.getBytes());
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

    // TODO On doit lever une erreur sur le texte a substitué n'est pas trouvé.
    // TODO Faut il gérer les textes coupés sur plusieurs balises ?
    @Test
    public void testRemplacementPlusieursTextes(@TempDir Path tempDir) throws Docx4JException, JAXBException {

        final Map<String, String> substitutions = Map.of(
                "<<LieuFormation>>", "Paris",
                "<<NomFormation>>", "Java",
                "<<NomFormateur>>", "John Doe");

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

    public static Stream<Arguments> testRemplirParticipantsAjouteleNomSurLaPremiereColonneDeChaqueLigne() {
        return Stream.of(
                Arguments.of("Cellule avec texte", new TemplateDocx().avecTable(10, 3, "texte")),
                Arguments.of("Cellule sans texte", new TemplateDocx().avecTable(10, 3))
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testRemplirParticipantsAjouteleNomSurLaPremiereColonneDeChaqueLigne(String description, TemplateDocx template, @TempDir Path tempDir) throws Docx4JException {

        final Path fichier = tempDir.resolve("template.docx");
        template.sauver(fichier);

        final Path fichierGenere = Paths.get("build/DocumentSauve.docx");
        {
            final FeuilleEmargementDocx feuilleEmargementDocx = new FeuilleEmargementDocx(fichier);

            final MainDocumentPart mainDocumentPartInitial = WordprocessingMLPackage.load(fichier.toFile()).getMainDocumentPart();

            feuilleEmargementDocx.remplirParticipants(List.of("John Doe", "Paul Young", "Marc Zen"));
            feuilleEmargementDocx.sauver(fichierGenere);
        }
        {
            final MainDocumentPart mainDocumentPartFinal = WordprocessingMLPackage.load(fichierGenere.toFile()).getMainDocumentPart();
            final Tbl tableFinal = FeuilleEmargementDocx.getElements(mainDocumentPartFinal, Tbl.class).get(0);
            final List<Tr> lignes = FeuilleEmargementDocx.getElements(tableFinal, Tr.class);

            assertEquals("John Doe", FeuilleEmargementDocx.getElements(lignes.get(2), P.class).get(0).toString());
            assertEquals("Paul Young", FeuilleEmargementDocx.getElements(lignes.get(3), P.class).get(0).toString());
            assertEquals("Marc Zen", FeuilleEmargementDocx.getElements(lignes.get(4), P.class).get(0).toString());
        }
    }

    // TODO ce comportement devra évoluer pour permettre d'ajouter plus de participants
    @Test
    public void testRemplirParticipantsAvecPlusDeParticipantsQueDelignesLeveUneErreur(@TempDir Path tempDir) throws Docx4JException {

        final Path fichier = tempDir.resolve("template.docx");
        final int nombreLigne = 5;
        new TemplateDocx().avecTable(nombreLigne, 3, "texte").sauver(fichier);

        final List<String> participants = IntStream.range(0, nombreLigne + 3).mapToObj(__ -> "").collect(Collectors.toList());

        final FeuilleEmargementDocx feuilleEmargementDocx = new FeuilleEmargementDocx(fichier);
        final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> feuilleEmargementDocx.remplirParticipants(participants));
        // Le tableau fait 5 lignes avec 2 lignes d'en-tête.
        assertEquals("La feuille d'émargement ne peut contenir que 3 particpants", runtimeException.getMessage());
    }

    private String getTexte(String fichier) throws Docx4JException, JAXBException {
        File doc = new File(fichier);
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(doc);
        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
        String xpath = "//w:p";
        List<Object> nodes = mainDocumentPart.getJAXBNodesViaXPath(xpath, true);
        return nodes.stream().map(Objects::toString).collect(Collectors.joining(" "));
    }

    static class TemplateDocx {

        private final WordprocessingMLPackage wordPackage;

        public TemplateDocx() {
            try {
                wordPackage = WordprocessingMLPackage.createPackage();
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e);
            }

            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            mainDocumentPart.addStyledParagraphOfText("Title", "Feuille émargement");
            mainDocumentPart.addParagraphOfText("Formation : <<NomFormation>>");
        }

        private TemplateDocx avecTable(int nombreLigne, int nombreColonne, String texte) {
            int writableWidthTwips = wordPackage.getDocumentModel()
                    .getSections().get(0).getPageDimensions().getWritableWidthTwips();
            Tbl tbl = TblFactory.createTable(nombreLigne, nombreColonne, writableWidthTwips / nombreColonne);

            if (texte != null) {
                for (Tr tr : (List<Tr>) (List) tbl.getContent()) {
                    for (Tc td : (List<Tc>) (List) tr.getContent()) {
                        positionnerTexte(td, texte);
                    }
                }
            }
            wordPackage.getMainDocumentPart().addObject(tbl);
            return this;
        }

        private TemplateDocx avecTable(int nombreLigne, int nombreColonne) {
            return avecTable(nombreLigne, nombreColonne, null);

        }

        public void sauver(Path nomFichier) throws Docx4JException {
            wordPackage.save(nomFichier.toFile());
        }

        private static void positionnerTexte(Tc td, String texte) {
            final Text text = new Text();
            text.setValue(texte);

            final R r = new R();
            r.getContent().add(text);

            final P p = (P) td.getContent().get(0);
            p.getContent().clear();
            p.getContent().add(r);
        }
    }


}
