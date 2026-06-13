package gui;

import controller.Controller;

/**
 * classe principale che fa partire tutto il sistema ospedaliero.
 * rappresenta il punto di ingresso dell'applicazione.
 */
public class AvvioProgramma {

    /**
     * metodo iniziale che viene eseguito appena si lancia il programma.
     * crea il controller centrale che prepara il database e apre la primissima finestra di login.
     *
     * @param args eventuali argomenti passati da riga di comando
     */
    public static void main(String[] args) {

        // inizializza il nucleo logico dell'applicazione e stabilisce la connessione iniziale con il database
        Controller controller = new Controller();

        // crea e mostra a schermo l'interfaccia grafica di base per l'autenticazione degli utenti
        new AccessoFrame(controller);
    }
}