import com.example.sistema.models.Usuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    private final String contrasenaCorrecta = "secreto123";
    private final String rolPrueba = "Cajero";

    @Test
    void testValidarContrasena_Correcta() {
        Usuario usuario = new Usuario(1, "admin", contrasenaCorrecta, rolPrueba);

        boolean resultado = usuario.validarContrasena(contrasenaCorrecta);

        assertTrue(resultado, "El método debe retornar true cuando la contraseña es correcta.");
    }


    @Test
    void testValidarContrasena_Incorrecta() {
        Usuario usuario = new Usuario(2, "supervisor", contrasenaCorrecta, rolPrueba);
        String contrasenaIncorrecta = "clavefalsa";

        boolean resultado = usuario.validarContrasena(contrasenaIncorrecta);

        assertFalse(resultado, "El método debe retornar false cuando la contraseña es incorrecta.");
    }


    @Test
    void testValidarContrasena_Null() {
        Usuario usuario = new Usuario(3, "test_null", contrasenaCorrecta, rolPrueba);

        assertFalse(usuario.validarContrasena(null), "El método debe retornar false si la contraseña ingresada es null");
    }
}
