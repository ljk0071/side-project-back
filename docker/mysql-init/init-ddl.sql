-- 문자셋 설정
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;
SET character_set_server = utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;
SET collation_server = utf8mb4_unicode_ci;

create table if not exists comment
(
    id       bigint auto_increment primary key comment '댓글 ID',
    contents varchar(500) not null comment '댓글 내용',
    board_id bigint       not null comment '게시판 ID',
    likes    bigint       not null comment '좋아요 수',
    dislikes bigint       not null comment '싫어요 수'
);

create table if not exists notice
(
    id          bigint auto_increment primary key comment '공지사항 ID',
    title       varchar(255) not null comment '게시글 제목',
    contents    text         not null comment '게시글 내용',
    likes       bigint       not null comment '좋아요 수',
    dislikes    bigint       not null comment '싫어요 수',
    created_at  timestamp    not null comment '생성일시',
    created_by  bigint       not null comment '생성자',
    modified_at timestamp    null comment '수정일시',
    modified_by bigint       null comment '수정자'
);

create table if not exists role
(
    id          varchar(30)  not null primary key comment '역할 ID',
    name        varchar(30)  not null comment '역할명',
    description varchar(255) null comment '설명',
    created_at  timestamp    not null comment '생성일시',
    created_by  bigint       not null comment '생성자',
    modified_at timestamp    null comment '수정일시',
    modified_by bigint       null comment '수정자'
);

create table if not exists role_hierarchy
(
    higher_role varchar(30) not null comment '상위 역할',
    lower_role  varchar(30) not null comment '하위 역할',
    primary key (higher_role, lower_role)
);

create table if not exists user
(
    unique_id    bigint auto_increment primary key comment '유저 고유 ID',
    user_id      varchar(50)                                     not null comment '유저 ID',
    password     varchar(255)                                    not null comment '비밀번호',
    name         varchar(50)                                     null comment '이름',
    phone_number varchar(15)                                     null comment '핸드폰번호',
    email        varchar(255)                                    not null comment '이메일',
    status       enum ('ACTIVE', 'DELETED', 'LOCKED', 'PENDING') not null comment '상태',
    type         enum ('ADMIN', 'NORMAL')                        not null comment '타입',
    description  text                                            null comment '설명',
    created_at   timestamp                                       not null comment '생성일시',
    created_by   bigint                                          not null comment '생성자',
    modified_at  timestamp                                       null comment '수정일시',
    modified_by  bigint                                          null comment '수정자'
);

create table if not exists user_role
(
    role_id        varchar(30) not null comment '역할 ID',
    user_unique_id bigint      not null comment '사용자 ID',
    created_at     timestamp   not null comment '생성일시',
    created_by     bigint      not null comment '생성자',
    modified_at    timestamp   null comment '수정일시',
    modified_by    bigint      null comment '수정자',
    primary key (role_id, user_unique_id)
);

insert into role (id, name, description, created_at, created_by)
values ('ROLE_ANONYMOUS', '익명 사용자', '로그인 하지 않은 사용자입니다.', now(), 1),
       ('ROLE_USER', '일반 사용자', null, now(), 1),
       ('ROLE_ADMIN', '관리자', null, now(), 1);

insert into role_hierarchy
values ('ROLE_USER', 'ROLE_ANONYMOUS'),
       ('ROLE_ADMIN', 'ROLE_USER');

insert into user_role (role_id, user_unique_id, created_at, created_by)
values ('ROLE_ADMIN', 1, now(), 1);

insert into user (user_id, password, name, email, status, type, created_at, created_by)
-- gmlwls@9833
values ('system', '$2a$10$feGvADMjJsX5ebEP7emA6eX7uO9w2sCCROTce0D0lXBqyW3XqXnEi', '시스템', '',
        'ACTIVE', 'ADMIN', now(), 1);