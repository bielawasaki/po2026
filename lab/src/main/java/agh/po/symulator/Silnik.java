package agh.po.symulator;

public class Silnik extends Komponent {
    protected int moc;
    protected int cylindry;
    protected float pojemnosc;
    protected int maxObroty;
    protected int obroty;

    public Silnik(String nazwa, float cena, float waga, int moc, int cylindry, float pojemnosc, int maxObroty) {
        super(nazwa, cena, waga);
        this.moc = moc;
        this.cylindry = cylindry;
        this.pojemnosc = pojemnosc;
        this.maxObroty = maxObroty;
    }

    public int getMoc() { return this.moc; }
    public int getCylindry() { return this.cylindry; }
    public float getPojemnosc() { return this.pojemnosc; }
    public float getMaxObroty() { return this.pojemnosc; }
    public int getObroty() { return this.obroty; }
}
