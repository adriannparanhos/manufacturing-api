# 🏭 Manufacturing API

API REST desenvolvida como parte do teste técnico para a vaga de Full Stack. O sistema gerencia o controle de produção, estoques de matérias-primas e cálculo de planejamento produtivo.

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Quarkus Framework** (Core, REST, Hibernate ORM Panache)
- **Oracle Database** (via Docker)
- **JUnit 5 & Mockito** (Testes Unitários)
- **Docker** (Infraestrutura de Banco de Dados)

## ⚙️ Pré-requisitos

Para executar este projeto, você precisará de:

- **JDK 21** instalado.
- **Docker** rodando na sua máquina (para o Banco de Dados).
- **Maven**

## 🗄️ Configuração do Banco de Dados (Docker)

O projeto utiliza o Oracle Database XE. Para subir o banco de dados rapidamente, execute o seguinte comando no seu terminal:

```bash
docker run -d --name oracle-db \
  -e ORACLE_PASSWORD=oracle \
  -p 1521:1521 \
  gvenzl/oracle-xe
```

# 🛠️ Como Rodar a Aplicação
Clone o repositório e entre na pasta:

````bash
git clone <URL_DO_SEU_REPOSITORIO>
cd manufacturing-api
````

Inicie a aplicação em modo de desenvolvimento: Este modo habilita o Live Reload e logs detalhados.

# 🔌 Documentação dos Endpoints
Abaixo estão as principais rotas disponíveis na API.

## 📦 Produtos (/products)
Gerencia os itens produzidos pela fábrica e suas receitas.

| Método | Endpoint         | Descrição                                                                 |
|--------|------------------|---------------------------------------------------------------------------|
| GET    | /products        | Lista todos os produtos e suas receitas.                                  |
| POST   | /products        | Cria um novo produto com sua composição (ingredientes).                   |
| DELETE | /products/{id}   | Remove um produto. (Cascade Delete: Remove também a receita e o histórico de produção). |

## 🧱 Matérias-Primas (/raw-materials)
Gerencia o estoque de insumos.

| Método | Endpoint              | Descrição                                                  |
|--------|-----------------------|------------------------------------------------------------|
| GET    | /raw-materials        | Lista todos os insumos e quantidade em estoque.           |
| POST   | /raw-materials        | Cria uma nova matéria-prima.                               |
| DELETE | /raw-materials/{id}   | Remove uma matéria-prima do estoque.                       |

## 📅 Planejamento & Produção (/planning)
O "cérebro" da aplicação.

| Método | Endpoint     | Descrição |
|--------|-------------|-----------|
| GET    | /planning   | Algoritmo Sugestivo: Retorna uma lista automática do que deve ser produzido baseada no estoque atual, priorizando produtos de maior valor agregado (Algoritmo Guloso). |
| POST   | /planning   | Ordem de Produção: Recebe `{productId, quantity}`, valida se há estoque suficiente, debita os materiais e registra o histórico. Retorna erro 400 se o estoque for insuficiente. |


# 🧪 Testes Automatizados
O projeto possui uma suíte de testes robusta cobrindo camadas de serviço e integração.

Para executar todos os testes:

````bash
./mvnw test
````
O que está sendo testado?

### Testes Unitários (ProductionPlanningServiceTest):
* Utiliza Mockito para simular o banco de dados.
* Valida a lógica de cálculo de estoque (se impede produção sem insumo).
* Valida a lógica de priorização de produtos.

### Testes de Integração (PlanningResourceTest):
* Utiliza RestAssured.
* obe o contexto do Quarkus e faz requisições HTTP reais para os endpoints.
* Valida os Status Codes (200, 400, 204) e a estrutura do JSON retornado.

##### **Desenvolvido por Adriann Postigo Paranhos**