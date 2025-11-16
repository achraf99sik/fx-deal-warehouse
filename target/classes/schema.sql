CREATE TABLE IF NOT EXISTS fx_deals (
    deal_id UUID PRIMARY KEY,
    from_currency_iso_code VARCHAR(3) NOT NULL,
    to_currency_iso_code VARCHAR(3) NOT NULL,
    deal_timestamp TIMESTAMP NOT NULL,
    deal_amount DECIMAL(18, 6) NOT NULL
);