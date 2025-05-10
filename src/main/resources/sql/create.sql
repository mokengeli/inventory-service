/* =============================================================
   SCHÉMA
   =============================================================*/
CREATE SCHEMA IF NOT EXISTS inventory_schema;
SET search_path TO inventory_schema;

/* =============================================================
   TABLES DE RÉFÉRENCE (lookup)
   =============================================================*/
CREATE TABLE units_of_measure (
                                 id          BIGSERIAL       PRIMARY KEY,
                                 name        TEXT            NOT NULL UNIQUE,
                                 created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                                 updated_at  TIMESTAMP WITH TIME ZONE
);

CREATE TABLE categories (
                            id          BIGSERIAL       PRIMARY KEY,
                            name        TEXT            NOT NULL UNIQUE,
                            description TEXT,
                            created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                            updated_at  TIMESTAMP WITH TIME ZONE
);

/* =============================================================
   PRODUITS  (multitenant)
   =============================================================*/
CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          code VARCHAR(255) NOT NULL UNIQUE,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          tenant_code VARCHAR(255) NOT NULL,
                          category_id BIGINT NOT NULL,
                          unit_of_measure_id INT NOT NULL,  -- Foreign key to the unit_of_measure table
                          volume  NUMERIC(14,3) NOT NULL, -- Volume per unit (e.g., 1.5 L per bottle)
                          created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                          updated_at TIMESTAMP WITH TIME ZONE,
                          CONSTRAINT unique_product_name_per_tenant UNIQUE (name, tenant_code)
);

/* Index couvrant la pagination par tenant */
CREATE INDEX idx_products_tenant_id
    ON products (tenant_code, id);

/* Recherche rapide par code */
CREATE UNIQUE INDEX idx_products_code
    ON products (code);

/* =============================================================
   ARTICLES  (1 : 1 avec product)
   =============================================================*/
CREATE TABLE articles (
                          id          BIGSERIAL   PRIMARY KEY,
                          product_id  BIGINT      NOT NULL UNIQUE
                              REFERENCES products(id)
                                  ON UPDATE CASCADE ON DELETE CASCADE,
                          quantity   NUMERIC(14,3)  NOT NULL DEFAULT 0,
                          created_at  TIMESTAMP WITH TIME ZONE  NOT NULL DEFAULT now(),
                          updated_at  TIMESTAMP WITH TIME ZONE
);

/* Accès le plus fréquent : findByProductId */
CREATE UNIQUE INDEX idx_articles_product
    ON articles (product_id);

/* =============================================================
   MOUVEMENTS DE STOCK  (forte volumétrie)
   =============================================================*/
CREATE TABLE stock_movements (
                                 id BIGSERIAL PRIMARY KEY,
                                 employee_number VARCHAR(100) NOT NULL,  -- Numéro de l'employé, récupéré via user-service
                                 observation TEXT,
                                 article_id BIGINT REFERENCES articles(id),
                                 movement_type VARCHAR(50) NOT NULL,   -- ENTREE ou SORTIE
                                 old_quantity NUMERIC(14,3) NOT NULL,  --
                                 quantity_moved NUMERIC(14,3) NOT NULL,  --
                                 new_quantity NUMERIC(14,3) NOT NULL,  --
                                 unit_of_measure VARCHAR(50) NOT NULL,
                                 movement_date TIMESTAMP WITH TIME ZONE NOT NULL,
                                 updated_at TIMESTAMP WITH TIME ZONE
);

/* Historique complet / dernier mouvement d’un article */
CREATE INDEX idx_mov_article_date
    ON stock_movements (article_id, movement_date DESC);

/* Rapports périodiques (plages de dates) */
CREATE INDEX idx_mov_date
    ON stock_movements (movement_date);

/* (Facultatif) index partiels pour ENTREE / SORTIE
CREATE INDEX idx_mov_in_dates
          ON stock_movements (movement_date)
          WHERE movement_type = 'ENTREE';

CREATE INDEX idx_mov_out_dates
          ON stock_movements (movement_date)
          WHERE movement_type = 'SORTIE';
*/
