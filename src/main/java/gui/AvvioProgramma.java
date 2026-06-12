package gui;

import controller.Controller;

/**
 * classe principale che fa partire tutto il sistema ospedaliero.
 * rappresenta il punto di ingresso (entry point) dell'applicazione.
 */
public class AvvioProgramma {

    /**
     * metodo iniziale che viene eseguito appena si lancia il programma.
     * crea il controller centrale (che prepara il database) e apre la primissima finestra di login.
     *
     * @param args eventuali argomenti passati da riga di comando (non utilizzati in questa app)
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        new AccessoFrame(controller);
    }
}