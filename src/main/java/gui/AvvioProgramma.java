package gui;

import controller.Controller;

public class AvvioProgramma {
    public static void main(String[] args) {
        Controller controller = new Controller();
        new AccessoFrame(controller);
    }
}