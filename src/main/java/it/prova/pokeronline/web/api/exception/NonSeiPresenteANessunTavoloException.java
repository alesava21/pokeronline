package it.prova.pokeronline.web.api.exception;

public class NonSeiPresenteANessunTavoloException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NonSeiPresenteANessunTavoloException(String message) {
		super(message);
	}

}
