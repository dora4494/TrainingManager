package fr.paloit.paloformation.docusignAPI;

import fr.paloit.paloformation.service.EmargementService;

public class DocuSignFeuilleEmargement implements EmargementService.FeuilleEmargement {
    private String texteDocument;
    private String nomFichier;

    public String getTexteDocument() {
        return texteDocument;
    }

    public void setTexteDocument(String texteDocument) {
        this.texteDocument = texteDocument;
    }
    @Override
    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    @Override
    public byte[] getBytes() {
        return this.texteDocument.getBytes();
    }
}
