INSERT INTO inventory_service_schema.unit_of_measure (name)
VALUES
    ('LITRE'),
    ('KILOGRAMME'),
    ('UNITE');

INSERT INTO inventory_service_schema.categories (id,  name, created_at)
VALUES (1, 'LIQUIDE', '2024-09-18');



INSERT INTO inventory_service_schema.products
(name, code, tenant_code, category_id, unit_of_measure_id, volume, created_at, updated_at)
VALUES ('Volvic Mineral Water', 'P1','T1', 1, 1, 1.5, NOW(), NOW()),
       ('Coca-Cola', 'P2', 'T1', 1, 1, 1.0, NOW(), NOW());
