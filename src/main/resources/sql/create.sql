-- Création du schéma
CREATE SCHEMA inventory_service_schema;

-- Table des produits (products)
CREATE TABLE inventory_service_schema.products (
                                                   id SERIAL PRIMARY KEY,
                                                   code VARCHAR(255) NOT NULL UNIQUE,
                                                   name VARCHAR(255) NOT NULL,
                                                   description TEXT,
                                                   tenant_code VARCHAR(255) NOT NULL,
                                                   category_id INT NOT NULL,
                                                   unit_of_measure VARCHAR(50) NOT NULL,  -- Ex: litre, kg
                                                   volume DOUBLE PRECISION NOT NULL,      -- Volume par unité (ex: 1,5 L par bouteille)
                                                   created_at TIMESTAMP NOT NULL,
                                                   updated_at TIMESTAMP
);

-- Table des articles (articles)
CREATE TABLE inventory_service_schema.articles (
                                                   id SERIAL PRIMARY KEY,
                                                   product_id INT NOT NULL REFERENCES inventory_service_schema.products(id),
                                                   total_volume DOUBLE PRECISION NOT NULL,  -- Volume total (ex: 1,5 * 5 = 7,5 L)
                                                   created_at TIMESTAMP NOT NULL,
                                                   updated_at TIMESTAMP
);

-- Table des catégories (categories)
CREATE TABLE inventory_service_schema.categories (
                                                     id SERIAL PRIMARY KEY,
                                                     code VARCHAR(255) NOT NULL UNIQUE,
                                                     name VARCHAR(100) NOT NULL,
                                                     description TEXT,
                                                     created_at TIMESTAMP NOT NULL,
                                                     updated_at TIMESTAMP
);

-- Table des mouvements de stock (stock_movements)
CREATE TABLE inventory_service_schema.stock_movements (
                                                          id SERIAL PRIMARY KEY,
                                                          article_id INT NOT NULL REFERENCES inventory_service_schema.articles(id),
                                                          movement_type VARCHAR(50) NOT NULL,   -- ENTREE ou SORTIE
                                                          quantity_moved DOUBLE PRECISION NOT NULL,  -- Quantité déplacée
                                                          movement_date TIMESTAMP NOT NULL,
                                                          created_at TIMESTAMP NOT NULL,
                                                          updated_at TIMESTAMP
);

-- Table des logs d'audit de stock (stock_audit_logs)
CREATE TABLE inventory_service_schema.stock_audit_logs (
                                                           id SERIAL PRIMARY KEY,
                                                           stock_movement_id INT NOT NULL REFERENCES inventory_service_schema.stock_movements(id),
                                                           employee_number VARCHAR(100) NOT NULL,  -- Numéro de l'employé, récupéré via user-service
                                                           action_type VARCHAR(50) NOT NULL,       -- Type d'action (ex: CREATE, UPDATE, DELETE)
                                                           description TEXT,
                                                           created_at TIMESTAMP NOT NULL
);
