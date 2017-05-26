create database internmatch;

use internmatch;

create table students 
(sid int unsigned not null auto_increment primary key, 
email char(50) not null, 
password char(70) not null, 
fname char(50) not null, 
lname char(50) not null,
gender char(25),
lev char (10),
gpa float(4,2));

create table companies 
(cid int unsigned not null auto_increment primary key, 
email char(50) not null, 
password char(70) not null,  
name char(80) not null);

create table emails
(email char(50) not null primary key,
id int unsigned not null,
usertype enum("company","student") not null);

create table studentquals 
(qualid int unsigned not null auto_increment primary key, 
qualname char(20) not null);

create table compattrs 
(attrid int unsigned not null auto_increment primary key,  
attrname char(30) not null);

create table possessedquals 
(qualid int unsigned not null, 
sid int unsigned not null, 
primary key (sid, qualid));

create table possessedattrs 
(attrid int unsigned not null, 
cid int unsigned not null, 
primary key (attrid, cid));

create table desiredattrs
(attrid int unsigned not null, 
sid int unsigned not null, 
primary key (attrid, sid));

 create table desiredquals 
(qualid int unsigned not null, 
cid int unsigned not null, 
primary key (qualid, cid));

/*create table studentinterests
(sid int unsigned not null, 
cid int unsigned not null, 
primary key (sid, cid));

create table compinterests 
(cid int unsigned not null, 
sid int unsigned not null, 
primary key(cid, sid));
*/
create table suggestions 
(cid int unsigned not null,  
sid int unsigned not null,  
percent float(4,2) not null, 
primary key (cid, sid));

create table matches 
(cid int unsigned not null,  
sid int unsigned not null, 
primary key (cid, sid));


create table studentrequests 
(sourceid int unsigned not null,  
targetid int unsigned not null, 
primary key (sourceid, targetid));

create table companyrequests 
(sourceid int unsigned not null,  
targetid int unsigned not null, 
primary key (sourceid, targetid));

