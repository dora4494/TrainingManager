package fr.paloit.paloformation.docusignAPI;

import jakarta.xml.bind.JAXBElement;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FeuilleEmargementDocx {
    private final Path cheminFichier;
    WordprocessingMLPackage wordMLPackage;

    public FeuilleEmargementDocx(String cheminFichier) throws Docx4JException {
        this.cheminFichier = Paths.get(cheminFichier);
        wordMLPackage = WordprocessingMLPackage.load(this.cheminFichier.toFile());
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

    public void ajouterLigneEmargement(String nom) {
        final List<Tbl> tables = getElements(wordMLPackage.getMainDocumentPart(), Tbl.class);
        for (Tbl table : tables) {
            final List<Object> tableContent = table.getContent();
            final Tr lastRow = (Tr) tableContent.get(tableContent.size() - 4);
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
            FeuilleEmargementDocx.getElements(newLine, Text.class).get(0).setValue(nom);
            table.getContent().add(newLine);
        }

    }

    public String getTexte()  {
        final List<P> stream = getElements(this.wordMLPackage, P.class);
        return getElements(this.wordMLPackage.getMainDocumentPart(), P.class).stream()
                .map(Objects::toString)
                .collect(Collectors.joining(" "));
    }
}
