
public class UsuarioController {
    private final GenericService<Usuario, UUID> usuarioService;

    public UsuarioController() {
        this.usuarioService = new UsuarioService(); 
    }

    public void cadastrar(String nome, String email) {
        try {
            Long id = usuarioService.salvar(new Usuario(nome, email));
            exibirMensagem("Usuário salvo com id " + id);
        } catch (RegraDeNegocioException e) {
            exibirErro(e.getMessage());
        }
    }
}