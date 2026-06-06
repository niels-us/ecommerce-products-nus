package nus.ecommerce_products;

public class ProdottoNonTrovatoException extends RuntimeException{
    // Costruttore con messaggio personalizzato
    public ProdottoNonTrovatoException(String message) {
        super(message);
    }

    // Costruttore che accetta l'ID
    public ProdottoNonTrovatoException(Long id) {
        super("Prodotto non trovato con id: " + id);
    }
}
