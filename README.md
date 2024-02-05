
# Servicio Usuarios

Este microservicio es una pequeña API desarrollada en Java utilizando Spring Boot para la creación de un usuario.

## Prerrequisitos

- Java 11

## Clone el repositorio 
   ```
   git clone https://github.com/kamalfontirroig/usuario-microservicio.git
   ```

## Uso

```javascript
curl --request POST \
  --url http://localhost:8080/usuarios \
  --header 'Content-type: application/json' \
  --data '{
    "email": "juan@rodriguez.org",
    "name": "Juan Rodriguez",
    "password": "hunt3rX1",
    "phones": [
        {
            "citycode": "1",
            "countrycode": "57",
            "number": "1234567"
        }
    ]
}'
```

## Respuesta

#### La API devolverá una respuesta en formato JSON:


✅  `201  Created` - El usuario ha sido registrado, se entrega un token JWT
```json
{
	"id": "1bbfb996-48a0-4d5b-8673-21da38c67fff",
	"created": "2024-02-04T16:13:55.083+00:00",
	"modified": "2024-02-04T16:13:55.083+00:00",
	"lastLogin": "2024-02-04T16:13:55.083+00:00",
	"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3Vlei5vcmciLCJleHAiOjE3MDcwNjQxMzV9.H9q35FP4G2ynd8rnRAQQ8tXv8Wndwdz4zjcQGBAaxyoRcXeoXWAD2MMOsJZJix0S9om6_d8fWsDQ1-CvNrT3EA",
	"isActive": true
}
```

❌ `400 Bad Request` - La password debe contener, al menos, una letra mayúscula, dos dígitos, letras minúsculas.
```json
{
	"mensaje": "La clave es invalida"
}
```

❌ `400 Bad Request` - El formato del email debe ser simple: "aaaaa1@aaa2.com".
```json
{
	"mensaje": "Correo electronico invalido"
}
```

⚠️ `409 Conflict` - El email ya fue utilizado para el registro de otro usuario.
```json
{
	"mensaje": "El correo ya esta registrado"
}
```


