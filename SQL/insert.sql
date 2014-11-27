begin;

	insert into bus_stops values
	    ('1','IIT Bombay',null),
	    ('2','Kanjurmarg',null),
	    ('3','Bhandup',null),
	    ('4','Mulund',null);
	    
	insert into paths values
	    ('100','1','2',4,10,interval '10 min', '{0.8,0.6,0.8,0.8,1.4,1.6,1.0,1.0,1.2,1.4,1.2,1.0}'),
	    ('101','2','3',4,10,interval '10 min', '{0.8,0.6,0.8,0.8,1.4,1.6,1.0,1.0,1.2,1.4,1.2,1.0}'),
	    ('102','3','4',4,10,interval '10 min', '{0.8,0.6,0.8,0.8,1.4,1.6,1.0,1.0,1.2,1.4,1.2,1.0}');

	insert into routes values
	    ('1000','1','4','{"1","2","3","4"}');

	insert into buses values
	    ('100','1000','NA',Array[time '5:00 PM',time '6:00 PM',time '10:00 PM' ]);

	insert into bus_users(username, password, fav_src, fav_dst, total_time) values
	    ('Rakesh','25604003','{"2"}','{"1"}' , 0 );                

	insert into bus_stops values
		('5','Hiranandani',null),
		('6','Police station,Powai' ,null),
		('7','Mahada',null),
		('8','Gandhinagar',null),
		('9','Bond',null),
		('10','Vikhroli station',null);

--create table paths (
--    path_id varchar(6) primary key,
--    src_id varchar(6)  references bus_stops(bus_stop_id),
--    dst_id varchar(6)  references bus_stops(bus_stop_id),
--    ticket_ac float check (ticket_ac > 0),
--    ticket_non_ac float check (ticket_non_ac > 0),
--    time_to_travel interval,
--    traffic_rate float ARRAY[12]
--);

	insert into paths values
		('103','2','1',4,10,interval '10 min', '{0.8,0.6,0.8,0.8,1.4,1.6,1.0,1.0,1.2,1.4,1.2,1.0}'),
    		('104','3','2',4,10,interval '10 min', '{0.8,0.6,0.8,0.8,1.4,1.6,1.0,1.0,1.2,1.4,1.2,1.0}'),
    		('105','4','3',4,10,interval '10 min', '{0.8,0.6,0.8,0.8,1.4,1.6,1.0,1.0,1.2,1.4,1.2,1.0}'),

		('106','1','8',9,20, interval '10 min', '{0.4,0.8,0.9,1.0,1.1,1.2,1.3,1.2,1.1,0.9,1.2,1.3}'),
		('107','8','1',9,20, interval '15 min', '{0.4,0.8,0.9,1.0,1.1,1.2,1.3,1.2,1.1,0.9,1.2,1.3}'),

		('108','8','10',18,35, interval '15 min','{1.4,1.8,1.9,1.0,1.4,1.0,1.3,1.2,1.5,1.9,1.2,1.3}'),
		('109','10','8',18,35, interval '15 min','{1.4,1.8,1.9,1.0,1.4,1.0,1.3,1.2,1.5,1.9,1.2,1.3}'),

		('110','1','5',20,40, interval '7 min','{1.6,1.2,0.9,1.0,1.4,1.5,1.35,1.2,1.8,0.9,1.7,1.6}'),
		('111','5','1',20,40, interval '10 min','{1.6,1.2,0.9,1.0,1.4,1.5,1.35,1.2,1.8,0.9,1.7,1.6}'),

		('112','6','5',25,45, interval '17 min','{1.6,1.2,0.9,1.5,1.4,1.5,1.5,1.2,1.8,1.0,1.7,1.9}'),
		('113','5','6',25,45, interval '20 min','{1.6,1.2,0.9,1.5,1.4,1.5,1.5,1.2,1.8,1.0,1.7,1.9}'),

		('114','6','7',20,40, interval '20 min','{1.6,1.2,0.9,1.5,1.4,1.5,1.5,1.2,1.8,1.0,1.7,1.9}'),
		('115','7','6',25,45, interval '25 min','{1.6,1.2,0.9,1.5,1.4,1.5,1.5,1.2,1.8,1.0,1.7,1.9}'),

		('116','6','9',20,45, interval '5 min','{1.6,1.2,0.9,1.5,1.4,1.5,1.5,1.2,1.8,1.0,1.7,1.9}'),
		('117','9','6',20,45, interval '10 min','{1.6,1.2,0.9,1.5,1.4,1.5,1.5,1.2,1.8,1.0,1.7,1.9}');



	insert into routes values
		('1001','4','1','{"4","3","2","1"}'),

    		('1002','6','10','{"6","5","1","8","10"}'),	--route 6-10,
    		('1003','10','6','{"10","8","1","5","6"}'),

		('1004','10','4','{"10","8","1","2","3","4"}'),    	--route 10-4
		('1005','4','10','{"4","3","2","1","8","10"}');

	insert into buses values
		('101','1001','NA',Array[time '6:00 PM',time '7:00 PM',time '11:00 PM' ]),

		('102','1002','NA',Array[time '6:00 PM',time '7:00 PM',time '11:00 PM' ]),
		('103','1003','NA',Array[time '6:00 PM',time '7:00 PM',time '11:00 PM' ]),

		('104','1004','NA',Array[time '5:00 PM',time '6:00 PM',time '7:00 PM' ]),
		('105','1005','NA',Array[time '7:00 PM',time '8:00 PM',time '10:30 PM' ]);

---to give bus top 7(mahada) some route
	insert into routes values
		('1006','7','2','{"7","6","5","1","2"}'),
		('1007','2','7','{"2","1","5","6","7"}');

	insert into buses values 
		('106','1006','NA',Array[time '6:00 PM',time '7:00 PM',time '8:00 PM' ]),
		('107','1007','NA',Array[time '6:00 PM',time '6:30 PM',time '11:00 PM' ]);
---#to give bus top 7(mahada) some route

commit;

