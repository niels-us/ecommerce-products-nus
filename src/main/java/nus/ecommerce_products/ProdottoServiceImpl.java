package nus.ecommerce_products;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class ProdottoServiceImpl implements ProdottoService { // implements = sigues una plantilla ProdottoService
    private static final Logger log = LoggerFactory.getLogger(ProdottoServiceImpl.class);

    @Autowired // ← Spring inietta automaticamente il ProdottoRepository
    private ProdottoRepository prodottoRepository; //← Questa è SOLO iniettata, usas la lógica ya escrita de ProdottoRepository

    @Override // ← Indica che stai implementando il metodo dell'interfaccia ProdottoService
    @Transactional(readOnly = true) // Ottimizza la transazione per sole operazioni di lettura (SELECT)
    public List<Prodotto> findAll() {
        return prodottoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Prodotto findById(Long id) {
        return prodottoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("❌ Prodotto non trovato con ID: {}", id);
                    return new ProdottoNonTrovatoException(id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prodotto> findByPrezzoLessThan(BigDecimal prezzo) {
        log.info("💰 Ricerca prodotti con prezzo minore di: {}", prezzo);
        return prodottoRepository.findByPrezzoLessThan(prezzo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prodotto> findByNomeContainingIgnoreCase(String keyword) {
        log.info("🔎 Ricerca prodotti con keyword: {}", keyword);
        return prodottoRepository.findByNomeContainingIgnoreCase(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prodotto> findQuantitaDisponibileCero() {
        log.info("🔎 Ricerca prodotti con keyword: {}");
        return prodottoRepository.findQuantitaDisponibileCero(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prodotto> getAllProdottiSort(String sort) {
        log.info("📋 Richiesta prodotti ordinati per: {}", sort);

        Sort sortOrder;

        switch (sort) {
            case "prezzo_asc":
                sortOrder = Sort.by("prezzo").ascending();
                break;
            case "nome_asc":
                sortOrder = Sort.by("nome").ascending();
                break;
            case "prezzo_desc":
                sortOrder = Sort.by("prezzo").descending();
                break;
            case "nome_desc":
                sortOrder = Sort.by("nome").descending();
                break;
            default:
                sortOrder = Sort.by("id").ascending();
                break;
        }

        return prodottoRepository.findAll(sortOrder);
    }

    @Override
    public Prodotto patchUpdate(Long id, Map<String, Object> updates) {
        log.info("🔧 PATCH aggiornamento parziale del prodotto con ID: {}", id);

        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new ProdottoNonTrovatoException(id));

        // Aggiorna solo i campi presenti nella mappa
        updates.forEach((key, value) -> {
            switch (key) {
                case "nome":
                    if (value != null && !value.toString().trim().isEmpty()) {
                        prodotto.setNome(value.toString());
                        log.info("   ✏️ Aggiornato nome: {}", value);
                    }
                    break;
                case "descrizione":
                    if (value != null) {
                        prodotto.setDescrizione(value.toString());
                        log.info("   ✏️ Aggiornata descrizione");
                    }
                    break;
                case "prezzo":
                    if (value != null) {
                        BigDecimal prezzo = new BigDecimal(value.toString());
                        if (prezzo.compareTo(BigDecimal.ZERO) > 0) {
                            prodotto.setPrezzo(prezzo);
                            log.info("   ✏️ Aggiornato prezzo: {}", prezzo);
                        } else {
                            throw new IllegalArgumentException("Il prezzo deve essere maggiore di zero");
                        }
                    }
                    break;
                case "quantitaDisponibile":
                    if (value != null) {
                        Integer quantita = Integer.valueOf(value.toString());
                        if (quantita >= 0) {
                            prodotto.setQuantitaDisponibile(quantita);
                            log.info("   ✏️ Aggiornata quantità: {}", quantita);
                        } else {
                            throw new IllegalArgumentException("La quantità non può essere negativa");
                        }
                    }
                    break;
                default:
                    log.warn("   ⚠️ Campo sconosciuto: {}", key);
            }
        });

        Prodotto saved = prodottoRepository.save(prodotto);
        log.info("✅ Prodotto con ID: {} aggiornato parzialmente", id);
        return saved;
    }

    @Override
    public Page<Prodotto> findAllWithPagination(Pageable pageable) {
        log.info("📋 Richiesta paginata - Page: {}, Size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return prodottoRepository.findAll(pageable);
    }


    @Override
    public Prodotto create(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
    }

    @Override
    public Prodotto update(Long id, Prodotto prodottoDetails) {
        Prodotto prodotto = findById(id);
        // Solo actualización (las validaciones ya ocurrieron en el Controller)
        if (prodottoDetails.getNome() != null) {
            prodotto.setNome(prodottoDetails.getNome());
        }
        if (prodottoDetails.getPrezzo() != null) {
            prodotto.setPrezzo(prodottoDetails.getPrezzo());
        }
        if (prodottoDetails.getQuantitaDisponibile() != null) {
            prodotto.setQuantitaDisponibile(prodottoDetails.getQuantitaDisponibile());
        }
        return prodottoRepository.save(prodotto);
    }

    public List<Prodotto> updateEsauriti() {
        List<Prodotto> prodotti = findAll();
        for (int i = 0; i < prodotti.size(); i++) {
            prodotti.get(i).setQuantitaDisponibile(0);
            update(prodotti.get(i).getId(), prodotti.get(i));
        }
        return findAll();
    }

    @Override
    public void delete(Long id) {
        // Verifica che il prodotto esista
        findById(id);
        prodottoRepository.deleteById(id);
    }

    // ✅ Metodo per decrementare la quantità dopo una vendita
    @Override
    public Prodotto decrementaQuantita(Long id, int quantitaVenduta) {
        log.info("🏷️ Vendita: decremento quantità prodotto ID: {} di {} unità", id, quantitaVenduta);

        // 1. Verifica che il prodotto esista
        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new ProdottoNonTrovatoException(id));

        // 2. Verifica che la quantità venduta sia valida
        if (quantitaVenduta <= 0) {
            log.warn("⚠️ Tentativo di vendita con quantità non positiva: {}", quantitaVenduta);
            throw new IllegalArgumentException("La quantità venduta deve essere maggiore di zero");
        }

        // 3. Verifica che ci sia abbastanza stock
        if (prodotto.getQuantitaDisponibile() < quantitaVenduta) {
            log.warn("⚠️ Stock insufficiente! Richiesto: {}, Disponibile: {}",
                    quantitaVenduta, prodotto.getQuantitaDisponibile());
            throw new IllegalArgumentException("Stock insufficiente. Disponibili: " +
                    prodotto.getQuantitaDisponibile());
        }

        // 4. Decrementa la quantità
        int nuovaQuantita = prodotto.getQuantitaDisponibile() - quantitaVenduta;
        prodotto.setQuantitaDisponibile(nuovaQuantita);

        // 5. Salva le modifiche
        Prodotto saved = prodottoRepository.save(prodotto);
        log.info("✅ Vendita completata. Nuova quantità disponibile: {}", nuovaQuantita);

        return saved;
    }

    // ✅ Metodo per getStatistics
    public ProdottoStats getStatistics() {
        List<Prodotto> prodotti = findAll();
        long totalProdotti = prodotti.size();
        long prodottiEsauriti = prodotti.stream()
                .filter(p -> p.getQuantitaDisponibile() == 0)
                .count();
        double prezzoMedio = prodotti.stream()
                .mapToDouble(p -> p.getPrezzo().doubleValue())
                .average()
                .orElse(0.0);
        log.info("📊 Statistiche calcolate - Totali: {}, Esauriti: {}, Prezzo medio: {:.2f}",
                totalProdotti, prodottiEsauriti, prezzoMedio);
        return new ProdottoStats(totalProdotti, prodottiEsauriti, prezzoMedio);
    }
}
