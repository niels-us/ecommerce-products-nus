package nus.ecommerce_products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProdottoServiceTestRunner implements CommandLineRunner {
    @Autowired
    private ProdottoService prodottoService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n========== TEST PRODOTTO SERVICE ==========\n");

        // ===== TEST CREATE =====
        System.out.println("--- TEST CREATE ---");

        // Test 1: Creazione prodotto valido
        try {
            Prodotto p1 = new Prodotto();
            p1.setNome("Prodotto Valido");
            p1.setDescrizione("Descrizione test");
            p1.setPrezzo(new BigDecimal("99.99"));
            p1.setQuantitaDisponibile(10);

            Prodotto saved = prodottoService.create(p1);
            System.out.println("✅ Prodotto valido creato con ID: " + saved.getId());
            System.out.println("   Nome: " + saved.getNome());
            System.out.println("   Prezzo: " + saved.getPrezzo());
        } catch (Exception e) {
            System.out.println("❌ Errore: " + e.getMessage());
        }

        // Test 2: Creazione con nome null (deve fallire)
        try {
            Prodotto p2 = new Prodotto();
            p2.setNome(null);
            p2.setPrezzo(new BigDecimal("99.99"));
            p2.setQuantitaDisponibile(10);

            prodottoService.create(p2);
            System.out.println("❌ ERRORE: Avrebbe dovuto fallire per nome null");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ CORRETTO: Nome null rifiutato - " + e.getMessage());
        }

        // Test 3: Creazione con prezzo zero (deve fallire)
        try {
            Prodotto p3 = new Prodotto();
            p3.setNome("Prodotto Prezzo Zero");
            p3.setPrezzo(BigDecimal.ZERO);
            p3.setQuantitaDisponibile(10);

            prodottoService.create(p3);
            System.out.println("❌ ERRORE: Avrebbe dovuto fallire per prezzo zero");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ CORRETTO: Prezzo zero rifiutato - " + e.getMessage());
        }

        // Test 4: Creazione con quantità negativa (deve fallire)
        try {
            Prodotto p4 = new Prodotto();
            p4.setNome("Prodotto Quantità Negativa");
            p4.setPrezzo(new BigDecimal("99.99"));
            p4.setQuantitaDisponibile(-5);

            prodottoService.create(p4);
            System.out.println("❌ ERRORE: Avrebbe dovuto fallire per quantità negativa");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ CORRETTO: Quantità negativa rifiutata - " + e.getMessage());
        }

        // ===== TEST FIND =====
        System.out.println("\n--- TEST FIND ---");

        // Test 5: Find all
        System.out.println("📋 Lista prodotti:");
        prodottoService.findAll().forEach(p ->
                System.out.println("   ID: " + p.getId() + " - " + p.getNome() + " (€" + p.getPrezzo() + ")")
        );

        // Test 6: Find by id (esistente)
        try {
            Prodotto found = prodottoService.findById(27L);
            System.out.println("✅ Prodotto trovato con ID 27: " + found.getNome());
        } catch (RuntimeException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // Test 7: Find by id (non esistente)
        try {
            prodottoService.findById(999L);
            System.out.println("❌ ERRORE: Avrebbe dovuto fallire per ID inesistente");
        } catch (RuntimeException e) {
            System.out.println("✅ CORRETTO: ID 999 non trovato - " + e.getMessage());
        }

        // ===== TEST UPDATE =====
        System.out.println("\n--- TEST UPDATE ---");

        // Test 8: Update con prezzo valido
        try {
            Prodotto updateData = new Prodotto();
            updateData.setPrezzo(new BigDecimal("149.99"));
            updateData.setNome("Prodotto Aggiornato");
            updateData.setDescrizione("Descripcione Aggiornato");

            Prodotto updated = prodottoService.update(27L, updateData);
            System.out.println("✅ Prodotto aggiornato: " + updated.getNome() + " - €" + updated.getPrezzo());
        } catch (Exception e) {
            System.out.println("❌ Errore: " + e.getMessage());
        }

        // Test 9: Update con prezzo negativo (deve fallire)
        try {
            Prodotto updateData = new Prodotto();
            updateData.setPrezzo(new BigDecimal("-50.00"));
            updateData.setNome("Prodotto Aggiornato");
            updateData.setDescrizione("Descripcione Aggiornato");

            prodottoService.update(27L, updateData);
            System.out.println("❌ ERRORE: Avrebbe dovuto fallire per prezzo negativo");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ CORRETTO: Prezzo negativo rifiutato - " + e.getMessage());
        }

        // Test 10: Update con quantità negativa (deve fallire)
        try {
            Prodotto updateData = new Prodotto();
            updateData.setQuantitaDisponibile(-10);
            updateData.setPrezzo(new BigDecimal("149.99"));
            updateData.setNome("Prodotto Aggiornato");
            updateData.setDescrizione("Descripcione Aggiornato");

            prodottoService.update(27L, updateData);
            System.out.println("❌ ERRORE: Avrebbe dovuto fallire per quantità negativa");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ CORRETTO: Quantità negativa rifiutata - " + e.getMessage());
        }

        // ===== TEST DELETE =====
        System.out.println("\n--- TEST DELETE ---");

        // Test 11: Delete prodotto esistente
        try {
            prodottoService.delete(27L);
            System.out.println("✅ Prodotto con ID 27 eliminato");
        } catch (Exception e) {
            System.out.println("❌ Errore: " + e.getMessage());
        }

        // Test 12: Verifica che sia stato eliminato
        System.out.println("\n📋 Lista finale prodotti:");
        prodottoService.findAll().forEach(p ->
                System.out.println("   ID: " + p.getId() + " - " + p.getNome())
        );

        System.out.println("\n========== FINE TEST ==========");
    }
}
