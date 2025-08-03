# User Plugin

Plugin de gerenciamento de usuários para o sistema microkernel.

## Funcionalidades

- **Listagem de usuários**: Visualiza todos os usuários cadastrados
- **Pesquisa**: Busca usuários por nome
- **Cadastro**: Adiciona novos usuários
- **Edição**: Modifica dados de usuários existentes
- **Exclusão**: Remove usuários do sistema
- **Validação**: Valida dados de entrada (nome, email)
- **Interface moderna**: UI responsiva com JavaFX

## Estrutura do Plugin

```
user-plugin/
├── src/main/java/br/edu/ifba/inf008/plugins/
│   ├── UserPlugin.java                 # Classe principal do plugin
│   └── user/
│       ├── model/
│       │   └── User.java              # Modelo de dados do usuário
│       ├── dao/
│       │   ├── DatabaseConnection.java # Conexão com banco
│       │   └── UserDAO.java           # Acesso a dados
│       ├── service/
│       │   └── UserService.java       # Lógica de negócio
│       ├── components/
│       │   ├── UserFormComponent.java  # Componente do formulário
│       │   └── UserTableComponent.java # Componente da tabela
│       └── views/
│           ├── UserListView.java       # View principal
│           └── UserFormDialog.java     # Dialog do formulário
└── src/main/resources/
    └── styles/
        └── user-plugin.css           # Estilos CSS
```

## Tecnologias

- **Java 11+**
- **JavaFX** - Interface gráfica
- **MariaDB** - Banco de dados
- **Maven** - Gerenciamento de dependências

## Configuração do Banco

O plugin conecta automaticamente ao banco MariaDB com as seguintes configurações:

- **Host**: localhost
- **Porta**: 3307
- **Database**: bookstore
- **Usuário**: root
- **Senha**: root

### Tabela de Usuários

```sql
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Como Usar

1. **Compilar**: Execute `mvn install` no diretório raiz do microkernel
2. **Executar**: Inicie a aplicação principal
3. **Acessar**: O plugin criará automaticamente:
   - Menu "Cadastros" > "Usuários"
   - Aba "Usuários" na interface principal

## Operações Disponíveis

### Listagem

- Visualiza todos os usuários em uma tabela
- Mostra ID, Nome, Email e Data de Cadastro
- Atualização automática após operações

### Pesquisa

- Campo de busca em tempo real
- Pesquisa por nome (busca parcial)
- Resultados instantâneos

### Cadastro/Edição

- Formulário com validação em tempo real
- Campos obrigatórios: Nome e Email
- Validação de formato de email
- Verificação de unicidade do email

### Exclusão

- Confirmação antes da exclusão
- Exclusão segura com feedback

## Validações

- **Nome**: Obrigatório, 2-100 caracteres
- **Email**: Obrigatório, formato válido, único no sistema

## Dependências

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>11.0.2</version>
    </dependency>
    <dependency>
        <groupId>org.mariadb.jdbc</groupId>
        <artifactId>mariadb-java-client</artifactId>
        <version>3.0.8</version>
    </dependency>
</dependencies>
```

## Arquitetura

O plugin segue o padrão de arquitetura em camadas:

1. **View Layer**: Interfaces gráficas (Views e Components)
2. **Service Layer**: Lógica de negócio e validações
3. **DAO Layer**: Acesso a dados
4. **Model Layer**: Entidades de domínio

## Tratamento de Erros

- Conexões de banco são tratadas com try-catch
- Operações assíncronas para não bloquear a UI
- Feedback visual para todas as operações
- Mensagens de erro amigáveis ao usuário

## Performance

- Operações de banco executadas em background
- Interface não-bloqueante
- Carregamento sob demanda
- Reutilização de componentes
