package it.prova.pokeronline.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TavoloDTO {
	
	private Long id;
	@PositiveOrZero
	private Integer esperienzaMinima;
	@PositiveOrZero
	private Integer cifraMinima;
	@NotBlank
	private String denominazione;
	private LocalDate dataCreazione;
	private UtenteDTO utenteCheCreaIlTavolo;
	
	@Builder.Default
	private List<UtenteDTO> utentiAlTavolo = new ArrayList<>();
	
	public Tavolo buildTavoloModel() {
		Utente creatore = utenteCheCreaIlTavolo.buildUtenteModel(true);
		System.out.println(creatore == null);
		Tavolo result = Tavolo.builder()
				.id(id)
				.esperienzaMinima(esperienzaMinima)
				.cifraMinima(cifraMinima)
				.denominazione(denominazione)
				.dataCreazione(dataCreazione)
				.utenteCheCreaIlTavolo(creatore)
				.utentiAlTavolo(UtenteDTO.createUtenteListModelFromDTO(utentiAlTavolo))
				.build();
		return result;
	}
	
	public static TavoloDTO buildTavoloDTOFromModel(Tavolo tavoloModel) {
		TavoloDTO result = TavoloDTO.builder()
				.id(tavoloModel.id())
				.esperienzaMinima(tavoloModel.esperienzaMinima())
				.cifraMinima(tavoloModel.cifraMinima())
				.dataCreazione(tavoloModel.dataCreazione())
				.denominazione(tavoloModel.denominazione())
				.utenteCheCreaIlTavolo(UtenteDTO.buildUtenteDTOFromModel(tavoloModel.utenteCheCreaIlTavolo()))
				.utentiAlTavolo(UtenteDTO.createUtenteListDTOFromModel(tavoloModel.utentiAlTavolo()))
				.build();
				
		return result;
	}
	
	public static List<TavoloDTO> createListDTOFromModel(List<Tavolo> tavoloListModel){
		return tavoloListModel.stream().map(tavolo -> {
			return TavoloDTO.buildTavoloDTOFromModel(tavolo);
		}).collect(Collectors.toList());
	}
	
	public static List<Tavolo> createListModelFromDTO(List<TavoloDTO> tavoloListDTO){
		return tavoloListDTO.stream().map(tavolo -> {
			return tavolo.buildTavoloModel();
		}).collect(Collectors.toList());
	}

}
