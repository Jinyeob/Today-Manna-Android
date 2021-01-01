package com.manna.parsing2.Model;
import java.io.Serializable;

public class MannaData implements Serializable {
    private String verse;
    private String content;

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
