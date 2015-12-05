ALTER TABLE `queues` ADD `test` int(1) unsigned NOT NULL DEFAULT 1;
UPDATE `queues` set test = 0 where queue_id in (1,2,4);