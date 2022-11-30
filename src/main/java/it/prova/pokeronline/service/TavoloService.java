package it.prova.pokeronline.service;

import java.util.List;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloService {

	public List<Tavolo> listAll();

	public List<Tavolo> listAllByEsperienzaAccumulata();

	public List<Tavolo> listAllByMyCreati();

	public Tavolo caricaSingoloElemento(Long id);

	public Tavolo caricaSingoloElementoEager(Long id);

	public void aggiorna(Tavolo tavoloInstance);

	public void inserisciNuovo(Tavolo tavoloInstance);

	public void rimuovi(Long idToRemove);

	public void inserisciNuovoDaApplication(Tavolo tavoloInstance);

	public Tavolo lastGame(Long id);

	public void abbandonaPartita(Long idTavolo);

}
