package nus.ecommerce_products;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class SimpleTest {
    @Autowired
    private ProdottoRepository repo;

    @Test
    void testRepository() {
        // Creare e salvare
        Prodotto p = new Prodotto();
        p.setNome("Test");
        p.setDescrizione("descrizione");
        p.setPrezzo(new BigDecimal("10.00"));
        p.setQuantitaDisponibile(5);
        repo.save(p);

        // Verificare
        System.out.println("ID: " + p.getId());
        System.out.println("Nome: " + p.getNome());
        System.out.println("descrizione: " + p.getDescrizione());
        System.out.println("Prezzo: " + p.getPrezzo());
        System.out.println("Totale prodotti: " + repo.count());
    }
}
