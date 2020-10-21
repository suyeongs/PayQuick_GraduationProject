/* 회원별 장바구니 상품 목록 */
CREATE TABLE `cart` (
	`cus_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`code_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`cus_id`, `code_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

/* 가입된 회원 정보 */
CREATE TABLE `customers` (
	`cus_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`cus_pw` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`cus_name` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`cus_phone` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`cus_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

/* 신제품 목록 */
CREATE TABLE `new` (
	`code_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`own_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`goods_name` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`size` VARCHAR(50) NULL DEFAULT 'F' COLLATE 'utf8_general_ci',
	`color` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`goods_img` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`price` INT(11) NOT NULL DEFAULT '0',
	`id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`code_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

/* 등록된 사업자 정보 */
CREATE TABLE `owners` (
	`own_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`own_pw` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`own_name` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`own_phone` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`biz_number` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`location` VARCHAR(50) NULL DEFAULT '' COLLATE 'utf8_general_ci',
	`biz_name` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`own_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

/* 회원별 결제 정보 */
CREATE TABLE `payinfo` (
	`pay_time` DATETIME NULL DEFAULT NULL,
	`pay_number` INT(11) NOT NULL AUTO_INCREMENT,
	`cus_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`own_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`code_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`pay_number`) USING BTREE,
	INDEX `FK__sold__pay` (`code_id`) USING BTREE,
	INDEX `FK__owners__pay` (`own_id`) USING BTREE,
	INDEX `FK__customers__pay` (`cus_id`) USING BTREE,
	CONSTRAINT `FK__customers__pay` FOREIGN KEY (`cus_id`) REFERENCES `payquick_db`.`customers` (`cus_id`) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT `FK__owners__pay` FOREIGN KEY (`own_id`) REFERENCES `payquick_db`.`owners` (`own_id`) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT `FK__sold__pay` FOREIGN KEY (`code_id`) REFERENCES `payquick_db`.`sold` (`code_id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=146
;

/* 판매된 상품 목록 */
CREATE TABLE `sold` (
	`code_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`own_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`goods_name` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`size` VARCHAR(50) NULL DEFAULT 'F' COLLATE 'utf8_general_ci',
	`color` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`goods_img` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`price` INT(11) NOT NULL DEFAULT '0',
	`id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`code_id`) USING BTREE,
	INDEX `FK__owners__sold` (`own_id`) USING BTREE,
	CONSTRAINT `FK__owners__sold` FOREIGN KEY (`own_id`) REFERENCES `payquick_db`.`owners` (`own_id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

/* 상품 재고 목록 */
CREATE TABLE `stock` (
	`code_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`own_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`goods_name` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`size` VARCHAR(50) NULL DEFAULT 'F' COLLATE 'utf8_general_ci',
	`color` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`goods_img` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`price` INT(11) NOT NULL DEFAULT '0',
	`id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`code_id`) USING BTREE,
	INDEX `FK__owners__stock` (`own_id`) USING BTREE,
	CONSTRAINT `FK__owners__stock` FOREIGN KEY (`own_id`) REFERENCES `payquick_db`.`owners` (`own_id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

/* 구매한 상품 모두 환불 처리하기 위한 임시 테이블  */
CREATE TABLE `tmp_stock` (
	`code_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`own_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`goods_name` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`size` VARCHAR(50) NULL DEFAULT 'F' COLLATE 'utf8_general_ci',
	`color` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`goods_img` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`price` INT(11) NOT NULL DEFAULT '0',
	`id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`code_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
