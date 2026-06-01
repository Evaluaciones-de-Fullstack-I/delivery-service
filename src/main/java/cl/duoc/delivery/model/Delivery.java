package cl.duoc.delivery.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "delivery")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Delivery {

  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombreRepartidor;

    @Column(nullable = false, length = 200)
    private String direccionEntrega;

    @Column(nullable = false)
    private LocalDate fechaDespacho;

    @Column(nullable = false)
    private LocalDate fechaEntrega;

    @Column(nullable = false, length = 50)
    private Integer pedidoId;
    @Column(nullable = false, length = 30)
    private String estadoEntrega;
}


