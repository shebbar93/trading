INSERT IGNORE INTO signal_configurations (id, steps) VALUES
(1, '[{"action":"setUp"},{"action":"setAlgoParam","param":1,"value":60},{"action":"performCalc"},{"action":"submitToMarket"}]'),
(2, '[{"action":"reverse"},{"action":"setAlgoParam","param":1,"value":80},{"action":"submitToMarket"}]'),
(3, '[{"action":"setAlgoParam","param":1,"value":90},{"action":"setAlgoParam","param":2,"value":15},{"action":"performCalc"},{"action":"submitToMarket"}]');
