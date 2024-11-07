DROP TABLE IF EXISTS BLAME;
DROP TABLE IF EXISTS REVIEW;
DROP TABLE IF EXISTS REPLY;
DROP TABLE IF EXISTS PICTURE;
DROP TABLE IF EXISTS SELECT_OUTFIT;
DROP TABLE IF EXISTS OUTFIT_STYLE;
DROP TABLE IF EXISTS OUTFIT_SITUATION;
DROP TABLE IF EXISTS SELECT_RECORD;
DROP TABLE IF EXISTS BOARD;
DROP TABLE IF EXISTS BAN;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS OUTFIT;
DROP TABLE IF EXISTS SITUATION;
DROP TABLE IF EXISTS STYLE;
DROP TABLE IF EXISTS `CONDITION`;
DROP TABLE IF EXISTS blame;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS reply;
DROP TABLE IF EXISTS picture;
DROP TABLE IF EXISTS select_outfit;
DROP TABLE IF EXISTS outfit_style;
DROP TABLE IF EXISTS outfit_situation;
DROP TABLE IF EXISTS select_record;
DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS ban;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS outfit;
DROP TABLE IF EXISTS situation;
DROP TABLE IF EXISTS style;
DROP TABLE IF EXISTS `condition`;

-- 부모 테이블부터 생성
CREATE TABLE style (
                       style_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                       style_name VARCHAR(50) NOT NULL COMMENT '캐주얼, 포멀, 스포티, 무관',
                       PRIMARY KEY (style_seq)
);

CREATE TABLE `condition` (
                             condition_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                             condition_name VARCHAR(50) NOT NULL COMMENT 'HOT, COLD, NORMAL',
                             PRIMARY KEY (condition_seq)
);

CREATE TABLE user (
                      user_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                      user_social_site VARCHAR(50) NOT NULL COMMENT 'KAKAO, NAVER, GOOGLE',
                      user_id VARCHAR(255) NOT NULL,
                      user_name VARCHAR(50) NOT NULL,
                      user_nickname VARCHAR(50) NULL,
                      user_phone_num VARCHAR(255) NULL,
                      user_birth_date DATE NULL,
                      user_gender VARCHAR(50) NULL COMMENT 'MALE, FEMALE',
                      user_state VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, BAN, DELETE',
                      reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      del_date DATETIME NULL,
                      user_auth VARCHAR(50) NOT NULL DEFAULT 'USER' COMMENT 'USER, ADMIN',
                      style_seq BIGINT NULL,
                      condition_seq BIGINT NULL,
                      PRIMARY KEY (user_seq)
);

CREATE TABLE outfit (
                        outfit_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                        outfit_name VARCHAR(50) NOT NULL,
                        outfit_weather INT NULL COMMENT '악세서리만',
                        outfit_temp_max DOUBLE NULL,
                        outfit_temp_min DOUBLE NULL,
                        outfit_category VARCHAR(50) NOT NULL COMMENT '상의, 하의, 아우터, 신발, 악세사리',
                        outfit_gender VARCHAR(50) NOT NULL COMMENT 'M, F, N (남, 여, 무관)',
                        outfit_level VARCHAR(50) NULL COMMENT '권장, 필수, 선택',
                        PRIMARY KEY (outfit_seq)
);

CREATE TABLE situation (
                           situation_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                           situation_name VARCHAR(50) NOT NULL COMMENT '일상, 여행, 운동, 데이트, 격식있는자리',
                           PRIMARY KEY (situation_seq)
);

-- 자식 테이블 생성
CREATE TABLE select_record (
                               select_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                               user_seq BIGINT NOT NULL,
                               situation_seq BIGINT NOT NULL,
                               select_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               custom_date DATETIME NOT NULL,
                               custom_location VARCHAR(50) NOT NULL,
                               weather_code VARCHAR(255) NOT NULL,
                               high_temp DOUBLE NOT NULL,
                               low_temp DOUBLE NOT NULL,
                               daily_temp DOUBLE NOT NULL,
                               cur_temp DOUBLE NOT NULL,
                               precipitation VARCHAR(255) NULL,
                               PRIMARY KEY (select_seq)
);

CREATE TABLE board (
                       board_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                       user_seq BIGINT NOT NULL,
                       board_title VARCHAR(50) NOT NULL,
                       board_content VARCHAR(1000) NOT NULL,
                       reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       up_date DATETIME NULL,
                       del_date DATETIME NULL,
                       board_is_blind BOOLEAN NOT NULL DEFAULT FALSE,
                       board_is_notice BOOLEAN NOT NULL DEFAULT FALSE COMMENT '관리자만 공지사항 작성 가능',
                       PRIMARY KEY (board_seq)
);

CREATE TABLE review (
                        review_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                        user_seq BIGINT NOT NULL,
                        select_seq BIGINT NOT NULL,
                        review_content VARCHAR(255) NOT NULL,
                        review_weather DECIMAL(10, 2) NULL,
                        review_location DECIMAL(10, 2) NULL,
                        review_blind BOOLEAN NOT NULL,
                        review_like_yn BOOLEAN NULL,
                        review_report INT NULL,
                        reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        up_date DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                        del_date DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (review_seq, user_seq)
);

CREATE TABLE reply (
                       reply_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                       board_seq BIGINT NOT NULL,
                       reply_user_seq BIGINT NOT NULL,
                       reply_content VARCHAR(1000) NOT NULL,
                       reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       del_date DATETIME NULL,
                       reply_is_blind BOOLEAN NOT NULL DEFAULT FALSE,
                       PRIMARY KEY (reply_seq)
);

CREATE TABLE picture (
                         picture_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                         picture_board_seq BIGINT NOT NULL,
                         picture_originename VARCHAR(255) NOT NULL,
                         picture_changedname VARCHAR(255) NOT NULL,
                         picture_url VARCHAR(255) NOT NULL,
                         picture_type VARCHAR(100) NOT NULL,
                         reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         del_date DATETIME NULL,
                         picture_is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                         PRIMARY KEY (picture_seq)
);

CREATE TABLE select_outfit (
                               select_outfit_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                               outfit_seq BIGINT NOT NULL,
                               select_seq BIGINT NOT NULL,
                               PRIMARY KEY (select_outfit_seq)
);

CREATE TABLE outfit_style (
                              select_style_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                              style_seq BIGINT NOT NULL,
                              outfit_seq BIGINT NOT NULL,
                              PRIMARY KEY (select_style_seq)
);

CREATE TABLE outfit_situation (
                                  outfit_situation_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                                  outfit_seq BIGINT NOT NULL,
                                  situation_seq BIGINT NOT NULL,
                                  PRIMARY KEY (outfit_situation_seq)
);

CREATE TABLE ban (
                     ban_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                     ban_user_seq BIGINT NOT NULL,
                     ban_startdate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     ban_enddate DATETIME NOT NULL,
                     ban_releasedate DATETIME NULL COMMENT '특별 사면되면 기입하는 컬럼',
                     PRIMARY KEY (ban_seq)
);

CREATE TABLE blame (
                       blame_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AUTO-INCREMENT',
                       blame_user_seq BIGINT NOT NULL,
                       blame_board_seq BIGINT NULL,
                       blame_reply_seq BIGINT NULL,
                       blame_review_seq BIGINT NOT NULL,
                       user_seq BIGINT NOT NULL,  -- 복합 외래 키로 사용할 user_seq 추가
                       blame_processingdate DATETIME NULL,
                       reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       up_date DATETIME NULL,
                       blame_status BOOLEAN NOT NULL DEFAULT TRUE COMMENT '처리 상태 (TRUE: 미처리, FALSE: 처리됨)',
                       PRIMARY KEY (blame_seq),
                       CONSTRAINT FK_blame_user FOREIGN KEY (blame_user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
                       CONSTRAINT FK_blame_board FOREIGN KEY (blame_board_seq) REFERENCES board (board_seq) ON DELETE CASCADE,
                       CONSTRAINT FK_blame_reply FOREIGN KEY (blame_reply_seq) REFERENCES reply (reply_seq) ON DELETE CASCADE,
                       CONSTRAINT FK_blame_review FOREIGN KEY (blame_review_seq, user_seq) REFERENCES review (review_seq, user_seq) ON DELETE CASCADE
);

-- 외래 키 제약 조건 추가 (ON DELETE CASCADE 적용)
ALTER TABLE user
    ADD CONSTRAINT FK_user_style FOREIGN KEY (style_seq) REFERENCES style (style_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_user_condition FOREIGN KEY (condition_seq) REFERENCES `condition` (condition_seq) ON DELETE CASCADE;

ALTER TABLE select_record
    ADD CONSTRAINT FK_select_record_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_select_record_situation FOREIGN KEY (situation_seq) REFERENCES situation (situation_seq) ON DELETE CASCADE;

ALTER TABLE board
    ADD CONSTRAINT FK_board_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE;

ALTER TABLE review
    ADD CONSTRAINT FK_review_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_review_select_record FOREIGN KEY (select_seq) REFERENCES select_record (select_seq) ON DELETE CASCADE;

ALTER TABLE reply
    ADD CONSTRAINT FK_reply_board FOREIGN KEY (board_seq) REFERENCES board (board_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_reply_user FOREIGN KEY (reply_user_seq) REFERENCES user (user_seq) ON DELETE CASCADE;

ALTER TABLE picture
    ADD CONSTRAINT FK_picture_board FOREIGN KEY (picture_board_seq) REFERENCES board (board_seq) ON DELETE CASCADE;

ALTER TABLE select_outfit
    ADD CONSTRAINT FK_select_outfit_outfit FOREIGN KEY (outfit_seq) REFERENCES outfit (outfit_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_select_outfit_select_record FOREIGN KEY (select_seq) REFERENCES select_record (select_seq) ON DELETE CASCADE;

ALTER TABLE outfit_style
    ADD CONSTRAINT FK_outfit_style_style FOREIGN KEY (style_seq) REFERENCES style (style_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_outfit_style_outfit FOREIGN KEY (outfit_seq) REFERENCES outfit (outfit_seq) ON DELETE CASCADE;

ALTER TABLE outfit_situation
    ADD CONSTRAINT FK_outfit_situation_outfit FOREIGN KEY (outfit_seq) REFERENCES outfit (outfit_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_outfit_situation_situation FOREIGN KEY (situation_seq) REFERENCES situation (situation_seq) ON DELETE CASCADE;

ALTER TABLE ban
    ADD CONSTRAINT FK_ban_user FOREIGN KEY (ban_user_seq) REFERENCES user (user_seq) ON DELETE CASCADE;