USE `crm-import`;

-- Create center view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`center` AS
SELECT
 center.id AS id,
 center.label AS name,
 ifnull(center.code,concat('0',cast(center.id as char(2) charset utf8))) AS reference
 FROM `crm`.`eurocommercial_center` AS center;


-- Create user view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`user` AS
SELECT
 u.user_id AS id,
 u.user_id AS reference,
 u.enabled AS enabled,
 CASE u.genre
 WHEN "m" THEN "MALE"
 WHEN "f" THEN "FEMALE" 
 ELSE "UNKNOWN_IMPORT"
 END AS gender,
 IFNULL(UCASE(u.title),"UNKNOWN_IMPORT") AS title,
 IFNULL(u.first_name,"UNKNOWN_IMPORT") AS firstName,
 IFNULL(u.last_name,"UNKNOWN_IMPORT") AS lastName,
 IFNULL(u.email,"UNKNOWN_IMPORT") AS email,
 IFNULL(u.address, "UNKNOWN_IMPORT") AS address,
 IFNULL(u.zipcode, "UNKNOWN_IMPORT") AS zipcode,
 IFNULL(u.city, "UNKNOWN_IMPORT") AS city,
 IFNULL(p.field_phone_value, "UNKNOWN_IMPORT") AS phoneNumber,
 IFNULL(u.card_number,"UNKNOWN_IMPORT") AS cardNumber,
 u.optin AS promotionalEmails,
 c.reference AS centerReference
 FROM `crm`.`eurocommercial_crm_user_view5` AS u
 INNER JOIN `center` AS c ON c.id = u.center_id
 INNER JOIN `crm`.`field_data_field_phone` AS p ON p.entity_id = u.user_id;
 
-- Create card view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`card` AS
SELECT
 card.id AS id,
 card.number AS number,
 UCASE(card.status) AS status,
 card.user_id AS userId,
 card.client_id AS clientId,
 center.reference AS centerReference,
 card.created_at AS createdAt,
 card.given_at AS givenToUserAt,
 card.sent_at AS sentToUserAt
 FROM `crm`.`eurocommercial_crm_user_card` AS card
 INNER JOIN `center` AS center ON center.id = card.center_id;
 
-- Create child view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`child` AS
SELECT
 child.id AS id,
 CASE gender.field_genre_value
 WHEN "m" THEN "MALE"
 WHEN "f" THEN "FEMALE" 
 ELSE "UNKNOWN_IMPORT"
 END AS gender,
 child.names AS name,
 bd.field_birthdate_value AS birthdate,
 child.user_id AS parentReference,
 child.info AS notes,
 child.start_at AS startTime,
 child.stop_at AS endTime
 FROM `crm`.`children` AS child
 INNER JOIN `crm`.`field_data_field_birthdate` AS bd ON child.id = bd.entity_id
 INNER JOIN `crm`.`field_data_field_genre` AS gender ON child.id = gender.entity_id