package org.esfe.config;

import org.esfe.modelos.EstadoPedido;
import org.esfe.modelos.Rol;
import org.esfe.repositorios.IEstadoPedidoRepository;
import org.esfe.repositorios.IRolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private IEstadoPedidoRepository estadoPedidoRepository;

    @Override
    public void run(String... args) {
        crearRolSiNoExiste("ADMIN");
        crearRolSiNoExiste("CLIENTE");
        crearEstadoSiNoExiste("PENDIENTE");
        crearEstadoSiNoExiste("CONFIRMADO");
        crearEstadoSiNoExiste("CANCELADO");
    }

    private void crearRolSiNoExiste(String nombre) {
        if (rolRepository.findByNombreIgnoreCase(nombre).isEmpty()) {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rolRepository.save(rol);
        }
    }

    private void crearEstadoSiNoExiste(String nombre) {
        if (estadoPedidoRepository.findByNombreIgnoreCase(nombre).isEmpty()) {
            EstadoPedido estado = new EstadoPedido();
            estado.setNombre(nombre);
            estadoPedidoRepository.save(estado);
        }
    }
}
