-- Users table test data
-- Clear existing users first
DELETE
FROM users;

-- Insert unique users
INSERT INTO users (username, password, role)
VALUES ('coordinator', 'password', 'EVENT_COORDINATOR'),
       ('admin', 'password', 'ADMIN');


-- SQL Server compatible date format
-- Clear existing data in correct order to respect foreign key constraints
DELETE
FROM reminders;
DELETE
FROM event_schedules;
DELETE
FROM weddings;

INSERT INTO weddings (COUPLE_NAMES, WEDDING_DATE, venue, guest_count, budget, theme_preference, music_style, status,special_requirements)
VALUES ('John Smith & Jane Doe', '2025-12-15', 'Grand Ballroom', 150, 25000.00, 'Classic Elegance', 'Classical','Planned', 'Outdoor ceremony preferred'),
       ('Mike Johnson & Sarah Wilson', '2025-11-20', 'Garden Pavilion', 80, 15000.00, 'Rustic', 'Jazz', 'In Progress','Vegetarian menu required'),
       ('David Brown & Lisa Davis', '2025-10-30', 'Beach Resort', 200, 35000.00, 'Beach Theme', 'Pop', 'Ready','Beach ceremony at sunset'),
       ('Alex Chen & Maria Rodriguez', '2025-09-15', 'Mountain Lodge', 120, 18000.00, 'Rustic Elegance', 'Acoustic','In Progress', 'Mountain view ceremony'),
       ('James Wilson & Emily Taylor', '2025-08-22', 'Historic Mansion', 180, 32000.00, 'Vintage Glamour', 'Big Band','Planned', 'Historic venue with vintage')


-- Decoration inventory
-- Clear existing inventory first to prevent duplicates
DELETE
FROM decoration_inventory;

INSERT INTO decoration_inventory (item_name, category, quantity_available, quantity_reserved, condition_status,
                                  description)
VALUES ('White Roses (50 stems)', 'Flowers', 20, 5, 'Excellent', 'Fresh white roses for centerpieces'),
       ('Candles (Set of 12)', 'Lighting', 15, 3, 'Good', 'White pillar candles with holders'),
       ('Table Linens (White)', 'Tableware', 30, 8, 'Excellent', 'Premium white tablecloths'),
       ('String Lights (50ft)', 'Lighting', 10, 2, 'Good', 'Warm white LED string lights'),
       ('Vases (Crystal)', 'Centerpieces', 25, 6, 'Excellent', 'Crystal vases for flower arrangements'),
       ('Red Roses (50 stems)', 'Flowers', 15, 4, 'Excellent', 'Deep red roses for romantic themes'),
       ('Champagne Glasses (Set of 24)', 'Tableware', 12, 2, 'Excellent', 'Crystal champagne flutes'),
       ('Fairy Lights (100ft)', 'Lighting', 8, 1, 'Good', 'Warm white fairy lights for ambiance'),
       ('Candlesticks (Silver)', 'Centerpieces', 20, 3, 'Excellent', 'Antique silver candlesticks'),
       ('Table Runners (Gold)', 'Tableware', 25, 5, 'Excellent', 'Luxury gold table runners'),
       ('Hydrangeas (30 stems)', 'Flowers', 12, 2, 'Good', 'Blue and white hydrangeas'),
       ('Chandeliers (Crystal)', 'Lighting', 6, 1, 'Excellent', 'Large crystal chandeliers'),
       ('Lanterns (Vintage)', 'Lighting', 15, 3, 'Good', 'Vintage metal lanterns'),
       ('Peonies (40 stems)', 'Flowers', 10, 2, 'Excellent', 'Pink and white peonies'),
       ('Mirror Tiles (Set of 12)', 'Centerpieces', 8, 1, 'Good', 'Decorative mirror tiles for tables'),
       ('Ribbon (Satin)', 'Decorations', 30, 5, 'Excellent', 'Various colored satin ribbons'),
       ('Balloons (Metallic)', 'Decorations', 100, 15, 'Good', 'Gold and silver metallic balloons'),
       ('Candles (Taper)', 'Lighting', 50, 8, 'Excellent', 'White taper candles'),
       ('Fabric Swags (Chiffon)', 'Decorations', 20, 3, 'Good', 'White chiffon fabric swags');


-- Event Schedules (sample data for existing weddings)
-- Note: Wedding IDs will be auto-generated, so we'll use a different approach
-- These will be created through the application interface
