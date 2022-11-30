package it.prova.pokeronline.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.CreditoMinimoException;

@RestController
@RequestMapping("/api/gioco")
public class GameController {
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private TavoloService tavoloService;
	
	@PutMapping("/compraCredito/{credito}")
	@ResponseStatus(HttpStatus.OK)
	public Integer compraCredito (@PathVariable(value = "credito", required = true)Integer credito ) {
		if (credito < 1) {
			throw new CreditoMinimoException("Impossibile comprare meno di 1$");
		}
		return utenteService.compraCredito(credito);
	}

}
