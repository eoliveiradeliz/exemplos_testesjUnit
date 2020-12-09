package br.edu.unoesc.java.servicos;

import br.edu.unoesc.java.entidades.Filme;
import br.edu.unoesc.java.entidades.Locacao;
import br.edu.unoesc.java.entidades.Usuario;
import br.edu.unoesc.java.repository.LocacaoServiceRepository;
import org.junit.*;
import org.mockito.Mockito;

public class LocacaoServiceTest {

    LocacaoService locacaoService = null;
    Usuario usuario = null;
    Filme filme = null;
    LocacaoServiceRepository locacaoServiceRepository;

    @Before
    public void iniciarTeste() {
        locacaoService = new LocacaoService();

        locacaoServiceRepository = Mockito.mock(LocacaoServiceRepository.class);
        locacaoService.setLocacaoServiceRepository(locacaoServiceRepository);

        usuario = new Usuario("Usuário");
        filme = new Filme("Filme", 2, 7.00);
    }

    @After
    public void finalizarTeste() {
        locacaoService = null;
        locacaoServiceRepository = null;
        usuario = null;
        filme = null;
    }

    @Test
    public void testeErroAoSalvar() {
        Locacao locacao = null;
        try {
            Mockito
                    .doThrow(new Exception("Erro ao Salvar!"))
                    .when(locacaoServiceRepository)
                    .salvar(Mockito.any());

            locacao = locacaoService.alugarFilme(usuario, filme);
            //Assert.fail("Estava esperando uma exception!");
        } catch (Exception e) {
            Assert.assertEquals("Erro ao Salvar!", e.getMessage());
        }

        Assert.assertNull("Não deveria ter gerado a locação!", locacao);
    }

    @Test
    public void testeAlugarFilme() throws Exception {
        //cenário
        Usuario expectedUsuario = new Usuario("Usuário");

        Double expectedResult = 7.00;

        //execução
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //validação
        Assert.assertEquals(expectedUsuario, locacao.getUsuario());
        Assert.assertEquals(filme, locacao.getFilme());
        Assert.assertEquals(expectedResult, locacao.getValor());
        Assert.assertNotNull(locacao.getDataLocacao());
        Assert.assertTrue(locacao.getDataRetorno().after(locacao.getDataLocacao()));
    }

    @Test
    public void TestAlugarFilme_SemUsuario() {
        //cenário

        //execução
        Locacao locacao = null;

        try {
          locacao = locacaoService.alugarFilme(null, filme);
          //Assert.fail("Estava esperando uma exception!");
        } catch (Exception e) {
          Assert.assertEquals("Usuário não informado!", e.getMessage());
        }

        Assert.assertNull("Não deveria ter gerado a locação!", locacao);
    }

    @Test
    public void TestAlugarFilme_SemFilme() {
        //cenário

        //execução
        Locacao locacao = null;

        try {
            locacao = locacaoService.alugarFilme(usuario, null);
            //Assert.fail("Estava esperando uma exception!");
        } catch (Exception e) {
            Assert.assertEquals("Filme não informado!", e.getMessage());
        }

        Assert.assertNull("Não deveria ter gerado a locação!", locacao);
    }

    @Test
    public void TestAlugarFilme_FilmeSemEstoque() {
        //cenário
        filme.setEstoque(0);

        //execução
        Locacao locacao = null;

        try {
            locacao = locacaoService.alugarFilme(usuario, filme);
            //Assert.fail("Estava esperando uma exception!");
        } catch (Exception e) {
            Assert.assertEquals("Filme sem Estoque!", e.getMessage());
        }

        Assert.assertNull("Não deveria ter gerado a locação!", locacao);
    }

    @Test
    public void TestDeveBaixarEstoque() throws Exception {
        //cenário
        Integer expectedEstoque = 1;

        //Execução
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //Validação/Verifição
        Assert.assertEquals(expectedEstoque, locacao.getFilme().getEstoque());
    }

}

