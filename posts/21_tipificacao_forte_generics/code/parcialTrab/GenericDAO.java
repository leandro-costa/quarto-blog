import java.util.List;

public interface GenericDAO<T extends AbstractModel<ID>, ID> {
    ID salvar(T entidade);
    void atualizar(T entidade);
    T buscarPorId(ID id);
    void deletar(ID id);
    List<T> buscarTodos();
}
