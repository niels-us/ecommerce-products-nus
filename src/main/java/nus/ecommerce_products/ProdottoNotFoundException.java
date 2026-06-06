package nus.ecommerce_products;

public class ProdottoNotFoundException extends RuntimeException {

    public ProdottoNotFoundException(String message) {
        super(message);
    }

    public ProdottoNotFoundException(Long id) {
        super("Prodotto non trovato con id: " + id);
    }
}
