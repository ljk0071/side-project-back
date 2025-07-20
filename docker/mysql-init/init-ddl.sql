-- 문자셋 설정
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;
SET character_set_server = utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;
SET collation_server = utf8mb4_unicode_ci;
SET time_zone = 'UTC';

create table role
(
    id          bigint primary key auto_increment comment '역할id',
    revision    bigint      not null comment '버전',
    code        varchar(30) not null comment '역할코드',
    name        varchar(50) not null comment '역할명',
    created_at  datetime    not null comment '생성일시',
    created_by  bigint      not null comment '생성자',
    modified_at datetime comment '수정일시',
    modified_by bigint comment '수정자'
);

create table role_modify_log
(
    log_id     bigint primary key auto_increment comment '역할id',
    id         bigint      not null comment '역할id',
    revision   bigint      not null comment '버전',
    code       varchar(30) not null comment '역할코드',
    name       varchar(30) not null comment '역할명',
    created_at datetime    not null comment '생성일시',
    created_by bigint      not null comment '생성자'
);

create table role_hierarchy
(
    id             bigint primary key auto_increment comment '역할관계id',
    revision       bigint   not null comment '버전',
    higher_role_id bigint   not null comment '상위역할id',
    lower_role_id  bigint   not null comment '하위역할id',
    created_at     datetime not null comment '생성일시',
    created_by     bigint   not null comment '생성자',
    modified_at    datetime comment '수정일시',
    modified_by    bigint comment '수정자'
);

create table role_hierarchy_modify_log
(
    log_id         bigint primary key auto_increment comment '역할관계id',
    id             bigint   not null comment '역할관계id',
    revision       bigint   not null comment '버전',
    higher_role_id bigint   not null comment '상위역할id',
    lower_role_id  bigint   not null comment '하위역할id',
    created_at     datetime not null comment '생성일시',
    created_by     bigint   not null comment '생성자'
);

create table user_role
(
    id             bigint primary key auto_increment comment '유저역할id',
    revision       bigint   not null comment '버전',
    status         char(1)  not null comment '상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)',
    role_id        bigint   not null comment '역할id',
    user_unique_id bigint   not null comment '사용자id',
    created_at     datetime not null comment '생성일시',
    created_by     bigint   not null comment '생성자',
    modified_at    datetime comment '수정일시',
    modified_by    bigint comment '수정자',
    deleted_at     datetime comment '삭제일시',
    deleted_by     bigint comment '삭제자',
    unique key uk_user_role_role_id_user_unique_id (role_id, user_unique_id)
);

create table user_role_modify_log
(
    log_id         bigint primary key auto_increment comment '유저역할로그id',
    id             bigint   not null comment '유저역할id',
    revision       bigint   not null comment '버전',
    status         char(1)  not null comment '상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)',
    role_id        bigint   not null comment '역할id',
    user_unique_id bigint   not null comment '사용자id',
    created_at     datetime not null comment '생성일시',
    created_by     bigint   not null comment '생성자'
);

create table user
(
    unique_id           bigint primary key auto_increment comment '유저고유id',
    revision            bigint       not null comment '버전',
    user_id             varchar(50)  not null comment '유저id',
    password            varchar(255) not null comment '비밀번호',
    password_updated_at datetime comment '패스워드변경일시',
    name                varchar(50)  not null comment '이름',
    email               varchar(255) null comment '이메일',
    status              char(1)      not null comment '상태:P(Pending),A(Active),L(Locked),S(Suspended),D(Deleted)',
    type                varchar(20)  not null comment '타입:ADMIN,NORMAL',
    description         varchar(255) comment '설명',
    created_at          datetime     not null comment '생성일시',
    created_by          bigint       not null comment '생성자',
    modified_at         datetime comment '수정일시',
    modified_by         bigint comment '수정자',
    deleted_at          datetime comment '삭제일시',
    deleted_by          bigint comment '삭제자',
    index idx_user_user_id (user_id),
    index idx_user_email (email),
    index idx_user_status (status)
);

create table user_modify_log
(
    log_id              bigint primary key auto_increment comment '유저로그id',
    unique_id           bigint       not null comment '유저고유id',
    revision            bigint       not null comment '버전',
    user_id             varchar(50)  not null comment '유저id',
    password            varchar(255) not null comment '비밀번호',
    password_updated_at datetime comment '패스워드변경일시',
    name                varchar(50)  not null comment '이름',
    email               varchar(255) comment '이메일',
    status              char(1)      not null comment '상태:P(Pending),A(Active),L(Locked),S(Suspended),D(Deleted)',
    type                varchar(20)  not null comment '타입:admin,normal',
    description         varchar(255) comment '설명',
    created_at          datetime     not null comment '생성일시',
    created_by          bigint       not null comment '생성자',
    index idx_user_modify_log_user_id (user_id),
    index idx_user_modify_log_email (email),
    index idx_user_modify_log_status (status)
);

create table user_discord_auth
(
    id                    bigint primary key auto_increment comment 'discord인증고유id',
    revision              bigint             not null comment '버전',
    user_unique_id        bigint             not null comment '유저고유id',
    discord_id            varchar(20) unique not null comment 'discord사용자id(snowflake)',
    discord_username      varchar(32)        not null comment 'discord사용자명',
    discord_discriminator varchar(4) comment 'discord태그번호(#1234)',
    discord_global_name   varchar(32) comment 'discord글로벌표시명',
    discord_email         varchar(255) comment 'discord이메일',
    discord_avatar        varchar(255) comment 'discord아바타해시',
    discord_verified      boolean comment 'discord이메일인증여부',
    created_at            datetime           not null comment '연동일시',
    created_by            bigint             not null comment '연동자',
    modified_at           datetime comment '수정일시',
    modified_by           bigint comment '수정자',
    index idx_user_discord_auth_discord_id (discord_id)
);

create table user_discord_auth_modify_log
(
    log_id                bigint primary key auto_increment comment 'discord인증로그id',
    id                    bigint             not null comment 'discord인증고유id',
    revision              bigint             not null comment '버전',
    user_unique_id        bigint             not null comment '유저고유id',
    discord_id            varchar(20) unique not null comment 'discord사용자id(snowflake)',
    discord_username      varchar(32)        not null comment 'discord사용자명',
    discord_discriminator varchar(4) comment 'discord태그번호(#1234)',
    discord_global_name   varchar(32) comment 'discord글로벌표시명',
    discord_email         varchar(255) comment 'discord이메일',
    discord_avatar        varchar(255) comment 'discord아바타해시',
    discord_verified      boolean comment 'discord이메일인증여부',
    created_at            datetime           not null comment '연동일시',
    created_by            bigint             not null comment '연동자',
    index idx_user_discord_auth_modify_log_discord_id (discord_id)
);

create table login_attempt_log
(
    id             bigint auto_increment,
    user_id        varchar(50) comment '시도한유저id',
    ip_address     varchar(45) not null comment 'ipv4/ipv6주소',
    user_agent     varchar(255) comment '브라우저정보',
    is_succeeded   boolean     not null comment '성공여부',
    failure_reason varchar(50) comment '실패사유:wrong_password,locked_account등',
    created_at     datetime    not null comment '시도일시',
    PRIMARY KEY (id, created_at),
    index idx_login_attempt_log_user_id_created_at (user_id, created_at),
    index idx_login_attempt_log_ip_address_created_at (ip_address, created_at)
);

create table notice_comment
(
    id                bigint primary key auto_increment comment '공지사항댓글id',
    revision          bigint       not null comment '버전',
    parent_comment_id bigint comment '부모댓글id',
    contents          varchar(255) not null comment '댓글내용',
    notice_id         bigint       not null comment '공지사항id',
    status            char(1)      not null comment '상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)',
    created_at        datetime     not null comment '생성일시',
    created_by        bigint       not null comment '생성자',
    modified_at       datetime comment '수정일시',
    modified_by       bigint comment '수정자',
    deleted_at        datetime comment '삭제일시',
    deleted_by        bigint comment '삭제자',
    index idx_notice_comment_notice_id (notice_id),
    index idx_notice_comment_parent_comment_id (parent_comment_id)
);

create table notice_comment_modify_log
(
    log_id            bigint primary key auto_increment comment '공지사항댓글로그id',
    id                bigint       not null comment '공지사항댓글id',
    revision          bigint       not null comment '버전',
    parent_comment_id bigint comment '부모댓글id',
    contents          varchar(255) not null comment '댓글내용',
    notice_id         bigint       not null comment '공지사항id',
    status            char(1)      not null comment '상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)',
    created_at        datetime     not null comment '생성일시',
    created_by        bigint       not null comment '생성자',
    index idx_notice_comment_modify_log_notice_id (notice_id),
    index idx_notice_comment_modify_log_parent_comment_id (parent_comment_id)
);

create table notice
(
    id          bigint primary key auto_increment comment '공지사항id',
    revision    bigint           not null comment '버전',
    title       varchar(100)     not null comment '게시글제목',
    contents    varchar(255)     not null comment '게시글내용',
    view_count  bigint default 0 not null comment '조회수',
    status      char(1)          not null comment '상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)',
    created_at  datetime         not null comment '생성일시',
    created_by  bigint           not null comment '생성자',
    modified_at datetime comment '수정일시',
    modified_by bigint comment '수정자',
    deleted_at  datetime comment '삭제일시',
    deleted_by  bigint comment '삭제자',
    index idx_notice_created_at (created_at),
    index idx_notice_status_created_at (status, created_at desc)
);

create table notice_modify_log
(
    log_id     bigint primary key auto_increment comment '공지사항로그id',
    id         bigint       not null comment '공지사항id',
    revision   bigint       not null comment '버전',
    title      varchar(100) not null comment '게시글제목',
    contents   varchar(255) not null comment '게시글내용',
    view_count bigint       not null comment '조회수',
    status     char(1)      not null comment '상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)',
    created_at datetime     not null comment '생성일시',
    created_by bigint       not null comment '생성자',
    index idx_notice_modify_log_created_at (created_at),
    index idx_notice_modify_log_status_created_at (status, created_at desc)
);

create table party_recruit
(
    id             bigint primary key auto_increment comment '파티모집글id',
    revision       bigint       not null comment '버전',
    user_unique_id bigint       not null comment '유저고유id',
    title          varchar(100) null comment '게시글제목',
    contents       varchar(255) not null comment '게시글내용',
    max_members    int          not null comment '최대모집인원',
    status         char(1)      not null comment '상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)',
    created_at     datetime     not null comment '생성일시',
    created_by     bigint       not null comment '생성자',
    modified_at    datetime comment '수정일시',
    modified_by    bigint comment '수정자',
    deleted_at     datetime comment '삭제일시',
    deleted_by     bigint comment '삭제자',
    index idx_party_recruit_user_unique_id (user_unique_id),
    index idx_party_recruit_status_created_at (status, created_at desc),
    index idx_party_recruit_recruit_status (status)
);

create table party_recruit_modify_log
(
    log_id         bigint primary key auto_increment comment '파티모집글로w그id',
    id             bigint       not null comment '파티모집글id',
    revision       bigint       not null comment '버전',
    user_unique_id bigint       not null comment '유저고유id',
    title          varchar(100) null comment '게시글제목',
    contents       varchar(255) not null comment '게시글내용',
    max_members    int          not null comment '최대모집인원',
    status         char(1)      not null comment '상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)',
    recruit_status varchar(20)  not null comment '모집상태:recruiting,closed,completed',
    created_at     datetime     not null comment '생성일시',
    created_by     bigint       not null comment '생성자',
    index idx_party_recruit_modify_log_user_unique_id (user_unique_id),
    index idx_party_recruit_modify_log_status_created_at (status, created_at desc),
    index idx_party_recruit_modify_log_recruit_status (recruit_status)
);

create table resume
(
    id             bigint primary key auto_increment comment '이력서id',
    revision       bigint       not null comment '버전',
    user_unique_id bigint       not null comment '유저고유id',
    status         char(1)      not null comment '상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)',
    contents       varchar(255) not null comment '이력서내용',
    created_at     datetime     not null comment '생성일시',
    created_by     bigint       not null comment '생성자',
    modified_at    datetime comment '수정일시',
    modified_by    bigint comment '수정자',
    deleted_at     datetime comment '삭제일시',
    deleted_by     bigint comment '삭제자',
    index idx_resume_user_unique_id_status (user_unique_id, status)
);

create table resume_modify_log
(
    log_id         bigint primary key auto_increment comment '이력서로그id',
    id             bigint comment '이력서id',
    revision       bigint       not null comment '버전',
    user_unique_id bigint       not null comment '유저고유id',
    status         char(1)      not null comment '상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)',
    contents       varchar(255) not null comment '이력서내용',
    created_at     datetime     not null comment '생성일시',
    created_by     bigint       not null comment '생성자',
    index idx_resume_modify_log_user_unique_id_status (user_unique_id, status)
);

create table party_application
(
    id               bigint primary key auto_increment comment '파티모집글과이력서매핑id',
    revision         bigint   not null comment '버전',
    party_recruit_id bigint   not null comment '파티모집글id',
    resume_id        bigint   not null comment '지원서id',
    status           char(1)  not null comment '지원상태:P(Pending),A(Accepted),R(Rejected),C(Canceled)',
    created_at       datetime not null comment '생성일시',
    created_by       bigint   not null comment '생성자',
    modified_at      datetime comment '수정일시',
    modified_by      bigint comment '수정자',
    unique key uk_party_application_party_recruit_id_resume_id (party_recruit_id, resume_id),
    index idx_party_application_party_recruit_id_status (party_recruit_id, status),
    index idx_party_application_resume_id_status (resume_id, status)
);

create table party_application_modify_log
(
    log_id           bigint primary key auto_increment comment '파티모집글과이력서매핑로그id',
    id               bigint   not null comment '파티모집글과이력서매핑id',
    revision         bigint   not null comment '버전',
    party_recruit_id bigint   not null comment '파티모집글id',
    resume_id        bigint   not null comment '지원서id',
    status           char(1)  not null comment '지원상태:P(Pending),A(Accepted),R(Rejected),C(Canceled)',
    created_at       datetime not null comment '지원일시',
    created_by       bigint   not null comment '생성자',
    index idx_party_application_party_recruit_id_status (party_recruit_id, status),
    index idx_party_application_resume_id_status (resume_id, status)
);

create table user_reaction
(
    id             bigint primary key auto_increment comment '유저반응id',
    user_unique_id bigint      not null comment '사용자id',
    target_type    varchar(20) not null comment '대상타입:notice,comment',
    target_id      bigint      not null comment '대상id(notice_id또는comment_id)',
    reaction_type  varchar(10) not null comment '반응타입:like,dislike',
    is_deleted     boolean     not null comment '취소여부',
    created_at     datetime    not null comment '반응일시',
    deleted_at     datetime comment '취소일시',
    unique key uk_user_reaction_user_unique_id_target_type_target_id (user_unique_id, target_type, target_id),
    index idx_user_reaction_target_type_target_id_reaction_type (target_type, target_id, reaction_type)
);

create table notification
(
    id             bigint auto_increment comment '알림id',
    user_unique_id bigint       not null comment '수신자',
    type           varchar(30)  not null comment '알림타입',
    title          varchar(100) not null comment '알림제목',
    message        varchar(255) not null comment '알림내용',
    target_type    varchar(20)  not null comment '관련타입:party,notice,comment',
    target_id      bigint       not null comment '관련id',
    is_read        boolean      not null comment '읽음여부',
    read_at        datetime comment '읽은시간',
    created_at     datetime     not null comment '생성일시',
    created_by     bigint       not null comment '생성자',
    deleted_at     datetime comment '삭제일시',
    deleted_by     bigint comment '삭제자',
    PRIMARY KEY (id, created_at),
    index idx_notification_user_unique_id_is_read_created_at (user_unique_id, is_read, created_at desc)
);

-- role_hierarchy: 역할의 계층 구조 정의
ALTER TABLE role_hierarchy
    ADD CONSTRAINT fk_role_hierarchy_role_higher FOREIGN KEY (higher_role_id) REFERENCES role (id);
ALTER TABLE role_hierarchy
    ADD CONSTRAINT fk_role_hierarchy_role_lower FOREIGN KEY (lower_role_id) REFERENCES role (id);

-- user_role: 사용자와 역할의 매핑
ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role (id);
ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_user FOREIGN KEY (user_unique_id) REFERENCES user (unique_id);

-- user_discord_auth: 사용자와 Discord 계정 연동 정보
ALTER TABLE user_discord_auth
    ADD CONSTRAINT fk_user_discord_auth_user FOREIGN KEY (user_unique_id) REFERENCES user (unique_id);

-- comment: 댓글의 계층 구조 및 공지사항과의 관계
ALTER TABLE notice_comment
    ADD CONSTRAINT fk_notice_comment_notice_comment FOREIGN KEY (parent_comment_id) REFERENCES notice_comment (id);
ALTER TABLE notice_comment
    ADD CONSTRAINT fk_notice_comment_notice FOREIGN KEY (notice_id) REFERENCES notice (id);

-- party_recruit: 파티 모집글과 작성자와의 관계
ALTER TABLE party_recruit
    ADD CONSTRAINT fk_party_recruit_user FOREIGN KEY (user_unique_id) REFERENCES user (unique_id);

-- resume: 이력서와 작성자와의 관계
ALTER TABLE resume
    ADD CONSTRAINT fk_resume_user FOREIGN KEY (user_unique_id) REFERENCES user (unique_id);

-- party_application: 파티 모집과 이력서(지원서)의 매핑
ALTER TABLE party_application
    ADD CONSTRAINT fk_party_application_party_recruit FOREIGN KEY (party_recruit_id) REFERENCES party_recruit (id);
ALTER TABLE party_application
    ADD CONSTRAINT fk_party_application_resume FOREIGN KEY (resume_id) REFERENCES resume (id);

-- user_reaction: 사용자의 반응(좋아요 등)과 반응 주체인 사용자와의 관계
ALTER TABLE user_reaction
    ADD CONSTRAINT fk_user_reaction_user FOREIGN KEY (user_unique_id) REFERENCES user (unique_id);

-- login_attempt_log 파티셔닝
ALTER TABLE login_attempt_log
    PARTITION BY RANGE (YEAR(created_at) * 100 + MONTH(created_at)) (
        PARTITION p2025_07 VALUES LESS THAN (202508),
        PARTITION p2025_08 VALUES LESS THAN (202509),
        PARTITION p2025_09 VALUES LESS THAN (202510),
        PARTITION p2025_10 VALUES LESS THAN (202511),
        PARTITION p2025_11 VALUES LESS THAN (202512),
        PARTITION p2025_12 VALUES LESS THAN (202601),
        PARTITION p2026_01 VALUES LESS THAN (202602),
        PARTITION pfuture VALUES LESS THAN MAXVALUE
        );

-- notification 파티셔닝
ALTER TABLE notification
    PARTITION BY RANGE (YEAR(created_at) * 100 + MONTH(created_at)) (
        PARTITION p2025_07 VALUES LESS THAN (202508),
        PARTITION p2025_08 VALUES LESS THAN (202509),
        PARTITION p2025_09 VALUES LESS THAN (202510),
        PARTITION p2025_10 VALUES LESS THAN (202511),
        PARTITION p2025_11 VALUES LESS THAN (202512),
        PARTITION p2025_12 VALUES LESS THAN (202601),
        PARTITION p2026_01 VALUES LESS THAN (202602),
        PARTITION pfuture VALUES LESS THAN MAXVALUE
        );

insert into role (revision, code, name, created_at, created_by)
    values (0, 'ROLE_ANONYMOUS', '익명 사용자', now(), 1),
           (0, 'ROLE_USER', '일반 사용자', now(), 1),
           (0, 'ROLE_ADMIN', '관리자', now(), 1);

insert into role_hierarchy(revision, higher_role_id, lower_role_id, created_at, created_by)
    values (0, 2, 1, now(), 1),
           (0, 3, 2, now(), 1);

insert into user (revision, user_id, password, name, email, status, type, created_at, created_by)
    values (0, 'system', '$2a$10$feGvADMjJsX5ebEP7emA6eX7uO9w2sCCROTce0D0lXBqyW3XqXnEi', '시스템', 'test@gmail.com',
            'A', 'ADMIN', now(), 1);

insert into user (revision, user_id, password, name, email, status, type, created_at, created_by)
    values (0, 'testUser', '$2a$10$/SibkoqGV/b.jexrNNNl5OgviGS7XcZKO1V6cqLBdWfuQYCUig7su', '테스트 유저', 'test@gmail.com',
            'A', 'NORMAL', now(), 1);

insert into user_role (revision, status, role_id, user_unique_id, created_at, created_by)
    values (0, 'Y', 3, 1, now(), 1);

insert into user_role (revision, status, role_id, user_unique_id, created_at, created_by)
    values (0, 'Y', 2, 2, now(), 1);