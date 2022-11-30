package it.prova.pokeronline.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;
import it.prova.pokeronline.repository.utente.UtenteRepository;
import it.prova.pokeronline.web.api.exception.NonFaiParteDiQuestoTavoloException;
import it.prova.pokeronline.web.api.exception.NonSeiPresenteANessunTavoloException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@Service
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository repository;

	@Autowired
	private UtenteRepository utenteRepository;

	@Override
	public List<Tavolo> listAll() {
		return (List<Tavolo>) repository.findAll();
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
		if (utenteInSessionUtente.ruoli().stream().anyMatch(r -> r.codice().equals(Ruolo.ROLE_SPECIAL_USER))
				&& !(utenteInSessionUtente.id() == tavoloDaEliminare.utenteCheCreaIlTavolo().id()))
			throw new TavoloNotFoundException(
					"impossibile aggiornare un tavolo non creato da te se hai ruolo special player");

		repository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Tavolo tavoloInstance) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(username);
		Utente utenteInSessionUtente = utenteRepository.findByUsername(username).orElse(null);
		tavoloInstance.utenteCheCreaIlTavolo(utenteInSessionUtente);
		System.out.println(tavoloInstance.utenteCheCreaIlTavolo().username());
		tavoloInstance.dataCreazione(LocalDate.now());
		if (tavoloInstance.cifraMinima() == null)
			tavoloInstance.cifraMinima();
		repository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessionUtente = utenteRepository.findByUsername(username).orElse(null);
		Tavolo tavoloDaEliminare = caricaSingoloElementoEager(idToRemove);
		if (utenteInSessionUtente.ruoli().stream().anyMatch(r -> r.codice().equals(Ruolo.ROLE_SPECIAL_USER))
				&& !(utenteInSessionUtente.id() == tavoloDaEliminare.utenteCheCreaIlTavolo().id()))
			throw new TavoloNotFoundException(
					"impossibile eliminare un tavolo non creato da te se hai ruolo special player");

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
		if (tavoloInstance.cifraMinima() == null)
			tavoloInstance.cifraMinima();
		repository.save(tavoloInstance);
	}

	@Override
	public Tavolo lastGame(Long id) {
		Tavolo resul = repository.findByutentiAlTavolo_id(id).orElse(null);

		if (resul == null) {
			throw new NonSeiPresenteANessunTavoloException("ATTENZIONE: non sei presente a nessun tavolo");
		}
		return resul;
	}

	@Override
	@Transactional
	public Utente abbandonaPartita(Long idTavolo) {
		Tavolo tavoloInstance = repository.findById(idTavolo).orElse(null);
		if (tavoloInstance == null)
			throw new TavoloNotFoundException("Tavolo con id: " + idTavolo + " not Found");

		Utente inSessione = utenteRepository
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
//utilizzo un contatore per vedere se l'utente e nel tavolo
		int contatore = 0;

//		Iterator<Utente> it = tavoloInstance.utentiAlTavolo().iterator();
//		while (it.hasNext()) {
//			Utente u = it.next();
//			if (u.id() == inSessione.id()) {
//				it.remove();
//				contatore++;
//			}
//		}
		for (Iterator<Utente> utenteDaEliminare = tavoloInstance.utentiAlTavolo().iterator(); utenteDaEliminare.hasNext();) {
			Utente utente = utenteDaEliminare.next();
			if (utente.id() == inSessione.id()) {
				utenteDaEliminare.remove();
				contatore ++;
				
			}
		}

		// FUNZIONANTE
//		for(Iterator<Utente> it = tavoloInstance.utentiAlTavolo().iterator(); it.hasNext();) {
//			Utente s = it.next();
//			if (s.id() == inSessione.id()) {
//				it.remove();
//				contatore ++;
//			}
//		}
		// NON FUNZIONANTE java.util.ConcurrentModificationException: null
//		for (Utente utenteItem : tavoloInstance.utentiAlTavolo()) {
//			if (utenteItem.id() == inSessione.id()) {
//				tavoloInstance.utentiAlTavolo().remove(utenteItem);
//				contatore++;
//			}
//		}
		/*
		 * se abbiamo il contatore che e == a 0 significa che l'utente non e presente al
		 * tavolo
		 */
		if (contatore == 0)
			throw new NonFaiParteDiQuestoTavoloException("Attenzione, non fai parte di questo tavolo");

		inSessione.esperienzaAccumulata(inSessione.esperienzaAccumulata() + 1);
		repository.save(tavoloInstance);
		utenteRepository.save(inSessione);

		return inSessione;

	}

}
