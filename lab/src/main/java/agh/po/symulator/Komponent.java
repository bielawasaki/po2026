package agh.po.symulator;

public abstract class Komponent {
    protected String nazwa;
    protected float cena;
    protected float waga;

    public Komponent(String nazwa, float cena, float waga) {
        this.nazwa = nazwa;
        this.cena = cena;
        this.waga = waga;
    }

    public String getNazwa() { return this.nazwa; }
    public float getCena() { return this.cena; }
    public float getWaga() { return this.waga; }
}
