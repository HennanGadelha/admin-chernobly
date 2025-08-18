# 📄 Comercial Conditions API

## 🚀 Intuito da Aplicação
Esta aplicação foi desenvolvida para gerenciar **condições comerciais de sellers** de forma versionada.  
A ideia central é permitir que as condições vigentes de um seller possam ser criadas, atualizadas e historicamente rastreadas por meio de snapshots.

Cada alteração gera uma **nova versão**, encerrando a anterior, mas mantendo o vínculo entre elas através de um ponteiro (`previousVersionId`).

---

## 🏗️ Entidades Principais

### **Seller**
- Representa o vendedor na plataforma.
- É a entidade lógica que conecta todas as condições comerciais.

### **CommercialConditionsHeader**
- Header que centraliza o controle das condições comerciais de um seller.
- Campos principais:
    - `sellerId`: identificador lógico.
    - `createdAt`: data de criação do header.
    - `createdBy`: usuário responsável pela criação.
    - `currentVersionId`: referência para a versão de snapshot atualmente ativa.

### **CommercialConditionsSnapshot**
- Representa uma **versão específica** das condições comerciais.
- Campos principais:
    - `versionId`: identificador único da versão.
    - `sellerId`: referência ao seller.
    - `status`: indica se está `ACTIVE` ou `INACTIVE`.
    - `commercialConditions`: objeto com as regras comerciais detalhadas.
    - `previousVersionId`: aponta para a versão anterior (quando não é a primeira).
    - `createdAt`, `createdBy`, `changeReason`: metadados de auditoria.

### **CommercialConditions**
Objeto que guarda as regras em si:
- `CreditCard` → regras para parcelamento e cashback.
- `Pix` → configurações de pagamento via Pix.
- `Points` → regras de pontos de fidelidade.

Esses objetos compartilham a estrutura de cashback configurável via `CashBackConfig`.

---

## 🔗 Relacionamento de Tabelas

- **sellers**  
  Tabela raiz que representa cada vendedor.

- **commercial_conditions_header**  
  Contém uma linha por seller, apontando sempre para a **versão vigente** (`currentVersionId`).

- **commercial_conditions_snapshot**  
  Armazena todas as versões de condições criadas para um seller.
    - Relaciona-se com **commercial_conditions_header** via `sellerId`.
    - Cada snapshot referencia a **versão anterior** através do campo `previousVersionId`.

Esse design garante:
- Histórico imutável de alterações.
- Fácil navegação entre versões (para trás e potencialmente para frente).
- Atualizações atômicas via transação.

---

## ⚙️ Fluxo de Criação/Atualização

1. **Primeira versão**
    - Criada quando `firstVersion = true` **e não há versões vigentes**.
    - Snapshot inicial é gerado e vinculado ao header.

2. **Nova versão**
    - Quando há uma versão ativa, ela é marcada como `INACTIVE`.
    - É criada uma nova com `ACTIVE` e ponteiro para a anterior.
    - O header é atualizado com o `currentVersionId` da nova.

---

## 🌐 Endpoints da API

### **Criar novo snapshot**
`POST /conditions/snapshot`

**Request Body**
```json
{
  "sellerId": "fc8b670b-9ffe-42f9-bc53-9f2227d190bb",
  "firstVersion": true,
  "commercialConditions": {
    "creditCard": {
      "minInstallments": 1,
      "maxInstallments": 12,
      "valueMinInstallments": 50.00,
      "cahbackConfig": {
        "isActive": true,
        "cashback": 0.10,
        "cpp": 0.50
      }
    },
    "pix": {
      "value": 100.00,
      "cashback": {
        "isActive": true,
        "cashback": 0.05,
        "cpp": 1.0
      },
      "isActive": true
    },
    "points": {
      "quantity": 500,
      "cashback": {
        "isActive": false,
        "cashback": 0.00,
        "cpp": 0.00
      },
      "isActive": false
    }
  },
  "createdBy": "admin_user",
  "changeReason": "Initial version of commercial conditions"
}
Response (201 CREATED):

{
  "versionId": "7c4b1b48-13d1-4fa3-bc2b-1de92f540aaa",
  "sellerId": "fc8b670b-9ffe-42f9-bc53-9f2227d190bb",
  "status": "ACTIVE",
  "commercialConditions": { ... },
  "createdAt": "2025-08-16T21:45:00Z",
  "createdBy": "admin_user",
  "changeReason": "Initial version of commercial conditions",
  "previousVersionId": null
}

---

Listar Headers Existentes

GET /conditions/headers

Response (200 OK):

[
  {
    "sellerId": "fc8b670b-9ffe-42f9-bc53-9f2227d190bb",
    "createdAt": "2025-08-10T10:00:00Z",
    "createdBy": "admin_user",
    "currentVersionId": "7c4b1b48-13d1-4fa3-bc2b-1de92f540aaa"
  }
]

---

📌 Funcionalidades Pendentes

Ainda não implementado:

   Documentacao swagger
   
📌 Funcionalidades Pendentes  

📌 Funcionalidades Pendentes  

- 🔍 **Listar todas as versões de um seller**  
- ⬅️ **Listar versão anterior de uma dada versão**  
- 🔄 **Reverter para uma versão anterior**  
- 🐳 **Dockerizar aplicação**  
- 🛒 **Criar categoria de produtos por seller**  
- 🏢 **Criar hierarquia entre sellers**  
- 💡 **Entre outras ainda não pensadas (rsrs)**  

---

🔮 Futuras Funcionalidades em Estudo

- Melhorias no modelo de auditoria (tracking de quem alterou o quê).  
- API para comparar diferenças entre versões de condições.  
- Regras de validação mais complexas (ex.: limites máximos de cashback).  
