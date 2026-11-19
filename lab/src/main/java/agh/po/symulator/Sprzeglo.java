package agh.po.symulator;

public class Sprzeglo extends Komponent {
    protected boolean stanSprzegla;

    public Sprzeglo(String nazwa, float cena, float waga) {
        super(nazwa, cena, waga);
    }

    public boolean getStanSprzegla() { return this.stanSprzegla; }
}
