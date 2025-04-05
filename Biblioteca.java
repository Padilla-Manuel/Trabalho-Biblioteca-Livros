package br.manuel.acervo.entidade;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Biblioteca {
    @Id
    private long id;
    private String nome;
    
    //um-para-muitos 
    @OneToMany(mappedBy="biblioteca", fetch = FetchType.EAGER)
    private List<Livro> livros;
}

