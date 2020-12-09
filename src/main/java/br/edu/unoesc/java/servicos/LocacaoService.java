package br.edu.unoesc.java.servicos;

import java.util.Date;

import br.edu.unoesc.java.entidades.Filme;
import br.edu.unoesc.java.entidades.Locacao;
import br.edu.unoesc.java.entidades.Usuario;
import br.edu.unoesc.java.repository.LocacaoServiceRepository;
import br.edu.unoesc.java.utils.DataUtils;

public class LocacaoService {

	LocacaoServiceRepository locacaoServiceRepository;
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) throws Exception {

		if (filme == null)
			throw new Exception("Filme não informado!");

		if (usuario == null)
			throw new Exception("Usuário não informado!");

		if (filme.getEstoque().equals(0) )
			throw new Exception("Filme sem Estoque!");

		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = DataUtils.incDays(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);

		filme.setEstoque(filme.getEstoque() - 1);
		
		//Salvando a locacao...	
		locacaoServiceRepository.salvar(locacao);
		
		return locacao;
	}

	public void setLocacaoServiceRepository(LocacaoServiceRepository locacaoServiceRepository) {
		this.locacaoServiceRepository = locacaoServiceRepository;
	}

}