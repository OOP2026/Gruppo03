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
 * controller principale dell'app.
 * fa da ponte tra l'interfaccia grafica e il database.
 * gestisce tutta la logica di base come i salvataggi e i controlli.
 */
public class Controller {

	private final UtenteDAO utenteDAO;
	private final PazienteDAO pazienteDAO;
	private final RicoveroDAO ricoveroDAO;
	private final LettoDAO lettoDAO;
	private final RepartoDAO repartoDAO;
	private final StanzaDAO stanzaDAO;

	/**
	 * crea il controller e prepara tutti i dao.
	 * alla fine chiama il metodo per creare i dati finti se il db è vuoto.
	 */
	public Controller() {
		// inizializzazione delle implementazioni concrete per comunicare con postgresql
		utenteDAO = new UtentePostgresDao();
		pazienteDAO = new PazientePostgresDao();
		ricoveroDAO = new RicoveroPostgresDao();
		lettoDAO = new LettoPostgresDao();
		repartoDAO = new RepartoPostgresDao();
		stanzaDAO = new StanzaPostgresDao();

		// popola il database con i dati di default se è la prima esecuzione
		creaDatiIniziali();
	}

	// =========================================================
	// LOGIN
	// =========================================================

	/**
	 * controlla se username e password sono giusti per fare il login.
	 *
	 * @param login l'username inserito
	 * @param password la password inserita
	 * @return true se i dati sono corretti, sennò false
	 */
	public boolean controllaAccesso(String login, String password) {
		try {
			// delega il controllo delle credenziali direttamente al dao
			return utenteDAO.checkCredentials(login, password);
		} catch (SQLException e) {
			// in caso di errore di connessione al db blocca l'accesso per sicurezza
			e.printStackTrace();
			return false;
		}
	}

	// =========================================================
	// ARCHIVIO DEGENTI
	// =========================================================

	/**
	 * controlla che i dati siano tutti compilati e poi salva il nuovo paziente.
	 *
	 * @param nome nome del paziente
	 * @param cognome cognome del paziente
	 * @param codiceFiscale codice fiscale
	 * @param dataNascitaTesto data di nascita scritta come testo
	 * @return messaggio di ok o l'errore da mostrare a schermo
	 */
	public String registraDegente(String nome, String cognome, String codiceFiscale, String dataNascitaTesto) {
		try {
			// validazione degli input di base per evitare campi vuoti
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

			// conversione e validazione della data di nascita
			LocalDate dataNascita;
			try {
				dataNascita = LocalDate.parse(dataNascitaTesto.trim());
			} catch (DateTimeParseException e) {
				return "ERRORE: la data deve essere nel formato corretto.";
			}

			// normalizza il codice fiscale in maiuscolo per evitare duplicati causati dal case
			String cfPulito = codiceFiscale.trim().toUpperCase();

			// controllo duplicati nel database
			Paziente esistente = pazienteDAO.findByCodiceFiscale(cfPulito);
			if (esistente != null) {
				return "ERRORE: paziente già presente nell'archivio.";
			}

			// creazione e salvataggio della nuova entità
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

	/**
	 * prende tutti i pazienti e li mette in una matrice per farli vedere nella tabella.
	 *
	 * @return matrice con i dati dei pazienti
	 */
	public Object[][] recuperaTabellaDegenti() {
		try {
			List<Paziente> pazienti = pazienteDAO.findAll();

			// inizializza una matrice adatta per essere inserita in un jtable
			Object[][] dati = new Object[pazienti.size()][4];

			// mappa le proprietà dell'oggetto sulle colonne della riga
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
			// ritorna matrice vuota in caso di errore per non far crashare la gui
			return new Object[0][0];
		}
	}

	/**
	 * prende i pazienti per metterli nei menu a tendina.
	 *
	 * @return array di stringhe con codice fiscale, cognome e nome
	 */
	public String[] recuperaPazientiPerCombo() {
		try {
			List<Paziente> pazienti = pazienteDAO.findAll();
			String[] dati = new String[pazienti.size()];

			// formatta la stringa per facilitare la lettura all'utente nella combobox
			for (int i = 0; i < pazienti.size(); i++) {
				Paziente p = pazienti.get(i);
				dati[i] = p.getCodiceFiscale() + " - " + p.getCognome() + " " + p.getNome();
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

	/**
	 * prende i nomi dei reparti per i menu a tendina.
	 *
	 * @return array con i nomi dei reparti
	 */
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

	/**
	 * cerca i letti vuoti in un reparto specifico.
	 *
	 * @param nomeReparto il reparto da controllare
	 * @return array con i codici dei letti liberi
	 */
	public String[] recuperaLettiLiberiPerReparto(String nomeReparto) {
		try {
			if (nomeReparto == null || nomeReparto.trim().isEmpty()) {
				return new String[0];
			}

			// usa il metodo specifico del dao per filtrare lato database
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

	/**
	 * fa i controlli e poi salva un nuovo ricovero associando il paziente al letto.
	 *
	 * @param codiceFiscale codice fiscale del paziente
	 * @param nomeReparto reparto scelto
	 * @param codiceLetto letto scelto
	 * @return messaggio di successo o di errore
	 */
	public String registraDegenza(String codiceFiscale, String nomeReparto, String codiceLetto) {
		try {
			// verifica la presenza dei parametri necessari
			if (codiceFiscale == null || codiceFiscale.trim().isEmpty()) {
				return "ERRORE: seleziona un paziente.";
			}
			if (nomeReparto == null || nomeReparto.trim().isEmpty()) {
				return "ERRORE: seleziona un reparto.";
			}
			if (codiceLetto == null || codiceLetto.trim().isEmpty()) {
				return "ERRORE: seleziona un letto libero.";
			}

			// recupera e verifica le entità coinvolte
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

			// verifica finale sulla disponibilità del letto per sicurezza
			if (isLettoOccupato(letto)) {
				return "ERRORE: il letto selezionato è già occupato.";
			}

			// assemblaggio del nuovo ricovero
			Ricovero ricovero = new Ricovero();
			ricovero.setPaziente(paziente);
			ricovero.setLetto(letto);
			// registra il momento esatto dell'ammissione
			ricovero.setDataOraInizio(LocalDateTime.now());
			// stima base di 7 giorni
			ricovero.setDataOraDimissioniPreviste(LocalDateTime.now().plusDays(7));
			// vuoto finché non viene dimesso
			ricovero.setDataOraDimissioneEffettuate(null);

			// salvataggio definitivo
			ricoveroDAO.save(ricovero);

			return "OK: ricovero registrato per " + paziente.getNome() + " " + paziente.getCognome() + " nel letto " + letto.getCodice();

		} catch (SQLException e) {
			e.printStackTrace();
			return "ERRORE: problema durante il salvataggio del ricovero.";
		}
	}

	/**
	 * controlla se un letto è già preso da qualche ricovero attivo.
	 *
	 * @param letto il letto da verificare
	 * @return true se è occupato, false se è libero
	 */
	private boolean isLettoOccupato(Letto letto) {
		try {
			List<Ricovero> ricoveriAttivi = ricoveroDAO.findRicoveriAttivi();

			// scorre i ricoveri correnti per vedere se il letto richiesto è in uso
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
			// in caso di errore di connessione assume che il letto sia occupato per evitare sovrascritture
			return true;
		}
	}

	// =========================================================
	// DASHBOARD
	// =========================================================

	/**
	 * conta quanti letti ci sono in totale in ospedale.
	 *
	 * @return numero totale dei letti
	 */
	public int contaLettiTotali() {
		try {
			return lettoDAO.findAll().size();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * conta quanti letti sono usati al momento.
	 *
	 * @return numero di letti occupati
	 */
	public int contaLettiOccupati() {
		try {
			return ricoveroDAO.findRicoveriAttivi().size();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * fa la sottrazione per capire quanti posti ci sono ancora.
	 *
	 * @return numero di letti liberi
	 */
	public int contaLettiLiberi() {
		// calcola la disponibilità derivandola dai totali per mantenere consistenza nei dati
		int liberi = contaLettiTotali() - contaLettiOccupati();
		// assicura che il numero non sia mai negativo
		return Math.max(liberi, 0);
	}

	// =========================================================
	// DATI INIZIALI STRUTTURALI
	// =========================================================

	/**
	 * crea utenti e reparti base se il database è appena stato creato.
	 */
	private void creaDatiIniziali() {
		try {
			creaUtentiIniziali();
			creaRepartiStanzeLettiIniziali();
		} catch (SQLException e) {
			// registra eventuali errori nella console del server
			e.printStackTrace();
		}
	}

	/**
	 * crea un paio di admin di default se non ci sono.
	 *
	 * @throws SQLException se salta il database
	 */
	private void creaUtentiIniziali() throws SQLException {
		// permette di avere subito accessi validi per testare la gui
		if (utenteDAO.findByLogin("daniele") == null) {
			utenteDAO.save(new Amministratore("daniele", "daniele", "Daniele"));
		}
		if (utenteDAO.findByLogin("arianna") == null) {
			utenteDAO.save(new Amministratore("arianna", "arianna", "Arianna"));
		}
	}

	/**
	 * crea la struttura base dell'ospedale con reparti, stanze e letti.
	 *
	 * @throws SQLException se c'è un problema di salvataggio
	 */
	private void creaRepartiStanzeLettiIniziali() throws SQLException {
		Reparto cardiologia = creaReparto("Cardiologia");
		Reparto ortopedia = creaReparto("Ortopedia");

		Stanza stanza101 = creaStanza("101", cardiologia);
		Stanza stanza102 = creaStanza("102", cardiologia);
		Stanza stanza201 = creaStanza("201", ortopedia);
		Stanza stanza202 = creaStanza("202", ortopedia);

		// popola le stanze con i posti letto
		creaLetto("L101A", stanza101);
		creaLetto("L101B", stanza101);
		creaLetto("L102A", stanza102);
		creaLetto("L102B", stanza102);

		creaLetto("L201A", stanza201);
		creaLetto("L201B", stanza201);
		creaLetto("L202A", stanza202);
		creaLetto("L202B", stanza202);
	}

	/**
	 * crea un reparto nuovo o lo recupera se c'è già.
	 *
	 * @param nome nome del reparto
	 * @return il reparto
	 * @throws SQLException errore db
	 */
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

	/**
	 * crea una stanza e la collega al suo reparto.
	 *
	 * @param nome numero o nome della stanza
	 * @param reparto reparto in cui sta la stanza
	 * @return la stanza creata
	 * @throws SQLException errore db
	 */
	private Stanza creaStanza(String nome, Reparto reparto) throws SQLException {
		Stanza stanza = new Stanza();
		stanza.setNome(nome);
		stanza.setReparto(reparto);
		stanzaDAO.save(stanza);
		return stanza;
	}

	/**
	 * crea un letto e lo piazza in una stanza se non esiste già.
	 *
	 * @param codice codice del letto
	 * @param stanza stanza di appartenenza
	 * @throws SQLException errore db
	 */
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