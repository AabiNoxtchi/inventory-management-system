drop database inventory;
create database inventory;
use inventory;

select * from pending_user;
select * from country;
select * from user where id = 4;/*user_name = 'emp0404';
/*select * from role;*/
select * from user;
select * from category;
select * from mol;
select * from city;
select * from country;
select * from supplier where user_id = 4;
select * from country c inner join city ci on ci.country_id = c.id;
select * from country c left join city ci on ci.country_id = c.id;
select * from mol_city;
select * from user_profile where user_id = 15;
select * from user_profile where product_detail_id = 177;
select * from product ;
select * from user_category uc inner join category c where uc.category_id = c.id and uc.user_id = 4; 
select * from lta_detail;
select * from delivery;
select * from user_profile where id = 308;
select * from delivery_detail where delivery_id = 2;
delete from delivery_detail where delivery_id = 2;
delete from user_profile where id = 1;


select * 
from product_detail pd 
inner join delivery_detail dd on dd.id=pd.delivery_detail_id 
where /*dd.id = 121*/pd.inventory_number like '91fed406-947f-4d4c-80ae-55f3be9b4dc1';

select * from product_detail pd 
right join delivery_detail dd on dd.id = pd.delivery_detail_id /*where dd.delivery_id = 17*/
inner join delivery d on d.id = dd.delivery_id /*where pd.is_discarded = true*/
inner join supplier s on s.id = d.supplier_id
where s.user_id = 4;
    
select * from delivery_detail dd join ( 
	select pd.delivery_detail_id as pd_dd_id 
	from product_detail pd 
	where pd.is_discarded is false group by pd.delivery_detail_id) 
pd on dd.id = pd_dd_id;

select * from delivery where id = 1;
select * from delivery_detail where delivery_id = 1; /*id = 2;*/
select * from product_detail pd where pd.delivery_detail_id = 1;

update product_detail pd set is_discarded = true where id = 2;


select count(*) from user u where u.mol_id = 4;
select count(*) from user_profile up inner join user u on u.id = up.user_id 
where u.mol_id = 4 or u.id = 4;
select * from user_category where user_id = 4;
select count(*) from product p inner join user_category uc on p.user_category_id = uc.id
where uc.user_id = 4; 
select count(*) from supplier where user_id = 4 ;

select  count(distinct d.number) from 
delivery d
inner join delivery_detail dd on d.id = dd.delivery_id
inner join product p on dd.product_id = p.id 
inner join user_category uc on uc.id = p.user_category_id
where uc.user_id = 4;

select count(*) from delivery_detail dd 
inner join product p on dd.product_id = p.id 
inner join user_category uc on uc.id = p.user_category_id
where uc.user_id = 4;

select count(*) from product_detail pd inner join delivery_detail dd on dd.id = pd.delivery_detail_id
inner join product p on p.id = dd.product_id 
inner join user_category uc on uc.id = p.user_category_id
where uc.user_id = 4;



select d.number, d.date, dd.product_id, p.name ,pd.inventory_number 
from 
delivery d 
inner join delivery_detail dd on  d.id=dd.delivery_id
inner join product p on p.id=dd.product_id  
inner join product_detail pd on pd.delivery_detail_id=dd.id 
where p.user_id=12 and d.number = 35
order by d.number,p.name;

select  d.date as d_date, d.number as d_number, p.name , pd.id as pd_id, up.id as up_id 
from 
product_detail pd 
inner join user_profile up on pd.id = up.product_detail_id 
inner join user u on u.id = up.user_id 
inner join delivery_detail dd on dd.id = pd.delivery_detail_id 
inner join delivery d on d.id = dd.delivery_id 
inner join product p on p.id=dd.product_id 
where u.id = 4
order by d.number,p.name;

select * from 
product_detail pd inner join 
user_profile up on up.product_detail_id = pd.id inner join 
user u on u.id = up.user_id
where u.id = 4 or u.mol_id = 4;

insert into profile_detail( user_profile_id, created_at, owed_amount, paid_amount) values(65, NOW(), 100,0);

update product_detail set econdition = 1 where id = 65;

select * from product p inner join user_category uc on uc.id = p.user_category_id inner join user u on u.id = uc.user_id
where u.id = 4;

select * from profile_detail;
select * from user_profile up inner join profile_detail pd on pd.user_profile_id = up.id;

select * from mol;

select * from 
product_detail pd 
inner join delivery_detail dd on dd.id = pd.delivery_detail_id 
inner join product p on p.id = dd.product_id
inner join user_category uc on p.user_category_id = uc.id where uc.user_id = 4;



select count(*)             
from 
product_detail pd 
inner join delivery_detail dd on dd.id=pd.delivery_detail_id 
where dd.product_id = 215; 
          
select sum(dd.price_per_one) 
from 
delivery_detail dd 
inner join product_detail pd on dd.id=pd.delivery_detail_id 
where dd.delivery_id = 1;  
          
select sum(d.quantity) 
from 
(select count(*) as quantity 
			from 
            product_detail pd 
            inner join delivery_detail dd on dd.id=pd.delivery_detail_id 
			where dd.product_id = 1) d; 
            
select count(*) from /* items count */
            (
            select pd.id as id 
			from 
            product_detail pd 
            inner join delivery_detail dd on dd.id=pd.delivery_detail_id 
            inner join product p on p.id = dd.product_id
			where p.user_id = 4) d;  
            
            
        