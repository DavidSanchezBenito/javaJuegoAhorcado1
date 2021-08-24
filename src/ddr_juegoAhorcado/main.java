package ddr_juegoAhorcado;

import java.util.Scanner;

public class main {

//	https://www.youtube.com/watch?v=su6_2XpMrjk&list=PLaxZkGlLWHGXQ9MSswRu4pI3ROgt2qzcX&index=19

// https://www.youtube.com/watch?v=PsEHmAaG0QM&t=716s
	//acabado el 20-8-21

	public static void main(String[] args) {

		final int PUNTUACION_SUPERAR = 3;  //el primero q llegue a 3 gana la partida
		final int NUMERO_ERRORES = 6;  //maximo de intentos para adivinar la palabra

		Scanner sc = new Scanner(System.in);

		String[] jugadores = new String[2];
		int puntos[] = new int[2]; // dos almacenes de puntos

		boolean[] caracteresInsertados = new boolean[26];

		jugadores[0] = pedirString(sc, "nombre del jugador 1: ");

		jugadores[1] = pedirString(sc, "nombre del jugador 2: ");

		String pista;
		int turno = 0;
		int numeroErrores = 6; // es el numero de vidas
		int reemplazos;

		String palabraElegida; // la q hay q adivinar
		String palabraAdivinar;
		String palabraUsuario; // la letra o si el usuario escribe una palabra

		char abecedario[] = generaCaracteres(); // creo el abecedario

		while (!fin(puntos, PUNTUACION_SUPERAR)) {

			palabraElegida = validar(sc,
					"jugador *** " + jugadores[turno]
							+ " *** escribe una palabra sin espacios, solo caracteres y con un minimo de dos",
					"jugador *** " + jugadores[turno]
							+ " *** ERROR, escribe una palabra sin espacios y solo caracteres, minimo de dos caract");

			pista = validar(sc,
					"*** " + jugadores[turno]
							+ " *** escribe una pista para el otro jugador, sin espacios y solo caracteres:  ",
					"ERROR, escribe una palabra sin espacios y solo caracteres, jugador " + jugadores[turno]);

			espacios(5);

			// creo la palabra oculta que iré mostrando letras segun se adivina
			palabraAdivinar = rellenaGuiones(palabraElegida); // genero la palabra oculta pero no se muestra

			while (!palabraCorrecta(palabraElegida, palabraAdivinar) && numeroErrores > 0) {
//mientras las palabras no coincidan, el programa continuará:

				// esto es para el turno del oponente:
				System.out.println("TURNO DEL OPONENTE");
				System.out.println("Pista facilitada para adivinar la palabra: " + pista + ", con estas posiciones: ");
				mostrarPalabra(palabraAdivinar); // muestro los guiones con las posiciones
				mostrarRepetidos(abecedario, caracteresInsertados); // muestra las posiciones de la letra
				palabraUsuario = pedirString(sc,
						"Escribe una letra (si escribes una palabra será como escribir la solucion)").toString();

				// el usuario puede escribir una letra o una palabra:
				if (palabraUsuario.length() == 1) {

					if (Character.isAlphabetic(palabraUsuario.charAt(0))) {
						if (caracterIntroducido(caracteresInsertados, palabraUsuario.charAt(0))) {
							// si esta insertado = mensaje
							System.out.println("el caracter ya está puesto, elige otro");

							// otra comprobacion despues a realizar
						} else if (numReemplazos(palabraElegida, palabraUsuario.charAt(0)) > 0) {
							palabraAdivinar = Reemplazar(palabraElegida, palabraAdivinar, palabraUsuario.charAt(0));
						} else {
							numeroErrores--;
							System.out.println("error, te quedan " + numeroErrores + " intentos");
							System.out.println("cambio de jugador");
						}
						actualizarInsertados(caracteresInsertados, palabraUsuario.charAt(0));
					}
					//si pone más de una letra, será como revisar una palabra:
				} else if(!palabraCorrecta(palabraElegida, palabraUsuario)) {
						// la función devuelve true si coinciden, pero como es falso:
						numeroErrores--;
						System.out.println("error, no es la palabra correcta");
				} else {
						palabraAdivinar = palabraUsuario;
						System.out.println("correcto!!!  has acertado");

					}
			}   //fin del While	

			if (numeroErrores > 0) {
				sumarPuntosOtroJugador(puntos, 1, turno);  //si estoy en el turno 0, puntua el de la 1
				turno = cambiaTurno(turno, jugadores.length - 1);

			} else {  //ya no tiene mas intentos.
				//el usuario que puso la palabra suma un punto y continua escribiendo la palabra
				puntos[turno]++;
			}
			mostrarPuntuaciones(jugadores, puntos);
		
			numeroErrores = NUMERO_ERRORES;  //reiniciamos los errores y los caracteres insertados
			actualizarResultados(caracteresInsertados);
			}
		mostrarGanador(puntos, jugadores, PUNTUACION_SUPERAR);
		System.out.println("FIN");
		

	}

	private static void mostrarRepetidos(char[] abecedario, boolean caracteresInsertados[]) {

		System.out.print("Letras introducidas hasta el momento:");
		for (int i = 0; i < caracteresInsertados.length; i++) { // recorro
			// caracteresInsertados se crea para almacenar 26 letras, pero no contiene datos
			if (caracteresInsertados[i]) {
				System.out.print(abecedario[i]);
			}

			/*
			 * if (caracteresInsertados.length == 0) {
			 * System.out.println("todavia no se han introducido letras"); } else {
			 * System.out.print(abecedario[i]+ " "); // muestra las repetidas }
			 */

		}
		System.out.println("");

	}

	public static String pedirString(Scanner sc, String mensaje) {
		System.out.println(mensaje);
		return sc.next();

	}

	public static char[] generaCaracteres() {
		// crear diccionario
		char[] caracteres = new char[26]; // creo las letras
		for (int i = 0, j = 97; i < caracteres.length; i++, j++) { // i para el indice, j para el valor de la letra

			caracteres[i] = (char) j; // transforma una posicion en letra, desde el 97 q es el abecederario

		}
		return caracteres;
	}

	public static boolean comprobarSoloLetras(String cadena) {

		// Recorremos cada caracter de la cadena y comprobamos si son letras.
		// Para comprobarlo, lo pasamos a mayuscula y consultamos su numero ASCII.
		// Si está fuera del rango 65 - 90, es que NO son letras.
		// Para ser más exactos al tratarse del idioma español, tambien comprobamos
		// el valor 165 equivalente a la Ñ

		for (int i = 0; i < cadena.length(); i++) {
			char caracter = cadena.charAt(i); // cogo la primera letra
//					int valorASCII = (int)caracter;
//					if (valorASCII != 165 && (valorASCII < 65 || valorASCII > 90))
//						return false; //Se ha encontrado un caracter que no es letra
//				}
			if (!Character.isAlphabetic(caracter)) { // si no ha metido letras
				return true;
			}
		}
		// Terminado el bucle sin que se hay retornado false, es que todos los
		// caracteres son letras
		return false;

	}

	public static boolean fin(int puntos[], int puntuacionASuperar) {
		for (int i = 0; i < puntos.length; i++) {
			if (puntos[i] > puntuacionASuperar) {
				return true;
			}
		}
		return false;
	}

	public static String validar(Scanner sc, String mensaje, String mensjError) {
		String palabra;
		do {

			palabra = pedirString(sc, mensaje).toLowerCase();
			// si es una sola letra, compruebo longitud y q sean solo letras
			if (comprobarSoloLetras(palabra) || palabra.length() <= 1) {
				System.out.println(mensjError);

			}

		} while (comprobarSoloLetras(palabra)); // si mete palabras y numeros

		return palabra;

	}

	public static void espacios(int numSaltos) {
//el numero q se le indique, son las lineas en blanco que dejara
		for (int i = 0; i < numSaltos; i++) {
			System.out.println("");
		}
	}

	public static String rellenaGuiones(String cadena) {
		// este metodo sustituyo las letras por guiones.
		String palabra = "";
		for (int i = 0; i < cadena.length(); i++) {
			palabra += "_";
		}
		return palabra;
	}

	public static void mostrarPalabra(String cadena) {

		for (int i = 0; i < cadena.length(); i++) {
			System.out.print(cadena.charAt(i) + " ");
		}

		System.out.println("");
		System.out.println("");
	}

	public static boolean palabraCorrecta(String original, String palabraUsuario) {
		// la palabra del usuario son rayas, por tanto no va a ser igual al principio
		return original.equals(palabraUsuario);
	}

	public static void sumarPuntosOtroJugador(int[] puntos, int puntosSuperar, int posExcluida) {

		for (int i = 0; i < puntos.length; i++) {
			if (i != posExcluida) {
				puntos[i]++;
			}
		}

	}

	public static int numReemplazos(String cadenaOriginal, char caracter) {
		// cuenta las letras se van a reemplazar
		int reemplazos = 0;
		char caracterCadena;

		for (int i = 0; i < cadenaOriginal.length(); i++) {
			caracterCadena = cadenaOriginal.charAt(i); // extraigo las letras de la palabra a adivinar

			if (caracterCadena == caracter) {
				reemplazos++;
			}
		}
		return reemplazos;
	}

	public static String Reemplazar(String cadenaOriginal, String cadenaReemplazar, char caracter) {

		// cuenta las letras se van a reemplazar
		String cadenaReemplazo = "";

		char caracterCadena; // dd almaceno la letra de la palabra a adivinar

		for (int i = 0; i < cadenaOriginal.length(); i++) {
			caracterCadena = cadenaOriginal.charAt(i); // letras de la palabra

			if (caracterCadena == caracter) { // comparo cada letra de la palabra con el caracter introducido
				cadenaReemplazo += caracter;
			} else {
				cadenaReemplazo += cadenaReemplazar.charAt(i);
			}
		}
		return cadenaReemplazo;
	}

	public static boolean caracterIntroducido(boolean[] insertados, char caracter) {

		return insertados[caracter - 'a'];
	}

	public static void actualizarInsertados(boolean[] insertados, char caracter) {

		insertados[caracter - 'a'] = true;
	}

	public static int cambiaTurno(int turnoActual, int limite) {
		// nos sirve para saber el nuevo turno
		// Cambia el turno del jugador, si llega al final, empieza de cero
		if (turnoActual == limite) {
			return 0;
		} else {
			return ++turnoActual;
		}

	}

	public static void mostrarPuntuaciones(String[] jugadores, int[] puntos) {
		for (int i = 0; i < puntos.length; i++) {
			System.out.println(jugadores[i] + ": " + puntos[i] + " puntos");
		}
		System.out.println("");
	}

	public static void mostrarGanador(int[] puntos, String jugadores[], int pUNTUACION_SUPERAR) {

		int indiceGanador = 0;
		for (int i = 0; i < puntos.length; i++) {
			if (puntos[i] <= pUNTUACION_SUPERAR) {
				indiceGanador = i;
				break;

			}
		}
		System.out.println("el ganador es: " + jugadores[indiceGanador]);

	}

	public static void actualizarResultados(boolean insertados[]) {
		for (int i = 0; i < insertados.length; i++) {
			insertados[i] = false;
		}

	}
}