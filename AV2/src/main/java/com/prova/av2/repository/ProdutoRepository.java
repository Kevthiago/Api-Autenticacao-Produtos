package com.prova.av2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prova.av2.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
}
