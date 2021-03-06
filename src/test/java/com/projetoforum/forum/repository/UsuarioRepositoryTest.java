package com.projetoforum.forum.repository;

import com.projetoforum.forum.model.Usuario;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void encontraUsuarioPeloEmail() {
        String emailUsuario = "testando@ibm.com";

        Usuario usuario = new Usuario();
        usuario.setNome("Joao");
        usuario.setEmail(emailUsuario);
        usuario.setSenha("123");
        mongoTemplate.save(usuario);

        usuario = usuarioRepository.findUsuarioByEmail(emailUsuario);
        Assert.assertNotNull(usuario);
        Assert.assertEquals(emailUsuario, usuario.getEmail());

        mongoTemplate.remove(usuarioRepository.findUsuarioByEmail("testando@ibm.com"));

    }

    @Test
    public void naoEncontraUsuarioCasoEmailNaoExista() {

        String emailUsuario = "testando@ibm.com";

        Usuario usuario = usuarioRepository.findUsuarioByEmail(emailUsuario);
        Assert.assertNull(usuario);
    }

    @Test
    public void deletaUsuarioPeloEmail() {
        String emailUsuario = "testando@ibm.com";

        Usuario usuario = new Usuario();
        usuario.setNome("Joao");
        usuario.setEmail(emailUsuario);
        usuario.setSenha("123");
        mongoTemplate.save(usuario);

        usuarioRepository.deleteUsuarioByEmail(emailUsuario);
        usuario = usuarioRepository.findUsuarioByEmail(emailUsuario);
        Assert.assertNull(usuario);
    }

    @Test
    public void naoDeletaUsuarioPeloEmail() {
        String emailUsuario = "testando@ibm.com";
        String emailErrado = "naoExiste@ibm.com";

        Usuario usuario = new Usuario();
        usuario.setNome("Joao");
        usuario.setEmail(emailUsuario);
        usuario.setSenha("123");
        mongoTemplate.save(usuario);

        usuarioRepository.deleteUsuarioByEmail(emailErrado);
        usuario = usuarioRepository.findUsuarioByEmail(emailUsuario);
        Assert.assertNotNull(usuario);

        mongoTemplate.remove(usuario);
    }

}