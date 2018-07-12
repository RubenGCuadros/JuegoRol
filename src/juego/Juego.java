package juego;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import control.Teclado;
import graficos.Pantalla;

public class Juego extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO = 1280;
	private static final int ALTO = 720;

	private static volatile boolean enFuncionamiento = false;

	private static final String NOMBRE = "juego";

	private static int aps = 0;
	private static int fps = 0;

	private static int x = 0;
	private static int y = 0;

	private static JFrame ventana;
	private static Thread thread;
	private static Teclado teclado;
	private static Pantalla pantalla;

	private static BufferedImage imagen = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);

	private static int[] pixeles = ((DataBufferInt) imagen.getRaster().getDataBuffer()).getData();

	private static final ImageIcon icono = new ImageIcon(Juego.class.getResource("/icono/icono.png"));

	private Juego() {
		setPreferredSize(new Dimension(ANCHO, ALTO));

		pantalla = new Pantalla(ANCHO, ALTO);

		teclado = new Teclado();
		addKeyListener(teclado);

		ventana = new JFrame(NOMBRE);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setResizable(false);
		ventana.setLayout(new BorderLayout());
		ventana.add(this, BorderLayout.CENTER);
		ventana.pack();
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
		ventana.setIconImage(icono.getImage());
	}

	public static void main(String[] args) {
		Juego juego = new Juego();
		juego.iniciar();
	}

	private synchronized void iniciar() {
		enFuncionamiento = true;

		thread = new Thread(this, "Graficos");
		thread.start();
	}

	private synchronized void detener() {
		enFuncionamiento = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void actualizar() {
		teclado.actualizar();

		if (teclado.arriba) {
			y++;
		}
		if (teclado.abajo) {
			y--;
		}
		if (teclado.izquierda) {
			x++;
		}
		if (teclado.derecha) {
			x--;
		}

		aps++;

	}

	private void mostrar() {
		BufferStrategy estrategia = getBufferStrategy();

		if (estrategia == null) {
			createBufferStrategy(3);
			return;
		}

		pantalla.limpiar();
		pantalla.mostrar(x, y);

		System.arraycopy(pantalla.pixeles, 0, pixeles, 0, pixeles.length);

		// puede ser lento en algunas maquinas
		// for (int i = 0; i < pixeles.length; i++) {
		// pixeles[i] = pantalla.pixeles[i];
		// }

		Graphics g = estrategia.getDrawGraphics();

		g.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
		g.dispose();

		estrategia.show();

		fps++;

	}

	public void run() {
		final int NS_POR_SEGUNDO = 1000000000;
		// APS=Actualizacion por segundo. un byte va de -128 a 127
		final byte APS_OBJETIVO = 60;
		// Cuantos nano segundos transcurren por actualizacion
		final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

		// Aqui se atribuye una cantidad de tiempo en nano segundos en ese
		// momento exacto. Los System.nanoTime se basa en el relos de la CPU
		long referenciaActualizacion = System.nanoTime();
		long referenciaContador = System.nanoTime();

		double tiempoTranscurrido;
		// Es la cantidad de tiempo que ha transcurrido hasta que se realiza una
		// actualización
		double delta = 0;

		// Este metodo es para darle a la pantalla el foco, es decir donde se
		// podrá utilizar el teclado de forma directa
		requestFocus();

		while (enFuncionamiento) {
			// Esta variable toma el valor en nanosegundos, pero este valor no
			// es el mismo que la variable anterior ya que ha transcurrido
			// tiempo de una variable a otro ya que esta unas lineas más abajo.
			// Esto equivale a iniciar el cronometro
			final long inicioBucle = System.nanoTime();

			// Aqui veremos el tiempo que ha pasado entre nanoTime
			tiempoTranscurrido = inicioBucle - referenciaActualizacion;
			referenciaActualizacion = inicioBucle;

			delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;

			while (delta >= 1) {
				actualizar();
				delta--;

			}

			mostrar();

			if ((System.nanoTime() - referenciaContador) > NS_POR_SEGUNDO) {
				ventana.setTitle(NOMBRE + " || APS: " + aps + " || FPS: " + fps);
				aps = 0;
				fps = 0;
				referenciaContador = System.nanoTime();
			}
		}
	}
}
