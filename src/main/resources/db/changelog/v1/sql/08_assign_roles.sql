DO $$
    DECLARE
        admin_id INTEGER;
        role_id INTEGER;

    BEGIN
        SELECT id INTO admin_id
        FROM users
        WHERE login = 'Hiricus';

        RAISE NOTICE 'Id администратора: %', admin_id;

        SELECT id INTO role_id
        FROM roles
        WHERE role_name = 'ROLE_ADMIN';

        RAISE NOTICE 'Id нужной роли: %', role_id;

        INSERT INTO user_role_relation (user_id, role_id, assigned_time)
        VALUES (admin_id, role_id, now());
    END;
$$ LANGUAGE plpgsql;