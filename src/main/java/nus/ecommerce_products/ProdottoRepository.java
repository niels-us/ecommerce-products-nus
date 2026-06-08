package nus.ecommerce_products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    // 1. Cerca prodotti per nome (case-insensitive)
    List<Prodotto> findByNomeContainingIgnoreCase(String nome);

    // 2. Cerca prodotti con prezzo minore di
    List<Prodotto> findByPrezzoLessThan(BigDecimal prezzo);

    // 3. Cerca prodotti con prezzo tra due valori
    List<Prodotto> findByPrezzoBetween(BigDecimal min, BigDecimal max);

    // 4. Cerca prodotti disponibili (quantità > 0)
    List<Prodotto> findByQuantitaDisponibileGreaterThan(Integer quantita);

    // 5. Query personalizzata con JPQL
    @Query("SELECT p FROM Prodotto p WHERE p.nome LIKE %:keyword% OR p.descrizione LIKE %:keyword%")
    List<Prodotto> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Prodotto p WHERE p.quantitaDisponibile= :zero")
    List<Prodotto> findQuantitaDisponibileCero(@Param("zero") int zero);

    //List<Prodotto> getAllProdottiSort(String sort);

    // 6. Contare prodotti con prezzo maggiore di
    long countByPrezzoGreaterThan(BigDecimal prezzo);

    // ✅ Metodo con paginazione
    Page<Prodotto> findAll(Pageable pageable);
}
