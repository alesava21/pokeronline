package it.prova.pokeronline.repository.tavolo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloRepository extends CrudRepository<Tavolo, Long>{

	List<Tavolo> findByDenominazione(String denominazione);
	
	@Query("select t from Tavolo t join fetch t.utenteCheCreaIlTavolo join fetch t.utentiAlTavolo")
	List<Tavolo> findAllEager();	
	
	@Query("select t from Tavolo t join t.utenteCheCreaIlTavolo where t.id = ?1 and t.utenteCheCreaIlTavolo.id = ?2")
	Optional<Tavolo> findByIdSpecialPlayer(Long idTavolo, Long idUtente);
	
	
}
