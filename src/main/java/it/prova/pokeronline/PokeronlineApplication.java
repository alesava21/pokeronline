package it.prova.pokeronline;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.RuoloService;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;

@SpringBootApplication
public class PokeronlineApplication implements CommandLineRunner{
	
	@Autowired
	private RuoloService ruoloServiceInstance;
	
	@Autowired
	private UtenteService utenteServiceInstance;
	
	@Autowired
	private TavoloService tavoloService;

	public static void main(String[] args) {
		SpringApplication.run(PokeronlineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		
		
		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN) == null) {
			ruoloServiceInstance.inserisciNuovo(Ruolo.builder().descrizione("Administrator").codice(Ruolo.ROLE_ADMIN).build());
		}

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Classic User", Ruolo.ROLE_CLASSIC_USER) == null) {
			ruoloServiceInstance.inserisciNuovo(Ruolo.builder().descrizione("Classic User").codice(Ruolo.ROLE_CLASSIC_USER).build());
		}
		
		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Special User", Ruolo.ROLE_SPECIAL_USER) == null) {
			ruoloServiceInstance.inserisciNuovo(Ruolo.builder().descrizione("Special User").codice(Ruolo.ROLE_SPECIAL_USER).build());
		}

		// a differenza degli altri progetti cerco solo per username perche' se vado
		// anche per password ogni volta ne inserisce uno nuovo, inoltre l'encode della
		// password non lo
		// faccio qui perche gia lo fa il service di utente, durante inserisciNuovo
		if (utenteServiceInstance.findByUsername("admin") == null) {
			Utente admin = Utente.builder()
					.nome("mario")
					.cognome("rossi")
					.username("admin")
					.password("admin")
					.dataRegistrazione(LocalDate.of(2000, 8, 10))
					.esperienzaAccumulata(3)
					.creditoAccumulato(500)
					.build();
			admin.ruoli().add(ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN));
			utenteServiceInstance.inserisciNuovo(admin);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(admin.id());
		}

		if (utenteServiceInstance.findByUsername("user") == null) {
			Utente classicUser = Utente.builder()
					.nome("roberto")
					.cognome("bianchi")
					.username("user")
					.password("user")
					.dataRegistrazione(LocalDate.of(2001, 10, 20))
					.esperienzaAccumulata(6)
					.creditoAccumulato(1000)
					.build();			
			classicUser.ruoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("Classic User", Ruolo.ROLE_CLASSIC_USER));
			utenteServiceInstance.inserisciNuovo(classicUser);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(classicUser.id());
		}

		if (utenteServiceInstance.findByUsername("userSpecial") == null) {
			Utente specialUser = Utente.builder()
					.nome("alessandro")
					.cognome("sava")
					.username("userSpecial")
					.password("userSpecial")
					.dataRegistrazione(LocalDate.of(1990, 1, 27))
					.esperienzaAccumulata(10)
					.creditoAccumulato(10000)
					.build();			
			specialUser.ruoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("Classic User", Ruolo.ROLE_CLASSIC_USER));
			utenteServiceInstance.inserisciNuovo(specialUser);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(specialUser.id());
		}

		if (utenteServiceInstance.findByUsername("user1") == null) {
			Utente user1 = Utente.builder()
					.nome("michele")
					.cognome("esposito")
					.username("user1")
					.password("user1")
					.dataRegistrazione(LocalDate.of(2010, 6, 10))
					.esperienzaAccumulata(0)
					.creditoAccumulato(0)
					.build();					
			user1.ruoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("Classic User", Ruolo.ROLE_CLASSIC_USER));
			utenteServiceInstance.inserisciNuovo(user1);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(user1.id());
		}
		
		Tavolo tavolo1 = Tavolo.builder()
				.id(1L)
				.esperienzaMinima(1)
				.cifraMinima(100)
				.denominazione("denominazione1")
				.dataCreazione(LocalDate.now())
				.utenteCheCreaIlTavolo(utenteServiceInstance.listAllUtenti().get(0))
				.build();
		tavoloService.inserisciNuovoDaApplication(tavolo1);
	}

}
