package it.prova.pokeronline.web.api.exception;

public class CreditoMinimoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CreditoMinimoException(String message) {
		super(message);
	}
}
