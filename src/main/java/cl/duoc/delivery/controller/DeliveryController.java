package cl.duoc.delivery.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import jakarta.validation.Valid;
import cl.duoc.delivery.dto.CreateRequestDelivery;
import cl.duoc.delivery.model.Delivery;
import cl.duoc.delivery.service.DeliveryService;
import cl.duoc.delivery.dto.UpdateRequestDelivery;
import cl.duoc.delivery.exception.ResourceNotFoundException;
import cl.duoc.delivery.mapper.DeliveryMapper;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
@RestController
@RequestMapping("/api/v1/delivery")

public class DeliveryController {

    private final DeliveryService deliveryService;
    private final WebClient webClient;

    public DeliveryController(
            DeliveryService deliveryService,
            WebClient webClient
    ) {
        this.deliveryService = deliveryService;
        this.webClient = webClient;
}
//endpoint basicos
//listar despachos
@GetMapping
public ResponseEntity<List<Delivery>> listarDespachos() {

    List<Delivery> despachos =
            deliveryService.getDeliverys();

    return ResponseEntity.ok(despachos);
}

//crear despacho post
@PostMapping
public ResponseEntity<Map<String, Object>> crearDespacho(
        @Valid @RequestBody CreateRequestDelivery request
) {

    Delivery nuevoDespacho =
            deliveryService.saveDelivery(
                    DeliveryMapper.toDelivery(request)
            );

    Map<String, Object> response = new HashMap<>();
    response.put("mensaje", "Despacho creado correctamente");
    response.put("id", nuevoDespacho.getId());

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
}

//buscar por id
@GetMapping("/{id}")
public ResponseEntity<Delivery> buscarDespacho(
        @PathVariable int id
) {

    Delivery delivery =
            deliveryService.getDeliveryById(id);

    if (delivery == null) {
        throw new ResourceNotFoundException(
                "Despacho con id=" + id + " no encontrado"
        );
    }

    return ResponseEntity.ok(delivery);
}
//actualizar despacho
@PutMapping("/{id}")
public ResponseEntity<Map<String, Object>> actualizarDespacho(
        @PathVariable int id,
        @Valid @RequestBody UpdateRequestDelivery request
) {

    Delivery actualizado =
            deliveryService.updateDelivery(id, request);

    Map<String, Object> response = new HashMap<>();
    response.put("mensaje", "Despacho actualizado correctamente");
    response.put("id", actualizado.getId());

    return ResponseEntity.ok(response);
}

//eliminar
@DeleteMapping("/{id}")
public ResponseEntity<Map<String, String>> eliminarDespacho(
        @PathVariable int id
) {

    boolean eliminado =
            deliveryService.deleteDelivery(id);

    if (!eliminado) {
        throw new ResourceNotFoundException(
                "Despacho con id=" + id + " no encontrado"
        );
    }

    Map<String, String> response = new HashMap<>();
    response.put("mensaje", "Despacho eliminado correctamente");

    return ResponseEntity.ok(response);
}



//endpoint de hsitoriasde usaurios 
//ver seguimiento de pedido

@GetMapping("/{id}/seguimiento")
public ResponseEntity<Map<String, Object>> verSeguimiento(
        @PathVariable int id
) {

    Delivery delivery =
            deliveryService.getDeliveryById(id);

    Map<String, Object> response = new HashMap<>();
    response.put("pedidoId", delivery.getPedidoId());
    response.put("estadoEntrega", delivery.getEstadoEntrega());

    return ResponseEntity.ok(response);
}

//cambiar estado de entrega

@PutMapping("/{id}/estado")
public ResponseEntity<Map<String, String>> cambiarEstadoEntrega(
        @PathVariable int id,
        @RequestBody Map<String, String> request
) {

    deliveryService.cambiarEstadoEntrega(
            id,
            request.get("estadoEntrega")
    );

    Map<String, String> response = new HashMap<>();
    response.put("mensaje",
            "Estado de entrega actualizado correctamente");

    return ResponseEntity.ok(response);
}

//buscar por pedido

@GetMapping("/pedido/{pedidoId}")
public ResponseEntity<List<Delivery>> buscarPorPedido(
        @PathVariable Integer pedidoId
) {

    List<Delivery> despachos =
            deliveryService.buscarPorPedido(pedidoId);

    return ResponseEntity.ok(despachos);
}

}