CREATE TABLE account
(
    id                         BIGINT AUTO_INCREMENT NOT NULL,
    created_at                 datetime              NULL COMMENT '생성일',
    updated_at                 datetime              NOT NULL COMMENT '수정일',
    uuid                       VARCHAR(255)          NOT NULL,
    username                   VARCHAR(255)          NOT NULL,
    password                   VARCHAR(255)          NOT NULL,
    email                      VARCHAR(255)          NOT NULL,
    nickname                   VARCHAR(255)          NOT NULL,
    roles                      VARCHAR(255)          NOT NULL,
    last_login_at              datetime              NULL,
    last_logout_at             datetime              NULL,
    is_account_non_expired     BIT(1)                NULL,
    is_account_non_locked      BIT(1)                NULL,
    is_credentials_non_expired BIT(1)                NULL,
    is_enabled                 BIT(1)                NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);