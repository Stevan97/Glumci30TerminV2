package com.example.glumci30terminv2.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Glumac.TABLE_GLUMAC)
public class Glumac {

    public static final String TABLE_GLUMAC = "glumac";
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAZIV = "naziv";
    private static final String FIELD_BIOGRAFIJA = "biografija";
    private static final String FIELD_OCENA_GLUMCA = "ocenaGlumca";
    private static final String FIELD_RODJENJE = "datumRodjenja";
    private static final String FIELD_FILMOVI = "filmovi";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAZIV)
    private String naziv;

    @DatabaseField(columnName = FIELD_BIOGRAFIJA)
    private String biografija;

    @DatabaseField(columnName = FIELD_OCENA_GLUMCA)
    private float ocenaGlumca;

    @DatabaseField(columnName = FIELD_RODJENJE)
    private String datumRodjenja;

    @ForeignCollectionField(columnName = FIELD_FILMOVI, eager = true)
    ForeignCollection<Filmovi> filmovi;

    public Glumac() {
        //empty constr
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getBiografija() {
        return biografija;
    }

    public void setBiografija(String biografija) {
        this.biografija = biografija;
    }

    public float getOcenaGlumca() {
        return ocenaGlumca;
    }

    public void setOcenaGlumca(float ocenaGlumca) {
        this.ocenaGlumca = ocenaGlumca;
    }

    public String getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(String datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public ForeignCollection<Filmovi> getFilmovi() {
        return filmovi;
    }

    public void setFilmovi(ForeignCollection<Filmovi> filmovi) {
        this.filmovi = filmovi;
    }

    public String toString() {
        return naziv;
    }

}
