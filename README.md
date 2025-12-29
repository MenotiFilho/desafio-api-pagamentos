# 🚀 Desafio Backend: API de Pagamentos Simplificada

---

# O Contexto

Você deve construir uma API RESTful para uma plataforma de pagamentos simplificada (estilo PicPay/Nubank). O objetivo principal é gerenciar carteiras de usuários e realizar transferências de valores entre eles com segurança.

## Requisitos Funcionais

### 1. Cadastro de Usuários

A API deve permitir criar usuários.

- O usuário deve ter: `Nome Completo`, `CPF` (único), `E-mail` (único) e `Senha`.
- O usuário começa com uma carteira com saldo `0.00`.
- **Diferencial:** Permitir que o usuário já comece com um saldo inicial informado no cadastro (apenas para facilitar seus testes manuais).

### 2. Transação (Transferência)

A API deve possuir um endpoint para realizar transferência de valores entre dois usuários.

- **Regra de Negócio 1:** O usuário não pode transferir dinheiro se não tiver saldo suficiente.
- **Regra de Negócio 2:** O usuário não pode transferir dinheiro para ele mesmo.
- **Regra de Negócio 3:** A operação deve ser atômica. Se algo der errado no meio do caminho (ex: debitou de um mas falhou ao creditar no outro), tudo deve ser desfeito (Rollback).

### 3. Consulta

- Um endpoint para consultar o saldo ou os dados de um usuário pelo ID.

---

## Requisitos Técnicos (O que vamos avaliar)

1. **Stack:** Java 17+ e Spring Boot 3+.
2. **Banco de Dados:** H2 (em memória) para facilitar a execução, ou PostgreSQL/MySQL (via Docker) se preferir.
3. **Arquitetura:** O projeto deve estar organizado em camadas (Controller, Service, Repository, Entity/Domain).
4. **Tratamento de Erros:**
    - Não queremos ver StackTrace estourando na cara do cliente.
    - Retorne status codes adequados (400 para erro de validação/saldo insuficiente, 404 para usuário não encontrado, 201 para criado, etc).
    - Use `@ControllerAdvice` e `@ExceptionHandler`.
5. **Validações:**
    - Use Bean Validation (`@NotNull`, `@Email`, `@CPF` se quiser usar lib extra ou regex, etc) nos DTOs de entrada.
6. **Tipagem de Dinheiro:**
    - **Atenção:** Dinheiro não é `double` nem `float`. Esperamos ver o uso do tipo de dados correto para valores monetários em Java.

## O que será considerado um diferencial (Bônus)

- **DTOs:** Uso de classes DTO (Data Transfer Object) para separar o que chega da API da sua Entidade de Banco (não expor a entidade JPA diretamente no Controller).
- **Testes:** Testes unitários na camada de Service (JUnit + Mockito).

---

## Exemplo de Payload (Sugestão)

**POST /transferencias**

JSON

# 

`{
  "payerId": 1,
  "payeeId": 2,
  "value": 100.00
}`

---

## 🕒 Instruções de Entrega

Você não tem limite de tempo rígido, mas esperamos que isso não tome mais do que algumas horas do seu dia.

**Sua missão:**

1. Escreva o código.
2. Cole aqui no chat as classes principais (Entidade, Repository, Service, Controller e o ExceptionHandler). **Não precisa colar imports ou arquivos de configuração (pom.xml/application.properties) a menos que tenha algo muito específico.**
3. Se tiver dúvidas sobre regras não especificadas, **assuma a decisão técnica** que achar melhor e justifique (nós avaliamos capacidade de decisão).

**Pode começar.** Boa sorte!
