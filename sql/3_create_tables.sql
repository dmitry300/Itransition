create table users
(
    id              bigint       not null auto_increment,
    activated       bit          not null,
    password        varchar(255) not null,
    activation_code varchar(255) null,
    user_name       varchar(30)  not null unique,
    email           varchar(255) not null unique,
    constraint PK_users PRIMARY KEY (id)
);

create table collections
(
    id          bigint       not null auto_increment,
    name        varchar(20)  not null,
    topic       varchar(20)  not null,
    description varchar(255) not null,
    file_name   varchar(255) not null,
    user_id     bigint,
    constraint PK_collections primary key (id),
    constraint FK_collectionsUsers
        foreign key (user_id) references users (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);
create table items
(
    id            bigint       not null auto_increment,
    name          varchar(20)  not null,
    tag           varchar(20)  not null,
    text1         varchar(255),
    text2         varchar(255),
    text3         varchar(255),
    date1         date,
    date2         date,
    date3         date,
    file_name     varchar(255) not null,
    collection_id bigint,
    constraint PK_items primary key (id),
    constraint FK_itemsCollections
        foreign key (collection_id) references collections (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);
create table user_role
(
    user_id bigint       not null,
    roles   varchar(255) not null,
    constraint FK_user_roleUsers
        foreign key (user_id) references users (id)
);

create table item_likes
(
    item_id bigint not null,
    user_id bigint not null,
    primary key (item_id, user_id),
    constraint FK_item_likesUsers foreign key (user_id)
        references users (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    constraint FK_item_likesItems foreign key (item_id)
        references items (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE comments
(
    id      bigint       NOT NULL AUTO_INCREMENT,
    text    VARCHAR(180) NOT NULL,
    date    timestamp    NOT NULL,
    user_id bigint       NOT NULL,
    item_id bigint       NOT NULL,
    CONSTRAINT PK_comments PRIMARY KEY (id),
    CONSTRAINT FK_commentsItems FOREIGN KEY (item_id)
        REFERENCES items (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT FK_commentsUsers FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);