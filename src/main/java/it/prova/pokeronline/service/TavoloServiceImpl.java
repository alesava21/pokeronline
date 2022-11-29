package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;

@Service
@Transactional(readOnly = true)
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository tavoloRepository;

	@Autowired
	private UtenteService utenteService;

	@Override
	public List<Tavolo> listAllElements(boolean eager) {
		return (List<Tavolo>) tavoloRepository.findAll();
	}

	@Override
	public Tavolo caricaSingoloElemento(Long id) {
		return tavoloRepository.findById(id).orElse(null);
	}

	@Override
	public Tavolo caricaSingoloElementoEager(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tavolo aggiorna(Tavolo tavoloInstance) {
		return tavoloRepository.save(tavoloInstance);

	}

	@Override
	public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
		return tavoloRepository.save(tavoloInstance);
	}

	@Override
	public void rimuovi(Long idToRemove) {
		tavoloRepository.findById(idToRemove);

	}

}
