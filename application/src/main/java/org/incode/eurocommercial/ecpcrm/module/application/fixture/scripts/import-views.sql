USE `crm-import`;

-- Create center view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`Center` AS
SELECT
 center.id AS id,
 center.label AS name,
 ifnull(center.code,concat('0',cast(center.id as char(2) charset utf8))) AS code,
 null AS mailchimpListId,
 null AS contactEmail
 FROM `crm`.`eurocommercial_center` AS center;


-- Create user view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`User` AS
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
 LCASE(REPLACE(u.email, ".C0M", ".COM")) AS email,
 u.birthdate AS birthDate,
 IFNULL(u.address, "UNKNOWN_IMPORT") AS address,
 IFNULL(u.zipcode, "UNKNOWN_IMPORT") AS zipcode,
 IFNULL(u.city, "UNKNOWN_IMPORT") AS city,
 IFNULL(p.field_phone_value, "UNKNOWN_IMPORT") AS phoneNumber,
 IFNULL(u.card_number,"UNKNOWN_IMPORT") AS cardNumber,
 u.optin AS promotionalEmails,
 car.field_car_value AS hasCar,
 c.code AS centerCode
 FROM (
 `crm`.`eurocommercial_crm_user_view5` AS u
 INNER JOIN `Center` AS c ON c.id = u.center_id)
 LEFT JOIN `crm`.`field_data_field_phone` AS p ON p.entity_id = u.user_id
 LEFT JOIN `crm`.`field_data_field_car` AS car ON car.entity_id = u.user_id
 WHERE u.email IS NOT NULL AND u.email NOT LIKE "CARD-%" AND u.email REGEXP '[0-9a-zA-Z!#$%&()<>@*+/=?^_{|}~@\.-]@[0-9a-zA-Z!#$%&()<>@*+/=?^_{|}~@\.-]\.[0-9a-zA-Z!#$%&()<>@*+/=?^_{|}~@\.-]'
 ORDER BY reference DESC;
 
-- Create card view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`Card` AS
SELECT
 card.id AS id,
 card.number AS number,
 UCASE(card.status) AS status,
 card.user_id AS userId,
 card.client_id AS clientId,
 center.code AS centerCode,
 card.created_at AS createdAt,
 card.given_at AS givenToUserAt,
 card.sent_at AS sentToUserAt
 FROM `crm`.`eurocommercial_crm_user_card` AS card
 INNER JOIN `Center` AS center ON center.id = card.center_id
 INNER JOIN `User` AS user ON card.user_id = user.id;
 
-- Create child view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`Child` AS
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
 LEFT JOIN `crm`.`field_data_field_birthdate` AS bd ON child.id = bd.entity_id
 LEFT JOIN `crm`.`field_data_field_genre` AS gender ON child.id = gender.entity_id;
 
-- Create cardRequest view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`CardRequest` AS
SELECT
 request.id AS id,
 center.code AS centerCode,
 request.user_id AS requestingUser,
 CASE request.hostess
  WHEN 0 THEN "SEND_TO_HOME"
  WHEN 1 THEN "PICK_UP_IN_CENTER" 
  ELSE "UNKNOWN_IMPORT"
 END AS type,
 request.created_at AS issueDate,
 request.performedat AS handleDate,
 CASE request.status
 WHEN "new" THEN null
 WHEN "handled" THEN true
 END AS approved,
 user.cardNumber AS assignedCard
 FROM `crm`.`eurocommercial_crm_user_request` AS request
 INNER JOIN `Center` AS center ON center.id = request.center_id
 INNER JOIN `User` AS user ON user.id = request.user_id
 WHERE request.user_id IS NOT NULL;