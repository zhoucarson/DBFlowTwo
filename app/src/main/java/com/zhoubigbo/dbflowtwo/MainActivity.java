package com.zhoubigbo.dbflowtwo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.zhoubigbo.dbflowtwo.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Button btnSyn, btnAsync, btnFast;
    private static final String DB = "DBFlow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSyn = (Button) findViewById(R.id.btn_syntr);
        btnAsync = (Button) findViewById(R.id.btn_asyncbtr);
        btnFast = (Button) findViewById(R.id.btn_fast);
        btnSyn.setOnClickListener(new OnSyn());
        btnAsync.setOnClickListener(new OnAsync());
        btnFast.setOnClickListener(new OnFast());
    }

    //同步
    class OnSyn implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction() {
                @Override
                public void execute(DatabaseWrapper databaseWrapper) {
                    System.out.println(databaseWrapper.getVersion());
                    User user = new User();
                    user.setName("张三");
                    user.setAge(18);
                    user.save(databaseWrapper);
                }
            });
        }
    }

    //异步
    class OnAsync implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
            Transaction transaction = database.beginTransactionAsync(new ITransaction() {
                @Override
                public void execute(DatabaseWrapper databaseWrapper) {
                    User user = new User();
                    user.setName("张三");
                    user.setAge(18);
                    user.save(databaseWrapper);
                }
            }).success(new OnSuccess()).error(new OnError()).build();
            transaction.execute();
        }
    }

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

    //集合或对象数组
    class OnFast implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                User user = new User();
                user.setName("张三");
                user.setAge(18);
                users.add(user);
            }
            DatabaseWrapper databaseWrapper = FlowManager.getDatabase(AppDatabase.class).getWritableDatabase();
            FastStoreModelTransaction.insertBuilder(FlowManager.getModelAdapter(User.class)).addAll(users).build().execute(databaseWrapper);
        }
    }
}
