-- One admin user, named admin1 with password 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with password 0wn3r
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
-- One vet user, named vet1 with password v3t
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
INSERT INTO specialties VALUES (4, 'toxicology');
INSERT INTO specialties VALUES (5, 'dermatology');
INSERT INTO specialties VALUES (6, 'pathology');
INSERT INTO specialties VALUES (7, 'nutrition');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 5);
INSERT INTO vet_specialties VALUES (6, 6);
INSERT INTO vet_specialties VALUES (6, 7);
INSERT INTO vet_specialties VALUES (4, 4);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');
INSERT INTO types VALUES (7, 'rabbit');

INSERT INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '608555102', 'owner1');
INSERT INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '608555174', 'owner2');
INSERT INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '608555876', 'owner3');
INSERT INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '608555319', 'owner4');
INSERT INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '608555276', 'owner5');
INSERT INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '608555265', 'owner6');
INSERT INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '608555538', 'owner7');
INSERT INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '608555768', 'owner8');
INSERT INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '608555943', 'owner9');
INSERT INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '608555548', 'owner10');

INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 0, '', true , 1, 1);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 1, 'It is impossible to accept it because the hamster quota has been exceeded', true , 6, 1);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (3, 'George', '2010-01-20', 2, '', true , 4, 1);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (4, 'Rosy', '2011-04-17', 2, '', true , 2, 3);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (5, 'Jewel', '2010-03-07', 0, '', false , 2, 3 );
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (6, 'Iggy', '2010-11-30', 1,'It is impossible to accept it because the lizard quota has been exceeded', true , 3, 3 );
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 0, '', true , 1, 3);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 'It is impossible to accept it because the cat quota has been exceeded', true , 1, 6);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 2, '', true , 5, 6);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 0, '', true , 2, 6);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 1, 'It is impossible to accept it because the bird quota has been exceeded', true , 5, 9);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, '', true , 2, 10);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 0, '', true , 1, 10);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (14, 'Nina', '2017-07-21', 0, '', true , 2, 2);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (15, 'Luca', '2016-08-16', 0, '', true , 2, 4);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (16, 'Goofy', '2014-11-04', 0, '', true , 6, 5);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (17, 'Sara', '2015-03-25', 0, '', true , 4, 2);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (18, 'Jara', '2018-05-17', 0, '', true , 5, 7);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (19, 'Chispa', '2011-09-03', 0, '', true , 7, 8);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (20, 'Luna', '2018-01-30', 0, '', true , 1, 7);
INSERT INTO pets(id,name,birth_date,status,justification,active,type_id,owner_id) VALUES (21, 'Romeo', '2019-02-18', 0, '', true , 5, 9);

INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (1,'2019-01-01','2018-12-20','Rabies shot',6,7,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (2,'2019-01-02','2018-12-13','Rabies shot',6,8,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (3,'2019-01-15','2019-01-02','Neutering',6,8,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (4,'2019-01-24','2019-01-11','Sterilization',6,7,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (5,'2020-04-20','2020-03-27','Medical appointment',1,1,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (6,'2020-06-30','2020-03-27','Radiographic study',1,1,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (7,'2020-08-30','2020-03-27','Fracture treatment',1,1,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (8,'2020-05-01','2020-03-27','Dental checkup',2,2,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (9,'2020-05-01','2020-03-27','Medical appointment',10,12,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (10,CURRENT_DATE()+2,'2020-03-27','Radiographic study',1,1,2);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (11,CURRENT_DATE(),'2020-03-27','Fracture treatment',1,1,2);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (12,CURRENT_DATE(),'2020-03-15','Rabies shot',4,15,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (13,CURRENT_DATE(),'2020-02-23','Annual checkup',7,20,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (14,'2020-03-24','2020-03-20','Radiographic study',2,14,2);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (15,'2020-03-10','2020-03-08','Toxicologic study',2,17,4);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (16,CURRENT_DATE()+2,'2020-04-18','Dental checkup',6,10,3);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (17,CURRENT_DATE(),'2020-05-02','Dental checkup',8,19,3);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (18,CURRENT_DATE()+3,'2020-04-23','Toxicologic study',9,21,4);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (19,CURRENT_DATE(),'2020-05-02','Examination of spots on the pet''s skin',5,16,5);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (20,CURRENT_DATE()+1,'2020-03-14','Fluid Analysis and CBC test',2,14,6);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (21,CURRENT_DATE(),'2020-02-23','Annual checkup',7,18,1);
INSERT INTO appointments(id,appointment_date,appointment_request_date,description,owner_id,pet_id,vet_id) VALUES (22,'2020-01-26','2019-12-05','Sterilization',3,5,4);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2019-01-01', 'Successful rabies vaccination');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2019-01-02', 'Successful rabies vaccination');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2019-01-15', 'Neutered pet properly');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2019-01-24', 'Spayed pet properly');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (5, 1, '2020-04-20', 'Clinical examination of the pet');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (6, 2, '2020-05-01', 'Dental checkup made. Everything is correct.');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (7, 12, '2020-05-01', 'Clinical examination of the pet');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (8, 14, '2020-03-24', 'Radiographic study: Some radiographies made, everything looks correct.');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (9, 17, '2020-03-10', 'Toxicologic study: At least one toxic substance has been found. A penicillin treatment is necessary.');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (10, 5, '2020-01-26', 'Neutered pet properly');

INSERT INTO medical_tests VALUES (1, 'Radiography', 'It is used to diagnose or treat patients by recording images of the internal structure of the body to assess the presence or absence of disease, foreign objects, and structural damage or anomaly. During a radiographic procedure, an x-ray beam is passed through the body.');
INSERT INTO medical_tests VALUES (2, 'Sonography', 'It uses ultrasonic sound waves in the frequency range of 1.5-15 megahertz (MHz) to create images of body structures based on the pattern of echoes reflected from the tissues and organs being imaged.');
INSERT INTO medical_tests VALUES (3, 'Toxicologic test', 'If your veterinarian suspects that your pet has been poisoned, samples will be collected for toxicologic tests to identify the poison and the amount of damage it may have caused. Some common poisons can be quickly identified. Rapid identification of a poison can be critical for your pet''s survival.');
INSERT INTO medical_tests VALUES (4, 'Serologic test', 'Serology is the study of blood serum and other body fluids. Most serologic tests determine the level of antibodies that are present and reactive against a particular infectious microorganism. ');
INSERT INTO medical_tests VALUES (5, 'Complete Blood Count (CBC)', 'This test determines the number and types of cells circulating in the bloodstream and provides basic information about anemia, inflammation, and clotting. Determining the number of red blood cells, their size and shape, and their hemoglobin content helps identify disorders such as anemia.');
INSERT INTO medical_tests VALUES (6, 'Fluid Analysis','Fluid analysis is the study of bodily fluids other than blood (urine, joint fluid, etc). Specialists in analyzing body fluids work closely with other specialists to help provide information about the health of an animal. Typically, fluid analysis includes checking the sample for cells and proteins.');

INSERT INTO visit_medical_tests VALUES (6, 1);
INSERT INTO visit_medical_tests VALUES (8, 1);
INSERT INTO visit_medical_tests VALUES (9, 2);
INSERT INTO visit_medical_tests VALUES (9, 3);
INSERT INTO visit_medical_tests VALUES (5, 5);
INSERT INTO visit_medical_tests VALUES (4, 2);

INSERT INTO medicines VALUES (1, 'Penicillin', 'PEN-2356', 'It doesn''t kill viruses, but it''s sometimes prescribed to treat secondary bacterial infections that can occur when an animal is ill from a viral infection.', '2021-07-04');
INSERT INTO medicines VALUES (2, 'Pet-Dalsy', 'PDA-334', 'Ideal for puppies', '2024-03-04');
INSERT INTO medicines VALUES (3, 'Oxycodone', 'OXD-027', 'This is a potent pain reliever. It must be highly controlled because of their addictive potential.', '2022-10-14');
INSERT INTO medicines VALUES (4, 'Antiparasitic', 'APA-153', 'This is a potent pain reliever. It must be highly controlled because of their addictive potential.', '2024-09-23');
INSERT INTO medicines VALUES (5, 'Methimazole ', 'MET-529', 'This medicine is made for abnormal thyroid hormone levels.', '2023-11-17');
INSERT INTO medicines VALUES (6, 'Atenolol', 'ATN-674', 'Heart medication used to control the circulatory system', '2024-05-09');

INSERT INTO banners(id, picture, slogan, target_url, organization_name, init_colab_date, end_colab_date) VALUES (1, 'https://pbs.twimg.com/media/ETaS5ThXsAIJST1.jpg', '#YoMeQuedoEnCasa', 'https://twitter.com/asoc_recal?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor', 'Recal: Refugio Canino', '2020-01-01', '2021-01-01');
INSERT INTO banners(id, picture, slogan, target_url, organization_name, init_colab_date, end_colab_date) VALUES (2, 'https://elrefugio.org/contenido/paginas/El-Refugio-ProyectoEdencabeceroweb.jpg', 'La vida no es eterna, pero el amor sí', 'https://elrefugio.org/default.aspx', 'elrefugio.org', '2019-10-01', '2020-10-01');
INSERT INTO banners(id, picture, slogan, target_url, organization_name, init_colab_date, end_colab_date) VALUES (3, 'https://scoobymedina.org/cache/resized/ac3a07da9000a7824d41c97f4d99d4ff.jpg', 'No compres y adopta ¡Pon color a sus vidas!', 'https://scoobymedina.org/es/', 'Protectora y Santuario Scooby', '2019-02-01', '2020-02-01');
INSERT INTO banners(id, picture, slogan, target_url, organization_name, init_colab_date, end_colab_date) VALUES (4, 'https://www.elhogar-animalsanctuary.org/wp-content/uploads/2016/10/fondo-web-2.jpg', '¡El amor nos cambia la vida a todos!', 'https://www.elhogar-animalsanctuary.org/el-santuario-2/', 'Fundación El Hogar', '2018-03-01', '2019-03-01');

INSERT INTO stays(id,pet_id,register_date,release_date,status) VALUES (1,1, '2020-10-01', '2020-10-05',0);
INSERT INTO stays(id,pet_id,register_date,release_date,status) VALUES (2,7, '2020-10-01', '2020-10-05',2);
INSERT INTO stays(id,pet_id,register_date,release_date,status) VALUES (3,7, '2020-11-01', '2020-11-05',2);

INSERT INTO treatments(id,name,description,time_limit,pet_id) VALUES (1,'Treatment 1', 'Description 1','2020-10-01',1);

INSERT INTO treatments_history(id,name,description,time_limit,medicines,treatment_id,pet_id) VALUES (1,'Treatment 1','Description 1','2020-09-01','(PEN-2356) - Penicillin#(APA-153) - Antiparasitic',1,1);
