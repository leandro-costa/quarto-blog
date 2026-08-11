import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UsuarioDAO extends GenericDAOImpl<Usuario, UUID> {
    public UsuarioDAO() {
        super(UUID.class);
    }

    public List<Usuario> buscarOrdenadosPorNome() {
        return buscarTodos()
                .stream()
                .sorted(Comparator.comparing(Usuario::getNome))
                .collect(Collectors.toList());
    }
}
