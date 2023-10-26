package fr.paloit.paloformation.docusignAPI;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class DocuSignConfig {

    @Autowired
    ResourceLoader resourceLoader;

    private String clientId;
    private String userId;
    private String rsaKeyFile;

    public DocuSignConfig() {
    }

    public DocuSignConfig(String clientId, String userId, String rsaKeyFile) {
        this.clientId = clientId;
        this.userId = userId;
        this.rsaKeyFile = rsaKeyFile;
    }

    @PostConstruct
    public void initConfig() throws IOException {
        Properties prop = new Properties();
        final File configFile = resourceLoader.getResource("classpath:docusign.properties").getFile();
        FileInputStream fis = new FileInputStream(configFile);
        prop.load(fis);

        this.clientId = prop.getProperty("docusign.clientId");
        this.userId = prop.getProperty("docusign.userId");
        this.rsaKeyFile = prop.getProperty("docusign.rsaKeyFile");
    }

    public String getClientId() {
        return clientId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRsaKeyFile() {
        return rsaKeyFile;
    }

}
