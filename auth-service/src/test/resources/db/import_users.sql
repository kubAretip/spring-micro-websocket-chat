insert into auth_service_database.user (id,
                                        username,
                                        password_hash,
                                        first_name,
                                        last_name,
                                        activation_key,
                                        email,
                                        enabled)
values (replace('f10e11d2-c603-4bc2-984f-44270391620d', '-', ''),
        'fake_user',
        'fake_pass',
        'fake_name',
        'fake_surname',
        'fake_key',
        'fake_mail',
        true);

insert into auth_service_database.user_authority (user_id,
                                                  authority_name)
values (replace('f10e11d2-c603-4bc2-984f-44270391620d', '-', ''),
        'ROLE_USER');