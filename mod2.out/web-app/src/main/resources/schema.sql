
DROP TABLE IF EXISTS "products" CASCADE;
DROP TABLE IF EXISTS "orders" CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS "transactions" CASCADE;
DROP TABLE IF EXISTS "user_demand" CASCADE;
DROP TABLE IF EXISTS "usercart" CASCADE;

CREATE TABLE IF NOT EXISTS "products" (
  "prod_id" VARCHAR(45) NOT NULL,
  "pname" VARCHAR(100) NULL DEFAULT NULL,
  "ptype" VARCHAR(20) NULL DEFAULT NULL,
  "prod_info" VARCHAR(350) NULL DEFAULT NULL,
  "pprice" NUMERIC(12,2) NULL DEFAULT NULL,
  "pquantity" INT NULL DEFAULT NULL,
  "prod_image" BYTEA NULL DEFAULT NULL,
  PRIMARY KEY ("prod_id")
);

CREATE TABLE IF NOT EXISTS "orders" (
  "orderid" VARCHAR(45) NOT NULL,
  "prodid" VARCHAR(45) NOT NULL,
  "quantity" INT NULL DEFAULT NULL,
  "amount" NUMERIC(10,2) NULL DEFAULT NULL,
  "shipped" INT NOT NULL DEFAULT 0,
  PRIMARY KEY ("orderid", "prodid"),
  CONSTRAINT "productid"
    FOREIGN KEY ("prodid")
    REFERENCES "products" ("prod_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS "users" (
  "email" VARCHAR(60) NOT NULL,
  "name" VARCHAR(30) NULL DEFAULT NULL,
  "mobile" VARCHAR(20) NULL DEFAULT NULL,
  "address" VARCHAR(250) NULL DEFAULT NULL,
  "pincode" VARCHAR(255) NULL DEFAULT NULL,
  "password" VARCHAR(255) NULL DEFAULT NULL,
  "role" VARCHAR(20) NULL DEFAULT NULL,
  PRIMARY KEY ("email")
);

CREATE TABLE IF NOT EXISTS "transactions" (
  "transid" VARCHAR(45) NOT NULL,
  "username" VARCHAR(60) NULL DEFAULT NULL,
  "time" TIMESTAMP NULL DEFAULT NULL,
  "amount" NUMERIC(10,2) NULL DEFAULT NULL,
  PRIMARY KEY ("transid"),
  CONSTRAINT "truserid"
    FOREIGN KEY ("username")
    REFERENCES "users" ("email")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS "user_demand" (
  "username" VARCHAR(60) NOT NULL,
  "prodid" VARCHAR(45) NOT NULL,
  "quantity" INT NULL DEFAULT NULL,
  PRIMARY KEY ("username", "prodid"),
  CONSTRAINT "userdemailemail"
    FOREIGN KEY ("username")
    REFERENCES "users" ("email")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT "prodid"
    FOREIGN KEY ("prodid")
    REFERENCES "products" ("prod_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS "usercart" (
  "username" VARCHAR(60) NULL DEFAULT NULL,
  "prodid" VARCHAR(45) NULL DEFAULT NULL,
  "quantity" INT NULL DEFAULT NULL,
  CONSTRAINT "useremail"
    FOREIGN KEY ("username")
    REFERENCES "users" ("email")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT "prodidcart"
    FOREIGN KEY ("prodid")
    REFERENCES "products" ("prod_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
