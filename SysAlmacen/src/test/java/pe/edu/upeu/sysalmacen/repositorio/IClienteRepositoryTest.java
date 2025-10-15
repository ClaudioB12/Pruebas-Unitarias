package pe.edu.upeu.sysalmacen.repositorio;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.upeu.sysalmacen.modelo.Cliente;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IClienteRepositoryTest {

    @Autowired
    private IClienteRepository clienteRepository;

    private static String clienteId;

    @BeforeEach
    public void setUp() {
        Cliente cliente = new Cliente();
        cliente.setDniruc("12345678"); // DNI de 8 dígitos válido
        cliente.setNombres("Juan Perez");
        cliente.setRepLegal("Maria Lopez");
        cliente.setTipoDocumento("DNI");
        cliente.setDireccion("Av. Principal 123");

        Cliente guardado = clienteRepository.save(cliente);
        clienteId = guardado.getDniruc(); // Guardamos el ID para pruebas posteriores
    }

    @Test
    @Order(1)
    public void testGuardarCliente() {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setDniruc("87654321");
        nuevoCliente.setNombres("Luis Ramirez");
        nuevoCliente.setRepLegal("Carla Diaz");
        nuevoCliente.setTipoDocumento("DNI");
        nuevoCliente.setDireccion("Calle Secundaria 456");

        Cliente guardado = clienteRepository.save(nuevoCliente);

        assertNotNull(guardado.getDniruc());
        assertEquals("Luis Ramirez", guardado.getNombres());
    }

    @Test
    @Order(2)
    public void testBuscarPorId() {
        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        assertTrue(cliente.isPresent());
        assertEquals("Juan Perez", cliente.get().getNombres());
    }

    @Test
    @Order(3)
    public void testActualizarCliente() {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow();
        cliente.setNombres("Juan Perez Modificado");
        Cliente actualizado = clienteRepository.save(cliente);

        assertEquals("Juan Perez Modificado", actualizado.getNombres());
    }

    @Test
    @Order(4)
    public void testListarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        assertFalse(clientes.isEmpty());
        System.out.println("Total clientes registrados: " + clientes.size());
        for (Cliente c : clientes) {
            System.out.println(c.getDniruc() + "\t" + c.getNombres());
        }
    }

    @Test
    @Order(5)
    public void testEliminarCliente() {
        clienteRepository.deleteById(clienteId);
        Optional<Cliente> eliminado = clienteRepository.findById(clienteId);
        assertFalse(eliminado.isPresent(), "El cliente debería haber sido eliminado");
    }
}
