# Sistema de Gerenciamento de Biblioteca - Arquitetura Microkernel

Sistema de gerenciamento de biblioteca implementado usando arquitetura de microkernel com JavaFX e MariaDB.

## 🚀 Execução do Projeto

### Pré-requisitos

- **Java 11+**
- **Maven 3.6+**
- **Docker** e **Docker Compose**

### Comandos de Execução

```bash
# 1. Navegar para o diretório do projeto
cd microkernel

# 2. Iniciar o banco de dados
docker-compose up -d

# 3. Compilar o projeto
mvn clean install

# 4. Executar a aplicação
mvn exec:java -pl app
```

**Configurações do Banco:**

- Host: localhost:3306
- Usuário: root / Senha: root
- Database: library

## 📖 Visão Geral

O Sistema de Gerenciamento de Biblioteca é uma aplicação JavaFX que utiliza uma arquitetura de microkernel para fornecer flexibilidade e extensibilidade através de plugins. O sistema permite o gerenciamento completo de usuários, livros, empréstimos e relatórios através de uma interface gráfica moderna e intuitiva.

### Características Principais

- **Arquitetura Microkernel**: Core mínimo com funcionalidades extensíveis
- **Interface JavaFX**: Interface moderna com sistema de abas
- **Banco MariaDB**: Persistência robusta de dados
- **Sistema de Plugins**: Módulos independentes e intercambiáveis
- **Gerenciamento Completo**: CRUD para todas as entidades

## 🏗️ Estrutura do Projeto

```
microkernel/
├── app/                    # Core da aplicação (microkernel)
│   └── src/main/java/br/edu/ifba/inf008/
│       ├── App.java                    # Classe principal
│       └── shell/                      # Controllers do sistema
├── interfaces/             # Interfaces compartilhadas
│   └── src/main/java/br/edu/ifba/inf008/interfaces/
├── plugins/               # Plugins do sistema
│   ├── book-plugin/       # Gerenciamento de livros
│   ├── user-plugin/       # Gerenciamento de usuários
│   ├── loan-plugin/       # Controle de empréstimos
│   └── report-plugin/     # Relatórios e estatísticas
├── docker-T2/            # Configuração do banco
│   ├── docker-compose.yml
│   └── init.sql
└── pom.xml               # Configuração Maven parent
```

## 🔌 Plugins Disponíveis

| Plugin           | Funcionalidades                                    |
| ---------------- | -------------------------------------------------- |
| **BookPlugin**   | CRUD de livros, gerenciamento de estoque           |
| **UserPlugin**   | CRUD de usuários, cadastro de clientes             |
| **LoanPlugin**   | Empréstimos, devoluções, controle de prazos        |
| **ReportPlugin** | Relatórios de empréstimos, estatísticas do sistema |

## 🗄️ Modelo de Dados

O sistema utiliza as seguintes entidades principais:

- **User**: Gerenciamento de usuários/clientes
- **Book**: Catálogo de livros da biblioteca
- **Loan**: Controle de empréstimos e devoluções

## 🎨 Interface do Usuário

- **Design Modular**: Cada plugin possui sua própria interface
- **Navegação por Abas**: Sistema de abas para diferentes funcionalidades
- **Menu Dinâmico**: Menus criados automaticamente pelos plugins
- **Formulários Responsivos**: Interface adaptativa para diferentes operações

## 🔧 Desenvolvimento de Plugins

### Criando um Novo Plugin

1. **Criar pasta do plugin** em `plugins/`:

```bash
mkdir plugins/meu-plugin
```

2. **Adicionar módulo** no `pom.xml` principal:

```xml
<modules>
    <module>interfaces</module>
    <module>app</module>
    <module>plugins/user-plugin</module>
    <module>plugins/book-plugin</module>
    <module>plugins/loan-plugin</module>
    <module>plugins/report-plugin</module>
    <module>plugins/meu-plugin</module>  <!-- ADICIONAR AQUI -->
</modules>
```

3. **Criar pom.xml** do plugin (usar um plugin existente como base)

4. **Implementar a interface IPlugin**:

```java
package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;

public class MeuPlugin implements IPlugin {
    @Override
    public boolean init() {
        // Implementar inicialização do plugin
        return true;
    }
}
```

5. **Compilar e executar**:

```bash
mvn clean install
mvn exec:java -pl app
```

### Convenções de Nomenclatura

- Classe principal: `br.edu.ifba.inf008.plugins.NomePlugin`
- Arquivo JAR: `NomePlugin.jar`

## 🔧 Comandos Úteis

```bash
# Parar o banco de dados
docker-compose down

# Logs do banco de dados
docker logs library-db

# Compilar apenas um plugin específico
cd plugins/book-plugin && mvn clean package

# Executar com JavaFX (se necessário)
mvn javafx:run -pl app
```

## 🚀 Arquitetura Microkernel

O sistema utiliza o padrão **Microkernel** onde:

- **Core (App)**: Fornece funcionalidades básicas e gerenciamento de plugins
- **Plugins**: Implementam funcionalidades específicas de negócio
- **Interfaces**: Definem contratos entre o core e os plugins

Isso permite:

- ✅ **Extensibilidade**: Novos plugins podem ser adicionados facilmente
- ✅ **Manutenibilidade**: Cada plugin é independente
- ✅ **Flexibilidade**: Plugins podem ser removidos/atualizados sem afetar o core
- ✅ **Reutilização**: Interfaces padronizadas facilitam o desenvolvimento

---

**Desenvolvido por:** Rafael Dantas Silva
