import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.reflect.Constructor;

public class IdGenerator {

    private static final AtomicLong contador = new AtomicLong(0);

    @SuppressWarnings("unchecked")
    public static <T> T gerarNovoId(Class<T> tipoClasse) {
        if (Number.class.isAssignableFrom(tipoClasse)) {
            try {
                Constructor<T> construtor = tipoClasse.getConstructor(long.class);
                return construtor.newInstance(contador.incrementAndGet());
            } catch (Exception e) {
                throw new RuntimeException("Falha ao gerar ID para o tipo: " + tipoClasse.getSimpleName(), e);
            }
        } else if (tipoClasse == UUID.class) {
            return (T) UUID.randomUUID();
        }
        throw new IllegalArgumentException("Tipo de ID não suportado: " + tipoClasse.getSimpleName());
    }
}
