# 🧪 Colección de Pruebas - Othello Backend API

Este archivo contiene ejemplos de peticiones para probar la API usando **PowerShell**, **cURL** o **Postman**.

## 📌 Variables Importantes

```bash
export BASE_URL="http://localhost:8080/api"
export JUEGO_ID="sustituir-con-id-real"
export VERSION=0  # Actualizar después de cada movimiento
```

```powershell
$BaseUrl = "http://localhost:8080/api"
$JuegoId = "sustituir-con-id-real"
$Version = 0  # Actualizar después de cada movimiento
```

---

## 1. Verificar Salud del Servicio

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

## 2. Crear Nuevo Juego (Humano vs IA)

### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/nuevo" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"nombreJugador1": "Stark", "nombreJugador2": "Ultron", "tipoJugador2": "IA"}'
```

### cURL (Linux/Mac)
```bash
curl -X POST "$BASE_URL/juego/nuevo" \
  -H "Content-Type: application/json" \
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
```

**⚠️ Importante**: Guarda el `id` y el `version` para las siguientes peticiones.

---

## 3. Crear Nuevo Juego (Humano vs Humano)

### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/nuevo" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"nombreJugador1": "Capitán América", "nombreJugador2": "Iron Man", "tipoJugador2": "HUMANO"}'
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

## 4. Obtener Estado de un Juego

### PowerShell
```powershell
$JuegoId = "f17b59f4-4eff-4b0f-a987-5600b31e0326"
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/$JuegoId"
```

### cURL (Linux/Mac)
```bash
curl -X GET "$BASE_URL/juego/$JUEGO_ID"
```

### Respuesta
El mismo formato que al crear el juego, con el estado actualizado.

---

## 5. Realizar un Movimiento

**⚠️ IMPORTANTE**: Debes incluir el campo `expectedVersion` con la versión actual del juego.

### PowerShell
```powershell
$JuegoId = "f17b59f4-4eff-4b0f-a987-5600b31e0326"
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/$JuegoId/movimiento" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"row": 2, "col": 3, "expectedVersion": 0}'
```

### cURL (Linux/Mac)
```bash
curl -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
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

## 6. Obtener Todos los Juegos

### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/todos"
```

### cURL (Linux/Mac)
```bash
curl -X GET "$BASE_URL/juego/todos"
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

## 7. Eliminar un Juego

### PowerShell
```powershell
$JuegoId = "f17b59f4-4eff-4b0f-a987-5600b31e0326"
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/$JuegoId" -Method DELETE
```

### cURL (Linux/Mac)
```bash
curl -X DELETE "$BASE_URL/juego/$JUEGO_ID"
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

Write-Host "=== 1. Verificar salud del servicio ===" -ForegroundColor Green
$health = Invoke-WebRequest -Uri "$BaseUrl/juego/health"
$health.Content | ConvertFrom-Json | ConvertTo-Json
Start-Sleep -Seconds 1

Write-Host "`n=== 2. Crear nuevo juego ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$BaseUrl/juego/nuevo" `
    -Method POST `
    -Headers @{"Content-Type"="application/json"} `
    -Body '{"nombreJugador1":"Stark","nombreJugador2":"Ultron","tipoJugador2":"IA"}'

$gameData = ($response.Content | ConvertFrom-Json).game
$juegoId = $gameData.id
$version = $gameData.version

Write-Host "Juego creado con ID: $juegoId"
Write-Host "Versión inicial: $version"
Start-Sleep -Seconds 1

Write-Host "`n=== 3. Obtener estado del juego ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$BaseUrl/juego/$juegoId"
($response.Content | ConvertFrom-Json).game | ConvertTo-Json -Depth 5
Start-Sleep -Seconds 1

Write-Host "`n=== 4. Realizar movimiento ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$BaseUrl/juego/$juegoId/movimiento" `
    -Method POST `
    -Headers @{"Content-Type"="application/json"} `
    -Body "{`"row`":2,`"col`":3,`"expectedVersion`":$version}"

$gameData = ($response.Content | ConvertFrom-Json).game
$version = $gameData.version

Write-Host "Nueva versión: $version"
($response.Content | ConvertFrom-Json).game | ConvertTo-Json -Depth 5
Start-Sleep -Seconds 1

Write-Host "`n=== 5. Listar todos los juegos ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$BaseUrl/juego/todos"
$response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 5

Write-Host "`n=== 6. Intentar movimiento con versión incorrecta ===" -ForegroundColor Yellow
try {
    Invoke-WebRequest -Uri "$BaseUrl/juego/$juegoId/movimiento" `
        -Method POST `
        -Headers @{"Content-Type"="application/json"} `
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

echo "=== 1. Verificar salud del servicio ==="
curl -X GET "$BASE_URL/juego/health"
echo -e "\n\n"

echo "=== 2. Crear nuevo juego ==="
RESPONSE=$(curl -s -X POST "$BASE_URL/juego/nuevo" \
  -H "Content-Type: application/json" \
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
curl -s -X GET "$BASE_URL/juego/$JUEGO_ID" | jq '.'
echo -e "\n\n"

echo "=== 4. Realizar movimiento ==="
RESPONSE=$(curl -s -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
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
curl -s -X GET "$BASE_URL/juego/todos" | jq '.'
echo -e "\n\n"

echo "=== 6. Intentar movimiento con versión incorrecta ==="
curl -s -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
  -d '{
    "row": 3,
    "col": 2,
    "expectedVersion": 0
  }' | jq '.'
echo -e "\n\n"
```

---

## 🧪 Casos de Prueba - Validaciones

### Error 400 - Movimiento Sin expectedVersion
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/juego/$JuegoId/movimiento" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"row": 2, "col": 3}'
```

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
  -d '{
    "row": 10,
    "col": 10,
    "expectedVersion": 0
  }'
```

### Error 404 - Juego No Encontrado
```bash
curl -X GET "$BASE_URL/juego/id-inexistente"
```

### Error 400 - Datos Inválidos (validación)
```bash
curl -X POST "$BASE_URL/juego/nuevo" \
  -H "Content-Type: application/json" \
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

### Health Check
- **Método**: GET
- **URL**: `http://localhost:8080/api/juego/health`

### Crear Juego
- **Método**: POST
- **URL**: `http://localhost:8080/api/juego/nuevo`
- **Body** (raw JSON):
```json
{
  "nombreJugador1": "Stark",
  "nombreJugador2": "Ultron",
  "tipoJugador2": "IA"
}
```

### Obtener Estado
- **Método**: GET
- **URL**: `http://localhost:8080/api/juego/{{juegoId}}`

### Realizar Movimiento
- **Método**: POST
- **URL**: `http://localhost:8080/api/juego/{{juegoId}}/movimiento`
- **Body** (raw JSON):
```json
{
  "row": 2,
  "col": 3,
  "expectedVersion": {{version}}
}
```

### Variables de Entorno en Postman
Crea un entorno con estas variables:
- `baseUrl`: `http://localhost:8080/api`
- `juegoId`: (se actualiza manualmente después de crear juego)
- `version`: (se actualiza manualmente después de cada movimiento)

---

## 🔧 Tips

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
