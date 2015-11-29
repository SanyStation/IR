package com.ak.ir.xmlParser;

import com.ak.ir.index.Zone;

/**
 * Created by sanystation on 29/11/15.
 */
public enum Fb2Rule {

    GENRE("//description/title-info/genre/text()", Zone.GENRE),
    AUTHOR_FIRST_NAME("//description/title-info/author/first-name/text()", Zone.AUTHOR),
    AUTHOR_LAST_NAME("//description/title-info/author/last-name/text()", Zone.AUTHOR),
    TITLE("//description/title-info/book-title/text()", Zone.TITLE),
    KEYWORDS("//description/title-info/keywords/text()", Zone.KEYWORDS),
    BODY("//body/section/p/text()", Zone.BODY),
    BODY_STRONG("//body/section/p/strong/text()", Zone.BODY);

    private String xpath;
    private Zone zone;

    Fb2Rule(String xpath, Zone zone) {
        this.xpath = xpath;
        this.zone = zone;
    }

    public String getXpath() {
        return xpath;
    }

    public Zone getZone() {
        return zone;
    }
}
