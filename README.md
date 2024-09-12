# Desafio Técnico - Sistema de Gerenciamento de Tarefas

## Descrição

Este projeto é uma aplicação web para gerenciamento de listas e itens. Permite aos usuários criar e gerenciar listas, adicionar, editar, remover itens, e destacar itens para indicar prioridade. A aplicação inclui uma API para operações principais e testes automatizados para garantir a qualidade.

## Requisitos Funcionais

1. **Criação de Listas**: Crie e gerencie listas, cada uma com itens associados.
2. **Gerenciamento de Itens**: Adicione, edite, remova e altere o estado dos itens dentro das listas.
3. **Visualização e Filtragem**: Visualize e organize listas e itens com opções de filtragem.
4. **Prioridade de Itens**: Destaque itens dentro das listas para indicar prioridade.

## Tecnologias Utilizadas

- ![Java](https://img.shields.io/badge/Java-007396?style=flat&logo=java&logoColor=white) Java
- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=springboot&logoColor=white) Spring Boot
- ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat&logo=spring&logoColor=white) Spring Data JPA
- ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat&logo=postgresql&logoColor=white) PostgreSQL
- ![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white) Maven
- ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white) Postman
- ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=white) Swagger
- ![JUnit](https://img.shields.io/badge/JUnit-25A162?style=flat&logo=junit5&logoColor=white) JUnit


## Instruções para Execução Local

### 1. Clonando o Repositório

Clone o repositório para sua máquina local utilizando o comando:

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio

```

### 2. Configurando o Ambiente

a. Configuração do Banco de Dados
Certifique-se de ter um banco de dados relacional instalado (por exemplo o PostgreSQL).

Crie um banco de dados e um usuário para a aplicação, se necessário. Em seguida, configure o banco de dados no arquivo de propriedades da aplicação.

b. Arquivo de Propriedades
Localize o arquivo de configuração de propriedades da aplicação (application.properties), e configure as seguintes propriedades para o banco de dados local:

```
# Configurações do Banco de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```
Substitua nome_do_banco, seu_usuario, e sua_senha pelos valores adequados.

### 3. Instalando Dependências

Execute o comando abaixo para instalar as dependencias necessárias.

```bash
mvn install
```

### 4. Execute a Aplicação

Para executar a aplicação localmente, utilize o comando Maven:

```bash
mvn spring-boot:run
```

## Testando a API com Postman

### 1. Importando a Coleção do Postman

1. **Abra o Postman** em seu computador.
2. **Clique no ícone de importação** (normalmente um botão com uma seta para baixo e uma pasta) no canto superior esquerdo da interface do Postman.
3. **Selecione "Importar arquivo"** e navegue até a pasta `postman` no seu projeto.
4. **Escolha o arquivo da coleção** (geralmente um arquivo `.json`) e clique em "Abrir" para importar a coleção.

### 2. Executando as Requisições

1. **Abra a coleção importada** no Postman.
2. **Expanda a coleção** para ver as requisições disponíveis.
3. **Selecione uma requisição** que deseja testar.
4. **Clique em "Enviar"** para executar a requisição e ver a resposta da API.

## Documentação

Quando a aplicação estiver rodando acesse o link para acessar o Swagger que foi desenvolviedo, ali será possivel testar os endpoints bem como os parâmteros e critérios necessários para cada um deles.

Link do Swagger -  http://localhost:8080/swagger-ui/index.html#/





