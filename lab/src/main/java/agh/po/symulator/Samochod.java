package agh.po.symulator;

public class Samochod {
    protected String nrRejestracyjny;
    protected String model;
    protected int predkoscMax;

    protected Position position;
    protected boolean stanWylaczenia;

    protected SkrzyniaBiegow skrzyniaBiegow;
    protected Silnik silnik;

    public Samochod(String model, String nrRejestracyjny, int predkoscMax, SkrzyniaBiegow skrzyniaBiegow, Silnik silnik) {
        this.model = model;
        this.nrRejestracyjny = nrRejestracyjny;
        this.predkoscMax = predkoscMax;

        this.skrzyniaBiegow = skrzyniaBiegow;
        this.silnik = silnik;

        this.position = new Position(0, 0);
    }

    public String getNrRejestracyjny() { return this.nrRejestracyjny; }
    public String getModel() { return this.model; }
    public int getPredkoscMax() { return this.predkoscMax; }

    public Position getPosition() { return this.position; }
    public boolean getStanWylaczenia() { return this.stanWylaczenia; }

    public SkrzyniaBiegow getSkrzyniaBiegow() { return this.skrzyniaBiegow; }
    public Silnik getSilnik() { return this.silnik; }

    public static void main(String[] args) {
        Silnik silnik1 = new Silnik("BMW mocarny silnik", 5000, 150, 300, 4, 3, 7000);
        Sprzeglo sprzeglo1 = new Sprzeglo("Potezne sprzeglo", 700, 10);
        SkrzyniaBiegow skrzyniaBiegow1 = new SkrzyniaBiegow("ZF", 10000, 50, "automatyczna", 8, sprzeglo1);

        Samochod mercedes = new Samochod("Mercedes", "KR 12345", 300, skrzyniaBiegow1, silnik1);
        printInfo(mercedes);
    }

    public static void printInfo(Samochod samochod) {
        System.out.println("------SAMOCHOD------");
        System.out.println("Model: " + samochod.getModel());
        System.out.println("Nr rejestracyjny: " + samochod.getNrRejestracyjny());
        System.out.println("Prędkość max: " + samochod.getPredkoscMax());
        System.out.println("------SILNIK------");
        Silnik silnik = samochod.getSilnik();
        System.out.println("Nazwa: " + silnik.getNazwa());
        System.out.println("Cena: " + silnik.getCena() + "zł");
        System.out.println("Waga: " + silnik.getWaga() + "kg");
        System.out.println("Moc: " + silnik.getMoc() + "hp");
        System.out.println("Cylindry: " + silnik.getCylindry());
        System.out.println("Pojemność: " + silnik.getCylindry() + "l");
        System.out.println("Max obroty: " + silnik.getMaxObroty() + "RPM");
        System.out.println("------SKRZYNIA BIEGOW------");
        SkrzyniaBiegow skrzyniaBiegow = samochod.getSkrzyniaBiegow();
        System.out.println("Nazwa: " + skrzyniaBiegow.getNazwa());
        System.out.println("Cena: " + skrzyniaBiegow.getCena() + "zł");
        System.out.println("Waga: " + skrzyniaBiegow.getWaga() + "kg");
        System.out.println("Typ: " + skrzyniaBiegow.getTyp());
        System.out.println("Liczba biegow: " + skrzyniaBiegow.getLiczbaBiegow());
        System.out.println("------SPRZEGLO------");
        Sprzeglo sprzeglo = skrzyniaBiegow.getSprzeglo();
        System.out.println("Nazwa: " + sprzeglo.getNazwa());
        System.out.println("Cena: " + sprzeglo.getCena() + "zł");
        System.out.println("Waga: " + sprzeglo.getWaga() + "kg");
    }
}
