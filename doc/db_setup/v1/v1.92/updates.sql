update queues_opening_hours set day_id = 1 where day_id = 2 and queue_id = 1;
update queues_opening_hours set day_id = 2 where day_id = 3 and queue_id = 1;
update queues_opening_hours set day_id = 3 where day_id = 4 and queue_id = 1;
update queues_opening_hours set day_id = 4 where day_id = 5 and queue_id = 1;
update queues_opening_hours set day_id = 5 where day_id = 6 and queue_id = 1;

update queues_opening_hours set day_id = 40 where day_id = 5 and queue_id = 1;
update queues_opening_hours set day_id = 5 where day_id = 4 and queue_id = 1;
update queues_opening_hours set day_id = 4 where day_id = 40 and queue_id = 1;

