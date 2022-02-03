INSERT INTO warehouses(name) VALUES ('Armazém de São Paulo');
INSERT INTO warehouses(name) VALUES ('Armazém da Bahia');

INSERT INTO seller (name) VALUES ('Sadia');
INSERT INTO seller (name) VALUES ('Procter & Gamble');

INSERT INTO agents(name, warehouse_id) VALUES ('Lucca', 1);
INSERT INTO agents(name, warehouse_id) VALUES ('João', 2);

INSERT INTO sections(category) VALUES ('REFRIGERADO');
INSERT INTO sections(category) VALUES ('CONGELADO');
INSERT INTO sections(category) VALUES ('FRESCO');

INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 1, 1);
INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 2, 1);
INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 3, 1);
INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 1, 2);
INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 2, 2);
INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 3, 2);
