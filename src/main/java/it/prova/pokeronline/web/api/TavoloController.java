package it.prova.pokeronline.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;

@RestController
@RequestMapping("apt/tavolo	")
public class TavoloController {
	
	@Autowired
	private TavoloService tavoloService;

	@Autowired
	private UtenteService utenteService;

}
