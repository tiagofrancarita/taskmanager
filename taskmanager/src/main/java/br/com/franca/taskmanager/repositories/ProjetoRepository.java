package br.com.franca.taskmanager.repositories;

import br.com.franca.taskmanager.models.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto,Long> {
}
