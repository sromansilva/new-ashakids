package com.example.service;

import com.example.model.CompraPaquete;
import com.example.model.Paquete;
import com.example.model.SesionConsumida;
import com.example.model.Usuario;
import com.example.model.Cita;
import com.example.repository.CompraPaqueteRepository;
import com.example.repository.PaqueteRepository;
import com.example.repository.SesionConsumidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompraPaqueteService {
    @Autowired
    private CompraPaqueteRepository compraPaqueteRepository;
    @Autowired
    private PaqueteRepository paqueteRepository;
    @Autowired
    private SesionConsumidaRepository sesionConsumidaRepository;

    // Comprar un paquete (estado pendiente)
    public CompraPaquete comprarPaquete(Usuario padre, Usuario terapeuta, Paquete paquete) {
        CompraPaquete compra = new CompraPaquete();
        compra.setPadre(padre);
        compra.setTerapeuta(terapeuta);
        compra.setPaquete(paquete);
        compra.setHoras_restantes(paquete.getHoras());
        compra.setEstadoPago(CompraPaquete.EstadoPago.pendiente);
        compra.setDeuda(paquete.getPrecio());
        return compraPaqueteRepository.save(compra);
    }

    // Confirmar pago (por terapeuta)
    public void confirmarPago(CompraPaquete compra) {
        compra.setEstadoPago(CompraPaquete.EstadoPago.confirmado);
        compra.setDeuda(0.0);
        compraPaqueteRepository.save(compra);
    }

    // Consultar horas disponibles para un padre y terapeuta (solo paquetes confirmados)
    public int horasDisponibles(Usuario padre, Usuario terapeuta) {
        List<CompraPaquete> compras = compraPaqueteRepository.findByPadreAndTerapeutaAndEstadoPago(
            padre, terapeuta, CompraPaquete.EstadoPago.confirmado);
        return compras.stream().mapToInt(CompraPaquete::getHoras_restantes).sum();
    }

    // Obtener la compra activa (con horas y confirmada) para un padre y terapeuta
    public Optional<CompraPaquete> getCompraActiva(Usuario padre, Usuario terapeuta) {
        List<CompraPaquete> compras = compraPaqueteRepository.findByPadreAndTerapeutaAndEstadoPago(
            padre, terapeuta, CompraPaquete.EstadoPago.confirmado);
        return compras.stream().filter(c -> c.getHoras_restantes() > 0).findFirst();
    }

    // Consumir horas al agendar una sesión
    public boolean consumirHoras(Usuario padre, Usuario terapeuta, Cita cita, int horas) {
        Optional<CompraPaquete> compraOpt = getCompraActiva(padre, terapeuta);
        if (compraOpt.isPresent()) {
            CompraPaquete compra = compraOpt.get();
            if (compra.getHoras_restantes() >= horas) {
                compra.setHoras_restantes(compra.getHoras_restantes() - horas);
                compraPaqueteRepository.save(compra);
                SesionConsumida sesion = new SesionConsumida();
                sesion.setCompra(compra);
                sesion.setCita(cita);
                sesion.setHoras_consumidas(horas);
                sesionConsumidaRepository.save(sesion);
                return true;
            }
        }
        return false;
    }

    // Verificar si el usuario puede agendar (tiene horas y pago confirmado)
    public boolean puedeAgendar(Usuario padre, Usuario terapeuta, int horas) {
        Optional<CompraPaquete> compraOpt = getCompraActiva(padre, terapeuta);
        return compraOpt.isPresent() && compraOpt.get().getHoras_restantes() >= horas;
    }

    public Optional<CompraPaquete> findById(Integer id) {
        return compraPaqueteRepository.findById(id);
    }

    public List<CompraPaquete> findByPadreAndEstadoPago(Usuario padre, CompraPaquete.EstadoPago estadoPago) {
        return compraPaqueteRepository.findByPadreAndEstadoPago(padre, estadoPago);
    }

    public List<CompraPaquete> findByTerapeutaAndEstadoPago(Usuario terapeuta, CompraPaquete.EstadoPago estadoPago) {
        // Buscar todas las compras donde el terapeuta es el dado y el estado de pago es el dado
        return compraPaqueteRepository.findAll().stream()
            .filter(c -> c.getTerapeuta().getId_usuario().equals(terapeuta.getId_usuario()) && c.getEstadoPago() == estadoPago)
            .toList();
    }
} 