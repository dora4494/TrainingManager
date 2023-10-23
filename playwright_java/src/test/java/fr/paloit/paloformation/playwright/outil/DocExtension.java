package fr.paloit.paloformation.playwright.outil;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import fr.paloit.paloformation.playwright.page.FormationPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DocExtension implements BeforeEachCallback, AfterEachCallback {
    private final Path docs = Paths.get("build", "docs");

    private String testName;

    public StringBuffer buffer = new StringBuffer();

    private int indexScreenshot = 0;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        indexScreenshot = 0;
        testName = context.getTestMethod().map(m -> m.getDeclaringClass().getSimpleName() + "." + m.getName()).get();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        context.getTestMethod();
        writeDocFile();
    }

    public void writeDocFile() {
        final String filename = docs.resolve(testName + ".adoc").toString();
        try (OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8)) {
            buffer.append("\n" + style());
            fileWriter.write(buffer.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buffer = new StringBuffer();
    }

    public String getTestName() {
        return testName;
    }

    public Path getPath() {
        return docs;
    }

    private String style() {
        return "\n" + String.join("\n",
                "++++",
                "<style>",
                "  img {",
                "    border: grey solid 1px;",
                "    box-shadow: 5px 5px 5px grey;",
                "    margin: 1em;",
                "    margin-left: 5em;",
                "  }",
                "</style>",
                "++++"
        );
    }

    public String takeNextScreenshot(Page page) {
        indexScreenshot++;
        final String imageFilename = getTestName() + "_" + indexScreenshot + ".jpg";
        final Path imagePath = getPath().resolve(imageFilename);
        page.screenshot(new Page.ScreenshotOptions().setPath(imagePath));
        return imageFilename;
    }

    public void write(String... texts) {
        buffer.append(Arrays.stream(texts).collect(Collectors.joining("\n")));
    }

    public void writeln(String... texts) {
        write(texts);
        buffer.append("\n");
    }
}
