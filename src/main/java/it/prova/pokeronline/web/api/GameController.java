package it.prova.pokeronline.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.CreditoInsufficenteException;
import it.prova.pokeronline.web.api.exception.CreditoMinimoException;
import it.prova.pokeronline.web.api.exception.EsperienzaAccomulataNonSufficente;

@RestController
@RequestMapping("/api/gioco")
public class GameController {

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private TavoloService tavoloService;

	@PutMapping("/compraCredito/{credito}")
	@ResponseStatus(HttpStatus.OK)
	public Integer compraCredito(@PathVariable(value = "credito", required = true) Integer credito) {
		if (credito < 1) {
			throw new CreditoMinimoException("Impossibile comprare meno di 1$");
		}
		return utenteService.compraCredito(credito);
	}

	@GetMapping("/lastgame")
	@ResponseStatus(HttpStatus.OK)
	public TavoloDTO dammiLastGame() {
		Utente inSessione = utenteService
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		return TavoloDTO.buildTavoloDTOFromModelLastGame(tavoloService.lastGame(inSessione.id()));
	}

	@GetMapping("/gioca/{idTavolo}")
	public UtenteDTO giocaPartita(@PathVariable(value = "idTavolo", required = true) Long idTavolo) {

		// Eseguo il metodo che controlla se faccio parte del tavolo mandato in input,
		// se si bene senno mi inserisce
		Tavolo tavoloInstance = tavoloService.caricaSingoloElementoEager(idTavolo);
		Utente inSessione = utenteService
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

		// Controllo che l' esperienza accomulata dall' utente sia sufficente
		if (tavoloInstance.esperienzaMinima() > inSessione.esperienzaAccumulata())
			throw new EsperienzaAccomulataNonSufficente(
					"Impossibile partecipare a questa partita, la tua esperienza è troppo bassa!");

		// Controllo il credito
		if (tavoloInstance.cifraMinima() > inSessione.creditoAccumulato())
			throw new CreditoInsufficenteException(
					"Impossibile giocarte questa partita, il tuo credito è insufficente!");

		if (!tavoloInstance.utentiAlTavolo().stream().anyMatch(giocatore -> giocatore.id() == inSessione.id())) {
			// Chiamo il metodo che mi aggiunge e gioca la partita
			return UtenteDTO.buildUtenteDTOFromModel(utenteService.partecipaEGiocaPartita(inSessione, tavoloInstance));
		}

		// se faccio parte del tavolo gioco direttamente
		return UtenteDTO.buildUtenteDTOFromModel(utenteService.giocaPartita(inSessione));
	}

	@PostMapping("/abbandonaPartita/{idTavolo}")
	@ResponseStatus(HttpStatus.OK)
	public UtenteDTO abbandonaPartita(@PathVariable(value = "idTavolo", required = true) Long idTavolo) {
		return UtenteDTO.buildUtenteDTOFromModel(tavoloService.abbandonaPartita(idTavolo));
	}

}
