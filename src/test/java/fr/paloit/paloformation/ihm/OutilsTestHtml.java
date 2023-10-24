package fr.paloit.paloformation.ihm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

public class OutilsTestHtml {

    public static Document toHtmlDocument(ResultActions resultActions) throws UnsupportedEncodingException {
        return Jsoup.parse(resultActions.andReturn().getResponse().getContentAsString());
    }
}
