package com.prova.av2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prova.av2.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
}
