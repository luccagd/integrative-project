# Projeto Integrador

## ⚔️ Requisito 01

`ml-insert-batch-in-fulfillment-warehouse-01`

**Descrição:** insira no lote no armazém de atendimento.

✅ **POST** `/api/v1/fresh-products/inboundorder`

<details>
  <summary>Detalhes da requisição</summary>

  Request Header
  ```json
  {
    "agentId": "Long"
  }
  ```
  
  Request Body:
  ```json
  {
    "sectionId": "Long",
    "warehouseId": "Long",
    "sellerId": "Long",
    "batchStock": {
      "products": [
        {
          "name": "String",
          "currentTemperature": "Double",
          "minimalTemperature": "Double",
          "quantity": "Integer",
          "dueDate": "LocalDate",
          "category": "CONGELADO, REFRIGERADO ou FRESCO",
          "price": "Double"
        }
      ]
    }
  }
  ```
</details>

<br>

✅ **PUT** `/api/v1/fresh-products/inboundorder/{inboundOrderId}/{productId}`

<details>
  <summary>Detalhes da requisição</summary>

  Path Variable
  ```json
  {
    "inboundOrderId": "Long",
    "productId": "Long"
  } 
  ```

  Request Body:
  ```json
  {        
    "name": "String",
    "currentTemperature": "Double",
    "minimalTemperature": "Double",
    "quantity": "Integer",
    "dueDate": "LocalDate",
    "category": "CONGELADO, REFRIGERADO ou FRESCO",
    "price": "Double"        
  }
  ```
</details>

<br>

## ⚔️ Requisito 02

`ml-add-products-to-cart-01`

**Descrição:** adicionar o produto ao carrinho de compras.

✅ **GET** `/api/v1/fresh-products`

<br>

✅ **GET** `/api/v1/fresh-products/list/byCategory?category={category}`

<details>
  <summary>Detalhes da requisição</summary>
  
  Request Params
  ```json
  {
    "category": "CONGELADO, REFRIGERADO ou FRESCO"
  } 
  ```
</details>

<br>

✅ **POST** `/api/v1/fresh-products/orders`

<details>
  <summary>Detalhes da requisição</summary>

  Request Body
  ```json
  {
    "buyerId": "Long",
    "date": "LocalDate",
    "products": [
      {
        "productId": "Long",
        "quantity": "Integer"
      }
    ]
  } 
  ```
</details>

<br>

✅ **GET** `/api/v1/fresh-products/orders/{idOrder}`

<details>
  <summary>Detalhes da requisição</summary>

  Path Variable
  ```json
  {
    "idOrder": "Long"
  } 
  ```
</details>

<br>

✅ **PUT** `/api/v1/fresh-products/orders?idOrder={idOrder}`

<details>
  <summary>Detalhes da requisição</summary>

  Request Param
  ```json
  {
    "idOrder": "Long"
  } 
  ```

  Request Body
  ```json
  {
    "buyerId": "Long",
    "date": "LocalDate",
    "products": [
      {
        "productId": "Long",
        "quantity": "Integer"
      }
    ]
  } 
  ```
</details>

<br>

## ⚔️ Requisito 03

`ml-check-product-location-in-warehouse-01`

**Descrição:** verifique a localização de um produto no armazém.

✅ **GET** `/api/v1/fresh-products/list/byName?name={name}&orderBy={orderBy}`

<details>
  <summary>Detalhes da requisição</summary>

  Request Param
  ```json
  {
    "name": "String",
    "orderBy": "L, C ou F"
  } 
  ```
</details>
    
<br>

## ⚔️ Requisito 04

`ml-check-product-stock-in-warehouses-04`

**Descrição:** consultar o estoque de um produto em todos os armazéns.

✅ **GET** `/api/v1/fresh-products/warehouse?product_name={product_name}`

<details>
  <summary>Detalhes da requisição</summary>

  Request Param
  ```json
  {
    "product_name": "String"
  } 
  ```
</details>

<br>

## ⚔️ Requisito 05

`ml-check-batch-stock-due-date-01`

**Descrição:** consultar a data de validade por lote.

✅ **GET** `/api/v1/fresh-products/due-date?sectionName={sectionName}&numberOfDays={numberOfDays}&asc={asc}`

<details>
  <summary>Detalhes da requisição</summary>

  Request Param
  ```json
  {
    "sectioName": "CONGELADO, REFRIGERADO ou FRESCO",
    "numberOfDays": "Integer",
    "asc": "true ou false"
  } 
  ```
</details>

<br>
