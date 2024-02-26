
insert into roles (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,"ADMIN");
insert into roles (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,"USER");

insert into categories (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false, "category 1");
insert into categories (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-11-20 14:30:00',false, "category 2");
insert into categories (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-10-20 14:30:00',false, "category 3");
insert into categories (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-9-20 14:30:00',false, "category 4");
insert into categories (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-1-20 14:30:00',false, "category 5");
insert into categories (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-2-20 14:30:00',false, "category 6");
insert into categories (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-6-20 14:30:00',false, "category 8");
insert into categories (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-4-20 14:30:00',false, "category 7");

insert into materials (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false, "material 1");
insert into materials (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-11-20 14:30:00',false, "material 2");
insert into materials (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-10-20 14:30:00',false, "material 3");
insert into materials (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-9-20 14:30:00',false, "material 4");
insert into materials (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-1-20 14:30:00',false, "material 5");
insert into materials (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-2-20 14:30:00',false, "material 6");
insert into materials (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-6-20 14:30:00',false, "material 8");
insert into materials (updated_at,created_at,is_deleted,name) values ('1000-12-20 14:30:00','2023-4-20 14:30:00',false, "material 7");


insert into products (category_id,material_id,updated_at,created_at,is_deleted,name,unit_price,description)
values (1,1,'1000-12-20 14:30:00','2023-12-20 14:30:00',false,'product 1',1.1,'description 1');
insert into products (category_id,material_id,updated_at,created_at,is_deleted,name,unit_price,description)
values (1,2,'1000-12-20 14:30:00','2023-11-20 14:30:00',false,'product 2',2.1,'description 2');
insert into products (category_id,material_id,updated_at,created_at,is_deleted,name,unit_price,description)
values (1,3,'1000-12-20 14:30:00','2023-10-20 14:30:00',false,'product 3',3.1,'description 3');
insert into products (category_id,material_id,updated_at,created_at,is_deleted,name,unit_price,description)
values (1,4,'1000-12-20 14:30:00','2023-4-20 14:30:00',false,'product 4',4.1,'description 4');
insert into products (category_id,material_id,updated_at,created_at,is_deleted,name,unit_price,description)
values (2,7,'1000-12-20 14:30:00','2023-2-20 14:30:00',false,'product 5',5.1,'description 5');
insert into products (category_id,material_id,updated_at,created_at,is_deleted,name,unit_price,description)
values (2,5,'1000-12-20 14:30:00','2023-4-20 14:30:00',false,'product 6',6.1,'description 6');
insert into products (category_id,material_id,updated_at,created_at,is_deleted,name,unit_price,description)
values (5,1,'1000-12-20 14:30:00','2023-1-20 14:30:00',false,'product 7',7.1,'description 7'); 

insert into users (updated_at,created_at,is_deleted,email,password,full_name,address,phone_number,role_id)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,"phucdang1@gmail.com","password1","name1","address1","15325152",1);
insert into users (updated_at,created_at,is_deleted,email,password,full_name,address,phone_number,role_id)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,"phucdang2@gmail.com","password1","name2","address1","15325152",2);
insert into users (updated_at,created_at,is_deleted,email,password,full_name,address,phone_number,role_id)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,"phucdang2.2@gmail.com","password1","name2","address1","15325152",2);
insert into users (updated_at,created_at,is_deleted,email,password,full_name,address,phone_number,role_id)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,"phucdang1.1@gmail.com","password1","name1","address1","15325152",1);

insert into comments (updated_at,created_at,is_deleted,product_id,user_id,content) 
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,1,1,'content 1 1.1');
insert into comments (updated_at,created_at,is_deleted,product_id,user_id,content) 
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,1,1,'content 1 1.2');
insert into comments (updated_at,created_at,is_deleted,product_id,user_id,content) 
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,1,2,'content 1 2');
insert into comments (updated_at,created_at,is_deleted,product_id,user_id,content) 
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,1,3,'content 1 3');
insert into comments (updated_at,created_at,is_deleted,product_id,user_id,content) 
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,2,1,'content 2 1');
insert into comments (updated_at,created_at,is_deleted,product_id,user_id,content) 
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,2,3,'content 2 3');

-- Coupons
insert into coupons(updated_at, created_at, is_deleted, code)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,"code 1");
insert into coupons(updated_at, created_at, is_deleted, code)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,"code 2");
insert into coupons(updated_at, created_at, is_deleted, code)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,"code 3");

-- Coupon Conditions
INSERT INTO coupon_conditions (updated_at, created_at, is_deleted, coupon_id, attribute, operator, value, discount_amount)
VALUES ('1000-12-20 14:30:00','2023-12-20 14:30:00',false, 1, 'minimum_amount', '>', '100', 10);

INSERT INTO coupon_conditions (updated_at, created_at, is_deleted, coupon_id, attribute, operator, value, discount_amount)
VALUES ('1000-12-20 14:30:00','2023-12-20 14:30:00',false, 1, 'applicable_date', 'BETWEEN', '2023-12-25', 5);

INSERT INTO coupon_conditions (updated_at, created_at, is_deleted, coupon_id, attribute, operator, value, discount_amount)
VALUES ('1000-12-20 14:30:00','2023-12-20 14:30:00',false, 2, 'minimum_amount', '>', '200', 20);

-- Orders
insert into orders (updated_at,created_at,is_deleted,user_id,full_name,address,phone_number,note,total_money,email,coupon_id,shipping_date,shipping_address,status)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,1,"full_name 1","address 1","37458235925","note 1",1262.35,"pshu@gmail.com",1,"2024-1-20","shipping address 1", "status 1");
insert into orders (updated_at,created_at,is_deleted,user_id,full_name,address,phone_number,note,total_money,email,coupon_id,shipping_date,shipping_address,status)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,1,"full_name 2","address 2","37458235925","note 2",3462.35,"ps3hu@gmail.com",1,"2024-1-20","shipping address 2", "status 2");
insert into orders (updated_at,created_at,is_deleted,user_id,full_name,address,phone_number,note,total_money,email,coupon_id,shipping_date,shipping_address,status)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,2,"full_name 3","address 3","37458235925","note 3",5262.35,"pshu@gmail.com",1,"2024-1-20","shipping address 3", "status 3");
insert into orders (updated_at,created_at,is_deleted,user_id,full_name,address,phone_number,note,total_money,email,coupon_id,shipping_date,shipping_address,status)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,2,"full_name 4","address 4","37458235925","note 4",1542.35,"ps1hu@gmail.com",1,"2024-1-20","shipping address 4", "status 4");
insert into orders (updated_at,created_at,is_deleted,user_id,full_name,address,phone_number,note,total_money,email,coupon_id,shipping_date,shipping_address,status)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,3,"full_name 5","address 5","37458235925","note 5",3562.35,"pshu@gmail.com",1,"2024-1-20","shipping address 5", "status 5");


-- Order Detail
insert into order_details (updated_at,created_at,is_deleted,number_of_products,unit_price,total_amount,product_id,order_id)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,8,3.1,24.8,3,5);
insert into order_details (updated_at,created_at,is_deleted,number_of_products,unit_price,total_amount,product_id,order_id)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,5,2.1,10.5,2,5);
insert into order_details (updated_at,created_at,is_deleted,number_of_products,unit_price,total_amount,product_id,order_id)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,11,1.1,13.1,1,5);
insert into order_details (updated_at,created_at,is_deleted,number_of_products,unit_price,total_amount,product_id,order_id)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,3,4.1,12.3,4,4);
insert into order_details (updated_at,created_at,is_deleted,number_of_products,unit_price,total_amount,product_id,order_id)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,17,5.1,86.7,5,4);
insert into order_details (updated_at,created_at,is_deleted,number_of_products,unit_price,total_amount,product_id,order_id)
values ('1000-12-20 14:30:00','2023-12-20 14:30:00',false,6,6.1,36.6,6,3);