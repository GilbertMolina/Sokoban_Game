package sokoban.logica.sokoban;

import javax.swing.JOptionPane;

public class MensajesInicioFin {
    
    public void bienvenidaJuego(){
        JOptionPane.showMessageDialog(null, "Bienvenido al juego de Sokoban", "Sokoban", JOptionPane.INFORMATION_MESSAGE);
        String mensajeInicial = "  El juego Sokoban se compone de los siguientes caracteres:\n" + 
                                "  ' '   : Esto es un espacio en blanco, esto representa el piso caminable.\n" + 
                                "  @  : El jugador.\n" + 
                                "  +    : El jugador estando sobre un punto clave.\n" + 
                                "  #    : Una pared.\n" + 
                                "  $    : Una caja.\n" + 
                                "  .     : Un punto clave.\n" + 
                                "  *    : Una caja sobre un punto clave.\n\n" + 
                                "  Para poder mover el jugador presionar las siguientes teclas:\n" + 
                                "  S   : Abajo.\n" + 
                                "  W  : Arriba.\n" + 
                                "  A   : Izquieda.\n" + 
                                "  D   : Derecha.\n\n" + 
                                "  Ganar el juego:\n" + 
                                "  El mapa o nivel se considera como vencido si y solo si, todos los puntos claves han sido cubiertos con cajas.\n\n";
        
        JOptionPane.showMessageDialog(null, mensajeInicial, "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "El juego será mostrado por medio de la consola enseguida.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("====================================================");
        System.out.println("|                  JUEGO SOKOBAN                   |");
        System.out.println("====================================================\n");
    }
    
    public void despedidaFinJuego(){
        System.out.println("¡Felicidades! Has concluido todos los niveles.");
        System.out.println("\n====================================================");
        System.out.println("|                 JUEGO FINALIZADO                 |");
        System.out.println("====================================================\n");
        JOptionPane.showMessageDialog(null, "Gracias por haber jugado el juego Sokoban", "Sokoban", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void despedidaFinJuegoPorUsuario(){
        System.out.println("\n====================================================");
        System.out.println("|           JUEGO FINALIZADO POR USUARIO           |");
        System.out.println("====================================================\n");
        JOptionPane.showMessageDialog(null, "Gracias por haber jugado el juego Sokoban", "Sokoban", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
