package com.alura.literalura;

import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Libro;
import com.alura.literalura.model.RespuestaAPI;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    @Autowired
    private LibroRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner lectura = new Scanner(System.in);
        ConsumoAPI consumoApi = new ConsumoAPI();
        ConvierteDatos conversor = new ConvierteDatos();
        String urlBase = "https://gutendex.com/books/?search=";

        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = lectura.nextInt();
            lectura.nextLine(); // Consumir nueva línea

            switch (opcion) {
                case 1:
                    System.out.println("Escribe el título del libro:");
                    String nombreLibro = lectura.nextLine();
                    String json = consumoApi.obtenerDatos(urlBase + nombreLibro.replace(" ", "+"));
                    RespuestaAPI datos = conversor.obtenerDatos(json, RespuestaAPI.class);

                    if (datos.resultado().isEmpty()) {
                        System.out.println("Libro no encontrado.");
                    } else {
                        DatosLibro primerLibro = datos.resultado().get(0);
                        Libro libro = new Libro(primerLibro);
                        
                        // Validación para no repetir libros
                        if(repository.findByTituloContainsIgnoreCase(libro.getTitulo()).isPresent()){
                            System.out.println("El libro ya está registrado.");
                        } else {
                            repository.save(libro);
                            System.out.println("Libro guardado: " + libro);
                        }
                    }
                    break;
                case 2:
                    List<Libro> libros = repository.findAll();
                    libros.forEach(System::println);
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
}