package com.projetoforum.forum.controller;

import com.projetoforum.forum.config.security.TokenService;
import com.projetoforum.forum.controller.dto.LoginDto;
import com.projetoforum.forum.model.Perfil;
import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.model.Usuario;
import com.projetoforum.forum.service.RespostaService;
import com.projetoforum.forum.service.TopicoService;
import com.projetoforum.forum.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class RespostaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    @Autowired
    TopicoService topicoService;

    LoginDto loginDto;

    Authentication authentication;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RespostaService respostaService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void responder() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);

        Topico topico = new Topico();
        topico.setTitulo("Teste");
        topico.setMensagem("Testando");
        topico.setTag("teste");
        topico.setAutor(usuario);
        topicoService.save(topico);

        String id = topico.getId();

        String json = "{\"mensagem\":\"Teste\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .post("/topico/responder/{id}", id)
                        .header("Authorization","Bearer "+token)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(topico);
        mongoTemplate.remove(usuario);

    }

    @Test
    void editarResposta() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);

        Topico topico = new Topico();
        topico.setTitulo("Teste");
        topico.setMensagem("Testando");
        topico.setTag("teste");
        topico.setAutor(usuario);
        topicoService.save(topico);

        Resposta resposta = new Resposta();
        resposta.setMensagem("teste");
        resposta.setAutor(usuario);
        resposta.setTopico_id(topico.getId());
        respostaService.save(resposta);

        topico.addResposta(resposta);
        topicoService.save(topico);

        String id = resposta.getId();

        String json = "{\"mensagem\":\"Atualizando\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .put("/topico/resposta/editar/{id}", id)
                        .header("Authorization","Bearer "+token)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(topico);
        mongoTemplate.remove(usuario);
        mongoTemplate.remove(resposta);
    }

    @Test
    void deletarResposta() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);

        Topico topico = new Topico();
        topico.setTitulo("Teste");
        topico.setMensagem("Testando");
        topico.setTag("teste");
        topico.setAutor(usuario);
        topicoService.save(topico);

        Resposta resposta = new Resposta();
        resposta.setMensagem("teste");
        resposta.setAutor(usuario);
        resposta.setTopico_id(topico.getId());
        respostaService.save(resposta);

        topico.addResposta(resposta);
        topicoService.save(topico);

        String id = resposta.getId();

        mvc
                .perform(MockMvcRequestBuilders
                        .delete("/topico/resposta/deletarresposta/{id}", id)
                        .header("Authorization","Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(topico);
        mongoTemplate.remove(usuario);

    }

    @Test
    void listar() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@testando.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        Perfil perfil = new Perfil();
        perfil.setNome("ROLE_USER");
        usuario.addPerfil(perfil);
        usuarioService.save(usuario);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("teste@testando.com");
        loginDto.setSenha("123456");

        UsernamePasswordAuthenticationToken dadosLogin = loginDto.converter();
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);

        Topico topico = new Topico();
        topico.setTitulo("Teste");
        topico.setMensagem("Testando");
        topico.setTag("teste");
        topico.setAutor(usuario);
        topicoService.save(topico);

        Resposta resposta = new Resposta();
        resposta.setMensagem("teste");
        resposta.setAutor(usuario);
        resposta.setTopico_id(topico.getId());
        respostaService.save(resposta);

        topico.addResposta(resposta);
        topicoService.save(topico);

        String id = topico.getId();

        mvc
                .perform(MockMvcRequestBuilders
                        .get("/topico/respostas/listar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(topico);
        mongoTemplate.remove(usuario);
        mongoTemplate.remove(resposta);
    }
}