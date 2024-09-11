package com.api.v1.tarefas.configuration;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Gerenciamento de Tarefas",
                version = "v1",
                description = "A API de Gerenciamento de Tarefas é uma solução web que permite aos usuários criar e gerenciar listas de tarefas, com funcionalidades como criação de listas, gerenciamento de itens, priorização e organização intuitiva. " +
                        "Os usuários podem adicionar, editar, remover e alterar o estado dos itens nas listas, além de destacá-los por prioridade. A API oferece persistência de dados robusta e testes automatizados, proporcionando uma plataforma eficiente para o controle de tarefas. " +
                        "Desenvolvida com Java 17, Spring Boot e JPA, esta aplicação é escalável e adaptável para diferentes cenários de uso. " +
                        "Entre em contato para mais informações.",
                contact = @Contact(name = "Arthur Felix", email = "dev.felixarthur@gmail.com")
        ),
        externalDocs = @ExternalDocumentation(description = "Documentação completa sobre as funcionalidades e a arquitetura da API")
)

public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("API_Tarefas")
                .packagesToScan("com.api.v1.tarefas")
                .pathsToMatch("/**")
                .build();
    }
}
