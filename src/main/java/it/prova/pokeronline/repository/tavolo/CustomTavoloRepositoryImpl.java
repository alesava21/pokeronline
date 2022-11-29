package it.prova.pokeronline.repository.tavolo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import it.prova.pokeronline.model.Tavolo;

public class CustomTavoloRepositoryImpl implements CustomTavoloRepository{
	
	@PersistenceContext
	private EntityManager entityManager;

	
	/*
	 * FindByExample per l'admin, Si puo anche ricercare utentechecreailtavolo_id
	 * Passando il giusto id.
	 * */
	@Override
	public List<Tavolo> findByExample(Tavolo example) {
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();
		
		StringBuilder queryBuilder = new StringBuilder("select t from Tavolo t where t.id = t.id ");

		if (StringUtils.isNotEmpty(example.denominazione())) {
			whereClauses.add(" t.denominazione  like :denominazione ");
			paramaterMap.put("denominazione", "%" + example.denominazione() + "%");
		}
		if (example.cifraMinima() != null && example.cifraMinima() > 0) {
			whereClauses.add(" t.cifraMinima > :cifraMinima ");
			paramaterMap.put("cifraMinima", example.cifraMinima());
		}
		if (example.esperienzaMinima() != null && example.esperienzaMinima() > 0) {
			whereClauses.add(" t.esperienzaMinima > :esperienzaMinima ");
			paramaterMap.put("esperienzaMinima", example.esperienzaMinima());
		}
		if (example.dataCreazione() != null) {
			whereClauses.add("t.dataCreazione>= :dataCreazione ");
			paramaterMap.put("dataCreazione", example.dataCreazione());
		}
		if (example.utenteCheCreaIlTavolo() != null && example.utenteCheCreaIlTavolo().id() != null
				&& example.utenteCheCreaIlTavolo().id() > 0) {
			whereClauses.add(" t.utentechecreailtavolo_id.id = :utentechecreailtavolo_id ");
			paramaterMap.put("utentechecreailtavolo_id", example.utenteCheCreaIlTavolo().id());
		}
		
		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Tavolo> typedQuery = entityManager.createQuery(queryBuilder.toString(), Tavolo.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();
	}

	@Override
	public List<Tavolo> findByExampleSpecialPlayer(Tavolo example, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
