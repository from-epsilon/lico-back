SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';
SET default_table_access_method = heap;

CREATE SEQUENCE public.chat_message_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.chat_messages (
    snapshot_version integer NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    id bigint NOT NULL,
    plan_id bigint NOT NULL,
    snapshot_id bigint NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    content text NOT NULL,
    type character varying(255) NOT NULL,
    CONSTRAINT chat_messages_type_check CHECK (((type)::text = ANY ((ARRAY['NAGGING'::character varying, 'PLAN_HISTORY'::character varying, 'USER_REPLY'::character varying])::text[])))
);

CREATE SEQUENCE public.plan_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.plan_snapshot_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.plan_snapshots (
    version integer NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    dtstart timestamp(6) with time zone NOT NULL,
    id bigint NOT NULL,
    plan_id bigint NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    action character varying(255) NOT NULL,
    memo text,
    motive text,
    purpose character varying(255),
    rrule character varying(255) NOT NULL
);

CREATE TABLE public.plans (
    current_snapshot_version integer NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    current_snapshot_id bigint,
    deleted_at timestamp(6) with time zone,
    id bigint NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    user_id uuid NOT NULL
);

CREATE TABLE public.user_social_accounts (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    email_at_provider character varying(255),
    provider character varying(255) NOT NULL,
    provider_user_id character varying(255) NOT NULL,
    CONSTRAINT user_social_accounts_provider_check CHECK (((provider)::text = ANY ((ARRAY['GOOGLE'::character varying, 'APPLE'::character varying])::text[])))
);

CREATE TABLE public.users (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    last_login_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    email character varying(255),
    nickname character varying(255),
    role character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['USER'::character varying, 'ADMIN'::character varying, 'GUEST'::character varying])::text[]))),
    CONSTRAINT users_status_check CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'SUSPENDED'::character varying, 'DELETED'::character varying])::text[])))
);

ALTER TABLE ONLY public.chat_messages
    ADD CONSTRAINT chat_messages_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.plan_snapshots
    ADD CONSTRAINT plan_snapshots_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.plans
    ADD CONSTRAINT plans_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.plan_snapshots
    ADD CONSTRAINT uk_plan_snapshots_plan_id_version UNIQUE (plan_id, version);

ALTER TABLE ONLY public.user_social_accounts
    ADD CONSTRAINT uk_social_provider_userid UNIQUE (provider, provider_user_id);

ALTER TABLE ONLY public.user_social_accounts
    ADD CONSTRAINT uk_social_user_provider UNIQUE (user_id, provider);

ALTER TABLE ONLY public.user_social_accounts
    ADD CONSTRAINT user_social_accounts_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

CREATE INDEX idx_chat_messages_plan_id_created_at ON public.chat_messages USING btree (plan_id, created_at);

CREATE INDEX idx_plan_snapshots_plan_id_version ON public.plan_snapshots USING btree (plan_id, version);

CREATE INDEX idx_plans_user_id ON public.plans USING btree (user_id);

CREATE INDEX idx_social_user_id ON public.user_social_accounts USING btree (user_id);

ALTER TABLE ONLY public.user_social_accounts
    ADD CONSTRAINT fk_social_user FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.plans
    ADD CONSTRAINT fkbybv5po44ssyv6svnv062dwrf FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.plan_snapshots
    ADD CONSTRAINT fkllix0h9knw9v78mx0gcgegch8 FOREIGN KEY (plan_id) REFERENCES public.plans(id);
