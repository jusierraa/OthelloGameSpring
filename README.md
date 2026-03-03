# 🎮 Othello Backend API

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)
![Maven](https://img.shields.io/badge/Maven-blue)
![Status](https://img.shields.io/badge/Status-Active-success)

API REST para el juego de Othello (Reversi) desarrollada con **Spring Boot**, siguiendo **arquitectura por capas** y **principios SOLID**.

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Arquitectura](#-arquitectura)
- [Principios SOLID](#-principios-solid-aplicados)
- [Tecnologías](#-tecnologías-utilizadas)
- [Instalación](#-instalación)
- [Uso](#-uso)
- [API Endpoints](#-api-endpoints)
- [Ejemplos de Uso](#-ejemplos-de-uso)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Autor](#-autor)

---

## 📖 Descripción

**Othello Backend** es una implementación del clásico juego de mesa Othello (también conocido como Reversi) en formato de API REST. El proyecto permite:

- ✅ Crear partidas entre dos jugadores (Humano vs Humano o Humano vs IA)
- ✅ Realizar movimientos y validar reglas del juego
- ✅ IA automática con estrategia de maximización de fichas volteadas
- ✅ Consultar estado del juego en tiempo real
- ✅ Gestión completa de múltiples partidas simultáneas

Este proyecto es una **migración y refactorización** del proyecto original de consola Java a una arquitectura moderna REST con Spring Boot.

---

## 🏗️ Arquitectura

El proyecto sigue una **arquitectura por capas (Layered Architecture)** separando responsabilidades:

```
┌─────────────────────────────────────────┐
│         CAPA DE PRESENTACIÓN            │
│         (Controller Layer)              │
│    - JuegoController                    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│        CAPA DE NEGOCIO                  │
│        (Service Layer)                  │
│    - JuegoService                       │
│    - TableroService                     │
│    - JugadorStrategy (IA/Humano)        │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│        CAPA DE DATOS                    │
│        (Repository Layer)               │
│    - JuegoRepository (In-Memory)        │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│        CAPA DE DOMINIO                  │
│        (Model Layer)                    │
│    - Juego, Tablero, Jugador, Ficha     │
└─────────────────────────────────────────┘
```

### Flujo de una Petición

```
Cliente HTTP → Controller → Service → Repository → Model
                    ↓           ↓
                   DTO      Lógica de Negocio
```

---

## 🎯 Principios SOLID Aplicados

### **S - Single Responsibility Principle (Responsabilidad Única)**

Cada clase tiene una única responsabilidad bien definida:

- **`JuegoController`**: Solo maneja peticiones HTTP
- **`JuegoService`**: Orquesta la lógica del juego
- **`TableroService`**: Maneja validaciones y movimientos del tablero
- **`JuegoRepository`**: Solo gestiona almacenamiento de datos
- **`Ficha`, `Tablero`, `Jugador`**: Representan entidades de dominio

### **O - Open/Closed Principle (Abierto/Cerrado)**

El sistema está abierto a extensión pero cerrado a modificación:

- **`IJugadorStrategy`**: Interface que permite agregar nuevos tipos de jugadores (ej: IA avanzada, jugador en red) sin modificar código existente
- Nuevas estrategias simplemente implementan la interface

### **L - Liskov Substitution Principle (Sustitución de Liskov)**

Las implementaciones pueden sustituirse sin afectar el comportamiento:

- **`JugadorHumanoStrategy`** y **`JugadorIAStrategy`** implementan `IJugadorStrategy`
- El servicio puede usar cualquier estrategia intercambiablemente

### **I - Interface Segregation Principle (Segregación de Interfaces)**

Interfaces específicas y cohesivas:

- **`IJuegoService`**: Operaciones de gestión de juegos
- **`ITableroService`**: Operaciones sobre el tablero
- **`IJugadorStrategy`**: Solo define cálculo de movimiento
- Las clases no dependen de métodos que no usan

### **D - Dependency Inversion Principle (Inversión de Dependencias)**

Las clases de alto nivel dependen de abstracciones, no de implementaciones:

```java
// ❌ MAL (depende de implementación concreta)
private TableroServiceImpl tableroService;

// ✅ BIEN (depende de abstracción)
private ITableroService tableroService;
```

**Inyección de Dependencias** con Spring facilita esto automáticamente.

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 17 | Lenguaje de programación |
| **Spring Boot** | 3.2.0 | Framework backend |
| **Spring Web** | 3.2.0 | Creación de API REST |
| **Spring Data MongoDB** | 3.2.0 | Persistencia en MongoDB |
| **Spring Security** | 3.2.0 | Autenticación y autorización |
| **JWT (JJWT)** | 0.11.5 | Tokens de autenticación |
| **MongoDB** | Atlas/Local | Base de datos NoSQL |
| **BCrypt** | - | Hash de contraseñas |
| **Spring Validation** | 3.2.0 | Validación de datos |
| **Lombok** | - | Reducción de boilerplate |
| **Maven** | 3.x | Gestión de dependencias |
| **SLF4J/Logback** | - | Sistema de logging |

---

## 📦 Instalación

### **Prerequisitos**

- Java 17 o superior
- Maven 3.6 o superior
- MongoDB (Atlas Cloud o Local)
  - **Opción 1 - MongoDB Atlas** (Recomendado): Cuenta gratuita en [mongodb.com/cloud/atlas](https://www.mongodb.com/cloud/atlas)
  - **Opción 2 - MongoDB Local**: Instalar [MongoDB Community Edition](https://www.mongodb.com/try/download/community)

### **Configuración de MongoDB**

#### Opción 1: MongoDB Atlas (Cloud - Recomendado)
1. Crea una cuenta gratuita en MongoDB Atlas
2. Crea un cluster gratuito (M0)
3. Configura acceso de red (allow from anywhere: 0.0.0.0/0)
4. Crea usuario de base de datos (ej: `othello` / `othello123`)
5. La URI ya está configurada en `application.yml`:
```yaml
mongodb+srv://othello:othello123@cluster0.mongodb.net/othello_db
```

#### Opción 2: MongoDB Local
1. Instala MongoDB Community Edition
2. Inicia el servicio:
```bash
# Windows
net start MongoDB

# Linux/Mac
sudo systemctl start mongod
```
3. Actualiza `application.yml`:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/othello_db
```

### **Pasos**

1. **Clonar el repositorio**
```bash
git clone <url-del-repositorio>
cd othAutenticación** - Base URL: `/api/auth`

| Método | Endpoint | Descripción | Auth Requerida |
|--------|----------|-------------|----------------|
| `POST` | `/register` | Registrar nuevo usuario | No |
| `POST` | `/login` | Iniciar sesión y obtener JWT | No |
| `GET` | `/me` | Obtener info del usuario actual | Sí |

### **Juegos** - Base URL: `/api/juego`

| Método | Endpoint | Descripción | Auth Requerida |
|--------|----------|-------------|----------------|
| `GET` | `/health` | Verificar salud del servicio | Sí |
| `POST` | `/nuevo` | Crear nueva partida | Sí |
| `GET` | `/{id}` | Obtener estado de un juego | Sí |
| `POST` | `/{id}/movimiento` | Realizar un movimiento | Sí |
| `GET` | `/todos` | Listar todos los juegos | Sí |
| `DELETE` | `/{id}` | Eliminar un juego | Sí |

**Nota**: Los endpoints que requieren autenticación necesitan el header:
```
Authorization: Bearer {jwt_token}
```
mvn spring-boot:run
```

O usando el JAR generado:
```bash
java -jar target/othello-backend-1.0.0.jar
```

4. **Verificar que esté funcionando**
```
http://localhost:8080/api/juego/health
```

---

## 🚀 Uso

La API estará disponible en: **`http://localhost:8080/api`**

---

## 📡 API Endpoints

### **Base URL**: `/api/juego`

| Méto🔐 1. Registrar Usuario**

**Request:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "juan",
  "email": "juan@example.com",
  "password": "123456"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuIiwiaWF0IjoxNzA...",
  "username": "juan",
  "email": "juan@example.com",
  "roles": ["ROLE_USER"]
}
```

### **🔓 2. Iniciar Sesión**

**Request:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "juan",
  "password": "123456"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuIiwiaWF0IjoxNzA...",
  "username": "juan",
  "email": "juan@example.com",
  "roles": ["ROLE_USER"]
}
```

**⚠️ Importante**: Guarda el `token` y úsalo en todos los endpoints protegidos.

### **3. Crear un Nuevo Juego**

**Request:**
```http
POST /api/juego/nuevo
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...| Realizar un movimiento |
| `GET` | `/todos` | Listar todos los juegos |
| `DELETE` | `/{id}` | Eliminar un juego |

---

## 📚 Ejemplos de Uso

### **4. Realizar un Movimiento**

**Request:**
```http
POST /api/juego/{id}/movimiento
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...

{
  "nombreJugador1": "Stark",
  "nombreJugador2": "Ultron",
  "tipoJugador2": "IA"
}
```

**Response:**
```json
{
  "exito": true,
  "mensaje": "Juego creado exitosamente",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "jugador1": {
      "nombre": "Stark",
      "color": "X",
      "tipo": "HUMANO",
      "puntaje": 2
    },
    "jugador2": {
      "nombre": "Ultron",
      "color": "O",
      "tipo": "IA",
      "puntaje": 2
    },
    "turnoActual": "X",
    "tablero": {
      "matriz": [
        ["-","-","-","-","-","-","-","-"],
        ["-","-","-","-","-","-","-","-"],
        ["-","-","-","-","-","-","-","-"],
        ["-","-","-","O","X","-","-","-"],
        ["-","-","-","X","O","-","-","-"],
        ["-","-","-","-","-","-","-","-"],
        ["-","-","-","-","-","-","-","-"],
        ["-","-","-","-","-","-","-","-"]
      5. Obtener Estado del Juego**

**Request:**
```http
GET /api/juego/{id}
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
      "Stark": 2,
      "Ultron": 2
    },
    "mensajeEstado": "Juego iniciado",
    "movimientosValidos": [
      {"fila": 2, "columna": 3},
      {"fila": 3, "columna": 2},
      {"fila": 4, "columna": 5},
      {"fila": 5, "columna": 4}
    ]
  }6. Listar Todos los Juegos**

**Request:**
```http
GET /api/juego/todos
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9... Movimiento**

**Request:**
```http
POST /api/juego/{id}/movimiento
Content-Type: application/json

{
  "fila": 2,
  "columna": 3
}
```

**Response:**
```json
{
  "exito": true,
  "mensaje": "Movimiento realizado exitosamente",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "turnoActual": "O",
    "tablero": { ... },
    "puntajes": {
      "Stark": 4,
      "Ultron": 1
    },
    "mensajeEstado": "Movimiento realizado",
    "movimientosValidos": [ ... ]
  }
}
```

### **3. Obtener Estado del Juego**

**Request:**
```http
GET /api/juego/{id}
```

**Response:**
```json
{
  "exito": true,
  "mensaje": "Juego obtenido exitosamente",
  "data": { ... }
}
```

### **4. Listar Todos los Juegos**

**Request:**
```http
GET /api/juego/todos
```

**Response:**
```json
{
  "exito": true,
  "mensaje": "Juegos obtenidos exitosamente",
  "data": [ ... ]
}7. Eliminar un Juego**

**Request:**
```http
DELETE /api/juego/{id}
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
**Request:**
```http
DELETE /api/juego/{id}
```

**Response:**
```json
{
  "exito": true,
  "mensaje": "Juego eliminado exitosamente"
}
```

---

## 🗂️ Estructura del Proyecto

```
othello-backend/
├── src/main/java/com/othello/
│   ├── OthelloApplication.java         # Clase principal Spring Boot
│   │
│   ├── model/                          # 📦 Capa de Dominio
│   │   ├── ColorFicha.java             # Enum: X, O, VACIO
│   │   ├── TipoJugador.java            # Enum: HUMANO, IA
│   │   ├── EstadoJuego.java            # Enum: EN_CURSO, FINALIZADO, EMPATE
│   │   ├── Ficha.java                  # Entidad: Ficha del tablero
│   │   ├── Tablero.java                # Entidad: Tablero 8x8
│   │   ├── Jugador.java                # Entidad: Jugador
│   │   ├── Juego.java                  # Entidad: Partida completa
│   │   └── Posicion.java               # Value Object: Coordenadas
│   │
│   ├── dto/                            # 📄 Data Transfer Objects
│   │   ├── JugadorDTO.java
│   │   ├── TableroDTO.java
│   │   ├── JuegoDTO.java
│   │   ├── MovimientoDTO.java
│   │   ├── PosicionDTO.java
│   │   ├── CrearJuegoDTO.java
│   │   └── RespuestaDTO.java
│   │
│   ├── repository/                     # 💾 Capa de Persistencia
│   │   └── JuegoRepository.java        # Repositorio in-memory
│   │
│   ├── service/                        # 🧠 Capa de Negocio
│   │   ├── interfaces/
│   │   │   ├── IJuegoService.java      # Interface del servicio de juego
│   │   │   ├── ITableroService.java    # Interface del servicio de tablero
│   │   │   └── IJugadorStrategy.java   # Interface Strategy para jugadores
│   │   └── impl/
│   │       ├── JuegoServiceImpl.java   # Implementación del servicio de juego
│   │       ├── TableroServiceImpl.java # Lógica del tablero Othello
│   │       ├── JugadorIAStrategy.java  # Estrategia de IA
│   │       └── JugadorHumanoStrategy.java
│   │
│   ├── controller/                     # 🌐 Capa de Presentación
│   │   └── JuegoController.java        # Controlador REST
│   │
│   ├── exception/                      # ⚠️ Manejo de Excepciones
│   │   ├── JuegoNoEncontradoException.java
│   │   ├── MovimientoInvalidoException.java
│   │   └── GlobalExceptionHandler.java
│   │
│   └── config/                         # ⚙️ Configuración
│       └── CorsConfig.java             # Configuración CORS
│
├── src/main/resources/
│   └── application.yml                 # Configuración de la aplicación
│
├── pom.xml                             # Dependencias Maven
└── README.md                           # Este archivo
```

---

## 🎮 Reglas del Juego Othello

1. El tablero es de 8x8 con 4 fichas centrales iniciales (2 X y 2 O)
2. Los jugadores alternan turnos colocando fichas
3. Un movimiento es válido si voltea al menos una ficha del oponente
4. Se voltean fichas cuando quedan atrapadas entre la nueva ficha y otra del mismo color
5. Si un jugador no tiene movimientos válidos, pasa el turno
6. El juego termina cuando ningún jugador puede mover
7. Gana quien tenga más fichas al final

---

## 📊 Diagrama de Clases (Simplificado)

```
┌─────────────────┐
│  JuegoController│
└────────┬────────┘
         │ usa
         ▼
┌─────────────────┐      ┌──────────────────┐
│  IJuegoService  │◄─────│ JuegoServiceImpl │
└─────────────────┘      └──────────────────┘
         │                        │
         │                        │ usa
         ▼                        ▼
┌──────────────────┐     ┌──────────────────┐
│ ITableroService  │◄────│TableroServiceImpl│
└──────────────────┘     └──────────────────┘

         ┌──────────────────┐
         │IJugadorStrategy  │
         └────────┬─────────┘
                  │
      ┌───────────┴──────────┐
      ▼                      ▼
┌─────────────────┐   ┌────────────────┐
│JugadorIAStrategy│   │JugadorHumano...│
└─────────────────┘   └────────────────┘
```

---

## 🧪 Testing (Futuro)

Para ejecutar las pruebas:
```bash
mvn test
```

---

## 🤝 Contribuciones

Este es un proyecto educativo. Las sugerencias y mejoras son bienvenidas.

---

## 📝 Notas de Migración

### Diferencias con el Proyecto Original

| Aspecto | Proyecto Original | Proyecto Spring Boot |
|---------|-------------------|---------------------|
| Interface | Consola + JOptionPane | API REST (JSON) |
| Arquitectura | Monolítica | Por capas |
| Principios | - | SOLID |
| Almacenamiento | No persistente | In-Memory Repository |
| Escalabilidad | 1 juego a la vez | Múltiples juegos simultáneos |
| Testing | Manual | Automatizable |

---

## 🚀 Próximas Mejoras

- [ ] Persistencia en base de datos (PostgreSQL/MongoDB)
- [ ] WebSockets para juego en tiempo real
- [ ] Autenticación y autorización (Spring Security)
- [ ] Frontend con React/Angular
- [ ] IA más avanzada (Minimax con poda alfa-beta)
- [ ] Sistema de rankings y estadísticas
- [ ] Tests unitarios y de integración

---

## 👨‍💻 Autor

**Juan Camilo Sierra**

Proyecto desarrollado como parte del Diplomado en IA y DevOps.

---

## 📄 Licencia

Este proyecto es de código abierto para fines educativos.

---

## 📞 Soporte

Para reportar problemas o sugerencias, por favor contacta al autor.

---

**¡Disfruta jugando Othello! 🎮**
#   O t h e l l o G a m e S p r i n g 
 
 
