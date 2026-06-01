package cl.duoc.delivery.service;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import cl.duoc.delivery.model.Delivery;
import cl.duoc.delivery.repository.DeliveryRepository;
import cl.duoc.delivery.dto.UpdateRequestDelivery;
import cl.duoc.delivery.mapper.DeliveryMapper;
import cl.duoc.delivery.exception.ResourceNotFoundException;
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final WebClient webClient;

    public DeliveryService(
            DeliveryRepository deliveryRepository,
            WebClient webClient
    ) {
        this.deliveryRepository = deliveryRepository;
        this.webClient = webClient;
    }
//listar despachos
    public List<Delivery> getDeliverys() {
        return deliveryRepository.findAll();
    }
//crear despacho
    public Delivery saveDelivery(Delivery delivery) {

        // Estado inicial del despacho
        delivery.setEstadoEntrega("PENDIENTE");

        return deliveryRepository.save(delivery);
    }
 // BUSCAR POR ID
    public Delivery getDeliveryById(int id) {
        return deliveryRepository.findById(id)
                .orElse(null);
    }

    // ACTUALIZAR DESPACHO
    public Delivery updateDelivery(
            int id,
            UpdateRequestDelivery request
    ) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Despacho no encontrado"));

        DeliveryMapper.updateDelivery(
                delivery,

  request
        );

        return deliveryRepository.save(delivery);

    }
     // ELIMINAR
    public boolean deleteDelivery(int id) {

        Optional<Delivery> deliveryOpt =
                deliveryRepository.findById(id);

        if (deliveryOpt.isPresent()) {

            deliveryRepository.delete(deliveryOpt.get());
            return true;

        } else {

            throw new ResourceNotFoundException(
                    "Despacho con id=" + id + " no encontrado"
            );
        }
    }

    // CAMBIAR ESTADO DE ENTREGA
    public void cambiarEstadoEntrega(
            int id,
            String estadoEntrega
    ) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Despacho no encontrado"
                        ));

        delivery.setEstadoEntrega(estadoEntrega);

        deliveryRepository.save(delivery);
    }

    // BUSCAR POR PEDIDO
    public List<Delivery> buscarPorPedido(
            Integer pedidoId
    ) {

        return deliveryRepository.findByPedidoId(
                pedidoId
        );
    }
}


