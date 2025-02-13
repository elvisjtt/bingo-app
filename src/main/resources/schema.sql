CREATE TABLE IF NOT EXISTS bingo_game (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,  
    status VARCHAR(50) NOT NULL,  
    called_numbers TEXT,  
    CHECK (status IN ('CREATED', 'IN_PROGRESS', 'FINISHED'))  
);

CREATE TABLE IF NOT EXISTS bingo_card (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,  
    game_id VARCHAR(255) NOT NULL,  
    numbers_json TEXT NOT NULL,  
    CONSTRAINT fk_game FOREIGN KEY (game_id) REFERENCES bingo_game(id)  
);

CREATE TABLE IF NOT EXISTS bingo_number (
    game_id VARCHAR(255) NOT NULL,  
    number INT NOT NULL,  
    column CHAR(1) NOT NULL,  
    CONSTRAINT fk_bingo_game FOREIGN KEY (game_id) REFERENCES bingo_game(id)  
);

CREATE TABLE IF NOT EXISTS bingo_card_check_request (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,  
    card_id UUID NOT NULL,  
    CONSTRAINT fk_bingo_card FOREIGN KEY (card_id) REFERENCES bingo_card(id)  
);

CREATE TABLE IF NOT EXISTS bingo_card_check_response (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,  
    request_id UUID NOT NULL,  
    is_winner BOOLEAN NOT NULL,  
    message TEXT,  
    CONSTRAINT fk_check_request FOREIGN KEY (request_id) REFERENCES bingo_card_check_request(id)  
);

