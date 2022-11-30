package it.prova.pokeronline.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.GiocatoriPresentiAlTavoloException;
import it.prova.pokeronline.web.api.exception.IdNotNullForInsertException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UtenteConCreazioneNotFoundException;

@RestController
@RequestMapping("api/tavolo")
public class TavoloController {

	@Autowired
	private TavoloService tavoloService;

	@Autowired
	private UtenteService utenteService;

	@GetMapping
	public List<TavoloDTO> getAll() {
		return TavoloDTO.createListDTOFromModel(tavoloService.listAll());
	}

	@GetMapping("/{id}")
	public TavoloDTO findById(@PathVariable(required = true) Long id) {
		Tavolo tavoloDaCaricare = tavoloService.caricaSingoloElementoEager(id);
		if (tavoloDaCaricare == null)
			throw new TavoloNotFoundException("Non e stato possibile trovate un Tavolo con id:" + id);

		return TavoloDTO.buildTavoloDTOFromModel(tavoloDaCaricare);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TavoloDTO createNew(@Valid @RequestBody TavoloDTO tavoloInput) {
		if (tavoloInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
		/**
		 * Controllo che l utenteCreazione sia != null se cosi lancio eccezione, perche
		 * non si può decidere per chi creare il tavolo, L utente Creazione viene
		 * settato da sistema con l utente in sessione.
		 */
		if (tavoloInput.getUtenteCheCreaIlTavolo() != null)
			throw new UtenteConCreazioneNotFoundException(
					"Non è ammesso fornire l' UtenteCreazione, impossibile Creare un tavolo per un Utente Specifico");

		// Carico l UtenteCreazione dentro il tavolo con l' utente in sessione
		tavoloInput.setUtenteCheCreaIlTavolo(UtenteDTO.buildUtenteDTOFromModelNoPassword(
				utenteService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())));
		Tavolo tavoloInserito = tavoloService.inserisciNuovo(tavoloInput.buildTavoloModel());

		return TavoloDTO.buildTavoloDTOFromModel(tavoloInserito);
	}

	@PutMapping
	public TavoloDTO update(@RequestBody TavoloDTO tavolo) {
		if (tavolo.getId() == null)
			throw new IdNotNullForInsertException("impossibile aggiornare un record se non si inserisce un id");

		Tavolo tavoloDaAggiornare = tavoloService.caricaSingoloElementoEager(tavolo.getId());
		System.out.println(tavoloDaAggiornare);
		if (tavoloDaAggiornare == null)
			throw new TavoloNotFoundException("tavolo non trovato");

		return TavoloDTO.buildTavoloDTOFromModel(tavoloDaAggiornare);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {
		Tavolo tavoloDaEliminare = tavoloService.caricaSingoloElementoEager(id);
		if (tavoloDaEliminare == null)
			throw new TavoloNotFoundException("tavolo non trovato");
		if (tavoloDaEliminare.utentiAlTavolo().size() != 0)
			throw new GiocatoriPresentiAlTavoloException("impossibile eliminare un tavolo con giocatori presenti");

		tavoloService.rimuovi(id);
	}

	@PostMapping("/search")
	public List<TavoloDTO> search(@RequestBody TavoloDTO example) {
		Utente inSessione = utenteService
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if (inSessione.ruoli().stream().anyMatch(ruolo -> ruolo.codice().equals(Ruolo.ROLE_ADMIN)))
			return TavoloDTO.createListDTOFromModel(tavoloService.findByExample(example.buildTavoloModel()));

		// Se Sono uno Special Player:
		// NoN ci deve essere l' UtenteCreazione all' interno dell' example.
		if (example.getUtenteCheCreaIlTavolo() != null)
			throw new UtenteConCreazioneNotFoundException(
					"All interno del tavolo Example è presente un UtenteCreazione, Impossibile procedere, rimuovi l' utenteCreazione dall' example");

		return TavoloDTO.createListDTOFromModel(
				tavoloService.findByExampleSpecialPlayer(example.buildTavoloModel(), inSessione.id()));
	}

}
