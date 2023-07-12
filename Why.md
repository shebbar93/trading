| Criteria                      | Strategy Design Pattern                   | Interpreter Pattern                 |
| ----------------------------- | ----------------------------------------- | ----------------------------------- |
| Scalability and Maintainability | 🟥 High complexity with growth. Redeployment required for new signals. | 🟩 Scripts can be added or updated at runtime without redeployment. |
| Flexibility                   | 🟥 Limited. Code changes and redeployment required for changes. | 🟩 High. Scripts can be dynamically updated at runtime. |
| Security                      | 🟩 Code is statically typed and checked at compile time. | 🟥 Running dynamically loaded code can have security implications if not properly sandboxed. |
| Testing                       | 🟩 Straightforward to write unit tests for classes implementing specific interface. | 🟥 More complex, might require executing the scripts and checking their effects. |


``` bash 
    With up to 50 new signals being added each month, 
    resulting in a total of 600 in a year, 
    and an increase to 600 signals per month after a year, 
    resulting in a total of 7200 signals in the following year, 
    the Interpreter Pattern (scriptable approach) would be a more suitable solution as the 
    numbers continue to grow thereafter.
```