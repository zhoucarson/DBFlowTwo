#DBFlow事务和更新#

1. 同步事务
2. 异步事务
3. 更新

开始之前先来了解`DatabaseWrapper`这个类，如图所示，可以看出它是一个接口，里面已经封装好了事务操作和增删改查。
![](http://i.imgur.com/d64bSME.png)
###1.同步事务（Synchronous Transactions）###
同步事务非常简单，只需获得`DatabaseDefinition`就可以去执行。有多个数据时，可以在`execute`中循环执行，官方建议不要放在UI线程

    FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction() {
    	@Override
    	public void execute(DatabaseWrapper databaseWrapper) {
   	 		User user = new User();
    		user.setName("张飞");
   			user.setAge(18);
   	 		user.save(databaseWrapper);
   	 	}
    });

###2.异步事务（Async Transactions）###

####2.1异步事务（Transaction）###
异步事务中，可以添加成功和失败的回调，已监听是否执行结果


    DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
    Transaction transaction = database.beginTransactionAsync(new ITransaction() {
    	@Override
    	public void execute(DatabaseWrapper databaseWrapper) {
    		User user = new User();
    		user.setName("张飞");
    		user.setAge(18);
    		user.save(databaseWrapper);
    	}
    }).success(new OnSuccess()).error(new OnError()).build();
    transaction.execute();

    class OnSuccess implements Transaction.Success {
        @Override
        public void onSuccess(Transaction transaction) {
            Log.d(DB, "成功");
        }
    }

    class OnError implements Transaction.Error {
        @Override
        public void onError(Transaction transaction, Throwable error) {
            Log.d(DB, "失败");
        }
    }

####2.2快速存储(FastStoreModelTransaction)###

如果数据是一个list集合或对象数组，可以使用FastStoreModelTransaction来保存。出了`insertBuilder`还支持`saveBuilder`，`updateBuilder`

            for (int i = 0; i < 100; i++) {
                User user = new User();
                user.setName("张飞");
                user.setAge(18);
                users.add(user);
            }
            DatabaseWrapper databaseWrapper = FlowManager.getDatabase(AppDatabase.class).getWritableDatabase();
            FastStoreModelTransaction.insertBuilder(FlowManager.getModelAdapter(User.class)).addAll(users).build().execute(databaseWrapper);


####2.3(ProcessModelTransaction)###
和`FastStoreModelTransaction`差不多，都继承了`ITransaction`
官方教程：[https://github.com/Raizlabs/DBFlow/blob/master/usage2/StoringData.md](https://github.com/Raizlabs/DBFlow/blob/master/usage2/StoringData.md)

###3.更新###
####3.1版本更新###
只需在`AppDatabase`中更改`VERSION`就可以了
####3.2表结构更新###
如果表增加了一列，就要继承`AlterTableMigration`在`onPreMigrate`方法中更改
如果表版本没有更改，`onPreMigrate`是不会执行的，一定要记住。两个参数分别是数据类型和表名，数据类型请查看`SQLiteType`类。ps:把版本号改一下，注释去掉就能更新数据库


    @Migration(version = AppDatabase.VERSION, database = AppDatabase.class)
    public class MigrationUp extends AlterTableMigration<User> {
    
    	public MigrationUp(Class<User> table) {
    		super(table);
    	}
    
    	@Override
    	public void onPreMigrate() {
    		super.onPreMigrate();
    		addColumn(SQLiteType.get(Long.class.getName()), User_Table.phone.getNameAlias().name());
    	}
    }
