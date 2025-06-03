package pl.koncerty.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bilet")
public class Bilet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "uzytkownik_id")
    private Uzytkownik uzytkownik;

    @ManyToOne
    @JoinColumn(name = "koncert_id")
    private Koncert koncert;

    @Column(name = "data_zakupu")
    private LocalDateTime dataZakupu;

    @Column(name = "numer_biletu")
    private String numerBiletu;

    public Bilet(Uzytkownik uzytkownik, Koncert koncert, LocalDateTime dataZakupu) {
        this.uzytkownik = uzytkownik;
        this.koncert = koncert;
        this.dataZakupu = dataZakupu;
        this.numerBiletu = "B" + System.currentTimeMillis();
    }

    public Bilet() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumerBiletu() { return numerBiletu; }
    public void setNumerBiletu(String numerBiletu) { this.numerBiletu = numerBiletu; }

    public LocalDateTime getDataZakupu() { return dataZakupu; }
    public void setDataZakupu(LocalDateTime dataZakupu) { this.dataZakupu = dataZakupu; }

    public Koncert getKoncert() { return koncert; }
    public void setKoncert(Koncert koncert) { this.koncert = koncert; }

    public Uzytkownik getUzytkownik() { return uzytkownik; }
    public void setUzytkownik(Uzytkownik uzytkownik) { this.uzytkownik = uzytkownik; }
}