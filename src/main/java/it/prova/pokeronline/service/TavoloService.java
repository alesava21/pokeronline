package it.prova.pokeronline.service;

import java.util.List;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloService {

	public List<Tavolo> listAll();

	List<Tavolo> listAllElements(boolean eager);

	Tavolo caricaSingoloElemento(Long id);

	Tavolo caricaSingoloElementoEager(Long id);

	public Tavolo aggiorna(Tavolo tavoloInstance, Tavolo tavoloCaricatoDalDB);

	Tavolo inserisciNuovo(Tavolo tavoloInstance);

	void rimuovi(Long idToRemove);

	List<Tavolo> findByDenominazione(String denominazione);

	List<Tavolo> findByExample(Tavolo example);

	List<Tavolo> findByExampleSpecialPlayer(Tavolo example, Long id);

}
