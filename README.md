# DIO Bank - Sistema Bancário Simulado

Este é um sistema bancário simples desenvolvido em Java que simula operações básicas com contas, carteiras de investimentos e transações via PIX. O projeto foi criado com foco em aprendizado, boas práticas de orientação a objetos, uso de BigDecimal para operações financeiras, tratamento de exceções, e testes unitários.

## ✨ Funcionalidades

- ✅ Criar contas com múltiplas chaves PIX
- ✅ Criar investimentos com taxa personalizada
- ✅ Depositar e sacar valores de contas
- ✅ Transferir valores entre contas via PIX
- ✅ Criar carteiras de investimento a partir de contas
- ✅ Realizar aportes e resgates em carteiras de investimento
- ✅ Atualizar automaticamente os rendimentos de investimentos
- ✅ Consultar o histórico de transações por PIX
- ✅ Suporte a múltiplas exceções específicas (ex: conta não encontrada, fundos insuficientes, PIX já em uso)

---

## 🧱 Arquitetura

- **Camada de modelo (**``**)**: Representa as entidades principais como `AccountWallet`, `Investment`, `InvestmentWallet` e `Money`.
- **Repositórios (**``**)**: Responsáveis por armazenar os dados em memória.
- **Camada de serviço (sugestão)**: Pode ser usada para separar lógica de negócio da interface.
- **Classe principal (**``**)**: Interface de linha de comando com menu interativo.
- **Exceções (**``**)**: Classes personalizadas para regras de negócio.
- **Utils**: Utilitário para formatação de valores em moeda brasileira.

---

## 📋 Exemplo de Uso

### Menu Interativo

Ao executar o programa, será apresentado um menu como este:

```
Bem-vindo ao DIO Bank
Selecione uma opção:
1. Criar Conta
2. Criar Investimento
3. Criar Carteira de Investimento
4. Depositar na Conta
5. Sacar da Conta
6. Transferir Dinheiro
7. Depositar no Investimento
8. Resgatar do Investimento
9. Listar Contas
10. Listar Investimentos
11. Listar Carteiras de Investimento
12. Atualizar Investimentos
13. Histórico da Conta
14. Sair
```

### Criação de Conta

- Solicita as chaves PIX (separadas por vírgula)
- Solicita o valor inicial (ex: `100,00`)
- Exibe mensagem de sucesso com o saldo formatado

---

## 🧰 Testes

Os testes de unidade foram implementados com **JUnit 5**, cobrindo:

- Criação de conta
- Criação de investimento
- Criação de carteiras de investimento
- Depósitos, saques e transferências
- Histórico de transações
- Validações de exceções (ex: fundos insuficientes)

### Exemplos de testes:

```java
@Test
void deveCriarContaComPixEValorInicial() {
    var conta = accountRepository.create(List.of("pix@dio.com"), new BigDecimal("100.00"));
    assertEquals(10000, conta.getFunds());
}
```

---

## 💡 Tecnologias e Boas Práticas

- Java 17+
- Uso de **BigDecimal** para valores monetários
- **Lombok** para getters e boilerplate
- Separação por responsabilidade (modelo, repositório, lógica de negócio)
- **Tratamento de exceções personalizado**
- Suporte a testes unitários com JUnit

---

## 📦 Como executar

1. Clone o repositório:

   ```bash
   git clone https://github.com/seu-usuario/dio-bank.git
   cd dio-bank
   ```

2. Compile o projeto:

   ```bash
   mvn clean install
   ```

3. Execute:

   ```bash
   mvn exec:java -Dexec.mainClass="br.com.dio.Main"
   ```

Ou diretamente via sua IDE (IntelliJ, Eclipse, etc.).

---

## 🔒 Tratamento de Exceções

Exceções personalizadas incluem:

- `PixInUseException`
- `NoFundsEnoughException`
- `AccountNotFoundException`
- `InvestmentNotFoundException`
- `AccountWithInvestmentException`
- `WalletNotFoundException`

---

## 🗂 Organização do Projeto

```
src/
├── br.com.dio
│   ├── Main.java
│   ├── model/
│   ├── repository/
│   ├── exception/
│   ├── util/
│   └── service/ (opcional)
```

---

## 🤝 Contribuições

Sugestões, correções ou melhorias são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

---

## 📄 Licença

Projeto com fins educacionais, sem licença comercial.

---

