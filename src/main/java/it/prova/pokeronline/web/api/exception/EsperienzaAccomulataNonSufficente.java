package it.prova.pokeronline.web.api.exception;

public class EsperienzaAccomulataNonSufficente extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EsperienzaAccomulataNonSufficente(String message) {
		super(message);
	}

}
