[![Build Status](https://travis-ci.org/ramoncunha/ponto-inteligente-api.svg?branch=master)](https://travis-ci.org/ramoncunha/ponto-inteligente-api)

# API Ponto Inteligente
📜 API do sistema de Ponto Inteligente

💡 Faça o controle de ponto dos seus colaboradores com esta API. Contamos com segurança JWT para mais segurança e cache para maior performance.

## 📈 Exemplo de uso

Verifique os recursos através do Swagger acessando
http://localhost:8080/swagger-ui.html#/

Crie um funcionário para que você possa realizar a autenticação.

Faça uma requisição POST criando um CadastroPJ, após isso, faça uma requisição POST para criar um CadastroPF e utilize e-mail e senha para autenticar no próximo passo.


Faça uma requisição POST para /auth. No body deve conter o seguinte objeto:
```sh
{
    "email": "email@email.com",
    "senha": "password"
}
```

Utilize o token retornado para as próximas requisições no sistema. Adicione no header "Bearer {token}".

## 💻 Configuração para Desenvolvimento

Execute os comandos abaixo para buildar o projeto.
```sh
mvn clean compile
```
 
 Também será necessário subir o container MySQL para que seja possível a comunicação com o BD.
```sh
docker-compose up
``` 
Crie um banco de dados chamado ponto_inteligente.
Execute a aplicação e as tabelas serão criadas pelo Flyway.

## 🗃 Histórico de lançamentos

* 0.0.1
    * Versão inicial