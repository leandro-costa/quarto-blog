
public class UsuarioService extends GenericServiceImpl<Usuario, Long> {
    public UsuarioService() {
        super(new UsuarioDAO());
    }

    @Override
    public void validar(Usuario usuario) throws RegraDeNegocioException {
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new RegraDeNegocioException("Nome do usuário é obrigatório");
        }
        boolean emailDuplicado = buscarTodos().stream()
                .anyMatch(u -> u.getEmail().equals(usuario.getEmail()));
        if (emailDuplicado) {
            throw new RegraDeNegocioException("E-mail já cadastrado");
        }
    }
}