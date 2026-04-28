package model.homework1;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Ricovero {
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraDimissioniPreviste;
    private LocalDateTime dataOraDimissioneEffettuate;

    private Paziente paziente;
    private Letto letto;
    private List<Prestazione> prestazioni = new ArrayList<>();
}
