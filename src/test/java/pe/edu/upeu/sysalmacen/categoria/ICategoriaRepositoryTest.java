package pe.edu.upeu.sysalmacen.categoria;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.sysalmacen.modelo.Categoria;
import pe.edu.upeu.sysalmacen.modelo.Marca;
import pe.edu.upeu.sysalmacen.repositorio.ICategoriaRepository;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
//@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@ActiveProfiles("test") //Para base de datos real de pruebas

public class ICategoriaRepositoryTest {
    @Autowired
    private ICategoriaRepository categoriaRepository;
    private static Long categoriaId;

    @BeforeEach
    public void setUp() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Electrónica");
        Categoria guardada = categoriaRepository.save(categoria);
        categoriaId = guardada.getIdCategoria(); // Guardamos el ID para pruebas posteriores
    }
    @Test
    @Order(1)
    public void testGuardarCategoria() {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre("Ropa");
        Categoria guardada = categoriaRepository.save(nuevaCategoria);
        assertNotNull(guardada.getIdCategoria());
        assertEquals("Ropa", guardada.getNombre());
    }

    @Test
    @Order(2)
    public void testBuscarPorId() {
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
        assertTrue(categoria.isPresent());
        assertEquals("Electrónica", categoria.get().getNombre());
    }

    @Test
    @Order(3)
    public void testActualizarCategoria() {
        Categoria categoria = categoriaRepository.findById(categoriaId).orElseThrow();
        categoria.setNombre("Electrónica de Consumo");
        Categoria actualizada = categoriaRepository.save(categoria);
        assertEquals("Electrónica de Consumo", actualizada.getNombre());
    }

    @Test
    @Order(4)
    public void testListarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        assertFalse(categorias.isEmpty());
        System.out.println("Total categorías registradas: " + categorias.size());
        for (Categoria c : categorias) {
            System.out.println(c.getNombre() + "\t" + c.getIdCategoria());
        }
    }

    @Test
    @Order(5)
    public void testEliminarCategoria() {
        categoriaRepository.deleteById(categoriaId);
        Optional<Categoria> eliminada = categoriaRepository.findById(categoriaId);
        assertFalse(eliminada.isPresent(), "La categoría debería haber sido eliminada");
    }
}
