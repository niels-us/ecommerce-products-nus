package nus.ecommerce_products;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


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
        for (int i=0; i < prodotti.size(); i++){
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





}
