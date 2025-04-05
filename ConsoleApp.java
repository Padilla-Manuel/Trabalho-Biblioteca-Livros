//MANUEL FERMIN SANCHEZ PADILLA - 10426597

package br.manuel.acervo.aplicacao;

//Nesta parte eu crie um novo projeto na supabase e criei uma nova interface com o usuário
//por isso, quando o usuário vai digitar para criar uma nova biblioteca, vai ser criado na supabase
import br.manuel.acervo.entidade.Biblioteca;
import br.manuel.acervo.entidade.Livro;
import br.manuel.acervo.repositorio.BibliotecaRepository;
import br.manuel.acervo.repositorio.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

//Implementei nova interface com o usuario para poder ver si ocurre realmente 
//@OneToMany com la classe Biblioteca e o @ManyToOne com a classe Livros
@Component
public class ConsoleApp {
    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    private final Scanner scanner = new Scanner(System.in);

    public ConsoleApp(LivroRepository livroRepository, BibliotecaRepository bibliotecaRepository) {
        this.livroRepository = livroRepository;
        this.bibliotecaRepository = bibliotecaRepository;
    }

    public void iniciar() {
        System.out.println("Método iniciar() foi chamado com sucesso!");

        while (true) {
            System.out.println("\n==== MENU ====");
            System.out.println("1 - Cadastrar Livro");
            System.out.println("2 - Listar Livros por Biblioteca");
            System.out.println("3 - Buscar por Autor");
            System.out.println("4 - Buscar por Ano");
            System.out.println("5 - Buscar por Título");
            System.out.println("6 - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida! Digite um número.");
                continue;
            }

            switch (opcao) {
                case 1 -> cadastrarLivro();
                case 2 -> listarLivrosPorBiblioteca();
                case 3 -> buscarPorAutor();
                case 4 -> buscarPorAno();
                case 5 -> buscarPorTitulo();
                case 6 -> {
                    System.out.println("Saindo... Obrigado por usar o sistema!");
                    return;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private void cadastrarLivro() {
        System.out.print("Digite o título: ");
        String titulo = scanner.nextLine().trim();

        System.out.print("Digite o autor: ");
        String autor = scanner.nextLine().trim();

        System.out.print("Digite o ano de publicação: ");
        int ano;
        try {
            ano = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ano inválido!");
            return;
        }

        System.out.print("Digite a editora: ");
        String editora = scanner.nextLine().trim();

        System.out.print("Digite o nome da biblioteca: ");
        String nomeBiblioteca = scanner.nextLine().trim();

        Biblioteca biblioteca = buscarOuCriarBiblioteca(nomeBiblioteca);

        if (livroRepository.existsByTituloAndAutor(titulo, autor)) {
            System.out.println("Livro já cadastrado!");
            return;
        }

        Livro livro = new Livro(titulo, autor, ano, editora);
        livro.setBiblioteca(biblioteca);

        livroRepository.save(livro);
        System.out.println("Livro cadastrado com sucesso na biblioteca \"" + biblioteca.getNome() + "\"!");
    }

    private Biblioteca buscarOuCriarBiblioteca(String nome) {
        Optional<Biblioteca> opcional = bibliotecaRepository.findAll()
                .stream().filter(b -> b.getNome().equalsIgnoreCase(nome)).findFirst();

        if (opcional.isPresent()) {
            return opcional.get();
        }

        Biblioteca nova = new Biblioteca();
        nova.setNome(nome);
        nova.setLivros(List.of());
        return bibliotecaRepository.save(nova);
    }

    private void listarLivrosPorBiblioteca() {
        List<Biblioteca> bibliotecas = bibliotecaRepository.findAll();
        if (bibliotecas.isEmpty()) {
            System.out.println("Nenhuma biblioteca encontrada.");
            return;
        }

        System.out.println("\nACERVO ORGANIZADO POR BIBLIOTECA");
        for (Biblioteca b : bibliotecas) {
            System.out.println("\nBiblioteca: " + b.getNome());
            List<Livro> livros = b.getLivros();
            if (livros == null || livros.isEmpty()) {
                System.out.println("  Nenhum livro cadastrado.");
            } else {
                for (Livro l : livros) {
                    System.out.printf("  - %s, por %s (%d, %s)%n",
                            l.getTitulo(), l.getAutor(), l.getAnoPublicacao(), l.getEditora());
                }
            }
        }
    }

    private void buscarPorAutor() {
        System.out.print("Digite o nome do autor: ");
        String autor = scanner.nextLine().trim();

        List<Livro> livros = livroRepository.findByAutor(autor);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para esse autor.");
            return;
        }

        System.out.println("\nLivros encontrados:");
        livros.forEach(l -> System.out.printf("- %s (%d, %s)%n",
                l.getTitulo(), l.getAnoPublicacao(), l.getEditora()));
    }

    private void buscarPorAno() {
        System.out.print("Digite o ano: ");
        int ano;
        try {
            ano = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ano inválido!");
            return;
        }

        List<Livro> livros = livroRepository.findByAnoPublicacao(ano);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para esse ano.");
            return;
        }

        System.out.println("\nLivros publicados em " + ano + ":");
        livros.forEach(l -> System.out.printf("- %s, por %s (%s)%n",
                l.getTitulo(), l.getAutor(), l.getEditora()));
    }

    private void buscarPorTitulo() {
        System.out.print("Digite um termo no título: ");
        String termo = scanner.nextLine().trim();

        List<Livro> livros = livroRepository.findByTituloContainingIgnoreCase(termo);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado com esse título.");
            return;
        }

        System.out.println("\nResultados da busca:");
        livros.forEach(l -> System.out.printf("- %s, por %s (%d)%n",
                l.getTitulo(), l.getAutor(), l.getAnoPublicacao()));
    }
}
