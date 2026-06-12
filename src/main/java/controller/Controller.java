package controller;

import dao.*;
import implementazioneDao.*;
import model.*;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controller principale dell'applicazione.
 *
 * La GUI parla con il Controller.
 * Il Controller parla con i DAO.
 * I DAO parlano con PostgreSQL.
 */


public class Controller {

	private final UtenteDAO utenteDAO;
	private final PazienteDAO pazienteDAO;
	private final RicoveroDAO ricoveroDAO;
	private final LettoDAO lettoDAO;
	private final RepartoDAO repartoDAO;
	private final StanzaDAO stanzaDAO;

	public Controller() {
		utenteDAO = new UtentePostgresDao();
		pazienteDAO = new PazientePostgresDao();
		ricoveroDAO = new RicoveroPostgresDao();
		lettoDAO = new LettoPostgresDao();
		repartoDAO = new RepartoPostgresDao();
		stanzaDAO = new StanzaPostgresDao();

		creaDatiIniziali();
	}

	// =========================================================
	// LOGIN
	// =========================================================

	public boolean controllaAccesso(String login, String password) {
		try {
			return utenteDAO.checkCredentials(login, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// =========================================================
	// ARCHIVIO DEGENTI
	// =========================================================

	public String registraDegente(String nome, String cognome, String codiceFiscale, String dataNascitaTesto) {
		try {
			if (nome == null || nome.trim().isEmpty()) {
				return "ERRORE: inserisci il nome.";
			}

			if (cognome == null || cognome.trim().isEmpty()) {
				return "ERRORE: inserisci il cognome.";
			}

			if (codiceFiscale == null || codiceFiscale.trim().isEmpty()) {
				return "ERRORE: inserisci il codice fiscale.";
			}

			if (dataNascitaTesto == null || dataNascitaTesto.trim().isEmpty()) {
				return "ERRORE: inserisci la data di nascita.";
			}

			LocalDate dataNascita;

			try {
				dataNascita = LocalDate.parse(dataNascitaTesto.trim());
			} catch (DateTimeParseException e) {
				return "ERRORE: la data deve essere nel formato AAAA-MM-GG, esempio 2001-05-23.";
			}

			String cfPulito = codiceFiscale.trim().toUpperCase();

			Paziente esistente = pazienteDAO.findByCodiceFiscale(cfPulito);

			if (esistente != null) {
				return "ERRORE: paziente già presente nell'archivio.";
			}

			Paziente nuovo = new Paziente();
			nuovo.setNome(nome.trim());
			nuovo.setCognome(cognome.trim());
			nuovo.setCodiceFiscale(cfPulito);
			nuovo.setDataNascita(dataNascita);

			pazienteDAO.save(nuovo);

			return "OK: paziente inserito correttamente.";

		} catch (SQLException e) {
			e.printStackTrace();
			return "ERRORE: problema durante il salvataggio del paziente.";
		}
	}

	public Object[][] recuperaTabellaDegenti() {
		try {
			List<Paziente> pazienti = pazienteDAO.findAll();

			Object[][] dati = new Object[pazienti.size()][4];

			for (int i = 0; i < pazienti.size(); i++) {
				Paziente p = pazienti.get(i);

				dati[i][0] = p.getNome();
				dati[i][1] = p.getCognome();
				dati[i][2] = p.getCodiceFiscale();
				dati[i][3] = p.getDataNascita();
			}

			return dati;

		} catch (SQLException e) {
			e.printStackTrace();
			return new Object[0][0];
		}
	}

	public String[] recuperaPazientiPerCombo() {
		try {
			List<Paziente> pazienti = pazienteDAO.findAll();

			String[] dati = new String[pazienti.size()];

			for (int i = 0; i < pazienti.size(); i++) {
				Paziente p = pazienti.get(i);

				dati[i] = p.getCodiceFiscale()
						+ " - "
						+ p.getCognome()
						+ " "
						+ p.getNome();
			}

			return dati;

		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0];
		}
	}

	// =========================================================
	// REPARTI E LETTI
	// =========================================================

	public String[] recuperaRepartiPerCombo() {
		try {
			List<Reparto> reparti = repartoDAO.findAll();

			String[] dati = new String[reparti.size()];

			for (int i = 0; i < reparti.size(); i++) {
				dati[i] = reparti.get(i).getNome();
			}

			return dati;

		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0];
		}
	}

	public String[] recuperaLettiLiberiPerReparto(String nomeReparto) {
		try {
			if (nomeReparto == null || nomeReparto.trim().isEmpty()) {
				return new String[0];
			}

			List<Letto> lettiLiberi = lettoDAO.findLiberiByReparto(nomeReparto);

			String[] dati = new String[lettiLiberi.size()];

			for (int i = 0; i < lettiLiberi.size(); i++) {
				dati[i] = lettiLiberi.get(i).getCodice();
			}

			return dati;

		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0];
		}
	}

	// =========================================================
	// GESTIONE DEGENZA / RICOVERO
	// =========================================================

	public String registraDegenza(String codiceFiscale, String nomeReparto, String codiceLetto) {
		try {
			if (codiceFiscale == null || codiceFiscale.trim().isEmpty()) {
				return "ERRORE: seleziona un paziente.";
			}

			if (nomeReparto == null || nomeReparto.trim().isEmpty()) {
				return "ERRORE: seleziona un reparto.";
			}

			if (codiceLetto == null || codiceLetto.trim().isEmpty()) {
				return "ERRORE: seleziona un letto libero.";
			}

			Paziente paziente = pazienteDAO.findByCodiceFiscale(codiceFiscale);

			if (paziente == null) {
				return "ERRORE: paziente non trovato nell'archivio.";
			}

			Reparto reparto = repartoDAO.findByNome(nomeReparto);

			if (reparto == null) {
				return "ERRORE: reparto non trovato.";
			}

			Letto letto = lettoDAO.findByCodice(codiceLetto);

			if (letto == null) {
				return "ERRORE: letto non trovato.";
			}

			if (isLettoOccupato(letto)) {
				return "ERRORE: il letto selezionato è già occupato.";
			}

			Ricovero ricovero = new Ricovero();
			ricovero.setPaziente(paziente);
			ricovero.setLetto(letto);
			ricovero.setDataOraInizio(LocalDateTime.now());
			ricovero.setDataOraDimissioniPreviste(LocalDateTime.now().plusDays(7));
			ricovero.setDataOraDimissioneEffettuate(null);

			ricoveroDAO.save(ricovero);

			return "OK: ricovero registrato per "
					+ paziente.getNome()
					+ " "
					+ paziente.getCognome()
					+ " nel letto "
					+ letto.getCodice();

		} catch (SQLException e) {
			e.printStackTrace();
			return "ERRORE: problema durante il salvataggio del ricovero.";
		}
	}

	private boolean isLettoOccupato(Letto letto) {
		try {
			List<Ricovero> ricoveriAttivi = ricoveroDAO.findRicoveriAttivi();

			for (Ricovero ricovero : ricoveriAttivi) {
				if (ricovero.getLetto() != null
						&& ricovero.getLetto().getCodice() != null
						&& ricovero.getLetto().getCodice().equals(letto.getCodice())) {
					return true;
				}
			}

			return false;

		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

	// =========================================================
	// DASHBOARD
	// =========================================================

	public int contaLettiTotali() {
		try {
			return lettoDAO.findAll().size();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int contaLettiOccupati() {
		try {
			return ricoveroDAO.findRicoveriAttivi().size();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int contaLettiLiberi() {
		int liberi = contaLettiTotali() - contaLettiOccupati();

		if (liberi < 0) {
			return 0;
		}

		return liberi;
	}

	// =========================================================
	// DATI INIZIALI STRUTTURALI
	// =========================================================

	private void creaDatiIniziali() {
		try {
			creaUtentiIniziali();
			creaRepartiStanzeLettiIniziali();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void creaUtentiIniziali() throws SQLException {
		if (utenteDAO.findByLogin("daniele") == null) {
			utenteDAO.save(new Amministratore("daniele", "daniele", "Daniele"));
		}

		if (utenteDAO.findByLogin("arianna") == null) {
			utenteDAO.save(new Amministratore("arianna", "arianna", "Arianna"));
		}
	}

	private void creaRepartiStanzeLettiIniziali() throws SQLException {
		Reparto cardiologia = creaReparto("Cardiologia");
		Reparto ortopedia = creaReparto("Ortopedia");

		Stanza stanza101 = creaStanza("101", cardiologia);
		Stanza stanza102 = creaStanza("102", cardiologia);
		Stanza stanza201 = creaStanza("201", ortopedia);
		Stanza stanza202 = creaStanza("202", ortopedia);

		creaLetto("L101A", stanza101);
		creaLetto("L101B", stanza101);
		creaLetto("L102A", stanza102);
		creaLetto("L102B", stanza102);

		creaLetto("L201A", stanza201);
		creaLetto("L201B", stanza201);
		creaLetto("L202A", stanza202);
		creaLetto("L202B", stanza202);
	}

	private Reparto creaReparto(String nome) throws SQLException {
		Reparto reparto = repartoDAO.findByNome(nome);

		if (reparto != null) {
			return reparto;
		}

		reparto = new Reparto();
		reparto.setNome(nome);

		repartoDAO.save(reparto);

		return reparto;
	}

	private Stanza creaStanza(String nome, Reparto reparto) throws SQLException {
		Stanza stanza = new Stanza();
		stanza.setNome(nome);
		stanza.setReparto(reparto);

		stanzaDAO.save(stanza);

		return stanza;
	}

	private void creaLetto(String codice, Stanza stanza) throws SQLException {
		if (lettoDAO.findByCodice(codice) != null) {
			return;
		}

		Letto letto = new Letto();
		letto.setCodice(codice);
		letto.setStanza(stanza);

		lettoDAO.save(letto);
	}
}