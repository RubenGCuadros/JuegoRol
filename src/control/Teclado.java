package control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class Teclado implements KeyListener {

	private final static int numeroTeclas = 120;
	private final boolean[] teclas = new boolean[numeroTeclas];

	// los declaramos public en vez de private para no lastrar el rendimiento
	// del juego con los getters y setters futuros

	public boolean arriba;
	public boolean abajo;
	public boolean izquierda;
	public boolean derecha;

	public void actualizar() {
		arriba = teclas[KeyEvent.VK_W];
		abajo = teclas[KeyEvent.VK_S];
		izquierda = teclas[KeyEvent.VK_A];
		derecha = teclas[KeyEvent.VK_D];
	}

	// Cuando pulsamos pero no la hemos soltado aún
	public void keyPressed(KeyEvent e) {
		teclas[e.getKeyCode()] = true;

	}

	// Cuando soltamos la tecla que habiamos pulsado
	public void keyReleased(KeyEvent e) {
		teclas[e.getKeyCode()] = false;

	}

	// Pulsar y soltar la tecla
	public void keyTyped(KeyEvent e) {

	}

}
