# 🧪 Colección de Pruebas - Othello Backend API

Este archivo contiene ejemplos de peticiones para probar la API usando **cURL** o **Postman**.

## Variables

```bash
export BASE_URL="http://localhost:8080/api"
export JUEGO_ID="sustituir-con-id-real"
```

---

## 1. Verificar Salud del Servicio

### cURL
```bash
curl -X GET "$BASE_URL/juego/health"
```

### Respuesta Esperada
```json
{
  "exito": true,
  "mensaje": "Othello Backend API está funcionando correctamente"
}
```

---

## 2. Crear Nuevo Juego (Humano vs IA)

### cURL
```bash
curl -X POST "$BASE_URL/juego/nuevo" \
  -H "Content-Type: application/json" \
  -d '{
    "nombreJugador1": "Stark",
    "nombreJugador2": "Ultron",
    "tipoJugador2": "IA"
  }'
```

### PowerShell
```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/juego/nuevo" `
  -ContentType "application/json" `
  -Body '{"nombreJugador1":"Stark","nombreJugador2":"Ultron","tipoJugador2":"IA"}'
```

---

## 3. Crear Nuevo Juego (Humano vs Humano)

### cURL
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

### cURL
```bash
curl -X GET "$BASE_URL/juego/$JUEGO_ID"
```

### PowerShell
```powershell
$juegoId = "tu-juego-id-aqui"
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/juego/$juegoId"
```

---

## 5. Realizar un Movimiento

### cURL
```bash
curl -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
  -d '{
    "fila": 2,
    "columna": 3
  }'
```

### PowerShell
```powershell
$juegoId = "tu-juego-id-aqui"
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/juego/$juegoId/movimiento" `
  -ContentType "application/json" `
  -Body '{"fila":2,"columna":3}'
```

---

## 6. Obtener Todos los Juegos

### cURL
```bash
curl -X GET "$BASE_URL/juego/todos"
```

### PowerShell
```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/juego/todos"
```

---

## 7. Eliminar un Juego

### cURL
```bash
curl -X DELETE "$BASE_URL/juego/$JUEGO_ID"
```

### PowerShell
```powershell
$juegoId = "tu-juego-id-aqui"
Invoke-RestMethod -Method Delete -Uri "http://localhost:8080/api/juego/$juegoId"
```

---

## 📋 Secuencia de Prueba Completa

### Bash Script
```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api"

echo "=== 1. Verificar salud del servicio ==="
curl -X GET "$BASE_URL/juego/health"
echo -e "\n\n"

echo "=== 2. Crear nuevo juego ==="
RESPONSE=$(curl -X POST "$BASE_URL/juego/nuevo" \
  -H "Content-Type: application/json" \
  -d '{
    "nombreJugador1": "Stark",
    "nombreJugador2": "Ultron",
    "tipoJugador2": "IA"
  }')
echo $RESPONSE
JUEGO_ID=$(echo $RESPONSE | jq -r '.data.id')
echo "Juego ID: $JUEGO_ID"
echo -e "\n\n"

echo "=== 3. Obtener estado del juego ==="
curl -X GET "$BASE_URL/juego/$JUEGO_ID"
echo -e "\n\n"

echo "=== 4. Realizar movimiento ==="
curl -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
  -d '{
    "fila": 2,
    "columna": 3
  }'
echo -e "\n\n"

echo "=== 5. Listar todos los juegos ==="
curl -X GET "$BASE_URL/juego/todos"
echo -e "\n\n"
```

### PowerShell Script
```powershell
$BaseUrl = "http://localhost:8080/api"

Write-Host "=== 1. Verificar salud del servicio ===" -ForegroundColor Green
Invoke-RestMethod -Method Get -Uri "$BaseUrl/juego/health" | ConvertTo-Json
Start-Sleep -Seconds 1

Write-Host "`n=== 2. Crear nuevo juego ===" -ForegroundColor Green
$nuevoJuego = @{
    nombreJugador1 = "Stark"
    nombreJugador2 = "Ultron"
    tipoJugador2 = "IA"
} | ConvertTo-Json

$response = Invoke-RestMethod -Method Post -Uri "$BaseUrl/juego/nuevo" `
    -ContentType "application/json" -Body $nuevoJuego
$juegoId = $response.data.id
Write-Host "Juego creado con ID: $juegoId"
Start-Sleep -Seconds 1

Write-Host "`n=== 3. Obtener estado del juego ===" -ForegroundColor Green
Invoke-RestMethod -Method Get -Uri "$BaseUrl/juego/$juegoId" | ConvertTo-Json -Depth 10
Start-Sleep -Seconds 1

Write-Host "`n=== 4. Realizar movimiento ===" -ForegroundColor Green
$movimiento = @{
    fila = 2
    columna = 3
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "$BaseUrl/juego/$juegoId/movimiento" `
    -ContentType "application/json" -Body $movimiento | ConvertTo-Json -Depth 10
Start-Sleep -Seconds 1

Write-Host "`n=== 5. Listar todos los juegos ===" -ForegroundColor Green
Invoke-RestMethod -Method Get -Uri "$BaseUrl/juego/todos" | ConvertTo-Json -Depth 10
```

---

## 🧪 Casos de Prueba - Validaciones

### Movimiento Inválido (posición ocupada)
```bash
curl -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
  -d '{
    "fila": 3,
    "columna": 3
  }'
```

### Movimiento Inválido (fuera de rango)
```bash
curl -X POST "$BASE_URL/juego/$JUEGO_ID/movimiento" \
  -H "Content-Type: application/json" \
  -d '{
    "fila": 10,
    "columna": 10
  }'
```

### Juego No Encontrado
```bash
curl -X GET "$BASE_URL/juego/id-inexistente"
```

### Datos Inválidos (validación)
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
3. Crea una nueva colección
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

### Realizar Movimiento
- **Método**: POST
- **URL**: `http://localhost:8080/api/juego/{{juegoId}}/movimiento`
- **Body** (raw JSON):
```json
{
  "fila": 2,
  "columna": 3
}
```

---

## 🔧 Tips

- Guarda el `juegoId` como variable de entorno en Postman
- Usa `| jq` en bash para formatear JSON
- Usa `| ConvertTo-Json -Depth 10` en PowerShell para ver objetos anidados
- Revisa los logs de la aplicación para debugging

---

**¡Happy Testing! 🚀**
