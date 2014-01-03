CREATE TABLE test (
    id INTEGER,
    name VARCHAR(50)
);

LOAD DUMP for test from 'test.dump';

SELECT id, name from test WHERE id=100500;
SELECT id, name from test WHERE id < 10 and id > 5;

CREATE INDEX test_index ON test (id);

SELECT id, name from test WHERE id=100500;
SELECT id, name from test WHERE id < 10 and id > 5;
SELECT id, name from test WHERE id=1;
SELECT id, name from test WHERE id=999999;
SELECT id, name from test WHERE id >= 999990 and id < 1000000;
SELECT id, name from test WHERE id >= 999990;

INSERT INTO test (id, name) VALUES(100500,'ASDASD');

CREATE TABLE test_children (
    id INTEGER,
    test_id INTEGER,
    value INTEGER
);

LOAD DUMP for test_children from 'test_children.dump';

select * from test_children join test on test_children.test_id = test.id where test.id=100500;
select test.id,test_children.test_id,test.name from test join test_children on test.id = test_children.test_id where test.id=100500;

delete from test_children where id > 100000;
select * from test_children where id  > 100000;

select value from test_children where id = 1;
update test_children set value=3 where id = 1;
select value from test_children where id = 1;

delete from test where id >= 100500 and id <= 110500;
select * from test where id > 100400 and id < 110600;
