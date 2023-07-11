INSERT IGNORE INTO signal_configurations (id, steps) VALUES
(1, '[{"action":"setUp"},{"action":"setAlgoParam","argument":[1,60]},{"action":"performCalc"},{"action":"submitToMarket"}]'),
(2, '[{"action":"reverse"},{"action":"setAlgoParam","argument":[1,80]},{"action":"submitToMarket"}]'),
(3, '[{"action":"setAlgoParam","argument":[1,90]},{"action":"setAlgoParam","argument":[2,15]},{"action":"performCalc"},{"action":"submitToMarket"}]'),
(4, '[{"action":"invalid","argument":[1,90]},{"action":"setAlgoParam","argument":[2,15]},{"action":"performCalc"},{"action":"submitToMarket"}]');
