package nus.ecommerce_products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProdottoService {
    // GET - Ottieni tutti i prodotti
    List<Prodotto> findAll();

    // GET - Ottieni un prodotto per ID
    Prodotto findById(Long id);

    List<Prodotto> findByPrezzoLessThan(BigDecimal prezzo);

    List<Prodotto> findByNomeContainingIgnoreCase(String keyword);

    List<Prodotto> getAllProdottiSort(String sort);

    // POST - Crea un nuovo prodotto
    Prodotto create(Prodotto prodotto);

    // PUT - Aggiorna un prodotto esistente
    Prodotto update(Long id, Prodotto prodotto);
    List<Prodotto> updateEsauriti();
    List<Prodotto> findQuantitaDisponibileCero();

    // DELETE - Elimina un prodotto
    void delete(Long id);

    // Paginazione
    Page<Prodotto> findAllWithPagination(Pageable pageable);
    // ✅ PATCH - Aggiornamento parziale
    Prodotto patchUpdate(Long id, Map<String, Object> updates);

    // ✅ Logica di business: decrementa quantità dopo una vendita
    Prodotto decrementaQuantita(Long id, int quantitaVenduta);

    ProdottoStats getStatistics();

}
