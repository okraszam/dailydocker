use dailydockerdb;

create table if not exists searches
(
    id int NOT NULL AUTO_INCREMENT,
    searchType varchar(100) NOT NULL,
    beginning varchar(20) NOT NULL,
    end varchar(20) NOT NULL,
    searchDate varchar(100) NOT NULL,
    `asc` bit not null,
    PRIMARY KEY (id)
);

create table if not exists scores
(
    id int NOT NULL AUTO_INCREMENT,
    `index` int NOT NULL,
    date varchar(20) NOT NULL,
    numbersString varchar(20) NOT NULL,
    PRIMARY KEY (id)
);