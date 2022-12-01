package it.prova.pokeronline.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@Setter
@ToString
@Entity
@Table(name = "tavolo")
public class Tavolo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "esperienzaminima")
	private Integer esperienzaMinima;
	@Column(name = "ciframinima")
	private Integer cifraMinima;
	@Column(name = "denominazione")
	private String denominazione;
	@Column(name = "datacreazione")
	private LocalDate dataCreazione;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utentechecreailtavolo_id", nullable = false)
	private Utente utenteCheCreaIlTavolo;

	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY)
	private List<Utente> utentiAlTavolo = new ArrayList<>();
}