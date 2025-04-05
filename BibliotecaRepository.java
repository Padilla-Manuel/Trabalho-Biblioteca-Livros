package br.manuel.acervo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import br.manuel.acervo.entidade.Biblioteca;

public interface BibliotecaRepository extends JpaRepository<Biblioteca, Long >{

}
