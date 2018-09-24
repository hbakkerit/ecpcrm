USE `passy-plaza-import`;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `passy-plaza-import`.`Center` AS
SELECT
 center.id AS id,
 center.name AS name,
 center.code AS code,
 "d9d3ad35f1" AS mailchimpListId,
 "accueil@passyplaza.com" AS contactEmail
FROM `crm-import`.`Center` AS center
WHERE code = 016;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `passy-plaza-import`.`User` AS
SELECT *
FROM `crm-import`.`User` AS user
WHERE user.centerCode = 016;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `passy-plaza-import`.`Card` AS
SELECT *
FROM `crm-import`.`Card` AS card
WHERE card.centerCode = 016;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `passy-plaza-import`.`Child` AS
SELECT child.*
FROM `crm-import`.`Child` AS child
INNER JOIN `crm-import`.`User` AS parent ON child.parentReference = parent.reference
WHERE parent.centerCode = 016;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `passy-plaza-import`.`CardRequest` AS
SELECT *
FROM `crm-import`.`CardRequest` AS cardRequest
WHERE cardRequest.centerCode = 016;
