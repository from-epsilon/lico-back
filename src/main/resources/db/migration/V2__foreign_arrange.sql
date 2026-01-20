ALTER TABLE ONLY public.chat_messages
    DROP CONSTRAINT IF EXISTS fk_chat_messages_plan_id;

ALTER TABLE ONLY public.chat_messages
    DROP CONSTRAINT IF EXISTS fk_chat_messages_snapshot_id;

ALTER TABLE ONLY public.chat_messages
    ADD CONSTRAINT fk_chat_messages_plan_id FOREIGN KEY (plan_id) REFERENCES public.plans(id);

ALTER TABLE ONLY public.chat_messages
    ADD CONSTRAINT fk_chat_messages_snapshot_id FOREIGN KEY (snapshot_id) REFERENCES public.plan_snapshots(id);

ALTER TABLE ONLY public.plans
    RENAME CONSTRAINT fkbybv5po44ssyv6svnv062dwrf TO fk_plans_user_id;

ALTER TABLE ONLY public.plan_snapshots
    RENAME CONSTRAINT fkllix0h9knw9v78mx0gcgegch8 TO fk_plan_snapshots_plan_id;

ALTER TABLE ONLY public.user_social_accounts
    RENAME CONSTRAINT fk_social_user TO fk_user_social_accounts_user_id;
