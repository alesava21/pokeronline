package it.prova.pokeronline.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.pokeronline.model.Ruolo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(fluent = true)
public class RuoloDTO {

	private Long id;
	private String descrizione;
	private String codice;

	public static RuoloDTO buildRuoloDTOFromModel(Ruolo ruoloModel) {
		return new RuoloDTO(ruoloModel.id(), ruoloModel.descrizione(), ruoloModel.codice());
	}

	public static List<RuoloDTO> createRuoloDTOListFromModelSet(Set<Ruolo> modelListInput) {
		return modelListInput.stream().map(ruoloEntity -> {
			return RuoloDTO.buildRuoloDTOFromModel(ruoloEntity);
		}).collect(Collectors.toList());
	}
	
	public static List<RuoloDTO> createRuoloDTOListFromModelList(List<Ruolo> modelListInput) {
		return modelListInput.stream().map(ruoloEntity -> {
			return RuoloDTO.buildRuoloDTOFromModel(ruoloEntity);
		}).collect(Collectors.toList());
	}
	
}
