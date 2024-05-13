package com.project.bookstore.config;

public class SwaggerConstants {
    /**
     * Authentication controller annotation
     */
    public static final String REGISTER_USER_SUM
            = "Register a new user";
    public static final String REGISTER_USER_DESC
            = """
            1. **Authorities:** Accessible by any 
            unregistered user.
            2. **Exceptions:** Throws RegistrationException if \
            a user with the same email already exists.
            3. **Field constraints:**\s
            - 'email' must be a valid email format.
            - 'password' and 'repeatPassword' must be at \
            least 8 characters long, not blank, and not null.
            - 'firstName' and 'lastName' must not be blank or null.
            - 'shippingAddress' is optional.
            4. **Other related information:** Upon successful 
            registration, \
            assigns USER_ROLE to the new user. If the new user 
            is the first one \
            to register, assigns ADMIN_ROLE as well.""";
    public static final String LOGIN_SUM
            = "User Login";
    public static final String LOGIN_DESC
            = """
            1. **Authorities:** Accessible by unauthenticated 
            users attempting to log in.
            2. **Exceptions:** Throws BadCredentialsException 
            if credentials are invalid.
            3. **Field constraints:**
            - 'email' must be a valid email format.
            - 'password' must be between 8 and 20 characters 
            in length.
            4. **Other related information:** Upon successful 
            authentication, \
            a JWT token is generated and returned.""";

    /**
     * Book controller annotation
     */
    public static final String FIND_ALL_BOOKS_SUM
            = "Find All Books";
    public static final String FIND_ALL_BOOKS_DESC
            = """
            1. **Authorities:** Accessible by users with 
            'user' authority.
            2. **Exceptions:** -
            3. **Field constraints:** -
            4. **Other related information:** This endpoint 
            retrieves a \
            paginated list of books. Each book includes 
            details such as ID, \
            title, author, ISBN, price, description, 
            cover image, \
            and associated category IDs.""";
    public static final String SEARCH_BOOKS_SUM
            = "Search Books";
    public static final String SEARCH_BOOKS_DESC
            = """
            1. **Authorities:** Accessible by users with 
            'user' authority.
            2. **Exceptions:** Throws IllegalArgumentException 
            if search parameters are invalid.
            3. **Request parameters:** Accepts parameters 
            for book\s
            titles, authors, prices, and ISBNs.
            4. **Request fields constraints:**
            - 'titles' can include one or more book titles 
            for search.
            - 'authors' can include one or more author names 
            for search.
            - 'prices' can include a range of prices (e.g., "10-20") 
            for search.
            - 'isbns' can include one or more ISBNs for search.
            5. **Other related information:** The search 
            is performed\s
            using a combination of provided parameters and supports\s
            pagination.""";
    public static final String FIND_BOOK_BY_ID_SUM
            = "Find Book by ID";
    public static final String FIND_BOOK_BY_ID_DESC
            = """
            1. **Authorities:** Accessible by users with 
            'user' authority.
            2. **Exceptions:** Throws EntityNotFoundException 
            if no book\s
            is found with the provided ID.
            3. **Request parameters:** Requires a 'Long id' 
            path variable to identify the book.
            4. **Request fields constraints:** No specific 
            field constraints as this method is for retrieving data.
            5. **Other related information:** This endpoint retrieves\s
            the details of a book identified by its ID, including\s
            title, author, ISBN, price, description, cover image,\s
            and category IDs.""";
    public static final String SAVE_BOOK_SUM
            = "Save new book to database";
    public static final String SAVE_BOOK_DESC
            = """
            1. **Authorities:** Accessible by users with 'admin' authority.

            2. **Exceptions:** May throw ConstraintViolationException\s
            if validation fails.

            3. **Request parameters:** None.

            4. **Request fields constraints:**
            - 'title': Not null, not blank.
            - 'author': Not null, not blank.
            - 'isbn': Must be a valid ISBN number (example: 
            978-5-04-116685-4).
            - 'price': Not null, must be a non-negative value.
            - 'description': Not null, not blank.
            - 'coverImage': Must be a valid URL format (example: 
            https://safe_net.jpg).
            - 'categoryIds': Not null, must contain valid category 
            IDs.

            5. **Other related information:** The 
            endpoint consumes\s
            a JSON payload representing a book and 
            persists it to\s
            the database. Returns the saved book's 
            data transfer\s
            object (DTO).""";
    public static final String SAVE_ALL_BOOKS_SUM
            = "Save array of books to database";
    public static final String SAVE_ALL_BOOKS_DESC
            = """
            1. **Authorities:** Accessible by users with 
            'admin' authority.
            2. **Exceptions:** May throw 
            ConstraintViolationException\s
            if validation fails.
            3. **Request parameters:** None.
            4. **Request fields constraints:**
            - 'title': Not null, not blank.
            - 'author': Not null, not blank.
            - 'isbn': Must be a valid ISBN number (example: 
            978-5-04-116685-4).
            - 'price': Not null, must be a non-negative value.
            - 'description': Not null, not blank.
            - 'coverImage': Must be a valid URL format (example: 
            https://safe_net.jpg).
            - 'categoryIds': Not null, must contain 
            valid category IDs.
            5. **Other related information:** The 
            endpoint accepts\s
            an array of JSON payloads, each representing 
            a book.\s
            It persists all provided books to the 
            database and\s
            returns a list of BookDto objects.""";
    public static final String UPDATE_BOOK_BY_ID_SUM
            = "Update book details by ID";
    public static final String UPDATE_BOOK_BY_ID_DESC
            = """
            1. **Authorities:** Accessible by users with 
            'admin' authority.
            2. **Exceptions:** May throw EntityNotFoundException\s
            if no book with the given ID exists.
            3. **Request parameters:** 'id' - The ID of the 
            book to update.

            4. **Request fields constraints:**
            - 'title': Not null, not blank.
            - 'author': Not null, not blank.
            - 'isbn': Must be a valid ISBN number (example: 
            978-5-04-116685-4).
            - 'price': Not null, must be a non-negative value.
            - 'description': Not null, not blank.
            - 'coverImage': Must be a valid URL format (example: 
            https://safe_net.jpg).
            - 'categoryIds': Not null, must contain valid category IDs.

            5. **Other related information:** The endpoint updates\s
            the book details for the given ID with the provided\s
            request data and returns the updated BookDto object.""";
    public static final String DELETE_BOOK_BY_ID_SUM
            = "Delete book by ID";
    public static final String DELETE_BOOK_BY_ID_DESC
            = """
            1. **Authorities:** Accessible by users with 'admin' 
            authority.
            2. **Exceptions:** May throw EntityNotFoundException 
            if no book with the given ID exists.
            3. **Request parameters:** 'id' - The ID of the book 
            to delete.
            4. **Request fields constraints:** Not applicable for 
            this endpoint.
            5. **Other related information:** The endpoint deletes 
            the book corresponding \
            to the provided ID from the database. If successful, 
            it returns a 204 No Content response.""";

    /**
     * Category controller annotation
     */
    public static final String SAVE_CATEGORY_SUM
            = "Save a new category";
    public static final String SAVE_CATEGORY_DESC = """
            1. **Possible response codes and messages:**
            - `200 OK`: Category successfully created
            - `400 Bad Request`: Validation error or missing 
            request body
            - `403 Forbidden`: User not authorized
            2. **Authorities that have access to the endpoint:**
            - `admin`
            3. **Exceptions that can be thrown:**
            4. **Request fields constraints (according to 
            validation):**
            - `name`: must not be blank, size must be 
            between 1 and 255
            - `description`: size must be between 0 and 255
            5. **Other related information:**
            - Only users with 'admin' authority can create 
            new categories.""";
    public static final String SAVE_ALL_CATEGORIES_SUM
            = "Save all categories";
    public static final String SAVE_ALL_CATEGORIES_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful creation of categories
            - 401 Unauthorized: Authentication is required 
            and has failed or has not yet been provided
            - 403 Forbidden: The user does not have the 
            necessary permissions for the resource
            2. **Authorities that have access to the endpoint:**
            - `admin`
            3. **Exceptions that can be thrown:**
            4. **Request fields constraints (according 
            to validation):**
            - name: must not be blank, size must be 
            between 3 and 50 characters
            - description: size must be between 10 
            and 200 characters
            5. **Other related information:**
            - This operation is transactional and 
            will either complete fully or roll back 
            completely.""";
    public static final String FIND_ALL_CATEGORIES_SUM
            = "Retrieve all categories";
    public static final String FIND_ALL_CATEGORIES_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful retrieval of categories
            - 401 Unauthorized: Authentication is required 
            and has failed or has not yet been provided
            - 403 Forbidden: The user does not have the 
            necessary permissions for the resource
            - 404 Not Found: No categories found
            2. **Authorities that have access to the endpoint:**
            - `user`
            3. **Exceptions that can be thrown:**
            4. **Request fields constraints (according 
            to validation):**
            - pageable: must not be null, must contain 
            valid pagination information
            5. **Other related information:**
            - The endpoint supports sorting and 
            pagination for efficient data retrieval.""";
    public static final String FIND_CATEGORY_BY_ID_SUM
            = "Find category by ID";
    public static final String FIND_CATEGORY_BY_ID_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful retrieval of the category
            - 401 Unauthorized: Authentication is required 
            and has failed or has not yet been provided
            - 403 Forbidden: The user does not have the 
            necessary permissions for the resource
            - 404 Not Found: No category found with the 
            provided ID
            2. **Authorities that have access to the endpoint:**
            - `user`
            3. **Exceptions that can be thrown:**
            - EntityNotFoundException: Can't find entity 
            by the provided ID
            4. **Request fields constraints (according 
            to validation):**
            - id: must be a positive long value
            5. **Other related information:**
            - The endpoint retrieves a single category based 
            on the unique ID provided.""";
    public static final String UPDATE_CATEGORY_BY_ID_SUM
            = "Update category by ID";
    public static final String UPDATE_CATEGORY_BY_ID_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful update of the category
            - 401 Unauthorized: Authentication is required 
            and has failed or has not yet been provided
            - 403 Forbidden: The user does not have the 
            necessary permissions for the resource
            - 404 Not Found: No category found with the 
            provided ID
            2. **Authorities that have access to the 
            endpoint:**
            - `admin`
            3. **Exceptions that can be thrown:**
            - EntityNotFoundException: Can't find 
            entity by the provided ID
            4. **Request fields constraints (according 
            to validation):**
            - name: must not be blank, size must be between 
            3 and 50 characters
            - description: size must be between 10 and 
            200 characters
            5. **Other related information:**
            - The endpoint allows updating a category's 
            name and description by its ID.""";
    public static final String FIND_ALL_BOOKS_BY_CATEGORY_ID_SUM
            = "Retrieve all books by category ID";
    public static final String FIND_ALL_BOOKS_BY_CATEGORY_ID_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful retrieval of books
            - 401 Unauthorized: Authentication is required 
            and has failed or has not yet been provided
            - 403 Forbidden: The user does not have the 
            necessary permissions for the resource
            2. **Authorities that have access to the endpoint:**
            - `user`
            3. **Exceptions that can be thrown:**
            4. **Request fields constraints (according 
            to validation):**
            - id: must be a positive long value
            - pageable: must not be null, must contain 
            valid pagination information
            5. **Other related information:**
            - The endpoint supports sorting and pagination 
            for efficient data retrieval of books within 
            a specific category.""";
    public static final String DELETE_CATEGORY_BY_ID_SUM
            = "Delete category by ID";
    public static final String DELETE_CATEGORY_BY_ID_DESC = """
            1. **Possible response codes and messages:**
            - 204 No Content: Successful deletion of the 
            category
            - 401 Unauthorized: Authentication is required 
            and has failed or has not yet been provided
            - 403 Forbidden: The user does not have the 
            necessary permissions for the resource
            - 404 Not Found: No category found with the 
            provided ID
            2. **Authorities that have access to the 
            endpoint:**
            - `admin`
            3. **Exceptions that can be thrown:**
            4. **Request fields constraints (according 
            to validation):**
            - id: must be a positive long value
            5. **Other related information:**
            - The endpoint will delete the category 
            if it exists and the user has the required 
            authority.""";

    /**
     * Order controller annotation
     */
    public static final String FIND_ALL_ORDERS_SUM
            = "Retrieve all orders for a user";
    public static final String FIND_ALL_ORDERS_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful retrieval of orders
            - 401 Unauthorized: Authentication is required 
            and has failed or has not yet been provided
            - 403 Forbidden: The user does not have the 
            necessary permissions for the resource
            - 404 Not Found: No orders found for the user
            2. **Authorities that have access to the endpoint:**
            - `user`
            3. **Exceptions that can be thrown:**
            4. **Request fields constraints (according to validation):**
            - username: must be a valid email format
            - pageable: must not be null, must contain valid 
            pagination information
            5. **Other related information:**
            - The endpoint supports sorting and 
            pagination for efficient data retrieval 
            of orders for the authenticated user.""";
    public static final String SAVE_ORDER_SUM
            = "Create a new order";
    public static final String SAVE_ORDER_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful creation of the order
            - 401 Unauthorized: Authentication is 
            required and has failed or has not yet 
            been provided
            - 403 Forbidden: The user does not have 
            the necessary permissions for the resource
            2. **Authorities that have access to the 
            endpoint:**
            - `user`
            3. **Exceptions that can be thrown:**
            - EntityNotFoundException: No user found 
            with the provided email
            4. **Request fields constraints (according 
            to validation):**
            - shippingAddress: must not be blank
            5. **Other related information:**
            - The endpoint creates a new order based 
            on the user's shopping cart and provided 
            shipping address.""";
    public static final String GET_ORDER_ITEMS_SUM
            = "Retrieve order items by order ID";
    public static final String GET_ORDER_ITEMS_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful retrieval of order items
            - 401 Unauthorized: Authentication is 
            required and has failed or has not yet 
            been provided
            - 403 Forbidden: The user does not have 
            the necessary permissions for the resource
            - 404 Not Found: No order found with the 
            provided ID or the order does not belong 
            to the user
            2. **Authorities that have access to 
            the endpoint:**
            - `user`
            3. **Exceptions that can be thrown:**
            - EntityNotFoundException: No order 
            found with the provided ID
            - AuthorizationException: Access denied 
            for viewing another user's orders
            4. **Request fields constraints 
            (according to validation):**
            - orderId: must be a positive long value
            5. **Other related information:**
            - The endpoint retrieves order items 
            associated with the provided order ID.""";
    public static final String FIND_ORDER_ITEM_BY_ID_SUM
            = "Find a specific order item by ID";
    public static final String FIND_ORDER_ITEM_BY_ID_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful retrieval of the order item
            - 401 Unauthorized: Authentication is 
            required and has failed or has not yet 
            been provided
            - 403 Forbidden: Access denied for 
            viewing another user's order items
            - 404 Not Found: No order or order 
            item found with the provided IDs
            2. **Authorities that have access 
            to the endpoint:**
            - `user`
            3. **Exceptions that can be thrown:**
            - EntityNotFoundException: Can't find 
            order by the provided ID
            - AuthorizationException: Access 
            denied for viewing another user's orders
            - IllegalArgumentException: Invalid 
            orderItem ID provided
            4. **Request fields constraints 
            (according to validation):**
            - orderId: must be a positive long value
            - itemId: must be a positive long value
            5. **Other related information:**
            - The endpoint retrieves a specific 
            order item associated with the 
            provided order ID and item ID.""";
    public static final String UPDATE_ORDER_STATUS_SUM
            = "Update order status by ID";
    public static final String UPDATE_ORDER_STATUS_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful update of the order status
            - 401 Unauthorized: Authentication 
            is required and has failed or has 
            not yet been provided
            - 403 Forbidden: The user does not 
            have the necessary permissions for 
            the resource
            - 404 Not Found: No order found with 
            the provided ID
            2. **Authorities that have access to 
            the endpoint:**
            - `admin`
            3. **Exceptions that can be thrown:**
            - EntityNotFoundException: Can't find 
            order by the provided ID
            - IllegalArgumentException: Invalid 
            status name provided
            4. **Request fields constraints 
            (according to validation):**
            - status: must be a valid status name 
            (e.g., NEW, PROCESSING, SHIPPED, DELIVERED)
            5. **Other related information:**
            - The endpoint allows admins to update 
            the status of an order by its ID.""";

    /**
     * Role controller annotation
     */
    public static final String FIND_ALL_ROLES_SUM
            = "Retrieve all roles";
    public static final String FIND_ALL_ROLES_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful retrieval of roles
            - 401 Unauthorized: Authentication is 
            required and has failed or has not 
            yet been provided
            - 403 Forbidden: The user does not 
            have the necessary permissions for 
            the resource
            2. **Authorities that have access to 
            the endpoint:**
            - `admin`
            3. **Exceptions that can be thrown:**
            4. **Request fields constraints 
            (according to validation):**
            - No constraints, as this is a simple 
            GET request without a request body
            5. **Other related information:**
            - This endpoint retrieves all roles 
            available in the system.""";

    /**
     * Shopping cart controller annotation
     */
    public static final String GET_CART_SUM
            = "Retrieve user's shopping cart";
    public static final String GET_CART_DESC = """
            1. **Possible response codes and 
            messages:**
            - 200 OK: Successful retrieval of 
            the user's shopping cart
            - 401 Unauthorized: Authentication 
            is required and has failed or has 
            not yet been provided
            - 403 Forbidden: The user does 
            not have the necessary permissions 
            for the resource
            - 404 Not Found: No shopping cart 
            found for the user
            2. **Authorities that have access 
            to the endpoint:**
            - `user`
            3. **Exceptions that can be thrown:**
            - EntityNotFoundException: No user 
            found with the provided email
            4. **Request fields constraints 
            (according to validation):**
            - No constraints, as this is a 
            simple GET request without a 
            request body
            5. **Other related information:**
            - The endpoint retrieves the user's 
            shopping cart, including cart items.""";
    public static final String POST_CART_ITEM_SUM
            = "Add an item to the shopping cart";
    public static final String POST_CART_ITEM_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful addition of the 
            item to the shopping cart
            - 401 Unauthorized: Authentication 
            is required and has failed or has 
            not yet been provided
            - 403 Forbidden: The user does not 
            have the necessary permissions 
            for the resource
            2. **Authorities that have access 
            to the endpoint:**
            - `user`
            3. **Exceptions that can be thrown:**
            4. **Request fields constraints 
            (according to validation):**
            - bookId: must be a positive long value
            - quantity: must be a positive integer
            5. **Other related information:**
            - The endpoint allows users to add 
            an item (book) to their shopping cart.""";
    public static final String UPDATE_CART_ITEM_SUM
            = "Update quantity of a cart item";
    public static final String UPDATE_CART_ITEM_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful update of the 
            cart item quantity
            - 401 Unauthorized: Authentication 
            is required and has failed or 
            has not yet been provided
            - 403 Forbidden: Access denied. 
            You can't edit shopping cart of another user
            - 404 Not Found: Can't find cart 
            item by the provided ID
            2. **Authorities that have 
            access to the endpoint:**
            - USER
            3. **Exceptions that can be thrown:**
            - EntityNotFoundException: 
            Can't find cart item by the provided ID
            - AuthorizationException: Access 
            denied. You can't edit shopping 
            cart of another user
            4. **Request fields constraints 
            (according to validation):**
            - quantity: must be a non-negative integer
            5. **Other related information:**
            - The endpoint allows users to 
            update the quantity of an existing 
            cart item.""";
    public static final String DELETE_CART_ITEM_SUM
            = "Delete a cart item";
    public static final String DELETE_CART_ITEM_DESC = """
            1. **Possible response codes and messages:**
            - 204 No Content: Successful deletion 
            of the cart item
            - 401 Unauthorized: Authentication 
            is required and 
            has failed or has not yet been 
            provided
            - 403 Forbidden: Access denied. 
            You can't edit shopping 
            cart of another user
            - 404 Not Found: Can't find cart 
            item by the provided ID
            2. **Authorities that have access 
            to the endpoint:**
            - USER
            3. **Exceptions that can be thrown:**
            - EntityNotFoundException: Can't 
            find cart item by the provided ID
            - AuthorizationException: Access 
            denied. You can't edit 
            shopping cart of another user
            4. **Request fields constraints 
            (according to validation):**
            - No constraints, as this is a 
            simple DELETE request 
            without a request body
            5. **Other related information:**
            - The endpoint allows users to 
            delete an item from 
            their shopping cart.""";

    /**
     * User controller annotation
     */
    public static final String UPDATE_USER_ROLES_SUM = "Update user roles";
    public static final String UPDATE_USER_ROLES_DESC = """
            1. **Possible response codes and messages:**
            - 200 OK: Successful update of user roles
            - 401 Unauthorized: Authentication is required and has 
            failed or has not yet been provided
            - 403 Forbidden: The user does not have the necessary 
            permissions for the resource
            - 404 Not Found: Can't find user by the provided ID
            2. **Authorities that have access to the endpoint:**
            - ADMIN
            3. **Exceptions that can be thrown:**
            - EntityNotFoundException: Can't find user by the provided ID
            4. **Request fields constraints (according to validation):**
            - id: must be a positive long value
            - roleIds: must be a list of positive long values
            5. **Other related information:**
            - The endpoint allows admins to update the roles assigned to a user.""";

}
