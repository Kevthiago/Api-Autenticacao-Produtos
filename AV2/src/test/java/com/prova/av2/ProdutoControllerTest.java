package com.prova.av2;

import com.prova.av2.controller.ProdutoController;
import com.prova.av2.model.Produto;
import com.prova.av2.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();
    }

    @Test
    void testListar() throws Exception {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        when(produtoService.listar()).thenReturn(List.of(produto));

        mockMvc.perform(get("/produto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(produto.getId()))
                .andExpect(jsonPath("$[0].nome").value(produto.getNome()));

        verify(produtoService, times(1)).listar();
    }

    @Test
    void testBuscarPorId() throws Exception {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        when(produtoService.buscarPorId(1L)).thenReturn(produto);

        mockMvc.perform(get("/produto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produto.getId()))
                .andExpect(jsonPath("$.nome").value(produto.getNome()));

        verify(produtoService, times(1)).buscarPorId(1L);
    }

    @Test
    void testSalvar() throws Exception {
        Produto produto = new Produto();
        produto.setNome("Novo Produto");
        Produto produtoSalvo = new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome(produto.getNome());

        when(produtoService.salvar(any(Produto.class))).thenReturn(produtoSalvo);

        mockMvc.perform(post("/produto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produtoSalvo.getId()))
                .andExpect(jsonPath("$.nome").value(produtoSalvo.getNome()));

        verify(produtoService, times(1)).salvar(any(Produto.class));
    }

    @Test
    void testAtualizar() throws Exception {
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNome("Produto Atualizado");

        when(produtoService.atualizar(eq(1L), any(Produto.class))).thenReturn(produtoAtualizado);

        mockMvc.perform(put("/produto/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produtoAtualizado.getId()))
                .andExpect(jsonPath("$.nome").value(produtoAtualizado.getNome()));

        verify(produtoService, times(1)).atualizar(eq(1L), any(Produto.class));
    }

    @Test
    void testDeletar() throws Exception {
        doNothing().when(produtoService).deletar(1L);

        mockMvc.perform(delete("/produto/1"))
                .andExpect(status().isOk());

        verify(produtoService, times(1)).deletar(1L);
    }
}
