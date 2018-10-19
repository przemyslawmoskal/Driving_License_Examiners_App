package com.ptmprojects.aplikacjaegzaminatora;

public class StringWithCorrespondingTag {
    private String stringPart;
    private int idPart;

    public StringWithCorrespondingTag(String stringPart, int idPart) {
        this.stringPart = stringPart;
        this.idPart = idPart;
    }

    public String getStringPart() {
        return stringPart;
    }

    public int getIdPart() {
        return idPart;
    }

    @Override
    public String toString() {
        return stringPart;
    }
}
