-- Tabela de vendedores
CREATE TABLE IF NOT EXISTS sellers (
    id   UUID PRIMARY KEY,
    name TEXT NOT NULL
);

-- Tabela de header das condições comerciais
CREATE TABLE IF NOT EXISTS commercial_conditions_header (
    seller_id        UUID PRIMARY KEY,
    created_at       TIMESTAMP NOT NULL,
    created_by       TEXT,
    current_version_id UUID,
    CONSTRAINT fk_header_seller FOREIGN KEY (seller_id) REFERENCES sellers(id)
);

-- Tabela de snapshots (histórico de versões)
CREATE TABLE IF NOT EXISTS commercial_conditions_snapshot (
    version_id         UUID PRIMARY KEY,
    seller_id          UUID NOT NULL,
    status             VARCHAR(50) NOT NULL,
    commercial_conditions JSONB NOT NULL,
    created_at         TIMESTAMP NOT NULL,
    created_by         TEXT,
    change_reason      TEXT,
    previous_version_id UUID,
    CONSTRAINT fk_snapshot_seller FOREIGN KEY (seller_id) REFERENCES sellers(id),
    CONSTRAINT fk_snapshot_previous FOREIGN KEY (previous_version_id) REFERENCES commercial_conditions_snapshot(version_id)
);
