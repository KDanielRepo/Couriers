package com.pkisi.dkuc.couriers;

public class Package {
    private int id;
    private String nr_paczki;
    private float koszt;
    private String miasto;
    private String ulica;
    private String nr_domu;
    private String nr_lokalu;

    public Package(){}

    public Package(int id, String nr_paczki, float koszt, String miasto, String ulica, String nr_domu, String nr_lokalu) {
        this.id = id;
        this.nr_paczki = nr_paczki;
        this.koszt = koszt;
        this.miasto = miasto;
        this.ulica = ulica;
        this.nr_domu = nr_domu;
        this.nr_lokalu = nr_lokalu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNr_paczki() {
        return nr_paczki;
    }

    public void setNr_paczki(String nr_paczki) {
        this.nr_paczki = nr_paczki;
    }

    public float getKoszt() {
        return koszt;
    }

    public void setKoszt(float koszt) {
        this.koszt = koszt;
    }

    public String getMiasto() {
        return miasto;
    }

    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getNr_domu() {
        return nr_domu;
    }

    public void setNr_domu(String nr_domu) {
        this.nr_domu = nr_domu;
    }

    public String getNr_lokalu() {
        return nr_lokalu;
    }

    public void setNr_lokalu(String nr_lokalu) {
        this.nr_lokalu = nr_lokalu;
    }
}
