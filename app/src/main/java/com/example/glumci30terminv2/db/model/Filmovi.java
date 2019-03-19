package com.example.glumci30terminv2.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Filmovi.TABLE_FILMOVI)
public class Filmovi {

    public static final String TABLE_FILMOVI = "filmovi";
    private static final String FIELD_ID = "id";
    private static final String FIELD_imeFILMA = "imeFilma";
    private static final String FIELD_ZANR = "zanr";
    private static final String FIELD_godinaIZLASKA = "godinaIzlaskaFilma";
    private static final String FIELD_GLUMAC = "glumac";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_imeFILMA)
    private String imeFilma;

    @DatabaseField(columnName = FIELD_ZANR)
    private String zanr;

    @DatabaseField(columnName = FIELD_godinaIZLASKA)
    private String godinaIzlaska;

    @DatabaseField(columnName = FIELD_GLUMAC, foreign = true, foreignAutoRefresh = true)
    private Glumac glumac;

    public Filmovi() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImeFilma() {
        return imeFilma;
    }

    public void setImeFilma(String imeFilma) {
        this.imeFilma = imeFilma;
    }

    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    public String getGodinaIzlaska() {
        return godinaIzlaska;
    }

    public void setGodinaIzlaska(String godinaIzlaska) {
        this.godinaIzlaska = godinaIzlaska;
    }

    public Glumac getGlumac() {
        return glumac;
    }

    public void setGlumac(Glumac glumac) {
        this.glumac = glumac;
    }

    public String toString() {
        return imeFilma;
    }
}
