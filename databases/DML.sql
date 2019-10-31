SET @user_1 = UUID();
SET @user_2 = UUID();
SET @user_3 = UUID();

INSERT INTO users(id, name) VALUES
     (@user_1, 'alice')
    ,(@user_2, 'bob')
    ,(@user_3, 'charlie');
