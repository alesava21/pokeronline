package it.prova.pokeronline.repository.tavolo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloRepository extends CrudRepository<Tavolo, Long>{

	List<Tavolo> findByDenominazione(String denominazione);
	
	
}
