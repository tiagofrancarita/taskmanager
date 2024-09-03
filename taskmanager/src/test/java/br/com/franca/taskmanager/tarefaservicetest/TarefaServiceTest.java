package br.com.franca.taskmanager.tarefaservicetest;

import br.com.franca.taskmanager.exceptions.ResourceNotFoundException;
import br.com.franca.taskmanager.exceptions.ValidationException;
import br.com.franca.taskmanager.models.Projeto;
import br.com.franca.taskmanager.models.Tarefa;
import br.com.franca.taskmanager.models.enums.Prioridade;
import br.com.franca.taskmanager.repositories.TarefaRepository;
import br.com.franca.taskmanager.service.ProjetoService;
import br.com.franca.taskmanager.service.TarefaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TarefaServiceTest {

    @InjectMocks
    private TarefaService tarefaService;

    @InjectMocks
    private ProjetoService projetoService;

    @Mock
    private Prioridade prioridade;

    @Mock
    private TarefaRepository tarefaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ReturnsListOfTarefas() {
        Tarefa tarefa1 = new Tarefa();
        Tarefa tarefa2 = new Tarefa();
        List<Tarefa> tarefas = Arrays.asList(tarefa1, tarefa2);

        when(tarefaRepository.findAll()).thenReturn(tarefas);

        List<Tarefa> result = tarefaService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findById_ValidId_ReturnsTarefa() {
        Long id = 1L;
        Tarefa tarefa = new Tarefa();
        tarefa.setId(id);

        when(tarefaRepository.findById(id)).thenReturn(Optional.of(tarefa));

        Optional<Tarefa> result = tarefaService.findById(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void findById_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> tarefaService.findById(null));
        assertThrows(IllegalArgumentException.class, () -> tarefaService.findById(-1L));
    }

    @Test
    void findById_IdNotFound_ThrowsException() {
        Long id = 1L;
        when(tarefaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tarefaService.findById(id));
    }

    @Test
    void save_ValidTarefa_Success() {

        // Criar um projeto com um ID válido
        Projeto projeto = new Projeto();
        projeto.setId(1L); // Defina um ID válido


        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Nova Tarefa");
        tarefa.setEstimativaHoras(1);
        tarefa.setPrioridade(prioridade.ALTA);
        tarefa.setProjeto(projeto);

        when(tarefaRepository.save(tarefa)).thenReturn(tarefa);

        Tarefa result = tarefaService.save(tarefa);
        assertNotNull(result);
        assertEquals("Nova Tarefa", result.getTitulo());
    }

    @Test
    void save_InvalidTarefa_ThrowsException() {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(""); // Invalid title

        assertThrows(ValidationException.class, () -> tarefaService.save(tarefa));
    }

    @Test
    void deleteById_ValidId_Success() {
        Long id = 1L;
        Tarefa tarefa = new Tarefa();
        tarefa.setId(id);

        when(tarefaRepository.findById(id)).thenReturn(Optional.of(tarefa));
        doNothing().when(tarefaRepository).deleteById(id);

        assertDoesNotThrow(() -> tarefaService.deleteById(id));
    }

    @Test
    void deleteById_IdNotFound_ThrowsException() {
        Long id = 1L;
        when(tarefaRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tarefaService.deleteById(id));
    }
}