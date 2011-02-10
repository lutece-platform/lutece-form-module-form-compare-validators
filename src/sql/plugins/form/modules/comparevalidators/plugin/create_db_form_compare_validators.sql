--
-- table structure for form_compare_validators_operator
--
CREATE TABLE form_compare_validators_operator
(
	id_operator INT NOT NULL,
	name VARCHAR(20) NOT NULL,
	class VARCHAR(255) NOT NULL,
	PRIMARY KEY(id_operator)
);

--
-- table structure for form_compare_validators_rule
--
CREATE TABLE form_compare_validators_rule
(
	id_rule INT NOT NULL,
	id_form INT NOT NULL,
	id_entry_1 INT NOT NULL,
	id_entry_2 INT NOT NULL,
	id_operator INT NOT NULL,
	PRIMARY KEY(id_rule)
);
