-- =====================================================
-- VIEW: posição atual do estoque
-- =====================================================
CREATE OR REPLACE VIEW vw_posicao_estoque AS
SELECT
    p.id                                   AS produto_id,
    p.nome                                 AS produto,
    p.sku                                  AS sku,
    f.nome                                 AS fornecedor,
    p.quantidade_atual                     AS quantidade_atual,
    p.estoque_minimo                       AS estoque_minimo,
    p.estoque_maximo                       AS estoque_maximo,
    p.preco                                AS preco,
    ROUND(p.preco * p.quantidade_atual, 2) AS valor_em_estoque,
    CASE
        WHEN p.quantidade_atual <= p.estoque_minimo      THEN 'BAIXO'
        WHEN p.quantidade_atual >= p.estoque_maximo * 0.9 THEN 'ALTO'
        ELSE 'NORMAL'
        END                                    AS situacao
FROM produto p
         JOIN fornecedor f ON f.id = p.fornecedor_id;


-- =====================================================
-- FUNCTION: giro de estoque dos últimos X dias
-- =====================================================
CREATE OR REPLACE FUNCTION fn_giro_estoque(p_dias INTEGER DEFAULT 30)
RETURNS TABLE (
    produto_id        BIGINT,
    produto           VARCHAR,
    sku               VARCHAR,
    quantidade_atual  INTEGER,
    total_entradas    BIGINT,
    total_saidas      BIGINT,
    giro              NUMERIC,
    dias_de_cobertura NUMERIC,
    classificacao     TEXT
)
LANGUAGE sql STABLE
AS $$
    WITH base AS (
        SELECT
            p.id,
            p.nome,
            p.sku,
            p.quantidade_atual,
            COALESCE(SUM(m.quantidade) FILTER (WHERE m.tipo = 'ENTRADA'), 0) AS entradas,
            COALESCE(SUM(m.quantidade) FILTER (WHERE m.tipo = 'SAIDA'), 0)   AS saidas
        FROM produto p
        LEFT JOIN movimentacao_estoque m
               ON m.produto_id = p.id
              AND m.data_hora >= LOCALTIMESTAMP - make_interval(days => p_dias)
        GROUP BY p.id, p.nome, p.sku, p.quantidade_atual
    )
SELECT
    b.id,
    b.nome,
    b.sku,
    b.quantidade_atual,
    b.entradas,
    b.saidas,
    ROUND(b.saidas::NUMERIC / NULLIF(b.quantidade_atual, 0), 2),
    ROUND(b.quantidade_atual::NUMERIC
              / NULLIF(b.saidas::NUMERIC / GREATEST(p_dias, 1), 0), 1),
    CASE
        WHEN b.saidas = 0                          THEN 'SEM_SAIDA'
        WHEN b.saidas >= b.quantidade_atual        THEN 'ALTO'
        WHEN b.saidas >= b.quantidade_atual * 0.3  THEN 'MEDIO'
        ELSE 'BAIXO'
        END
FROM base b
ORDER BY b.saidas DESC, b.nome;
$$;


-- =====================================================
-- PROCEDURE: conciliar o estoque com o histórico
-- =====================================================
CREATE OR REPLACE PROCEDURE sp_conciliar_estoque()
LANGUAGE plpgsql
AS $$
DECLARE
v_corrigidos INTEGER;
BEGIN
UPDATE produto p
SET quantidade_atual = s.saldo
    FROM (
            SELECT pr.id,
                   COALESCE(SUM(CASE WHEN m.tipo = 'ENTRADA' THEN m.quantidade
                                     ELSE -m.quantidade END), 0) AS saldo
              FROM produto pr
              LEFT JOIN movimentacao_estoque m ON m.produto_id = pr.id
             GROUP BY pr.id
           ) s
WHERE s.id = p.id
  AND p.quantidade_atual <> s.saldo;

GET DIAGNOSTICS v_corrigidos = ROW_COUNT;
RAISE NOTICE 'Produtos corrigidos: %', v_corrigidos;
END;
$$;