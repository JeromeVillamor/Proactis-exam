# Product Management Application

## 1. Overall Design Approach
This application follows a clean and straightforward MVC architecture with a service layer. My development philosophy prioritizes simplicity above all else. I believe that simple, readable code is maintainable code, and maintainable code is the foundation of any successful application.

## 2. Key Assumptions Made During Implementation
I added a bulk product creation endpoint (`POST /store/{storeName}/products`) based on real-world e-commerce experience. This feature is commonly needed when merchants need to upload multiple products at once, making it a practical addition to the core functionality.

## 3. Design Alternatives Considered
I deliberately chose not to over-engineer this solution. While there are many architectural patterns available (microservices, event driven), the current MVC approach with service layers provides the right balance of structure.

## 4. Testing Strategy and Reasoning
My testing approach follows a layered strategy:
- **Unit tests** for services and controllers to verify business logic and API behavior
- **Integration / Repository tests** to ensure the composite unique constraint works correctly

## 5. Technologies and Libraries Used
- **Spring Boot Framework** 
- **Spring Data JPA**
- **MySQL**
- **Lombok** - Reduces boilerplate code
- **H2 Database** - Lightweight in-memory database used for integration testing
- **Faker** - For generation of test data

Each technology choice supports the core principle of keeping the solution simple.