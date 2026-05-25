package controller;

import model.Paziente;
import model.Ricovero;
import model.Letto;
import model.Stanza;
import model.Reparto;
import model.Utente;
import model.Amministratore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Unico controllore dell'applicazione.
 * Contiene tutta la logica e i dati temporanei.
 */
public class Controller {

	private List<Paziente> pazienti;
	private List<Ricovero> ricoveri;
	private List<Utente> utenti;
	private List<Reparto> reparti;
	private List<Letto> letti;

	public Controller() {
		pazienti = new ArrayList<>();
		ricoveri = new ArrayList<>();
		utenti = new ArrayList<>();
		reparti = new ArrayList<>();
		letti = new ArrayList<>();
		creaDati();
	}

	// ------ LOGIN ------
	public boolean controllaAccesso(String login, String password) {
		for (Utente u : utenti) {
			if (u.login(login, password)) return true;
		}
		return false;
	}

	// ------ PAZIENTI (degenti) ------
	public void registraDegente(String nome, String cognome, String documento) {
		boolean esiste = pazienti.stream()
				.anyMatch(p -> p.getCodiceFiscale().equals(documento));
		if (esiste) {
			System.err.println("Paziente con CF " + documento + " già presente.");
			return;
		}
		Paziente nuovo = new Paziente();
		nuovo.setNome(nome);
		nuovo.setCognome(cognome);
		nuovo.setCodiceFiscale(documento);
		pazienti.add(nuovo);
	}

	public Object[][] recuperaTabellaDegenti() {
		Object[][] dati = new Object[pazienti.size()][3];
		for (int i = 0; i < pazienti.size(); i++) {
			Paziente p = pazienti.get(i);
			dati[i][0] = p.getNome();
			dati[i][1] = p.getCognome();
			dati[i][2] = p.getCodiceFiscale();
		}
		return dati;
	}

	// ------ RICOVERI (degenze) ------
	public void registraDegenza(String documento, String settore, String posto) {
		Paziente paziente = pazienti.stream()
				.filter(p -> p.getCodiceFiscale().equals(documento))
				.findFirst()
				.orElse(null);
		if (paziente == null) {
			System.err.println("Paziente non trovato: " + documento);
			return;
		}

		Reparto reparto = reparti.stream()
				.filter(r -> r.getNome().equalsIgnoreCase(settore))
				.findFirst()
				.orElse(null);
		if (reparto == null) {
			reparto = new Reparto();
			reparto.setNome(settore);
			reparti.add(reparto);
		}

		Letto lettoScelto = null;
		if (posto != null && !posto.isEmpty()) {
			lettoScelto = letti.stream()
					.filter(l -> l.getCodice().equals(posto) && !isLettoOccupato(l))
					.findFirst()
					.orElse(null);
		}
		if (lettoScelto == null) {
			lettoScelto = cercaLettoLiberoInReparto(reparto);
		}

		if (lettoScelto == null) {
			System.err.println("Nessun letto disponibile nel reparto " + settore);
			return;
		}

		Ricovero ricovero = new Ricovero();
		ricovero.setPaziente(paziente);
		ricovero.setLetto(lettoScelto);
		ricovero.setDataOraInizio(LocalDateTime.now());
		ricovero.setDataOraDimissioniPreviste(LocalDateTime.now().plusDays(7));
		ricoveri.add(ricovero);
		System.out.println("Ricovero creato per " + paziente.getNome() + " " + paziente.getCognome());
	}

	private boolean isLettoOccupato(Letto letto) {
		return ricoveri.stream()
				.anyMatch(r -> r.getLetto() != null &&
						r.getLetto().equals(letto) &&
						r.getDataOraDimissioneEffettuate() == null);
	}

	private Letto cercaLettoLiberoInReparto(Reparto reparto) {
		for (Stanza stanza : reparto.getStanze()) {
			for (Letto letto : stanza.getLetti()) {
				if (!isLettoOccupato(letto)) return letto;
			}
		}
		return null;
	}

	private void creaDati() {
		// ARIANNA
		Amministratore arianna = new Amministratore("arianna", "arianna", "Arianna");
		utenti.add(arianna);

		// DANIELE
		Amministratore daniele = new Amministratore("daniele", "daniele", "Daniele");
		utenti.add(daniele);

		// PAZIENTI DI ESEMPIO
		Paziente p1 = new Paziente();
		p1.setNome("Mario");
		p1.setCognome("Rossi");
		p1.setCodiceFiscale("RSSMRA80A01H501U");
		pazienti.add(p1);

		Paziente p2 = new Paziente();
		p2.setNome("Luisa");
		p2.setCognome("Bianchi");
		p2.setCodiceFiscale("BNCLSU85B43F205X");
		pazienti.add(p2);

		// REPARTO E LETTI
		Reparto cardio = new Reparto();
		cardio.setNome("Cardiologia");
		reparti.add(cardio);

		Stanza stanza101 = new Stanza();
		stanza101.setNome("101");
		cardio.getStanze().add(stanza101);

		Letto letto1 = new Letto();
		letto1.setCodice("L101A");
		letto1.setStanza(stanza101);
		stanza101.getLetti().add(letto1);
		letti.add(letto1);

		Letto letto2 = new Letto();
		letto2.setCodice("L101B");
		letto2.setStanza(stanza101);
		stanza101.getLetti().add(letto2);
		letti.add(letto2);
	}
}