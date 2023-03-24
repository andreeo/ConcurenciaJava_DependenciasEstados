package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parque implements IParque{

	// MIN y MAX son los valores mínimo y máximo de personas que pueden estar en el parque
	private final static int MIN = 0;
	private final static int MAX = 100;

	// Generador de números aleatorios
	private static Random generadorAleatorios = new Random();

	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	
	
	public Parque() {
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
	}


	@Override
	public synchronized void entrarAlParque(String puerta) throws InterruptedException {
		while(!comprobarAntesDeEntrar()) {
			wait();
		}

		// Aumentar el total
		contadorPersonasTotales++;

		try { // Esperar un tiempo aleatorio
			TimeUnit.MICROSECONDS.sleep(generadorAleatorios.nextInt(3000));
		} catch (InterruptedException e) { // Interrupción del hilo
			Logger.getGlobal().log(Level.INFO, "Interrupción del hilo que utiliza el objeto parque");
			return;
		}

		// Si no hay entradas por esa puerta, inicializar
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		// Aumentar el individual	
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		// Imprimir el estado del parque
		imprimirInfo(puerta, "Entrada");
		
		checkInvariante();
		
		//Notificar cambio de estado
		notifyAll();
		
	}
	
	@Override
	public synchronized void salirDelParque(String puerta) throws InterruptedException {}
	
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		// TODO 
		// TODO
		
		
		
	}

	protected boolean comprobarAntesDeEntrar(){
		if (contadorPersonasTotales == MAX) {
			return false;
		} else {
			return true;
		}
	}

	protected boolean comprobarAntesDeSalir(){
		if (contadorPersonasTotales == MIN) {
			return false;
		} else {
			return true;
		}
	}


}
