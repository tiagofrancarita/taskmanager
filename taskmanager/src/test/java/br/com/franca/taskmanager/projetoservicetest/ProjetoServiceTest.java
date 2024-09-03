package br.com.franca.taskmanager.projetoservicetest;

import br.com.franca.taskmanager.exceptions.ResourceNotFoundException;
import br.com.franca.taskmanager.exceptions.ValidationException;
import br.com.franca.taskmanager.models.Projeto;
import br.com.franca.taskmanager.repositories.ProjetoRepository;
import br.com.franca.taskmanager.service.ProjetoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ProjetoServiceTest {

    @InjectMocks
    private ProjetoService projetoService;

    @Mock
    private ProjetoRepository projetoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ReturnsListOfProjetos() {
        Projeto projeto1 = new Projeto();
        Projeto projeto2 = new Projeto();
        List<Projeto> projetos = Arrays.asList(projeto1, projeto2);

        when(projetoRepository.findAll()).thenReturn(projetos);

        List<Projeto> result = projetoService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findById_ValidId_ReturnsProjeto() {
        Long id = 1L;
        Projeto projeto = new Projeto();
        projeto.setId(id);

        when(projetoRepository.findById(id)).thenReturn(Optional.of(projeto));

        Optional<Projeto> result = projetoService.findById(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void findById_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> projetoService.findById(null));
        assertThrows(IllegalArgumentException.class, () -> projetoService.findById(-1L));
    }

    @Test
    void findById_IdNotFound_ThrowsException() {
        Long id = 1L;
        when(projetoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projetoService.findById(id));
    }

    @Test
    void save_ValidProjeto_Success() {

        Date dataInicio = new Date();


        Projeto projeto = new Projeto();
        projeto.setTitulo("Novo Projeto");
        projeto.setDataInicio(dataInicio);

        when(projetoRepository.save(projeto)).thenReturn(projeto);

        Projeto result = projetoService.save(projeto);
        assertNotNull(result);
        assertEquals("Novo Projeto", result.getTitulo());
    }

    @Test
    void save_InvalidProjeto_ThrowsException() {
        Projeto projeto = new Projeto();
        projeto.setTitulo(""); // Invalid title

        assertThrows(ValidationException.class, () -> projetoService.save(projeto));
    }

    @Test
    void deleteById_ValidId_Success() {
        Long id = 1L;
        Projeto projeto = new Projeto();
        projeto.setId(id);

        when(projetoRepository.findById(id)).thenReturn(Optional.of(projeto));
        doNothing().when(projetoRepository).deleteById(id);

        assertDoesNotThrow(() -> projetoService.deleteById(id));
    }

    @Test
    void deleteById_IdNotFound_ThrowsException() {
        Long id = 1L;
        when(projetoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projetoService.deleteById(id));
    }
}
