package it.prova.pokeronline.service;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.StatoUtente;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.utente.UtenteRepository;
import lombok.experimental.Accessors;

@Service
@Transactional(readOnly = true)
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Utente> listAllUtenti() {
		return (List<Utente>) repository.findAll();
	}

	public Utente caricaSingoloUtente(Long id) {
		return repository.findById(id).orElse(null);
	}

	public Utente caricaSingoloUtenteConRuoli(Long id) {
		return repository.findByIdConRuoli(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Utente utenteInstance) {
		// deve aggiornare solo nome, cognome, username, ruoli
		Utente utenteReloaded = repository.findById(utenteInstance.id()).orElse(null);
		if (utenteReloaded == null)
//			new Utente(null, "mario", null, null, null, null, null, null, null, null)
			throw new RuntimeException("Elemento non trovato");
		utenteReloaded.nome(utenteInstance.nome()).cognome(utenteInstance.cognome()).username(utenteInstance.username())
				.ruoli(utenteInstance.ruoli());
		repository.save(utenteReloaded);
	}

	@Transactional
	public void inserisciNuovo(Utente utenteInstance) {
		utenteInstance.stato(StatoUtente.CREATO)
				.password(passwordEncoder.encode(utenteInstance.password()));
		repository.save(utenteInstance);
	}

	@Transactional
	public void rimuovi(Long idToRemove) {
		repository.deleteById(idToRemove);
		;
	}

	public List<Utente> findByExample(Utente example) {
		// TODO Da implementare
		return listAllUtenti();
	}

	public Utente eseguiAccesso(String username, String password) {
		return repository.findByUsernameAndPasswordAndStato(username, password, StatoUtente.ATTIVO);
	}

	public Utente findByUsernameAndPassword(String username, String password) {
		return repository.findByUsernameAndPassword(username, password);
	}

	@Transactional
	public void changeUserAbilitation(Long utenteInstanceId) {
		Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
		if (utenteInstance == null)
			throw new RuntimeException("Elemento non trovato.");

		if (utenteInstance.stato() == null || utenteInstance.stato().equals(StatoUtente.CREATO))
			utenteInstance.stato(StatoUtente.ATTIVO);
		else if (utenteInstance.stato().equals(StatoUtente.ATTIVO))
			utenteInstance.stato(StatoUtente.DISABILITATO);
		else if (utenteInstance.stato().equals(StatoUtente.DISABILITATO))
			utenteInstance.stato(StatoUtente.ATTIVO);
	}

	public Utente findByUsername(String username) {
		return repository.findByUsername(username).orElse(null);
	}

}
