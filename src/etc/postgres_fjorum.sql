--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.1
-- Dumped by pg_dump version 9.5.0

-- Started on 2016-02-14 21:53:07

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 184 (class 3079 OID 11855)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2071 (class 0 OID 0)
-- Dependencies: 184
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 173 (class 1259 OID 24755)
-- Name: category; Type: TABLE; Schema: public; Owner: fjorum
--

CREATE TABLE category (
    id bigint NOT NULL,
    cat_name character varying(255),
    order_id integer,
    parent_id bigint
);


ALTER TABLE category OWNER TO fjorum;

--
-- TOC entry 172 (class 1259 OID 24753)
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: fjorum
--

CREATE SEQUENCE category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE category_id_seq OWNER TO fjorum;

--
-- TOC entry 2072 (class 0 OID 0)
-- Dependencies: 172
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fjorum
--

ALTER SEQUENCE category_id_seq OWNED BY category.id;


--
-- TOC entry 174 (class 1259 OID 24761)
-- Name: included_roles; Type: TABLE; Schema: public; Owner: fjorum
--

CREATE TABLE included_roles (
    role_id bigint NOT NULL,
    included_role_id bigint NOT NULL
);


ALTER TABLE included_roles OWNER TO fjorum;

--
-- TOC entry 176 (class 1259 OID 24768)
-- Name: reply; Type: TABLE; Schema: public; Owner: fjorum
--

CREATE TABLE reply (
    id bigint NOT NULL,
    reply_content character varying(255),
    created timestamp without time zone,
    modified timestamp without time zone,
    topic_id bigint,
    user_id bigint
);


ALTER TABLE reply OWNER TO fjorum;

--
-- TOC entry 175 (class 1259 OID 24766)
-- Name: reply_id_seq; Type: SEQUENCE; Schema: public; Owner: fjorum
--

CREATE SEQUENCE reply_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reply_id_seq OWNER TO fjorum;

--
-- TOC entry 2073 (class 0 OID 0)
-- Dependencies: 175
-- Name: reply_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fjorum
--

ALTER SEQUENCE reply_id_seq OWNED BY reply.id;


--
-- TOC entry 178 (class 1259 OID 24776)
-- Name: roles; Type: TABLE; Schema: public; Owner: fjorum
--

CREATE TABLE roles (
    id bigint NOT NULL,
    description character varying(255),
    name character varying(255) NOT NULL,
    predefined boolean
);


ALTER TABLE roles OWNER TO fjorum;

--
-- TOC entry 177 (class 1259 OID 24774)
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: fjorum
--

CREATE SEQUENCE roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE roles_id_seq OWNER TO fjorum;

--
-- TOC entry 2074 (class 0 OID 0)
-- Dependencies: 177
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fjorum
--

ALTER SEQUENCE roles_id_seq OWNED BY roles.id;


--
-- TOC entry 180 (class 1259 OID 24787)
-- Name: topic; Type: TABLE; Schema: public; Owner: fjorum
--

CREATE TABLE topic (
    id bigint NOT NULL,
    created timestamp without time zone,
    topic_name character varying(255),
    open boolean,
    sticky boolean,
    category_id bigint,
    user_id bigint
);


ALTER TABLE topic OWNER TO fjorum;

--
-- TOC entry 179 (class 1259 OID 24785)
-- Name: topic_id_seq; Type: SEQUENCE; Schema: public; Owner: fjorum
--

CREATE SEQUENCE topic_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE topic_id_seq OWNER TO fjorum;

--
-- TOC entry 2075 (class 0 OID 0)
-- Dependencies: 179
-- Name: topic_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fjorum
--

ALTER SEQUENCE topic_id_seq OWNED BY topic.id;


--
-- TOC entry 181 (class 1259 OID 24793)
-- Name: user_role; Type: TABLE; Schema: public; Owner: fjorum
--

CREATE TABLE user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE user_role OWNER TO fjorum;

--
-- TOC entry 183 (class 1259 OID 24800)
-- Name: users; Type: TABLE; Schema: public; Owner: fjorum
--

CREATE TABLE users (
    id bigint NOT NULL,
    active boolean,
    confirm_code character varying(255),
    email character varying(255),
    user_name character varying(255),
    pwd_hash character varying(255),
    recover_pwd_code character varying(255)
);


ALTER TABLE users OWNER TO fjorum;

--
-- TOC entry 182 (class 1259 OID 24798)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: fjorum
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO fjorum;

--
-- TOC entry 2076 (class 0 OID 0)
-- Dependencies: 182
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: fjorum
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- TOC entry 1915 (class 2604 OID 24758)
-- Name: id; Type: DEFAULT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY category ALTER COLUMN id SET DEFAULT nextval('category_id_seq'::regclass);


--
-- TOC entry 1916 (class 2604 OID 24771)
-- Name: id; Type: DEFAULT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY reply ALTER COLUMN id SET DEFAULT nextval('reply_id_seq'::regclass);


--
-- TOC entry 1917 (class 2604 OID 24779)
-- Name: id; Type: DEFAULT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY roles ALTER COLUMN id SET DEFAULT nextval('roles_id_seq'::regclass);


--
-- TOC entry 1918 (class 2604 OID 24790)
-- Name: id; Type: DEFAULT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY topic ALTER COLUMN id SET DEFAULT nextval('topic_id_seq'::regclass);


--
-- TOC entry 1919 (class 2604 OID 24803)
-- Name: id; Type: DEFAULT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- TOC entry 2053 (class 0 OID 24755)
-- Dependencies: 173
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: fjorum
--

COPY category (id, cat_name, order_id, parent_id) FROM stdin;
1	__ROOT__	0	\N
\.


--
-- TOC entry 2077 (class 0 OID 0)
-- Dependencies: 172
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fjorum
--

SELECT pg_catalog.setval('category_id_seq', 2, true);


--
-- TOC entry 2054 (class 0 OID 24761)
-- Dependencies: 174
-- Data for Name: included_roles; Type: TABLE DATA; Schema: public; Owner: fjorum
--

COPY included_roles (role_id, included_role_id) FROM stdin;
5	1
5	3
6	2
6	5
7	6
\.


--
-- TOC entry 2056 (class 0 OID 24768)
-- Dependencies: 176
-- Data for Name: reply; Type: TABLE DATA; Schema: public; Owner: fjorum
--

COPY reply (id, reply_content, created, modified, topic_id, user_id) FROM stdin;
\.


--
-- TOC entry 2078 (class 0 OID 0)
-- Dependencies: 175
-- Name: reply_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fjorum
--

SELECT pg_catalog.setval('reply_id_seq', 2, true);


--
-- TOC entry 2058 (class 0 OID 24776)
-- Dependencies: 178
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: fjorum
--

COPY roles (id, description, name, predefined) FROM stdin;
1	access.moderation.page	ACCESS_MODERATION_PAGE	t
2	access.administration.page	ACCESS_ADMINISTRATION_PAGE	t
3	administrate.users	ADMINISTRATE_USERS	t
4	role.user	ROLE_USER	t
5	role.moderator	ROLE_MODERATOR	t
6	role.administrator	ROLE_ADMINISTRATOR	t
7	role.owner	ROLE_OWNER	t
8	role.guest	ROLE_GUEST	t
\.


--
-- TOC entry 2079 (class 0 OID 0)
-- Dependencies: 177
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fjorum
--

SELECT pg_catalog.setval('roles_id_seq', 8, true);


--
-- TOC entry 2060 (class 0 OID 24787)
-- Dependencies: 180
-- Data for Name: topic; Type: TABLE DATA; Schema: public; Owner: fjorum
--

COPY topic (id, created, topic_name, open, sticky, category_id, user_id) FROM stdin;
\.


--
-- TOC entry 2080 (class 0 OID 0)
-- Dependencies: 179
-- Name: topic_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fjorum
--

SELECT pg_catalog.setval('topic_id_seq', 1, true);


--
-- TOC entry 2061 (class 0 OID 24793)
-- Dependencies: 181
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: fjorum
--

COPY user_role (user_id, role_id) FROM stdin;
1	7
\.


--
-- TOC entry 2063 (class 0 OID 24800)
-- Dependencies: 183
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: fjorum
--

COPY users (id, active, confirm_code, email, user_name, pwd_hash, recover_pwd_code) FROM stdin;
1	t	\N	admin@foo.bar	admin	$2a$10$dpU75A19kg1/SljP.oIsDe04mh5VR5o5OyQQ6adneX3P8js8bBUCe	\N
\.


--
-- TOC entry 2081 (class 0 OID 0)
-- Dependencies: 182
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fjorum
--

SELECT pg_catalog.setval('users_id_seq', 2, true);


--
-- TOC entry 1921 (class 2606 OID 24760)
-- Name: category_pkey; Type: CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 1923 (class 2606 OID 24765)
-- Name: included_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY included_roles
    ADD CONSTRAINT included_roles_pkey PRIMARY KEY (role_id, included_role_id);


--
-- TOC entry 1925 (class 2606 OID 24773)
-- Name: reply_pkey; Type: CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY reply
    ADD CONSTRAINT reply_pkey PRIMARY KEY (id);


--
-- TOC entry 1927 (class 2606 OID 24784)
-- Name: roles_pkey; Type: CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- TOC entry 1929 (class 2606 OID 24792)
-- Name: topic_pkey; Type: CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY topic
    ADD CONSTRAINT topic_pkey PRIMARY KEY (id);


--
-- TOC entry 1931 (class 2606 OID 24797)
-- Name: user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 1933 (class 2606 OID 24808)
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 1935 (class 2606 OID 24814)
-- Name: fk_1g2nvlkcg3jvjh8r54nw9ns30; Type: FK CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY included_roles
    ADD CONSTRAINT fk_1g2nvlkcg3jvjh8r54nw9ns30 FOREIGN KEY (included_role_id) REFERENCES roles(id);


--
-- TOC entry 1938 (class 2606 OID 24829)
-- Name: fk_27gfp3pljar6cakq1u6f9bpxk; Type: FK CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY reply
    ADD CONSTRAINT fk_27gfp3pljar6cakq1u6f9bpxk FOREIGN KEY (user_id) REFERENCES users(id);


--
-- TOC entry 1934 (class 2606 OID 24809)
-- Name: fk_81thrbnb8c08gua7tvqj7xdqk; Type: FK CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY category
    ADD CONSTRAINT fk_81thrbnb8c08gua7tvqj7xdqk FOREIGN KEY (parent_id) REFERENCES category(id);


--
-- TOC entry 1937 (class 2606 OID 24824)
-- Name: fk_8utcv8vwnym37rctfnlm6t4mw; Type: FK CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY reply
    ADD CONSTRAINT fk_8utcv8vwnym37rctfnlm6t4mw FOREIGN KEY (topic_id) REFERENCES topic(id);


--
-- TOC entry 1942 (class 2606 OID 24849)
-- Name: fk_apcc8lxk2xnug8377fatvbn04; Type: FK CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fk_apcc8lxk2xnug8377fatvbn04 FOREIGN KEY (user_id) REFERENCES users(id);


--
-- TOC entry 1940 (class 2606 OID 24839)
-- Name: fk_c975xs66f0v6extdjdl0csctr; Type: FK CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY topic
    ADD CONSTRAINT fk_c975xs66f0v6extdjdl0csctr FOREIGN KEY (user_id) REFERENCES users(id);


--
-- TOC entry 1936 (class 2606 OID 24819)
-- Name: fk_dfeoejuk4gxqusmlo18c723q5; Type: FK CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY included_roles
    ADD CONSTRAINT fk_dfeoejuk4gxqusmlo18c723q5 FOREIGN KEY (role_id) REFERENCES roles(id);


--
-- TOC entry 1939 (class 2606 OID 24834)
-- Name: fk_hj09a8a5m77ettlp827l9y7qf; Type: FK CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY topic
    ADD CONSTRAINT fk_hj09a8a5m77ettlp827l9y7qf FOREIGN KEY (category_id) REFERENCES category(id);


--
-- TOC entry 1941 (class 2606 OID 24844)
-- Name: fk_it77eq964jhfqtu54081ebtio; Type: FK CONSTRAINT; Schema: public; Owner: fjorum
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fk_it77eq964jhfqtu54081ebtio FOREIGN KEY (role_id) REFERENCES roles(id);


--
-- TOC entry 2070 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-02-14 21:53:07

--
-- PostgreSQL database dump complete
--

