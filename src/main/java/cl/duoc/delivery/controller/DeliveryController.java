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



// IMPORTACIONES DE OPENAPI / SWAGGER
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/delivery")
@Tag(name = "Delivery Controller", description = "Endpoints para la gestión, despacho y seguimiento logístico de los pedidos")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final WebClient webClient;

    public DeliveryController(DeliveryService deliveryService, WebClient webClient) {
        this.deliveryService = deliveryService;
        this.webClient = webClient;
    }

    // LISTAR DESPACHOS
    @GetMapping
    @Operation(summary = "Listar despachos", description = "Obtiene un listado con todas las órdenes de delivery registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de despachos obtenida con éxito")
    public ResponseEntity<List<Delivery>> listarDespachos() {
        List<Delivery> despachos = deliveryService.getDeliverys();
        return ResponseEntity.ok(despachos);
    }

    // CREAR DESPACHO
    @PostMapping
    @Operation(summary = "Crear un nuevo despacho", description = "Registra una nueva orden de despacho asociada a un pedido en estado inicial.")
    @ApiResponse(responseCode = "201", description = "Despacho creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    public ResponseEntity<Map<String, Object>> crearDespacho(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Estructura JSON necesaria para registrar el despacho",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateRequestDelivery.class),
                examples = @ExampleObject(
                    name = "Ejemplo de Despacho",
                    value = "{\n  \"pedidoId\": 105,\n  \"direccion\": \"Av. Concha y Toro 1340\",\n  \"comuna\": \"Puente Alto\",\n  \"comentario\": \"Dejar en conserjería\"\n}"
                )
            )
        )
        @Valid @RequestBody CreateRequestDelivery request
    ) {
        Delivery nuevoDespacho = deliveryService.saveDelivery(DeliveryMapper.toDelivery(request));
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Despacho creado correctamente");
        response.put("id", nuevoDespacho.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar despacho por ID", description = "Recupera la información de una orden de delivery específica mediante su identificador único.")
    @ApiResponse(responseCode = "200", description = "Despacho encontrado")
    @ApiResponse(responseCode = "404", description = "El ID del despacho solicitado no existe")
    public ResponseEntity<Delivery> buscarDespacho(@PathVariable int id) {
        Delivery delivery = deliveryService.getDeliveryById(id);
        if (delivery == null) {
            throw new ResourceNotFoundException("Despacho con id=" + id + " no encontrado");
        }
        return ResponseEntity.ok(delivery);
    }

    // ACTUALIZAR DESPACHO
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos del despacho", description = "Permite modificar la dirección o la configuración logística de un delivery en curso.")
    @ApiResponse(responseCode = "200", description = "Despacho actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Despacho no encontrado")
    public ResponseEntity<Map<String, Object>> actualizarDespacho(
        @PathVariable int id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Campos a actualizar de la orden logístca",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UpdateRequestDelivery.class),
                examples = @ExampleObject(
                    name = "Ejemplo de Actualización",
                    value = "{\n  \"direccion\": \"Nueva Avenida 456\",\n  \"comuna\": \"La Florida\"\n}"
                )
            )
        )
        @Valid @RequestBody UpdateRequestDelivery request
    ) {
        Delivery actualizado = deliveryService.updateDelivery(id, request);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Despacho actualizado correctamente");
        response.put("id", actualizado.getId());
        return ResponseEntity.ok(response);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar despacho", description = "Borra físicamente el registro de despacho del sistema.")
    @ApiResponse(responseCode = "200", description = "Despacho eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Despacho no encontrado")
    public ResponseEntity<Map<String, String>> eliminarDespacho(@PathVariable int id) {
        boolean eliminado = deliveryService.deleteDelivery(id);
        if (!eliminado) {
            throw new ResourceNotFoundException("Despacho con id=" + id + " no encontrado");
        }
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Despacho eliminado correctamente");
        return ResponseEntity.ok(response);
    }

    // VER SEGUIMIENTO DE PEDIDO
    @GetMapping("/{id}/seguimiento")
    @Operation(summary = "Consultar seguimiento", description = "Endpoint optimizado para ver de forma rápida el estado operativo de la entrega a través del ID del despacho.")
    @ApiResponse(responseCode = "200", description = "Estado de seguimiento recuperado")
    public ResponseEntity<Map<String, Object>> verSeguimiento(@PathVariable int id) {
        Delivery delivery = deliveryService.getDeliveryById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("pedidoId", delivery.getPedidoId());
        response.put("estadoEntrega", delivery.getEstadoEntrega());
        return ResponseEntity.ok(response);
    }

    // CAMBIAR ESTADO DE ENTREGA
    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del despacho", description = "Permite modificar el estado de la entrega en tránsito (ej: PENDIENTE, EN_CAMINO, ENTREGADO).")
    @ApiResponse(responseCode = "200", description = "Estado logístico actualizado con éxito")
    public ResponseEntity<Map<String, String>> cambiarEstadoEntrega(
        @PathVariable int id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON con el nuevo estado del delivery",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Ejemplo Cambio Estado Delivery",
                    value = "{\n  \"estadoEntrega\": \"EN_CAMINO\"\n}"
                )
            )
        )
        @RequestBody Map<String, String> request
    ) {
        deliveryService.cambiarEstadoEntrega(id, request.get("estadoEntrega"));
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Estado de entrega actualizado correctamente");
        return ResponseEntity.ok(response);
    }

    // BUSCAR POR PEDIDO
    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Buscar despachos asociados a un pedido", description = "Filtra y devuelve todos las órdenes de delivery vinculadas a un ID de compra específico.")
    @ApiResponse(responseCode = "200", description = "Despachos del pedido listados")
    public ResponseEntity<List<Delivery>> buscarPorPedido(@PathVariable Integer pedidoId) {
        List<Delivery> despachos = deliveryService.buscarPorPedido(pedidoId);
        return ResponseEntity.ok(despachos);
    }
}