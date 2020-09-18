INSERT INTO `empresa` (`id`, `cnpj`, `data_atualizacao`, `data_criacao`, `razao_social`)
VALUES (null, '82198127000121', CURRENT_DATE(), CURRENT_DATE(), 'Ramon TI');

INSERT INTO `funcionario` (`id`, `cpf`, `data_atualizacao`, `data_criacao`, `email`, `nome`,
                   `perfil`, `qtd_horas_almoco`, `qtd_horas_trabalho_dia`, `senha`, `valor_hora`, `empresa_id`)
VALUES (null, '16248890935', CURRENT_DATE(), CURRENT_DATE(), 'admin@ramonti.com', 'ADMIN', 'ROLE_ADMIN', null, null,
        '$2b$10$cjInsJ50UNwmxcuanIdjv.vpmME5HjhTQzCN6OZHwGGhe9AOLbS3m', null, (SELECT `id` FROM `empresa` WHERE cnpj = '82198127000121'));