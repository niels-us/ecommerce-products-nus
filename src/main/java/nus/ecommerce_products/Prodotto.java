package nus.ecommerce_products;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prodotti")  // ← La tabella si chiama "prodotti"
public class Prodotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Il nome è obbligatorio")
    @NotBlank(message = "Il nome non può essere vuoto")
    @Size(min = 2, max = 100, message = "Il nome deve essere tra 2 e 100 caratteri")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 500, message = "La descrizione non può superare 500 caratteri")
    @Column(length = 500)
    private String descrizione;

    @NotNull(message = "Il prezzo è obbligatorio")
    @Min(value = 2, message = "La quantità può essere mayorene a 1")
    @Positive(message = "Il prezzo deve essere maggiore di zero")
    @DecimalMin(value = "0.01", message = "Il prezzo minimo è 0.01")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzo;

    @NotNull(message = "La quantità è obbligatoria")
    @Min(value = 0, message = "La quantità non può essere negativa")
    @Column(name = "quantita_disponibile", nullable = false)
    private Integer quantitaDisponibile;

    @Column(name = "data_creazione", updatable = false, nullable = false)
    private LocalDateTime dataCreazione;

    @Column(name = "data_aggiornamento")
    private LocalDateTime dataAggiornamento;

    // Costruttore vuoto (obbligatorio per JPA)
    public Prodotto() {}

    // Costruttore con parametri
    public Prodotto(String nome, String descrizione, BigDecimal prezzo, Integer quantitaDisponibile, String codiceProdotto) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantitaDisponibile = quantitaDisponibile;
    }

    @PrePersist //solo se ejecuta en el primer guardado
    protected void onCreate() { dataCreazione = LocalDateTime.now(); dataAggiornamento = LocalDateTime.now();}
    @PreUpdate //se ejecuta en cada actualización
    protected void onUpdate() {
        dataAggiornamento = LocalDateTime.now();
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

    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }
    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public Integer getQuantitaDisponibile() { return quantitaDisponibile; }
    public void setQuantitaDisponibile(Integer quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDateTime getDataAggiornamento() {
        return dataAggiornamento;
    }
    public void setDataAggiornamento(LocalDateTime dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
    }
}
