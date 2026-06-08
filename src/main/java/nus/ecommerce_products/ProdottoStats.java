package nus.ecommerce_products;

public class ProdottoStats {
    private long totalProdotti;
    private long prodottiEsauriti;
    private double prezzoMedio;

    // Costruttore
    public ProdottoStats(long totalProdotti, long prodottiEsauriti, double prezzoMedio) {
        this.totalProdotti = totalProdotti;
        this.prodottiEsauriti = prodottiEsauriti;
        this.prezzoMedio = Math.round(prezzoMedio * 100.0) / 100.0;  // Arrotonda a 2 decimali
    }

    // Getter
    public long getTotalProdotti() {
        return totalProdotti;
    }

    public long getProdottiEsauriti() {
        return prodottiEsauriti;
    }

    public double getPrezzoMedio() {
        return prezzoMedio;
    }
}
