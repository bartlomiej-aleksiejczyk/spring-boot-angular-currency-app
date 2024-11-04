# Currency App - instrukcja uruchomienia deweloperskiego dla systemu Linux

## Warunki wstępne
1. Zainstalowane JDK w wersji 21
2. Zainstalowane cli dla angulara 18
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
