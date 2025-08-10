
# Database Schema

This document outlines the database schema for the `shopping-cart` application, as inferred from the `mysql_query.sql` file.

## Tables

### `product`

Stores product information.

| Column      | Type          | Description                   |
|-------------|---------------|-------------------------------|
| `pid`       | VARCHAR(45)   | Primary Key, Product ID       |
| `pname`     | VARCHAR(100)  | Product Name                  |
| `ptype`     | VARCHAR(20)   | Product Type (e.g., 'mobile') |
| `pinfo`     | VARCHAR(350)  | Product Information           |
| `pprice`    | DECIMAL(12,2) | Product Price                 |
| `pquantity` | INT           | Product Quantity in stock     |
| `image`     | LONGBLOB      | Product Image                 |

### `orders`

Stores information about items in an order.

| Column    | Type         | Description                                         |
|-----------|--------------|-----------------------------------------------------|
| `orderid` | VARCHAR(45)  | Part of the Primary Key, Foreign Key to `transactions` |
| `prodid`  | VARCHAR(45)  | Part of the Primary Key, Foreign Key to `product`      |
| `quantity`| INT          | Quantity of the product in the order                |
| `amount`  | DECIMAL(10,2)| Amount for this item in the order                   |
| `shipped` | INT          | Shipping status (0 for not shipped, 1 for shipped)  |

### `user`

Stores user information.

| Column    | Type         | Description      |
|-----------|--------------|------------------|
| `email`   | VARCHAR(60)  | Primary Key, User's email |
| `name`    | VARCHAR(30)  | User's name      |
| `mobile`  | BIGINT       | User's mobile number |
| `address` | VARCHAR(250) | User's address   |
| `pincode` | INT          | Pincode of the user's address |
| `password`| VARCHAR(20)  | User's password  |

### `transactions`

Stores transaction information.

| Column   | Type         | Description                        |
|----------|--------------|------------------------------------|
| `transid`| VARCHAR(45)  | Primary Key, Transaction ID        |
| `username`| VARCHAR(60)  | Foreign Key to `user`              |
| `time`   | DATETIME     | Time of the transaction            |
| `amount` | DECIMAL(10,2)| Total amount of the transaction    |

### `user_demand`

Stores information about products demanded by users that are out of stock.

| Column   | Type        | Description                     |
|----------|-------------|---------------------------------|
| `username`| VARCHAR(60) | Part of the Primary Key, Foreign Key to `user` |
| `prodid` | VARCHAR(45) | Part of the Primary Key, Foreign Key to `product` |
| `quantity`| INT         | Quantity demanded by the user   |

### `usercart`

Stores items in a user's shopping cart.

| Column   | Type        | Description                     |
|----------|-------------|---------------------------------|
| `username`| VARCHAR(60) | Foreign Key to `user`           |
| `prodid` | VARCHAR(45) | Foreign Key to `product`          |
| `quantity`| INT         | Quantity of the product in the cart |

