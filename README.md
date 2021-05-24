# 💵 Bank-api

## 🍧 Features
On card creation return `201` status code  
If the card isn't found - `404`  
`400` On bad request  
If card is closed you can't update it. Even it's status  
If card status is `PENDING` (`1`) it updates to `ACTIVE` (`2`) with first deposit  
Updates account balance when card balance is updated  

## 💳 Card API
```
GET

api/card/all

api/card/all/status

api/card?number=4000000967827322

api/card/balance?number=4000000967827322

api/card/status?number=4000000967827322
```
```
POST

api/card
{"account":"22222222222222222222"}

api/card/deposit
{"number":"4000000967827322","amount":15.30}

api/card/status
{"number":"4000000967827322","status":2}

api/card/activate
{"number":"4000000967827322"}

api/card/close
{"number":"4000000967827322"}

api/card/delete
{"number":"4000000967827322"}
```

## 🧮 Account API
```
GET

api/account/all
api/account?id=2
```
```
POST

api/account
{"phone":"+75557774430"}
```

Author: Roman Kukin
