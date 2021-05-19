# Bank API

## Card API
```
GET
api/card/all
api/card
api/card/balance


POST
api/card
api/card/deposit
api/card/activate
api/card/close
```

```
api/card/deposit
curl -X POST -H "Content-Type: application/json" http://localhost:8080/api/card/deposit -d '{"number":"4000002698974233", "amount":"10.10"}'
```

```
api/card/balance
curl -X POST -H "Content-Type: application/json" http://localhost:8080/api/card/balance -d '{"number":"4000002698974233"}'
```