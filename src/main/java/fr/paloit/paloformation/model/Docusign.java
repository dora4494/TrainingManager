package fr.paloit.paloformation.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Docusign {

    public String signerEmail;
    public String signerName;

    public Docusign(String signerEmail, String signerName) {

    }


    private static Docusign getAskUserInformation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the JWT Code example! ");
        System.out.print("Enter the signer's email address: ");
        String signerEmail = scanner.nextLine();
        System.out.print("Enter the signer's name: ");
        String signerName = scanner.nextLine();
        System.out.print("Enter the carbon copy's email address: ");
        String ccEmail = scanner.nextLine();
        System.out.print("Enter the carbon copy's name: ");
        String ccName = scanner.nextLine();
        Docusign result = new Docusign(signerEmail, signerName);
        return result;
    }
/*
    private static Properties getAppConfig() throws IOException {
        // Get information fro app.config
        Properties prop = new Properties();
        String fileName = "app.config";
        FileInputStream fis = new FileInputStream(fileName);
        prop.load(fis);
        return prop;
    }

*/


}
