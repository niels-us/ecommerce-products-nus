package nus.ecommerce_products;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {
    private static final Logger log = LoggerFactory.getLogger(ProdottoController.class);

    @Autowired
    private ProdottoService prodottoService;

    // GET all - Ottieni tutti i prodotti
    @GetMapping
    public ResponseEntity<List<Prodotto>> getAllProdotti() {
        log.info("📋 GET /api/prodotti - Richiesta di tutti i prodotti");
        List<Prodotto> prodotti = prodottoService.findAll();
        return ResponseEntity.ok(prodotti);
    }

    // GET by id - Ottieni prodotto per ID
    @GetMapping("/{id}")
    public ResponseEntity<Prodotto> getProdottoById(@PathVariable Long id) {
        log.info("🔍 GET /api/prodotti/{} - Richiesta prodotto specifico", id);
        Prodotto prodotto = prodottoService.findById(id);
        return ResponseEntity.ok(prodotto);
    }

    // Get by - prodotti con prezzo minore di
    @GetMapping("/prezzo-minore-di/{prezzo}")
    public ResponseEntity<List<Prodotto>> getProdottiByPrezzoLessThan(@PathVariable BigDecimal prezzo) {
        log.info("💰 GET /api/prodotti/prezzo-minore-di/{} - Ricerca per prezzo", prezzo);
        List<Prodotto> prodotti = prodottoService.findByPrezzoLessThan(prezzo);
        return ResponseEntity.ok(prodotti);
    }

    // Get by -  Ricerca testuale
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Prodotto>> searchByKeyword(@PathVariable String keyword) {
        log.info("🔎 GET /api/prodotti/search/{} - Ricerca testuale", keyword);
        List<Prodotto> prodotti = prodottoService.findByNomeContainingIgnoreCase(keyword);
        return ResponseEntity.ok(prodotti);
    }

    // Get by -  Ricerca testuale
    @GetMapping("/search/findQuantitaDisponibileCero")
    public ResponseEntity<List<Prodotto>> searchByKeyword() {
        log.info("🔎 GET /api/prodotti/search/findQuantitaDisponibileCero - Ricerca findQuantitaDisponibileCero");
        List<Prodotto> prodotti = prodottoService.findQuantitaDisponibileCero();
        return ResponseEntity.ok(prodotti);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Prodotto>> getAllProdottiSort(
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sort) {
        //Sort sort = Sort.by(sortParam).ascending();

        log.info("📋 GET /api/prodotti?sort={}", sort);
        List<Prodotto> prodotti = prodottoService.getAllProdottiSort(sort);
        return ResponseEntity.ok(prodotti);
    }

    // Endpoint con paginazione
    @GetMapping("/paginati")
    public ResponseEntity<Page<Prodotto>> getProdottiPaginati(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        log.info("📋 GET /api/prodotti/paginati?page={}&size={}&sortBy={}", page, size, sortBy);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Prodotto> prodotti = prodottoService.findAllWithPagination(pageable);

        return ResponseEntity.ok(prodotti);
    }

    // ✅ PATCH - Aggiornamento parziale
    @PatchMapping("/{id}")
    public ResponseEntity<Prodotto> patchUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        log.info("🔧 PATCH /api/prodotti/{} - Aggiornamento parziale", id);
        Prodotto updated = prodottoService.patchUpdate(id, updates);
        return ResponseEntity.ok(updated);
    }

    // POST create - Crea un nuovo prodotto
    @PostMapping
    public ResponseEntity<Prodotto> createProdotto(@Valid @RequestBody Prodotto prodotto) {
        log.info("📝 POST /api/prodotti - Creazione nuovo prodotto: {}", prodotto.getNome());
        Prodotto saved = prodottoService.create(prodotto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // PUT update - Aggiorna un prodotto esistente
    @PutMapping("/{id}")
    public ResponseEntity<Prodotto> updateProdotto(@PathVariable Long id, @Valid @RequestBody Prodotto prodotto) {
        log.info("✏️ PUT /api/prodotti/{} - Aggiornamento prodotto", id);
        Prodotto updated = prodottoService.update(id, prodotto);
        return ResponseEntity.ok(updated);
    }

    // PUT update - Aggiorna un prodotto esistente a quantida cero
    @PutMapping("/esauriti")
    public ResponseEntity<List<Prodotto>> updateEsauritiProdotto() {
        log.info("✏️ PUT /api/prodotti/{} - esauriti prodotto");
        List<Prodotto> updateEsauriti = prodottoService.updateEsauriti();
        return ResponseEntity.ok(updateEsauriti);
    }

    // DELETE - Elimina un prodotto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProdotto(@PathVariable Long id) {
        log.info("🗑️ DELETE /api/prodotti/{} - Eliminazione prodotto", id);
        prodottoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Endpoint per decrementare la quantità dopo una vendita
    @PostMapping("/{id}/vendita")
    public ResponseEntity<Prodotto> vendita(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {

        int quantitaVenduta = request.get("quantita");
        log.info("🏷️ POST /api/prodotti/{}/vendita - Quantità: {}", id, quantitaVenduta);

        Prodotto prodotto = prodottoService.decrementaQuantita(id, quantitaVenduta);
        return ResponseEntity.ok(prodotto);
    }

    // ✅ NUOVO ENDPOINT PER STATISTICHE
    @GetMapping("/statistiche")
    public ResponseEntity<ProdottoStats> getStatistics() {
        log.info("📊 GET /api/prodotti/statistiche - Richiesta statistiche");
        ProdottoStats stats = prodottoService.getStatistics();
        return ResponseEntity.ok(stats);
    }
}
