# 🧪 Colección de Pruebas - Othello Backend API

Este archivo contiene ejemplos de peticiones para probar la API usando **PowerShell**, **cURL** o **Postman**.

## 📌 Variables Importantes

```bash
export BASE_URL="http://localhost:8080/api"
export JUEGO_ID="sustituir-con-id-real"
export VERSION=0  # Actualizar después de cada movimiento
export JWT_TOKEN="sustituir-con-token-recibido"
```

```powershell
$BaseUrl = "http://localhost:8080/api"
$JuegoId = "sustituir-con-id-real"
$Version = 0  # Actualizar después de cada movimiento
$JwtToken = "sustituir-con-token-recibido"
```

---

## 🔐 AUTENTICACIÓN - Endpoints de Login

### 1. Registrar Nuevo Usuario

#### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/auth/register" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"username": "juan", "email": "juan@example.com", "password": "123456"}'
```

#### cURL (Linux/Mac)
```bash
curl -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan",
    "email": "juan@example.com",
    "password": "123456"
  }'
```

#### Respuesta Esperada
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuIiwiaWF0IjoxNzA...",
  "username": "juan",
  "email": "juan@example.com",
  "roles": ["ROLE_USER"]
}
```

**⚠️ Importante**: Guarda el `token` para usarlo en peticiones protegidas.

### 2. Iniciar Sesión (Login)

#### PowerShell
```powershell
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"username": "juan", "password": "123456"}'

$jwt = ($response.Content | ConvertFrom-Json).token
$JwtToken = $jwt
Write-Host "Token JWT: $JwtToken"
```

#### cURL (Linux/Mac)
```bash
RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan",
    "password": "123456"
  }')
echo $RESPONSE | jq '.'
JWT_TOKEN=$(echo $RESPONSE | jq -r '.token')
echo "Token JWT: $JWT_TOKEN"
```

#### Respuesta Esperada
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuIiwiaWF0IjoxNzA...",
  "username": "juan",
  "email": "juan@example.com",
  "roles": ["ROLE_USER"]
}
```

### 3. Obtener Información del Usuario Actual

#### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/auth/me" `
  -Headers @{
    "Authorization"="Bearer $JwtToken"
  }
```

#### cURL (Linux/Mac)
```bash
curl -X GET "$BASE_URL/auth/me" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

#### Respuesta Esperada
```json
{
  "username": "juan",
  "email": "juan@example.com",
  "roles": ["ROLE_USER"],
  "partidasJugadas": 15,
  "partidasGanadas": 8,
  "partidasPerdidas": 7,
  "fechaRegistro": "2026-03-03T10:30:00",
  "ultimoAcceso": "2026-03-03T14:25:00"
}
```

---

## 🗄️ INFORMACIÓN DE BASE DE DATOS MONGODB

### Configuración de Conexión

**URL de conexión** (MongoDB Atlas - Cloud):
```
mongodb+srv://othello:othello123@cluster0.mongodb.net/othello_db?retryWrites=true&w=majority
```

**URL de conexión** (MongoDB Local):
```
mongodb://localhost:27017/othello_db
```

### Colecciones en la Base de Datos

1. **usuarios** - Almacena cuentas de usuario
   - `_id`: ID de MongoDB
   - `username`: Nombre de usuario (único)
   - `email`: Email (único)
   - `password`: Contraseña hasheada (BCrypt)
   - `roles`: Roles del usuario
   - `fechaRegistro`: Fecha de creación
   - `partidasJugadas`, `partidasGanadas`, `partidasPerdidas`: Estadísticas

2. **juegos** - Almacena partidas de Othello
   - `_id`: ID de MongoDB
   - `jugador1`, `jugador2`: Información de jugadores
   - `tablero`: Estado del tablero 8x8
   - `estado`: ACTIVE, FINISHED, DRAW
   - `fechaCreacion`, `fechaUltimoMovimiento`: Timestamps
   - `version`: Control de concurrencia

### Acceso Directo a MongoDB

#### Conectar con MongoDB Shell
```bash
# MongoDB Atlas
mongosh "mongodb+srv://othello:othello123@cluster0.mongodb.net/othello_db"

# MongoDB Local
mongosh mongodb://localhost:27017/othello_db
```

#### Consultas Útiles
```javascript
// Ver todos los usuarios
db.usuarios.find().pretty()

// Ver todos los juegos
db.juegos.find().pretty()

// Contar usuarios registrados
db.usuarios.countDocuments()

// Buscar un usuario específico
db.usuarios.findOne({username: "juan"})

// Ver juegos activos
db.juegos.find({estado: "ACTIVE"}).pretty()

// Eliminar todos los juegos
db.juegos.deleteMany({})
```

---

## 🎮 ENDPOINTS DE JUEGO (Requieren Autenticación)

### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/health"
```

### cURL (Linux/Mac)
```bash
curl -X GET "$BASE_URL/juego/health"
```

### Respuesta Esperada
```json
{
  "status": "ok",
  "message": "Othello Backend API está funcionando correctamente"
}
```

---

## 🎮 ENDPOINTS DE JUEGO (Requieren Autenticación)

**Nota**: Todos los endpoints de juego requieren el header `Authorization: Bearer {token}`

### 1. Verificar Salud del Servicio

#### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/health" `
  -Headers @{"Authorization"="Bearer $JwtToken"}
```

#### cURL (Linux/Mac)
```bash
curl -X GET "$BASE_URL/juego/health" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

#### Respuesta Esperada
```json
{
  "status": "ok",
  "message": "Othello Backend API está funcionando correctamente"
}
```

---

### 2. Crear Nuevo Juego (Humano vs IA)

### 2. Crear Nuevo Juego (Humano vs IA)

#### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/nuevo" `
  -Method POST `
  -Headers @{
    "Content-Type"="application/json"
    "Authorization"="Bearer $JwtToken"
  } `
  -Body '{"nombreJugador1": "Stark", "nombreJugador2": "Ultron", "tipoJugador2": "IA"}'
```

#### cURL (Linux/Mac)
```bash
curl -X POST "$BASE_URL/juego/nuevo" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "nombreJugador1": "Stark",
    "nombreJugador2": "Ultron",
    "tipoJugador2": "IA"
  }'
```

### Respuesta Esperada
```json
{
  "game": {
    "id": "f17b59f4-4eff-4b0f-a987-5600b31e0326",
    "board": [
      ["", "", "", "", "", "", "", ""],
      ["", "", "", "", "", "", "", ""],
      ["", "", "", "", "", "", "", ""],
      ["", "", "", "W", "B", "", "", ""],
      ["", "", "", "B", "W", "", "", ""],
      ["", "", "", "", "", "", "", ""],
      ["", "", "", "", "", "", "", ""],
      ["", "", "", "", "", "", "", ""]
    ],
    "currentPlayer": "B",
    "status": "active",
    "passCount": 0,
    "winner": null,
    "score": {
      "Stark": 2,
      "Ultron": 2
    },
    "validMoves": [
      {"row": 2, "col": 3},
      {"row": 3, "col": 2},
      {"row": 4, "col": 5},
      {"row": 5, "col": 4}
    ],
    "version": 0
  }
}
``# 3. Crear Nuevo Juego (Humano vs Humano)

#### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/nuevo" `
  -Method POST `
  -Headers @{
    "Content-Type"="application/json"
    "Authorization"="Bearer $JwtToken"
  } `
  -Body '{"nombreJugador1": "Capitán América", "nombreJugador2": "Iron Man", "tipoJugador2": "HUMANO"}'
```

#### cURL (Linux/Mac)
```bash
curl -X POST "$BASE_URL/juego/nuevo" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN América", "nombreJugador2": "Iron Man", "tipoJugador2": "HUMANO"}'
```

### cURL (Linux/Mac)
```bash
curl -X POST "$BASE_URL/juego/nuevo" \
  -H "Content-Type: application/json" \
  -d '{
    "nombreJugador1": "Capitán América",
    "nombreJugador2": "Iron Man",
    "tipoJugador2": "HUMANO"
  }'
```

---

### 4. Obtener Estado de un Juego

#### PowerShell
```powershell
$JuegoId = "f17b59f4-4eff-4b0f-a987-5600b31e0326"
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/$JuegoId" `
  -Headers @{"Authorization"="Bearer $JwtToken"}
```

#### cURL (Linux/Mac)
```bash
curl -X GET "$BASE_URL/juego/$JUEGO_ID" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### Respuesta
El mismo formato que al crear el juego, con el estado actualizado.

---

### 5. Realizar un Movimiento

**⚠️ IMPORTANTE**: Debes incluir el campo `expectedVersion` con la versión actual del juego.

#### PowerShell
```powershell
$JuegoId = "f17b59f4-4eff-4b0f-a987-5600b31e0326"
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/$JuegoId/movimiento" `
  -Method POST `
  -Headers @{
    "Content-Type"="application/json"
    "Authorization"="Bearer $JwtToken"
  } `
  -Body '{"row": 2, "col": 3, "expectedVersion": 0}'
```

#### cURL (Linux/Mac)
```bash
curl -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "row": 2,
    "col": 3,
    "expectedVersion": 0
  }'
```

### Respuesta Exitosa
```json
{
  "game": {
    "id": "f17b59f4-4eff-4b0f-a987-5600b31e0326",
    "board": [
      ["", "", "", "", "", "", "", ""],
      ["", "", "", "", "", "", "", ""],
      ["", "", "W", "B", "", "", "", ""],
      ["", "", "", "W", "B", "", "", ""],
      ["", "", "", "B", "W", "", "", ""],
      ["", "", "", "", "", "", "", ""],
      ["", "", "", "", "", "", "", ""],
      ["", "", "", "", "", "", "", ""]
    ],
    "currentPlayer": "B",
    "status": "active",
    "passCount": 0,
    "winner": null,
    "score": {
      "Stark": 3,
      "Ultron": 3
    },
    "validMoves": [...],
    "version": 2
  }
}
```

**📝 Nota**: La versión se incrementa después de cada movimiento (jugador + IA = +2 versiones).

### Error 409 - Conflicto de Versión
Si otro jugador hizo un movimiento antes, recibirás:
```json
{
  "error": "version_conflict",
  "message": "Conflicto de versión: esperada 0, actual 2",
  "expectedVersion": 0,
  "currentVersion": 2
}
```

**Solución**: Obtén el estado actual del juego (GET) para conseguir la versión correcta.

---

### 6. Obtener Todos los Juegos

#### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/todos" `
  -Headers @{"Authorization"="Bearer $JwtToken"}
```

#### cURL (Linux/Mac)
```bash
curl -X GET "$BASE_URL/juego/todos" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### Respuesta
```json
[
  {
    "game": {
      "id": "game-1-id",
      "board": [...],
      "currentPlayer": "B",
      "status": "active",
      "version": 5,
      ...
    }
  },
  {
    "game": {
      "id": "game-2-id",
      "board": [...],
      "currentPlayer": "W",
      "status": "finished",
      "winner": "Iron Man",
      "version": 48,
      ...
    }
  }
]
```

---

### 7. Eliminar un Juego

#### PowerShell
```powershell
$JuegoId = "f17b59f4-4eff-4b0f-a987-5600b31e0326"
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/$JuegoId" `
  -Method DELETE `
  -Headers @{"Authorization"="Bearer $JwtToken"}
```

#### cURL (Linux/Mac)
```bash
curl -X DELETE "$BASE_URL/juego/$JUEGO_ID" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### Respuesta
```json
{
  "message": "Juego eliminado exitosamente"
}
```

---

## 📋 Secuencia de Prueba Completa

### PowerShell Script
```powershell
$BaseUrl = "http://localhost:8080/api"

Write-Host "=== 0. Iniciar sesión ===" -ForegroundColor Cyan
$loginResponse = Invoke-WebRequest -Uri "$BaseUrl/auth/login" `
    -Method POST `
    -Headers @{"Content-Type"="application/json"} `
    -Body '{"username":"juan","password":"123456"}'

$JwtToken = ($loginResponse.Content | ConvertFrom-Json).token
Write-Host "Token JWT obtenido: $($JwtToken.Substring(0,20))..." -ForegroundColor Cyan
Start-Sleep -Seconds 1

Write-Host "`n=== 1. Verificar salud del servicio ===" -ForegroundColor Green
$health = Invoke-WebRequest -Uri "$BaseUrl/juego/health" `
    -Headers @{"Authorization"="Bearer $JwtToken"}
$health.Content | ConvertFrom-Json | ConvertTo-Json
Start-Sleep -Seconds 1

Write-Host "`n=== 2. Crear nuevo juego ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$BaseUrl/juego/nuevo" `
    -Method POST `
    -Headers @{
        "Content-Type"="application/json"
        "Authorization"="Bearer $JwtToken"
    } `
    -Body '{"nombreJugador1":"Stark","nombreJugador2":"Ultron","tipoJugador2":"IA"}'

$gameData = ($response.Content | ConvertFrom-Json).game
$juegoId = $gameData.id
$version = $gameData.version

Write-Host "Juego creado con ID: $juegoId"
Write-Host "Versión inicial: $version"
Start-Sleep -Seconds 1

Write-Host "`n=== 3. Obtener estado del juego ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$BaseUrl/juego/$juegoId" `
    -Headers @{"Authorization"="Bearer $JwtToken"}
($response.Content | ConvertFrom-Json).game | ConvertTo-Json -Depth 5
Start-Sleep -Seconds 1

Write-Host "`n=== 4. Realizar movimiento ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$BaseUrl/juego/$juegoId/movimiento" `
    -Method POST `
    -Headers @{
        "Content-Type"="application/json"
        "Authorization"="Bearer $JwtToken"
    } `
    -Body "{`"row`":2,`"col`":3,`"expectedVersion`":$version}"

$gameData = ($response.Content | ConvertFrom-Json).game
$version = $gameData.version

Write-Host "Nueva versión: $version"
($response.Content | ConvertFrom-Json).game | ConvertTo-Json -Depth 5
Start-Sleep -Seconds 1

Write-Host "`n=== 5. Listar todos los juegos ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$BaseUrl/juego/todos" `
    -Headers @{"Authorization"="Bearer $JwtToken"}
$response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 5

Write-Host "`n=== 6. Intentar movimiento con versión incorrecta ===" -ForegroundColor Yellow
try {
    Invoke-WebRequest -Uri "$BaseUrl/juego/$juegoId/movimiento" `
        -Method POST `
        -Headers @{
            "Content-Type"="application/json"
            "Authorization"="Bearer $JwtToken"
        } `
        -Body '{"row":3,"col":2,"expectedVersion":0}'
} catch {
    $statusCode = [int]$_.Exception.Response.StatusCode
    $stream = $_.Exception.Response.GetResponseStream()
    $reader = New-Object System.IO.StreamReader($stream)
    $responseBody = $reader.ReadToEnd()
    Write-Host "Status Code: $statusCode (Conflict esperado)" -ForegroundColor Yellow
    Write-Host "Response: $responseBody"
}
```

### Bash Script (Linux/Mac)
```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api"

echo "=== 0. Iniciar sesión ==="
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"juan","password":"123456"}')
JWT_TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.token')
echo "Token JWT obtenido: ${JWT_TOKEN:0:20}..."
echo -e "\n\n"

echo "=== 1. Verificar salud del servicio ==="
curl -X GET "$BASE_URL/juego/health" \
  -H "Authorization: Bearer $JWT_TOKEN"
echo -e "\n\n"

echo "=== 2. Crear nuevo juego ==="
RESPONSE=$(curl -s -X POST "$BASE_URL/juego/nuevo" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "nombreJugador1": "Stark",
    "nombreJugador2": "Ultron",
    "tipoJugador2": "IA"
  }')
echo $RESPONSE | jq '.'
JUEGO_ID=$(echo $RESPONSE | jq -r '.game.id')
VERSION=$(echo $RESPONSE | jq -r '.game.version')
echo "Juego ID: $JUEGO_ID"
echo "Versión inicial: $VERSION"
echo -e "\n\n"

echo "=== 3. Obtener estado del juego ==="
curl -s -X GET "$BASE_URL/juego/$JUEGO_ID" \
  -H "Authorization: Bearer $JWT_TOKEN" | jq '.'
echo -e "\n\n"

echo "=== 4. Realizar movimiento ==="
RESPONSE=$(curl -s -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d "{
    \"row\": 2,
    \"col\": 3,
    \"expectedVersion\": $VERSION
  }")
echo $RESPONSE | jq '.'
VERSION=$(echo $RESPONSE | jq -r '.game.version')
echo "Nueva versión: $VERSION"
echo -e "\n\n"

echo "=== 5. Listar todos los juegos ==="
curl -s -X GET "$BASE_URL/juego/todos" \
  -H "Authorization: Bearer $JWT_TOKEN" | jq '.'
echo -e "\n\n"

echo "=== 6. Intentar movimiento con versión incorrecta ==="
curl -s -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Conten1 - Sin Autenticación
```powershell
# Intentar acceder sin token JWT
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/health"
# Respuesta: 401 Unauthorized
```

### Error 403 - Token Inválido o Expirado
```bash
curl -X GET "$BASE_URL/juego/health" \
  -H "Authorization: Bearer token_invalido"
# Respuesta: 403 Forbidden
```

### Error 400 - Movimiento Sin expectedVersion
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/$JuegoId/movimiento" `
  -Method POST `
  -Headers @{
    "Content-Type"="application/json"
    "Authorization"="Bearer $JwtToken"
  } `
  -Body '{"row": 2, "col": 3}'
```

### Error 409 - Conflicto de Versión
```powershell
# Intentar con una versión antigua
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/$JuegoId/movimiento" `
  -Method POST `
  -Headers @{
    "Content-Type"="application/json"
    "Authorization"="Bearer $JwtToken"
  } `
  -Body '{"row": 2, "col": 3, "expectedVersion": 0}'
```

### Error 400 - Movimiento Inválido (posición ocupada)
```bash
curl -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN

### Error 409 - Conflicto de Versión
```powershell
# Intentar con una versión antigua
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/$JuegoId/movimiento" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"row": 2, "col": 3, "expectedVersion": 0}'
```

### Error 400 - Movimiento Inválido (posición ocupada)
```bash
curl -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
  -d '{
    "row": 3,
    "col": 3,
    "expectedVersion": 0
  }'
```

### Error 400 - Movimiento Inválido (fuera de rango)
```bash
curl -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "row": 10,
    "col": 10,
    "expectedVersion": 0
  }'
```

### Error 404 - Juego No Encontrado
```bash
curl -X GET "$BASE_URL/juego/id-inexistente" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### Error 400 - Datos Inválidos (validación)
```bash
curl -X POST "$BASE_URL/juego/nuevo" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "nombreJugador1": "",
    "nombreJugador2": "Ultron"
  }'
```

---

## 📦 Importar en Postman

1. Abre Postman
2. Click en **Import**
3. Crea una nueva colección "Othello API"
4. Agrega las siguientes peticiones:

### 0. Variables de Entorno
Crea un entorno con estas variables:
- `baseUrl`: `http://localhost:8080/api`
- `jwtToken`: (se actualiza después de login)
- `juegoId`: (se actualiza después de crear juego)
- `version`: (se actualiza después de cada movimiento)

### 1. Registrar Usuario
- **Método**: POST
- **URL**: `{{baseUrl}}/auth/register`
- **Body** (raw JSON):
```json
{
  "username": "juan",
  "email": "juan@example.com",
  "password": "123456"
}
```

### 2. Login
- **Método**: POST
- **URL**: `{{baseUrl}}/auth/login`
- **Body** (raw JSON):
```json
{
  "username": "juan",
  "password": "123456"
}
```
- **Tests** (para capturar token automáticamente):
```javascript
pm.test("Login exitoso", function () {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.environment.set("jwtToken", jsonData.token);
});
```

### 3. Health Check
- **Método**: GET
- **URL**: `{{baseUrl}}/juego/health`
- **Headers**: 
  - `Authorization`: `Bearer {{jwtToken}}`

### 4. Crear Juego
- **Método**: POST
- **URL**: `{{baseUrl}}/juego/nuevo`
- **Headers**: 
  - `Authorization`: `Bearer {{jwtToken}}`
  - `Content-Type`: `application/json`
- **Body** (raw JSON):
```json
{
  "nombreJugador1": "Stark",
  "nombreJugador2": "Ultron",
  "tipoJugador2": "IA"
}
```
- **Tests** (para capturar ID y versión):
```javascript
pm.test("Juego creado", function () {
    var jsonData = pm.response.json();
    pm.environment.set("juegoId", jsonData.game.id);
    pm.environment.set("version", jsonData.game.version);
});
```

### 5. Obtener Estado
- **Método**: GET
- **URL**: `{{baseUrl}}/juego/{{juegoId}}`
- **Headers**: 
  - `Authorization`: `Bearer {{jwtToken}}`

### 6. Realizar Movimiento
- **Método**: POST
- **URL**: `{{baseUrl}}/juego/{{juegoId}}/movimiento`
- **Headers**: 
  - `Authorization`: `Bearer {{jwtToken}}`
  - `Content-Type`: `application/json`
- **Body** (raw JSON):
```json
{
  "row": 2,
  "col": 3,
  "expectedVersion": {{version}}
}
```
- **Tests** (para actualizar versión):
```javascript
pm.test("Movimiento exitoso", function () {
    var jsonData = pm.response.json();
    pm.environment.set("version", jsonData.game.version);
});
```

---

## 🔗 Comandos cURL para Postman

Estos comandos están en formato compatible con Postman. Puedes copiarlos y usar el botón "Import" → "Raw text" en Postman.

**⚠️ Importante**: Primero ejecuta el endpoint de login (comando 1) y copia el token recibido.

### 0. Registrar Nuevo Usuario
```bash
curl --location 'http://localhost:8080/api/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "juan",
    "email": "juan@example.com",
    "password": "123456"
}'
```

### 1. Login y Obtener Token JWT
```bash
curl --location 'http://localhost:8080/api/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "juan",
    "password": "123456"
}'
```
**Respuesta**: Copia el valor de `token` y úsalo en los siguientes comandos.

### 2. Health Check
```bash
curl --location 'http://localhost:8080/api/juego/health' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}'
```

### 3. Crear Juego vs IA
```bash
curl --location 'http://localhost:8080/api/juego/nuevo' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}' \
--data '{
    "nombreJugador1": "Juan",
    "nombreJugador2": "IA",
    "tipoJugador2": "IA"
}'
```

### 4. Crear Juego vs Humano
```bash
curl --location 'http://localhost:8080/api/juego/nuevo' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}' \
--data '{
    "nombreJugador1": "Juan",
    "nombreJugador2": "Pedro",
    "tipoJugador2": "HUMANO"
}'
```

### 5. Obtener Estado del Juego
```bash
curl --location 'http://localhost:8080/api/juego/{JUEGO_ID}' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}'
```

### 6. Realizar Movimiento
```bash
curl --location 'http://localhost:8080/api/juego/{JUEGO_ID}/movimiento' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}' \
--data '{
    "row": 2,
    "col": 3,
    "expectedVersion": 0
}'
```

### 7. Listar Todos los Juegos
```bash
curl --location 'http://localhost:8080/api/juego/todos' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}'
```

### 8. Eliminar Juego
```bash
curl --location --request DELETE 'http://localhost:8080/api/juego/{JUEGO_ID}' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}'
```

### 9. Error 409 - Movimiento con Versión Incorrecta
```bash
curl --location 'http://localhost:8080/api/juego/{JUEGO_ID}/movimiento' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}' \
--data '{
    "row": 3,
    "col": 2,
    "expectedVersion": 0
}'
```

### 10. Error 400 - Movimiento en Posición Ocupada
```bash
curl --location 'http://localhost:8080/api/juego/{JUEGO_ID}/movimiento' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}' \
--data '{
    "row": 3,
    "col": 3,
    "expectedVersion": 0
}'
```

### 11. Error 400 - Movimiento Fuera de Rango
```bash
curl --location 'http://localhost:8080/api/juego/{JUEGO_ID}/movimiento' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}' \
--data '{
    "row": 10,
    "col": 10,
    "expectedVersion": 0
}'
```

### 12. Error 400 - Crear Juego con Datos Inválidos
```bash
curl --location 'http://localhost:8080/api/juego/nuevo' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}' \
--data '{
    "nombreJugador1": "",
    "nombreJugador2": "IA"
}'
```

### 13. Error 404 - Obtener Juego Inexistente
```bash
curl --location 'http://localhost:8080/api/juego/id-inexistente' \
--header 'Authorization: Bearer {TU_TOKEN_AQUI}'
```

### 14. Error 401 - Sin Autenticación
```bash
curl --location 'http://localhost:8080/api/juego/health'
```

### 15. Error 403 - Token Inválido
```bash
curl --location 'http://localhost:8080/api/juego/health' \
--header 'Authorization: Bearer token_invalido'
```

**📝 Notas:**
- Reemplaza `{TU_TOKEN_AQUI}` con el token JWT obtenido del login
- Reemplaza `{JUEGO_ID}` con el ID real del juego creado
- Actualiza `expectedVersion` con la versión actual antes de cada movimiento
- Para importar en Postman: Click en "Import" → "Raw text" → Pega el curl → "Continue"
- El token JWT expira en 24 horas, después deberás hacer login nuevamente

---

## �🔧 Tips

- **PowerShell**: Usa `| ConvertFrom-Json | ConvertTo-Json -Depth 10` para formatear JSON
- **Bash**: Usa `| jq '.'` para formatear JSON (requiere instalación de `jq`)
- **Versión**: Siempre obtén la versión actual antes de hacer un movimiento
- **IDs**: Guarda el ID del juego como variable de entorno
- **Logs**: Revisa los logs de la aplicación para debugging detallado

---

## 📚 Campos Importantes

### Board (Tablero)
- **B**: Ficha negra (Black)
- **W**: Ficha blanca (White)  
- **""**: Casilla vacía (Empty)

### Status (Estado del Juego)
- **active**: Juego en curso
- **finished**: Juego terminado con ganador
- **draw**: Empate

### Coordenadas
- **row**: Fila (0-7)
- **col**: Columna (0-7)
- Origen (0,0) en esquina superior izquierda

### Control de Versión
- Cada movimiento incrementa `version`
- Movimiento del jugador humano: +1
- Movimiento de la IA (automático): +1
- Total por turno completo: +2 (si juega contra IA)

---

**¡Happy Testing! 🚀**
