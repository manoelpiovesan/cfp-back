# Call for Papers

Tecnologias usadas:

- Quarkus
- Postgres
- Docker
- Flutter (Frontend)

## Rodando o projeto

Antes de rodar o projeto, você deve ter o Docker e o Docker Compose instalados na sua máquina. Para mais informações,
acesse a [documentação oficial](https://docs.docker.com/get-docker/).

1. Clone este repositorio

```bash
git clone https://github.com/manoelpiovesan/cfp-back.git
```

⚠️ Nota: O backend está configurado para lidar com autenticação via JWT, então você precisa configurar as chaves
públicas e
privadas para que o backend possa assinar e verificar os tokens. Para isso, você deve seguir os passos presentes no
repositório:
[manoelpiovesan/quarkus-jwt](https://github.com/manoelpiovesan/quarkus-jwt)

2. Dentro do repositório, faça o build do backend Quarkus

```bash
./gradlew clean build
```

Para facilitar o processo de execução do projeto, o frontend, criado em Flutter, foi pré-buildado e está presente no
diretório `frontend`.
Você pode ver o código fonte do frontend em [manoelpiovesan/cfp-front](https://github.com/manoelpiovesan/cfp-front)

3. Agora suba a stack do projeto com o docker compose

```bash
docker compose up
```

Tudo pronto! Agora você pode acessar a documentação da API (Swagger)
em [http://localhost:8080/api/swagger-ui/](http://localhost:8080/q/swagger-ui/)
e o frontend em [http://localhost:8081](http://localhost:8081).

## Diagrama de Entidade-Relacionamento do Banco de Dados

```mermaid
classDiagram
    class PaperProjection {
        String title
        String summary
        String author
        String email
        String submittedAt
    }
    class User {
        String firstName
        String lastName
        String email
        String password
        String role
        String createdAt
        String updatedAt
        String deletedAt
    }
    class Paper {
        String title
        String summary
        String createdAt
        String updatedAt
        String deletedAt
    }
    User "1" -- "N" Paper: has
```

## Capturas de Tela

Lista de Papers
<img src="readme_files/image1.png" alt="">

Tela de Submissão
<img src="readme_files/image2.png" alt="">

Tela de Login
<img src="readme_files/image3.png" alt="">

Busca por Papers (com filtro)
<img src="readme_files/image4.png" alt="">

Registro de Usuário
<img src="readme_files/image5.png" alt="">

## Exemplo de submissão

[Vídeo de exemplo](https://youtu.be/UWyIPV5Uk1k)

