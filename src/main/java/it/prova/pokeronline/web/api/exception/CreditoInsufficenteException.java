package it.prova.pokeronline.web.api.exception;

public class CreditoInsufficenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CreditoInsufficenteException(String message) {
		super(message);
	}

}
