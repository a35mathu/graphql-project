# graphql-project
Some queries to try out in the browser!
- http://localhost:8080/graphql?query=%7Bpatients(id%3A%221%22)%7B%20firstName%20lastName%20age%20nurse%7BfirstName%20lastName%7D%7D%7D

This query in graphql actually looks like:
```
patients(id:["1"]) {
    firstName
    lastName
    age
    nurse {
        firstName
        lastName
    }
}
```
- http://localhost:8080/graphql?query=%7Bpatients(id%3A%221%22)%7BfirstName%20lastName%20age%7D%7D

This query in graphql actually looks like:
```
patients(id:["1"]) {
    firstName
    lastName
    age
}
```
- http://localhost:8080/graphql?query=%7Bpatients(id%3A[%223%22,%221%22,%222%22,%224%22])%7Bid%20firstName%20nurse%7BfirstName%20patients%7Bid%20firstName%20lastName%7D%7D%7D%7D

This query in graphql actually looks like:
```
patients(id: ["3", "1", "2", "4"]){
    id
    firstName
    nurse {
        firstName
        patients {
            id
            firstName
        }
    }
}
```
