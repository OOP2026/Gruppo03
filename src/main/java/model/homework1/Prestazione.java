package model.homework1;

import java.time.LocalDateTime;

public class Prestazione {
    private String esito;
    private TipoPrestazione tipo;
    private LocalDateTime dataOraInizio;
    private float durata;

    private Ricovero ricovero;
    private Medico medico;
}
