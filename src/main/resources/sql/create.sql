-- Création du schéma
CREATE SCHEMA inventory_service_schema;

CREATE TABLE inventory_service_schema.unit_of_measure (
                                                          id SERIAL PRIMARY KEY,
                                                          name VARCHAR(50) NOT NULL UNIQUE  -- Ex: litre, kg, unit
);

-- Table des catégories (categories)
CREATE TABLE inventory_service_schema.categories (
                                                     id SERIAL PRIMARY KEY,
                                                     name VARCHAR(100) NOT NULL UNIQUE,
                                                     description TEXT,
                                                     created_at TIMESTAMP NOT NULL,
                                                     updated_at TIMESTAMP
);
-- Table des produits (products)
CREATE TABLE inventory_service_schema.products (
                                                   id SERIAL PRIMARY KEY,
                                                   code VARCHAR(255) NOT NULL UNIQUE,
                                                   name VARCHAR(255) NOT NULL,
                                                   description TEXT,
                                                   tenant_code VARCHAR(255) NOT NULL,
                                                   category_id INT NOT NULL,
                                                   unit_of_measure_id INT NOT NULL,  -- Foreign key to the unit_of_measure table
                                                   volume DOUBLE PRECISION NOT NULL, -- Volume per unit (e.g., 1.5 L per bottle)
                                                   created_at TIMESTAMP NOT NULL,
                                                   updated_at TIMESTAMP,
                                                   CONSTRAINT unique_product_name_per_tenant UNIQUE (name, tenant_code),
                                                   FOREIGN KEY (category_id) REFERENCES inventory_service_schema.categories (id),
                                                   FOREIGN KEY (unit_of_measure_id) REFERENCES inventory_service_schema.unit_of_measure (id)
);



-- Table des articles (articles)
CREATE TABLE inventory_service_schema.articles (
                                                   id SERIAL PRIMARY KEY,
                                                   product_id INT NOT NULL REFERENCES inventory_service_schema.products(id),
                                                   quantity DOUBLE PRECISION NOT NULL,  -- Volume total (ex: 1,5 * 5 = 7,5 L)
                                                   created_at TIMESTAMP NOT NULL,
                                                   updated_at TIMESTAMP
);



-- Table des mouvements de stock (stock_movements)
CREATE TABLE inventory_service_schema.stock_movements (
                                                          id SERIAL PRIMARY KEY,
                                                          employee_number VARCHAR(100) NOT NULL,  -- Numéro de l'employé, récupéré via user-service
                                                          observation TEXT,
                                                          article_id INT NOT NULL REFERENCES inventory_service_schema.articles(id),
                                                          movement_type VARCHAR(50) NOT NULL,   -- ENTREE ou SORTIE
                                                          old_quantity DOUBLE PRECISION NOT NULL,  --
                                                          quantity_moved DOUBLE PRECISION NOT NULL,  --
                                                          new_quantity DOUBLE PRECISION NOT NULL,  --
                                                          unit_of_measure VARCHAR(50) NOT NULL,
                                                          movement_date TIMESTAMP NOT NULL,
                                                          updated_at TIMESTAMP
);
