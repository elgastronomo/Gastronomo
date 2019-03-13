-- 
-- SQL script that will be used to initialize the database
-- Note that the syntax is very picky. A reference is available at
--   http://www.hsqldb.org/doc/guide/sqlgeneral-chapt.html
-- 
-- When writing INSERT statements, the order must be exactly as found in
-- the server logs (search for "create table user"), or as 
-- specified within (creation-compatible) parenthesis:
--     INSERT INTO user(id,enabled,login,password,roles) values (...)
-- vs
--     INSERT INTO user VALUES (...)
-- You can find the expected order by inspecting the output of Hibernate
-- in your server logs (assuming you use Spring + JPA)
--

-- On passwords:
--
-- valid bcrypt-iterated, salted-and-hashed passwords can be generated via
-- https://www.dailycred.com/article/bcrypt-calculator
-- (or any other implementation) and prepending the text '{bcrypt}'
--
-- Note that every time that you generate a bcrypt password with a given 
-- password you will get a different result, because the first characters
-- after the third $ correspond to a random salt that will be different each time.
--
-- a few simple examples:
-- {bcrypt}$2a$04$2ao4NQnJbq3Z6UeGGv24a.wRRX0FGq2l5gcy2Pjd/83ps7YaBXk9C == 'a'
-- {bcrypt}$2a$04$5v02dQ.kxt7B5tJIA4gh3u/JFQlxmoCadSnk76PnvoN35Oz.ge3GK == 'p'
-- {bcrypt}$2a$04$9rrSETFYL/gqiBxBCy3DMOIZ6qmLigzjqnOGbsNji/bt65q.YBfjK == 'q'

-- an admin with password 'a'
INSERT INTO user(id,enabled,login,password,roles) VALUES (
	1, 1, 'a', 
	'{bcrypt}$2a$04$2ao4NQnJbq3Z6UeGGv24a.wRRX0FGq2l5gcy2Pjd/83ps7YaBXk9C',
	'USER,ADMIN'
);
-- a teacher with password 'p'
INSERT INTO user(id,enabled,login,password,roles) VALUES (
	2, 1, 'p', 
	'{bcrypt}$2a$04$5v02dQ.kxt7B5tJIA4gh3u/JFQlxmoCadSnk76PnvoN35Oz.ge3GK', 'USER');
-- a teacher with password 'q'
INSERT INTO user(id,enabled,login,password,roles) VALUES (
	3, 1, 'q', 
	'{bcrypt}$2a$04$9rrSETFYL/gqiBxBCy3DMOIZ6qmLigzjqnOGbsNji/bt65q.YBfjK', 'USER');


INSERT INTO recipe(id,name,url,attribution,duration,calories,weight,rations,difficulty,cuisine,steps,user_id) VALUES (1,'Ensalada de pasta italiana','http://www.comidakraft.com/sp/recipes/ensalada-de-pasta-italiana-152154.aspx','Comida Kraft',40,2243.07,974.5,8.0,'Medio','Italiana','{"1": "Combina todos los ingredientes menos el aderezo en un tazón grande.", "2": "Agrega el aderezo; mezcla todo ligeramente.", "3": "Refrigera la ensalada durante varias horas."}',1);
INSERT INTO recipe(id,name,url,attribution,duration,calories,weight,rations,difficulty,cuisine,steps,user_id) VALUES (2,'Crema pastelera tradicional italiana','https://cookpad.com/es/recetas/162212-crema-pastelera-tradicional-italiana','Cookpad Spain',40,1088.3436928,588.582855,6.0,'Medio','Italiana','',1);
INSERT INTO recipe(id,name,url,attribution,duration,calories,weight,rations,difficulty,cuisine,steps,user_id) VALUES (3,'Sopa italiana de patatas','http://sopa-italiana-de-patatas.recetascomidas.com/','Recetal Comidas',40,1455.90016,2287.414446,6.0,'Medio','Italiana','',1);
INSERT INTO recipe(id,name,url,attribution,duration,calories,weight,rations,difficulty,cuisine,steps,user_id) VALUES (4,'Salsa de tomates italiana','http://allrecipes.com.ar/receta/93/salsa-de-tomates-italiana.aspx?o_is=Hub_TopRecipe_4','Allrecipes Argentina',40,678.72657,575.6943405,4.0,'Medio','Italiana','',1);
INSERT INTO recipe(id,name,url,attribution,duration,calories,weight,rations,difficulty,cuisine,steps,user_id) VALUES (5,'Tiramisú, receta italiana','https://cookpad.com/es/recetas/263150-tiramisu-receta-italiana','Cookpad Spain',40,40549.328,17588.35,34.0,'Medio','Italiana','',1);

INSERT INTO nutrient(id,nutrient) VALUES (1,'Calcium');
INSERT INTO nutrient(id,nutrient) VALUES (2,'Carbs');
INSERT INTO nutrient(id,nutrient) VALUES (3,'Cholesterol');
INSERT INTO nutrient(id,nutrient) VALUES (4,'Fat');
INSERT INTO nutrient(id,nutrient) VALUES (5,'Iron');
INSERT INTO nutrient(id,nutrient) VALUES (6,'Fiber');
INSERT INTO nutrient(id,nutrient) VALUES (7,'Potassium');
INSERT INTO nutrient(id,nutrient) VALUES (8,'Magnesium');
INSERT INTO nutrient(id,nutrient) VALUES (9,'Sodium');
INSERT INTO nutrient(id,nutrient) VALUES (10,'Vitamin B6');

INSERT INTO ingredient(id,name) VALUES (1,'3 tazas de pasta rotini, cocida');
INSERT INTO ingredient(id,name) VALUES (2,'2 tazas de floretes de brócoli');
INSERT INTO ingredient(id,name) VALUES (3,'1/2 taza de queso parmesano rallado KRAFT Grated Parmesan Cheese');
INSERT INTO ingredient(id,name) VALUES (4,'1/2 taza de pimiento (pimentón) rojo picado');
INSERT INTO ingredient(id,name) VALUES (5,'1/2 taza de rebanadas de cebolla morada');
INSERT INTO ingredient(id,name) VALUES (6,'1/2 taza de aceitunas negras sin hueso');
INSERT INTO ingredient(id,name) VALUES (7,'1 taza de aderezo italiano fuerte KRAFT Zesty Italian Dressing');
INSERT INTO ingredient(id,name) VALUES (8,'2 cucharadas de azúcar');
INSERT INTO ingredient(id,name) VALUES (9,'2 cucharadas de harina.');
INSERT INTO ingredient(id,name) VALUES (10,'1 huevo');
INSERT INTO ingredient(id,name) VALUES (11,'1  limón la cáscara');
INSERT INTO ingredient(id,name) VALUES (12,'200ml de nata o crema de leche, un vaso');
INSERT INTO ingredient(id,name) VALUES (13,'300ml  de leche vaso y medio');
INSERT INTO ingredient(id,name) VALUES (14,'1/2 kg. de patatas');
INSERT INTO ingredient(id,name) VALUES (15,'1 cebolla');
INSERT INTO ingredient(id,name) VALUES (16,'1 ajo');
INSERT INTO ingredient(id,name) VALUES (17,'2 ramas de apio');
INSERT INTO ingredient(id,name) VALUES (18,'1/4 kg. de tomates');
INSERT INTO ingredient(id,name) VALUES (19,'60 gr. de parmesano rallado');
INSERT INTO ingredient(id,name) VALUES (20,'1 cucharadita de salvia picada');
INSERT INTO ingredient(id,name) VALUES (21,'1 cucharada de perejil picado');
INSERT INTO ingredient(id,name) VALUES (22,'1 l. de caldo de carne');
INSERT INTO ingredient(id,name) VALUES (23,'Sal');
INSERT INTO ingredient(id,name) VALUES (24,'Pimienta');
INSERT INTO ingredient(id,name) VALUES (25,'Aceite de oliva');
INSERT INTO ingredient(id,name) VALUES (26,'500g de tomates enlatados, picados');
INSERT INTO ingredient(id,name) VALUES (27,'4 dientes de ajo, finamente picados');
INSERT INTO ingredient(id,name) VALUES (28,'2 cucharadas de aceite de oliva');
INSERT INTO ingredient(id,name) VALUES (29,'2 cucharadas de pesto de albahaca');
INSERT INTO ingredient(id,name) VALUES (30,'Sal y pimienta a gusto');
INSERT INTO ingredient(id,name) VALUES (31,'3 huevos (3 claras, 3 yemas)');
INSERT INTO ingredient(id,name) VALUES (32,'250 g queso mascarpone');
INSERT INTO ingredient(id,name) VALUES (33,'3 cucharadas azúcar');
INSERT INTO ingredient(id,name) VALUES (34,'Bizcochos soletilla');
INSERT INTO ingredient(id,name) VALUES (35,'1 vaso café');
INSERT INTO ingredient(id,name) VALUES (36,'Brandy');
INSERT INTO ingredient(id,name) VALUES (37,'Chocolate negro, unos cuadrados o cacao en polvo');

INSERT INTO tag(id,tag) VALUES (1,'Balanced');
INSERT INTO tag(id,tag) VALUES (2,'Vegetarian');
INSERT INTO tag(id,tag) VALUES (3,'Egg-Free');
INSERT INTO tag(id,tag) VALUES (4,'Peanut-Free');
INSERT INTO tag(id,tag) VALUES (5,'Tree-Nut-Free');
INSERT INTO tag(id,tag) VALUES (6,'Soy-Free');
INSERT INTO tag(id,tag) VALUES (7,'Fish-Free');
INSERT INTO tag(id,tag) VALUES (8,'Shellfish-Free');
INSERT INTO tag(id,tag) VALUES (9,'Low-Carb');
INSERT INTO tag(id,tag) VALUES (10,'Low-Sodium');
INSERT INTO tag(id,tag) VALUES (11,'Gluten-Free');
INSERT INTO tag(id,tag) VALUES (12,'Low-Fat');

INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (1,1);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (1,2);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (1,3);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (1,4);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (1,5);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (1,6);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (1,7);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (1,8);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (2,9);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (2,10);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (2,2);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (2,4);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (2,5);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (2,6);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (2,7);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (2,8);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (3,1);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (3,11);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (3,3);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (3,4);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (3,5);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (3,6);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (3,7);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (3,8);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (4,9);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (4,2);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (4,11);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (4,3);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (4,4);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (4,6);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (4,7);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (4,8);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (5,12);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (5,10);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (5,2);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (5,4);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (5,5);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (5,6);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (5,7);
INSERT INTO recipe_tags(recipe_id,tags_id) VALUES (5,8);

INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (1,907.755,90.7755,'mg',1,1);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (2,285.81,95.27,'g',2,1);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (3,195.87,65.29,'mg',3,1);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (4,93.2633,143.482,'g',4,1);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (5,9.67765,53.7647222222222,'mg',5,1);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (6,18.82,75.28,'g',6,1);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (7,1642.515,46.929,'mg',7,1);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (8,251.42,62.855,'mg',8,1);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (9,1370.455,57.1022916666667,'mg',9,1);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (10,1.188995,59.44975,'mg',10,1);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (11,503.59376865,50.359376865,'mg',1,2);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (12,58.0064177525,19.3354725841667,'g',2,2);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (13,446.303198,148.767732666667,'mg',3,2);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (14,87.825977925,135.116889115385,'g',4,2);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (15,1.0164848315,5.64713795277778,'mg',5,2);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (16,0.430773625,1.7230945,'g',6,2);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (17,628.49264985,17.9569328528571,'mg',7,2);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (18,52.9983905,13.249597625,'mg',8,2);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (19,263.5867689,10.9827820375,'mg',9,2);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (20,0.2350498978,11.75249489,'mg',10,2);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (21,1013.74870704,101.374870704,'mg',1,3);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (22,176.3057968,58.7685989333333,'g',2,3);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (23,71.23368,23.74456,'mg',3,3);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (24,56.821522,87.4177261538462,'g',4,3);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (25,10.3238504718,57.3547248433333,'mg',5,3);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (26,22.0744,88.2976,'g',6,3);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (27,4967.66047568,141.933156448,'mg',7,3);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (28,289.57882446,72.394706115,'mg',8,3);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (29,4250.55758068,177.106565861667,'mg',9,3);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (30,3.08009116,154.004558,'mg',10,3);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (31,282.45115172,28.245115172,'mg',1,4);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (32,27.0419536,9.01398453333333,'g',2,4);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (33,10.12095,3.37365,'mg',3,4);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (34,62.090923,95.5244969230769,'g',4,4);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (35,4.85737432365,26.9854129091667,'mg',5,4);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (36,7.824228,31.296912,'g',6,4);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (37,1526.14052724,43.604015064,'mg',7,4);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (38,131.853523405,32.96338085125,'mg',8,4);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (39,1728.62692099,72.0261217079167,'mg',9,4);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (40,0.6908725,34.543625,'mg',10,4);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (41,384.13,38.413,'mg',1,5);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (42,136.63589,45.5452966666667,'g',2,5);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (43,784.88,261.626666666667,'mg',3,5);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (44,123.1418,189.448923076923,'g',4,5);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (45,12.515435,69.5301944444444,'mg',5,5);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (46,2.45265,9.8106,'g',6,5);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (47,1252.2075,35.7773571428571,'mg',7,5);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (48,86.2925,21.573125,'mg',8,5);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (49,1740.6285,72.5261875,'mg',9,5);
INSERT INTO recipe_nutrient(id,cuantity,daily_percentage,unit,nutrient_id,recipe_id) VALUES (50,0.5083045,25.415225,'mg',10,5);

INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (1,315,1,1);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (2,176,1,2);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (3,56,1,3);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (4,74,1,4);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (5,57,1,5);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (6,60,1,6);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (7,235,1,7);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (8,25,2,8);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (9,15,2,9);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (10,38,2,10);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (11,0,2,11);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (12,200,2,12);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (13,309,2,13);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (14,500,3,14);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (15,110,3,15);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (16,5,3,16);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (17,312,3,17);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (18,250,3,18);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (19,60,3,19);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (20,0,3,20);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (21,3,3,21);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (22,1014,3,22);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (23,4,3,23);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (24,0,3,24);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (25,27,3,25);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (26,500,4,26);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (27,12,4,27);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (28,27,4,28);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (29,34,4,29);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (30,2,4,30);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (31,114,5,31);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (32,250,5,32);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (33,37,5,33);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (34,130,5,34);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (35,237,5,35);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (36,16791,5,36);
INSERT INTO recipe_ingredient(id,weight,recipe_id,ingredient_id) VALUES (37,28,5,37);
