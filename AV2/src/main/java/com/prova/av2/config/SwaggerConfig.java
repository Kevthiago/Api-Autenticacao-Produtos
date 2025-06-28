package com.prova.av2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Autenticação e Gestão de Produtos - JWT")
                .version("1.0.0")
                .description("""
                    Esta API fornece funcionalidades de autenticação com JWT e acesso a endpoints protegidos, como produtos e categorias.

                    ### 🔐 Funcionalidades de Autenticação:
                    - Login com usuário e senha via `/auth/login`
                    - Validação de token via `/auth/validate`
                    - Acesso autenticado a `/hello`
                    - Acesso restrito a administradores via `/admin`

                    ### 📦 Endpoints de Produto (`/produto`)
                    - `GET /produto` — Listar todos os produtos (USER e ADMIN)
                    - `GET /produto/{id}` — Buscar produto por ID (USER e ADMIN)
                    - `POST /produto` — Cadastrar novo produto (autenticado)
                    - `PUT /produto/{id}` — Atualizar produto (autenticado)
                    - `DELETE /produto/{id}` — Remover produto (apenas ADMIN)

                    ### 🗂️ Endpoints de Categoria (`/categoria`)
                    - `GET /categoria` — Listar todas as categorias (USER e ADMIN)
                    - `GET /categoria/{id}` — Buscar categoria por ID (USER e ADMIN)
                    - `POST /categoria` — Cadastrar nova categoria (autenticado)
                    - `PUT /categoria/{id}` — Atualizar categoria (autenticado)
                    - `DELETE /categoria/{id}` — Remover categoria (apenas ADMIN)

                    ### ✅ Como usar:
                    1. Faça login em `/auth/login` com seu usuário e senha.
                    2. Copie o token JWT retornado.
                    3. Valide o token em `/auth/validate` se desejar.
                    4. Clique no botão **"Authorize"** no topo da página do Swagger.
                    5. Cole o token no formato: `seu_token_aqui`
                    6. Pronto! Agora você pode testar os endpoints protegidos.
                    """)
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact()
                    .name("Kevin Thiago")
                    .email("kevinthiago126@gmail.com")
                    .url("https://github.com/Kevthiago"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
            )
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Informe um token JWT válido no formato: `<seu_token>`")
                )
            );
    }
}
