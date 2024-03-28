\c temperature_db;
DO $$
    DECLARE
        room_one_id integer;
        room_two_id integer;
    BEGIN

    insert into room(identifier) values ('room_1');
    room_one_id := (SELECT t.id from room t where t.identifier = 'room_1');

    insert into room(identifier) values ('room_2');
    room_two_id := (SELECT t.id from room t where t.identifier = 'room_2');

    insert into thermometer(identifier, room_id) values('thermometer_1', room_one_id);
    insert into thermometer(identifier, room_id) values('thermometer_2', room_one_id);
    insert into thermometer(identifier, room_id) values('thermometer_3', room_two_id);
    insert into thermometer(identifier, room_id) values('thermometer_4', room_two_id);

    END;

$$;


