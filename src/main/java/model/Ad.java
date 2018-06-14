package model;

public class Ad {

    private String header;
    private String pubDate; // i'm to lazy x)
    private String text;

    public String getHeader() {

        return header;

    }

    public String getPubDate() {

        return pubDate;

    }

    public String getText() {

        return text;

    }

    public Ad(String header, String date, String text) {

        this.header = header;
        this.pubDate = date;
        this.text = text;

    }
}
