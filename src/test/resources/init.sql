-- == 초기 데이터 ===

-- 비번: 1234
INSERT INTO AA_USER (USER_ID, PASSWD, NAME, DELETE_FLAG)
	VALUES('boksl', '$2a$10$28DKQkHqi1WjK/5/KtQtL.Zeow7FACzUs5NLm79mZGXpw5vKPFDuy', '복슬이',  'N');
	
INSERT INTO AB_ROLE (ROLE_SEQ, ROLE_NAME, USERNAME)
	VALUES(1, 'ROLE_USER', 'boksl');

INSERT INTO AB_ROLE (ROLE_SEQ, ROLE_NAME, USERNAME)
	VALUES(2, 'ROLE_ADMIN', 'boksl');