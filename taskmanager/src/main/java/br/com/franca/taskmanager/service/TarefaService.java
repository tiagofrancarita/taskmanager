package br.com.franca.taskmanager.service;

import br.com.franca.taskmanager.exceptions.ResourceNotFoundException;
import br.com.franca.taskmanager.exceptions.ValidationException;
import br.com.franca.taskmanager.models.Tarefa;
import br.com.franca.taskmanager.repositories.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public List<Tarefa> findAll() {
        return tarefaRepository.findAll();
    }

    public Optional<Tarefa> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID fornecido deve ser um número positivo.");
        }

        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        if (!tarefa.isPresent()) {
            throw new ResourceNotFoundException("Tarefa com ID " + id + " não encontrada.");
        }
        return tarefa;
    }

    public Tarefa save(Tarefa tarefa) {
        validateTarefa(tarefa);
        return tarefaRepository.save(tarefa);
    }

    public void deleteById(Long id) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        if (!tarefa.isPresent()) {
            throw new ResourceNotFoundException("Tarefa com ID " + id + " não encontrada.");
        }
        tarefaRepository.deleteById(id);
    }

    private void validateTarefa(Tarefa tarefa) {
        if (tarefa.getTitulo() == null || tarefa.getTitulo().trim().isEmpty()) {
            throw new ValidationException("O título da tarefa é obrigatório.");
        }

        if (tarefa.getEstimativaHoras() == null || tarefa.getEstimativaHoras() <= 0) {
            throw new ValidationException("A estimativa de horas da tarefa deve ser maior que zero.");
        }

        if (tarefa.getPrioridade() == null) {
            throw new ValidationException("A prioridade da tarefa é obrigatória.");
        }

        if (tarefa.getProjeto() == null || tarefa.getProjeto().getId() == null) {
            throw new ValidationException("A tarefa deve estar associada a um projeto.");
        }
    }
}
