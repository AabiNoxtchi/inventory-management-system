drop database inventory;
create database inventory;
use inventory;

/*select * from role;*/
select * from user;
select * from mol;
select * from city;
select * from country;
select * from country c inner join city ci on ci.country_id = c.id;
select * from mol_city;
select * from user_profile where user_id = 4;
select * from user_profile where product_detail_id = 177;
select * from product ;
select * from user_category uc inner join category c where uc.category_id = c.id and uc.user_id = 4; 
select * from lta_detail;
select * from delivery;
select * from user_profile where id = 308;

delete from user_profile where id = 1;

select * 
from product_detail pd 
inner join delivery_detail dd on dd.id=pd.delivery_detail_id 
where /*dd.id = 121*/pd.inventory_number like '91fed406-947f-4d4c-80ae-55f3be9b4dc1';

select d.number, d.date, dd.product_id, p.name ,pd.inventory_number 
from 
delivery d 
inner join delivery_detail dd on  d.id=dd.delivery_id
inner join product p on p.id=dd.product_id  
inner join product_detail pd on pd.delivery_detail_id=dd.id 
where p.user_id=12 and d.number = 35
order by d.number,p.name;

select d.date as d_date, d.number as d_number, p.name , pd.id as pd_id, up.id as up_id 
from 
product_detail pd 
inner join user_profile up on pd.id = up.product_detail_id 
inner join user u on u.id = up.user_id 
inner join delivery_detail dd on dd.id = pd.delivery_detail_id 
inner join delivery d on d.id = dd.delivery_id 
inner join product p on p.id=dd.product_id 
where u.id = 4
order by d.number,p.name;

select * from product p inner join user_category uc on uc.id = p.user_category_id inner join user u on u.id = uc.user_id
where u.id = 4;

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
        