package graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HojaSprites {
	private final int ancho;
	private final int alto;

	public final int[] pixeles;

	// Coleccion de hojas de sprites
	public static HojaSprites desierto = new HojaSprites("/texturas/plantilla_sprites_1.png", 320, 320);

	// Fin de la coleccion

	public HojaSprites(final String ruta, final int ancho, final int alto) {
		// cuando ponemos "this" le estamos diciendo que el valor es el propio
		// de la "clase (class)"
		this.ancho = ancho;
		this.alto = alto;

		pixeles = new int[ancho * alto];

		BufferedImage imagen;
		try {
			imagen = ImageIO.read(HojaSprites.class.getResource(ruta));

			imagen.getRGB(0, 0, ancho, alto, pixeles, 0, ancho);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}
}
