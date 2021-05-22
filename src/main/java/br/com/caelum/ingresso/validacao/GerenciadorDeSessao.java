package br.com.caelum.ingresso.validacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import br.com.caelum.ingresso.model.Sessao;

public class GerenciadorDeSessao {

	private final List<Sessao> sessoesDaSala;

	public GerenciadorDeSessao(List<Sessao> sessoesDaSala) {
		this.sessoesDaSala = sessoesDaSala;
	}

	public boolean cabe(Sessao sessaoNova) {
		if (terminaAmanha(sessaoNova)) {
			return false;
		}
		return sessoesDaSala.stream().noneMatch(sessaoExistente -> horarioIsConflitante(sessaoExistente, sessaoNova));
	}

	private boolean horarioIsConflitante(Sessao sessaoExistente, Sessao sessaoNova) {
		LocalDateTime inicioSessaoExistente = getInicioSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime terminoSessaoExistente = getTerminoSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime inicioSessaoNova = getInicioSessaoComDiaDeHoje(sessaoNova);
		LocalDateTime terminoSessaoNova = getTerminoSessaoComDiaDeHoje(sessaoNova);

		boolean sessaoNovaTerminaAntesDaExistente = terminoSessaoNova.isBefore(inicioSessaoExistente);
		boolean sessaoNovaComecaDepoisDaExistente = terminoSessaoExistente.isBefore(inicioSessaoNova);

		if (sessaoNovaTerminaAntesDaExistente || sessaoNovaComecaDepoisDaExistente) {
			return false;
		}
		return true;
	}

	private boolean terminaAmanha(Sessao sessaoNova) {
		LocalDateTime terminoSessaoNova = getTerminoSessaoComDiaDeHoje(sessaoNova);
		LocalDateTime ultimoSegundoDeHoje = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		if (terminoSessaoNova.isAfter(ultimoSegundoDeHoje)) {
			return true;
		}
		return false;
	}

	private LocalDateTime getTerminoSessaoComDiaDeHoje(Sessao sessaoNova) {
		LocalDateTime inicioSessaoNova = getInicioSessaoComDiaDeHoje(sessaoNova);
		return inicioSessaoNova.plus(sessaoNova.getFilme().getDuracao());
	}

	private LocalDateTime getInicioSessaoComDiaDeHoje(Sessao sessaoNova) {
		LocalDate hoje = LocalDate.now();
		return sessaoNova.getHorario().atDate(hoje);
	}
}