INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3333r_1',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner2','0wn3333r_2',TRUE);
INSERT INTO authorities VALUES ('owner2','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner3','0wn3333r_3',TRUE);
INSERT INTO authorities VALUES ('owner3','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner4','0wn3333r_4',TRUE);
INSERT INTO authorities VALUES ('owner4','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner5','0wn3333r_5',TRUE);
INSERT INTO authorities VALUES ('owner5','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner6','0wn3333r_6',TRUE);
INSERT INTO authorities VALUES ('owner6','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner7','0wn3333r_7',TRUE);
INSERT INTO authorities VALUES ('owner7','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner8','0wn3333r_8',TRUE);
INSERT INTO authorities VALUES ('owner8','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner9','0wn3333r_9',TRUE);
INSERT INTO authorities VALUES ('owner9','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner10','0wn3333r_10',TRUE);
INSERT INTO authorities VALUES ('owner10','owner');
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3terinarian_1',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');
INSERT INTO users(username,password,enabled) VALUES ('vet2','v3terinarian_2',TRUE);
INSERT INTO authorities VALUES ('vet2','veterinarian');
INSERT INTO users(username,password,enabled) VALUES ('vet3','v3terinarian_3',TRUE);
INSERT INTO authorities VALUES ('vet3','veterinarian');
INSERT INTO users(username,password,enabled) VALUES ('vet4','v3terinarian_4',TRUE);
INSERT INTO authorities VALUES ('vet4','veterinarian');
INSERT INTO users(username,password,enabled) VALUES ('vet5','v3terinarian_5',TRUE);
INSERT INTO authorities VALUES ('vet5','veterinarian');
INSERT INTO users(username,password,enabled) VALUES ('vet6','v3terinarian_6',TRUE);
INSERT INTO authorities VALUES ('vet6','veterinarian');

INSERT INTO vets VALUES (1, 'James', 'Carter', '110 W. Liberty St.', 'Madison', '608555102', 'vet1');
INSERT INTO vets VALUES (2, 'Helen', 'Leary','110 W. Liberty St.', 'Madison', '608555102', 'vet2');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas', '110 W. Liberty St.', 'Madison', '608555102', 'vet3');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega', '110 W. Liberty St.', 'Madison', '608555102', 'vet4');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens', '110 W. Liberty St.', 'Madison', '608555102', 'vet5');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins', '110 W. Liberty St.', 'Madison', '608555102', 'vet6');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '608555102', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '608555174', 'owner2');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '608555876', 'owner3');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '608555319', 'owner4');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '608555276', 'owner5');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '608555265', 'owner6');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '608555538', 'owner7');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '608555768', 'owner8');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '608555943', 'owner9');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '608555548', 'owner10');

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

INSERT INTO medical_tests VALUES (1, 'Radiography', 'It is used to diagnose or treat patients by recording images of the internal structure of the body to assess the presence or absence of disease, foreign objects, and structural damage or anomaly. During a radiographic procedure, an x-ray beam is passed through the body.');
INSERT INTO medical_tests VALUES (2, 'Sonography', 'It uses ultrasonic sound waves in the frequency range of 1.5-15 megahertz (MHz) to create images of body structures based on the pattern of echoes reflected from the tissues and organs being imaged.');

INSERT INTO visit_medical_tests VALUES (2, 1);

INSERT INTO medicines VALUES (1, 'Paracetamol', 'BAY-2356', 'Antinflamatorio', '2021-07-04');
INSERT INTO medicines VALUES (2, 'Pet Dalsy', 'BOT-334', 'Ideal para cachorros', '2024-03-04');
INSERT INTO medicines VALUES (3, 'Ibuprofeno', 'IUP-004', 'Elimina síntomas de gripe', '2022-10-14');

INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (1,'2020-04-20','2020-03-27','Cita médica',1,1,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (2,'2020-06-30','2020-03-27','Estudio radiográfico',1,1,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (3,'2020-08-30','2020-03-27','Tratamiento de fractura',1,1,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (4,'2020-04-21','2020-03-27','Revisión dental',2,2,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (5,'2020-04-22','2020-03-27','Cita médica',10,12,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (6,SYSDATE+2,'2020-03-27','Revisión',1,1,2);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (7,SYSDATE,'2020-03-27','Revisión',1,1,2);

INSERT INTO banners(id, picture, slogan, target_url, organization_name, init_colab_date, end_colab_date) VALUES (1, 'https://pbs.twimg.com/media/ETaS5ThXsAIJST1.jpg', '#YoMeQuedoEnCasa', 'https://twitter.com/asoc_recal?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor', 'Recal: Refugio Canino', '2020-01-01', '2021-01-01');
INSERT INTO banners(id, picture, slogan, target_url, organization_name, init_colab_date, end_colab_date) VALUES (2, 'https://elrefugio.org/contenido/paginas/El-Refugio-ProyectoEdencabeceroweb.jpg', 'La vida no es eterna, pero el amor sí', 'https://elrefugio.org/default.aspx', 'elrefugio.org', '2019-10-01', '2020-10-01');
INSERT INTO banners(id, picture, slogan, target_url, organization_name, init_colab_date, end_colab_date) VALUES (3, 'https://scoobymedina.org/cache/resized/ac3a07da9000a7824d41c97f4d99d4ff.jpg', 'No compres y adopta ¡Pon color a sus vidas!', 'https://scoobymedina.org/es/', 'Protectora y Santuario Scooby', '2019-02-01', '2020-02-01');
INSERT INTO banners(id, picture, slogan, target_url, organization_name, init_colab_date, end_colab_date) VALUES (4, 'https://www.elhogar-animalsanctuary.org/wp-content/uploads/2016/10/fondo-web-2.jpg', '¡El amor nos cambia la vida a todos!', 'https://www.elhogar-animalsanctuary.org/el-santuario-2/', 'Fundación El Hogar', '2018-03-01', '2019-03-01');

INSERT INTO stays(id,pet_id,register_date,release_date,status) VALUES (1,1, '2020-10-01', '2020-10-05',true);
INSERT INTO stays(id,pet_id,register_date,release_date,status) VALUES (2,2, '2020-10-01', '2020-10-05',null);
INSERT INTO stays(id,pet_id,register_date,release_date,status) VALUES (3,2, '2020-11-01', '2020-11-05',null);
