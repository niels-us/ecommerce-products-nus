package nus.ecommerce_products;

import java.math.BigDecimal;

public class ProdottoDTO {
    private Long id;
    private String nome;
    private BigDecimal prezzo;
    private Integer quantitaDisponibile;

    // Costruttore vuoto (per Jackson)
    public ProdottoDTO() {
    }

    // Costruttore con parametri
    public ProdottoDTO(Long id, String nome, BigDecimal prezzo, Integer quantitaDisponibile) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantitaDisponibile = quantitaDisponibile;
    }

    // Costruttore che accetta un'entità Prodotto
    public ProdottoDTO(Prodotto prodotto) {
        this.id = prodotto.getId();
        this.nome = prodotto.getNome();
        this.prezzo = prodotto.getPrezzo();
        this.quantitaDisponibile = prodotto.getQuantitaDisponibile();
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public Integer getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(Integer quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }
}
