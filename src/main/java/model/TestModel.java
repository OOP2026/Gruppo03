package model;

/**
 * classe di utilità per testare il funzionamento base dell'oggetto utente
 */
public class TestModel {

	public static void main(String[] args) {
		Utente u = new Utente("topolino", "minni");

		// test di login fallito
		System.out.println(u.login("pippo", "pluto"));

		// test di login riuscito
		System.out.println(u.login("topolino", "minni"));
	}
}