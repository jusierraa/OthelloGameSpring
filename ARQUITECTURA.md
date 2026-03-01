# 📐 Arquitectura del Sistema - Othello Backend

## Diagrama de Arquitectura Completa

```
┌──────────────────────────────────────────────────────────────────────┐
│                          CLIENTE HTTP                                 │
│              (Postman, cURL, Frontend, Móvil, etc.)                  │
└───────────────────────────────┬──────────────────────────────────────┘
                                │
                                │ HTTP Request (JSON)
                                │
┌───────────────────────────────▼──────────────────────────────────────┐
│                      SPRING BOOT APPLICATION                          │
│ ┌──────────────────────────────────────────────────────────────────┐ │
│ │                   CAPA DE PRESENTACIÓN                           │ │
│ │                    (Controller Layer)                            │ │
│ │  ┌────────────────────────────────────────────────────────┐     │ │
│ │  │           JuegoController                              │     │ │
│ │  │  - POST   /nuevo                                       │     │ │
│ │  │  - GET    /{id}                                        │     │ │
│ │  │  - POST   /{id}/movimiento                            │     │ │
│ │  │  - GET    /todos                                       │     │ │
│ │  │  - DELETE /{id}                                        │     │ │
│ │  └────────────────────────┬───────────────────────────────┘     │ │
│ └──────────────────────────┼───────────────────────────────────────┘ │
│                             │                                         │
│                             │ Inyección de Dependencias (DIP)        │
│                             │                                         │
│ ┌──────────────────────────▼───────────────────────────────────────┐ │
│ │                   CAPA DE NEGOCIO                                │ │
│ │                    (Service Layer)                               │ │
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
│ │  └────────────────────────────────────────────────────┘         │ │
│ │                                                                  │ │
│ │  ┌────────────────────────────────────────────────────────┐     │ │
│ │  │       IJugadorStrategy (Interface)                     │     │ │
│ │  │  ▲                                ▲                    │     │ │
│ │  │  │ Implementa                     │ Implementa         │     │ │
│ │  │  │                                │                    │     │ │
│ │  │  ├─ JugadorHumanoStrategy         └─ JugadorIAStrategy│     │ │
│ │  │     (recibe input vía API)          (calcula mejor    │     │ │
│ │  │                                      movimiento)       │     │ │
│ │  └────────────────────────────────────────────────────────┘     │ │
│ └──────────────────────────┬─────────────────────────────────────┘ │
│                             │                                        │
│                             │ Acceso a datos                         │
│                             │                                        │
│ ┌──────────────────────────▼───────────────────────────────────────┐ │
│ │                    CAPA DE PERSISTENCIA                          │ │
│ │                    (Repository Layer)                            │ │
│ │  ┌────────────────────────────────────────────────────────┐     │ │
│ │  │           JuegoRepository                              │     │ │
│ │  │  - guardar(Juego)                                      │     │ │
│ │  │  - buscarPorId(String)                                 │     │ │
│ │  │  - buscarTodos()                                       │     │ │
│ │  │  - eliminar(String)                                    │     │ │
│ │  │  - existe(String)                                      │     │ │
│ │  └────────────────────────┬───────────────────────────────┘     │ │
│ └──────────────────────────┼───────────────────────────────────────┘ │
│                             │                                         │
│                             │ CRUD                                    │
│                             │                                         │
│ ┌──────────────────────────▼───────────────────────────────────────┐ │
│ │                      CAPA DE DOMINIO                             │ │
│ │                      (Model Layer)                               │ │
│ │  ┌─────────────────────────────────────────────────────┐        │ │
│ │  │  Entidades:                                         │        │ │
│ │  │  • Juego (id, jugadores, tablero, estado)          │        │ │
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
│ └──────────────────────────────────────────────────────────────────┘ │
│                                                                       │
│ ┌──────────────────────────────────────────────────────────────────┐ │
│ │                 MANEJO DE EXCEPCIONES                            │ │
│ │  - GlobalExceptionHandler (@RestControllerAdvice)                │ │
│ │  - JuegoNoEncontradoException                                    │ │
│ │  - MovimientoInvalidoException                                   │ │
│ └──────────────────────────────────────────────────────────────────┘ │
│                                                                       │
│ ┌──────────────────────────────────────────────────────────────────┐ │
│ │                    CONFIGURACIÓN                                 │ │
│ │  - CorsConfig (permite peticiones desde cualquier origen)        │ │
│ │  - application.yml (puerto, logging, etc.)                       │ │
│ └──────────────────────────────────────────────────────────────────┘ │
└───────────────────────────────────────────────────────────────────────┘
                                │
                                │ HTTP Response (JSON)
                                │
┌───────────────────────────────▼──────────────────────────────────────┐
│                          CLIENTE HTTP                                 │
└──────────────────────────────────────────────────────────────────────┘
```

---

## Flujo de una Petición Completa

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
