package com.zhoubigbo.dbflowtwo;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.zhoubigbo.dbflowtwo.model.User;
import com.zhoubigbo.dbflowtwo.model.User_Table;

@Migration(version = AppDatabase.VERSION, database = AppDatabase.class)
public class MigrationUp extends AlterTableMigration<User> {

    public MigrationUp(Class<User> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();

      //  addColumn(SQLiteType.get(Long.class.getName()), User_Table.phone.getNameAlias().name());
    }
}
