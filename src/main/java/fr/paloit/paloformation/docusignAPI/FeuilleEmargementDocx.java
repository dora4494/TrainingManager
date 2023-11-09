package fr.paloit.paloformation.docusignAPI;

import fr.paloit.paloformation.service.EmargementService;
import jakarta.xml.bind.JAXBElement;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FeuilleEmargementDocx implements EmargementService.FeuilleEmargement {

    WordprocessingMLPackage wordMLPackage;
    private File file;

    public FeuilleEmargementDocx(String cheminFichier) throws Docx4JException {
        this(Paths.get(cheminFichier));
    }

    public FeuilleEmargementDocx(Path cheminFichier) throws Docx4JException {
        this(cheminFichier.toFile());
    }

    public FeuilleEmargementDocx(File fichier) throws Docx4JException {
        file = fichier;
        wordMLPackage = WordprocessingMLPackage.load(file);
    }

    public void remplacer(String texteARemplacer, String nouveauTexte) {
        final List<Text> texts = getElements(wordMLPackage.getMainDocumentPart(), Text.class);
        for (Text text : texts) {
            remplacerTexte(text, texteARemplacer, nouveauTexte);
        }
    }

    public static <T> List<T> getElements(Object obj, Class<T> toSearch) {

        if (obj instanceof JAXBElement) {
            return getElements(((JAXBElement<?>) obj).getValue(), toSearch);
        }

        if (obj.getClass().equals(toSearch)) {
            return List.of((T) obj);
        }

        if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            return children.stream()
                    .flatMap(child -> getElements(child, toSearch).stream())
                    .toList();
        }

        return Collections.emptyList();
    }


    private static void remplacerTexte(Text text, String toReplace, String newText) {
        String textValue = text.getValue();
        if (textValue.contains(toReplace)) {
            text.setValue(textValue.replace(toReplace, newText));
        }
    }

    public void sauver(Path fichierCible) throws Docx4JException {
        wordMLPackage.save(fichierCible.toFile());
    }

    public void sauver(OutputStream output) throws Docx4JException {
        wordMLPackage.save(output);
    }

    public void ajouterLigneEmargement(String nom) {
        final List<Tbl> tables = getElements(wordMLPackage.getMainDocumentPart(), Tbl.class);
        for (Tbl table : tables) {
            final List<Object> tableContent = table.getContent();
            final Tr lastRow = (Tr) tableContent.get(tableContent.size() - 1);
            final Tr newLine = XmlUtils.deepCopy(lastRow);
            // TODO Vérifier ce qu'il se passe si la cellule ne contient pas de Text
//            final List<Object> content = newLine.getContent();
//            final Tc cell = content.stream()
//                    .map(o -> (Tc) ((JAXBElement) o).getValue())
//                    .findFirst().orElseThrow(() -> new RuntimeException());
//            final P premierP = cell.getContent().stream().map(o -> (P) o).toList().get(0);
//            final R premierR = (R) premierP.getContent().get(0);
//            final Text premierText = (Text) ((JAXBElement) premierR.getContent().get(0)).getValue();
//            premierText.setValue(nom);
            // Positionne le texte sur le premier Text trouvé sur la ligne.

//            FeuilleEmargementDocx.getElements(newLine, Text.class).get(0).setValue(nom);
            table.getContent().add(newLine);
            ecrireDansCellule(table, tableContent.size()-1, 0, nom);
        }

    }

    public String getTexte()  {
        final List<P> stream = getElements(this.wordMLPackage, P.class);
        return getElements(this.wordMLPackage.getMainDocumentPart(), P.class).stream()
                .map(Objects::toString)
                .collect(Collectors.joining(" "));
    }

    public void remplirParticipants(List<String> noms) {
        final List<Tbl> tables = getElements(wordMLPackage.getMainDocumentPart(), Tbl.class);
        final Tbl tableEmargement = tables.get(0);
        for (int indexNom = 0; indexNom < noms.size(); indexNom++) {
            final int NB_LIGNE_EN_TETE = 2;
            final int indexLigne = indexNom + NB_LIGNE_EN_TETE;
            final int indexColonne = 0;
            List<Tr> lignes = FeuilleEmargementDocx.getElements(tableEmargement, Tr.class);
            if (lignes.size() <= indexLigne) {
                throw new RuntimeException("La feuille d'émargement ne peut contenir que " + (lignes.size()-NB_LIGNE_EN_TETE) + " particpants");
            }
            ecrireDansCellule(tableEmargement, indexLigne, indexColonne, noms.get(indexNom));
        }
    }

    private static void ecrireDansCellule(Tbl table, int indexLigne, int indexColonne, String texte) {
        List<Tr> lignes = FeuilleEmargementDocx.getElements(table, Tr.class);
        final Tr ligne = lignes.get(indexLigne);
        final Tc td = FeuilleEmargementDocx.getElements(ligne, Tc.class).get(indexColonne);
        final P p = FeuilleEmargementDocx.getElements(td, P.class).get(0);
        final R premierR = getElementAddedIfNeeded(p, 0, () -> new R());
        final Text premierText = getElementAddedIfNeeded(premierR, 0, () -> new Text());
        premierText.setValue(texte);
    }

    private static <CA extends ContentAccessor, T> T getElementAddedIfNeeded(CA contentAccessor, int index, Supplier<T> builder) {
        final List<T> content = (List<T>)contentAccessor.getContent();
        while (content.size() <= 0) {
            content.add(builder.get());
        }

        final T element = content.get(index);
        if (element instanceof JAXBElement) {
            return ((JAXBElement<T>) element).getValue();
        } else {
            return element;
        }
    }

    @Override
    public String getNomFichier() {
        return file.getName();
    }

    @Override
    public byte[] getBytes() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            sauver(output);
        } catch (Docx4JException e) {
            throw new RuntimeException("La feuille d'émargement n'a pas pu être générée.", e);
        }
        return output.toByteArray();
    }
}
