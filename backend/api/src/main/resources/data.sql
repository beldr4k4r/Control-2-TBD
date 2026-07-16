CREATE EXTENSION IF NOT EXISTS postgis;
TRUNCATE TABLE tasks, auth_user, sectors RESTART IDENTITY CASCADE;

INSERT INTO sectors (sector_name, sector_location) VALUES
('Reparación de semáforos', ST_SetSRID(ST_MakePoint(-70.5354, -33.4872), 4326)),
('Limpieza vial', ST_SetSRID(ST_MakePoint(-70.6117, -33.4260), 4326)),
('Construcción', ST_SetSRID(ST_MakePoint(-70.5751, -33.4089), 4326)),
('Mantención de paraderos', ST_SetSRID(ST_MakePoint(-70.6693, -33.4489), 4326)),
('Poda de árboles', ST_SetSRID(ST_MakePoint(-70.5980, -33.4569), 4326));

--Contraseña para todos: password123
INSERT INTO auth_user (username, password, location) VALUES
('jperez', '$2a$10$BabNEZNrZr0eYggM.5uwX.t1yUtKK7P43PKEp2PKMf.MtG/aEKoae', ST_SetSRID(ST_MakePoint(-70.5478, -33.4831), 4326)),
('mgonzalez', '$2a$10$BabNEZNrZr0eYggM.5uwX.t1yUtKK7P43PKEp2PKMf.MtG/aEKoae', ST_SetSRID(ST_MakePoint(-70.6089, -33.4260), 4326)),
('crodriguez', '$2a$10$BabNEZNrZr0eYggM.5uwX.t1yUtKK7P43PKEp2PKMf.MtG/aEKoae', ST_SetSRID(ST_MakePoint(-70.5654, -33.4095), 4326));

INSERT INTO tasks (task_name, task_descrition, task_expired, complete_task, task_create, task_update, user_id, id_sector) VALUES
('Reparar semáforo intersección Vicuña Mackenna',
 'Semáforo intermitente presenta fallas en la fase peatonal, se requiere revisión eléctrica completa.',
 '2026-07-20', false, NOW(), NOW(),
 1, (SELECT id_sector FROM sectors WHERE sector_name = 'Reparación de semáforos')),

('Reemplazar módulo LED semáforo Av. Tobalaba',
 'Módulo LED quemado en luz roja, riesgo de accidente vehicular durante la noche.',
 '2026-07-25', false, NOW(), NOW(),
 2, (SELECT id_sector FROM sectors WHERE sector_name = 'Reparación de semáforos')),

('Limpieza de escombros calle Los Leones',
 'Retiro de escombros de obra dejados en la vía pública que obstruyen el tránsito peatonal.',
 '2026-07-18', false, NOW(), NOW(),
 3, (SELECT id_sector FROM sectors WHERE sector_name = 'Limpieza vial')),

('Barrido y lavado Av. Providencia',
 'Lavado programado de calzada tras evento masivo de fin de semana en el sector.',
 '2026-07-22', true, NOW(), NOW(),
 1, (SELECT id_sector FROM sectors WHERE sector_name = 'Limpieza vial')),

('Reparación de vereda calle Apoquindo',
 'Levantamiento de baldosas y reconstrucción de vereda dañada por raíces de árboles.',
 '2026-08-01', false, NOW(), NOW(),
 2, (SELECT id_sector FROM sectors WHERE sector_name = 'Construcción')),

('Instalación de rampa accesibilidad plaza central',
 'Construcción de rampa de acceso universal solicitada por vecinos de la comuna.',
 '2026-08-10', false, NOW(), NOW(),
 3, (SELECT id_sector FROM sectors WHERE sector_name = 'Construcción')),

('Reparar techo paradero Av. Kennedy',
 'Techo de paradero de buses presenta filtraciones de agua durante la lluvia.',
 '2026-07-19', false, NOW(), NOW(),
 1, (SELECT id_sector FROM sectors WHERE sector_name = 'Mantención de paraderos')),

('Repintado señalética paradero Estación Central',
 'Señalética de horarios y rutas ilegible, se requiere repintado y actualización de información.',
 '2026-07-30', true, NOW(), NOW(),
 2, (SELECT id_sector FROM sectors WHERE sector_name = 'Mantención de paraderos')),

('Poda de árboles Parque Bustamante',
 'Ramas de árboles interfieren con cableado eléctrico y visibilidad de semáforos cercanos.',
 '2026-07-28', false, NOW(), NOW(),
 3, (SELECT id_sector FROM sectors WHERE sector_name = 'Poda de árboles')),

('Retiro de árbol caído calle Manquehue',
 'Árbol caído tras temporal bloquea parcialmente la vía, requiere retiro urgente.',
 '2026-07-16', true, NOW(), NOW(),
 1, (SELECT id_sector FROM sectors WHERE sector_name = 'Poda de árboles')),

('Revisión temporizador semáforo Plaza Italia',
 'Ciclo de luces desincronizado genera congestión vehicular en horario punta.',
 '2026-07-24', false, NOW(), NOW(),
 2, (SELECT id_sector FROM sectors WHERE sector_name = 'Reparación de semáforos')),

('Cambio de controlador semáforo Av. Grecia',
 'Controlador antiguo presenta fallas intermitentes, se recomienda reemplazo completo.',
 '2026-08-05', false, NOW(), NOW(),
 3, (SELECT id_sector FROM sectors WHERE sector_name = 'Reparación de semáforos')),

('Retiro de grafitis calle Bandera',
 'Limpieza de fachadas y mobiliario urbano afectado por grafitis en el sector céntrico.',
 '2026-07-21', true, NOW(), NOW(),
 2, (SELECT id_sector FROM sectors WHERE sector_name = 'Limpieza vial')),

('Despeje de basurales Av. Departamental',
 'Acumulación de residuos domiciliarios en sitio eriazo requiere retiro y fiscalización.',
 '2026-07-27', false, NOW(), NOW(),
 1, (SELECT id_sector FROM sectors WHERE sector_name = 'Limpieza vial')),

('Pavimentación calle interior Villa Frei',
 'Calle de tierra genera polvo y dificulta el tránsito en días de lluvia.',
 '2026-08-15', false, NOW(), NOW(),
 3, (SELECT id_sector FROM sectors WHERE sector_name = 'Construcción')),

('Ampliación de vereda peatonal Metro Escuela Militar',
 'Vereda angosta provoca aglomeración de peatones en horario de alta demanda.',
 '2026-08-20', false, NOW(), NOW(),
 1, (SELECT id_sector FROM sectors WHERE sector_name = 'Construcción')),

('Instalación de bancas paradero Av. Matta',
 'Paradero sin asientos genera molestias a adultos mayores que esperan la locomoción.',
 '2026-07-31', false, NOW(), NOW(),
 2, (SELECT id_sector FROM sectors WHERE sector_name = 'Mantención de paraderos')),

('Reparación vidrio roto paradero Irarrázaval',
 'Vidrio de protección del paradero fue vandalizado y presenta bordes cortantes.',
 '2026-07-23', true, NOW(), NOW(),
 3, (SELECT id_sector FROM sectors WHERE sector_name = 'Mantención de paraderos')),

('Poda preventiva Av. Los Trapenses',
 'Ramas altas representan riesgo de caída sobre tendido eléctrico durante temporal.',
 '2026-08-08', false, NOW(), NOW(),
 2, (SELECT id_sector FROM sectors WHERE sector_name = 'Poda de árboles')),

('Extracción de raíces plaza Ñuñoa',
 'Raíces de árbol levantan baldosas de la plaza y afectan accesibilidad peatonal.',
 '2026-08-12', false, NOW(), NOW(),
 1, (SELECT id_sector FROM sectors WHERE sector_name = 'Poda de árboles'));
