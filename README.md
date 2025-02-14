# 🎱 Bingo API

Una API RESTful para gestionar un juego de Bingo, implementada con **Spring Boot** y **Spring WebFlux**. Esta API permite crear juegos de Bingo, generar tarjetas, llamar números aleatorios y consultar números ya llamados. Utiliza **R2DBC** para una conexión no bloqueante con bases de datos relacionales.

---

## 🚀 Funcionalidades

La API ofrece las siguientes operaciones principales:

1. **Crear un juego de Bingo**: Inicia un nuevo juego con un estado inicial.
2. **Generar una tarjeta de Bingo**: Crea una tarjeta de Bingo aleatoria para un juego específico.
3. **Llamar un número de Bingo**: Selecciona un número aleatorio que no haya sido llamado previamente en el juego.
4. **Obtener números llamados**: Consulta los números que ya han sido llamados en un juego.

---

## 🛠️ Tecnologías

- **Spring Boot**: Framework para desarrollar aplicaciones Java basadas en microservicios.
- **Spring WebFlux**: Manejo de peticiones reactivas y no bloqueantes.
- **R2DBC**: Conexión no bloqueante con bases de datos relacionales.
- **JUnit 5** y **WebTestClient**: Para pruebas unitarias y de integración.
- **H2**: Base de datos en memoria utilizada durante el desarrollo y pruebas.

---

## 📋 Requisitos

- **JDK 17** o superior.
- **Maven 3.8+** para compilar el proyecto.
- **Docker** (opcional, si se desea usar una base de datos como servicio).

---

## 🛠️ Instalación

### 1. Clonar el repositorio

git clone https://github.com/tu-usuario/bingo-api.git
cd bingo-api

## 2. Compilar el proyecto

Para compilar y construir el proyecto, utiliza Maven con el siguiente comando:

mvn clean install


## 3. Ejecutar la aplicación

Ejecuta el proyecto con el siguiente comando:

mvn spring-boot:run


La aplicación estará disponible en `http://localhost:8080`.

---

## 🌐 Endpoints de la API

### 1. Crear un juego de Bingo

- **Método**: `POST`
- **URL**: `/api/bingo/game`
- **Respuesta**:
    - `201 CREATED`: Juego creado exitosamente.
    - **Cuerpo de la respuesta**:
        ```json
        {
          "id": "UUID-del-juego",
          "status": "CREATED",
          "calledNumbers": ""
        }
        

### 2. Generar una tarjeta de Bingo

- **Método**: `POST`
- **URL**: `/api/bingo/{gameId}/cards`
- **Respuesta**:
    - `201 CREATED`: Tarjeta generada exitosamente.
    - **Cuerpo de la respuesta**:
        ```json
        {
          "gameId": "UUID-del-juego",
          "numbersJson": "números-generados-en-una-cadena"
        }
        

### 3. Llamar un número de Bingo

- **Método**: `POST`
- **URL**: `/api/bingo/{gameId}/numbers`
- **Respuesta**:
    - `200 OK`: Número llamado exitosamente.
    - **Cuerpo de la respuesta**:
        ```json
        {
          "number": 23,
          "column": "B"
        }
        

### 4. Obtener los números llamados

- **Método**: `GET`
- **URL**: `/api/bingo/{gameId}/numbers`
- **Respuesta**:
    - `200 OK`: Lista de números llamados.
    - **Cuerpo de la respuesta**:
        ```json
        {
          "calledNumbers": [1, 15, 23, 45]
        }
### 5. Comprobar tarjeta de Bingo
- **Método**: `POST`
- **URL**: `/api/bingo/check`
- - **Request Body**:
    - **Tipo**: `BingoCardCheckRequest`
    - **Descripción**: El cuerpo de la solicitud debe contener la información de la tarjeta y el juego a comprobar.
    - **Formato**:
        ```json
        {
          "gameId": "UUID-del-juego",
          "cardId": "UUID-de-la-tarjeta"
        }
        

    - **Campos**:
        - `gameId` (UUID): El identificador único del juego.
        - `cardId` (UUID): El identificador único de la tarjeta de Bingo.
- **Respuesta**:
   - **200 OK**: Si la tarjeta cumple con los requisitos para ganar, se retorna la respuesta con el resultado de la comprobación.
    - **Cuerpo de la respuesta**: `BingoCardCheckResponse`
        ```json
        {
          "isWinner": true,
          "winningNumbers": [5, 12, 23, 45, 60],
          "message": "¡Felicidades! ¡Has ganado!"
        }

## 🧪 Pruebas

El proyecto incluye pruebas unitarias y de integración utilizando **JUnit 5** y **WebTestClient**. Para ejecutar las pruebas, usa el siguiente comando:

mvn test

### Pruebas de integración

Se incluyen pruebas para asegurar que:

1. La creación del juego funciona correctamente.
2. La generación de la tarjeta de Bingo se realiza de manera adecuada.
3. Los números de Bingo se llaman correctamente, sin duplicados.

---

## 📄 Licencia

Este proyecto está bajo la **Licencia MIT**. Consulta el archivo [LICENSE](https://chat.deepseek.com/a/chat/s/LICENSE) para más detalles.
