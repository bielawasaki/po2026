package agh.po.symulator;

public class SkrzyniaBiegow extends Komponent {
    protected String typ;
    protected int liczbaBiegow;
    protected int aktualnyBieg;
    protected float aktualnePrzelozenie;
    protected Sprzeglo sprzeglo;

    public SkrzyniaBiegow(String nazwa, float cena, float waga, String typ, int liczbaBiegow, Sprzeglo sprzeglo) {
        super(nazwa, cena, waga);
        this.typ = typ;
        this.liczbaBiegow = liczbaBiegow;
        this.sprzeglo = sprzeglo;
    }

    public String getTyp() { return this.typ; }
    public int getLiczbaBiegow() { return this.liczbaBiegow; }
    public Sprzeglo getSprzeglo() { return this.sprzeglo; }
    public int getAktualnyBieg() { return this.aktualnyBieg; }
    public float getAktualnePrzelozenie() { return this.aktualnePrzelozenie; }
}
