package gui;

import controller.Controller;

// classe principale che fa partire tutto il sistema ospedaliero
public class AvvioProgramma {
    public static void main(String[] args) {
        Controller controller = new Controller();
        new AccessoFrame(controller);
    }
}