-- Drop user "BricksCollector_User"
REVOKE ALL PRIVILEGES ON *.* FROM "BricksCollector_User"@"localhost";
DROP USER "BricksCollector_User"@"localhost";
-- Drop and create database "BricksCollector"
DROP DATABASE BricksCollector;
CREATE DATABASE BricksCollector;
-- Use database "BricksCollector", for creating the tables
USE BricksCollector;
-- Create table "ColourTypes"
CREATE TABLE ColourTypes(
    ID SMALLINT PRIMARY KEY,
    Description CHAR(16) NOT NULL UNIQUE,
    InProduction BOOLEAN DEFAULT 1
);
-- Insert records into table "ColourTypes"
INSERT INTO ColourTypes(ID, Description, InProduction) VALUES 
    (1, "Solid", 1),
    (2, "Transparent", 1),
    (3, "Chrome", 1),
    (4, "Pearl", 1),
    (5, "Satin", 1),
    (6, "Metallic", 1),
    (7, "Milky", 1),
    (8, "Glitter", 1),
    (9, "Speckle", 0),
    (10, "Modulex", 0);
-- Create table "Currencies"
CREATE TABLE Currencies(
    ID SMALLINT PRIMARY KEY,
    Description CHAR(24) NOT NULL UNIQUE,
    UnicodeSymbol CHAR(1) CHARACTER SET utf8mb4 NOT NULL
);
-- Insert records into table "Currencies"
INSERT INTO Currencies(ID, Description, UnicodeSymbol) VALUES 
    (0, "Unknown Currency", '¤'),
    (1, "US Dollar", '$'),
    (2, "GB Sterling Pound", '£'),
    (3, "Japanese Yen", '¥'),
    (4, "EU Eurozone Euro", '€');
-- Create table "ItemDefects"
CREATE TABLE ItemDefects(
    ID SMALLINT PRIMARY KEY,
    Description CHAR(32) NOT NULL UNIQUE,
    AppliesTo CHAR(8) NOT NULL DEFAULT "Bricks"   -- The options are: "Bricks" or "Paper"
);
-- Insert records into table "ItemDefects"
INSERT INTO ItemDefects(ID, Description, AppliesTo) VALUES 
    (1, "Bent or Warped", "Bricks"),
    (2, "Bite Marks", "Bricks"),
    (3, "Broken", "Bricks"),
    (4, "Broken, Missing Parts", "Bricks"),
    (5, "Broken, Glued", "Bricks"),
    (6, "Cracks", "Bricks"),
    (7, "Cracks: Hairline", "Bricks"),
    (8, "Cracks: Small", "Bricks"),
    (9, "Cracks: Large", "Bricks"),
    (10, "Damaged Sticker", "Bricks"),
    (11, "Damaged Sticker: Little", "Bricks"),
    (12, "Damaged Sticker: Very", "Bricks"),
    (13, "Dents", "Bricks"),
    (14, "Dirty", "Bricks"),
    (15, "Dirty: Very", "Bricks"),
    (16, "Discolouration/Yellowing", "Bricks"),
    (17, "Discolouration/Yellowing: Little", "Bricks"),
    (18, "Discolouration/Yellowing: Very", "Bricks"),
    (19, "Fading Print", "Bricks"),
    (20, "Fading Print: Little", "Bricks"),
    (21, "Fading Print: Very", "Bricks"),
    (22, "Ink Marks", "Bricks"),
    (23, "Melted", "Bricks"),
    (24, "Play-Ware", "Bricks"),
    (25, "Rusted Metal Axle/Spring", "Bricks"),
    (26, "Scratches", "Bricks"),
    (27, "Scratches: Deep", "Bricks"),
    (28, "Scratches: Superficial", "Bricks"),
    (29, "Stress Marks", "Bricks"),
    (30, "Worn Chrome", "Bricks"),
    (31, "Worn Chrome: Little", "Bricks"),
    (32, "Worn Chrome: Very", "Bricks"),
    (33, "Glue Residue", "Bricks"),
    (100, "Creased", "Paper"),
    (101, "Dog Eared", "Paper"),
    (102, "Wrinkled", "Paper"),
    (103, "Torn", "Paper"),
    (104, "Torn, Missing Parts", "Paper"),
    (105, "Torn + Sticky Tape", "Paper"),
    (106, "Faded Print", "Paper"),
    (107, "Stained", "Paper"),
    (108, "Water Damaged", "Paper");
-- Create table "Marketplaces"
CREATE TABLE Marketplaces(
    ID SMALLINT AUTO_INCREMENT PRIMARY KEY,
    Description CHAR(32) CHARACTER SET utf8mb4 NOT NULL UNIQUE
);
-- Insert records into table "Marketplaces"
INSERT INTO Marketplaces(Description) VALUES 
    ("eBay"),
    ("BrickLink"),
    ("Brick Owl");
-- Create table "Stores"
CREATE TABLE Stores(
    ID SMALLINT AUTO_INCREMENT PRIMARY KEY,
    Description CHAR(32) CHARACTER SET utf8mb4 NOT NULL UNIQUE
);
-- Create table "Colours"
CREATE TABLE Colours(
    BriclinkID SMALLINT PRIMARY KEY,
    BriclinkDescription CHAR(32) NOT NULL UNIQUE,
    BriclinkTypeID SMALLINT NOT NULL DEFAULT 0,
    LegoID SMALLINT NOT NULL UNIQUE,
    LegoDescription CHAR(32) NOT NULL UNIQUE,
    ProductionYearStart SMALLINT  NOT NULL DEFAULT (YEAR(CURRENT_DATE())),
    ProductionYearEnd SMALLINT DEFAULT NULL,
    NumOfParts INT DEFAULT NULL,  -- Total number of parts made in this colour
    NumOfSets INT DEFAULT NULL    -- Total number of sets that contain parts in this colour
);
-- Create indexes and constrains for table "Colours" fields
-- CREATE INDEX IDX_Colours ON Colours ( BriclinkDescription );
-- ALTER TABLE
--     Colours ADD CONSTRAINT UC_Colours UNIQUE(
--         BriclinkID,
--         BriclinkDescription,
--         LegoID,
--         LegoDescription
--     );
-- ALTER TABLE
--     Colours ADD CONSTRAINT PK_Colours PRIMARY KEY(BriclinkID);
ALTER TABLE
    Colours ADD CONSTRAINT FK_Colours_ColourTypes FOREIGN KEY(BriclinkTypeID) REFERENCES ColourTypes(ID);
-- Create table "Orders"
CREATE TABLE Orders(
    ID SMALLINT AUTO_INCREMENT PRIMARY KEY,
    MarketplaceID SMALLINT NOT NULL,
    StoreID SMALLINT NOT NULL,
    BuyDate DATE  NOT NULL DEFAULT (CURRENT_DATE()),
    Total DECIMAL(7,2) NOT NULL DEFAULT 0.00,     -- Total order cost
    Shipping DECIMAL(7,2) NOT NULL DEFAULT 0.00,  -- Shipping cost
    Packaging DECIMAL(7,2) NOT NULL DEFAULT 0.00, -- Handling and Packaging cost
    CurrencyID SMALLINT NOT NULL DEFAULT 0
);
-- Create indexes and constrains for table "Orders" fields
ALTER TABLE
    Orders ADD CONSTRAINT FK_Orders_Marketplaces FOREIGN KEY(MarketplaceID) REFERENCES Marketplaces(ID);
ALTER TABLE
    Orders ADD CONSTRAINT FK_Orders_Stores FOREIGN KEY(StoreID) REFERENCES Stores(ID);
ALTER TABLE
    Orders ADD CONSTRAINT FK_Orders_Currencies FOREIGN KEY(CurrencyID) REFERENCES Currencies(ID);
-- Create user "BricksCollector_User" and give him access to all "BricksCollector" tables
CREATE USER "BricksCollector_User"@"localhost" IDENTIFIED BY "SecretPassword2!";
GRANT INSERT, UPDATE, DELETE, SELECT, CREATE TEMPORARY TABLES, EXECUTE ON BricksCollector.* TO "BricksCollector_User"@"localhost";

