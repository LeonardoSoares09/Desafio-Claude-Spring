# Desafio Técnico — Backend Developer
**Empresa:** Biblion (fictícia)  
**Nível:** Júnior / Pleno Iniciante  
**Stack:** Java 17+ com Spring Boot 3.x  
**Prazo estimado:** 4–8 horas

---

## Contexto

A Biblion é uma plataforma de gestão de bibliotecas comunitárias. Você foi contratado para construir a **API REST** que suporta o módulo de **acervo e empréstimos**.

O frontend já existe (feito por outro time) e vai consumir sua API. O contrato precisa ser respeitado.

---

## O que você vai construir

Uma API REST para gerenciar **livros**, **membros** da biblioteca e **empréstimos**.

---

## Requisitos Funcionais

### Livros

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/books` | Cadastrar novo livro |
| `GET` | `/api/books` | Listar livros do acervo |
| `GET` | `/api/books/{id}` | Buscar livro por ID |
| `PUT` | `/api/books/{id}` | Atualizar dados do livro |
| `DELETE` | `/api/books/{id}` | Remover livro do acervo |

**Payload para criação/atualização de livro:**
```json
{
  "title": "O Senhor dos Anéis",
  "author": "J.R.R. Tolkien",
  "isbn": "978-8533613379",
  "publishedYear": 1954,
  "totalCopies": 3
}
```

> `totalCopies` representa quantos exemplares físicos a biblioteca possui daquele livro.

A listagem deve suportar os seguintes **query params** opcionais:

- `author` — filtrar por autor (busca parcial, case-insensitive)
- `title` — filtrar por título (busca parcial, case-insensitive)
- `available` — se `true`, retorna apenas livros com ao menos um exemplar disponível para empréstimo

---

### Membros

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/members` | Cadastrar novo membro |
| `GET` | `/api/members/{id}` | Buscar membro por ID |
| `GET` | `/api/members/{id}/loans` | Listar empréstimos de um membro |

**Payload para criação de membro:**
```json
{
  "name": "Maria Oliveira",
  "email": "maria@email.com",
  "phone": "51999990000"
}
```

> Email deve ser único — não pode ter dois membros com o mesmo email.

---

### Empréstimos

Esta é a parte central do sistema. Um empréstimo registra que um membro retirou um exemplar de um livro.

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/loans` | Registrar novo empréstimo |
| `POST` | `/api/loans/{id}/return` | Registrar devolução |
| `GET` | `/api/loans/{id}` | Buscar empréstimo por ID |
| `GET` | `/api/loans` | Listar todos os empréstimos |

**Payload para criação de empréstimo:**
```json
{
  "memberId": 1,
  "bookId": 2
}
```

**Regras de negócio obrigatórias:**

1. Ao criar um empréstimo, o sistema define automaticamente a **data de empréstimo** (hoje) e a **data de devolução prevista** (14 dias depois). O cliente não envia essas datas.
2. Não é possível emprestar um livro que não tem **exemplares disponíveis**. Retorne `400` com mensagem explicativa.
3. Um membro não pode ter **mais de 3 empréstimos ativos** ao mesmo tempo. Retorne `400` com mensagem explicativa.
4. Ao registrar a devolução (`POST /api/loans/{id}/return`), o sistema deve:
   - Marcar o empréstimo como devolvido, registrando a data real de devolução
   - Calcular e retornar se há **multa por atraso** (R$ 1,00 por dia de atraso)
5. Não é possível devolver um empréstimo que já foi devolvido.

**Resposta esperada ao registrar devolução:**
```json
{
  "loanId": 5,
  "returnedAt": "2025-06-20",
  "dueDate": "2025-06-15",
  "daysLate": 5,
  "fine": 5.00
}
```

> Se devolvido no prazo, `daysLate` e `fine` devem ser `0`.

A listagem de empréstimos deve suportar os seguintes **query params** opcionais:

- `status` — `ACTIVE` ou `RETURNED`
- `overdue` — se `true`, retorna apenas empréstimos ativos com data de devolução prevista já passada

---

## Requisitos Técnicos

### Obrigatórios

- **Spring Boot 3.x** com **Spring Web** e **Spring Data JPA**
- **Banco de dados**: H2 em memória é suficiente. PostgreSQL é aceito.
- **Validação de entrada** com Bean Validation (`@Valid`, `@NotBlank`, `@NotNull`, etc.)
- **Tratamento de erros centralizado** com `@ControllerAdvice`
  - Não deixar o Spring retornar HTML ou stack trace cru
  - Resposta de erro padronizada:
    ```json
    {
      "status": 400,
      "error": "Bad Request",
      "message": "Não há exemplares disponíveis para o livro solicitado"
    }
    ```
- **HTTP status codes corretos**: `201` para criação, `204` para deleção, `404` quando não encontrado, `400` para regras de negócio violadas
- Código organizado em camadas: `controller`, `service`, `repository`, `dto`, `model`

### Diferenciais (não obrigatórios, mas contam)

- Testes unitários para a camada de `service` com JUnit 5 e Mockito — especialmente as regras de negócio do empréstimo
- Documentação com **SpringDoc OpenAPI** (Swagger UI em `/swagger-ui.html`)
- Paginação na listagem de livros e empréstimos
- `docker-compose.yml` funcional com PostgreSQL

---

## O que **não** precisa fazer

- Autenticação/JWT — fora do escopo desta etapa
- Deploy em cloud
- Frontend

---

## Critérios de Avaliação

Em ordem de importância:

1. **Corretude** — os endpoints funcionam? As regras de negócio estão implementadas?
2. **Tratamento de erros** — a API se comporta bem com inputs inválidos e regras violadas?
3. **Organização** — as camadas estão separadas? Tem lógica de negócio no controller?
4. **Qualidade do código** — nomes legíveis, sem código morto, sem `System.out.println`
5. **Uso correto de HTTP** — verbos e status codes adequados

---

## Entrega

- Repositório público no **GitHub**
- `README.md` com:
  - Como rodar o projeto localmente
  - Exemplos de requisição (curl ou link para Swagger)

---

## Observação final

Este desafio é intencional em **não especificar tudo**. Algumas decisões de modelagem e design são suas — por exemplo, como representar a disponibilidade de exemplares, ou como estruturar o status do empréstimo. O que avaliamos não é só se funciona, mas como você pensa quando tem autonomia.

Boa sorte.