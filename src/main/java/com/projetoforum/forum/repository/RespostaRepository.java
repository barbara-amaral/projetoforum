package com.projetoforum.forum.repository;

import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.Topico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespostaRepository extends MongoRepository<Resposta, String> {

    Resposta getById(String id);
    List<Resposta> findRespostaByAutorNome(String nome);
}
