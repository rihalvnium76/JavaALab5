--�������ݿ�
create database MovieDB on (
	name=MovieDB,
	filename='D:\SQ\MovieDB.mdf', --�Լ��������
	size=10,
	maxsize=100,
	filegrowth=1
)
go

--������
use MovieDB
go
create table Users (
	UserID varchar(10) primary key,
	LoginName varchar(20),
	Password varchar(20),
	UserType varchar(10) check(UserType='��ͨ�û�' or UserType='����Ա' or UserType is null)
)
create table Movie (
	MovieID varchar(10) primary key,
	MovieName varchar(50),
	Director varchar(50),
	MainActors varchar(100),
	MoviePoster varchar(255), -- only flie name
	MovieType varchar(20),
	price float(2),
	MovieInfo text
)
create table Theater (
	TheaterID varchar(6) primary key,
	TheaterName varchar(20),
	Capacity int
)
create table Schedule (
	ScheduleID varchar(10) primary key,
	ScheduleTime datetime,
	MovieID varchar(10) foreign key references Movie(MovieID),
	TheaterID varchar(6) foreign key references Theater(TheaterID)
)
create table Ticket (
	TicketID varchar(10) primary key,
	UserID varchar(10) foreign key references Users(UserID) null,
	Row int,
	Col int,
	ScheduleID varchar(10) foreign key references Schedule(ScheduleID),
	Status char(4) check(Status='�ѹ�' or Status='δ��')
)