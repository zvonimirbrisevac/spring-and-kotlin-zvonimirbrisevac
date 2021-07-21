
CREATE  TABLE IF NOT EXISTS cars (
    id              BIGSERIAL PRIMARY KEY,
    addedDate       TEXT ,
    manufacturer    TEXT,
    productionYear  INTEGER ,
    serialNumber    TEXT
);

CREATE TABLE IF NOT EXISTS checkups (
    id          BIGSERIAL PRIMARY KEY,
    timeAndDate TEXT,
    workerName  TEXT,
    price       DECIMAL (8, 2),
    carId       BIGINT
);



