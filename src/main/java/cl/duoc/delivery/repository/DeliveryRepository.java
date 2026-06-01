package cl.duoc.delivery.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.delivery.model.Delivery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

 
List<Delivery> findByNombreRepartidor(String nombreRepartidor);
List<Delivery> findByPedidoId(Integer pedidoId);
List<Delivery> findByEstadoEntrega(String estadoEntrega);
    @Query(
        value = "SELECT * FROM delivery WHERE pedido_id = :pedidoId",
        nativeQuery = true
    )
    List<Delivery> buscarPorPedido(
            @Param("pedidoId") Integer pedidoId
    );


    @Query(
    value = "SELECT * FROM delivery WHERE estado_entrega = :estadoEntrega",
    nativeQuery = true
    )
    List<Delivery> buscarPorEstado(
        @Param("estadoEntrega") String estadoEntrega
    );
}
