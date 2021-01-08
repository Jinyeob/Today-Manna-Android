package com.manna.parsing2.Model;

/***
 * Create by Jinyeob
 */
public class Mccheyne {

    private String title;
    private String point;
    private String text;

    public Mccheyne(String title, String point, String text) {
        this.title = title;
        this.point = point;
        this.text = text;
    }

    public Mccheyne() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Mccheyne [title=" + title + ", point=" + point + ", text=" + text + "]";
    }

}
