package com.prova.av2;

import com.prova.av2.controller.CategoriaController;
import com.prova.av2.model.Categoria;
import com.prova.av2.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
    }

    @Test
    void testListar() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria Teste");
        when(categoriaService.listar()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/categoria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(categoria.getId()))
                .andExpect(jsonPath("$[0].nome").value(categoria.getNome()));

        verify(categoriaService, times(1)).listar();
    }

    @Test
    void testBuscarPorId() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria Teste");
        when(categoriaService.buscarPorId(1L)).thenReturn(Optional.of(categoria));

        mockMvc.perform(get("/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoria.getId()))
                .andExpect(jsonPath("$.nome").value(categoria.getNome()));

        verify(categoriaService, times(1)).buscarPorId(1L);
    }

    @Test
    void testBuscarPorIdNaoEncontrado() throws Exception {
        when(categoriaService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/categoria/1"))
                .andExpect(status().isNotFound()); // Espera 404, não 500

        verify(categoriaService, times(1)).buscarPorId(1L);
    }

    @Test
    void testSalvar() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNome("Nova Categoria");
        Categoria categoriaSalva = new Categoria();
        categoriaSalva.setId(1L);
        categoriaSalva.setNome(categoria.getNome());

        when(categoriaService.salvar(any(Categoria.class))).thenReturn(categoriaSalva);

        mockMvc.perform(post("/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoriaSalva.getId()))
                .andExpect(jsonPath("$.nome").value(categoriaSalva.getNome()));

        verify(categoriaService, times(1)).salvar(any(Categoria.class));
    }

    @Test
    void testAtualizar() throws Exception {
        Categoria categoriaAtualizada = new Categoria();
        categoriaAtualizada.setId(1L);
        categoriaAtualizada.setNome("Categoria Atualizada");

        when(categoriaService.atualizar(eq(1L), any(Categoria.class))).thenReturn(categoriaAtualizada);

        mockMvc.perform(put("/categoria/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaAtualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoriaAtualizada.getId()))
                .andExpect(jsonPath("$.nome").value(categoriaAtualizada.getNome()));

        verify(categoriaService, times(1)).atualizar(eq(1L), any(Categoria.class));
    }

    @Test
    void testDeletar() throws Exception {
        doNothing().when(categoriaService).deletar(1L);

        mockMvc.perform(delete("/categoria/1"))
                .andExpect(status().isOk());

        verify(categoriaService, times(1)).deletar(1L);
    }
}
