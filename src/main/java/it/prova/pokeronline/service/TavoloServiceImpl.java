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
public class TavoloServiceImpl implements TavoloService{

	@Autowired
	private TavoloRepository repository;
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	@Override
	public List<Tavolo> listAll() {
		return (List<Tavolo>)repository.findAll();
	}

	@Override
	public Tavolo caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Tavolo tavoloInstance) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessionUtente = utenteRepository.findByUsername(username).orElse(null);
		Tavolo tavoloDaEliminare = caricaSingoloElementoEager(tavoloInstance.id());
		if(utenteInSessionUtente.ruoli().stream().anyMatch(r -> r.codice().equals(Ruolo.ROLE_SPECIAL_USER))
				&& !(utenteInSessionUtente.id() == tavoloDaEliminare.utenteCheCreaIlTavolo().id()))
			throw new TavoloNotFoundException("impossibile aggiornare un tavolo non creato da te se hai ruolo special player");
		
		repository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Tavolo tavoloInstance) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessionUtente = utenteRepository.findByUsername(username).orElse(null);
		tavoloInstance.utenteCheCreaIlTavolo(utenteInSessionUtente);
		
		tavoloInstance.dataCreazione(LocalDate.now());
		if(tavoloInstance.cifraMinima() == null)
			tavoloInstance.cifraMinima();
		repository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessionUtente = utenteRepository.findByUsername(username).orElse(null);
		Tavolo tavoloDaEliminare = caricaSingoloElementoEager(idToRemove);
		if(utenteInSessionUtente.ruoli().stream().anyMatch(r -> r.codice().equals(Ruolo.ROLE_SPECIAL_USER))
				&& !(utenteInSessionUtente.id() == tavoloDaEliminare.utenteCheCreaIlTavolo().id()))
			throw new TavoloNotFoundException("impossibile eliminare un tavolo non creato da te se hai ruolo special player");
			
		repository.deleteById(idToRemove);
	}

	@Override
	public Tavolo caricaSingoloElementoEager(Long id) {
		return repository.findIdByEager(id);
	}

	@Override
	public List<Tavolo> listAllByEsperienzaAccumulata() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessionUtente = utenteRepository.findByUsername(username).orElse(null);
		return repository.listAllByEsperienzaMinima(utenteInSessionUtente.esperienzaAccumulata());
	}

	@Override
	public List<Tavolo> listAllByMyCreati() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessionUtente = utenteRepository.findByUsername(username).orElse(null);
		return repository.listAllByMyCreati(utenteInSessionUtente.id());
	}
	
	@Override
	public void inserisciNuovoDaApplication(Tavolo tavoloInstance) {
		tavoloInstance.dataCreazione(LocalDate.now());
		if(tavoloInstance.cifraMinima() == null)
			tavoloInstance.cifraMinima();
		repository.save(tavoloInstance);
	}

}
