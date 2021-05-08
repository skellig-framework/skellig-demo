CREATE TABLE Event (
                       code VARCHAR(30) PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       date TIME NOT NULL,
                       location VARCHAR(255) NOT NULL,
                       pricePerSeats VARCHAR(255) NOT NULL,
                       takenSeats VARCHAR(255) NOT NULL
);

CREATE TABLE Booking (
                         id NUMERIC PRIMARY KEY,
                         seats VARCHAR(255) NOT NULL,
                         price DECIMAL NOT NULL,
                         time TIME NOT NULL,
                         status VARCHAR(64) NOT NULL,
                         paymentToken VARCHAR(128) NOT NULL,
                         eventCode VARCHAR(30),

                         CONSTRAINT fk_event_booking FOREIGN KEY (eventCode) REFERENCES Event(code)
)

