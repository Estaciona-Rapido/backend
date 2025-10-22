INSERT INTO scenario(scenario_name, capacity, scenario_start, scenario_end) VALUES ('dia-a-dia', 30, '1970-01-01 00:00:00-03', '9999-12-31 23:59:59-03');
INSERT INTO price_model(model_name, price, frequency_type, id_scenario) VALUES ('horista', 4, 'HOUR', 1);
INSERT INTO business_hour (start_week_day, end_week_day, start_time, end_time, id_scenario) VALUES (1, 5, TIME '10:00' , TIME '18:00', 1);