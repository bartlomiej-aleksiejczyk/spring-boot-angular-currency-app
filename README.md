# Currency App - instrukcja uruchomienia deweloperskiego dla systemu Linux

## Wersja live na AWS

* Dokumentacja API w formie OpenAPI pod linkiem:
``` 
http://currency-info-app.eu-north-1.elasticbeanstalk.com/swagger-ui/index.html
```
* Frontend aplikacji (skomunikowany z backendem):
```
http://currency-info-app.s3-website.eu-north-1.amazonaws.com/
```

## Warunki wstępne
1. Zainstalowane JDK w wersji 21
2. Zainstalowane cli dla angulara 17.3.3
3. Odpowiednio skonfigurowane środowisko we front-endzie w pliku "frontend/src/environment/environment.ts"

## Uruchomienie
### 1. Serwer API
Należy przejść do folderu "backend" w projekcie i użyć komendy:
```
./gradlew bootRun
```
backend będzie dostępny w lokacji `http://localhost:8080/`
### 2. Frontend
Należy przejść do folderu "frontend" w projekcie i użyć komendy:
```
ng serve
```
frontend będzie dostępny w lokacji `http://localhost:4200/`

## Testowanie
### 1. Serwer API
Należy przejść do folderu "backend" w projekcie i użyć komendy:
```
./gradlew test
```
### 2. Frontend
Należy przejść do folderu "frontend" w projekcie i użyć komendy:
```
ng test
```

