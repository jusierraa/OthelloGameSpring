# 📐 Arquitectura del Sistema - Othello Backend

## Diagrama de Arquitectura Completa

```
┌──────────────────────────────────────────────────────────────────────┐
│                          CLIENTE HTTP                                 │
│              (Postman, cURL, Frontend, Móvil, etc.)                  │
└───────────────────────────────┬──────────────────────────────────────┘
                                │
                                │ HTTP Request (JSON + JWT Token)
                                │
┌───────────────────────────────▼──────────────────────────────────────┐
│                      SPRING BOOT APPLICATION                          │
│ ┌──────────────────────────────────────────────────────────────────┐ │
│ │                    CAPA DE SEGURIDAD                             │ │
│ │  ┌────────────────────────────────────────────────────────┐     │ │
│ │  │    JwtAuthenticationFilter (OncePerRequestFilter)     │     │ │
│ │  │  - Intercepta todas las peticiones                    │     │ │
│ │  │  - Extrae JWT del header Authorization                │     │ │
│ │  │  - Valida token con JwtTokenProvider                  │     │ │
│ │  │  - Carga usuario con CustomUserDetailsService         │     │ │
│ │  │  - Establece SecurityContext                          │     │ │
│ │  └────────────────────────┬───────────────────────────────┘     │ │
│ │                            │                                     │ │
│ │  ┌────────────────────────▼───────────────────────────────┐     │ │
│ │  │    SecurityConfig                                      │     │ │
│ │  │  - /auth/** → Público                                  │     │ │
│ │  │  - /juego/** → Requiere autenticación                 │     │ │
│ │  │  - CSRF deshabilitado                                  │     │ │
│ │  │  - Sesiones STATELESS                                  │     │ │
│ │  └────────────────────────────────────────────────────────┘     │ │
│ └──────────────────────────┼───────────────────────────────────────┘ │
│                             │                                         │
│ ┌──────────────────────────▼───────────────────────────────────────┐ │
│ │                   CAPA DE PRESENTACIÓN                           │ │
│ │                    (Controller Layer)                            │ │
│ │  ┌────────────────────────────────────────────────────────┐     │ │
│ │  │           AuthController                               │     │ │
│ │  │  - POST   /auth/register                               │     │ │
│ │  │  - POST   /auth/login                                  │     │ │
│ │  │  - GET    /auth/me                                     │     │ │
│ │  └────────────────────────────────────────────────────────┘     │ │
│ │  ┌────────────────────────────────────────────────────────┐     │ │
│ │  │           JuegoController                              │     │ │
│ │  │  - POST   /juego/nuevo                                 │     │ │
│ │  │  - GET    /juego/{id}                                  │     │ │
│ │  │  - POST   /juego/{id}/movimiento                       │     │ │
│ │  │  - GET    /juego/todos                                 │     │ │
│ │  │  - DELETE /juego/{id}                                  │     │ │
│ │  └────────────────────────┬───────────────────────────────┘     │ │
│ └──────────────────────────┼───────────────────────────────────────┘ │
│                             │                                         │
│                             │ Inyección de Dependencias (DIP)        │
│                             │                                         │
│ ┌──────────────────────────▼───────────────────────────────────────┐ │
│ │                   CAPA DE NEGOCIO                                │ │
│ │                    (Service Layer)                               │ │
│ │  ┌──────────────────────────────────────────────────────┐       │ │
│ │  │           AuthService                                │       │ │
│ │  │  - registrarUsuario()                                │       │ │
│ │  │  - login()                                           │       │ │
│ │  │  - Genera JWT con JwtTokenProvider                   │       │ │
│ │  │  - Valida credenciales con AuthenticationManager    │       │ │
│ │  └──────────────────────────────────────────────────────┘       │ │
│ │  ┌──────────────────────────────────────────────────────┐       │ │
│ │  │           CustomUserDetailsService                   │       │ │
│ │  │  - Implementa UserDetailsService                     │       │ │
│ │  │  - loadUserByUsername() desde MongoDB                │       │ │
│ │  └──────────────────────────────────────────────────────┘       │ │
│ │  ┌──────────────────────────────────────────────────────┐       │ │
│ │  │           JwtTokenProvider                           │       │ │
│ │  │  - generateToken(Authentication)                     │       │ │
│ │  │  - getUsernameFromJWT(String)                        │       │ │
│ │  │  - validateToken(String)                             │       │ │
│ │  │  - HMAC-SHA512 con secret de 64 caracteres          │       │ │
│ │  └──────────────────────────────────────────────────────┘       │ │
│ │  ┌──────────────────────────────────────────────────────┐       │ │
│ │  │           IJuegoService (Interface)                  │       │ │
│ │  │  ▲                                                    │       │ │
│ │  │  │ Implementa                                         │       │ │
│ │  │  │                                                    │       │ │
│ │  │  └──────── JuegoServiceImpl                          │       │ │
│ │  │            - crearJuego()                             │       │ │
│ │  │            - obtenerJuego()                           │       │ │
│ │  │            - realizarMovimiento()                     │       │ │
│ │  │            - convertirADTO()                          │       │ │
│ │  └──────────────────────┬───────────────────────────────┘       │ │
│ │                         │  usa                                   │ │
│ │                         │                                        │ │
│ │  ┌──────────────────────▼───────────────────────────────┐       │ │
│ │  │       ITableroService (Interface)                    │       │ │
│ │  │  ▲                                                    │       │ │
│ │  │  │ Implementa                                         │       │ │
│ │  │  │                                                    │       │ │
│ │  │  └──────── TableroServiceImpl                        │       │ │
│ │  │            - esMovimientoValido()                     │       │ │
│ │  │            - ejecutarMovimiento()                     │       │ │
│ │  │            - obtenerMovimientosValidos()              │       │ │
│ │  │            - tieneMovimientosValidos()                │       │ │
│ │  └───UsuarioRepository extends MongoRepository           │     │ │
│ │  │  - findByUsername(String)                             │     │ │
│ │  │  - findByEmail(String)                                │     │ │
│ │  │  - existsByUsername(String)                           │     │ │
│ │  │  - existsByEmail(String)                              │     │ │
│ │  └────────────────────────────────────────────────────────┘     │ │
│ │  ┌────────────────────────────────────────────────────────┐     │ │
│ │  │   JuegoRepository extends MongoRepository             │     │ │
│ │  │  - save(Juego)                                        │     │ │
│ │  │  - findById(String)                                   │     │ │
│ │  │  - findAll()                                          │     │ │
│ │  │  - deleteById(String)                                 │     │ │
│ │  │  - existsById(String)                                 │     │ │
│ │  │  - findByEstado(EstadoJuego)                          │     │ │
│ │  │  - findByFechaCreacionAfter(LocalDateTime)           │     │ │
│ │  └────────────────────────┬───────────────────────────────┘     │ │
│ └──────────────────────────┼───────────────────────────────────────┘ │
│                             │                                         │
│                             │ MongoDB CRUD                            │
│                             │                                         │
│ ┌──────────────────────────▼───────────────────────────────────────┐ │
│ │                      CAPA DE DOMINIO                             │ │
│ │                      (Model Layer)                               │ │
│ │  ┌─────────────────────────────────────────────────────┐        │ │
│ │  │  Entidades MongoDB:                                 │        │ │
│ │  │  • Usuario (@Document "usuarios")                   │        │ │
│ │  │    - id, username, email, password (BCrypt)         │        │ │
│ │  │    - roles, partidasJugadas/Ganadas/Perdidas        │        │ │
│ │  │  • Juego (@Document "juegos")                       │        │ │
│ │  │    - id, jugadores, tablero, estado                 │        │ │
│ │  │  • Tablero (matriz 8x8, tamano)                    │        │ │
│ │  │  • Jugador (nombre, color, tipo, puntaje)          │        │ │
│ │  │  • Ficha (color)                                    │        │ │
│ │  │  • Posicion (fila, columna)                         │        │ │
│ │  │                                                     │        │ │
│ │  │  Enums:                                             │        │ │
│ │  │  • ColorFicha (X, O, VACIO)                        │        │ │
│ │  │  • TipoJugador (HUMANO, IA)                        │        │ │
│ │  │  • EstadoJuego (EN_CURSO, FINALIZADO, EMPATE)      │        │ │
│ │  └─────────────────────────────────────────────────────┘        │ │
│ └──────────────────────────┬───────────────────────────────────────┘ │
│                              │                                        │
│ ┌────────────────────────────▼─────────────────────────────────────┐ │
│ │                    BASE DE DATOS                                 │ │
│ │               MongoDB Atlas / MongoDB Local                      │ │
│ │  Colecciones:                                                    │ │
│ │  • usuarios - Almacena usuarios registrados                      │ │
│ │  • juegos - Almacena partidas de Othello                 ─────────┘ │
│                             │                                         │
│                             │ CRUD                                    │
│                             │                                         │
│ ┌──────────────────────────▼───────────────────────────────────────┐ │
│ │                      CAPA DE DOMINIO                             │ │
│ │                      (Model Layer)                               │ │
│ │  ┌─────────────────────────────────────────────────────┐        │ │
│ │  │  Entidades:                                         │        │ │
│ │  │  • Juego (id, jugadores, tablero, estado)          │        │ │
│ │  │ SecurityConfig (Spring Security + JWT)                        │ │
│ │  - CorsConfig (permite peticiones desde cualquier origen)        │ │
│ │  - application.yml (MongoDB URI, JWT secret, puerto, logging)  │ │
│ │  │  • Ficha (color)                                    │        │ │
│ │  │  • Posicion (fila, columna)                         │        │ │
│ │  │                                                     │        │ │
│ │  │  Enums:                                             │        │ │
│ │  │  • ColorFicha (X, O, VACIO)                        │        │ │
│ │  │  • TipoJugador (HUMANO, IA)                        │        │ │
│ │  │  • EstadoJuego (EN_CURSO, FINALIZADO, EMPATE)      │        │ │
│ │  └─────────────────────────────────────────────────────┘        │ │
│ └──────────────────────────────────────────────────────────────────┘ │
│                                                                       │
│ ┌──────────────────────────────────────────────────────────────────┐ │
│ │         Autenticación

### Registro de Usuario

```
1. CLIENTE envía POST /api/auth/register
   {username, email, password}
   ↓
2. AuthController recibe la petición
   • Valida el DTO (@Valid RegisterRequestDTO)
   ↓
3. AuthController delega a AuthService
   • authService.registrarUsuario(registerDTO)
   ↓
4. AuthService ejecuta:
   • Verifica que username no exista (UsuarioRepository)
   • Verifica que email no exista (UsuarioRepository)
   • Hashea password con BCryptPasswordEncoder
   • Crea objeto Usuario con rol "ROLE_USER"
   • Guarda en MongoDB (usuarioRepository.save())
   • Genera JWT token (JwtTokenProvider)
   ↓
5. Retorna AuthResponseDTO con token, username, email, roles
   ↓
6. CLIENTE guarda el token para futuras peticiones
```

### Inicio de Sesión

```
1. CLIENTE envía POST /api/auth/login
   {username, password}
   ↓
2. AuthController recibe la petición
   • Valida el DTO (@Valid LoginRequestDTO)
   ↓
3. AuthController delega a AuthService
   • authService.login(loginDTO)
   ↓
4. AuthService ejecuta:
   • Crea UsernamePasswordAuthenticationToken
   • AuthenticationManager.authenticate() valida credenciales
   • CustomUserDetailsService carga usuario desde MongoDB
   • BCrypt compara password hasheado
   • Si válido, actualiza ultimoAcceso del usuario
   • Genera JWT token (JwtTokenProvider)
   ↓
5. Retorna AuthResponseDTO con token
   ↓
6. CLIENTE guarda el token para futuras peticiones
```

---

## Flujo de una Petición Protegida

### Ejemplo: Crear un Nuevo Juego

```
1. CLIENTE envía POST /api/juego/nuevo
   Header: Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
   ↓
2. JwtAuthenticationFilter intercepta la petición
   • Extrae token del header Authorization
   • JwtTokenProvider.validateToken(token)
   • JwtTokenProvider.getUsernameFromJWT(token)
   • CustomUserDetailsService.loadUserByUsername(username)
   • Crea Authentication y lo establece en SecurityContext
   ↓
3. SecurityConfig verifica autorización
   • /juego/** requiere autenticación → OK
   • Permite continuar la petición
   ↓
4. JuegoController recibe la petición
   • Valida el DTO (@Valid CrearJuegoDTO)
   ↓
5. JuegoController delega a IJuegoService
   • juegoService.crearJuego(crearJuegoDTO)
   ↓
6. JuegoServiceImpl ejecuta la lógica:
   • Crea los objetos Jugador
   • Crea el objeto Juego
   • Inicializa el Tablero
   • Actualiza puntajes
   ↓
7. JuegoServiceImpl usa JuegoRepository
   • juegoRepository.save(juego)
   ↓
8. Spring Data MongoDB persiste en MongoDB Atlas/Local
   • Colección: "juegos"

### Ejemplo: Crear un Nuevo Juego

```
1. CLIENTE envía POST /api/juego/nuevo
   ↓
2. JuegoController recibe la petición
   • Valida el DTO (@Valid CrearJuegoDTO)
   ↓
3. JuegoController delega a IJuegoService
   • juegoService.crearJuego(crearJuegoDTO)
   ↓
4. JuegoServiceImpl ejecuta la lógica:
   • Crea los objetos Jugador
   • Crea el objeto Juego
   • Inicializa el Tablero
   • Actualiza puntajes
   ↓
5. JuegoServiceImpl usa JuegoRepository
   • juegoRepository.guardar(juego)
   ↓
6. JuegoRepository almacena en ConcurrentHashMap
   • En memoria (sin persistencia en BD)
   ↓
7. JuegoServiceImpl convierte Juego a JuegoDTO
   • convertirADTO(juego)
   ↓
8. JuegoController envuelve en RespuestaDTO
   • return ResponseEntity.ok(respuesta)
   ↓
9. CLIENTE recibe JSON con el juego creado
```

---

## Flujo de un Movimiento

### Ejemplo: Realizar Movimiento en Juego

```
1. CLIENTE envía POST /api/juego/{id}/movimiento
   Body: {"fila": 2, "columna": 3}
   ↓
2. JuegoController recibe la petición
   • Valida el MovimientoDTO
   ↓
3. JuegoController delega a IJuegoService
   • juegoService.realizarMovimiento(id, movimiento)
   ↓
4. JuegoServiceImpl obtiene el juego:
   • juegoRepository.buscarPorId(id)
   • Si no existe → JuegoNoEncontradoException
   ↓
5. JuegoServiceImpl valida el movimiento:
   • tableroService.esMovimientoValido(...)
   • Si no es válido → MovimientoInvalidoException
   ↓
6. JuegoServiceImpl ejecuta el movimiento:
   • tableroService.ejecutarMovimiento(...)
   • Voltea fichas según reglas de Othello
   ↓
7. JuegoServiceImpl actualiza estado:
   • juego.actualizarPuntajes()
   • juego.cambiarTurno()
   • Verifica si hay movimientos válidos
   • Finaliza juego si es necesario
   ↓
8. Si el siguiente jugador es IA:
   • jugadorIAStrategy.calcularMovimiento(...)
   • tableroService.ejecutarMovimiento(...)
   • Actualiza estado nuevamente
   ↓
9. JuegoServiceImpl guarda cambios:
   • juegoRepository.guardar(juego)
   ↓
10. JuegoServiceImpl convierte a DTO:
    • convertirADTO(juego)
    • Incluye movimientos válidos
    ↓
11. JuegoController envuelve en RespuestaDTO
    ↓
12. CLIENTE recibe JSON con estado actualizado
```

---

## Aplicación de Principios SOLID

### 1. Single Responsibility Principle (SRP)

| Clase | Una Sola Responsabilidad |
|-------|-------------------------|
| `JuegoController` | Maneja peticiones HTTP |
| `JuegoServiceImpl` | Orquesta lógica del juego |
| `TableroServiceImpl` | Validaciones y movimientos del tablero |
| `JuegoRepository` | Almacenamiento de datos |
| `Ficha`, `Tablero`, `Jugador` | Representan entidades |

### 2. Open/Closed Principle (OCP)

```java
// ✅ Abierto a extensión, cerrado a modificación
public interface IJugadorStrategy {
    Posicion calcularMovimiento(Tablero tablero, ColorFicha color);
}

// Implementaciones actuales
- JugadorHumanoStrategy
- JugadorIAStrategy

// Futuras implementaciones (sin modificar código existente)
- JugadorIAAvanzadaStrategy (Minimax)
- JugadorEnRedStrategy (Multiplayer)
- JugadorAleatoriStrategy (Testing)
```

### 3. Liskov Substitution Principle (LSP)

```java
// Las implementaciones son intercambiables
IJugadorStrategy estrategia;

// Puede ser cualquier implementación
estrategia = new JugadorIAStrategy(tableroService);
estrategia = new JugadorHumanoStrategy();

// El código que usa la estrategia no necesita cambiar
Posicion movimiento = estrategia.calcularMovimiento(tablero, color);
```

### 4. Interface Segregation Principle (ISP)

```java
// Interfaces específicas y cohesivas

// ✅ BIEN - Interfaces segregadas
interface IJuegoService {
    // Solo operaciones de juego
}

interface ITableroService {
    // Solo operaciones de tablero
}

interface IJugadorStrategy {
    // Solo cálculo de movimiento
}

// ❌ MAL - Interface "dios" (no usamos esto)
interface ITodoElJuego {
    // Mezcla de todas las responsabilidades
}
```

### 5. Dependency Inversion Principle (DIP)

```java
// ✅ Depender de abstracciones, no de implementaciones

public class JuegoServiceImpl implements IJuegoService {
    
    // Depende de interfaces (abstracciones)
    private final ITableroService tableroService;
    private final IJugadorStrategy jugadorIAStrategy;
    
    // Inyección de dependencias
    public JuegoServiceImpl(
        ITableroService tableroService,
        IJugadorStrategy jugadorIAStrategy
    ) {
        this.tableroService = tableroService;
        this.jugadorIAStrategy = jugadorIAStrategy;
    }
}

// Spring autowirea automáticamente las implementaciones
```

---

## Diagrama de Secuencia - Crear Juego

```
Cliente          Controller         Service           Repository        Model
  │                  │                 │                  │               │
  │─POST /nuevo─────>│                 │                  │               │
  │                  │                 │                  │               │
  │                  │─crearJuego()───>│                  │               │
  │                  │                 │                  │               │
  │                  │                 │─new Jugador()───────────────────>│
  │                  │                 │<────────────────────────────────│
  │                  │                 │                  │               │
  │                  │                 │─new Juego()─────────────────────>│
  │                  │                 │<────────────────────────────────│
  │                  │                 │                  │               │
  │                  │                 │─guardar()───────>│               │
  │                  │                 │<────────────────│               │
  │                  │                 │                  │               │
  │                  │<─JuegoDTO──────│                  │               │
  │                  │                 │                  │               │
  │<──JSON Response──│                 │                  │               │
  │                  │                 │                  │               │
```

---

## Tecnologías y Patrones de Diseño

### Patrones Implementados

| Patrón | Dónde se Usa | Propósito |
|--------|--------------|-----------|
| **MVC** | Toda la app | Separación de responsabilidades |
| **Strategy** | `IJugadorStrategy` | Algoritmos intercambiables |
| **Repository** | `JuegoRepository` | Abstracción del almacenamiento |
| **DTO** | Capa de presentación | Transferencia de datos |
| **Dependency Injection** | Spring | Inversión de control |
| **Singleton** | `@Service`, `@Repository` | Una instancia por Spring |

### Anotaciones Spring

| Anotación | Uso |
|-----------|-----|
| `@SpringBootApplication` | Clase principal |
| `@RestController` | Controlador REST |
| `@Service` | Lógica de negocio |
| `@Repository` | Acceso a datos |
| `@Configuration` | Configuración |
| `@Autowired` / Constructor | Inyección de dependencias |
| `@RestControllerAdvice` | Manejo global de excepciones |

---

## Ventajas de esta Arquitectura

✅ **Mantenibilidad**: Código organizado y fácil de entender  
✅ **Escalabilidad**: Fácil agregar nuevas funcionalidades  
✅ **Testabilidad**: Cada capa se puede probar independientemente  
✅ **Reusabilidad**: Componentes desacoplados y reutilizables  
✅ **Flexibilidad**: Cambios en una capa no afectan otras  
✅ **Profesionalismo**: Sigue estándares de la industria  

---

**Proyecto desarrollado por Grupo 6**
