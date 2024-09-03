package br.com.franca.taskmanager.service;

import br.com.franca.taskmanager.exceptions.ResourceNotFoundException;
import br.com.franca.taskmanager.exceptions.ValidationException;
import br.com.franca.taskmanager.models.Projeto;
import br.com.franca.taskmanager.repositories.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    public List<Projeto> findAll() {
        List<Projeto> projetos = projetoRepository.findAll();
        return (projetos != null) ? projetos : new ArrayList<>();
    }

    public Optional<Projeto> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID fornecido deve ser um número positivo.");
        }

        Optional<Projeto> projeto = projetoRepository.findById(id);
        if (!projeto.isPresent()) {
            throw new ResourceNotFoundException("Projeto com ID " + id + " não encontrado.");
        }
        return projeto;
    }

    public Projeto save(Projeto projeto) {
        validateProjeto(projeto);
        return projetoRepository.save(projeto);
    }

    public void deleteById(Long id) {
        Optional<Projeto> projeto = projetoRepository.findById(id);
        if (!projeto.isPresent()) {
            throw new ResourceNotFoundException("Projeto com ID " + id + " não encontrado.");
        }
        projetoRepository.deleteById(id);
    }

    private void validateProjeto(Projeto projeto) {
        if (projeto.getTitulo() == null || projeto.getTitulo().trim().isEmpty()) {
            throw new ValidationException("O título do projeto é obrigatório.");
        }

        if (projeto.getDataInicio() == null) {
            throw new ValidationException("A data de início do projeto é obrigatória.");
        }

        if (projeto.getDataInicio().after(new Date())) {
            throw new ValidationException("A data de início do projeto não pode ser no futuro.");
        }

        // Verificar se o título do projeto já existe
        Optional<Projeto> existingProjeto = projetoRepository.findAll().stream()
                .filter(p -> p.getTitulo().equals(projeto.getTitulo()))
                .findFirst();

        if (existingProjeto.isPresent() && !existingProjeto.get().getId().equals(projeto.getId())) {
            throw new ValidationException("Já existe um projeto com este título.");
        }

        // Verificar validade das tarefas associadas
        if (projeto.getTarefas() != null) {
            projeto.getTarefas().forEach(tarefa -> {
                if (tarefa.getTitulo() == null || tarefa.getTitulo().trim().isEmpty()) {
                    throw new ValidationException("O título da tarefa é obrigatório.");
                }
                if (tarefa.getEstimativaHoras() == null || tarefa.getEstimativaHoras() <= 0) {
                    throw new ValidationException("A estimativa de horas da tarefa deve ser maior que zero.");
                }
            });
        }
    }
}
