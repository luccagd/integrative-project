# Agents Endpoins

Aqui estão presentes os endpoints para operações relacionadas aos representantes dos Armazéns. Através deles é possível buscar e/ou alterar representantes que estejam cadastrados na bases de dados, e também criar novos representantes.

URL base para execuçlão dos endpoints: `http://localhost:8080/api/v1`

<br>

## Buscar todos os representantes
O busca por todos os representantes cadastrados e apresenta os seus dados, além do id e nome do armazém pelo qual ele é responsável.

Método HTTP: `GET`

Endpoint: `/agents`

Exemplo de RESPONSE:
```json
[
    {
        "id": 1,
        "name": "Jose",
        "userName": "jose",
        "warehouseId": 1,
        "warehouseName": "Armazem SP"
    },
    {
        "id": 2,
        "name": "Joao",
        "userName": "Joao",
        "warehouseId": 2,
        "warehouseName": "Armazem GO"
    }
]
```

<br>

## Buscar representante por id
O um representante pelo seu Id. Apresenta os seus dados e também o id e nome do armazém pelo qual ele é responsável.

Método HTTP: `GET`

Endpoint: `/agents/{agentId}`

Exemplo de RESPONSE:
```json
{
    "id": 1,
    "name": "Jose",
    "userName": "jose",
    "warehouseId": 1,
    "warehouseName": "Armazem SP"
}
```

<br>

## Cadastra novo representante
Cadastra um novo representante, validando se o id do armazém passado no corpo da requisição já está associado à um outro representante.

Método HTTP: `POST`

Endpoint: `/agents`

Exemplo de REQUEST:
```json
{
    "name": "John Doe",
    "userName": "johndoe",
    "password": "password123",
    "warehouseId": 3
}
```

Exemplo de RESPONSE:
```json
{
    "id": 3,
    "name": "John Doe",
    "userName": "johndoe",
    "warehouseId": 3,
    "warehouseName": "Armazem PR"
}
```

<br>

## Altera um representante
Altera os dados de um representante que esteja cadastrado, validando se representante se encontra na base de dados e se o id do armazém passado no corpo da requisição já está associado à um outro representante.

Método HTTP: `PUT`

Endpoint: `/agents/{agentId}`

Exemplo de REQUEST:
```json
{
    "name": "John Doe",
    "userName": "john2022",
    "password": "password123",
    "warehouseId": 4
}
```

Exemplo de RESPONSE:
```json
{
    "id": 3,
    "name": "John Doe",
    "userName": "john2022",
    "warehouseId": 4,
    "warehouseName": "Armazem RS"
}
```