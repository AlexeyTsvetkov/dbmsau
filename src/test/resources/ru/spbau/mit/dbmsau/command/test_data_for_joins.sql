CREATE TABLE test (
    id INTEGER,
    name VARCHAR(50)
);

CREATE TABLE test_children (
    id INTEGER,
    test_id INTEGER,
    value INTEGER
);

INSERT INTO test (id, name) VALUES(1, 'tlbv');
INSERT INTO test (id, name) VALUES(2, 'gmtb');
INSERT INTO test (id, name) VALUES(3, 'wyqa');
INSERT INTO test (id, name) VALUES(4, 'bfex');
INSERT INTO test (id, name) VALUES(5, 'myxb');
INSERT INTO test (id, name) VALUES(6, 'xswb');
INSERT INTO test (id, name) VALUES(7, 'kmsi');
INSERT INTO test (id, name) VALUES(8, 'cbxv');
INSERT INTO test (id, name) VALUES(9, 'dcna');
INSERT INTO test (id, name) VALUES(10, 'fpqq');
INSERT INTO test (id, name) VALUES(11, 'hqct');
INSERT INTO test (id, name) VALUES(12, 'geol');
INSERT INTO test (id, name) VALUES(13, 'kreb');
INSERT INTO test (id, name) VALUES(14, 'wvgl');
INSERT INTO test (id, name) VALUES(15, 'hrjd');
INSERT INTO test (id, name) VALUES(16, 'pezt');
INSERT INTO test (id, name) VALUES(17, 'zhku');
INSERT INTO test (id, name) VALUES(18, 'ofog');
INSERT INTO test (id, name) VALUES(19, 'mamo');
INSERT INTO test (id, name) VALUES(20, 'vayg');
INSERT INTO test_children (id, test_id, value) VALUES(1, 18, 500);
INSERT INTO test_children (id, test_id, value) VALUES(2, 17, 470);
INSERT INTO test_children (id, test_id, value) VALUES(3, 6, 697);
INSERT INTO test_children (id, test_id, value) VALUES(4, 16, 77);
INSERT INTO test_children (id, test_id, value) VALUES(5, 4, 710);
INSERT INTO test_children (id, test_id, value) VALUES(6, 19, 354);
INSERT INTO test_children (id, test_id, value) VALUES(7, 4, 482);
INSERT INTO test_children (id, test_id, value) VALUES(8, 11, 142);
INSERT INTO test_children (id, test_id, value) VALUES(9, 10, 807);
INSERT INTO test_children (id, test_id, value) VALUES(10, 9, 282);
INSERT INTO test_children (id, test_id, value) VALUES(11, 12, 506);
INSERT INTO test_children (id, test_id, value) VALUES(12, 6, 544);
INSERT INTO test_children (id, test_id, value) VALUES(13, 6, 924);
INSERT INTO test_children (id, test_id, value) VALUES(14, 20, 297);
INSERT INTO test_children (id, test_id, value) VALUES(15, 19, 345);
INSERT INTO test_children (id, test_id, value) VALUES(16, 20, 259);
INSERT INTO test_children (id, test_id, value) VALUES(17, 4, 781);
INSERT INTO test_children (id, test_id, value) VALUES(18, 10, 674);
INSERT INTO test_children (id, test_id, value) VALUES(19, 16, 447);
INSERT INTO test_children (id, test_id, value) VALUES(20, 17, 221);
