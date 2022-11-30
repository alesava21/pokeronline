package it.prova.pokeronline.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;
import it.prova.pokeronline.repository.utente.UtenteRepository;
import it.prova.pokeronline.web.api.exception.GiocatoriPresentiAlTavoloException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@Service
@Transactional(readOnly = true)
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository tavoloRepository;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private UtenteRepository utenteRepository;

	@Override
	public List<Tavolo> listAllElements(boolean eager) {
		if (eager)
			return tavoloRepository.findAllEager();
		return (List<Tavolo>) tavoloRepository.findAll();
	}

	@Override
	public Tavolo caricaSingoloElemento(Long id) {
		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(roleItem -> roleItem.getAuthority().equals(Ruolo.ROLE_SPECIAL_USER))) {
			return tavoloRepository.findByIdSpecialPlayer(id, utenteRepository
					.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get().id())
					.orElse(null);

		}
		return tavoloRepository.findById(id).orElse(null);
	}

	@Override
	public Tavolo caricaSingoloElementoEager(Long id) {
		return tavoloRepository.findByIdEager(id);
	}

	@Override
	@Transactional
	public Tavolo aggiorna(Tavolo tavoloInstance, Tavolo tavoloCaricatoDalDB) {
		if (tavoloCaricatoDalDB.utentiAlTavolo().size() > 0)
			throw new GiocatoriPresentiAlTavoloException(
					"Impossibile eliminare questo Tavolo, sono ancora presenti dei giocatori al suo interno");

		return tavoloRepository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
		if (tavoloInstance.dataCreazione() == null) {
			tavoloInstance.dataCreazione(LocalDate.now());
		}
		return tavoloRepository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) {
		tavoloRepository.deleteById(idToRemove);
	}

	@Override
	public List<Tavolo> findByDenominazione(String denominazione) {
		return tavoloRepository.findByDenominazione(denominazione);
	}

	@Override
	public List<Tavolo> listAll() {
		return (List<Tavolo>) tavoloRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> findByExample(Tavolo example) {
		return tavoloRepository.findByExample(example);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> findByExampleSpecialPlayer(Tavolo example, Long id) {
		return tavoloRepository.findByExampleSpecialPlayer(example, id);
	}

}
