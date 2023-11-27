# bank exersice

## Assumptions:
- For simplicity, the account number will be the db id, in the real world that is not secure.
- In the exercise there is one example with GBP and one with EUR, which means that the bank accepts transfers for more than one currency, for simplicity if the user does not have a wallet for this currency then the transaction will be declined. 
An alternative solution could be a new wallet(currency) to be created.
Or the system to convert the currency to match the target account.
- In the real world, banking systems use event sourcing for auditing purposes, but for simplicity I will not implement an event sourcing approach for this exercise.
- There is no need for Users service or auth, as in the real world there will be another microservice which will be responsible for the Users/Accounts relationship
- Using MySQL is important to annotate methods with Transactional and to make sure that the debit and credit requests are under the same transaction. But for the exercise I used a Map as an in memory db.
- Create account and all other account facade methods are on purpose not exposed as there was no requirement for that.
- I did not log anything on purpose, as it is a financial service and logs can not contain details about customer data. Logs will be helpful if there were more interactions with external services though.


## Next steps
- Acceptance criteria tests can run without the spring context. 

## Technical notes
- swagger documentation for the exposed apis http://localhost:8080/swagger-ui/index.html