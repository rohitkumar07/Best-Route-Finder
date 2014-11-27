begin;


drop table bus_stops cascade;
drop table routes cascade;
drop table buses cascade;
drop table paths cascade;
drop table bus_users cascade;

create table bus_stops (
	bus_stop_id varchar(6) primary key,
	bus_stop_name varchar(30),
	list_buses varchar(6) array
);

create index bus_name_index on bus_stops (bus_stop_name);
-- Index on the bus_stop_name of the bus_stop

create table paths (
	path_id varchar(6) primary key,
	src_id varchar(6)  references bus_stops(bus_stop_id),
	dst_id varchar(6)  references bus_stops(bus_stop_id),
	ticket_ac float check (ticket_ac > 0),
	ticket_non_ac float check (ticket_non_ac > 0),
	time_to_travel interval,
	traffic_rate float ARRAY[12]
);

create table routes(
	route_id varchar(6) primary key,
	src_id varchar(6)  references bus_stops(bus_stop_id),
	dst_id varchar(6)  references bus_stops(bus_stop_id),
	list_bus_stops varchar(6) array
) ;


create table buses(
	bus_id varchar(6) primary key,
	route_id  varchar(6) references routes(route_id),
	bus_type char(2) check (bus_type in ('AC','NA')),
	timings time ARRAY);

create table bus_users(
	user_id serial primary key,
	username varchar(50),
	password varchar(30),
	fav_src  varchar(6) array,
	fav_dst  varchar(6) array,
	total_time numeric(10,0) check (total_time >= 0)
	);
    
---------------------------------------------------------------------------------------------
create or replace function insert_into_stop() returns trigger as $insert_bus_trigger$
DECLARE my_bus_stop record;
BEGIN
RAISE NOTICE 'insert triggered';
	for my_bus_stop in (select unnest(list_bus_stops) as bus_stop_id from routes where route_id = new.route_id)
	LOOP
    	RAISE NOTICE 'updating(%)', my_bus_stop;
    	update bus_stops set list_buses = array_append(list_buses, new.bus_id)  where bus_stop_id = my_bus_stop.bus_stop_id;
	END LOOP;
	RETURN new;
END;
$insert_bus_trigger$ LANGUAGE plpgsql;
	 
create trigger insert_bus_trigger after insert on buses
	for each row
	execute procedure  insert_into_stop();
---------------------------------------------------------------------------------------------
create or replace function delete_from_stop() returns trigger as $delete_bus_trigger$
DECLARE my_bus_stop record;
BEGIN
RAISE NOTICE 'delete triggered';
	for my_bus_stop in (select unnest(list_bus_stops) as bus_stop_id from routes where route_id = old.route_id)
	LOOP
    	RAISE NOTICE 'updating(%)', my_bus_stop;
    	update bus_stops set list_buses = array(select unnest(list_buses) except select old.bus_id)  where bus_stop_id = my_bus_stop.bus_stop_id;
	END LOOP;
	RETURN NULL;
END;
$delete_bus_trigger$ LANGUAGE plpgsql;
	 
create trigger delete_bus_trigger after delete on buses
	for each row
	execute procedure  delete_from_stop();
----------------------------------------------------------------------------------------------
create or replace function update_at_stop() returns trigger as $update_bus_trigger$
DECLARE my_bus_stop record;
BEGIN
	for my_bus_stop in (select unnest(list_bus_stops) as bus_stop_id from routes where route_id = old.route_id)
	LOOP
    	RAISE NOTICE 'updating(%)', my_bus_stop;
    	update bus_stops set list_buses = array(select unnest(list_buses) except select old.bus_id)  where bus_stop_id = my_bus_stop.bus_stop_id;
	END LOOP;
	for my_bus_stop in (select unnest(list_bus_stops) as bus_stop_id from routes where route_id = new.route_id)
	LOOP
    	RAISE NOTICE 'updating(%)', my_bus_stop;
    	update bus_stops set list_buses = array_append(list_buses, new.bus_id)  where bus_stop_id = my_bus_stop.bus_stop_id;
	END LOOP;
	RETURN new;
END;
$update_bus_trigger$ LANGUAGE plpgsql;
	 
create trigger update_bus_trigger after update of route_id on buses
	for each row
	execute procedure  update_at_stop();    
--------------------------------------------------------------------------------------------  
commit;
