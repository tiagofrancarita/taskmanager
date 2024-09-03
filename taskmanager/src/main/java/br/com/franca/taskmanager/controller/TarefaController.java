package br.com.franca.taskmanager.controller;

import br.com.franca.taskmanager.exceptions.ResourceNotFoundException;
import br.com.franca.taskmanager.exceptions.ValidationException;
import br.com.franca.taskmanager.models.Tarefa;
import br.com.franca.taskmanager.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/tarefas")
public class TarefaController {

        @Autowired
        private TarefaService tarefaService;

        @GetMapping("/getAllTarefas")
        public List<Tarefa> getAllTarefas() {
            return tarefaService.findAll();
        }

        @GetMapping("getTarefaById/{id}")
        public ResponseEntity<Tarefa> getTarefaById(@PathVariable Long id) {
            Optional<Tarefa> tarefa = tarefaService.findById(id);
            return tarefa.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping("/createTarefa")
        public ResponseEntity<Tarefa> createTarefa(@RequestBody Tarefa tarefa) {
            try {
                Tarefa savedTarefa = tarefaService.save(tarefa);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedTarefa);
            } catch (ValidationException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        @PutMapping("updateTarefa/{id}")
        public ResponseEntity<Tarefa> updateTarefa(@PathVariable Long id, @RequestBody Tarefa tarefa) {
            Optional<Tarefa> existingTarefa = tarefaService.findById(id);
            if (!existingTarefa.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            tarefa.setId(id);
            try {
                Tarefa updatedTarefa = tarefaService.save(tarefa);
                return ResponseEntity.ok(updatedTarefa);
            } catch (ValidationException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        @DeleteMapping("deleteTarefaById/{id}")
        public ResponseEntity<Void> deleteTarefaById(@PathVariable Long id) {
            try {
                tarefaService.deleteById(id);
                return ResponseEntity.noContent().build();
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.notFound().build();
            }
        }
    }