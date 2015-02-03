package sokoban.logica.sokobanJuego;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import sokoban.logica.generics.Lista;
import sokoban.logica.sokoban.MensajesInicioFin;

public class Juego {

    private final Scanner in = new Scanner(System.in);
    private int filas, columnas, numeroNivelActual, totalNumeroNiveles, iJugador, jJugador;
    private char matrizJuego[][];
    private char matrizJuegoRetroceso[][];
    private final String rutaNiveles;
    private String seleccionJugador;
    private boolean nivelPasado;
    private long tiempoInicio;
    private Lista<String> listaMovimientos;

    public Juego() {
        this.filas = 0;
        this.columnas = 0;
        this.numeroNivelActual = 1;
        this.totalNumeroNiveles = 0;
        this.iJugador = 0;
        this.jJugador = 0;
        this.matrizJuego = new char[this.filas][this.columnas];
        this.matrizJuegoRetroceso = new char[this.filas][this.columnas];
        this.rutaNiveles = "src/sokoban/niveles";
        this.seleccionJugador = "";
        this.nivelPasado = false;
        this.listaMovimientos = new Lista();
    }

    public void iniciarJuego() {
        MensajesInicioFin mif = new MensajesInicioFin();
        mif.bienvenidaJuego();
        avanceNiveles();
        mif.despedidaFinJuego();
    }

    private void avanceNiveles() {
        totalNivelesEnDirectorio();
        while (this.numeroNivelActual <= this.totalNumeroNiveles) {
            resetearVariables();
            obtenerFilasColumnasArchivo("src/sokoban/niveles/Nivel_" + this.numeroNivelActual + ".xsb");
            llenarMatricesBlancos();
            llenarMatrizConNivel("src/sokoban/niveles/Nivel_" + this.numeroNivelActual + ".xsb");
            mostrarActualizarMatriz();
            this.nivelPasado = false;
            do {
                obtenerPosicionJugador();
                solicitarMovimiento();
                if (this.seleccionJugador.equalsIgnoreCase("e")) {
                    this.numeroNivelActual--;
                    break;
                }
                mostrarActualizarMatriz();
                verificarNivelGanado();
                if (this.nivelPasado == true) {
                    mostrarResumenNivel();
                }
            } while (this.nivelPasado == false);
            this.numeroNivelActual++;
        }
    }

    private void totalNivelesEnDirectorio() {
        File f = new File(this.rutaNiveles);
        this.totalNumeroNiveles = f.list().length;
    }

    private void resetearVariables() {
        this.filas = 0;
        this.columnas = 0;
        this.tiempoInicio = java.util.Calendar.getInstance().getTimeInMillis();
        this.listaMovimientos = new Lista();
    }

    private void obtenerFilasColumnasArchivo(String pArchivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(pArchivo)));
            String filaActualArchivo;
            while ((filaActualArchivo = br.readLine()) != null) {
                this.filas++;
                numeroMaximoColumnasArchivo(filaActualArchivo);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void numeroMaximoColumnasArchivo(String cantidadCaracteresFila) {
        if (cantidadCaracteresFila.length() > this.columnas) {
            this.columnas = cantidadCaracteresFila.length();
        }
    }

    private void llenarMatricesBlancos() {
        this.matrizJuego = new char[this.filas][this.columnas];
        this.matrizJuegoRetroceso = new char[this.filas][this.columnas];
        for (int i = 0; i < this.matrizJuego.length; i++) {
            for (int j = 0; j < this.matrizJuego[i].length; j++) {
                this.matrizJuego[i][j] = ' ';
                this.matrizJuegoRetroceso[i][j] = ' ';
            }
        }
    }

    private void llenarMatrizConNivel(String pArchivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(pArchivo)));
            String filaActualArchivo;
            for (int i = 0; i < this.matrizJuego.length; i++) {
                filaActualArchivo = br.readLine();
                for (int j = 0; j < filaActualArchivo.length(); j++) {
                    this.matrizJuego[i][j] = filaActualArchivo.charAt(j);
                    this.matrizJuegoRetroceso[i][j] = filaActualArchivo.charAt(j);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void mostrarActualizarMatriz() {
        System.out.println("NIVEL: " + this.numeroNivelActual);
        for (int i = 0; i < this.matrizJuego.length; i++) {
            for (int j = 0; j < this.matrizJuego[i].length; j++) {
                System.out.print(this.matrizJuego[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private void respaldarMatrizAntesDeMovimiento() {
        for (int i = 0; i < this.matrizJuego.length; i++) {
            for (int j = 0; j < this.matrizJuego[i].length; j++) {
                this.matrizJuegoRetroceso[i][j] = this.matrizJuego[i][j];
            }
        }
    }

    private void obtenerPosicionJugador() {
        for (int i = 0; i < this.matrizJuego.length; i++) {
            for (int j = 0; j < this.matrizJuego[i].length; j++) {
                if (this.matrizJuego[i][j] == '@' || this.matrizJuego[i][j] == '+') {
                    this.iJugador = i;
                    this.jJugador = j;
                }
            }
        }
    }

    private void solicitarMovimiento() {
        System.out.print("Opciones:\n" + 
                "-Movimientos (a, w, s, d)\n" + 
                "-Retrocer al movimiento anterior (r)\n" + 
                "-Reiniciar nivel (e)\n" + 
                "-Para salir (q)\n" + 
                "Selección: ");
        this.seleccionJugador = in.next();
        juezMovimientosCaracter(seleccionJugador);
        System.out.println();
        System.out.println("-------------------------------------");
        System.out.println();
    }

    private void juezMovimientosCaracter(String seleccion) {
        if (seleccion.equalsIgnoreCase("a")) {
            respaldarMatrizAntesDeMovimiento();
            validarMovimientoIzquierda();
            listaMovimientos.add("A");
        } else if (seleccion.equalsIgnoreCase("w")) {
            respaldarMatrizAntesDeMovimiento();
            validarMovimientoArriba();
            listaMovimientos.add("W");
        } else if (seleccion.equalsIgnoreCase("s")) {
            respaldarMatrizAntesDeMovimiento();
            validarMovimientoAbajo();
            listaMovimientos.add("S");
        } else if (seleccion.equalsIgnoreCase("d")) {
            respaldarMatrizAntesDeMovimiento();
            validarMovimientoDerecha();
            listaMovimientos.add("D");
        } else if (seleccion.equalsIgnoreCase("r")) {
            retrocederMovimientoAnterior();
        } else if (seleccion.equalsIgnoreCase("q")) {
            MensajesInicioFin mif = new MensajesInicioFin();
            mif.despedidaFinJuegoPorUsuario();
            System.exit(0);
        }
    }

    private void validarMovimientoIzquierda() {
        if (this.matrizJuego[this.iJugador][this.jJugador - 1] == ' ') {
            this.matrizJuego[this.iJugador][this.jJugador - 1] = '@';
            if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                this.matrizJuego[this.iJugador][this.jJugador] = '.';
            } else {
                this.matrizJuego[this.iJugador][this.jJugador] = ' ';
            }
        } else if (this.matrizJuego[this.iJugador][this.jJugador - 1] == '.') {
            this.matrizJuego[this.iJugador][this.jJugador - 1] = '+';
            if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                this.matrizJuego[this.iJugador][this.jJugador] = '.';
            } else {
                this.matrizJuego[this.iJugador][this.jJugador] = ' ';
            }
        } else if (this.matrizJuego[this.iJugador][this.jJugador - 1] == '$') {
            if (this.matrizJuego[this.iJugador][this.jJugador - 2] == ' ') {
                this.matrizJuego[this.iJugador][this.jJugador - 2] = '$';
                this.matrizJuego[this.iJugador][this.jJugador - 1] = '@';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            } else if (this.matrizJuego[this.iJugador][this.jJugador - 2] == '.') {
                this.matrizJuego[this.iJugador][this.jJugador - 2] = '*';
                this.matrizJuego[this.iJugador][this.jJugador - 1] = '@';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            }
        } else if (this.matrizJuego[this.iJugador][this.jJugador - 1] == '*') {
            if (this.matrizJuego[this.iJugador][this.jJugador - 2] == '.') {
                this.matrizJuego[this.iJugador][this.jJugador - 2] = '*';
                this.matrizJuego[this.iJugador][this.jJugador - 1] = '+';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            } else if (this.matrizJuego[this.iJugador][this.jJugador - 2] == ' ') {
                this.matrizJuego[this.iJugador][this.jJugador - 2] = '$';
                this.matrizJuego[this.iJugador][this.jJugador - 1] = '+';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            }
        }
    }

    private void validarMovimientoArriba() {
        if (this.matrizJuego[this.iJugador - 1][this.jJugador] == ' ') {
            this.matrizJuego[this.iJugador - 1][this.jJugador] = '@';
            if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                this.matrizJuego[this.iJugador][this.jJugador] = '.';
            } else {
                this.matrizJuego[this.iJugador][this.jJugador] = ' ';
            }
        } else if (this.matrizJuego[this.iJugador - 1][this.jJugador] == '.') {
            this.matrizJuego[this.iJugador - 1][this.jJugador] = '+';
            if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                this.matrizJuego[this.iJugador][this.jJugador] = '.';
            } else {
                this.matrizJuego[this.iJugador][this.jJugador] = ' ';
            }
        } else if (this.matrizJuego[this.iJugador - 1][this.jJugador] == '$') {
            if (this.matrizJuego[this.iJugador - 2][this.jJugador] == ' ') {
                this.matrizJuego[this.iJugador - 2][this.jJugador] = '$';
                this.matrizJuego[this.iJugador - 1][this.jJugador] = '@';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            } else if (this.matrizJuego[this.iJugador - 2][this.jJugador] == '.') {
                this.matrizJuego[this.iJugador - 2][this.jJugador] = '*';
                this.matrizJuego[this.iJugador - 1][this.jJugador] = '@';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            }
        } else if (this.matrizJuego[this.iJugador - 1][this.jJugador] == '*') {
            if (this.matrizJuego[this.iJugador - 2][this.jJugador] == '.') {
                this.matrizJuego[this.iJugador - 2][this.jJugador] = '*';
                this.matrizJuego[this.iJugador - 1][this.jJugador] = '+';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            } else if (this.matrizJuego[this.iJugador - 2][this.jJugador] == ' ') {
                this.matrizJuego[this.iJugador - 2][this.jJugador] = '$';
                this.matrizJuego[this.iJugador - 1][this.jJugador] = '+';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            }
        }
    }

    private void validarMovimientoAbajo() {
        if (this.matrizJuego[this.iJugador + 1][this.jJugador] == ' ') {
            this.matrizJuego[this.iJugador + 1][this.jJugador] = '@';
            if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                this.matrizJuego[this.iJugador][this.jJugador] = '.';
            } else {
                this.matrizJuego[this.iJugador][this.jJugador] = ' ';
            }
        } else if (this.matrizJuego[this.iJugador + 1][this.jJugador] == '.') {
            this.matrizJuego[this.iJugador + 1][this.jJugador] = '+';
            if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                this.matrizJuego[this.iJugador][this.jJugador] = '.';
            } else {
                this.matrizJuego[this.iJugador][this.jJugador] = ' ';
            }
        } else if (this.matrizJuego[this.iJugador + 1][this.jJugador] == '$') {
            if (this.matrizJuego[this.iJugador + 2][this.jJugador] == ' ') {
                this.matrizJuego[this.iJugador + 2][this.jJugador] = '$';
                this.matrizJuego[this.iJugador + 1][this.jJugador] = '@';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            } else if (this.matrizJuego[this.iJugador + 2][this.jJugador] == '.') {
                this.matrizJuego[this.iJugador + 2][this.jJugador] = '*';
                this.matrizJuego[this.iJugador + 1][this.jJugador] = '@';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            }
        } else if (this.matrizJuego[this.iJugador + 1][this.jJugador] == '*') {
            if (this.matrizJuego[this.iJugador + 2][this.jJugador] == '.') {
                this.matrizJuego[this.iJugador + 2][this.jJugador] = '*';
                this.matrizJuego[this.iJugador + 1][this.jJugador] = '+';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            } else if (this.matrizJuego[this.iJugador + 2][this.jJugador] == ' ') {
                this.matrizJuego[this.iJugador + 2][this.jJugador] = '$';
                this.matrizJuego[this.iJugador + 1][this.jJugador] = '+';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            }
        }
    }

    private void validarMovimientoDerecha() {
        if (this.matrizJuego[this.iJugador][this.jJugador + 1] == ' ') {
            this.matrizJuego[this.iJugador][this.jJugador + 1] = '@';
            if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                this.matrizJuego[this.iJugador][this.jJugador] = '.';
            } else {
                this.matrizJuego[this.iJugador][this.jJugador] = ' ';
            }
        } else if (this.matrizJuego[this.iJugador][this.jJugador + 1] == '.') {
            this.matrizJuego[this.iJugador][this.jJugador + 1] = '+';
            if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                this.matrizJuego[this.iJugador][this.jJugador] = '.';
            } else {
                this.matrizJuego[this.iJugador][this.jJugador] = ' ';
            }
        } else if (this.matrizJuego[this.iJugador][this.jJugador + 1] == '$') {
            if (this.matrizJuego[this.iJugador][this.jJugador + 2] == ' ') {
                this.matrizJuego[this.iJugador][this.jJugador + 2] = '$';
                this.matrizJuego[this.iJugador][this.jJugador + 1] = '@';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            } else if (this.matrizJuego[this.iJugador][this.jJugador + 2] == '.') {
                this.matrizJuego[this.iJugador][this.jJugador + 2] = '*';
                this.matrizJuego[this.iJugador][this.jJugador + 1] = '@';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            }
        } else if (this.matrizJuego[this.iJugador][this.jJugador + 1] == '*') {
            if (this.matrizJuego[this.iJugador][this.jJugador + 2] == '.') {
                this.matrizJuego[this.iJugador][this.jJugador + 2] = '*';
                this.matrizJuego[this.iJugador][this.jJugador + 1] = '+';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            } else if (this.matrizJuego[this.iJugador][this.jJugador + 2] == ' ') {
                this.matrizJuego[this.iJugador][this.jJugador + 2] = '$';
                this.matrizJuego[this.iJugador][this.jJugador + 1] = '+';
                if (this.matrizJuego[this.iJugador][this.jJugador] == '+') {
                    this.matrizJuego[this.iJugador][this.jJugador] = '.';
                } else {
                    this.matrizJuego[this.iJugador][this.jJugador] = ' ';
                }
            }
        }
    }

    private void verificarNivelGanado() {
        int objetivos = 0, jugadorSobreObjetivo = 0;
        for (int i = 0; i < this.matrizJuego.length; i++) {
            for (int j = 0; j < this.matrizJuego[i].length; j++) {
                if (this.matrizJuego[i][j] == '.') {
                    objetivos++;
                }
                if (this.matrizJuego[i][j] == '+') {
                    jugadorSobreObjetivo++;
                }
            }
        }
        if (objetivos == 0 && jugadorSobreObjetivo == 0) {
            this.nivelPasado = true;
        }
    }

    private void retrocederMovimientoAnterior() {
        for (int i = 0; i < matrizJuegoRetroceso.length; i++) {
            for (int j = 0; j < matrizJuegoRetroceso[i].length; j++) {
                matrizJuego[i][j] = matrizJuegoRetroceso[i][j];
            }
        }
    }

    private long calcularTiempo() {
        long tiempoTranscurrido = java.util.Calendar.getInstance().getTimeInMillis();
        long diferenciaTiempo = (tiempoTranscurrido - this.tiempoInicio) / 1000;
        return diferenciaTiempo;
    }

    private void mostrarResumenNivel() {
        System.out.println("Listo! Has completado el Nivel #" + this.numeroNivelActual + " en " + calcularTiempo() + " segundos y has realizado los siguientes " + listaMovimientos.size() + " movimientos:");
        for (String l : listaMovimientos) {
            System.out.print(l.toUpperCase());
        }
        System.out.println("\n");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println();

    }

}
