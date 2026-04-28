package model.homework1;

import java.util.List;
import java.util.ArrayList;

public class Medico extends Utente {
    private String matricola;
    private String specializzazione;

    private Reparto reparto;
    private List<Turno> turni = new ArrayList<>();
    private List<Prestazione> prestazioni = new ArrayList<>();

}