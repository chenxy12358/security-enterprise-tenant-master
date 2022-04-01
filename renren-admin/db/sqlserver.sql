CREATE TABLE sys_tenant (
  id bigint NOT NULL,
  tenant_code bigint,
  tenant_name varchar(50),
  status int,
  remark varchar(200),
  user_id bigint,
  username varchar(50),
  del_flag int,
  sys_tenant int,
  creator bigint,
  create_date datetime,
  updater bigint,
  update_date datetime,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uk_tenant_code on sys_tenant(tenant_code);
CREATE INDEX idx_sys_tenant_create_date on sys_tenant(create_date);


CREATE TABLE sys_user (
  id bigint NOT NULL,
  username varchar(50) NOT NULL,
  password varchar(100),
  real_name varchar(50),
  head_url varchar(200),
  gender  int,
  email varchar(100),
  mobile varchar(100),
  dept_id bigint,
  super_admin  int,
  super_tenant int,
  status  int,
  tenant_code bigint,
  creator bigint,
  create_date datetime,
  updater bigint,
  update_date datetime,
  primary key (id)
);

CREATE UNIQUE INDEX uk_sys_user_username on sys_user(username);
CREATE INDEX idx_sys_user_create_date on sys_user(create_date);


CREATE TABLE sys_dept (
  id bigint NOT NULL,
  pid bigint,
  pids varchar(500),
  name varchar(50),
  sort  int,
  tenant_code bigint,
  creator bigint,
  create_date datetime,
  updater bigint,
  update_date datetime,
  primary key (id)
);
CREATE INDEX idx_sys_dept_pid on sys_dept(pid);
CREATE INDEX idx_sys_dept_idx_sort on sys_dept(sort);


create table sys_role
(
  id                   bigint NOT NULL,
  name                 varchar(50),
  remark               varchar(100),
  dept_id              bigint,
  tenant_code          bigint,
  creator              bigint,
  create_date          datetime,
  updater              bigint,
  update_date          datetime,
  primary key (id)
);

CREATE INDEX idx_sys_role_dept_id on sys_role(dept_id);


create table sys_menu
(
  id                   bigint NOT NULL,
  pid                  bigint,
  url                  varchar(200),
  permissions          varchar(500),
  type                 int,
  open_style           int,
  icon                 varchar(50),
  sort                 int,
  creator              bigint,
  create_date          datetime,
  updater              bigint,
  update_date          datetime,
  primary key (id)
);

CREATE INDEX idx_sys_menu_pid on sys_menu(pid);
CREATE INDEX idx_sys_menu_sort on sys_menu(sort);


create table sys_role_user
(
  id                   bigint NOT NULL,
  role_id              bigint,
  user_id              bigint,
  creator              bigint,
  create_date          datetime,
  primary key (id)
);

CREATE INDEX idx_sys_role_user_role_id on sys_role_user(role_id);
CREATE INDEX idx_sys_role_user_user_id on sys_role_user(user_id);


create table sys_role_menu
(
  id                   bigint NOT NULL,
  role_id              bigint,
  menu_id              bigint,
  creator              bigint,
  create_date          datetime,
  primary key (id)
);

CREATE INDEX idx_sys_role_menu_role_id on sys_role_menu(role_id);
CREATE INDEX idx_sys_role_menu_menu_id on sys_role_menu(menu_id);


create table sys_role_data_scope
(
  id                   bigint NOT NULL,
  role_id              bigint,
  dept_id              bigint,
  creator              bigint,
  create_date          datetime,
  primary key (id)
);
CREATE INDEX idx_data_scope_role_id on sys_role_data_scope(role_id);


create table sys_params
(
  id                   bigint NOT NULL,
  param_code           varchar(32),
  param_value          varchar(2000),
  param_type            int DEFAULT 1 NOT NULL,
  remark               varchar(200),
  creator              bigint,
  create_date          datetime,
  updater              bigint,
  update_date          datetime,
  primary key (id)
);
CREATE UNIQUE INDEX uk_sys_params_param_code on sys_params(param_code);
CREATE INDEX idx_sys_params_create_date on sys_params(create_date);


create table sys_dict_type
(
    id                   bigint NOT NULL,
    dict_type            varchar(100),
    dict_name            varchar(255),
    remark               varchar(255),
    sort                 int,
    creator              bigint,
    create_date          datetime,
    updater              bigint,
    update_date          datetime,
    primary key (id)
);
CREATE UNIQUE INDEX uk_sys_dict_type_dict_type on sys_dict_type(dict_type);


create table sys_dict_data
(
    id                   bigint NOT NULL,
    dict_type_id         bigint NOT NULL,
    dict_label           varchar(255),
    dict_value           varchar(255),
    remark               varchar(255),
    sort                 int,
    creator              bigint,
    create_date          datetime,
    updater              bigint,
    update_date          datetime,
    primary key (id)
);
CREATE INDEX idx_sys_dict_data_sort on sys_dict_data(sort);
CREATE UNIQUE INDEX uk_dict_type_value on sys_dict_data(dict_type_id, dict_value);


create table sys_log_login
(
  id                   bigint NOT NULL,
  operation            int,
  status               int,
  user_agent           varchar(500),
  ip                   varchar(32),
  creator_name         varchar(50),
  creator              bigint,
  create_date          datetime,
  primary key (id)
);
CREATE INDEX idx_login_status on sys_log_login(status);
CREATE INDEX idx_login_create_date on sys_log_login(create_date);


create table sys_log_operation
(
  id                   bigint NOT NULL,
  operation            varchar(50),
  request_uri          varchar(200),
  request_method       varchar(20),
  request_params       text,
  request_time          int,
  user_agent           varchar(500),
  ip                   varchar(32),
  status                int,
  creator_name         varchar(50),
  creator              bigint,
  create_date          datetime,
  primary key (id)
);
CREATE INDEX idx_operation_create_date on sys_log_operation(create_date);


create table sys_log_error
(
  id                   bigint NOT NULL,
  request_uri          varchar(200),
  request_method       varchar(20),
  request_params       text,
  user_agent           varchar(500),
  ip                   varchar(32),
  error_info           text,
  creator              bigint,
  create_date          datetime,
  primary key (id)
);
CREATE INDEX idx_error_create_date on sys_log_error(create_date);


create table sys_sms
(
    id                   bigint NOT NULL,
    sms_code             varchar(32),
    platform             int,
    sms_config           varchar(2000),
    remark               varchar(200),
    creator              bigint,
    create_date          datetime,
    updater              bigint,
    update_date          datetime,
    primary key (id)
);
CREATE UNIQUE INDEX uk_sms_code on sys_sms(sms_code);
CREATE INDEX idx_sys_sms_create_date on sys_sms(create_date);


CREATE TABLE sys_sms_log (
    id bigint NOT NULL,
    sms_code   varchar(32),
    platform int,
    mobile varchar(20),
    params_1 varchar(50),
    params_2 varchar(50),
    params_3 varchar(50),
    params_4 varchar(50),
    status int,
    creator bigint,
    create_date datetime,
    PRIMARY KEY (id)
);
CREATE INDEX idx_sys_sms_log_sms_code on sys_sms_log(sms_code);

CREATE TABLE sys_notice (
    id bigint NOT NULL,
    type int NOT NULL,
    title varchar(200),
    content text,
    receiver_type int,
    receiver_type_ids varchar(500),
    status int,
    sender_name varchar(50),
    sender_date datetime,
    creator bigint,
    create_date datetime,
    PRIMARY KEY (id)
);
CREATE INDEX idx_sys_notice_create_date on sys_notice(create_date);

CREATE TABLE sys_notice_user (
    receiver_id bigint NOT NULL,
    notice_id bigint NOT NULL,
    read_status int NOT NULL,
    read_date datetime,
    PRIMARY KEY (receiver_id, notice_id)
);

CREATE TABLE sys_mail_template (
  id bigint NOT NULL,
  name varchar(100),
  subject varchar(200),
  content text,
  creator bigint,
  create_date datetime,
  updater bigint,
  update_date datetime,
  PRIMARY KEY (id)
);

CREATE INDEX idx_mail_template_create_date on sys_mail_template(create_date);


CREATE TABLE sys_mail_log (
  id bigint NOT NULL,
  template_id bigint NOT NULL,
  mail_from varchar(200),
  mail_to varchar(400),
  mail_cc varchar(400),
  subject varchar(200),
  content text,
  status  int,
  creator bigint,
  create_date datetime,
  PRIMARY KEY (id)
);

CREATE INDEX idx_mail_log_create_date on sys_mail_log(create_date);


CREATE TABLE sys_oss (
  id bigint NOT NULL,
  url varchar(200),
  creator bigint,
  create_date datetime,
  PRIMARY KEY (id)
);
CREATE INDEX idx_sys_oss_create_date on sys_oss(create_date);


CREATE TABLE schedule_job (
  id bigint NOT NULL,
  bean_name varchar(200),
  params varchar(2000),
  cron_expression varchar(100),
  status  int,
  remark varchar(255),
  creator bigint,
  create_date datetime,
  updater bigint,
  update_date datetime,
  PRIMARY KEY (id)
);

CREATE INDEX idx_schedule_job_create_date on schedule_job(create_date);


CREATE TABLE schedule_job_log (
  id bigint NOT NULL,
  job_id bigint NOT NULL,
  bean_name varchar(200),
  params varchar(2000),
  status  int,
  error varchar(2000),
  times  int,
  create_date datetime,
  PRIMARY KEY (id)
);

CREATE INDEX idx_job_log_job_id on schedule_job_log(job_id);
CREATE INDEX idx_job_log_create_date on schedule_job_log(create_date);


CREATE TABLE sys_user_token (
  id bigint NOT NULL,
  user_id bigint,
  token varchar(100),
  expire_date datetime,
  update_date datetime,
  create_date datetime,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uk_sys_user_token_user_id on sys_user_token(user_id);
CREATE UNIQUE INDEX uk_sys_user_token on sys_user_token(token);


CREATE TABLE sys_language (
  table_name varchar(32) NOT NULL,
  table_id bigint NOT NULL,
  field_name varchar(32) NOT NULL,
  field_value varchar(200) NOT NULL,
  language varchar(10) NOT NULL,
  primary key (table_name, table_id, field_name, language)
);

CREATE INDEX idx_sys_language_table_id on sys_language(table_id);

CREATE TABLE sys_region (
    id bigint NOT NULL,
    pid bigint,
    name varchar(100),
    tree_level int,
    leaf int,
    sort bigint,
    creator bigint,
    create_date datetime,
    updater bigint,
    update_date datetime,
    PRIMARY KEY (id)
);


CREATE TABLE tb_news (
  id bigint NOT NULL,
  title varchar(100),
  content text,
  pub_date datetime,
  dept_id bigint,
  creator bigint,
  create_date datetime,
  updater bigint,
  update_date datetime,
  PRIMARY KEY (id)
);


CREATE TABLE tb_correction (
   id bigint NOT NULL,
   apply_post varchar(255),
   entry_date datetime,
   correction_date datetime,
   work_content varchar(2000),
   achievement varchar(2000),
   instance_id varchar(80),
   creator bigint,
   create_date datetime,
   PRIMARY KEY (id)
);

CREATE TABLE tb_product (
    id bigint NOT NULL,
    name varchar(100),
    content varchar(2000),
    creator bigint,
    create_date datetime,
    updater bigint,
    update_date datetime,
    PRIMARY KEY (id)
);

CREATE TABLE tb_product_params (
    id bigint NOT NULL,
    param_name varchar(100),
    param_value varchar(200),
    product_id bigint,
    creator bigint,
    create_date datetime,
    updater bigint,
    update_date datetime,
    PRIMARY KEY (id)
);

create table sys_ureport_data (
    id bigint NOT NULL,
    file_name varchar(200),
    content text,
    create_date datetime,
    update_date datetime,
    PRIMARY KEY (id)
);

create table sys_post (
    id bigint NOT NULL,
    post_code varchar(100),
    post_name varchar(100),
    sort int,
    status int,
    tenant_code bigint,
    creator bigint,
    create_date datetime,
    updater bigint,
    update_date datetime,
    PRIMARY KEY (id)
);

CREATE TABLE sys_user_post (
    id                   bigint NOT NULL,
    post_id              bigint,
    user_id              bigint,
    creator              bigint,
    create_date          datetime,
    primary key (id)
);

CREATE INDEX idx_sys_user_post_post_id on sys_user_post(post_id);
CREATE INDEX idx_sys_user_post_user_id on sys_user_post(user_id);


CREATE TABLE tb_excel_data (
    id bigint NOT NULL,
    real_name varchar(100),
    [identity] varchar(100),
    address varchar(200),
    join_date datetime,
    class_name varchar(100),
    creator bigint,
    create_date datetime,
    primary key (id)
);


CREATE TABLE tb_order (
    id bigint NOT NULL,
    order_id bigint,
    product_id bigint,
    product_name varchar(100),
    pay_amount numeric(10,2),
    status int,
    user_id bigint,
    pay_at datetime,
    create_date datetime,
    primary key (id)
);

CREATE UNIQUE INDEX uk_tb_order_order_id on tb_order(order_id);


CREATE TABLE tb_alipay_notify_log (
    id bigint NOT NULL,
    out_trade_no bigint,
    total_amount numeric(10,2),
    buyer_pay_amount numeric(10,2),
    receipt_amount numeric(10,2),
    invoice_amount numeric(10,2),
    notify_id varchar(50),
    buyer_id varchar(50),
    seller_id varchar(50),
    trade_no varchar(50),
    trade_status varchar(50),
    create_date datetime,
    primary key (id)
);

CREATE TABLE mp_account (
    id bigint NOT NULL,
    name varchar(100),
    app_id varchar(100),
    app_secret varchar(100),
    token varchar(100),
    aes_key varchar(100),
    creator bigint,
    create_date datetime,
    updater bigint,
    update_date datetime,
    primary key (id)
);

CREATE TABLE mp_menu (
    id bigint NOT NULL,
    menu varchar(2000),
    app_id varchar(100),
    creator bigint,
    create_date datetime,
    updater bigint,
    update_date datetime,
    primary key (id)
);

-- 初始数据
INSERT INTO sys_tenant(id, tenant_code, tenant_name, status, remark, user_id, username, del_flag, sys_tenant, creator, create_date, updater, update_date) VALUES ('1067246875800001000', 1001, '默认租户', 1, NULL, 1067246875800000001, 'admin', 0, 1, NULL, getdate(), NULL, NULL);

INSERT INTO sys_user(id, username, password, real_name, gender, email, mobile, status, dept_id, tenant_code, super_admin, super_tenant, creator, create_date, updater, update_date) VALUES (1067246875800000001, 'admin', '$2a$10$012Kx2ba5jzqr9gLlG4MX.bnQJTD9UWqF57XDo2N3.fPtLne02u/m', '管理员', 0, 'root@renren.io', '13612345678', 1, null, 1001, 1, 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_user(id, username, password, real_name, gender, email, mobile, dept_id, super_admin, super_tenant, status, tenant_code, creator, create_date, updater, update_date) VALUES (1353943117220315138, 'test', '$2a$10$012Kx2ba5jzqr9gLlG4MX.bnQJTD9UWqF57XDo2N3.fPtLne02u/m', '测试用户', 0, 'test@renren.io', '13012345678', 1067246875800000066, 0, 0, 1, 1001, 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000002, '0', NULL, NULL, 0, 0, 'icon-safetycertificate', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000003, 1067246875800000055, NULL, 'sys:user:save,sys:dept:list,sys:role:list', 1, 0, NULL, 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000004, 1067246875800000055, NULL, 'sys:user:update,sys:dept:list,sys:role:list', 1, 0, NULL, 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000005, 1067246875800000055, NULL, 'sys:user:delete', 1, 0, NULL, 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000006, 1067246875800000055, NULL, 'sys:user:export', 1, 0, NULL, 4, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000007, 1067246875800000002, 'sys/role', NULL, 0, 0, 'icon-team', 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000008, 1067246875800000007, NULL, 'sys:role:page,sys:role:info', 1, 0, NULL, 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000009, 1067246875800000007, NULL, 'sys:role:save,sys:menu:select,sys:dept:list', 1, 0, NULL, 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000010, 1067246875800000007, NULL, 'sys:role:update,sys:menu:select,sys:dept:list', 1, 0, NULL, 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000011, 1067246875800000007, NULL, 'sys:role:delete', 1, 0, NULL, 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000012, 1067246875800000002, 'sys/dept', NULL, 0, 0, 'icon-apartment', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000014, 1067246875800000012, NULL, 'sys:dept:list,sys:dept:info', 1, 0, NULL, 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000015, 1067246875800000012, NULL, 'sys:dept:save', 1, 0, NULL, 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000016, 1067246875800000012, NULL, 'sys:dept:update', 1, 0, NULL, 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000017, 1067246875800000012, NULL, 'sys:dept:delete', 1, 0, NULL, 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000018, 1150940491508928513, 'flow/process', 'sys:process:all', 0, 0, 'icon-detail', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000019, 1150940491508928513, 'flow/model', 'sys:model:all', 0, 0, 'icon-appstore-fill', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000020, 1150940491508928513, 'flow/running', 'sys:running:all,sys:flow:all', 0, 0, 'icon-play-square', 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000021, 1067246875800000024, 'message/sms', 'sys:sms:all', 0, 0, 'icon-message-fill', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000022, 1067246875800000024, 'message/mail_template', 'sys:mail:all', 0, 0, 'icon-mail', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000023, 1067246875800000024, 'message/mail_log', 'sys:mail:log', 0, 0, 'icon-detail-fill', 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000024, '0', NULL, NULL, 0, 0, 'icon-message', 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000025, 1067246875800000035, 'sys/menu', NULL, 0, 0, 'icon-unorderedlist', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000026, 1067246875800000025, NULL, 'sys:menu:list,sys:menu:info', 1, 0, NULL, 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000027, 1067246875800000025, NULL, 'sys:menu:save', 1, 0, NULL, 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000028, 1067246875800000025, NULL, 'sys:menu:update', 1, 0, NULL, 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000029, 1067246875800000025, NULL, 'sys:menu:delete', 1, 0, NULL, 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000030, 1067246875800000035, 'job/schedule', NULL, 0, 0, 'icon-dashboard', 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000031, 1067246875800000030, NULL, 'sys:schedule:page,sys:schedule:info', 1, 0, NULL, 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000032, 1067246875800000030, NULL, 'sys:schedule:save', 1, 0, NULL, 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000033, 1067246875800000030, NULL, 'sys:schedule:update', 1, 0, NULL, 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000034, 1067246875800000030, NULL, 'sys:schedule:delete', 1, 0, NULL, 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000035, '0', NULL, NULL, 0, 0, 'icon-setting', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000036, 1067246875800000030, NULL, 'sys:schedule:pause', 1, 0, NULL, 4, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000037, 1067246875800000030, NULL, 'sys:schedule:resume', 1, 0, NULL, 5, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000038, 1067246875800000030, NULL, 'sys:schedule:run', 1, 0, NULL, 6, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000039, 1067246875800000030, NULL, 'sys:schedule:log', 1, 0, NULL, 7, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000040, 1067246875800000035, 'sys/params', '', 0, 0, 'icon-fileprotect', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000041, 1067246875800000035, 'sys/dict-type', NULL, 0, 0, 'icon-golden-fill', 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000042, 1067246875800000041, NULL, 'sys:dict:page,sys:dict:info', 1, 0, NULL, 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000043, 1067246875800000041, NULL, 'sys:dict:save', 1, 0, NULL, 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000044, 1067246875800000041, NULL, 'sys:dict:update', 1, 0, NULL, 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000045, 1067246875800000041, NULL, 'sys:dict:delete', 1, 0, NULL, 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000046, '0', NULL, NULL, 0, 0, 'icon-container', 5, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000047, 1067246875800000035, 'oss/oss', 'sys:oss:all', 0, 0, 'icon-upload', 4, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000048, 1067246875800000046, 'sys/log-login', 'sys:log:login', 0, 0, 'icon-filedone', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000049, 1067246875800000046, 'sys/log-operation', 'sys:log:operation', 0, 0, 'icon-solution', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000050, 1067246875800000046, 'sys/log-error', 'sys:log:error', 0, 0, 'icon-file-exception', 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000051, 1067246875800000053, '{{ApiUrl}}/druid/sql.html', NULL, 0, 0, 'icon-database', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000052, 1067246875800000054, 'demo/news', 'demo:news:all', 0, 0, 'icon-file-word', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000053, '0', NULL, NULL, 0, 0, 'icon-desktop', 5, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000054, '0', NULL, NULL, 0, 0, 'icon-windows', 6, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000055, 1067246875800000002, 'sys/user', NULL, 0, 0, 'icon-user', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000056, 1067246875800000055, NULL, 'sys:user:page,sys:user:info', 1, 0, NULL, 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000057, 1067246875800000040, NULL, 'sys:params:save', 1, 0, NULL, 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000058, 1067246875800000040, NULL, 'sys:params:export', 1, 0, NULL, 4, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000059, 1067246875800000040, '', 'sys:params:page,sys:params:info', 1, 0, NULL, 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000060, 1067246875800000040, NULL, 'sys:params:update', 1, 0, NULL, 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000061, 1067246875800000040, '', 'sys:params:delete', 1, 0, '', 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1150940491508928513, '0', NULL, NULL, 0, 0, 'icon-reloadtime', 4, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1150941310262235138, '0',  NULL, NULL, 0, 0, 'icon-user', 4, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1150941384811794433, 1150941310262235138, 'flow/todo', 'sys:flow:all,sys:user:page', 0, 0, 'icon-dashboard', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1150941447038488577, 1150941310262235138, 'flow/done', 'sys:flow:all', 0, 0, 'icon-check-square', 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1150941506626965506, 1150941310262235138, 'flow/start', 'sys:flow:all', 0, 0, 'icon-play-square', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1150941588046794754, 1150941310262235138, 'flow/my-send', 'sys:flow:all', 0, 0, 'icon-edit-square', 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1156748733921165314, 1067246875800000053, '{{ApiUrl}}/doc.html', '', 0, 1, 'icon-file-word', 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1113375699198033921, 0, '', '', 0, 0, 'icon-home', 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1113378406403162113, 1113375699198033921, 'tenant/tenant', 'sys:tenant:page,sys:tenant:info,sys:tenant:password,sys:tenant:save,sys:tenant:update,sys:tenant:delete', 0, 0, 'icon-team', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1114779542196600833, 1113375699198033921, 'tenant/tenant-role', 'tenant:role:page,tenant:role:list,tenant:role:info,tenant:role:save,tenant:role:update', 0, 0, 'icon-team', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1158267114314842115, 1067246875800000035, 'sys/region', NULL, 0, 0, 'icon-location', 4, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1158267114314842116, 1158267114314842115, NULL, 'sys:region:list,sys:region:info', 1, 0, NULL, 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1158267114314842117, 1158267114314842115, NULL, 'sys:region:save', 1, 0, NULL, 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1158267114314842118, 1158267114314842115, NULL, 'sys:region:update', 1, 0, NULL, 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1158267114314842119, 1158267114314842115, NULL, 'sys:region:delete', 1, 0, NULL, 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());


INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000300, '0',  NULL, NULL, 0, 0, 'icon-bell', 5, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000301, 1067246875800000300, 'notice/notice', 'sys:notice:all', 0, 0, 'icon-bell', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000302, 1067246875800000300, 'notice/notice-user', 'sys:notice:all', 0, 0, 'icon-notification', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1067246875800000303, 1067246875800000024, 'message/sms-log', 'sys:smslog:all', 0, 0, 'icon-unorderedlist', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1270380959719501825, 1067246875800000054, 'demo/product', NULL, 0, 0, 'icon-tag', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1270380959719501826, 1270380959719501825, NULL, 'demo:product:page,demo:product:info', 1, 0, NULL, 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1270380959719501827, 1270380959719501825, NULL, 'demo:product:save', 1, 0, NULL, 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1270380959719501828, 1270380959719501825, NULL, 'demo:product:update', 1, 0, NULL, 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1270380959719501829, 1270380959719501825, NULL, 'demo:product:delete', 1, 0, NULL, 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1300278047072649217, 0, '', '', 0, 0, 'icon-filesearch', 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1300278435729440769, 1300278047072649217, '{{ApiUrl}}/ureport/designer', '', 0, 0, 'icon-book', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1300381796852060161, 1300278047072649217, 'ureport/ureport', 'sys:ureport:all', 0, 0, 'icon-up-circle', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());


INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000300, 'name', 'Station Notice', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000300, 'name', '站内通知', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000300, 'name', '站內通知', 'zh-TW');

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000301, 'name', 'Notice Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000301, 'name', '通知管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000301, 'name', '通知管理', 'zh-TW');

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000302, 'name', 'My Notice', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000302, 'name', '我的通知', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000302, 'name', '我的通知', 'zh-TW');

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000303, 'name', 'SMS History', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000303, 'name', '短信发送记录', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000303, 'name', '短信發送記錄', 'zh-TW');

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1300278047072649217, 'name', 'Report Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1300278047072649217, 'name', '报表管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1300278047072649217, 'name', '報表管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1300278435729440769, 'name', 'Report Design', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1300278435729440769, 'name', '报表设计器', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1300278435729440769, 'name', '報表設計器', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1300381796852060161, 'name', 'Report List', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1300381796852060161, 'name', '报表列表', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1300381796852060161, 'name', '報表列表', 'zh-TW');

INSERT INTO sys_dict_type(id, dict_type, dict_name, remark, sort, creator, create_date, updater, update_date) VALUES (1225813644059140097, 'notice_type', '站内通知-类型', '', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814069634195457, 1225813644059140097, '公告', '0', '', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814107559092225, 1225813644059140097, '会议', '1', '', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814271879340034, 1225813644059140097, '其他', '2', '', 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO sys_sms(id, sms_code, platform, sms_config, remark, creator, create_date, updater, update_date) VALUES (1228954061084676097, '1001', 1, '{"aliyunAccessKeyId":"1","aliyunAccessKeySecret":"1","aliyunSignName":"1","aliyunTemplateCode":"1","qcloudAppKey":"","qcloudSignName":"","qcloudTemplateId":"","qiniuAccessKey":"","qiniuSecretKey":"","qiniuTemplateId":""}', '', 1067246875800000001, getdate(), 1067246875800000001, getdate());


INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000002, 'name', 'Authority Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000002, 'name', '权限管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000002, 'name', '權限管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000003, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000003, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000003, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000004, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000004, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000004, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000005, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000005, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000005, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000006, 'name', 'Export', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000006, 'name', '导出', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000006, 'name', '導出', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000007, 'name', 'Role Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000007, 'name', '角色管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000007, 'name', '角色管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000008, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000008, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000008, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000009, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000009, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000009, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000010, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000010, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000010, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000011, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000011, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000011, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000012, 'name', 'Department Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000012, 'name', '部门管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000012, 'name', '部門管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000014, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000014, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000014, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000015, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000015, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000015, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000016, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000016, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000016, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000017, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000017, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000017, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000018, 'name', 'Process Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000018, 'name', '流程管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000018, 'name', '流程管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000019, 'name', 'Model Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000019, 'name', '模型管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000019, 'name', '模型管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000020, 'name', 'Running Process', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000020, 'name', '运行中的流程', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000020, 'name', '運行中的流程', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000021, 'name', 'SMS Service', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000021, 'name', '短信服务', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000021, 'name', '短信服務', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000022, 'name', 'Mail Template', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000022, 'name', '邮件模板', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000022, 'name', '郵件模板', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000023, 'name', 'Mail Log', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000023, 'name', '邮件发送记录', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000023, 'name', '郵件發送記錄', 'zh-TW ');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000024, 'name', 'Message Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000024, 'name', '消息管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000024, 'name', '消息管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000025, 'name', 'Menu Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000025, 'name', '菜单管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000025, 'name', '菜單管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000026, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000026, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000026, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000027, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000027, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000027, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000028, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000028, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000028, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000029, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000029, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000029, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000030, 'name', 'Timed Task', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000030, 'name', '定时任务', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000030, 'name', '定時任務', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000031, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000031, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000031, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000032, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000032, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000032, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000033, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000033, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000033, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000034, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000034, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000034, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000035, 'name', 'Setting', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000035, 'name', '系统设置', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000035, 'name', '系統設置', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000036, 'name', 'Pause', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000036, 'name', '暂停', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000036, 'name', '暫停', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000037, 'name', 'Resume', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000037, 'name', '恢复', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000037, 'name', '恢復', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000038, 'name', 'Execute', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000038, 'name', '立即执行', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000038, 'name', '立即執行', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000039, 'name', 'Log List', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000039, 'name', '日志列表', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000039, 'name', '日誌列表', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000040, 'name', 'Parameter Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000040, 'name', '参数管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000040, 'name', '參數管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000041, 'name', 'Dict Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000041, 'name', '字典管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000041, 'name', '字典管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000042, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000042, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000042, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000043, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000043, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000043, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000044, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000044, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000044, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000045, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000045, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000045, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000046, 'name', 'Log Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000046, 'name', '日志管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000046, 'name', '日誌管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000047, 'name', 'File Upload', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000047, 'name', '文件上传', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000047, 'name', '文件上傳', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000048, 'name', 'Login Log', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000048, 'name', '登录日志', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000048, 'name', '登錄日誌', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000049, 'name', 'Operation Log', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000049, 'name', '操作日志', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000049, 'name', '操作日誌', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000050, 'name', 'Error Log', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000050, 'name', '异常日志', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000050, 'name', '異常日誌', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000051, 'name', 'SQL Monitoring', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000051, 'name', 'SQL监控', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000051, 'name', 'SQL監控', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000052, 'name', 'News Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000052, 'name', '新闻管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000052, 'name', '新聞管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000053, 'name', 'System Monitoring', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000053, 'name', '系统监控', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000053, 'name', '系統監控', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000054, 'name', 'Demo', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000054, 'name', '功能示例', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000054, 'name', '功能示例', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000055, 'name', 'User Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000055, 'name', '用户管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000055, 'name', '用戶管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000056, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000056, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000056, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000057, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000057, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000057, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000058, 'name', 'Export', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000058, 'name', '导出', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000058, 'name', '導出', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000059, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000059, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000059, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000060, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000060, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000060, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000061, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000061, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000061, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150940491508928513, 'name', 'Process Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150940491508928513, 'name', '流程管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150940491508928513, 'name', '流程管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941310262235138, 'name', 'Personal Office', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941310262235138, 'name', '办公管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941310262235138, 'name', '辦公管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941384811794433, 'name', 'Todo', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941384811794433, 'name', '待办任务', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941384811794433, 'name', '待辦任務', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941447038488577, 'name', 'Task Already', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941447038488577, 'name', '已办任务', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941447038488577, 'name', '已辦任務', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941506626965506, 'name', 'Initiation Process', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941506626965506, 'name', '发起流程', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941506626965506, 'name', '發起流程', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941588046794754, 'name', 'My Send', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941588046794754, 'name', '我发起的', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1150941588046794754, 'name', '我發起的', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1156748733921165314, 'name', 'Interface Document', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1156748733921165314, 'name', '接口文档', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1156748733921165314, 'name', '接口文檔', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1113375699198033921, 'name', 'Tenant Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1113375699198033921, 'name', '租户管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1113375699198033921, 'name', '租戶管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1113378406403162113, 'name', 'Tenant Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1113378406403162113, 'name', '租户管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1113378406403162113, 'name', '租戶管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1114779542196600833, 'name', 'Tenant Role', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1114779542196600833, 'name', '租户角色', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1114779542196600833, 'name', '租戶角色', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842115, 'name', 'Administrative Regions', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842115, 'name', '行政区域', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842115, 'name', '行政區域', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842116, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842116, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842116, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842117, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842117, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842117, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842118, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842118, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842118, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842119, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842119, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1158267114314842119, 'name', '刪除', 'zh-TW');

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501825, 'name', 'Master And Child', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501825, 'name', '主子表演示', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501825, 'name', '主子表演示', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501826, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501826, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501826, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501827, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501827, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501827, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501828, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501828, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501828, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501829, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501829, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501829, 'name', '刪除', 'zh-TW');

INSERT INTO sys_dept(id, pid, pids, name, sort, tenant_code, creator, create_date, updater, update_date) VALUES (1067246875800000062, 1067246875800000063, '1067246875800000066,1067246875800000063', '技术部', 2, 1001, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dept(id, pid, pids, name, sort, tenant_code, creator, create_date, updater, update_date) VALUES (1067246875800000063, 1067246875800000066, '1067246875800000066', '长沙分公司', 1, 1001, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dept(id, pid, pids, name, sort, tenant_code, creator, create_date, updater, update_date) VALUES (1067246875800000064, 1067246875800000066, '1067246875800000066', '上海分公司', 0, 1001, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dept(id, pid, pids, name, sort, tenant_code, creator, create_date, updater, update_date) VALUES (1067246875800000065, 1067246875800000064, '1067246875800000066,1067246875800000064', '市场部', 0, 1001, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dept(id, pid, pids, name, sort, tenant_code, creator, create_date, updater, update_date) VALUES (1067246875800000066, 0, '0', '人人开源集团', 0, 1001, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dept(id, pid, pids, name, sort, tenant_code, creator, create_date, updater, update_date) VALUES (1067246875800000067, 1067246875800000064, '1067246875800000066,1067246875800000064', '销售部', 0, 1001, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dept(id, pid, pids, name, sort, tenant_code, creator, create_date, updater, update_date) VALUES (1067246875800000068, 1067246875800000063, '1067246875800000066,1067246875800000063', '产品部', 1, 1001, 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO sys_role(id, name, remark, dept_id, tenant_code, creator, create_date, updater, update_date) VALUES (1125415693534105602, '默认租户角色', '', NULL, NULL, 1067246875800000001, getdate(), NULL, NULL);
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693672517633, 1125415693534105602, 1067246875800000002, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693680906241, 1125415693534105602, 1067246875800000055, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693689294849, 1125415693534105602, 1067246875800000056, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693697683457, 1125415693534105602, 1067246875800000003, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693714460674, 1125415693534105602, 1067246875800000004, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693748015105, 1125415693534105602, 1067246875800000005, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693764792322, 1125415693534105602, 1067246875800000006, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693773180930, 1125415693534105602, 1067246875800000012, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693773180931, 1125415693534105602, 1067246875800000014, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693789958145, 1125415693534105602, 1067246875800000015, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693789958146, 1125415693534105602, 1067246875800000016, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693798346753, 1125415693534105602, 1067246875800000017, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693806735362, 1125415693534105602, 1067246875800000007, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693815123970, 1125415693534105602, 1067246875800000008, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693823512577, 1125415693534105602, 1067246875800000009, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693831901186, 1125415693534105602, 1067246875800000010, 1067246875800000001, getdate());
INSERT INTO sys_role_menu(id, role_id, menu_id, creator, create_date) VALUES (1125415693848678401, 1125415693534105602, 1067246875800000011, 1067246875800000001, getdate());


INSERT INTO sys_dict_type(id, dict_type, dict_name, remark, sort, creator, create_date, updater, update_date) VALUES (1160061077912858625, 'gender', '性别', '', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061112075464705, 1160061077912858625, '男', '0', '', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061146967879681, 1160061077912858625, '女', '1', '', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061190127267841, 1160061077912858625, '保密', '2', '', 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO sys_params(id, param_code, param_value, param_type, remark, creator, create_date, updater, update_date) VALUES (1067246875800000073, 'CLOUD_STORAGE_CONFIG_KEY', '{"type":1,"qiniuDomain":"http://test.oss.renren.io","qiniuPrefix":"upload","qiniuAccessKey":"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ","qiniuSecretKey":"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV","qiniuBucketName":"renren-oss","aliyunDomain":"","aliyunPrefix":"","aliyunEndPoint":"","aliyunAccessKeyId":"","aliyunAccessKeySecret":"","aliyunBucketName":"","qcloudDomain":"","qcloudPrefix":"","qcloudSecretId":"","qcloudSecretKey":"","qcloudBucketName":""}', '0', '云存储配置信息', 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_params(id, param_code, param_value, param_type, remark, creator, create_date, updater, update_date) VALUES (1067246875800000075, 'MAIL_CONFIG_KEY', '{"smtp":"smtp.163.com","port":25,"username":"renrenio_test@163.com","password":"renren123456"}', 0, '邮件配置信息', 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO schedule_job (id, bean_name, params, cron_expression, status, remark, creator, create_date, updater, update_date) VALUES (1067246875800000076, 'testTask', 'renren', '0 0/30 * * * ?', 0, '有参测试，多个参数使用json', 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO sys_mail_template(id, name, subject, content, create_date) VALUES (1067246875800000077, '验证码模板', '人人开源注册验证码', '<p>人人开源注册验证码：${code}</p>', getdate());

INSERT INTO sys_user(id, username, password, real_name, head_url, gender, email, mobile, dept_id, super_admin, super_tenant, status, tenant_code, creator, create_date, updater, update_date) VALUES (1161936575773290497, 'test02', '$2a$10$0HZpiqYWyPXRE/qEZ.UbxuGXOaI4lvA9mfq84DYX/PwNI6AAngs/a', 'test02', NULL, 2, 'test02@163.com', '13900000001', NULL, 0, 1, 1, 1002, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_role_user(id, role_id, user_id, creator, create_date) VALUES (1161936575941062658, 1125415693534105602, 1161936575773290497, 1067246875800000001, getdate());


INSERT INTO tb_order(id, order_id, product_id, product_name, pay_amount, status, user_id, pay_at, create_date) VALUES (1343491774781419523, 1343491774781419523, 1, '人人企业版', 3600.00, 1, 1067246875800000001, getdate(), getdate());
INSERT INTO tb_order(id, order_id, product_id, product_name, pay_amount, status, user_id, pay_at, create_date) VALUES (1343491827268939779, 1343491827268939778, 2, '人人微服务版', 4800.00, 0, 1067246875800000001, NULL, getdate());
INSERT INTO tb_alipay_notify_log(id, out_trade_no, total_amount, buyer_pay_amount, receipt_amount, invoice_amount, notify_id, buyer_id, seller_id, trade_no, trade_status, create_date) VALUES (1343493644518195201, 1343491774781419523, 3600.00, 3600.00, 3600.00, 3600.00, '2020122800222174658006930510128003', '2088102177806934', '2088102177441441', '2020122822001406930501194003', 'TRADE_SUCCESS', getdate());


INSERT INTO sys_dict_type(id, dict_type, dict_name, remark, sort, creator, create_date, updater, update_date) VALUES (1341593474355838978, 'post_status', '岗位管理状态', '', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_type(id, dict_type, dict_name, remark, sort, creator, create_date, updater, update_date) VALUES (1343069688596295682, 'order_status', '订单状态', '', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1341593562419445762, 1341593474355838978, '停用', '0', '', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1341593595407646722, 1341593474355838978, '正常', '1', '', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1343069765549191170, 1343069688596295682, '已取消', '-1', '', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1343069839847092226, 1343069688596295682, '等待付款', '0', '', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1343069914518286337, 1343069688596295682, '已完成', '1', '', 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO sys_post(id, post_code, post_name, sort, status, tenant_code, creator, create_date, updater, update_date) VALUES (1341597192832811009, 'tech', '技术岗', 0, 1, 1001, 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO tb_excel_data(id, real_name, [identity], address, join_date, class_name, creator, create_date) VALUES (1343762012112445441, '大力', '430212199910102980', '上海市长宁区中山公园', getdate(), '姚班2101', 1067246875800000001, getdate());

INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1341596622612987906, 1067246875800000002, 'sys/post', NULL, 0, 0, 'icon-pic-left', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1341596622688485377, 1341596622612987906, NULL, 'sys:post:page,sys:post:info', 1, 0, NULL, 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1341596622755594242, 1341596622612987906, NULL, 'sys:post:save', 1, 0, NULL, 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1341596622835286018, 1341596622612987906, NULL, 'sys:post:update', 1, 0, NULL, 2, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1341596622902394881, 1341596622612987906, NULL, 'sys:post:delete', 1, 0, NULL, 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1341659662599495681, 1067246875800000053, 'sys/online', 'sys:online:all', 0, 0, 'icon-team', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1341676084016852994, 1067246875800000054, 'demo/excel', 'demo:excel:all', 0, 0, 'icon-table', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1343074487777677313, 0, '', '', 0, 0, 'icon-Dollar', 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1343074685589442561, 1343074487777677313, 'pay/order', 'pay:order:all', 0, 0, 'icon-unorderedlist', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1343074794440019970, 1343074487777677313, 'pay/alipaynotifylog', 'pay:alipayNotifyLog:all', 0, 0, 'icon-filedone', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO sys_menu(id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1270380959719501800, 1067246875800000054, 'demo/charts', NULL, 0, 0, 'icon-tag', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501800, 'name', 'ECharts', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501800, 'name', 'ECharts', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1270380959719501800, 'name', 'ECharts', 'zh-TW');

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341659662599495681, 'name', 'Online User', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341659662599495681, 'name', '在线用户', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341659662599495681, 'name', '在線用戶', 'zh-TW');

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341676084016852994, 'name', 'Excel Demo', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341676084016852994, 'name', 'Excel导入演示', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341676084016852994, 'name', 'Excel導入演示', 'zh-TW');

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074487777677313, 'name', 'Pay Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074487777677313, 'name', '支付管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074487777677313, 'name', '支付管理', 'zh-TW');

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074685589442561, 'name', 'Order Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074685589442561, 'name', '订单管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074685589442561, 'name', '訂單管理', 'zh-TW');

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074794440019970, 'name', 'AliPay Log', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074794440019970, 'name', '支付宝回调日志', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074794440019970, 'name', '支付寶回調日誌', 'zh-TW');

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622612987906, 'name', 'Job Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622612987906, 'name', '岗位管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622612987906, 'name', '崗位管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622688485377, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622688485377, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622688485377, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622755594242, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622755594242, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622755594242, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622835286018, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622835286018, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622835286018, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622902394881, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622902394881, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1341596622902394881, 'name', '刪除', 'zh-TW');


INSERT INTO sys_menu (id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1419551957005963266, 0, '', '', 0, 0, 'icon-wechat-fill', 3, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu (id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1419553543706972161, 1419551957005963266, 'mp/account', 'mp:account:all', 0, 0, 'icon-user', 0, 1067246875800000001, getdate(), 1067246875800000001, getdate());
INSERT INTO sys_menu (id, pid, url, permissions, type, open_style, icon, sort, creator, create_date, updater, update_date) VALUES (1419963799817691137, 1419551957005963266, 'mp/menu', 'mp:menu:all', 0, 0, 'icon-unorderedlist', 1, 1067246875800000001, getdate(), 1067246875800000001, getdate());

INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419551957005963266, 'name', 'Wechat Management', 'en-US');
INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419551957005963266, 'name', '微信管理', 'zh-CN');
INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419551957005963266, 'name', '微信管理', 'zh-TW');

INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419553543706972161, 'name', 'Mp Management', 'en-US');
INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419553543706972161, 'name', '公众号管理', 'zh-CN');
INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419553543706972161, 'name', '公眾號管理', 'zh-TW');

INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419963799817691137, 'name', 'Custom Menu', 'en-US');
INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419963799817691137, 'name', '自定义菜单', 'zh-CN');
INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419963799817691137, 'name', '自定義選單', 'zh-TW');


--  quartz自带表结构
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
  ALTER TABLE [dbo].[QRTZ_TRIGGERS] DROP CONSTRAINT FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
  ALTER TABLE [dbo].[QRTZ_CRON_TRIGGERS] DROP CONSTRAINT FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
  ALTER TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS] DROP CONSTRAINT FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[FK_QRTZ_SIMPROP_TRIGGERS_QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
  ALTER TABLE [dbo].[QRTZ_SIMPROP_TRIGGERS] DROP CONSTRAINT FK_QRTZ_SIMPROP_TRIGGERS_QRTZ_TRIGGERS
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_CALENDARS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
  DROP TABLE [dbo].[QRTZ_CALENDARS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_CRON_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
  DROP TABLE [dbo].[QRTZ_CRON_TRIGGERS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_BLOB_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
  DROP TABLE [dbo].[QRTZ_BLOB_TRIGGERS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_FIRED_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
  DROP TABLE [dbo].[QRTZ_FIRED_TRIGGERS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_PAUSED_TRIGGER_GRPS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
  DROP TABLE [dbo].[QRTZ_PAUSED_TRIGGER_GRPS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_SCHEDULER_STATE]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
  DROP TABLE [dbo].[QRTZ_SCHEDULER_STATE]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_LOCKS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
  DROP TABLE [dbo].[QRTZ_LOCKS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_JOB_DETAILS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
  DROP TABLE [dbo].[QRTZ_JOB_DETAILS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_SIMPLE_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
  DROP TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_SIMPROP_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
  DROP TABLE [dbo].[QRTZ_SIMPROP_TRIGGERS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
  DROP TABLE [dbo].[QRTZ_TRIGGERS]
GO

CREATE TABLE [dbo].[QRTZ_CALENDARS] (
  [SCHED_NAME] [VARCHAR] (120)  NOT NULL ,
  [CALENDAR_NAME] [VARCHAR] (200)  NOT NULL ,
  [CALENDAR] [IMAGE] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_CRON_TRIGGERS] (
  [SCHED_NAME] [VARCHAR] (120)  NOT NULL ,
  [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
  [CRON_EXPRESSION] [VARCHAR] (120)  NOT NULL ,
  [TIME_ZONE_ID] [VARCHAR] (80)
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_FIRED_TRIGGERS] (
  [SCHED_NAME] [VARCHAR] (120)  NOT NULL ,
  [ENTRY_ID] [VARCHAR] (95)  NOT NULL ,
  [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
  [INSTANCE_NAME] [VARCHAR] (200)  NOT NULL ,
  [FIRED_TIME] [BIGINT] NOT NULL ,
  [SCHED_TIME] [BIGINT] NOT NULL ,
  [PRIORITY] [INTEGER] NOT NULL ,
  [STATE] [VARCHAR] (16)  NOT NULL,
  [JOB_NAME] [VARCHAR] (200)  NULL ,
  [JOB_GROUP] [VARCHAR] (200)  NULL ,
  [IS_NONCONCURRENT] [VARCHAR] (1)  NULL ,
  [REQUESTS_RECOVERY] [VARCHAR] (1)  NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_PAUSED_TRIGGER_GRPS] (
  [SCHED_NAME] [VARCHAR] (120)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_SCHEDULER_STATE] (
  [SCHED_NAME] [VARCHAR] (120)  NOT NULL ,
  [INSTANCE_NAME] [VARCHAR] (200)  NOT NULL ,
  [LAST_CHECKIN_TIME] [BIGINT] NOT NULL ,
  [CHECKIN_INTERVAL] [BIGINT] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_LOCKS] (
  [SCHED_NAME] [VARCHAR] (120)  NOT NULL ,
  [LOCK_NAME] [VARCHAR] (40)  NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_JOB_DETAILS] (
  [SCHED_NAME] [VARCHAR] (120)  NOT NULL ,
  [JOB_NAME] [VARCHAR] (200)  NOT NULL ,
  [JOB_GROUP] [VARCHAR] (200)  NOT NULL ,
  [DESCRIPTION] [VARCHAR] (250) NULL ,
  [JOB_CLASS_NAME] [VARCHAR] (250)  NOT NULL ,
  [IS_DURABLE] [VARCHAR] (1)  NOT NULL ,
  [IS_NONCONCURRENT] [VARCHAR] (1)  NOT NULL ,
  [IS_UPDATE_DATA] [VARCHAR] (1)  NOT NULL ,
  [REQUESTS_RECOVERY] [VARCHAR] (1)  NOT NULL ,
  [JOB_DATA] [IMAGE] NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS] (
  [SCHED_NAME] [VARCHAR] (120)  NOT NULL ,
  [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
  [REPEAT_COUNT] [BIGINT] NOT NULL ,
  [REPEAT_INTERVAL] [BIGINT] NOT NULL ,
  [TIMES_TRIGGERED] [BIGINT] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_SIMPROP_TRIGGERS] (
  [SCHED_NAME] [VARCHAR] (120)  NOT NULL ,
  [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
  [STR_PROP_1] [VARCHAR] (512) NULL,
  [STR_PROP_2] [VARCHAR] (512) NULL,
  [STR_PROP_3] [VARCHAR] (512) NULL,
  [INT_PROP_1] [INT] NULL,
  [INT_PROP_2] [INT] NULL,
  [LONG_PROP_1] [BIGINT] NULL,
  [LONG_PROP_2] [BIGINT] NULL,
  [DEC_PROP_1] [NUMERIC] (13,4) NULL,
  [DEC_PROP_2] [NUMERIC] (13,4) NULL,
  [BOOL_PROP_1] [VARCHAR] (1) NULL,
  [BOOL_PROP_2] [VARCHAR] (1) NULL,
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_BLOB_TRIGGERS] (
  [SCHED_NAME] [VARCHAR] (120)  NOT NULL ,
  [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
  [BLOB_DATA] [IMAGE] NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_TRIGGERS] (
  [SCHED_NAME] [VARCHAR] (120)  NOT NULL ,
  [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
  [JOB_NAME] [VARCHAR] (200)  NOT NULL ,
  [JOB_GROUP] [VARCHAR] (200)  NOT NULL ,
  [DESCRIPTION] [VARCHAR] (250) NULL ,
  [NEXT_FIRE_TIME] [BIGINT] NULL ,
  [PREV_FIRE_TIME] [BIGINT] NULL ,
  [PRIORITY] [INTEGER] NULL ,
  [TRIGGER_STATE] [VARCHAR] (16)  NOT NULL ,
  [TRIGGER_TYPE] [VARCHAR] (8)  NOT NULL ,
  [START_TIME] [BIGINT] NOT NULL ,
  [END_TIME] [BIGINT] NULL ,
  [CALENDAR_NAME] [VARCHAR] (200)  NULL ,
  [MISFIRE_INSTR] [SMALLINT] NULL ,
  [JOB_DATA] [IMAGE] NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_CALENDARS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_CALENDARS] PRIMARY KEY  CLUSTERED
    (
      [SCHED_NAME],
      [CALENDAR_NAME]
    )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_CRON_TRIGGERS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_CRON_TRIGGERS] PRIMARY KEY  CLUSTERED
    (
      [SCHED_NAME],
      [TRIGGER_NAME],
      [TRIGGER_GROUP]
    )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_FIRED_TRIGGERS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_FIRED_TRIGGERS] PRIMARY KEY  CLUSTERED
    (
      [SCHED_NAME],
      [ENTRY_ID]
    )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_PAUSED_TRIGGER_GRPS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_PAUSED_TRIGGER_GRPS] PRIMARY KEY  CLUSTERED
    (
      [SCHED_NAME],
      [TRIGGER_GROUP]
    )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_SCHEDULER_STATE] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_SCHEDULER_STATE] PRIMARY KEY  CLUSTERED
    (
      [SCHED_NAME],
      [INSTANCE_NAME]
    )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_LOCKS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_LOCKS] PRIMARY KEY  CLUSTERED
    (
      [SCHED_NAME],
      [LOCK_NAME]
    )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_JOB_DETAILS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_JOB_DETAILS] PRIMARY KEY  CLUSTERED
    (
      [SCHED_NAME],
      [JOB_NAME],
      [JOB_GROUP]
    )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_SIMPLE_TRIGGERS] PRIMARY KEY  CLUSTERED
    (
      [SCHED_NAME],
      [TRIGGER_NAME],
      [TRIGGER_GROUP]
    )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_SIMPROP_TRIGGERS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_SIMPROP_TRIGGERS] PRIMARY KEY  CLUSTERED
    (
      [SCHED_NAME],
      [TRIGGER_NAME],
      [TRIGGER_GROUP]
    )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_TRIGGERS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_TRIGGERS] PRIMARY KEY  CLUSTERED
    (
      [SCHED_NAME],
      [TRIGGER_NAME],
      [TRIGGER_GROUP]
    )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_CRON_TRIGGERS] ADD
  CONSTRAINT [FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS] FOREIGN KEY
    (
      [SCHED_NAME],
      [TRIGGER_NAME],
      [TRIGGER_GROUP]
    ) REFERENCES [dbo].[QRTZ_TRIGGERS] (
    [SCHED_NAME],
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  ) ON DELETE CASCADE
GO

ALTER TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS] ADD
  CONSTRAINT [FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS] FOREIGN KEY
    (
      [SCHED_NAME],
      [TRIGGER_NAME],
      [TRIGGER_GROUP]
    ) REFERENCES [dbo].[QRTZ_TRIGGERS] (
    [SCHED_NAME],
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  ) ON DELETE CASCADE
GO

ALTER TABLE [dbo].[QRTZ_SIMPROP_TRIGGERS] ADD
  CONSTRAINT [FK_QRTZ_SIMPROP_TRIGGERS_QRTZ_TRIGGERS] FOREIGN KEY
    (
      [SCHED_NAME],
      [TRIGGER_NAME],
      [TRIGGER_GROUP]
    ) REFERENCES [dbo].[QRTZ_TRIGGERS] (
    [SCHED_NAME],
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  ) ON DELETE CASCADE
GO

ALTER TABLE [dbo].[QRTZ_TRIGGERS] ADD
  CONSTRAINT [FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS] FOREIGN KEY
    (
      [SCHED_NAME],
      [JOB_NAME],
      [JOB_GROUP]
    ) REFERENCES [dbo].[QRTZ_JOB_DETAILS] (
    [SCHED_NAME],
    [JOB_NAME],
    [JOB_GROUP]
  )
GO