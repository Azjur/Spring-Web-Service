package Taavi.Lond;

public class DataRow {
    private final int Koht;
    private final String Rahvus;
    private final String Klubi;
    private final String Nimi;
    private final int Punkte;

    // Constructors
    public DataRow(int Koht, String Rahvus, String Klubi, String Nimi, int Punkte) {
        this.Koht = Koht;
        this.Rahvus = Rahvus;
        this.Klubi = Klubi;
        this.Nimi = Nimi;
        this.Punkte = Punkte;
    }

    // Getters
    public int getKoht() {
        return Koht;
    }

    public String getRahvus() {
        return Rahvus;
    }

    public String getKlubi() {
        return Klubi;
    }

    public String getNimi() {
        return Nimi;
    }

    public int getPunkte() {
        return Punkte;
    }

    // toString for debugging
    @Override
    public String toString() {
        return "DataRow{" +
                "Koht=" + Koht +
                ", Rahvus='" + Rahvus + '\'' +
                ", Klubi='" + Klubi + '\'' +
                ", Nimi='" + Nimi + '\'' +
                ", Punkte=" + Punkte +
                '}';
    }
}
