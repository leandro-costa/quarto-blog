import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericDAOImpl<T extends AbstractModel<ID>, ID> implements GenericDAO<T, ID> {
    private Map<ID, T> bancoDeDados = new HashMap<>();
    private final Class<ID> tipoIdClass;

    public GenericDAOImpl(Class<ID> tipoIdClass) {
        this.tipoIdClass = tipoIdClass;
    }

    @Override
    public ID salvar(T entidade) {
        ID novoId = IdGenerator.gerarNovoId(tipoIdClass);
        entidade.setId(novoId);
        entidade.setCreatedAt(LocalDateTime.now());        
        bancoDeDados.put(entidade.getId(), entidade);
        return novoId;
    }

    @Override
    public void atualizar(T entidade) {
        if (!bancoDeDados.containsKey(entidade.getId())) {
            throw new IllegalArgumentException(
                "Entidade não encontrada com o id " + entidade.getId());
        }
        entidade.setUpdatedAt(LocalDateTime.now());
        bancoDeDados.put(entidade.getId(), entidade);
    }

    @Override
    public T buscarPorId(ID id) {
        return bancoDeDados.get(id);
    }

    @Override
    public void deletar(ID id) {
        bancoDeDados.remove(id);
    }

    @Override
    public List<T> buscarTodos() {
        return new ArrayList<>(bancoDeDados.values());
    }
}
