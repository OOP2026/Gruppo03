package model;

/**
 * classe di utilità per testare il funzionamento base dell'oggetto utente.
 */
public class TestModel {

	public static void main(String[] args) {

		// creazione di un utente di prova con credenziali fittizie
		Utente u = new Utente("topolino", "minni");

		// test di simulazione per un tentativo di accesso con credenziali errate
		System.out.println(u.login("pippo", "pluto"));

		// test di simulazione per un accesso con le credenziali corrette
		System.out.println(u.login("topolino", "minni"));
	}
}