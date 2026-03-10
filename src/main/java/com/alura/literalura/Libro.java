package com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String autor;
    private String idioma;
    private Double numeroDeDescargas;

    public Libro() {}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.autor = datosLibro.autor().get(0).nombre(); // Tomamos el primer autor
        this.idioma = datosLibro.idiomas().get(0); // Tomamos el primer idioma
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }

    // Getters y Setters (obligatorios para JPA)
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIdioma() { return idioma; }
    public Double getNumeroDeDescargas() { return numeroDeDescargas; }

    @Override
    public String toString() {
        return "Libro: " + titulo + " | Autor: " + autor + " | Idioma: " + idioma;
    }
}