package nus.ecommerce_products;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdottoService {
    // GET - Ottieni tutti i prodotti
    List<Prodotto> findAll();

    // GET - Ottieni un prodotto per ID
    Prodotto findById(Long id);

    List<Prodotto> findByPrezzoLessThan(BigDecimal prezzo);

    List<Prodotto> findByNomeContainingIgnoreCase(String keyword);

    // POST - Crea un nuovo prodotto
    Prodotto create(Prodotto prodotto);

    // PUT - Aggiorna un prodotto esistente
    Prodotto update(Long id, Prodotto prodotto);
    List<Prodotto> updateEsauriti();
    List<Prodotto> findQuantitaDisponibileCero();

    // DELETE - Elimina un prodotto
    void delete(Long id);

}
