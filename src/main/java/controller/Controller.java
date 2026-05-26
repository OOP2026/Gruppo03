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
 * unico controllore dell'applicazione.
 * qui dentro c'è tutta la logica: gestione degenti, degenze, login.
 * seguiamo l'architettura bce, quindi questo è il control.
 */
public class Controller {

	// queste liste fanno le veci del database per ora.
	// tengono i dati in memoria mentre il programma è aperto.
	private List<Paziente> pazienti;   //lista di tutti i pazienti (degenti)
	private List<Ricovero> ricoveri;   // lista di tutti i ricoveri (degenze)
	private List<Utente> utenti;       // utenti che possono fare login (amministratori, medici...)
	private List<Reparto> reparti;     //reparti dell'ospedale
	private List<Letto> letti;         // tutti i lettipresenti

	/*
	 * costruttore del controller.
	 * inizializza le liste vuote e poi chiama il metodo che crea dati di esempio.
	 */
	public Controller() {
		pazienti = new ArrayList<>();
		ricoveri = new ArrayList<>();
		utenti = new ArrayList<>();
		reparti = new ArrayList<>();
		letti = new ArrayList<>();
		creaDati();   // popola le liste con valori fittizi (amministratori, pazienti, letti)
	}

	// ---------- metodi per il login ----------

	/**
	 * funzionalità:
	 * verifica se le credenziali (login e password) corrispondono a qualche utente nella lista.
	 * scorre tutti gli utenti e chiama il metodo login() di ciascuno.
	 * se trova una corrispondenza, restituisce true, altrimenti false.
	 */


	public boolean controllaAccesso(String login, String password) {
		for (Utente u : utenti) {
			if (u.login(login, password)) return true;
		}
		return false;
	}

	// ---------- metodi per la gestione dei pazienti (degenti) ----------

	/**
	 * funzionalità presenti:
	 * registra un nuovo paziente (degente) nel sistema.
	 * prima controlla se esiste già un paziente con lo stesso codice fiscale.
	 * se non esiste, crea un nuovo oggetto Paziente, lo riempie con i dati e lo aggiunge alla lista.
	 */
	public void registraDegente(String nome, String cognome, String documento) {


		//controllo se esiste già un paziente con lo stesso codice fiscale
		boolean esiste = pazienti.stream()
				.anyMatch(p -> p.getCodiceFiscale().equals(documento));
		if (esiste) {
			System.err.println("paziente con cf " + documento + " già presente. non lo reinserisco.");
			return;
		}
		//creo un nuovo paziente usando il costruttore vuoto e i setter
		Paziente nuovo = new Paziente();
		nuovo.setNome(nome);
		nuovo.setCognome(cognome);
		nuovo.setCodiceFiscale(documento);
		pazienti.add(nuovo);
	}

	/**
	 * restituisce una matrice di oggetti (object[][]) con i dati di tutti i pazienti.
	 * ogni riga della matrice contiene { nome, cognome, codiceFiscale }.
	 * serve per riempire la tabella nella finestra archivio degenti.
	 */
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

	// ---------- metodi per la gestione dei ricoveri (degenze) ----------

	/**
	 * funzionalità presenti:
	 * assegna una degenza (ricovero) a un paziente esistente.
	 * cerca il paziente tramite il codice fiscale.
	 * poi cerca (o crea) il reparto indicato.
	 * infine cerca un letto libero: se l'utente ha specificato un codice letto, prova quello;
	 * altrimenti cerca automaticamente nel reparto.
	 * se trova tutto, crea un nuovo ricovero con data inizio = oggi e dimissioni previste tra 7 giorni.
	 */

	public void registraDegenza(String documento, String settore, String posto) {

		//trovo il paziente a partire dal codice fiscale
		Paziente paziente = pazienti.stream()
				.filter(p -> p.getCodiceFiscale().equals(documento))
				.findFirst()
				.orElse(null);
		if (paziente == null) {
			System.err.println("paziente non trovato: " + documento);
			return;
		}

		//trovo il reparto (se non esiste lo creo subito)
		Reparto reparto = reparti.stream()
				.filter(r -> r.getNome().equalsIgnoreCase(settore))
				.findFirst()
				.orElse(null);
		if (reparto == null) {
			reparto = new Reparto();
			reparto.setNome(settore);
			reparti.add(reparto);
		}

		//cerco un letto libero
		Letto lettoScelto = null;
		//se l'utente ha specificato un posto, provo a prendere proprio quel letto
		if (posto != null && !posto.isEmpty()) {
			lettoScelto = letti.stream()
					.filter(l -> l.getCodice().equals(posto) && !isLettoOccupato(l))
					.findFirst()
					.orElse(null);
			if (lettoScelto == null) {
				System.err.println("letto specificato " + posto + " non esiste o è occupato, ne cerco un altro.");
			}
		}

		// se non ho ancora un letto (perché non specificato o perché occupato), ne cerco uno libero nel reparto
		if (lettoScelto == null) {
			lettoScelto = cercaLettoLiberoInReparto(reparto);
		}

		if (lettoScelto == null) {
			System.err.println("nessun letto disponibile nel reparto " + settore);
			return;
		}

		//creo il ricovero e lo aggiungo alla lista
		Ricovero ricovero = new Ricovero();
		ricovero.setPaziente(paziente);
		ricovero.setLetto(lettoScelto);
		ricovero.setDataOraInizio(LocalDateTime.now());               //data e ora attuali
		ricovero.setDataOraDimissioniPreviste(LocalDateTime.now().plusDays(7)); // dimissioni previste tra una settimana
		ricoveri.add(ricovero);
		System.out.println("ricovero creato per " + paziente.getNome() + " " + paziente.getCognome() +
				" nel letto " + lettoScelto.getCodice());
	}

	/**
	 * funzionalità:
	 * verifica se un letto è attualmente occupato.
	 * un letto è occupato se esiste almeno un ricovero che lo usa e che non ha ancora una data di dimissione effettiva.
	 *
	 */
	private boolean isLettoOccupato(Letto letto) {
		return ricoveri.stream()
				.anyMatch(r -> r.getLetto() != null &&
						r.getLetto().equals(letto) &&
						r.getDataOraDimissioneEffettuate() == null);
	}

	/**
	 * funzionalità:
	 * cerca il primo letto libero all'interno di un reparto.
	 * scorre tutte le stanze del reparto e tutti i letti di ogni stanza.
	 * appena trova un letto che non è occupato, lo restituisce.
	 *
	 * @param reparto il reparto in cui cercare
	 * @return il primo letto libero trovato, oppure null se nessuno è libero
	 */
	private Letto cercaLettoLiberoInReparto(Reparto reparto) {
		for (Stanza stanza : reparto.getStanze()) {
			for (Letto letto : stanza.getLetti()) {
				if (!isLettoOccupato(letto)) return letto;
			}
		}
		return null;
	}

	/**
	 * metodo privato che crea dati di esempio per far partire l'applicazione con informazioni già pronte.
	 * qui creiamo due amministratori (arianna e daniele), due pazienti di esempio,
	 */
	private void creaDati() {
		// ---- amministratori ----
		// arianna: login "arianna", password "arianna", nome visualizzato "Arianna"
		Amministratore arianna = new Amministratore("arianna", "arianna", "Arianna");
		utenti.add(arianna);

		// daniele: login "daniele", password "daniele", nome visualizzato "Daniele"
		Amministratore daniele = new Amministratore("daniele", "daniele", "Daniele");
		utenti.add(daniele);

		// ---- pazienti di esempio ----
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

		// ---- reparto cardiologia con una stanza e due letti ----
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