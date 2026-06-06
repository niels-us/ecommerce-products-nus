package nus.ecommerce_products;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
}
