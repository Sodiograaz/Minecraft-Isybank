CREATE TABLE IF NOT EXISTS users (
		username VARCHAR(16) NOT NULL,
		userId VARCHAR(64) NOT NULL,
		PRIMARY KEY (userId)
);

CREATE TABLE IF NOT EXISTS users_bank_data (
		userId VARCHAR(64) NOT NULL,
		bank_availability BIGINT NOT NULL,
		bankId VARCHAR(64) NOT NULL,
		FOREIGN KEY (userId) REFERENCES users(userId)
);

CREATE TABLE IF NOT EXISTS users_bank_payments_staff (
	moderator VARCHAR(64) NOT NULL REFERENCES users(userId),
	modifiedUser VARCHAR(64) NOT NULL REFERENCES users(userId),
	paymentId VARCHAR(64) NOT NULL,
	bill bigint NOT NULL DEFAULT 0 -- Added, Removed or Set to n
);

CREATE TABLE IF NOT EXISTS users_bank_payments (
		holder VARCHAR(64) NOT NULL REFERENCES users_bank_data(bankId),
		payee VARCHAR(64) NOT NULL REFERENCES users_bank_data(bankId),
		paymentId VARCHAR(64) NOT NULL,
		'transaction' bigint not null default 0,
		transaction_description TINYTEXT not null default 'No description provided',
		gross_bill bigint not null default 0,
		vat bigint not null default 0,
		PRIMARY KEY(paymentId)
);