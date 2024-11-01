DROP TABLE IF EXISTS BLAME;
DROP TABLE IF EXISTS REVIEW;
DROP TABLE IF EXISTS REPLY;
DROP TABLE IF EXISTS PICTURE;
DROP TABLE IF EXISTS SELECT_CLOTHING;
DROP TABLE IF EXISTS OUTFIT_STYLE;
DROP TABLE IF EXISTS OUTFIT_SITUATION;
DROP TABLE IF EXISTS SELECT_RECORD;
DROP TABLE IF EXISTS BOARD;
DROP TABLE IF EXISTS OUTFIT;
DROP TABLE IF EXISTS STYLE;
DROP TABLE IF EXISTS SITUATION;
DROP TABLE IF EXISTS PREFERENCE;
DROP TABLE IF EXISTS BAN;
DROP TABLE IF EXISTS USER;

CREATE TABLE USER (
                      user_seq BIGINT NOT NULL,
                      user_social_site VARCHAR(50) NOT NULL COMMENT 'KAKAO, NAVER, GOOGLE',
                      user_id VARCHAR(255) NOT NULL,
                      user_name VARCHAR(50) NOT NULL,
                      user_nickname VARCHAR(50) NULL,
                      user_phone_num VARCHAR(255) NULL,
                      user_birth_date DATE NULL,
                      user_gender VARCHAR(50) NULL COMMENT 'MALE, FEMALE',
                      user_state VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, BAN, DELETE',
                      reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                      del_date DATETIME NULL,
                      user_auth VARCHAR(50) NOT NULL DEFAULT 'USER' COMMENT 'USER, ADMIN',
                      PRIMARY KEY (user_seq)
);

CREATE TABLE PREFERENCE (
                            user_seq BIGINT NOT NULL,
                            preference_condition VARCHAR(50) NOT NULL DEFAULT 'NORMAL' COMMENT 'HOT, COLD, NORMAL',
                            preference_style VARCHAR(50) NOT NULL DEFAULT 'NORMAL' COMMENT 'CASUAL, SPORTY, FORMAL, NORMAL',
                            PRIMARY KEY (user_seq)
);

CREATE TABLE OUTFIT (
                        outfit_seq BIGINT NOT NULL AUTO_INCREMENT,
                        outfit_name VARCHAR(50) NOT NULL,
                        outfit_weather INT NULL COMMENT '악세서리만',
                        outfit_temp_max DOUBLE NULL,
                        outfit_temp_min DOUBLE NULL,
                        outfit_category VARCHAR(50) NOT NULL COMMENT '상의, 하의, 아우터, 신발, 악세사리',
                        outfit_gender VARCHAR(50) NOT NULL COMMENT 'M, F, N (남, 여, 무관)',
                        outfit_level VARCHAR(50) NULL COMMENT '권장, 필수, 선택',
                        outfit_img VARCHAR(500) NOT NULL,
                        PRIMARY KEY (outfit_seq)
);

CREATE TABLE STYLE (
                       style_seq BIGINT NOT NULL AUTO_INCREMENT,
                       style_name VARCHAR(50) NOT NULL COMMENT '캐주얼, 포멀, 스포티',
                       PRIMARY KEY (style_seq)
);

CREATE TABLE SITUATION (
                           situation_seq BIGINT NOT NULL AUTO_INCREMENT,
                           situation_name VARCHAR(50) NOT NULL COMMENT '일상, 여행, 운동, 데이트, 격식있는자리',
                           PRIMARY KEY (situation_seq)
);

CREATE TABLE SELECT_RECORD (
                               select_seq BIGINT NOT NULL AUTO_INCREMENT,
                               user_seq BIGINT NOT NULL,
                               situation_seq BIGINT NOT NULL,
                               select_date DATETIME NOT NULL,
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

CREATE TABLE BOARD (
                       board_seq BIGINT NOT NULL AUTO_INCREMENT,
                       user_seq BIGINT NOT NULL,
                       board_title VARCHAR(50) NOT NULL,
                       board_content VARCHAR(1000) NOT NULL,
                       reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                       up_date DATETIME NULL,
                       del_date DATETIME NULL,
                       board_is_blind BOOLEAN NOT NULL DEFAULT FALSE,
                       board_is_notice BOOLEAN NOT NULL DEFAULT FALSE COMMENT '관리자만 공지사항 작성 가능',
                       PRIMARY KEY (board_seq)
);

CREATE TABLE REVIEW (
                        review_seq BIGINT NOT NULL,
                        user_seq BIGINT NOT NULL,
                        select_seq BIGINT NOT NULL,
                        review_content VARCHAR(255) NOT NULL,
                        review_weather DECIMAL(10, 2) NULL,
                        review_location DECIMAL(10, 2) NULL,
                        review_blind BOOLEAN NOT NULL,
                        review_like_yn BOOLEAN NULL,
                        review_report INT NULL,
                        reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                        up_date DATETIME NULL DEFAULT LOCALTIME,
                        del_date DATETIME NULL DEFAULT LOCALTIME,
                        PRIMARY KEY (review_seq, user_seq)
);

CREATE TABLE BLAME (
                       blame_seq BIGINT NOT NULL AUTO_INCREMENT,
                       blame_user_seq BIGINT NOT NULL,
                       blame_board_seq BIGINT NULL,
                       blame_reply_seq BIGINT NULL,
                       blame_review_seq BIGINT NULL,
                       blame_processingdate DATETIME NULL,
                       reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                       up_date DATETIME NULL,
                       blame_status BOOLEAN NOT NULL DEFAULT TRUE COMMENT '처리 상태 (TRUE: 미처리, FALSE: 처리됨)',
                       PRIMARY KEY (blame_seq)
);

CREATE TABLE REPLY (
                       reply_seq BIGINT NOT NULL AUTO_INCREMENT,
                       board_seq BIGINT NOT NULL,
                       reply_user_seq BIGINT NOT NULL,
                       reply_content VARCHAR(1000) NOT NULL,
                       reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                       del_date DATETIME NULL,
                       reply_is_blind BOOLEAN NOT NULL DEFAULT FALSE,
                       PRIMARY KEY (reply_seq)
);

CREATE TABLE PICTURE (
                         picture_seq BIGINT NOT NULL AUTO_INCREMENT,
                         picture_board_seq BIGINT NOT NULL,
                         picture_originename VARCHAR(255) NOT NULL,
                         picture_changedname VARCHAR(255) NOT NULL,
                         picture_url VARCHAR(255) NOT NULL,
                         picture_type VARCHAR(100) NOT NULL,
                         reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                         del_date DATETIME NULL,
                         picture_is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                         PRIMARY KEY (picture_seq)
);

CREATE TABLE SELECT_CLOTHING (
                                 select_clothing_seq BIGINT NOT NULL AUTO_INCREMENT,
                                 outfit_seq BIGINT NOT NULL,
                                 select_seq BIGINT NOT NULL,
                                 PRIMARY KEY (select_clothing_seq)
);

CREATE TABLE OUTFIT_STYLE (
                              select_style_seq BIGINT NOT NULL AUTO_INCREMENT,
                              style_seq BIGINT NOT NULL,
                              outfit_seq BIGINT NOT NULL,
                              PRIMARY KEY (select_style_seq)
);

CREATE TABLE OUTFIT_SITUATION (
                                  outfit_situation_seq BIGINT NOT NULL AUTO_INCREMENT,
                                  outfit_seq BIGINT NOT NULL,
                                  situation_seq BIGINT NOT NULL,
                                  PRIMARY KEY (outfit_situation_seq)
);

CREATE TABLE BAN (
                     ban_seq BIGINT NOT NULL AUTO_INCREMENT,
                     ban_user_seq BIGINT NOT NULL,
                     ban_startdate DATETIME NOT NULL DEFAULT LOCALTIME,
                     ban_enddate DATETIME NOT NULL,
                     ban_releasedate DATETIME NULL COMMENT '특별 사면되면 기입하는 컬럼',
                     PRIMARY KEY (ban_seq)
);


ALTER TABLE PREFERENCE ADD CONSTRAINT FK_user_TO_PREFERENCE_1 FOREIGN KEY (user_seq) REFERENCES USER (user_seq);
ALTER TABLE SELECT_RECORD ADD CONSTRAINT FK_user_TO_SELECT_RECORD FOREIGN KEY (user_seq) REFERENCES USER (user_seq);
ALTER TABLE SELECT_RECORD ADD CONSTRAINT FK_situation_TO_SELECT_RECORD FOREIGN KEY (situation_seq) REFERENCES SITUATION (situation_seq);
ALTER TABLE BOARD ADD CONSTRAINT FK_user_TO_BOARD FOREIGN KEY (user_seq) REFERENCES USER (user_seq);
ALTER TABLE REVIEW ADD CONSTRAINT FK_user_TO_REVIEW FOREIGN KEY (user_seq) REFERENCES USER (user_seq);
ALTER TABLE REVIEW ADD CONSTRAINT FK_select_TO_REVIEW FOREIGN KEY (select_seq) REFERENCES SELECT_RECORD (select_seq);
ALTER TABLE BLAME ADD CONSTRAINT FK_user_TO_BLAME FOREIGN KEY (blame_user_seq) REFERENCES USER (user_seq);
ALTER TABLE BLAME ADD CONSTRAINT FK_board_TO_BLAME FOREIGN KEY (blame_board_seq) REFERENCES BOARD (board_seq);
ALTER TABLE BLAME ADD CONSTRAINT FK_review_TO_BLAME FOREIGN KEY (blame_review_seq) REFERENCES REVIEW (review_seq);
ALTER TABLE REPLY ADD CONSTRAINT FK_board_TO_REPLY FOREIGN KEY (board_seq) REFERENCES BOARD (board_seq);
ALTER TABLE REPLY ADD CONSTRAINT FK_user_TO_REPLY FOREIGN KEY (reply_user_seq) REFERENCES USER (user_seq);
ALTER TABLE PICTURE ADD CONSTRAINT FK_board_TO_PICTURE FOREIGN KEY (picture_board_seq) REFERENCES BOARD (board_seq);
ALTER TABLE SELECT_CLOTHING ADD CONSTRAINT FK_outfit_TO_SELECT_CLOTHING FOREIGN KEY (outfit_seq) REFERENCES OUTFIT (outfit_seq);
ALTER TABLE SELECT_CLOTHING ADD CONSTRAINT FK_select_TO_SELECT_CLOTHING FOREIGN KEY (select_seq) REFERENCES SELECT_RECORD (select_seq);
ALTER TABLE OUTFIT_STYLE ADD CONSTRAINT FK_style_TO_OUTFIT_STYLE FOREIGN KEY (style_seq) REFERENCES STYLE (style_seq);
ALTER TABLE OUTFIT_STYLE ADD CONSTRAINT FK_outfit_TO_OUTFIT_STYLE FOREIGN KEY (outfit_seq) REFERENCES OUTFIT (outfit_seq);
ALTER TABLE OUTFIT_SITUATION ADD CONSTRAINT FK_outfit_TO_OUTFIT_SITUATION FOREIGN KEY (outfit_seq) REFERENCES OUTFIT (outfit_seq);
ALTER TABLE OUTFIT_SITUATION ADD CONSTRAINT FK_situation_TO_OUTFIT_SITUATION FOREIGN KEY (situation_seq) REFERENCES SITUATION (situation_seq);
ALTER TABLE BAN ADD CONSTRAINT FK_user_TO_BAN FOREIGN KEY (ban_user_seq) REFERENCES USER (user_seq);