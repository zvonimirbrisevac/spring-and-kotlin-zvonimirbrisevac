CREATE TABLE IF NOT EXISTS cars (
    id              BIGSERIAL PRIMARY KEY,
    addedDate       DATE,
    manufacturer    VARCHAR (25),
    productionYear  INTEGER ,
    serialNumber    VARCHAR (20)
);

CREATE TABLE IF NOT EXISTS checkups (
    id          BIGSERIAL PRIMARY KEY,
    timeAndDate TIMESTAMP(0),
    workerName  VARCHAR (25),
    price       DECIMAL (8, 2),
    carId       BIGINT
);