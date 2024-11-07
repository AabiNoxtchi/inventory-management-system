## Inventory management system ##

This system is designed to 
    - manage inventory internally, for example inventory handed to employees, 
    - track current holder, the status of the inventory, and current value in case of amortization.
    - And inventory deliveries and the status of those deliveries, if all it's delivered items are amortized fully,
    - the system sends notifications to responsible user if they wish to delete the entry.
    - the system has 3 roles, the admin, inventory responsible, and the users of the inventory.
    - the inventory responsible registers their inventory and it's users
    - users can access the system and check what inventory is registered to their accounts


The system is a client server system, consisting of one server : inventory, and two clients, 
    one android for mobile android users : inventory_UI
    and one web client developed in react js framework : test-react-app


The server is designed in a monolith architecture, using JAVA, Spring Boot, hibernate and MySql db technology stack.
    It demonstrates custom filtering on all possible properties, custom pagination, custom annotations, Spring security,
    Jpa repository, repository implementations, entity inheritance schemas and code first approach.