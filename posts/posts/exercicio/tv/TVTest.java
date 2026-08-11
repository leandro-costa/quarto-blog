import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TVTest {
    private TV tv;

    @Before
    public void setUp() {
        tv = new TV(42.0f, "Samsung", Voltagem.V110);
    }

    @Test
    public void testConstrutor() {
        assertEquals(42.0f, tv.tamanhoTela, 0.0f);
        assertEquals("Samsung", tv.marca);
        assertEquals(Voltagem.V110, tv.voltagem);
        assertFalse(tv.ligado);
        assertEquals(5, tv.volume);
    }

    @Test
    public void testLigar() {
        float consumo = tv.ligar();
        assertTrue(tv.ligado);
        assertEquals(42.0f * 110, consumo, 0.0f);
    }

    @Test
    public void testDesligar() {
        tv.ligar();
        boolean resultado = tv.desligar();
        assertFalse(tv.ligado);
        assertFalse(resultado);
    }

    @Test
    public void testAumentarVolume() {
        tv.ligar();
        assertEquals(6, tv.aumentarVolume());
        assertEquals(7, tv.aumentarVolume());
    }

    @Test
    public void testAumentarVolumeMaximo() {
        tv.ligar();
        for (int i = 0; i < 10; i++) {
            tv.aumentarVolume();
        }
        assertEquals(10, tv.volume);
    }

    @Test
    public void testAumentarVolumeDesligado() {
        assertEquals(5, tv.aumentarVolume());
    }

    @Test
    public void testDiminuirVolume() {
        tv.ligar();
        assertEquals(4, tv.diminuirVolume());
        assertEquals(3, tv.diminuirVolume());
    }

    @Test
    public void testDiminuirVolumeMinimo() {
        tv.ligar();
        for (int i = 0; i < 10; i++) {
            tv.diminuirVolume();
        }
        assertEquals(1, tv.volume);
    }

    @Test
    public void testDiminuirVolumeDesligado() {
        assertEquals(5, tv.diminuirVolume());
    }

    @Test
    public void testSubirCanal() {
        tv.ligar();
        assertEquals(1, tv.subirCanal());
        assertEquals(2, tv.subirCanal());
    }

    @Test
    public void testSubirCanalDesligado() {
        assertEquals(0, tv.subirCanal());
    }

    @Test
    public void testDescerCanal() {
        tv.ligar();
        tv.subirCanal();
        tv.subirCanal();
        assertEquals(1, tv.descerCanal());
    }

    @Test
    public void testDescerCanalLimiteMinimo() {
        tv.ligar();
        assertEquals(0, tv.descerCanal());
    }

    @Test
    public void testDescerCanalDesligado() {
        assertEquals(0, tv.descerCanal());
    }
}