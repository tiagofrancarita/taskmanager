package br.com.franca.taskmanager.controller;

import br.com.franca.taskmanager.models.Projeto;
import br.com.franca.taskmanager.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @GetMapping("/getAllProjetos")
    public List<Projeto> getAllProjetos() {
        return projetoService.findAll();
    }

    @GetMapping("getProjetoById/{id}")
    public ResponseEntity<Projeto> getProjetoById(@PathVariable Long id) {
        Optional<Projeto> projeto = projetoService.findById(id);
        return projeto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/createProjeto")
    public Projeto createProjeto(@RequestBody Projeto projeto) {
        return projetoService.save(projeto);
    }

    @PutMapping("updateProjeto/{id}")
    public ResponseEntity<Projeto> updateProjeto(@PathVariable Long id, @RequestBody Projeto projeto) {
        if (!projetoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        projeto.setId(id);
        return ResponseEntity.ok(projetoService.save(projeto));
    }

    @DeleteMapping("deleteProjetoById/{id}")
    public ResponseEntity<Void> deleteProjetoById(@PathVariable Long id) {
        if (!projetoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        projetoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}