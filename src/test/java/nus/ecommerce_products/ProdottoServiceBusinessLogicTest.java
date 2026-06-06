package nus.ecommerce_products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProdottoServiceBusinessLogicTest {
    @Autowired
    private ProdottoService prodottoService;

    private Prodotto prodottoValido;

    @BeforeEach
    void setUp() {
        prodottoValido = new Prodotto();
        prodottoValido.setNome("Prodotto Test");
        prodottoValido.setDescrizione("Descrizione test");
        prodottoValido.setPrezzo(new BigDecimal("99.99"));
        prodottoValido.setQuantitaDisponibile(10);
    }

    // ===== TEST CREATE =====
    @Test
    void testCreateProdottoValido() {
        Prodotto saved = prodottoService.create(prodottoValido);

        assertNotNull(saved.getId());
        assertEquals("Prodotto Test", saved.getNome());
        assertEquals(new BigDecimal("99.99"), saved.getPrezzo());
    }

    @Test
    void testCreateConNomeNull() {
        prodottoValido.setNome(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prodottoService.create(prodottoValido);
        });

        assertEquals("Il nome del prodotto è obbligatorio", exception.getMessage());
    }

    @Test
    void testCreateConNomeVuoto() {
        prodottoValido.setNome("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prodottoService.create(prodottoValido);
        });

        assertEquals("Il nome del prodotto è obbligatorio", exception.getMessage());
    }

    @Test
    void testCreateConPrezzoNull() {
        prodottoValido.setPrezzo(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prodottoService.create(prodottoValido);
        });

        assertEquals("Il prezzo del prodotto è obbligatorio", exception.getMessage());
    }

    @Test
    void testCreateConPrezzoZero() {
        prodottoValido.setPrezzo(BigDecimal.ZERO);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prodottoService.create(prodottoValido);
        });

        assertEquals("Il prezzo deve essere maggiore di zero", exception.getMessage());
    }

    @Test
    void testCreateConPrezzoNegativo() {
        prodottoValido.setPrezzo(new BigDecimal("-10.00"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prodottoService.create(prodottoValido);
        });

        assertEquals("Il prezzo deve essere maggiore di zero", exception.getMessage());
    }

    @Test
    void testCreateConQuantitaNegativa() {
        prodottoValido.setQuantitaDisponibile(-5);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prodottoService.create(prodottoValido);
        });

        assertEquals("La quantità non può essere negativa", exception.getMessage());
    }

    // ===== TEST UPDATE =====

    @Test
    void testUpdateConPrezzoNegativo() {
        Prodotto saved = prodottoService.create(prodottoValido);

        Prodotto updateData = new Prodotto();
        updateData.setPrezzo(new BigDecimal("-50.00"));
        updateData.setNome(new String("Producto Nuovo"));
        updateData.setDescrizione(new String("Producto Descripcione"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prodottoService.update(saved.getId(), updateData);
        });

        assertEquals("Il prezzo deve essere maggiore di zero", exception.getMessage());
    }

    @Test
    void testUpdateConQuantitaNegativa() {
        Prodotto saved = prodottoService.create(prodottoValido);

        Prodotto updateData = new Prodotto();
        updateData.setQuantitaDisponibile(-10);
        updateData.setNome(new String("Producto Nuovo"));
        updateData.setDescrizione(new String("Producto Descripcione"));
        updateData.setPrezzo(new BigDecimal("99.99"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prodottoService.update(saved.getId(), updateData);
        });

        assertEquals("La quantità non può essere negativa", exception.getMessage());
    }

    @Test
    void testUpdateConPrezzoValido() {
        Prodotto saved = prodottoService.create(prodottoValido);

        Prodotto updateData = new Prodotto();
        updateData.setPrezzo(new BigDecimal("99.99"));
        updateData.setNome(new String("Producto Nuovo"));
        updateData.setDescrizione(new String("Producto Descripcione"));

        Prodotto updated = prodottoService.update(saved.getId(), updateData);

        assertEquals(new BigDecimal("99.99"), updated.getPrezzo());
    }
}
