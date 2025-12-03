DROP DATABASE IF EXISTS cmpe343_project;

CREATE DATABASE cmpe343_project CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE cmpe343_project;

-- Create Database User
DROP USER IF EXISTS 'myuser'@'localhost';
CREATE USER 'myuser'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON cmpe343_project.* TO 'myuser'@'localhost';
FLUSH PRIVILEGES;

-- 1. Create Users Table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    role ENUM('TESTER', 'JUNIOR', 'SENIOR', 'MANAGER') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE activity_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action_type VARCHAR(50) NOT NULL, -- LOGIN, ADD, UPDATE, DELETE
    details TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 2. Create Contacts Table (with is_deleted for Soft Delete)
CREATE TABLE contacts (
    contact_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    phone_primary VARCHAR(20) NOT NULL,
    phone_secondary VARCHAR(20),
    email VARCHAR(100),
    linkedin_url VARCHAR(255),
    birth_date DATE,
    is_deleted BOOLEAN DEFAULT FALSE, -- Soft Delete Flag
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. Create Contact History Table (for Undo & Logging)
CREATE TABLE contact_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    contact_id INT NOT NULL,
    action_type ENUM('CREATE', 'UPDATE', 'DELETE', 'RESTORE') NOT NULL,
    changed_by_user_id INT,
    change_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    -- Snapshot of data
    first_name VARCHAR(50),
    middle_name VARCHAR(50),
    last_name VARCHAR(50),
    nickname VARCHAR(50),
    phone_primary VARCHAR(20),
    phone_secondary VARCHAR(20),
    email VARCHAR(100),
    linkedin_url VARCHAR(255),
    birth_date DATE,
    
    FOREIGN KEY (contact_id) REFERENCES contacts(contact_id),
    FOREIGN KEY (changed_by_user_id) REFERENCES users(user_id)
);



INSERT INTO users (username, password_hash, name, surname, role) VALUES 
('tt', '0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82', 'Test', 'Elemanı', 'TESTER'),
('jd', 'ad3e69e9aa860657cc6476770fe253d08198746b9fcf9dc3186b47eb85c30335', 'Junior', 'Dev', 'JUNIOR'),
('sd', '03042cf8100db386818cee4ff0f2972431a62ed78edbd09ac08accfabbefd818', 'Senior', 'Dev', 'SENIOR'),
('man', '48b676e2b107da679512b793d5fd4cc4329f0c7c17a97cf6e0e3d1005b600b03', 'Proje', 'Müdürü', 'MANAGER');

-- 5. Insert Sample Contacts (50 Records)
INSERT INTO contacts (first_name, middle_name, last_name, nickname, phone_primary, phone_secondary, email, linkedin_url, birth_date) VALUES 
('Ahmet', 'Can', 'Yılmaz', 'Hızlı', '05321112233', '05421112233', 'ahmet.yilmaz@mail.com', 'linkedin.com/in/ahmetyilmaz', '1990-05-15'),
('Ayşe', NULL, 'Kaya', 'Gül', '05552223344', NULL, 'ayse.kaya@test.com', 'linkedin.com/in/aysekaya', '1992-08-20'),
('Mehmet', 'Ali', 'Demir', 'Mali', '05443334455', '05333334455', 'mehmet.demir@ornek.com', NULL, '1985-01-10'),
('Fatma', NULL, 'Çelik', 'Fatoş', '05334445566', NULL, 'fatma.celik@sirket.com', 'linkedin.com/in/fatmacelik', '1995-03-25'),
('Mustafa', NULL, 'Şahin', 'Mıstık', '05055556677', '05555556677', 'mustafa.sahin@okul.edu.tr', NULL, '1998-11-12'),
('Zeynep', 'Su', 'Yıldız', 'Zey', '05326667788', NULL, 'zeynep.yildiz@mail.com', 'linkedin.com/in/zeynepyildiz', '1993-07-30'),
('Emre', NULL, 'Öztürk', 'Emo', '05427778899', '05327778899', 'emre.ozturk@tech.com', 'linkedin.com/in/emreozturk', '1991-09-05'),
('Elif', NULL, 'Arslan', 'Ela', '05558889900', NULL, 'elif.arslan@art.com', NULL, '1996-02-14'),
('Burak', 'Can', 'Doğan', 'Buro', '05339990011', '05449990011', 'burak.dogan@spor.com', 'linkedin.com/in/burakdogan', '1994-06-18'),
('Selin', NULL, 'Kılıç', 'Selo', '05050001122', NULL, 'selin.kilic@moda.com', 'linkedin.com/in/selinkilic', '1997-12-01'),
('Cem', NULL, 'Koç', 'Cemo', '05321113344', NULL, 'cem.koc@muzik.com', NULL, '1989-04-23'),
('Gamze', 'Nur', 'Kurt', 'Gamzeli', '05442224455', '05332224455', 'gamze.kurt@saglik.com', 'linkedin.com/in/gamzekurt', '1990-10-08'),
('Oğuz', NULL, 'Aydın', 'Ozi', '05553335566', NULL, 'oguz.aydin@yazilim.com', 'linkedin.com/in/oguzaydin', '1993-05-19'),
('Deniz', NULL, 'Bulut', 'Mavi', '05334446677', '05424446677', 'deniz.bulut@deniz.com', NULL, '1995-08-30'),
('Eren', 'Can', 'Güneş', 'Ero', '05055557788', NULL, 'eren.gunes@enerji.com', 'linkedin.com/in/erengunes', '1992-01-15'),
('İrem', NULL, 'Polat', 'İro', '05326668899', '05556668899', 'irem.polat@hukuk.com', 'linkedin.com/in/irempolat', '1994-11-22'),
('Kaan', NULL, 'Taş', 'Kaya', '05427779900', NULL, 'kaan.tas@insaat.com', NULL, '1988-03-10'),
('Esra', 'Gül', 'Aksoy', 'Es', '05558880011', '05338880011', 'esra.aksoy@banka.com', 'linkedin.com/in/esraaksoy', '1991-07-05'),
('Mert', NULL, 'Yalçın', 'Merto', '05339991122', NULL, 'mert.yalcin@otomotiv.com', 'linkedin.com/in/mertyalcin', '1996-09-14'),
('Buse', NULL, 'Korkmaz', 'Bus', '05050002233', '05440002233', 'buse.korkmaz@medya.com', NULL, '1993-12-25'),
('Onur', 'Ege', 'Çetin', 'One', '05321114455', NULL, 'onur.cetin@finans.com', 'linkedin.com/in/onurcetin', '1990-02-28'),
('Seda', NULL, 'Ünal', 'Sedo', '05442225566', '05332225566', 'seda.unal@turizm.com', 'linkedin.com/in/sedaunal', '1995-06-12'),
('Volkan', NULL, 'Erdoğan', 'Volki', '05553336677', NULL, 'volkan.erdogan@spor.com', NULL, '1987-10-03'),
('Gizem', 'Su', 'Şen', 'Gizi', '05334447788', '05424447788', 'gizem.sen@sanat.com', 'linkedin.com/in/gizemsen', '1994-04-18'),
('Barış', NULL, 'Yüksel', 'Baro', '05055558899', NULL, 'baris.yuksel@baris.com', 'linkedin.com/in/barisyuksel', '1992-08-09'),
('Pınar', NULL, 'Aslan', 'Pın', '05326669900', '05556669900', 'pinar.aslan@doga.com', NULL, '1991-01-20'),
('Serkan', 'Ali', 'Kara', 'Serko', '05427770011', NULL, 'serkan.kara@lojistik.com', 'linkedin.com/in/serkankara', '1989-11-05'),
('Hande', NULL, 'Bilgin', 'Han', '05558881122', '05338881122', 'hande.bilgin@egitim.com', 'linkedin.com/in/handebilgin', '1993-03-15'),
('Uğur', NULL, 'Sarı', 'Ugi', '05339992233', NULL, 'ugur.sari@teknoloji.com', NULL, '1996-07-28'),
('Melis', 'Naz', 'Bozkurt', 'Meli', '05050003344', '05440003344', 'melis.bozkurt@tasarim.com', 'linkedin.com/in/melisbozkurt', '1995-12-10'),
('Tolga', NULL, 'Güler', 'Tolg', '05321115566', NULL, 'tolga.guler@muhendis.com', 'linkedin.com/in/tolgaguler', '1990-09-22'),
('Derya', NULL, 'Avcı', 'Dery', '05442226677', '05332226677', 'derya.avci@denizcilik.com', NULL, '1994-02-05'),
('Hakan', 'Can', 'Tunç', 'Hako', '05553337788', NULL, 'hakan.tunc@guvenlik.com', 'linkedin.com/in/hakantunc', '1988-06-18'),
('Aslı', NULL, 'Koçak', 'As', '05334448899', '05424448899', 'asli.kocak@reklam.com', 'linkedin.com/in/aslikocak', '1993-10-30'),
('Murat', NULL, 'Özdemir', 'Muro', '05055559900', NULL, 'murat.ozdemir@ticaret.com', NULL, '1991-04-12'),
('Ezgi', 'Nur', 'Keskin', 'Ezo', '05326660011', '05556660011', 'ezgi.keskin@saglik.com', 'linkedin.com/in/ezgikeskin', '1996-08-25'),
('Sinan', NULL, 'Acar', 'Sino', '05427771122', NULL, 'sinan.acar@spor.com', 'linkedin.com/in/sinanacar', '1992-12-08'),
('Berna', NULL, 'Uçar', 'Bern', '05558882233', '05338882233', 'berna.ucar@havacilik.com', NULL, '1995-05-03'),
('Levent', 'Ali', 'Yavuz', 'Leo', '05339993344', NULL, 'levent.yavuz@yazilim.com', 'linkedin.com/in/leventyavuz', '1990-01-18'),
('Tuğçe', NULL, 'Çetin', 'Tuğ', '05050004455', '05440004455', 'tugce.cetin@egitim.com', 'linkedin.com/in/tugcecetin', '1994-09-28'),
('Kerem', NULL, 'Aktaş', 'Kero', '05321116677', NULL, 'kerem.aktas@oyun.com', NULL, '1993-02-14'),
('İpek', 'Su', 'Gür', 'İp', '05442227788', '05332227788', 'ipek.gur@moda.com', 'linkedin.com/in/ipekgur', '1996-06-20'),
('Okan', NULL, 'Duran', 'Oki', '05553338899', NULL, 'okan.duran@muzik.com', 'linkedin.com/in/okanduran', '1991-11-01'),
('Nazlı', NULL, 'Sönmez', 'Naz', '05334449900', '05424449900', 'nazli.sonmez@sanat.com', NULL, '1995-03-15'),
('Arda', 'Can', 'Tekin', 'Ar', '05055550011', NULL, 'arda.tekin@teknoloji.com', 'linkedin.com/in/ardatekin', '1992-07-25'),
('Ece', NULL, 'Vural', 'Ec', '05326661122', '05556661122', 'ece.vural@mimarlik.com', 'linkedin.com/in/ecevural', '1994-12-05'),
('Metin', NULL, 'Kurt', 'Meto', '05427772233', NULL, 'metin.kurt@spor.com', NULL, '1990-04-10'),
('Yasemin', 'Nur', 'Erol', 'Yas', '05558883344', '05338883344', 'yasemin.erol@saglik.com', 'linkedin.com/in/yaseminerol', '1993-08-22'),
('Fatih', NULL, 'Özkan', 'Fati', '05339994455', NULL, 'fatih.ozkan@egitim.com', 'linkedin.com/in/fatihozkan', '1996-01-05'),
('Ceren', NULL, 'Akyol', 'Cer', '05050005566', '05440005566', 'ceren.akyol@hukuk.com', NULL, '1991-05-18');
